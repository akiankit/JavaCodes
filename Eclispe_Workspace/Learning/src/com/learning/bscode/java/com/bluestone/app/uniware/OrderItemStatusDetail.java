package com.bluestone.app.uniware;

import java.util.Calendar;

import com.bluestone.app.core.util.DateTimeUtil;

/**
 * @author Rahul Agrawal
 *         Date: 10/23/12
 */
public class OrderItemStatusDetail implements Comparable<OrderItemStatusDetail> {
    private String orderId;
    private String orderItemCode;
    private String shippingPackageCode;
    private String orderItemStatusCode;
    private String orderStatusCode; // this only comes via export and not via webservice api calls
    private boolean isCancellable;
    private boolean onHold;
    private String packetNumber;
    private ShippingDetails shippingDetails;


    /*OrderItemStatusDetail(String orderItemCode, boolean cancellable, String orderItemStatusCode, String shippingPackageCode) {
        this.orderItemCode = orderItemCode;
        this.isCancellable = cancellable;
        this.orderItemStatusCode = orderItemStatusCode;
        this.shippingPackageCode = shippingPackageCode;
        this.setOnHold(false);
    }*/

    public OrderItemStatusDetail() {
        onHold = false;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public String getUniwareOrderItemCode() {
        return getOrderItemCode();
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public String getShippingPackageCode() {
        return shippingPackageCode;
    }

    public void setShippingPackageCode(String shippingPackageCode) {
        this.shippingPackageCode = shippingPackageCode;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public void setCancellable(boolean isCancellable) {
        this.isCancellable = isCancellable;
    }

    public boolean isOnHold() {
        return onHold;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public ShippingDetails getShippingDetails() {
        return shippingDetails;
    }

    public void setShippingDetails(ShippingDetails shippingDetails) {
        this.shippingDetails = shippingDetails;
    }

    public ShippingDetails createShippingDetails() {
        shippingDetails = new ShippingDetails();
        return shippingDetails;
    }

    @Override
    public int compareTo(OrderItemStatusDetail obj2) {
        if (this.orderId == obj2.getOrderId()) {
            return 0;
        }
        return (orderId.compareTo(obj2.getOrderId()));
    }


    public class ShippingDetails {
        private String shippingProvider;
        private String statusCode;
        private String trackingNumber;
        private String shippingPackageType;
        private Calendar createdOn;
        private Calendar dispatchedOn;
        private Calendar deliveredOn;
        private String shippingMethod;

        private ShippingDetails() {
        }

        public String getShippingProvider() {
            return shippingProvider;
        }

        public void setShippingProvider(String shippingProvider) {
            this.shippingProvider = shippingProvider;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getTrackingNumber() {
            return trackingNumber;
        }

        public void setTrackingNumber(String trackingNumber) {
            this.trackingNumber = trackingNumber;
        }

        public String getShippingPackageType() {
            return shippingPackageType;
        }

        public void setShippingPackageType(String shippingPackageType) {
            this.shippingPackageType = shippingPackageType;
        }

        public Calendar getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Calendar createdOn) {
            this.createdOn = createdOn;
        }

        public Calendar getDispatchedOn() {
            return dispatchedOn;
        }

        public void setDispatchedOn(Calendar dispatchedOn) {
            this.dispatchedOn = dispatchedOn;
        }

        public Calendar getDeliveredOn() {
            return deliveredOn;
        }

        public void setDeliveredOn(Calendar deliveredOn) {
            this.deliveredOn = deliveredOn;
        }


        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("\nShippingDetails for the item:");
            if (createdOn != null) {
                sb.append("\n\t Created On=").append(DateTimeUtil.getSimpleDateAndTime(createdOn.getTime())).append("\n");
            }

            if (deliveredOn != null) {
                sb.append("\t Delivered On=").append(DateTimeUtil.getSimpleDateAndTime(deliveredOn.getTime())).append("\n");
            }
            if (dispatchedOn != null) {
                sb.append("\t Dispatched On=").append(DateTimeUtil.getSimpleDateAndTime(dispatchedOn.getTime())).append("\n");
            }
            sb.append("\t Shipping Package Type=").append(shippingPackageType).append("\n");
            sb.append("\t Shipping Provider=").append(shippingProvider).append("\n");
            sb.append("\t Status Code=").append(statusCode).append("\n");
            sb.append("\t Tracking Number=").append(trackingNumber).append("\n");
            sb.append("\t ShippingMethod=").append(shippingMethod).append("\n");
            return sb.toString();
        }

        public String getShippingMethod() {
            return shippingMethod;
        }

        public void setShippingMethod(String shippingMethod) {
            this.shippingMethod = shippingMethod;
        }
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public void setPacketNumber(String packetNumber) {
        this.packetNumber = packetNumber;
    }

    public String getPacketNumber() {
        return packetNumber;
    }

    public String getOrderItemStatusCode() {
        return orderItemStatusCode;
    }

    public void setOrderItemStatusCode(String orderItemStatusCode) {
        this.orderItemStatusCode = orderItemStatusCode;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OrderItemStatusDetail");
        sb.append("\n\t OrderId=").append(orderId).append("\n");
        sb.append("\t OrderStatusCode=").append(orderStatusCode).append("\n");

        sb.append("\t OrderItemCode=").append(orderItemCode).append("\n");
        sb.append("\t OrderItemStatusCode=").append(orderItemStatusCode).append("\n");
        sb.append("\t isCancellable=").append(isCancellable).append("\n");
        sb.append("\t onHold=").append(onHold).append("\n");

        sb.append("\t packetNumber=").append(packetNumber).append("\n");
        sb.append("\t shippingPackageCode=").append(shippingPackageCode);
        sb.append("\t ").append(shippingDetails).append("\n");
        return sb.toString();
    }
}
