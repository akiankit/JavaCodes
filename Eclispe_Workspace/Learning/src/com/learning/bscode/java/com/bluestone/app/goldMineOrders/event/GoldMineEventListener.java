package com.bluestone.app.goldMineOrders.event;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.event.listeners.EventListener;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanPaymentService;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.integration.crm.CRMService;
import com.bluestone.app.order.event.OrderEventListener;

@Component
public class GoldMineEventListener extends EventListener<GoldMineEvent> {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @Autowired
    private GoldMineInternalNotification goldMineInternalNotification;

    @Autowired
    private GoldMineCustomerNotification goldMineCustomerNotification;

    @Autowired
    private GoldMinePlanPaymentService goldMinePlanPaymentService;

    @Autowired
    private CRMService crmService;


    @Value("${order.event.created.mail.to}")
    private String orderCreatedMailTo;

    @Override
    protected void handleSyncEvent(GoldMineEvent orderEvent) {
        log.debug("GoldMineEventListener.handleSyncEvent(): Event={}", orderEvent);
        RaiseEvent raiseEvent = orderEvent.getRaiseEvent();
        MessageContext messageContext = orderEvent.getMessageContext();
        Map<String, Serializable> messageContextMap = messageContext.getMessageContextMap();
        Long goldMinePaymentId = (Long) messageContext.get(MessageContext.GOLD_MINE_PAYMENT_ID);
        GoldMinePlanPayment goldMinePlanPayment = goldMinePlanPaymentService.getGoldMinePlanPaymentById(goldMinePaymentId);
        if (goldMinePlanPayment == null) {
            log.error("ERROR : GoldMineEventListener.handleSyncEvent(): GOLD_MINE_PLAN_ENROLMENT event raised, " +
                      "but the plan could not be located in the system. GoldMinePaymentPlan Id={}", goldMinePaymentId);
            return;

        }
        try {
            switch (raiseEvent.eventType()) {
                case GOLD_MINE_PLAN_ENROLMENT:
                    sendInternalNotification(goldMinePlanPayment, false);
                    sendEnrollmentNotificationToCustomer(goldMinePlanPayment);
                    crmService.send(CRM_EVENT.GOLD_MINE, messageContextMap);
                    break;
                case GOLD_MINE_PLAN_PAYMENT:
                    sendInternalNotification(goldMinePlanPayment, true);
                    notifyCustomer(goldMinePlanPayment);
                    crmService.send(CRM_EVENT.GOLD_MINE, messageContextMap);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            log.error("Unable to process event=[{}] MessageContext {}", raiseEvent.eventType().toString(), messageContext, e);
        }

    }

    private void notifyCustomer(GoldMinePlanPayment goldMinePlanPayment) {
        goldMineCustomerNotification.installmentPaymentReceived(goldMinePlanPayment);
    }

    private void sendEnrollmentNotificationToCustomer(GoldMinePlanPayment goldMinePlanPayment) {
        goldMineCustomerNotification.newOrderConfirmed(goldMinePlanPayment);
    }

    private void sendInternalNotification(GoldMinePlanPayment goldMinePlanPayment, boolean isInstallmentPayment) {
        goldMineInternalNotification.sendInternalMail(goldMinePlanPayment, isInstallmentPayment);

    }

    @Override
    protected void handleAsyncEvent(GoldMineEvent event) {
        log.debug("GoldMineEventListener.handleAsyncEvent(): to be implemented");
    }
}
