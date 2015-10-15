package com.bluestone.app.payment.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.payment.dao.PaymentDao;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;
import com.bluestone.app.payment.spi.model.SubPayment;
import com.bluestone.app.payment.spi.service.PaymentGatewayFinder;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class PaymentTransactionService {
    
    @Autowired
    private PaymentDao paymentDao;
    
    @Autowired
    private MasterPaymentService masterPaymentService;
    
    @Autowired
    private SubPaymentService subPaymentService;
    
    @Autowired
    private PaymentGatewayFinder paymentGatewayFinder;


    @Autowired
    protected PaymentInstrumentService paymentInstrumentService;
    
    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionService.class);
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public PaymentTransaction createPaymentTransaction(Cart cart,
                                                       Long masterPaymentId,
                                                       String instrumentType,
                                                       String cardPaymentNetwork,
                                                       String issuingAuthority,
                                                       String flowId,
                                                       List<PaymentMetaData> paymentMetaDatas) throws PaymentException {
    	PaymentTransaction paymentTransaction = createPaymentTransaction(cart, masterPaymentId, instrumentType, cardPaymentNetwork, issuingAuthority, flowId);
    	if(!paymentMetaDatas.isEmpty()){
        	for (PaymentMetaData paymentMetaData : paymentMetaDatas) {
				paymentMetaData.setPaymentTransaction(paymentTransaction);
			}
        	paymentTransaction.setPaymentMetaDatas(paymentMetaDatas);
        }
    	return paymentTransaction;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public PaymentTransaction createPaymentTransaction(Cart cart,String instrumentType,String flowId,List<PaymentMetaData> paymentMetaDatas) throws PaymentException{
    	PaymentTransaction paymentTransaction = createPaymentTransaction(cart, null,instrumentType, null,null,flowId, paymentMetaDatas);
    	return paymentTransaction;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor=Exception.class)
    public PaymentTransaction createPaymentTransaction(Cart cart, Long masterPaymentId, String instrumentType,
													   String cardPaymentNetwork, String issuingAuthority, 
													   String flowId) throws PaymentException{
    	log.debug("PaymentTransactionService.createPaymentTransaction(): FlowId={} , MasterPaymentId={}, InstrumentType={}", flowId, masterPaymentId, instrumentType);
        PaymentTransaction paymentTransaction = new PaymentTransaction();
        PaymentGateway paymentGateway = paymentGatewayFinder.findPaymentGateway(instrumentType, cardPaymentNetwork, issuingAuthority);
        paymentTransaction.setPaymentGateway(paymentGateway);
        
        long visitorId = 0l;
        String clientIp = "";
        if(cart.getVisitor() != null) {
            Visitor visitor = cart.getVisitor(); // did it inside the check so that nobody can access visitor , which can be null 
            clientIp = visitor.getClientIp();
            visitorId = visitor.getId();
            paymentTransaction.setCustomerIP(clientIp);
        }
        log.info("PaymentTransactionService.createPaymentTransaction(): FlowId={} , VisitorId={}, ClientIP={}, MasterPaymentId={} , PaymentGateway={}",
                 flowId, visitorId, clientIp, masterPaymentId, paymentGateway);
        
        MasterPayment masterPayment = masterPaymentService.createOrGetMasterPayment(masterPaymentId, cart);
        SubPayment subPayment = subPaymentService.createSubPayment(masterPayment);
        
        paymentTransaction.setAmount(cart.getFinalPrice()); // TODO when subpayment logic is implemented , replace this with subpayment amount
        PaymentInstrument selectedPaymentInstrument = paymentInstrumentService.createOrGetPaymentInstrument(instrumentType, issuingAuthority, cardPaymentNetwork);
        paymentTransaction.setPaymentInstrument(selectedPaymentInstrument);
        
        subPayment.setPaymentTransaction(paymentTransaction);

        final PaymentTransactionStatus paymentTransactionStatus = computePaymentTransactionStatus(instrumentType, flowId);
        paymentTransaction.setPaymentTransactionStatus(paymentTransactionStatus);
        paymentDao.create(subPayment);
        
        String encryptedTxnId = CryptographyUtil.encrypt(paymentTransaction.getId() + "", 10);
        log.info("PaymentTransactionService.createPaymentTransaction(): FlowId={} , VisitorId={}, ClientIP={}, MasterPaymentId={} , " +
                 "SubPaymentId={}, PaymentGateway={} , EncryptedTxnId={} , paymentTransactionId={}",
                 flowId, visitorId, clientIp, masterPaymentId, subPayment.getId(), paymentGateway, encryptedTxnId, paymentTransaction.getId());
        paymentTransaction.setBluestoneTxnId(encryptedTxnId);
        paymentTransaction.setFlowExecutionId(flowId); // TODO cleanup once paymentmetadeta is cleaned up. In case of crm order creation, flow id is not going to paymentmetadeta table 
        
        paymentTransaction.setSubPayment(subPayment);

        populateMessageContextWithPaymentTransactionId(paymentTransaction);
        log.debug("PaymentTransactionService.createPaymentTransaction() resulted in {}", paymentTransaction);
        return paymentTransaction;
	}

    private PaymentTransactionStatus computePaymentTransactionStatus(String instrumentType, String flowId) {
        log.info("PaymentTransactionService.computePaymentTransactionStatus() for instrumentType={} , flowId={}", instrumentType, flowId);
        //@todo : Rahul Agrawal : To discuss if we shd handle logic to set the payment status by CRM here ? Ideally it should not be outside this.
        return PaymentTransactionStatus.CREATED;
    }
    
    public PaymentTransaction getPaymentTransactionWithRowLock(long paymentTransactionId) {
        return paymentDao.getPaymentTransactionWithRowLock(paymentTransactionId);
    }
    
    public PaymentTransaction getPaymentTransaction(long paymentTransactionId) {
        return paymentDao.find(PaymentTransaction.class, paymentTransactionId, true);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, readOnly = false)
    public PaymentTransaction updatePaymentTransaction(PaymentTransaction paymentTransaction) {
        return paymentDao.update(paymentTransaction);
    }

    public PaymentTransaction getPaymentTransactionFromEncryptedTxnId(String encryptedTxnId) {
        log.debug("PaymentTransactionService.getPaymentTransactionFromEncryptedTxnId() from encryptedTxnId={}", encryptedTxnId);
        if (StringUtils.isBlank(encryptedTxnId)) {
            return null;
        }
        String decryptedTxnId = CryptographyUtil.decrypt(encryptedTxnId);
        try {
            long id = Long.parseLong(decryptedTxnId);
            return getPaymentTransaction(id);
        } catch (NumberFormatException numberFormatException) {
            log.error("Error getting PaymentTransaction from Invalid encrypted Transaction id {}", encryptedTxnId, numberFormatException);
            return null;
        } catch (Throwable throwable) {
            log.error("Error getting PaymentTransaction from the encrypted transaction id={}", encryptedTxnId, throwable);
            return null;
        }
    }

    @RaiseEvent(eventType=EventType.ON_PAYMENT_RESPONSE)
    public void triggerPaymentResponseEvent(PaymentTransaction paymentTransaction) {
        if(paymentTransaction != null) {
            Util.getMessageContext().put(MessageContext.PAYMENT_TRANSACTION_ID, paymentTransaction.getId());
        }
    }
    
    /*@RaiseEvent(eventType=EventType.ON_PAYMENT_STARTED)
    public String getPaymentForm(PaymentTransaction paymentTransaction, Cart originalCart) throws PaymentException {
        log.debug("PaymentTransactionService.getPaymentForm(): Flow Execution Id={} , PaymentTransaction Id={} , BlueStoneTxnId={}",
                  paymentTransaction.getFlowExecutionId(), paymentTransaction.getId(), paymentTransaction.getBluestoneTxnId());

        PaymentGatewayService paymentGatewayService = paymentGatewayFinder.getPaymentGatewayService(paymentTransaction.getPaymentGateway());
        Cart clonedCart = cartService.cloneCart(originalCart);
        paymentTransaction.setAmount(clonedCart.getFinalPrice());
        List<PaymentMetaData> paymentMetaDatas = paymentTransaction.getPaymentMetaDatas();
        PaymentMetaDataFactory.create(PaymentMetaData.Name.CLONED_CART_ID, String.valueOf(clonedCart.getId()), paymentMetaDatas);
        for (PaymentMetaData paymentMetaData : paymentMetaDatas) {
			paymentMetaData.setPaymentTransaction(paymentTransaction);
		}
		paymentTransaction.setPaymentMetaDatas(paymentMetaDatas);
        updatePaymentTransaction(paymentTransaction);
        
        PaymentRequest paymentRequest = paymentGatewayService.createPaymentRequest(paymentTransaction, clonedCart, originalCart);
        
        if (log.isTraceEnabled()) {
			Set<String> keySet = paymentRequest.getTemplateVariables().keySet();
			for (String eachKey : keySet) {
				log.trace("key {} , value {}", eachKey, paymentRequest.getTemplateVariables().get(eachKey));
			}
		}
        
        log.debug("PaymentTransactionService.getPaymentForm() for \n{} \nPaymentTransaction={}" , paymentRequest, paymentTransaction);
        Map<String, Object> templateVariables = paymentRequest.getTemplateVariables();
        Map<String , Object> velocityVariables = new HashMap<String, Object>();
        velocityVariables.put("formParams", templateVariables);
        String form = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                                  "velocity/" + paymentRequest.getVmTemplatePath(),
                                                                  velocityVariables);
        populateMessageContextWithPaymentTransactionId(paymentTransaction);
        return form;
    }*/

    private void populateMessageContextWithPaymentTransactionId(PaymentTransaction paymentTransaction) {
        Util.getMessageContext().put(MessageContext.PAYMENT_TRANSACTION_ID, paymentTransaction.getId());
    }

    @Transactional(readOnly=false, propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public PaymentTransaction updatePaymentTransactionStatus(PaymentTransaction paymentTransaction, PaymentTransactionStatus paymentTransactionStatus) {
		paymentTransaction.setPaymentTransactionStatus(paymentTransactionStatus);
		return updatePaymentTransaction(paymentTransaction);
	}
    

    /*
    public void createPaymentMetaData(PaymentMetaData.Name name, String value, List<PaymentMetaData> paymentMetaDatas) {
		PaymentMetaData paymentMetaData = new PaymentMetaData();
		paymentMetaData.setName(name);
		paymentMetaData.setValue(value);
		paymentMetaDatas.add(paymentMetaData);
	}*/
	
}
