package com.bluestone.app.uniware;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

import com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrder;
import com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem;
import com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 4/20/13
 */
class UniwareSaleOrderParser {
    private static final Logger log = LoggerFactory.getLogger(UniwareSaleOrderParser.class);

    LinkedList<OrderItemStatusDetail> execute(GetSaleOrderResponseSaleOrder responseSaleOrder, String orderId) {
        log.debug("UniwareSaleOrderParser.execute():OrderId=[{}]", orderId);
        LinkedList<OrderItemStatusDetail> orderItemStatusDetails = parseOrderItems(orderId, responseSaleOrder.getSaleOrderItems());
        GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[] shippingPackages = responseSaleOrder.getShippingPackages();
        HashMap<String, GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage> shippingPackageHashMap = collectShippingPackages(shippingPackages);
        for (int i = 0; i < orderItemStatusDetails.size(); i++) {
            OrderItemStatusDetail eachItemDetail = orderItemStatusDetails.get(i);
            String shippingPackageCode = eachItemDetail.getShippingPackageCode();
            GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage shippingPackage = shippingPackageHashMap.get(shippingPackageCode);
            if (shippingPackage != null) {
                OrderItemStatusDetail.ShippingDetails shippingDetails = eachItemDetail.createShippingDetails();
                populateShippingDetails(shippingPackage, shippingDetails);
            }
            log.debug("UniwareSaleOrderParser.execute(): Each Order Item  fetched from uniware=\n{}", eachItemDetail);
        }
        return orderItemStatusDetails;
    }

    private void populateShippingDetails(GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage shippingPackage,
                                         OrderItemStatusDetail.ShippingDetails shippingDetails) {
        log.debug("UniwareSaleOrderParser.populateShippingDetails()");
        String shippingProvider = shippingPackage.getShippingProvider();
        shippingDetails.setShippingProvider(shippingProvider);

        String statusCode = shippingPackage.getStatusCode();
        shippingDetails.setStatusCode(statusCode);

        String trackingNumber = shippingPackage.getTrackingNumber();
        shippingDetails.setTrackingNumber(trackingNumber);

        String shippingPackageType = shippingPackage.getShippingPackageType();
        shippingDetails.setShippingPackageType(shippingPackageType);

        Calendar createdOn = shippingPackage.getCreatedOn();
        if (createdOn != null) {
            shippingDetails.setCreatedOn(createdOn);
        }

        Calendar dispatchedOn = shippingPackage.getDispatchedOn();
        if (dispatchedOn != null) {
            shippingDetails.setDispatchedOn(dispatchedOn);
        }

        Calendar deliveredOn = shippingPackage.getDeliveredOn();
        if (deliveredOn != null) {
            shippingDetails.setDeliveredOn(deliveredOn);
        }

    }

    private HashMap<String, GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage> collectShippingPackages(GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[] shippingPackages) {
        log.debug("UniwareSaleOrderParser.processShippingPackages()");
        HashMap<String, GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage> map = new HashMap<String, GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage>();
        if (shippingPackages != null) {
            for (int i = 0; i < shippingPackages.length; i++) {
                GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage eachPackage = shippingPackages[i];
                String shippingPackageCode = eachPackage.getShipmentCode();
                map.put(shippingPackageCode, eachPackage);
            }
        }
        return map;
    }

    private LinkedList<OrderItemStatusDetail> parseOrderItems(String orderId, GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems) {
        log.debug("UniwareSaleOrderParser.parseOrderItems() for OrderId={}", orderId);
        LinkedList<OrderItemStatusDetail> list = new LinkedList<OrderItemStatusDetail>();
        for (int i = 0; i < saleOrderItems.length; i++) {
            GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem eachUniwareItem = saleOrderItems[i];
            String orderItemCode = eachUniwareItem.getCode();
            String shippingPackageCode = eachUniwareItem.getShippingPackageCode();
            String orderItemStatusCode = eachUniwareItem.getStatusCode();
            boolean cancellable = eachUniwareItem.isCancellable();
            //OrderItemStatusDetail orderItemDetail = new OrderItemStatusDetail(orderItemCode, cancellable, orderItemStatusCode, shippingPackageCode);
            String packetNumber = null;
            if (eachUniwareItem.getPacketNumber() != null) {
                packetNumber = eachUniwareItem.getPacketNumber().toString();
            }

            OrderItemStatusDetail eachBSItem = new OrderItemStatusDetail();
            eachBSItem.setOrderId(orderId);
            eachBSItem.setOrderItemCode(orderItemCode);
            eachBSItem.setShippingPackageCode(shippingPackageCode);
            eachBSItem.setOrderItemStatusCode(orderItemStatusCode);
            //eachBSItem.setOrderStatusCode(eachUniwareItem.get);// we don;t get this via web service api calls
            eachBSItem.setCancellable(cancellable);
            if (packetNumber != null) {
                eachBSItem.setPacketNumber(packetNumber);
            }
            list.add(eachBSItem);
        }
        return list;
    }
}
