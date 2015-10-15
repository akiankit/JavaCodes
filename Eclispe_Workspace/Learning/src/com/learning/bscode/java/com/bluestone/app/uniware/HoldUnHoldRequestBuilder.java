package com.bluestone.app.uniware;

import java.util.List;

import com.unicommerce.uniware.services.HoldSaleOrderItemsRequest;
import com.unicommerce.uniware.services.HoldSaleOrderItemsRequestSaleOrder;
import com.unicommerce.uniware.services.HoldSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem;
import com.unicommerce.uniware.services.HoldSaleOrderRequest;
import com.unicommerce.uniware.services.HoldSaleOrderRequestSaleOrder;
import com.unicommerce.uniware.services.UnholdSaleOrderItemsRequest;
import com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrder;
import com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem;
import com.unicommerce.uniware.services.UnholdSaleOrderRequest;
import com.unicommerce.uniware.services.UnholdSaleOrderRequestSaleOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 2/24/13
 */
class HoldUnHoldRequestBuilder {

    private static final Logger log = LoggerFactory.getLogger(HoldUnHoldRequestBuilder.class);

    static HoldSaleOrderItemsRequest getHoldItemsRequest(String bsOrderId, List<String> orderItemIds) {
        log.info("HoldUnHoldRequestBuilder.getHoldItemsRequest(): OrderId[{}] , OrderItemIds={}", bsOrderId, orderItemIds);
        HoldSaleOrderItemsRequestSaleOrder saleOrder = new HoldSaleOrderItemsRequestSaleOrder();
        saleOrder.setCode(bsOrderId);
        HoldSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems =
                new HoldSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[orderItemIds.size()];

        for (int i = 0; i < orderItemIds.size(); i++) {
            String bsItemId = orderItemIds.get(i);
            saleOrderItems[i] = new HoldSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem(bsItemId);
        }

        saleOrder.setSaleOrderItems(saleOrderItems);
        HoldSaleOrderItemsRequest holdSaleOrderItemsRequest = new HoldSaleOrderItemsRequest(saleOrder);
        return holdSaleOrderItemsRequest;
    }

    static UnholdSaleOrderItemsRequest getUnHoldItemsRequest(String bsOrderId, List<String> orderItemIds) {
        log.info("HoldUnHoldRequestBuilder.getUnHoldItemsRequest(): OrderId[{}] , OrderItemIds={}", bsOrderId, orderItemIds);
        UnholdSaleOrderItemsRequestSaleOrder request = new UnholdSaleOrderItemsRequestSaleOrder();
        request.setCode(bsOrderId);

        UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems
                = new UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[orderItemIds.size()];
        for (int i = 0; i < orderItemIds.size(); i++) {
            String eachItemId = orderItemIds.get(i);
            saleOrderItems[i].setCode(eachItemId);
        }
        request.setSaleOrderItems(saleOrderItems);

        UnholdSaleOrderItemsRequest unholdSaleOrderItemsRequest = new UnholdSaleOrderItemsRequest(request);
        return unholdSaleOrderItemsRequest;
    }

    static HoldSaleOrderRequest getHoldOrderRequest(String bsOrderId) {
        log.info("HoldUnHoldRequestBuilder.getHoldOrderRequest(): OrderId=[{}]", bsOrderId);
        HoldSaleOrderRequestSaleOrder requestSaleOrder = new HoldSaleOrderRequestSaleOrder(bsOrderId);
        HoldSaleOrderRequest request = new HoldSaleOrderRequest(requestSaleOrder);
        return request;
    }

    static UnholdSaleOrderRequest getUnHoldOrderRequest(String bsOrderId) {
        log.info("HoldUnHoldRequestBuilder.getUnHoldOrderRequest(): OrderId=[{}]", bsOrderId);
        return new UnholdSaleOrderRequest(new UnholdSaleOrderRequestSaleOrder(bsOrderId));
    }
}
