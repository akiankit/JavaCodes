package com.bluestone.app.core.feedback.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class OrderFeedbackEmailService {
	
	private static final Logger log = LoggerFactory.getLogger(OrderFeedbackEmailService.class);
	
	private static final String ORDER_FEEDBACK_MAIL_SUBJECT = "Post Order Feedback: BlueStone.com";
	
	@Autowired
    private EmailService emailService;
    
    @Autowired
    private OrderService orderService;

    public void sendOrderFeedbackEmail(String orderId) throws EncryptionException {
        log.info("OrderFeedbackEmailService.sendOrderFeedbackEmail(): for Order Id={}", orderId);
        final TemplateVariables templateVariables = emailService.getTemplateVariables("customer_feedback.vm");
        Order order = orderService.getOrder(Long.parseLong(orderId));
        Customer customer = order.getCustomer();
        templateVariables.put("feedback_link", OrderFeedbackProcessor.getOrderFeedbackURL(orderId, Constants.key));
        templateVariables.put("orderCode",order.getCode());
        templateVariables.setCustomer(customer);
        templateVariables.setToField(customer.getEmail());
        templateVariables.setSubject(ORDER_FEEDBACK_MAIL_SUBJECT);        
        emailService.sendOrderFeedbackEmail(templateVariables);
        log.info("OrderFeedbackEmailService.sendOrderFeedbackEmail(): done for Customer=[{}]", customer.getEmail());
    }
	
}
