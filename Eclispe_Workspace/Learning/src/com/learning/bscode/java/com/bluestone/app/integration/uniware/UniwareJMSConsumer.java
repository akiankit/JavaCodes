package com.bluestone.app.integration.uniware;

import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.uniware.UniwareOrderService;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UniwareJMSConsumer implements SessionAwareMessageListener {

    private static final Logger log = LoggerFactory.getLogger(UniwareJMSConsumer.class);

    @Value("${build.mode}")
    private String buildMode;

    @Autowired
    private UniwareOrderService uniwareOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderStatusFilter orderStatusFilter;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;

    @Autowired
    private FixedFlexibleProcessor fixedFlexibleProcessor;

    @Override
    public void onMessage(Message message, Session session) {
        String orderId = "Not yet initialized";
        String jmsMessageID = "Not yet initialized";
        try {
            jmsMessageID = message.getJMSMessageID();
            if (message instanceof TextMessage) {
                orderId = ((TextMessage) message).getText();
                log.info("UniwareJMSConsumer.onMessage(): IsRedelivered={} MessageID={} Orderid=[{}]", message.getJMSRedelivered(), jmsMessageID , orderId);
                createUniwareSaleOrder(orderId);
                message.acknowledge();
            } else {
                log.warn("UniwareJMSConsumer.onMessage() Message is not an TextMessage *******");
            }
        } catch (JMSException jmsAPIException) {
            log.error("UniwareJMSConsumer.onMessage() : JMS Error during sending OrderId={} to Uniware- ErrorCode={} Cause={}", orderId, jmsAPIException.getErrorCode(),
                      JmsUtils.buildExceptionMessage(jmsAPIException), jmsAPIException);
            productionAlert.send("Failed to push the orderId=" + orderId + " into Uniware.", jmsAPIException);
            forceMessageReDeliveryAsPerHornetJMSSpec(session);
        } catch (Throwable throwable) {
            log.error("UniwareJMSConsumer.onMessage() : Error during Sending orderId={} to Uniware - {}", orderId, throwable.toString(), Throwables.getRootCause(throwable));
            productionAlert.send("Failed to push the orderId=" + orderId + " into Uniware", throwable);
            forceMessageReDeliveryAsPerHornetJMSSpec(session);
        }
    }

    private void createUniwareSaleOrder(String orderId) throws RemoteException {
        log.debug("UniwareJMSConsumer.createUniwareSaleOrder(): OrderId={}", orderId);
        final Order order = orderService.getOrder(Long.parseLong(orderId));
        final String statusName = order.getStatus().getStatusName();
        if (orderStatusFilter.execute(order)) {
            log.debug("UniwareJMSConsumer.createUniwareSaleOrder():Pushing to Uniware: OrderId={} Status={}", orderId, statusName);
            fixedFlexibleProcessor.execute(order);
            final boolean isCreated = uniwareOrderService.createASaleOrder(order);
        } else {
            log.info("********Not pushing the Order id={} into Uniware as the Order Status={}", order.getId(), statusName);
        }
    }

    private void forceMessageReDeliveryAsPerHornetJMSSpec(Session session) {
        try {
            session.recover();
        } catch (JMSException e) {
            log.error("UniwareJMSConsumer.onMessage()- Inside catch block while trying to recover session - failed to recover session. {}",
                      JmsUtils.buildExceptionMessage(e), e);
        }
    }
}
