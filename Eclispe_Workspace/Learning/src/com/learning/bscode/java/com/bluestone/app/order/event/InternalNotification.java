package com.bluestone.app.order.event;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.payment.spi.model.PaymentTransaction;

/**
 * @author Rahul Agrawal
 *         Date: 4/21/13
 */
@Service
public class InternalNotification {

    private static final Logger log = LoggerFactory.getLogger(InternalNotification.class);

    @Value("${order.event.created.mail.to}")
    private String orderCreatedMailTo;

    @Value("${solitaire.order.event.created.mail.to}")
    private String solitaireOrderCreatedMailTo;

    @Autowired
    private EmailService emailService;

    public void sendNewOrderEmail(Order order, Map<String, Serializable> messageContextMap) {
        log.debug("InternalNotification.sendNewOrderEmail():");
        try {
            TemplateVariables emailVariables = emailService.getTemplateVariables("ordercreated_internal.vm");
            Cart cart = order.getCart();
            emailVariables.put("amount", NumberUtil.formatPriceIndian(cart.getFinalPrice().doubleValue(), ","));
            emailVariables.put("customerName", cart.getCustomer().getUserName());
            emailVariables.put("paymentStatus", messageContextMap.get(MessageContext.ORDER_PAYMENT_TRANSACTION_STATUS));
            emailVariables.put("orderId", order.getId());
            emailVariables.put("orderCode", order.getCode());
            enrichEmail(order, emailVariables);
            emailVariables.put("subject", "New Order has been placed with Order Id - " + order.getId());
            emailVariables.put("to", orderCreatedMailTo);
            emailService.sendInternalEmailsWithoutImages(emailVariables);
        } catch (Exception e) {
            log.error("Failed to send an internal email for Order Id = {}", order.getId(), e);
        }
        List<OrderItem> orderItems = order.getOrderItems(Product.PRODUCT_TYPE.SOLITAIRE);
        if (!orderItems.isEmpty()) {
            sendSolitaireMail(order);
        }
    }

    private void sendSolitaireMail(Order order) {
        log.debug("InternalNotification.sendNewOrderEmail():");
        try {
            TemplateVariables emailVariables = emailService.getTemplateVariables("solitaireorder_internal.vm");
            List<OrderItem> orderItems = order.getOrderItems(Product.PRODUCT_TYPE.SOLITAIRE);
            emailVariables.put("order", order);
            emailVariables.put("orderItems", (Serializable) orderItems);
            emailVariables.setToField(solitaireOrderCreatedMailTo);
            emailVariables.setSubject("New Solitaire Order has been placed with order id - " + order.getId());
            emailService.sendInternalEmailsWithoutImages(emailVariables);
        } catch (Exception e) {
            log.error("Failed to send an internal email for Order Id = {}", order.getId(), e);
        }
    }

    private String enrichEmail(Order order, TemplateVariables emailVariables) {
        StringBuilder text = new StringBuilder();
        try {
            final PaymentTransaction paymentTransaction = order.getMasterPayment().getLatestSubPayment().getPaymentTransaction();
            emailVariables.put("paymentTransactionId", paymentTransaction.getId());
            emailVariables.put("details", paymentTransaction.toStringBrief());
        } catch (Exception exception) {
            log.error("Error: InternalNotification.enrichEmail(): ", exception);
        }
        return text.toString();
    }
}
