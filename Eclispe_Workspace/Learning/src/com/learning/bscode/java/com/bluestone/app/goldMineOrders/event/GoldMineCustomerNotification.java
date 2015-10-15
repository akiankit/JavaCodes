package com.bluestone.app.goldMineOrders.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanService;

/**
 * @author Rahul Agrawal
 *         Date: 4/22/13
 */
@Service
public class GoldMineCustomerNotification {

    private static final Logger log = LoggerFactory.getLogger(GoldMineCustomerNotification.class);

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;

    @Autowired
    private GoldMineEmailService goldMineEmailService;
    
    @Autowired
    private GoldMinePlanService goldMinePlanService;

    private final String ENROLLMENT_EMAIL_SUBJECT = "[BlueStone] Confirmation of your Gold Mine Saving Plan ";

    private final String INSTALLMENT_PAYMENT_RECEIVED_EMAIL_SUBJECT =
            "[BlueStone] Confirmation of your Installment Payment for your Gold Mine Saving Plan ";

    public void newOrderConfirmed(final GoldMinePlanPayment goldMinePlanPayment) {
        log.debug("GoldMineCustomerNotification.newOrderConfirmed():");
        GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
        String subject = ENROLLMENT_EMAIL_SUBJECT + goldMinePlan.getCode();
        try {
            goldMineEmailService.sendMailToCustomer("GoldMineInvestmentConfirmation.vm",
                                                    subject,
                                                    goldMinePlan.getCustomer().getEmail(),
                                                    goldMinePlanPayment);
        } catch (Exception exception) {
            productionAlert.send("Failed to send goldmine enrollment email to customer", exception);
        }
    }
    
    public void resendGoldMineMail(final long goldMinePlanId){
    	GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlan(goldMinePlanId);
    	GoldMinePlanPayment goldMinePlanPayment = goldMinePlan.getGoldMinePlanPayments().get(0);
    	newOrderConfirmed(goldMinePlanPayment);
    }

    public void installmentPaymentReceived(final GoldMinePlanPayment goldMinePlanPayment) {
        log.debug("GoldMineCustomerNotification.installmentPaymentReceived():");
        GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
        String subject = INSTALLMENT_PAYMENT_RECEIVED_EMAIL_SUBJECT + goldMinePlan.getCode();
        try {
            goldMineEmailService.sendMailToCustomer("GoldMinePaymentReceived.vm",
                                                    subject,
                                                    goldMinePlan.getCustomer().getEmail(),
                                                    goldMinePlanPayment);
        } catch (Exception exception) {
            productionAlert.send("Failed to send goldmine payment receipt email to customer", exception);
        }
    }
}
