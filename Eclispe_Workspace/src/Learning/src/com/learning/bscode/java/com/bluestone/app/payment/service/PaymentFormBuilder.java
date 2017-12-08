package com.bluestone.app.payment.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentRequest;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.service.PaymentGatewayFinder;
import com.bluestone.app.payment.spi.service.PaymentGatewayService;

/**
 * @author Rahul Agrawal
 *         Date: 5/22/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class PaymentFormBuilder {

    private static final Logger log = LoggerFactory.getLogger(PaymentFormBuilder.class);

    @Autowired
    private PaymentGatewayFinder paymentGatewayFinder;

    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private VelocityEngine velocityEngine;

    @RaiseEvent(eventType = EventType.ON_PAYMENT_STARTED)
    public String getPaymentForm(PaymentTransaction paymentTransaction, Cart originalCart) throws PaymentException {
        log.debug("PaymentFormBuilder.getPaymentForm(): Flow Execution Id={} , PaymentTransaction Id={} , BlueStoneTxnId={}",
                  paymentTransaction.getFlowExecutionId(), paymentTransaction.getId(), paymentTransaction.getBluestoneTxnId());

        PaymentGatewayService paymentGatewayService = paymentGatewayFinder.getPaymentGatewayService(paymentTransaction.getPaymentGateway());
        Cart clonedCart = cartService.cloneCart(originalCart);
        paymentTransaction.setAmount(clonedCart.getFinalPrice());
        List<PaymentMetaData> paymentMetaDatas = paymentTransaction.getPaymentMetaDatas();

        PaymentMetaDataFactory.addMetaData(PaymentMetaData.Name.CLONED_CART_ID, String.valueOf(clonedCart.getId()), paymentMetaDatas);

        for (PaymentMetaData paymentMetaData : paymentMetaDatas) {
            paymentMetaData.setPaymentTransaction(paymentTransaction);
        }
        paymentTransaction.setPaymentMetaDatas(paymentMetaDatas);

        paymentTransaction = paymentTransactionService.updatePaymentTransaction(paymentTransaction);

        PaymentRequest paymentRequest = paymentGatewayService.createPaymentRequest(paymentTransaction, clonedCart, originalCart);

        if (log.isTraceEnabled()) {
            Set<String> keySet = paymentRequest.getTemplateVariables().keySet();
            for (String eachKey : keySet) {
                log.trace("key {} , value {}", eachKey, paymentRequest.getTemplateVariables().get(eachKey));
            }
        }

        log.debug("PaymentFormBuilder.getPaymentForm() for \n{} \nPaymentTransaction={}", paymentRequest, paymentTransaction);
        Map<String, Object> templateVariables = paymentRequest.getTemplateVariables();
        Map<String, Object> velocityVariables = new HashMap<String, Object>();
        velocityVariables.put("formParams", templateVariables);
        String form = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                                  "velocity/" + paymentRequest.getVmTemplatePath(),
                                                                  velocityVariables);
        populateMessageContextWithPaymentTransactionId(paymentTransaction);
        return form;
    }

    private void populateMessageContextWithPaymentTransactionId(PaymentTransaction paymentTransaction) {
        Util.getMessageContext().put(MessageContext.PAYMENT_TRANSACTION_ID, paymentTransaction.getId());
    }
}
