package com.bluestone.app.payment.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.BaseCartAction;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.payment.service.PaymentMetaDataFactory;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;
import com.bluestone.app.payment.spi.model.SubPayment;
import com.google.common.base.Throwables;

@Component
public class PaymentAction extends BaseCartAction {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentAction.class);
    
    public static final String PAYMENT_TRANSACTION = "paymentTransaction";
    
    @Autowired
    protected PaymentTransactionService paymentTransactionService;
    
    @Autowired
    private OrderService        orderService;
    
    @Qualifier("emailAlertService")
    @Autowired
    protected ProductionAlert productionAlert;
    
    public Event createPaymentTransaction(RequestContext requestContext) throws PaymentException {
        String flowId = getFlowIdFromRequestContext(requestContext);

        log.info("PaymentAction.createPaymentTransaction(): Started for flow id={}", flowId);

        try {
            ParameterMap requestParameterMap = requestContext.getExternalContext().getRequestParameterMap();
            String instrumentType = requestParameterMap.get("instrumentType");
            String cardPaymentNetwork = requestParameterMap.get("cardPaymentNetwork");
            String issuingAuthority = requestParameterMap.get("issuingAuthority");
            
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            Cart cart = validateAndGetCart(requestContext);
            
            
            if(!doesCartHaveAnyActiveProducts(requestContext, cart)) {
                return error();
            }
            if(isCartValueLessThenZero(requestContext, cart)) {
                return error();
            }
            
            Long masterPaymentId = (Long) flowScope.get("masterPaymentId");
            PaymentTransaction paymentTransaction = null;
            List<PaymentMetaData> paymentMetaDatas = new ArrayList<PaymentMetaData>();
            PaymentMetaDataFactory.addMetaData(PaymentMetaData.Name.FLOW_EXECUTION_ID, flowId, paymentMetaDatas);
            paymentTransaction = paymentTransactionService.createPaymentTransaction(cart, masterPaymentId, 
                                                                                        instrumentType, cardPaymentNetwork,
                                                                                        issuingAuthority, flowId,paymentMetaDatas);
            
            SubPayment subPayment = paymentTransaction.getSubPayment();
            flowScope.put("masterPaymentId", subPayment.getMasterPayment().getId());            
            addPaymentTranactionToScope(requestContext, paymentTransaction);
            return success();
        } catch (Throwable e) {
            handleException("PaymentAction.createPaymentTransaction():",requestContext, e);
            return error();
        }
    }

    protected void addPaymentTranactionToScope(RequestContext requestContext, PaymentTransaction paymentTransaction) {
        MutableAttributeMap flowScope = requestContext.getFlowScope();
        flowScope.put(PAYMENT_TRANSACTION, paymentTransaction);
    }
    
    protected void removePaymentTransactionFromScope(RequestContext requestContext) {
        log.info("PaymentAction.removePaymentTransactionFromScope()");
        MutableAttributeMap flowScope = requestContext.getFlowScope();
        flowScope.remove(PAYMENT_TRANSACTION);
    }

    public Event handlePostPaidPayment(RequestContext requestContext) throws PaymentException {
        log.info("PaymentAction.handlePostPaidPayment actionName {}, flow id={}", getActionNameForLogging(),
                 getFlowIdFromRequestContext(requestContext));
        try {
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            PaymentTransaction paymentTransaction  = (PaymentTransaction) flowScope.get(PAYMENT_TRANSACTION);
            paymentTransaction.setPaymentTransactionStatus(PaymentTransactionStatus.COD_ON_HOLD);
            Order order = createPostPaidOrder(paymentTransaction);
            requestContext.getFlowScope().put("order", order);
            setIsNewCustomer(requestContext);//For Analytics
            return success();
        } catch (Throwable e) {
            handleException("PaymentAction.handlePostPaidPayment() :",requestContext, e);
            return error();
        }  finally {
            removePaymentTransactionFromScope(requestContext);
        }

    }

    private Order createPostPaidOrder(PaymentTransaction paymentTransaction) {
    	Order order = null;
		try {
			order = orderService.createOrder(paymentTransaction);
		} catch (Throwable throwable) {
			log.error(
					"Error: PaymentService.createPostPaidOrder():PaymentTransactionId={} Post paid Order Creation Failed. : Reason={} ",
					paymentTransaction.getId(), paymentTransaction.getPaymentTransactionStatus().name(), throwable.toString());
			productionAlert.send("Failure to create post paid order. PaymentTransactionId="+ paymentTransaction.getId() + " : "+ paymentTransaction.toStringBrief(), throwable);
			Throwables.propagate(throwable);
		}
		return order;
	}
    

	public Event paymentSuccess(RequestContext requestContext) throws PaymentException {
        log.info("PaymentAction.paymentSuccess actionName {} , flowId={}", getActionNameForLogging(), getFlowIdFromRequestContext(requestContext));
        ParameterMap requestParameterMap = requestContext.getExternalContext().getRequestParameterMap();
        String orderCode = requestParameterMap.get("oid");
        try {            
            if(StringUtils.isBlank(orderCode)){
            	throw new PaymentException(PaymentError.EMPTY_ORDER_ID);
            }
            handlePaymentSuccessPage(orderCode, requestContext);
            setIsNewCustomer(requestContext);//For Analytics    
            return success();
        } catch (Throwable e) {            
        	PaymentTransaction paymentTransactionInFlowScope = (PaymentTransaction) requestContext.getFlowScope().get(PAYMENT_TRANSACTION);
        	log.info("Payment Success Page Error - Failed to get order for orderId {} and transaction id {}", orderCode, paymentTransactionInFlowScope.getBluestoneTxnId());
            String errorMessage = "Failed to get order for orderId = " + orderCode + "and transaction id = "+ paymentTransactionInFlowScope.getBluestoneTxnId()
            		+". Thread Id = " + Thread.currentThread().getId();
            productionAlert.send(errorMessage, e);
            return error();
        } finally {
            removePaymentTransactionFromScope(requestContext);
        }
    }
    
    protected void handlePaymentSuccessPage(String orderCode, RequestContext requestContext) throws PaymentException{
    	log.info("PaymentAction.handlePaymentSuccessPage actionName {} , flowId={}, orderCode={}", getActionNameForLogging(), getFlowIdFromRequestContext(requestContext),orderCode);
    	Order order = orderService.getOrderFromOrderCode(orderCode);
        if(order==null){
        	throw new PaymentException(PaymentError.ORDER_NOT_FOUND);
        }
        requestContext.getFlowScope().put("order", order);        
    }

    protected void setIsNewCustomer(RequestContext requestContext) {
    	Order order = (Order) requestContext.getFlowScope().get("order");
    	Customer customer = null;
   		customer = order.getCart().getCustomer();
    	List<Order> allOrdersForCustomer = orderService.getAllOrdersForCustomer(customer);
    	if(allOrdersForCustomer.size()>1){
    		requestContext.getFlowScope().put("customerStatus", "Existing");
    	}else{
    		requestContext.getFlowScope().put("customerStatus", "New");
    	}
    }
    	
	public Event paymentFailure(RequestContext context) throws PaymentException {
		log.info("PaymentAction.paymentFailure actionName {} ", getActionNameForLogging());
		try {
			PaymentTransaction paymentTransaction = getPaymentTransaction(context);
			PaymentTransactionStatus paymentTransactionStatus = paymentTransaction.getPaymentTransactionStatus();
			if (PaymentTransactionStatus.AUTHORIZATION_FAILURE.equals(paymentTransactionStatus)
                || PaymentTransactionStatus.FAILURE.equals(paymentTransactionStatus)) {

                if(PaymentGateway.AMEX.equals(paymentTransaction.getPaymentGateway())){
					context.getFlowScope().put("failureMessage", Constants.AMEX_ERROR_MSG);
				}else{
					context.getFlowScope().put("failureMessage", paymentTransaction.getPaymentResponse().getFailureMessage());
				}
			} else {
				log.error("PaymentAction.paymentFailure(): Flow id={} : Illegal Payment status for {}", getFlowIdFromRequestContext(context), paymentTransaction);
                PaymentException paymentException = new PaymentException(PaymentError.TRANSACTION_STATE_MISMATCHED, paymentTransaction.getBluestoneTxnId());
                paymentException.setDetailedReason("[Expected Status=" + PaymentTransactionStatus.AUTHORIZATION_FAILURE.name()
                                                   + " or " + paymentTransactionStatus.FAILURE.name()
                                                   + " Actual Status = " + paymentTransaction.getPaymentTransactionStatus().name()
                                                   + " ]");
                throw paymentException;
			}
			removePaymentTransactionFromScope(context);
			return success();
		} catch (Throwable e) {
			handleException("PaymentAction.paymentFailure(): ", context, e);
			return error();
		}
	}
    
    protected PaymentTransaction getPaymentTransaction(RequestContext requestContext) throws PaymentException {
        log.debug("PaymentAction.getPaymentTransaction() from the requestContext for flow Id={}", getFlowIdFromRequestContext(requestContext));
        String encryptedTransactionId = "";
        try {
            ParameterMap requestParameterMap = requestContext.getExternalContext().getRequestParameterMap();
            encryptedTransactionId = requestParameterMap.get("txn");

            String decryptedTxnId = CryptographyUtil.decrypt(encryptedTransactionId);
            long paymentTxnId = Long.parseLong(decryptedTxnId);
            PaymentTransaction paymentTransaction = paymentTransactionService.getPaymentTransaction(paymentTxnId);
            if (paymentTransaction == null) {
                throw new PaymentException(PaymentError.TRANSACTION_NOT_FOUND, encryptedTransactionId);
            }
            return paymentTransaction;
        } catch (PaymentException e) {
            log.error("PaymentAction.getPaymentTransaction(): Error occurred while executing getPaymentTransaction for flow Id={} . Reason={} ",
                      getFlowIdFromRequestContext(requestContext), e.toString(), e);
            throw e;
        } catch (Throwable e) {
            log.error("PaymentAction.getPaymentTransaction(): Error occurred while executing getPaymentTransaction for flow Id={} . Reason={} ",
                      getFlowIdFromRequestContext(requestContext), e.toString(), e);
            throw new PaymentException(PaymentError.BAD_TRANSACTION_ID, e, encryptedTransactionId);
        }
    }
    
    protected void handleException(String errorContext, RequestContext requestContext, Throwable exception) throws PaymentException {
        log.error("PaymentAction.handleException(): Error in payment flow action {} during {} ", getActionNameForLogging(), errorContext, exception);
        String bluestoneTxnId = "";
        MutableAttributeMap flowScope = null;
        try {
            flowScope = requestContext.getFlowScope();
            PaymentTransaction paymentTransaction = (PaymentTransaction) flowScope.get(PAYMENT_TRANSACTION);
            if (paymentTransaction != null) {
                bluestoneTxnId = paymentTransaction.getBluestoneTxnId();
            } else {
                log.info("PaymentAction.handleException(): Payment Transaction is null, so we don't have a bluestoneTxnId. " +
                         "Either error happened before it was generated. Or , if that is not true, then there is some other issue.");
            }
            PaymentException paymentException = new PaymentException(PaymentError.INTERNAL_ERROR, bluestoneTxnId);
            if(exception instanceof PaymentException) {
                paymentException = (PaymentException) exception;
            } else if(exception instanceof Throwable) {
                paymentException = new PaymentException(PaymentError.INTERNAL_ERROR, exception, bluestoneTxnId);
            }
            
            throw paymentException;

        } catch (Throwable th) {
            throw new PaymentException(PaymentError.INTERNAL_ERROR, th, "");
        }
    }
    
}
 