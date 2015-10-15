package com.bluestone.app.order.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bluestone.app.uniware.OrderItemStatusDetail;
import com.bluestone.app.uniware.OrderItemStatusDetail.ShippingDetails;

/**
 * @author Rahul Agrawal
 *         Date: 12/1/12
 */
public class ShippingPackageHelper {

    public static Map<String, ShippingDetails> getShippingDetailsMap(List<OrderItemStatusDetail> itemStatusDetails) {
        HashMap<String, ShippingDetails> map = new HashMap<String, ShippingDetails>();
        for (int i = 0; i < itemStatusDetails.size(); i++) {
            OrderItemStatusDetail orderItemStatusDetail = itemStatusDetails.get(i);
            if(orderItemStatusDetail.getShippingPackageCode() !=null) {
                String shippingPackageCode = orderItemStatusDetail.getShippingPackageCode();
                map.put(shippingPackageCode, orderItemStatusDetail.getShippingDetails());
            }
        }
        return map;
    }
    
    public static List<String> getOrderItemDetailsList(List<OrderItemStatusDetail> itemStatusDetails, String eachShippingPackageCode) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < itemStatusDetails.size(); i++) {
            OrderItemStatusDetail orderItemStatusDetail = itemStatusDetails.get(i);
            if(orderItemStatusDetail.getShippingPackageCode() !=null) {
                String shippingPackageCode = orderItemStatusDetail.getShippingPackageCode();
                if(shippingPackageCode.equals(eachShippingPackageCode)) {
                    list.add(orderItemStatusDetail.getUniwareOrderItemCode());
                }
            }
        }
        return list;
    }
}
