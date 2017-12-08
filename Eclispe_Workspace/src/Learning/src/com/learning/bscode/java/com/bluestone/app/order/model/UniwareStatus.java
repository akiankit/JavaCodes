package com.bluestone.app.order.model;

/**
 * @author Rahul Agrawal
 *         Date: 10/27/12
 */
public class UniwareStatus {
    // Isolated the status out here, so that it is easy to deal. Once we are out of uniware, we can renae this class.

    public enum ORDER_ITEM_STATUS {
        CREATED("Created"),
        FULFILLABLE("Fulfillable"),
        UNFULFILLABLE("Unfulfillable"),
        CANCELLED("Cancelled"),
        DISPATCHED("Dispatched"),
        DELIVERED("Delivered"),
        REPLACED("Replaced"),
        RESHIPPED("Reshipped"),
        UNABLE_TO_PURCHASE("Unable_To_Purchase"),
        ALTERNATE_SUGGESTED("Alternate_Suggested"),
        ALTERNATE_ACCEPTED("Alternate_Accepted");

        final private String value;

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }

        private ORDER_ITEM_STATUS(String value) {
            this.value = value;
        }
    }

    public enum SHIPPING_PACKAGE_STATUS {
        CREATED("Created"),
        LOCATION_NOT_SERVICEABLE("Location_Not_Serviceable"),
        PICKING("Picking"),
        PICKED("Picked"),
        PACKED("Packed"),
        READY_TO_SHIP("Ready_To_Ship"),
        DISPATCHED("Dispatched"),
        SHIPPED("Shipped"),
        DELIVERED("Delivered"),
        RETURN_EXPECTED("Return_Expected"),
        RETURNED("Returned"), // is what  is same as return received
        SPLITTED("Splitted"),
        CANCELLED("Cancelled"),
        RETURN_ACKNOWLEDGED("Return_Acknowledged");

        final private String value;

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }

        private SHIPPING_PACKAGE_STATUS(String value) {
            this.value = value;
        }
    }

    public enum ORDER_STATUS {
        CREATED("Created"),
        PROCESSING("Processing"),
        DISPATCHED("Dispatched"),
        DELIVERED("Delivered"),
        CANCELLED("Cancelled"),
        CANCELLATION_REQUESTED("Cancellation Requested"),
        COMPLETE("Complete"),
        COMPLETE_PARTIALLY("Complete Partially"),
        PREPARATION_IN_PROGRESS("Preparation in progress"); // for old prestashop orders.

        final private String value;

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return getValue();
        }

        private ORDER_STATUS(String value) {
            this.value = value;
        }
    }
}
