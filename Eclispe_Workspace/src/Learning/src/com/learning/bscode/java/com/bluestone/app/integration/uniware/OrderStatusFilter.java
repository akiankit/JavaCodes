package com.bluestone.app.integration.uniware;


import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderHistory;
import com.bluestone.app.order.model.UniwareStatus;

/**
 * @author Rahul Agrawal
 *         Date: 4/20/13
 */

@Service
class OrderStatusFilter {
    private static final Logger log = LoggerFactory.getLogger(OrderStatusFilter.class);

    boolean execute(Order order) {
        boolean result = false;
        String latestStatusName = order.getStatus().getStatusName();
        log.info("OrderStatusFilter.isEligibleForUniware(): OrderId=[{}] with latest Status=[{}]", order.getId(), latestStatusName);

        if (UniwareStatus.ORDER_STATUS.PROCESSING.getValue().equals(latestStatusName)) {
            return true;
        }

        final List<OrderHistory> orderHistory = order.getOrderhistory(); // sorts by descending
        if (orderHistory.size() == 2) {
            final String processing = orderHistory.get(1).getOrderStatus().getStatusName();
            if (UniwareStatus.ORDER_STATUS.PROCESSING.getValue().equals(processing)
                && (UniwareStatus.ORDER_STATUS.CREATED.getValue().equals(latestStatusName))) {
                final Date processedOn = orderHistory.get(1).getCreatedAt();
                final Date createdOn = order.getLatestOrderHistory().getCreatedAt();
                log.info("** This is a case of System Date time issue on tomcat boxes where timestamp of status[{}] is=[{}] Higher than that of [{}]=[{}]",
                         latestStatusName, DateTimeUtil.getSimpleDateAndTime(createdOn),
                         processing, DateTimeUtil.getSimpleDateAndTime(processedOn));
                result = true;
            } else {
                log.error("Error: OrderStatusFilter.isEligibleForUniware(): We should not have landed here for the OrderI=[{}]. **** Please investigate asap ", order.getId());
            }
        } else {
            log.error("Error: OrderStatusFilter.isEligibleForUniware(): We should not have landed here for the OrderI=[{}]. **** Please investigate asap ", order.getId());
        }
        return result;
    }
}
