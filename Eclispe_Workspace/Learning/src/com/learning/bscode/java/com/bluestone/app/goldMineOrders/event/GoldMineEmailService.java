package com.bluestone.app.goldMineOrders.event;

import java.util.Calendar;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;

@Service
public class GoldMineEmailService {

    private static final Logger log = LoggerFactory.getLogger(GoldMineEmailService.class);

    @Autowired
    private EmailService emailService;

    public void sendMailToCustomer(String vmTemplateName, String subject, String to, GoldMinePlanPayment goldMinePlanPayment) {
        log.info("GoldMineEmailService.sendMailToCustomer(): To=[{}] Subject=[{}]", to, subject);
        GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
        try {
            TemplateVariables emailVariables = emailService.getTemplateVariables(vmTemplateName);

            emailVariables.put("goldMinePlanPayment", goldMinePlanPayment);
            emailVariables.setCustomer(goldMinePlan.getCustomer());
            emailVariables.setSubject(subject);
            emailVariables.setToField(to);
            emailVariables.put("number", NumberUtil.class);
            emailVariables.put("date", DateTimeUtil.class);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(goldMinePlanPayment.getDueDate()); // TODO if next payment is bluestone paid 
            calendar.add(Calendar.MONTH, 1);
            emailVariables.put("nextDueDate", calendar.getTime());
            emailService.send(emailVariables);
        } catch (Exception e) {
            log.error("Error occurred while executing GoldMineEmailService.sendMailToCustomer for GoldMinePlanId=[{}] Subject=[{}]  emailTo=[{}]",
                      goldMinePlan.getId(), subject, to, e);
            Throwables.propagate(Throwables.getRootCause(e));
        }
    }
}
