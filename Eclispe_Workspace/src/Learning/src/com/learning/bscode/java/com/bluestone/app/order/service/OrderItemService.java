package com.bluestone.app.order.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.order.dao.OrderDao;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.OrderItemHistory;
import com.bluestone.app.order.model.OrderItemStatus;
import com.bluestone.app.shipping.model.Address;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    //private UserService         userService;

    @Autowired
    private OrderItemHistoryService orderItemHistoryService;

    @Autowired
    private OrderDao orderDao;

    @RaiseEvent(eventType = EventType.ORDERITEM_STATUS_UPDATED)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderItem updateOrderItemStatus(OrderItem orderItem, String newStatus, boolean isCancellable, String userEmail) {
        log.info("OrderItemService.updateOrderItemStatus(): [{}] \nNew Status={} , isCancellable=[{}]  email=[{}]",
                 orderItem, newStatus, isCancellable, userEmail);

        OrderItemStatus orderItemStatusInDB = orderItem.getStatus();
        log.info("OrderItemService.updateOrderItemStatus():OrderItemId=[{}] OrderItemStatus in DB=[{}]", orderItem.getId(), orderItemStatusInDB.getStatusName());
        MessageContext messageContext = Util.getMessageContext();
        messageContext.addOrderItemIds(orderItem.getId());
        // additional null check because of prestashop orders, for the first , time we will have no order item history
        OrderItem updatedOrderItem = null;
        if ((orderItemStatusInDB != null) && (orderItemStatusInDB.getStatusName().equals(newStatus))) {
            log.info("OrderService.updateOrderItemStatus(): No need to update the status for the OrderItemId=[{}]: Existing status=[{}] equals New Status requested=[{}]",
                     orderItem, newStatus, newStatus);
            updatedOrderItem = orderItem;
        } else {
            orderItem.setCancellable(isCancellable);
            OrderItemHistory orderItemHistory = orderItemHistoryService.createOrderItemHistory(newStatus, userEmail, orderItem);
            updatedOrderItem = orderDao.update(orderItem);
            messageContext.addOrderItemHistoryId(orderItemHistory.getId());
        }
        return updatedOrderItem;
    }

    /*private OrderItemHistory createOrderItemHistory(String orderItemStatus, String userEmail, OrderItem orderItem) {
        log.info("OrderService.createOrderItemHistory() for OrderItemId=[{}] OrderItem's new Status={}",orderItem.getId(), orderItemStatus);
        OrderItemHistory orderItemHistory = new OrderItemHistory();
        if (StringUtils.isNotBlank(userEmail)) {
            User user = userService.getUserByEmailId(userEmail);
            orderItemHistory.setUser(user);
        }
        OrderItemStatus orderItemStatusFromDb = orderDao.getOrderItemStatus(orderItemStatus);
        orderItemHistory.setOrderItemStatus(orderItemStatusFromDb);
        
        orderItemHistory.setOrderItem(orderItem);
        orderDao.create(orderItemHistory);
        return orderItemHistory;
    }*/

    public OrderItem getOrderItem(Long orderItemId) {
        return orderDao.find(OrderItem.class, orderItemId, true);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderItem updateOrderItemAddress(OrderItem orderItem, Address address) {
        // @todo P1 : Rahul Agrawal :
        // fetch the latest state of the order item, only it is unfullfillable
        // and no shipping packets attached, we allow an address up
        orderItem.setShippingAddress(address);
        return orderDao.update(orderItem);
        // make a call to uniware for the address update
        // send an email to the customer
    }

    public OrderItemHistory getOrderItemHistory(Long orderItemHistoryId) {
        return orderDao.findAny(OrderItemHistory.class, orderItemHistoryId);
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderDao.getOrderItems(orderId);
    }

}
