package com.bluestone.app.core.event;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.bluestone.app.account.event.CustomerEvent;
import com.bluestone.app.checkout.event.CartEvent;
import com.bluestone.app.checkout.event.PaymentEvent;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.goldMineOrders.event.GoldMineEvent;
import com.bluestone.app.order.event.OrderEvent;

@Aspect
@Component
public class RaiseEventProcessor implements Ordered{

    private static final Logger log = LoggerFactory.getLogger(RaiseEventProcessor.class);

    @Autowired
    private EventPublisher eventPublisher;

    //@Order(value=Integer.MAX_VALUE)
    @Before(value = "@annotation(raiseEvent)")
    public void beforeMethodProcessing(JoinPoint joinPoint, RaiseEvent raiseEvent) {
        //log.debug("RaiseEventProcessor.beforeMethodProcessing(): RaiseEvent={}", raiseEvent);
        EventType eventType = raiseEvent.eventType();
        Event event = null;
        switch (eventType) {
            default:
                break;
        }
        //@todo : Rahul Agrawal : Since the event type is null here for now no point in firing the event.
        //fireEvent(event);
    }

    @AfterReturning(value = "@annotation(raiseEvent)")
    public void afterMethodProcessing(JoinPoint joinPoint, RaiseEvent raiseEvent) {
        log.debug("RaiseEventProcessor.afterMethodProcessing(): RaiseEvent for {}", raiseEvent);
        EventType eventType = raiseEvent.eventType();
        Event event = null;
        switch (eventType) {
            case ON_PAYMENT_STARTED:
            case ON_PAYMENT_RESPONSE:
                event = new PaymentEvent(raiseEvent);
                break;

            case CUSTOMER_LOGIN:
            case CUSTOMER_REGISTERED:
                event = new CustomerEvent(raiseEvent);
                break;

            case ORDER_CREATED:
            case ORDER_STATUS_UPDATED:
            case ORDER_CANCELED:
            case ORDERITEM_SHIPPING_STATUS_UPDATED:
            case ORDER_PAYMENT_STATUS_UPDATED:
            case ORDERITEM_STATUS_UPDATED:
                event = new OrderEvent(raiseEvent);
                break;

            case CART_CREATED:
            case CART_UPDATED:
                event = new CartEvent(raiseEvent);
                break;
                
            case GOLD_MINE_PLAN_ENROLMENT:
            case GOLD_MINE_PLAN_PAYMENT:
            	event = new GoldMineEvent(raiseEvent);
            default:
                break;
        }
        fireEvent(event);
    }

    private void fireEvent(Event event) {
        //log.debug("RaiseEventProcessor.fireEvent():{}", event);
        if (event != null) {
            MessageContext messageContext = Util.getMessageContext();
            //Map<String, Serializable> messageContextMap = messageContext.getMessageContextMap();
            //event.setMessageContextMap(messageContextMap);
            event.setMessageContext(messageContext);
            eventPublisher.fireEvent(event);
        } else {
            log.warn("Event was null, so no action was taken. Ideally there should not be a null event. ***Needs investigation****");
        }
    }


    /*
        Advice ordering
        What happens when multiple pieces of advice all want to run at the same join point? Spring AOP follows
        the same precedence rules as AspectJ to determine the order of advice execution.
        #########################
        The highest precedence advice runs first "on the way in"
        (so given two pieces of before advice, the one with highest precedence runs first).

        "On the way out" from a join point, the highest precedence advice runs last (so given two
        pieces of after advice, the one with the highest precedence will run second).

        When two pieces of advice defined in different aspects both need to run at the same join point, unless you
        specify otherwise the order of execution is undefined. You can control the order of execution by
        specifying precedence. This is done in the normal Spring way by either implementing the
        org.springframework.core.Ordered interface in the aspect class or annotating it with the
        Order annotation.

        Given two aspects, the aspect returning the lower value from Ordered.getValue() (or the annotation value)
        has the higher precedence.

        When two pieces of advice defined in the same aspect both need to run at the same join point, the
        ordering is undefined (since there is no way to retrieve the declaration order via reflection for
        javac-compiled classes). Consider collapsing such advice methods into one advice method per join point
        in each aspect class, or refactor the pieces of advice into separate aspect classes - which can be ordered at
        the aspect level.
        Introductions
     */

    @Override
    public int getOrder() {
        log.debug("RaiseEventProcessor's AspectOrder={}" , Ordered.LOWEST_PRECEDENCE);
        return Ordered.LOWEST_PRECEDENCE;
        // this means it will run first for the after method processing.
    }
}
