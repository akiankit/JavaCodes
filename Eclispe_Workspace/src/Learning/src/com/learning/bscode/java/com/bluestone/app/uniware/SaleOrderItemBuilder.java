package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.unicommerce.uniware.services.Address;
import com.unicommerce.uniware.services.SaleOrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 10/22/12
 */
class SaleOrderItemBuilder {

    private static final Logger log = LoggerFactory.getLogger(SaleOrderItemBuilder.class);

    public enum ShippingMethodCode {
        STD
    }

    private String code;
    private String itemSKU;

    private String shippingMethodCode;
    private BigDecimal discount;
    private BigDecimal totalPrice;
    private String voucherCode;
    private BigDecimal voucherValue;
    private Address shippingAddress;

    private BigDecimal sellingPrice;

    private BigInteger packetNumber = new BigInteger("0");

    private boolean onHold = false;


/*
If 0 is passed packages are flexible,
otherwise order will be packed corresponding to the numbers sent in this field. i.e

SOI1   1   SP1
SOI2   1   SP1
SOI3   2   SP2
SOI4   2   SP2

*/


    //private String facilityCode = "03";


//    private Boolean giftWrap;
//    private String giftMessage;
//    private BigDecimal sellingPrice;
//    private BigDecimal shippingCharges;
//    private BigDecimal shippingMethodCharges;
//    private BigDecimal cashOnDeliveryCharges;
//    private BigDecimal giftWrapCharges;
//    private BigDecimal storeCredit;
//


    public void setShippingMethodCode(String shippingMethodCode) {
        if (shippingMethodCode != null) {
            this.shippingMethodCode = shippingMethodCode;
        }
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public void setVoucherValue(BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setItemSKU(String itemSKU) {
        this.itemSKU = itemSKU;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public SaleOrderItemBuilder() {
        shippingMethodCode = ShippingMethodCode.STD.name();
    }

    public void setPacketNumber(BigInteger packetNumber) {
        this.packetNumber = packetNumber;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setOnHold(boolean onHold) {
        this.onHold = onHold;
    }

    public SaleOrderItem create() {
        log.debug("SaleOrderItemBuilder.create()");
        validate();
        SaleOrderItem uniwareSaleOrderItem = new SaleOrderItem();
        //saleOrderItem.setFacilityCode(facilityCode);
        uniwareSaleOrderItem.setCode(code);
        uniwareSaleOrderItem.setItemSKU(itemSKU);
        uniwareSaleOrderItem.setPacketNumber(packetNumber);
        uniwareSaleOrderItem.setOnHold(onHold);
        uniwareSaleOrderItem.setShippingMethodCode(shippingMethodCode);
        uniwareSaleOrderItem.setDiscount(discount);
        uniwareSaleOrderItem.setVoucherCode(voucherCode);
        //saleOrderItem.setVoucherValue(voucherValue);
        uniwareSaleOrderItem.setTotalPrice(totalPrice);
        uniwareSaleOrderItem.setSellingPrice(sellingPrice); // selling price has to be same as total price , it gets adjusted after spreading the discount.
        uniwareSaleOrderItem.setShippingAddress(AddressBuilder.createUniwareAddressRef(shippingAddress));
        log.debug("SaleOrderItemBuilder.create():{}", this.toString());
        return uniwareSaleOrderItem;
    }

    private void validate() {
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SaleOrderItem Details to be sent to uniware = ");
        sb.append("{\n\t Code=").append(code).append("\n");
        sb.append("\t ShippingMethodCode=").append(shippingMethodCode).append("\n");
        sb.append("\t ItemSKU=").append(itemSKU).append("\n");
        sb.append("\t Packet Number=").append(packetNumber).append("\n");
        sb.append("\t On Hold=").append(onHold).append("\n");
        sb.append("\t Discount=").append(discount).append("\n");
        sb.append("\t Total Price=").append(totalPrice).append("\n");
        //sb.append("\t FacilityCode=").append(facilityCode).append("\n");
        sb.append("\t VoucherCode=").append(voucherCode).append("\n");
        sb.append("\t VoucherValue=").append(voucherValue).append("\n");
        sb.append("\t Shipping Address=").append(shippingAddress).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
