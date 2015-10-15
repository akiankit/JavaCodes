package com.bluestone.app.admin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.order.event.CustomerNotification;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.service.OrderService;

@Service
public class AdminOrderMailService {
    
    private static final Logger log = LoggerFactory.getLogger(AdminOrderMailService.class);
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CustomerNotification customerNotification;

    @Transactional(readOnly=true)
    public void reSendOrderMail(long orderId) throws Exception {
        Order order = orderService.getOrder(orderId);
        if(order == null) {
            throw new Exception("Order Not found");
        }
        List<OrderItem> orderItems = order.getOrderItems();
        log.debug("Order Items : {}" , orderItems);
      
        customerNotification.sendNewOrderEmail(order);
/*        MessageContext messageContext = Util.getMessageContext();
        messageContext.put(MessageContext.ORDERID, orderId);
*/    }
    
}
