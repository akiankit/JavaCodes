package com.bluestone.app.uniware.util;

public enum SaleOrderExportJobColumn {

    /*{"exportJobTypeName":"Sale Orders",
      "exportFilters":[{"id":"updatedSince","text":"28/11/2012"}],
      "exportColums":[
           "code",
           "shippingMethod",
           "packetNumber",
           "saleOrderCode",
           "onhold",
           "status",
           "priority",
           "SoiStatus",
           "shippingProvider",
           "ShippingPackageCode",
           "ShippingPackageCreationDate",
           "shippingPackageStatusCode",
           "deliveryTime",
           "TrackingNumber",
           "dispatchDate",
           "returnedDate",
           "created",
           "updated"]}*/
    SALE_ORDER_ITEM_CODE("code"), // sale order item code
    SHIPPING_METHOD("shippingMethod"), // shipping method
    PACKET_NUMBER("packetNumber"), // packet number
    CODE("saleOrderCode"),// sale order code 
    ON_HOLD("onhold"), // is item on hold
    SALE_ORDER_STATUS("status"),  // sale order status
    SALE_ORDER_ITEM_STATUS("SoiStatus"), // sale order item status
    SHIPPING_PROVIDER("shippingProvider"),
    SHIPPING_PACKAGE_CODE("ShippingPackageCode"),
    SHIPPING_PACKAGE_CREATION_DATE("ShippingPackageCreationDate"),
    SHIPPING_PACKAGE_STATUS_CODE("shippingPackageStatusCode"),
    DELIVERY_TIME("deliveryTime"),
    TRACKING_NUMBER("TrackingNumber"),
    DISPATCH_DATE("dispatchDate");

    private final String uniwareInternalFieldName;

    private SaleOrderExportJobColumn(String uniwareColumnName) {
        this.uniwareInternalFieldName = uniwareColumnName;
    }

    public String getUniwareInternalFieldName() {
        return uniwareInternalFieldName;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.name()).append("(").append(uniwareInternalFieldName).append(")");
        sb.append("");
        return sb.toString();
    }

    public static String[] getExportColumns() {
        SaleOrderExportJobColumn[] values = SaleOrderExportJobColumn.values();
        String[] exportColNames = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            SaleOrderExportJobColumn value = values[i];
            exportColNames[i] = value.getUniwareInternalFieldName();
        }
        return exportColNames;
    }


}
