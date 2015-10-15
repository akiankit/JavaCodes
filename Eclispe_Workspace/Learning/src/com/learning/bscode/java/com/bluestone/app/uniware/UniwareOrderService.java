package com.bluestone.app.uniware;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.unicommerce.uniware.services.CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem;
import com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrder;
import com.unicommerce.uniware.services.SaleOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;

/**
 * @author Rahul Agrawal
 *         Date: 10/23/12
 */
@Service
public class UniwareOrderService {

    private static final Logger log = LoggerFactory.getLogger(UniwareOrderService.class);

    @Autowired
    private WebService webService;

    public LinkedList<OrderItemStatusDetail> fetchOrderDetails(String orderId) throws RemoteException {
        log.info("UniwareOrderService.fetchOrderDetails() for OrderId={}", orderId);
        LinkedList<OrderItemStatusDetail> orderItemStatusDetails = new LinkedList<OrderItemStatusDetail>();

        GetSaleOrderResponseSaleOrder responseSaleOrder = webService.getSaleOrder(orderId);
        if (responseSaleOrder == null) {
            log.info("UniwareOrderService.fetchOrderDetails() for OrderId={} . Did not get any result.", orderId);
        } else {
            orderItemStatusDetails = (new UniwareSaleOrderParser()).execute(responseSaleOrder, orderId);
        }
        return orderItemStatusDetails;
    }

    public boolean createASaleOrder(Order order) throws RemoteException {
        log.debug("UniwareOrderService.createASaleOrder() for OrderId={}", order.getId());
        boolean isSuccess = false;
        SaleOrder saleOrder = SaleOrderFactory.create(order);
        isSuccess = webService.createNewSaleOrder(saleOrder);
        return isSuccess;
    }

    public boolean cancelOrder(String orderId, List<OrderItem> orderItemId) throws RemoteException {
        Assert.isTrue(orderItemId != null, "OrderItemId that needs to be cancelled is missing.");
        ArrayList<CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem> saleOrderItems = new ArrayList<CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem>(orderItemId.size());

        for (OrderItem eachItem : orderItemId) {
            log.info("UniwareOrderService.cancelOrder(): OrderId={} , orderItemId={} , UniwareOrderItemID={}",
                     orderId, eachItem.getId(), eachItem.getUniwareOrderItemId());
            String uniwareOrderItemId = eachItem.getUniwareOrderItemId(); // need to have this instead of orderItemId because of prestashop world.
            saleOrderItems.add(new CancelSaleOrderRequestSaleOrderSaleOrderItemsSaleOrderItem(uniwareOrderItemId));
        }
        return webService.cancelSaleOrder(orderId, saleOrderItems);
    }
}
