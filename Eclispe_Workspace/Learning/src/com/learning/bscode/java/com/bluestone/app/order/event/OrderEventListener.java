package com.bluestone.app.order.event;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.event.listeners.EventListener;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.integration.crm.CRMService;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;


@Component
public class OrderEventListener extends EventListener<OrderEvent> {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private CRMService crmService;

    @Autowired
    private UniwareProcessor uniwareProcessor;

    @Autowired
    private InternalNotification internalNotification;

    @Autowired
    private CustomerNotification customerNotification;

    @Override
    protected void handleSyncEvent(OrderEvent orderEvent) {
        log.debug("OrderEventListener.handleSyncEvent(): Event={}", orderEvent);
        RaiseEvent raiseEvent = orderEvent.getRaiseEvent();
        MessageContext messageContext = orderEvent.getMessageContext();
        Map<String, Serializable> messageContextMap = messageContext.getMessageContextMap();
        Long orderId = (Long) messageContext.get(MessageContext.ORDERID);
        Order order;
        switch (raiseEvent.eventType()) {
            case ORDER_CREATED:
                order = orderService.getOrder(orderId);
                if (order != null) {
                    sendMailToCustomer(order);
                    sendInternalMail(order, messageContextMap);
                    pushToUniware(order, messageContextMap);
                    crmService.send(CRM_EVENT.ORDER_CREATED, messageContextMap);
                } else {
                    log.error("ERROR : OrderEventListener.handleSyncEvent(): ORDER_CREATED event raised, but the order could not be located in the system. Order Id={}", orderId);
                }
                break;
            case ORDER_STATUS_UPDATED:
                crmService.send(CRM_EVENT.ORDER_STATUS_UPDATE, messageContextMap);
                break;
            case ORDER_CANCELED:
                crmService.send(CRM_EVENT.ORDER_STATUS_UPDATE, messageContextMap);
                break;
            case ORDERITEM_STATUS_UPDATED:
                //crmService.send(CRM_EVENT.ORDER_ITEM_STATUS_UPDATE, messageContextMap);
                break;
            case ORDERITEM_SHIPPING_STATUS_UPDATED:
                crmService.send(CRM_EVENT.ORDER_ITEM_SHIPPING_STATUS_UPDATE, messageContextMap);
                break;
            case ORDER_PAYMENT_STATUS_UPDATED:
                crmService.send(CRM_EVENT.ORDER_PAYMENT_STATUS_UPDATE, messageContextMap);
                order = orderService.getOrder(orderId);
                pushToUniware(order, messageContextMap);
                break;
            default:
                break;
        }
    }

    private void pushToUniware(Order order, Map<String, Serializable> messageContextMap) {
        uniwareProcessor.execute(order, messageContextMap);
    }

    private void sendInternalMail(Order order, Map<String, Serializable> messageContextMap) {
        log.debug("OrderEventListener.sendInternalMail():");
        internalNotification.sendNewOrderEmail(order, messageContextMap);
    }


    private void sendMailToCustomer(Order order) {
        log.debug("OrderEventListener.sendMailToCustomer():");
        customerNotification.sendNewOrderEmail(order);
    }

    @Override
    protected void handleAsyncEvent(OrderEvent event) {
        log.debug("OrderEventListener.handleAsyncEvent(): to be implemented");
    }
}
