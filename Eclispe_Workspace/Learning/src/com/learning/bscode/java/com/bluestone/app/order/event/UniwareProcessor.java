package com.bluestone.app.order.event;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.integration.uniware.QService;
import com.bluestone.app.order.model.Order;

/**
 * @author Rahul Agrawal
 *         Date: 3/26/13
 */
@Service
public class UniwareProcessor {

    private static final Logger log = LoggerFactory.getLogger(UniwareProcessor.class);

    @Autowired
    private QService qService;

    public void execute(Order order, Map<String, Serializable> messageContextMap) {
        final String paymentTransactionStatus = (String) messageContextMap.get(MessageContext.ORDER_PAYMENT_TRANSACTION_STATUS);
        log.debug("OrderEventListener.pushToUniware(): OrderId={}, PaymentTransactionStatus={}", order.getId(), paymentTransactionStatus);
        if (OrderFilter.isEligibleForUniware(order, paymentTransactionStatus)) {
            // eligible for uniware push : On the consumer side after the transaction is committed, we do a check on order status also.
            qService.send(order.getId());
        } else {
            log.info("OrderEventListener.pushToUniware(): ***** Do Not enqueue the message to Uniware as the OrderId={} ***", order.getId(), paymentTransactionStatus);
        }
    }
}
