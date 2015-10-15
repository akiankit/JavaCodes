package com.bluestone.app.payment.spi.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.payment.service.PaymentInstrumentService;
import com.bluestone.app.payment.service.PaymentResponseService;
import com.bluestone.app.payment.service.PrePaidPaymentSuccessService;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.CaptureTransaction;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentResponse.GATEWAYRESPONSE;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;
import com.bluestone.app.payment.spi.service.PaymentGatewayFinder;
import com.bluestone.app.payment.spi.service.PaymentGatewayService;

@Component
public abstract class PaymentResponseController {

    protected static final String EVENT_ID_PAYMENTFAILURE = "&_eventId=paymentfailure";

    private static final Logger log = LoggerFactory.getLogger(PaymentResponseController.class);

    @Autowired
    protected PaymentTransactionService paymentTransactionService;

    @Autowired
    protected PaymentInstrumentService paymentInstrumentService;
    
    @Autowired
    protected PaymentResponseService paymentResponseService;
    
    @Autowired
    protected PrePaidPaymentSuccessService paymentSuccessService;

    @Autowired
    protected PaymentGatewayFinder paymentGatewayFinder;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;
    
    public void handlePostAuthorizeResponse(HttpServletRequest httpServletRequest,
                                            HttpServletResponse httpServletResponse,
                                            PaymentGateway paymentGateway) throws PaymentException {
        log.info("PaymentResponseController.handlePostAuthorizeResponse(): PaymentGateway={}", paymentGateway.name());
        String encryptedTxnId = "";
        PaymentTransaction paymentTransaction = null;
        try {
            PaymentGatewayService paymentGatewayService = paymentGatewayFinder.getPaymentGatewayService(paymentGateway);
            PaymentResponse paymentResponse = paymentGatewayService.handlePaymentResponse(httpServletRequest);
            log.info("PaymentResponseController.handlePostAuthorizeResponse(): Handling of response from {} for {} executed without exception.",
                     paymentGateway.name(), paymentResponse.getMerchantFields());            

            // this call will internally take row level lock on transaction record in db and update the status
            paymentTransaction = paymentResponseService.updatePaymentStatusAfterResponse(paymentResponse);
            encryptedTxnId = paymentTransaction.getBluestoneTxnId();


            MessageContext messageContext = Util.getMessageContext();
            messageContext.put(MessageContext.PAYMENT_TRANSACTION_ID, paymentTransaction.getId());                                    

            String path="";
            String flowId = paymentTransaction.getPaymentMetaDataValue(PaymentMetaData.Name.FLOW_EXECUTION_ID);
            
            GATEWAYRESPONSE gatewayResponse = paymentResponse.getGatewayResponse();
			log.info("PaymentResponseController. paymetStatus()=[{}] for {}", gatewayResponse, paymentTransaction);

			
            if (!GATEWAYRESPONSE.SUCCESS.equals(gatewayResponse)) {
            	//Authorization Failure
            	paymentTransaction = paymentTransactionService.updatePaymentTransactionStatus(paymentTransaction, PaymentTransactionStatus.AUTHORIZATION_FAILURE);
            	path = getPaymentFailureUrl(flowId, encryptedTxnId);
            } else {
            	paymentTransaction = paymentTransactionService.updatePaymentTransactionStatus(paymentTransaction, PaymentTransactionStatus.AUTHORIZATION_SUCCESS);
            	//Start Capture
                CaptureTransaction captureTransaction = doCapture(encryptedTxnId, paymentTransaction, paymentGatewayService);
                paymentTransaction.setCaptureTransaction(captureTransaction);        
                paymentTransaction = paymentTransactionService.updatePaymentTransaction(paymentTransaction);
                GATEWAYRESPONSE captureTransactionResponse = captureTransaction.getGatewayResponse();
                
                
				if (!GATEWAYRESPONSE.SUCCESS.equals(captureTransactionResponse)) {
                	//Capture Failure
					paymentTransaction = paymentTransactionService.updatePaymentTransactionStatus(paymentTransaction, PaymentTransactionStatus.FAILURE);
                	path = getPaymentFailureUrl(flowId, encryptedTxnId);
                } else {
    			    //Capture Success and Create Order
                	paymentTransaction = paymentTransactionService.updatePaymentTransactionStatus(paymentTransaction, PaymentTransactionStatus.SUCCESS);
    			    path = paymentSuccessService.handlePrePaidPaymentSuccess(paymentTransaction);
                }
            }
        	httpServletResponse.sendRedirect(path);
            
        } catch (Exception e) {
            String moreDetails = "";
            if (e instanceof PaymentException) {
                moreDetails = ((PaymentException) e).getPaymentResponse();
                if (moreDetails == null) {
                    moreDetails = "null";
                }
            }
            String errorMessage = "Payment status might be successful for transaction id " + encryptedTxnId
                                  + " , but we failed to create the order. Thread Id = " + Thread.currentThread().getId()
                                  + " - PaymentGateway Response=" + moreDetails;

            productionAlert.send(errorMessage, e);
            handleException(e, paymentGateway, encryptedTxnId);
        } finally {
            paymentTransactionService.triggerPaymentResponseEvent(paymentTransaction);
        }
    }    

    private CaptureTransaction doCapture(String encryptedTxnId, PaymentTransaction paymentTransaction, PaymentGatewayService paymentGatewayService) throws PaymentException {
        log.info("PaymentResponseController.doCapture(): encryptedTxnId=[{}]", encryptedTxnId, paymentTransaction.toStringBrief());        
        CaptureTransaction captureTransaction = paymentGatewayService.capture(paymentTransaction);
        if (!captureTransaction.getGatewayResponse().equals(GATEWAYRESPONSE.SUCCESS)) {
            PaymentException paymentException = new PaymentException(PaymentError.CAPTURE_ERROR, encryptedTxnId);
            paymentException.setPaymentResponse(captureTransaction.getResponseAsText());
            throw paymentException;
        }
        return captureTransaction;
    }

    private void handleException(Exception e, PaymentGateway paymentGateway, String encryptedTxnId) throws PaymentException {
        log.error("Error occurred while executing handlePostAuthorizeResponse for paymentGateway " + paymentGateway.name(), e);
        if (e instanceof IOException) {
            throw new PaymentException(PaymentError.REDIRECT_ERROR, e, encryptedTxnId);
        } else if (e instanceof PaymentException) {
            throw (PaymentException) e;
        } else if (e instanceof Exception) {
            throw new PaymentException(PaymentError.INTERNAL_ERROR, e, encryptedTxnId);
        }
    }

    private String getPaymentFailureUrl(String flowId, String encryptedTxnId) {
        StringBuilder url = new StringBuilder();
        url.append(Util.getSiteUrlWithContextPath());
        url.append("/order?execution=");
        url.append(flowId);
        url.append(EVENT_ID_PAYMENTFAILURE);
        url.append("&txn=");
        url.append(encryptedTxnId);
        String path = url.toString();
        log.info("PaymentResponseController.getFlowUrl() = {}", path);
        return path;
    }

}

