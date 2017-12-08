package com.bluestone.app.design.goldmine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanService;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.payment.action.PaymentAction;
import com.bluestone.app.payment.service.PaymentMetaDataFactory;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.SubPayment;

@Component
public class GoldMinePaymentAction extends PaymentAction {
    
    private static final Logger log = LoggerFactory.getLogger(GoldMinePaymentAction.class);
    
    @Autowired
    private GoldMinePlanService goldMinePlanService;
    
    @Autowired
    private OrderService orderService;
    
    public Event createGoldMinePaymentTransaction(RequestContext requestContext) throws GoldMinePaymentException{
        String flowId = getFlowIdFromRequestContext(requestContext);
        log.info("GoldMinePaymentAction.createGoldMinePaymentTransaction(): Started for flow id={}", flowId);

        try {
            Cart cart = (Cart) requestContext.getFlowScope().get(CART);
            
            ParameterMap requestParameterMap = requestContext.getExternalContext().getRequestParameterMap();
            
            String instrumentType = requestParameterMap.get("instrumentType");
            String cardPaymentNetwork = requestParameterMap.get("cardPaymentNetwork");
            String issuingAuthority = requestParameterMap.get("issuingAuthority");
            
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            
            
            if(!doesCartHaveAnyActiveProducts(requestContext, cart)) {
                return error();
            }
            if(isCartValueLessThenZero(requestContext, cart)) {
                return error();
            }
            
            String goldMineSchemeTerm = (String)flowScope.get("goldMineSchemeTerm");
            String goldMinePaymentid = (String)flowScope.get("paymentId");
            Long masterPaymentId = (Long) flowScope.get("masterPaymentId");
            List<PaymentMetaData> paymentMetaDatas = new ArrayList<PaymentMetaData>();
            
            //TODO: to cleaup meta deta creation
            PaymentMetaDataFactory.addMetaData(PaymentMetaData.Name.FLOW_EXECUTION_ID, flowId, paymentMetaDatas);
        	
            if(StringUtils.isBlank(goldMineSchemeTerm) && StringUtils.isBlank(goldMinePaymentid)) {
            	log.error("Both Gold Mine scheme term {} and gold mine payment plan id {} are empty", goldMineSchemeTerm, goldMinePaymentid);
            	return error();
            }
            
            if(StringUtils.isNotBlank(goldMineSchemeTerm)){
                PaymentMetaDataFactory.addMetaData(PaymentMetaData.Name.SCHEME_TERM, goldMineSchemeTerm, paymentMetaDatas);
            }
        	
            if(StringUtils.isNotBlank(goldMinePaymentid)) {
                PaymentMetaDataFactory.addMetaData(PaymentMetaData.Name.PAYMENTPLAN_ID, goldMinePaymentid, paymentMetaDatas);
            }
        	
			PaymentTransaction paymentTransaction = paymentTransactionService.createPaymentTransaction(cart, masterPaymentId,
																					instrumentType, cardPaymentNetwork, 
																					issuingAuthority, flowId,paymentMetaDatas);
            
            SubPayment subPayment = paymentTransaction.getSubPayment();
            flowScope.put("masterPaymentId", subPayment.getMasterPayment().getId());
            addPaymentTranactionToScope(requestContext, paymentTransaction);
            return success();
        } catch (Throwable e) {
            handleException("GoldMinePaymentAction.createGoldMinePaymentTransaction():",requestContext, e);
            return error();
        }
    }

    protected void handleException(String errorContext, RequestContext requestContext, Throwable exception) throws GoldMinePaymentException {
        log.error("GoldMinePaymentAction.handleException(): Error in payment flow action {} during {} ", getActionNameForLogging(), errorContext, exception);
        String bluestoneTxnId = "";
        MutableAttributeMap flowScope = null;
        try {
            flowScope = requestContext.getFlowScope();
            PaymentTransaction paymentTransaction = (PaymentTransaction) flowScope.get(PAYMENT_TRANSACTION);
            if (paymentTransaction != null) {
                bluestoneTxnId = paymentTransaction.getBluestoneTxnId();
            } else {
                log.info("GoldMinePaymentAction.handleException(): Payment Transaction is null, so we don't have a bluestoneTxnId. " +
                         "Either error happened before it was generated. Or , if that is not true, then there is some other issue.");
            }
            GoldMinePaymentException goldMinePaymentException = new GoldMinePaymentException(PaymentError.INTERNAL_ERROR, bluestoneTxnId);
            if(exception instanceof GoldMinePaymentException) {
                 goldMinePaymentException=(GoldMinePaymentException) exception;
            } else if(exception instanceof Throwable) {
                goldMinePaymentException = new GoldMinePaymentException(PaymentError.INTERNAL_ERROR, exception, bluestoneTxnId);
            }
            
            throw goldMinePaymentException;

        } catch (Throwable th) {
            throw new GoldMinePaymentException(PaymentError.INTERNAL_ERROR, th, "");
        }
    }    
    
    protected void handlePaymentSuccessPage(String orderCode, RequestContext requestContext) throws PaymentException {
    	log.info("GoldMinePaymentAction.handlePaymentSuccessPage actionName {} , flowId={}, orderCode={}", getActionNameForLogging(), getFlowIdFromRequestContext(requestContext),orderCode);                 
        GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlanByCode(orderCode);            
        if(goldMinePlan==null){
        	throw new PaymentException(PaymentError.ORDER_NOT_FOUND);
        }
        PaymentTransaction paymentTransaction = getPaymentTransaction(requestContext);            
        String paymentId = paymentTransaction.getPaymentMetaDataValue(PaymentMetaData.Name.PAYMENTPLAN_ID);
        if(StringUtils.isNotBlank(paymentId)){
        	GoldMinePlanPayment goldMinePlanPayment = goldMinePlan.getLatestPaidPayment();					
			requestContext.getFlowScope().put("goldMinePlanPayment", goldMinePlanPayment);
        }    		
		requestContext.getFlowScope().put("goldMinePlan", goldMinePlan);               
    }
    
	protected void setIsNewCustomer(RequestContext requestContext) {
		Customer customer = null;
		GoldMinePlan goldMinePlan = (GoldMinePlan) requestContext.getFlowScope().get("goldMinePlan");
		if (goldMinePlan != null) {
			customer = goldMinePlan.getCustomer();
		}
		List<Order> allOrdersForCustomer = orderService.getAllOrdersForCustomer(customer);
		if (allOrdersForCustomer.size() > 1) {
			requestContext.getFlowScope().put("customerStatus", "Existing");
		} else {
			requestContext.getFlowScope().put("customerStatus", "New");
		}
	}
}
