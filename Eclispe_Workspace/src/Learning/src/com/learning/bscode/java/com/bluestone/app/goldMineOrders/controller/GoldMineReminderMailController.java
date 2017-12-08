package com.bluestone.app.goldMineOrders.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.goldMineOrders.event.GoldMineEmailService;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment.GoldMinePlanPaymentStatus;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanPaymentService;

@Controller
@RequestMapping("/gmsp*")
public class GoldMineReminderMailController {
    private static final Logger log = LoggerFactory.getLogger(GoldMineReminderMailController.class);

    private static final String GOLD_MINE_PAYMENT_REMINDER = "[BlueStone] Payment reminder for Gold Mine Saving Plan.";

    @Autowired
    private GoldMinePlanPaymentService goldMinePlanPaymentService;

    @Autowired
    private GoldMineEmailService goldMineEmailService;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    String updateAndsendReminderMail(HttpServletRequest request) {
        String urlQueryString = request.getQueryString();
        String[] paramAndValue;
        try {
            String[] queryParts = urlQueryString.split(Constants.URL_QUERY_STRING_DELIMITER);
            paramAndValue = queryParts[0].split(Constants.EQUALS_SIGN);
            String paramName = paramAndValue[0].trim();
            final String goldminePaymentId = paramAndValue[1].trim();

            GoldMinePlanPayment goldMinePlanPayment = goldMinePlanPaymentService.getGoldMinePlanPaymentById(Long.parseLong(goldminePaymentId));
            GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
            if (paramName.equalsIgnoreCase("due")) {
                log.debug("GoldMineReminderMailController: updateAndsendReminderMail for goldminepaymentID={}", goldminePaymentId);
                goldMineEmailService.sendMailToCustomer("GoldMinePaymentReminder.vm", GOLD_MINE_PAYMENT_REMINDER,
                                                        goldMinePlan.getCustomer().getEmail(), goldMinePlanPayment);
                log.debug("GoldMineReminderMailController: updateAndsendReminderMail sent mail for goldminepaymentID={}", goldminePaymentId);
                return "Sucessfully sent reminder mail for payment id = " + goldMinePlanPayment.getId();
            }
            if (paramName.equalsIgnoreCase("overdue")) {
                log.debug("GoldMineReminderMailController: updateAndsendReminderMail for goldminepaymentID={}", goldminePaymentId);
                goldMinePlanPayment.setGoldMinePlanPaymentStatus(GoldMinePlanPaymentStatus.OVERDUE);
                goldMinePlanPaymentService.update(goldMinePlanPayment);
                log.debug("GoldMineReminderMailController: updateAndsendReminderMail updated status as OVERDUE for goldminepaymentID={}", goldminePaymentId);
                return "Sucessfully updated status to OVERDUE for payment id = " + goldMinePlanPayment.getId();
            }
            return "Could not match any action for payment id = " + goldMinePlanPayment.getId();
        } catch (Exception e) {
            log.error("Error in sending reminder mail for url {}", urlQueryString, e);
            return "Error in sending reminder mail for url = " + urlQueryString + " Error=" + e.getMessage();
        }
    }
}
