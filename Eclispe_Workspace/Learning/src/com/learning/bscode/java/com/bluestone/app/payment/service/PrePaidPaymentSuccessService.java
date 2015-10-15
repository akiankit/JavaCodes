package com.bluestone.app.payment.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanService;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.google.common.base.Throwables;

@Service
public class PrePaidPaymentSuccessService {

	private static final Logger log = LoggerFactory.getLogger(PrePaidPaymentSuccessService.class);
	
    protected static final String EVENT_ID_PAYMENTSUCCESS = "&_eventId=paymentsuccess";


    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;
    
    @Autowired
    private GoldMinePlanService goldMinePlanService;
    
    @Autowired
    private OrderService orderService;


	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String handlePrePaidPaymentSuccess(PaymentTransaction paymentTransaction) {
    	log.info("PrePaidPaymentSuccessService.handlePostPaymentSuccess for paymenttransaction {}" , paymentTransaction);
    	String bluestoneTxnId = paymentTransaction.getBluestoneTxnId();
		String paymentPlanId = paymentTransaction.getPaymentMetaDataValue(PaymentMetaData.Name.PAYMENTPLAN_ID);
        String schemeTerm = paymentTransaction.getPaymentMetaDataValue(PaymentMetaData.Name.SCHEME_TERM);
        String flowId = paymentTransaction.getPaymentMetaDataValue(PaymentMetaData.Name.FLOW_EXECUTION_ID);
        
    	String path="";
    	
    	//TODO: find a better way to distinguish goldmine and jewellery orders
        if(StringUtils.isNotBlank(paymentPlanId) || StringUtils.isNotBlank(schemeTerm)){
        	log.info("Handling gold mine payment");
         	//GoldMine Order
         	GoldMinePlan goldMinePlan = createGoldMineOrder(paymentTransaction);        	                
         	path = getPaymentSuccessUrl(flowId, goldMinePlan.getCode(), bluestoneTxnId);                
        } else{
        	log.info("Handling jewellery payment");
         	Order order = createOrder(paymentTransaction);        	                
         	path = getPaymentSuccessUrl(flowId, order.getCode(), bluestoneTxnId);                            
        }   
        return path;
    }
    
    private String getPaymentSuccessUrl(String flowId, String encryptedOrderId,	String encryptedTxnId) {
		StringBuilder url = new StringBuilder();
		url.append(Util.getSiteUrlWithContextPath());
		url.append("/order?execution=");
		url.append(flowId);
		url.append(EVENT_ID_PAYMENTSUCCESS);
		url.append("&oid=");
		url.append(encryptedOrderId);
		url.append("&txn=");
        url.append(encryptedTxnId);
		String path = url.toString();
		log.info("PrePaidPaymentSuccessService.getFlowUrl() = {}", path);
		return path;
	}

    
	public Order createOrder(PaymentTransaction paymentTransaction) {
		log.info("Begin PrePaidPaymentSuccessService.createOrder() for {}",paymentTransaction.toStringBrief());
		Order order = null;
		try {
			order = orderService.createOrder(paymentTransaction);
		} catch (Throwable throwable) {
			log.error(
					"Error: PrePaidPaymentSuccessService.createOrder():PaymentTransactionId={} payment status={} was done, but Order Creation Failed. : Reason={} ",
					paymentTransaction.getId(), paymentTransaction.getPaymentTransactionStatus().name(), throwable.toString());
			productionAlert.send("Payment was made, but Failure to create the order. PaymentTransactionId="+ paymentTransaction.getId() + " : "+ paymentTransaction.toStringBrief(), throwable);
			Throwables.propagate(throwable);
		}
		return order;
	}
	
	private GoldMinePlan createGoldMineOrder(PaymentTransaction paymentTransaction) {
        log.info("Begin PrePaidPaymentSuccessService.createGoldMineOrder() for {}", paymentTransaction.toStringBrief());
        GoldMinePlan goldMinePlan = null;
        try {
        	List<PaymentMetaData> paymentMetaDatas = paymentTransaction.getPaymentMetaDatas();
    		for (PaymentMetaData paymentMetaData : paymentMetaDatas) {
    			PaymentMetaData.Name name = paymentMetaData.getName();
    			if(name.equals(PaymentMetaData.Name.PAYMENTPLAN_ID)){
    				String paymentId = paymentMetaData.getValue();
					GoldMinePlanPayment goldMinePlanPayment = goldMinePlanService.addPaymentToPlan(paymentTransaction, paymentId);
					goldMinePlan = goldMinePlanPayment.getGoldMinePlan();					
    			} else if(name.equals(PaymentMetaData.Name.SCHEME_TERM)){
    				int term = Integer.valueOf(paymentMetaData.getValue());
    				goldMinePlan = goldMinePlanService.enrollPlan(paymentTransaction, term);    	            
    			}    			
    		}
        } catch (Throwable throwable) {
            log.error("Error: PrePaidPaymentSuccessService.createGoldMineOrder():PaymentTransactionId=[{}] payment status=[{}] was done, but Gold Mine action Failed. : Reason={} ",
                      paymentTransaction.getId(), paymentTransaction.getPaymentTransactionStatus().name(), throwable.toString());
            productionAlert.send("Payment was made, but Failure to act upon Gold Mine order. PaymentTransactionId=" + paymentTransaction.getId()
                                 + " : " + paymentTransaction.toStringBrief(), throwable);
            Throwables.propagate(throwable);
        }
        return goldMinePlan;
    }

}
