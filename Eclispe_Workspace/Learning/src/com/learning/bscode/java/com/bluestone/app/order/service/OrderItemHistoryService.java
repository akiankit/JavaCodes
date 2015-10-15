package com.bluestone.app.order.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.UserService;
import com.bluestone.app.account.model.User;
import com.bluestone.app.order.dao.OrderDao;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.OrderItemHistory;
import com.bluestone.app.order.model.OrderItemStatus;

/**
 * @author Rahul Agrawal
 *         Date: 4/12/13
 */
@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class OrderItemHistoryService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemHistoryService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private OrderDao orderDao;


    public OrderItemHistory createOrderItemHistory(String orderItemStatus, String userEmail, OrderItem orderItem) {
        log.info("OrderItemHistoryService.createOrderItemHistory() for OrderItemId=[{}] OrderItem's new Status={}", orderItem.getId(), orderItemStatus);
        OrderItemHistory orderItemHistory = new OrderItemHistory();
        if (StringUtils.isNotBlank(userEmail)) {
            User user = userService.getUserByEmailId(userEmail);
            orderItemHistory.setUser(user);
        }
        OrderItemStatus itemStatus = orderDao.getOrderItemStatus(orderItemStatus);
        orderItemHistory.setOrderItemStatus(itemStatus);

        orderItemHistory.setOrderItem(orderItem);
        orderDao.create(orderItemHistory);
        return orderItemHistory;
    }

}
