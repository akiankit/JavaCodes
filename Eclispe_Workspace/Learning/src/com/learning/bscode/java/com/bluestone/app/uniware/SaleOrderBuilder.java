package com.bluestone.app.uniware;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.unicommerce.uniware.services.Address;
import com.unicommerce.uniware.services.SaleOrder;
import com.unicommerce.uniware.services.SaleOrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 9/28/12
 */
class SaleOrderBuilder {
    private static final Logger log = LoggerFactory.getLogger(SaleOrderBuilder.class);

    private String code;
    private String displayOrderCode;
    private Calendar displayOrderDateTime;

    private String notificationEmail;
    private String notificationMobile;

    private Boolean cashOnDelivery;

    private String additionalInfo;
    private Map<String, String> customFields;

    private SaleOrderItem[] saleOrderItems;

    private Address shippingAddress;
    private Address billingAddress;


    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public void setNotificationMobile(String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }

    public void setCashOnDelivery(Boolean cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDisplayOrderCode(String displayOrderCode) {
        this.displayOrderCode = displayOrderCode;
    }

    public void setDisplayOrderDateTime(Calendar displayOrderDateTime) {
        this.displayOrderDateTime = displayOrderDateTime;
    }

    /* public void setSaleOrderItems(SaleOrderItem[] saleOrderItems) {
        this.saleOrderItems = saleOrderItems;
    }*/

    public void setSaleOrderItems(List<SaleOrderItem> saleOrderItemList) {
        this.saleOrderItems = new SaleOrderItem[saleOrderItemList.size()];
        for (int i = 0; i < saleOrderItems.length; i++) {
            saleOrderItems[i] = saleOrderItemList.get(i);
        }
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public SaleOrderBuilder() {
    }

    public SaleOrder create() {
        log.debug("SaleOrderBuilder.create(): Order Code={}", code);
        validate();
        SaleOrder uniwareSaleOrder = new SaleOrder();
        populateAddressDetails(uniwareSaleOrder);
        uniwareSaleOrder.setCode(code);
        uniwareSaleOrder.setDisplayOrderCode(displayOrderCode);
        uniwareSaleOrder.setDisplayOrderDateTime(displayOrderDateTime);
        uniwareSaleOrder.setSaleOrderItems(saleOrderItems);
        uniwareSaleOrder.setCashOnDelivery(cashOnDelivery);
        uniwareSaleOrder.setNotificationEmail(notificationEmail);
        uniwareSaleOrder.setNotificationMobile(notificationMobile);
        uniwareSaleOrder.setAdditionalInfo(additionalInfo);
        uniwareSaleOrder.setCustomFields(CustomFieldBuilder.createCustomFields(customFields));
        return uniwareSaleOrder;
    }

    private void validate() {
    }

    private void populateAddressDetails(final SaleOrder saleOrder) {
        Address[] addresses = new Address[2];
        addresses[0] = billingAddress;
        addresses[1] = shippingAddress;
        saleOrder.setAddresses(addresses);

        saleOrder.setBillingAddress(AddressBuilder.createUniwareAddressRef(billingAddress));
        saleOrder.setShippingAddress(AddressBuilder.createUniwareAddressRef(shippingAddress));
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SaleOrderBuilder");
        sb.append("{\n\t Additionalinfo=").append(additionalInfo).append("\n");
        sb.append("\t Code=").append(code).append("\n");
        sb.append("\t Display OrderCode=").append(displayOrderCode).append("\n");
        sb.append("\t Is Cash OnDelivery=").append(cashOnDelivery).append("\n");
        sb.append("\t Display OrderDateTime=").append(displayOrderDateTime).append("\n");
        sb.append("\t Billing Address=").append(billingAddress).append("\n");
        sb.append("\t Customfields=").append(customFields).append("\n");
        sb.append("\t Notificationemail=").append(notificationEmail).append("\n");
        sb.append("\t Notificationmobile=").append(notificationMobile).append("\n");
        sb.append("\t SaleOrderItems=").append(saleOrderItems == null ? "null" : Arrays.asList(saleOrderItems).toString()).append("\n");
        sb.append("\t Shipping Address=").append(shippingAddress).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
