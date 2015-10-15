package com.bluestone.app.order.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bluestone.app.account.PasswordProcessors;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;
import com.bluestone.app.order.model.Order;

/**
 * @author Rahul Agrawal
 *         Date: 4/21/13
 */
@Service
public class CustomerNotification {

    private static final Logger log = LoggerFactory.getLogger(CustomerNotification.class);

    @Autowired
    private EmailService emailService;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;

    private final String ORDER_CONFIRMATION_MAIL_SUBJECT = "[BlueStone] Confirmation of your BlueStone Order ";

    private String EMAIL_TEMPLATE_NAME = "ordercreated_customer.vm";

    public void sendNewOrderEmail(Order order) {
        log.info("CustomerNotification.sendNewOrderEmail(): for OrderId=[{}]", order.getId());
        try {
            TemplateVariables emailVariables = emailService.getTemplateVariables(EMAIL_TEMPLATE_NAME);
            emailVariables.put("order", order);
            Customer customer = order.getCart().getCustomer();
            emailVariables.setCustomer(customer);
            emailVariables.put("cart", order.getCart());
            emailVariables.setSubject(ORDER_CONFIRMATION_MAIL_SUBJECT + order.getCode());
            emailVariables.setToField(customer.getEmail());
            emailVariables.put("number", NumberUtil.class);
            emailVariables.put("date", DateTimeUtil.class);
            emailService.send(emailVariables);
        } catch (Exception e) {
            log.error("Error: CustomerNotification.sendNewOrderEmail(): for OrderId=[{}]", order.getId(), e);
            productionAlert.send("Failed - CustomerNotification.sendNewOrderEmail(): for OrderId=" + order.getId(), e);
        }
    }
}
