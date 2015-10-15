package com.bluestone.app.integration.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.OrderItemHistory;
import com.bluestone.app.order.model.OrderItemStatus;

@Component
public class CRMService {

    private static final Logger log = LoggerFactory.getLogger(CRMService.class);

    @Autowired
    private CrmMessageProducer crmMessageProducer;

    public void send(CRM_EVENT crmEvent, Map<String, Serializable> messageContextMap) {
        log.debug("CRMService.send() for event = {}", crmEvent.name());
        crmMessageProducer.queueMessage(new CRMMessage(crmEvent, messageContextMap));
    }

    public List<Map<String, Object>> getOrderItemsForCRM(Order order) {
        log.info("CRMService.getOrderItemsForCRM(): OrderId={}", order.getId());

        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        List<OrderItem> orderItems = order.getOrderItems();

        for (OrderItem orderItem : orderItems) {
            CartItem cartItem = orderItem.getCartItem();
            Product product = cartItem.getProduct();
            Map<String, Object> singleProduct = new HashMap<String, Object>();
            singleProduct.put("ps_orderitem_id", orderItem.getId());
            singleProduct.put("quantity", 1); //each orderItem will have quantity 1. 
            singleProduct.put("price", orderItem.getPrice());
            singleProduct.put("supplier_reference", cartItem.getSkuCodeWithSize());
            singleProduct.put("category", product.getCategory());
            singleProduct.put("name", product.getName());
            OrderItemHistory latestOrderItemHistory = orderItem.getLatestOrderItemHistory();
            String status = "Created_Default";
            if (latestOrderItemHistory != null) {
                OrderItemStatus orderItemStatus = latestOrderItemHistory.getOrderItemStatus();
                if (orderItemStatus != null) {
                    status = orderItemStatus.getStatusName();
                }
            }
            singleProduct.put("status", status);
            productList.add(singleProduct);
        }
        return productList;
    }

}
