package com.bluestone.app.order.service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.order.dao.OrderDao;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.ShippingItem;
import com.bluestone.app.order.model.ShippingPackageHelper;
import com.bluestone.app.order.model.ShippingPacket;
import com.bluestone.app.order.model.ShippingPacketHistory;
import com.bluestone.app.order.model.ShippingPacketStatus;
import com.bluestone.app.order.model.UniwareStatus;
import com.bluestone.app.uniware.OrderItemStatusDetail;
import com.bluestone.app.uniware.OrderItemStatusDetail.ShippingDetails;
import com.bluestone.app.uniware.UniwareOrderService;

/**
 * @author Rahul Agrawal
 *         Date: 11/22/12
 */

//@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
@Service
public class UniwareSynchroniser {

    private static final Logger log = LoggerFactory.getLogger(UniwareSynchroniser.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UniwareOrderService uniwareOrderService;

    @Autowired
    private OrderItemService orderItemService;
    
    @Autowired
    private OrderShipmentService orderShipmentService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void execute(String orderId, final String orderStateInDatabase) {
        log.info("UniwareSynchroniser.execute(): OrderId=[{}] has Status=[{}] in our database.", orderId, orderStateInDatabase);
        List<OrderItemStatusDetail> itemStatusDetails = fetchOrderDetailsFromUniware(orderId, orderStateInDatabase);
        updateDatabase(itemStatusDetails);
    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateDatabase(List<OrderItemStatusDetail> orderItemStatusDetails) {
        for (OrderItemStatusDetail eachItemDetail : orderItemStatusDetails)
            try {
                log.info("UniwareSynchroniser.updateDatabase() for OrderId=[{}] UniwareOrderItemId=[{}]", eachItemDetail.getOrderId(),
                         eachItemDetail.getUniwareOrderItemCode());
                String statusInUniware = eachItemDetail.getOrderItemStatusCode();
                UniwareStatus.ORDER_ITEM_STATUS statusFromUniware = UniwareStatus.ORDER_ITEM_STATUS.valueOf(UniwareStatus.ORDER_ITEM_STATUS.class,
                                                                                                            statusInUniware.toUpperCase());
                OrderItem orderItem = getBlueStoneOrderItem(eachItemDetail.getUniwareOrderItemCode());
                if (orderItem != null) {
                    OrderItem updatedOrderItem = orderItemService.updateOrderItemStatus(orderItem,
                                                                                        statusFromUniware.getValue(),
                                                                                        eachItemDetail.isCancellable(),
                                                                                        null);
                } else {
                    log.warn("Error: UniwareSynchroniser.updateDatabase(): *** Our database does not has the OrderId=[{}] UniwareOrderItem ID ={} ",
                             eachItemDetail.getOrderId(), eachItemDetail.getOrderItemCode());
                }
            } catch (IllegalArgumentException e) {
                String incomingStatus = "";
                if (eachItemDetail != null) {
                    incomingStatus = eachItemDetail.getOrderItemStatusCode();
                }
                log.error("UniwareSynchroniser.updateDatabase(): Could not update our Database because Status sent by Uniware={}. It does not match with the list of expected statuses", incomingStatus, e);
                throw new RuntimeException(
                        "OrderItem Status received from uniware does not match with the expected list of statuses. Status received="
                        + incomingStatus);
            }
    }


    private List<OrderItemStatusDetail> fetchOrderDetailsFromUniware(String orderId, final String orderStateInDatabase) {
        log.info("UniwareSynchroniser.fetchOrderDetailsFromUniware() for OrderId=[{}] current status in db=[{}]", orderId, orderStateInDatabase);
        List<OrderItemStatusDetail> itemDetails = new LinkedList<OrderItemStatusDetail>();
        boolean notYetSentToUniware = UniwareStatus.ORDER_STATUS.CREATED.getValue().equals(orderStateInDatabase);
        if (notYetSentToUniware) {
            log.info("Status of OrderId=[{}] is [{}]: This order has NOT yet been created in Uniware, hence no need to hit Uniware.",
                      orderId, orderStateInDatabase);
        } else {
            try {
                itemDetails = uniwareOrderService.fetchOrderDetails(orderId);
            } catch (Exception e) {
                log.error("Error while fetching the order details for orderId={} from Uniware. See the log file for error {} details", orderId,
                          e.toString(), Throwables.getRootCause(e));
            }
        }
        return itemDetails;
    }

    private OrderItem getBlueStoneOrderItem(String uniwareOrderItemId) {
        OrderItem orderItem = null;
        try {
            orderItem = orderDao.getOrderItemIdFrmUniwareItemIdCode(uniwareOrderItemId);
            log.info("UniwareSynchroniser.getBlueStoneOrderItem() for UniwareOrderItemCode={} gives OrderItemId={}", uniwareOrderItemId, orderItem.getId());
        } catch (Exception exception) {
            log.error("Error: UniwareSynchroniser.getBlueStoneOrderItem(): Failed to get the order item id in our DB for uniwareOrderItemId=[{}] in uniware. Reason={}",
                      uniwareOrderItemId, exception.getLocalizedMessage(), Throwables.getRootCause(exception));
        }
        return orderItem;
    }
    
    /*private void updateShippingDetails(List<OrderItemStatusDetail> orderItemStatusDetails) {
        Map<String, ShippingDetails> shippingCodeVsShippingDetails = ShippingPackageHelper.getShippingDetailsMap(orderItemStatusDetails);
        Set<String> shippingPackageCodes = shippingCodeVsShippingDetails.keySet();
        
        for (String eachShippingPackageCode : shippingPackageCodes) {
            ShippingDetails shippingDetails = shippingCodeVsShippingDetails.get(eachShippingPackageCode);
            ShippingPacket shippingPacket = orderShipmentService.getShippingPacket(eachShippingPackageCode);
            if(shippingPacket == null) {
                shippingPacket = new ShippingPacket(eachShippingPackageCode, shippingDetails);
                orderShipmentService.createShippingPacket(shippingPacket);
            } else {
                String statusCode = shippingDetails.getStatusCode();
                ShippingPacketHistory latestShippingPacketHistory = shippingPacket.getLatestShippingPacketHistory();
                
                if(latestShippingPacketHistory != null) {
                    String statusName = latestShippingPacketHistory.getShippingPacketStatus().getStatusName();
                    if(statusName.equals(statusCode)) {
                        log.debug("No need to update shipping packet as status {} from uniware is same as exisiting status {} in db", statusCode, statusName); 
                    } else {
                        createShippingPacketHistory(shippingPacket, statusCode);
                        updateShippingPacket(shippingDetails, shippingPacket);
                    }
                } else {
                    createShippingPacketHistory(shippingPacket, statusCode);
                    updateShippingPacket(shippingDetails, shippingPacket);
                }
            }
            List<String> orderItemDetailsList = ShippingPackageHelper.getOrderItemDetailsList(orderItemStatusDetails, eachShippingPackageCode);
            for (String eachOrderItem : orderItemDetailsList) {
                OrderItem orderItem = getBlueStoneOrderItem(eachOrderItem);
                if(eachOrderItem !=null) {
                    ShippingItem shippingItem = new ShippingItem();
                    shippingItem.setShippingPacket(shippingPacket);
                    shippingItem.setOrderItem(orderItem);
                    orderShipmentService.createShippingItem(shippingItem);
                }
            }
        }
    }


    private void createShippingPacketHistory(ShippingPacket shippingPacket, String statusCode) {
        ShippingPacketHistory shippingPacketHistory = new ShippingPacketHistory();
        shippingPacketHistory.setShippingPacket(shippingPacket);
        ShippingPacketStatus shippingPacketStatus = orderShipmentService.getShippingPacketStatus(statusCode);
        if(shippingPacketStatus == null) {
            shippingPacketStatus  =  orderShipmentService.createShippingPacketStatus(statusCode);
        }
        shippingPacketHistory.setShippingPacketStatus(shippingPacketStatus);
        orderShipmentService.createShippingPacketHistory(shippingPacketHistory);
    }


    private void updateShippingPacket(ShippingDetails shippingDetails, ShippingPacket shippingPacket) {
        Calendar createdOn = shippingDetails.getCreatedOn();
        if(createdOn != null) {
            shippingPacket.setPacketCreationDate(createdOn.getTime());
        }
        
        Calendar deliveredOn = shippingDetails.getDeliveredOn();
        if(deliveredOn != null) {
            shippingPacket.setPacketDeliveredDate(deliveredOn.getTime());
        }
        
        Calendar dispatchedOn = shippingDetails.getDispatchedOn();
        if(dispatchedOn != null) {
            shippingPacket.setPacketDispatchDate(dispatchedOn.getTime());
        }
        
        shippingPacket.setShippingPackageType(shippingDetails.getShippingPackageType());
        shippingPacket.setShippingProvider(shippingDetails.getShippingProvider());
        shippingPacket.setTrackingNumber(shippingDetails.getTrackingNumber());
        orderShipmentService.updateShippingPacket(shippingPacket);
    }
*/

}
