package com.bluestone.app.checkout.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.event.listeners.EventListener;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.integration.crm.CRMService;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentResponse.GATEWAYRESPONSE;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.SubPayment;

@Component
public class PaymentEventListener extends EventListener<PaymentEvent> {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private CRMService crmService;
    
    @Autowired
    private CartService cartService;

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Value("${payment.event.started.mail.to}")
    private String paymentStartedMailTo;

    @Value("${payment.event.success.mail.to}")
    private String paymentSuccessMailTo;

    @Override
    protected void handleSyncEvent(PaymentEvent event) {
        RaiseEvent raiseEvent = event.getRaiseEvent();
        MessageContext messageContext = event.getMessageContext();
        log.debug("PaymentEventListener.handleSyncEvent(): {} for MessageContext Id={}", raiseEvent.eventType(), messageContext.getId());
        TemplateVariables emailVariables = emailService.getTemplateVariables("payment_mail.vm");
        Cart cart = null;
        Long paymentTransactionId = (Long) messageContext.get(MessageContext.PAYMENT_TRANSACTION_ID);
        PaymentTransaction paymentTransaction = null;
        if (paymentTransactionId != null) {
            paymentTransaction = paymentTransactionService.getPaymentTransaction(paymentTransactionId);
            if (paymentTransaction != null) {
                SubPayment subPayment = paymentTransaction.getSubPayment();
                cart = subPayment.getMasterPayment().getCart();
                emailVariables.put("paymentTransaction", paymentTransaction);
                PaymentInstrument paymentInstrument = paymentTransaction.getPaymentInstrument();
                emailVariables.put("cart", cart);
                emailVariables.put("paymentInstrument", paymentInstrument);
                emailVariables.put("paymentTransactionId", paymentTransaction.getId());
                emailVariables.put("details", paymentTransaction.toString());
            } else {
                log.warn("PaymentEventListener payment transaction is could not be found for paymentTransactionId=[{}]", paymentTransactionId);
            }
        }else{
            log.warn("PaymentEventListener.handleSyncEvent(): PaymentTransactionId in message context is NULL");
        }

        switch (raiseEvent.eventType()) {
            case ON_PAYMENT_STARTED:
                emailVariables.setSubject("Payment Transaction Started");
                emailVariables.setToField(paymentStartedMailTo);
                emailService.sendInternalEmailsWithoutImages(emailVariables);
                break;

            case ON_PAYMENT_RESPONSE:
                if (paymentTransaction != null) {
                    PaymentResponse paymentResponse = paymentTransaction.getPaymentResponse();
                    long clonedCartId = paymentResponse.getClonedCartId();
                    if(clonedCartId != 0l) {
                        // setting cloned cart details for sending payment emails
                        Cart clonedCart = cartService.getInactiveCart(clonedCartId);
                        emailVariables.put("cart", clonedCart);
                    }
                    GATEWAYRESPONSE gatewayResponse = paymentResponse.getGatewayResponse();
                    switch (gatewayResponse) {
                        case SUCCESS:
                            emailVariables.setSubject("Payment Transaction Response Received");
                            emailVariables.setToField(paymentSuccessMailTo);
                            emailService.sendInternalEmailsWithoutImages(emailVariables);
                            break;

                        case FAILURE:
                            emailVariables.setSubject("Payment Transaction Failure");
                            emailVariables.setToField(paymentSuccessMailTo);
                            emailService.sendInternalEmailsWithoutImages(emailVariables);
                            crmService.send(CRM_EVENT.PAYMENT_FAILURE, messageContext.getMessageContextMap());
                            break;

                        case PENDING:
                            break;

                        default:
                            break;
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void handleAsyncEvent(PaymentEvent event) {
        log.debug("PaymentEventListener.handleAsyncEvent(): Should not have landed here.");
    }
}
