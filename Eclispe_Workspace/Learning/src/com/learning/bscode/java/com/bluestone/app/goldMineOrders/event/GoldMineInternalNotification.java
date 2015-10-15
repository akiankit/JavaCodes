package com.bluestone.app.goldMineOrders.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.PaymentTransaction;

/**
 * @author Rahul Agrawal
 *         Date: 4/22/13
 */
@Service
public class GoldMineInternalNotification {

    private static final Logger log = LoggerFactory.getLogger(GoldMineInternalNotification.class);

    @Autowired
    private EmailService emailService;

    @Value("${order.event.created.mail.to}")
    private String orderCreatedMailTo;

    private final String INTERNAL_EMAIL_TEMPLATE = "goldmine_internal.vm";

    public void sendInternalMail(GoldMinePlanPayment goldMinePlanPayment, boolean isInstallmentPayment) {
        log.debug("GoldMineInternalNotification.sendInternalMail()");
        try {
            MasterPayment masterPayment = goldMinePlanPayment.getMasterPayment();
            PaymentTransaction paymentTransaction = masterPayment.getLatestSubPayment().getPaymentTransaction();
            GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
            TemplateVariables emailVariables = emailService.getTemplateVariables(INTERNAL_EMAIL_TEMPLATE);
            Cart cart = goldMinePlanPayment.getCart();
            emailVariables.put("amount", NumberUtil.formatPriceIndian(cart.getFinalPrice().doubleValue(), ","));
            emailVariables.put("customerName", cart.getCustomer().getUserName());
            emailVariables.put("paymentStatus", paymentTransaction.getPaymentTransactionStatus());

            emailVariables.put("goldMinePlan", goldMinePlan);
            emailVariables.put("goldMinePayment", goldMinePlanPayment);
            emailVariables.put("paymentTransactionId", paymentTransaction.getId());
            emailVariables.put("details", paymentTransaction.toStringBrief());

            if (isInstallmentPayment) {
                emailVariables.setSubject("Gold Mine Saving plan installment made with installment id - " + goldMinePlanPayment.getId());
                emailVariables.put("isInstallment", true);
            } else {
                emailVariables.setSubject("New Gold Mine Saving plan enrollment with plan Id - " + goldMinePlan.getId());
                emailVariables.put("isInstallment", false);
            }
            emailVariables.setToField(orderCreatedMailTo);
            emailService.send(emailVariables);
        } catch (Exception e) {
            log.error("Failed to send an internal email for Gold Mine Payment Id = {}", goldMinePlanPayment.getId(), e);
        }
    }

}
