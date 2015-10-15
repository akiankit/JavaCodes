package com.bluestone.app.payment.gateways.atom;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.integration.RestConnection;
import com.bluestone.app.integration.SecureHTTPConnection;
import com.bluestone.app.payment.service.PaymentParametersHandler;
import com.bluestone.app.payment.spi.PaymentUtil.BLUESTONE_FIELDS;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.CaptureTransaction;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentRequest;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentResponse.GATEWAYRESPONSE;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.service.PaymentGatewayService;
import com.google.common.base.Throwables;

@Service
public class AtomPaymentService implements PaymentGatewayService {
	
	private static final Logger log = LoggerFactory.getLogger(AtomPaymentService.class);    

    @Value("${atom.loginId}")
    private String loginId;

    @Value("${atom.password}")
    private String password;

    @Value("${atom.productId}")
    private String productId;
    
    @Value("${atom.gatewayHost}")
    private String gatewayHost;
    
    @Value("${atom.gatewayPath}")
    private String gatewayPath;
    
    @Value("${atom.connectionType}")
    private String connectionType;
    
    @Autowired
    private AtomRequestBuilderService atomRequestBuilderService;        
    
    @Autowired
    private RestConnection httpConnection;
    
    @Autowired
    private SecureHTTPConnection httpsConnection;
    
    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;
    
    @Autowired
    private PaymentParametersHandler paymentParametersHandler;

	@Override
	public PaymentGateway getPaymentGateway() {
		return PaymentGateway.ATOM;
	}

	@Override
	public PaymentRequest createPaymentRequest(PaymentTransaction paymentTransaction, Cart clonedCart, Cart originalCart) throws PaymentException {
		log.debug("AtomPaymentService.createPaymentRequest() for {}", paymentTransaction.toStringBrief());
        try {        	             

            String vXMLStr = "";
            Map<String, Object> requestParameterMap = atomRequestBuilderService.getRequestParameterMap(paymentTransaction, clonedCart, loginId,password,productId);            
			if(connectionType.equals("http")){
            	vXMLStr = httpConnection.executeGetRequest(gatewayHost, gatewayPath, requestParameterMap);
            }else{
            	vXMLStr = httpsConnection.executeGetRequest(gatewayHost, gatewayPath, requestParameterMap);
            }
			
			if(StringUtils.isBlank(vXMLStr)){
				String errorMessage = "AtomPaymentService.createPaymentRequest() First request failed for "+paymentTransaction.toStringBrief();
				log.error("AtomPaymentService.createPaymentRequest() First request failed for {}", paymentTransaction.toStringBrief());
				productionAlert.send(errorMessage);
				PaymentException paymentException = new PaymentException(PaymentError.ATOM_MALFORMED_FIRST_RESPONSE);
				paymentException.setDetailedReason("Atom - First Request Failure for transaction id - "+paymentTransaction.getBluestoneTxnId());
				throw paymentException;
			}
                        
            String secondRequestURL = atomRequestBuilderService.buildSecondRequestURL(vXMLStr);

            PaymentRequest preAuthorizeRequest = new PaymentRequest();
            preAuthorizeRequest.setVmTemplate(true);
            preAuthorizeRequest.setVmTemplatePath("pgforms/atom.vm");
            preAuthorizeRequest.addTemplateVariable("paymentGatewayUrl", secondRequestURL);
            return preAuthorizeRequest;
        } catch (Exception e) {
            log.error("AtomPaymentService - Error while creating payment request for {} ", paymentTransaction, Throwables.getRootCause(e));
            productionAlert.send("AtomPaymentService.createPaymentRequest() Error while creating payment request for {} "+paymentTransaction.toStringBrief(),e);
            throw new PaymentException(PaymentError.PREAUTH_REQUEST_GENERATION_FAILED, e, paymentTransaction.getBluestoneTxnId());
        }
	}		

	@Override
	public PaymentResponse handlePaymentResponse(HttpServletRequest httpServletRequest) throws PaymentException {
		log.debug("AtomPaymentService.handlePaymentResponse(): ");		
		String responseString = paymentParametersHandler.generateDebugMessage(httpServletRequest).toString();
		String encrytedTxnId = "";				
		PaymentResponse paymentResponse = new PaymentResponse();
		try {
			Map<String, Object> responseFields = paymentParametersHandler.buildResponseParameterMap(httpServletRequest);
			
			validateMerchantFields(responseFields, paymentResponse);

			encrytedTxnId = paymentResponse.getMerchantFields().get(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID);
			
			paymentResponse.setResponseAsText(responseString);
			paymentResponse.setGatewayTransactionId((String) responseFields.get("mmp_txn"));
			paymentResponse.setAmount(new BigDecimal((String) responseFields.get("amt")));
			paymentResponse.setBankReferenceNo("bank_txn");

			validateTransactionResponseCode(paymentResponse, (String) responseFields.get("f_code"));			
			
		} catch (Exception e) {
			log.error("Error while executing AtomPaymentService.handlePaymentResponse() EncrytedTxnId={} , Response={} ", encrytedTxnId, responseString, e);			
            PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE, e, encrytedTxnId);
            paymentException.setPaymentResponse(responseString);
            throw paymentException;
		}
		return paymentResponse;
	}
	
	private void validateTransactionResponseCode(PaymentResponse paymentResponse, String txnResponseCode) {		
		if ((!txnResponseCode.equalsIgnoreCase("ok"))) {			
			log.error("AtomPaymentService: Transaction Failure Error - Transaction Response Code ={}", txnResponseCode);
			paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
		}else{
			paymentResponse.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);
		}
	}
	
	private void validateMerchantFields(Map<String, Object> responseFields, PaymentResponse paymentResponse) throws PaymentException {
		String encrytedTxnId = (String) responseFields.get("mer_txn");
		log.debug("AtomPaymentService..validateMerchantFields() - Recived Encryption ID={} ", encrytedTxnId);
		if (StringUtils.isBlank(encrytedTxnId)) {
			paymentResponse.setFailureMessage(PaymentError.MALFORMED_RESPONSE.getErrorMsg());
		    paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);	            
		    throw new PaymentException(PaymentError.MALFORMED_RESPONSE, encrytedTxnId);
		}		
		paymentResponse.addMerchantField(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID, encrytedTxnId);		
	}

	@Override
	public CaptureTransaction capture(PaymentTransaction paymentTransaction) throws PaymentException {
		CaptureTransaction captureTransaction = new CaptureTransaction();
    	captureTransaction.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);
    	return captureTransaction;
	}

	@Override
	public void cancel(PaymentTransaction paymentTransaction) throws PaymentException {
		
	}

	@Override
	public void refund(PaymentTransaction paymentTransaction) throws PaymentException {

	}		

}
