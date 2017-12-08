/**
 * SaleOrderItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SaleOrderItem  implements java.io.Serializable {
    private java.lang.String code;

    private java.lang.String itemSKU;

    private java.lang.String shippingMethodCode;

    private java.lang.Boolean giftWrap;

    private java.lang.String giftMessage;

    private java.math.BigDecimal totalPrice;

    private java.math.BigDecimal sellingPrice;

    private java.math.BigDecimal shippingCharges;

    private java.math.BigDecimal shippingMethodCharges;

    private java.math.BigDecimal cashOnDeliveryCharges;

    private java.math.BigDecimal discount;

    private java.math.BigDecimal giftWrapCharges;

    private java.lang.String voucherCode;

    private java.math.BigDecimal voucherValue;

    private java.math.BigDecimal storeCredit;

    private java.math.BigInteger packetNumber;

    private java.lang.Boolean onHold;

    private java.lang.String facilityCode;

    private com.unicommerce.uniware.services.AddressRef shippingAddress;

    public SaleOrderItem() {
    }

    public SaleOrderItem(
           java.lang.String code,
           java.lang.String itemSKU,
           java.lang.String shippingMethodCode,
           java.lang.Boolean giftWrap,
           java.lang.String giftMessage,
           java.math.BigDecimal totalPrice,
           java.math.BigDecimal sellingPrice,
           java.math.BigDecimal shippingCharges,
           java.math.BigDecimal shippingMethodCharges,
           java.math.BigDecimal cashOnDeliveryCharges,
           java.math.BigDecimal discount,
           java.math.BigDecimal giftWrapCharges,
           java.lang.String voucherCode,
           java.math.BigDecimal voucherValue,
           java.math.BigDecimal storeCredit,
           java.math.BigInteger packetNumber,
           java.lang.Boolean onHold,
           java.lang.String facilityCode,
           com.unicommerce.uniware.services.AddressRef shippingAddress) {
           this.code = code;
           this.itemSKU = itemSKU;
           this.shippingMethodCode = shippingMethodCode;
           this.giftWrap = giftWrap;
           this.giftMessage = giftMessage;
           this.totalPrice = totalPrice;
           this.sellingPrice = sellingPrice;
           this.shippingCharges = shippingCharges;
           this.shippingMethodCharges = shippingMethodCharges;
           this.cashOnDeliveryCharges = cashOnDeliveryCharges;
           this.discount = discount;
           this.giftWrapCharges = giftWrapCharges;
           this.voucherCode = voucherCode;
           this.voucherValue = voucherValue;
           this.storeCredit = storeCredit;
           this.packetNumber = packetNumber;
           this.onHold = onHold;
           this.facilityCode = facilityCode;
           this.shippingAddress = shippingAddress;
    }


    /**
     * Gets the code value for this SaleOrderItem.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this SaleOrderItem.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the itemSKU value for this SaleOrderItem.
     * 
     * @return itemSKU
     */
    public java.lang.String getItemSKU() {
        return itemSKU;
    }


    /**
     * Sets the itemSKU value for this SaleOrderItem.
     * 
     * @param itemSKU
     */
    public void setItemSKU(java.lang.String itemSKU) {
        this.itemSKU = itemSKU;
    }


    /**
     * Gets the shippingMethodCode value for this SaleOrderItem.
     * 
     * @return shippingMethodCode
     */
    public java.lang.String getShippingMethodCode() {
        return shippingMethodCode;
    }


    /**
     * Sets the shippingMethodCode value for this SaleOrderItem.
     * 
     * @param shippingMethodCode
     */
    public void setShippingMethodCode(java.lang.String shippingMethodCode) {
        this.shippingMethodCode = shippingMethodCode;
    }


    /**
     * Gets the giftWrap value for this SaleOrderItem.
     * 
     * @return giftWrap
     */
    public java.lang.Boolean getGiftWrap() {
        return giftWrap;
    }


    /**
     * Sets the giftWrap value for this SaleOrderItem.
     * 
     * @param giftWrap
     */
    public void setGiftWrap(java.lang.Boolean giftWrap) {
        this.giftWrap = giftWrap;
    }


    /**
     * Gets the giftMessage value for this SaleOrderItem.
     * 
     * @return giftMessage
     */
    public java.lang.String getGiftMessage() {
        return giftMessage;
    }


    /**
     * Sets the giftMessage value for this SaleOrderItem.
     * 
     * @param giftMessage
     */
    public void setGiftMessage(java.lang.String giftMessage) {
        this.giftMessage = giftMessage;
    }


    /**
     * Gets the totalPrice value for this SaleOrderItem.
     * 
     * @return totalPrice
     */
    public java.math.BigDecimal getTotalPrice() {
        return totalPrice;
    }


    /**
     * Sets the totalPrice value for this SaleOrderItem.
     * 
     * @param totalPrice
     */
    public void setTotalPrice(java.math.BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    /**
     * Gets the sellingPrice value for this SaleOrderItem.
     * 
     * @return sellingPrice
     */
    public java.math.BigDecimal getSellingPrice() {
        return sellingPrice;
    }


    /**
     * Sets the sellingPrice value for this SaleOrderItem.
     * 
     * @param sellingPrice
     */
    public void setSellingPrice(java.math.BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    /**
     * Gets the shippingCharges value for this SaleOrderItem.
     * 
     * @return shippingCharges
     */
    public java.math.BigDecimal getShippingCharges() {
        return shippingCharges;
    }


    /**
     * Sets the shippingCharges value for this SaleOrderItem.
     * 
     * @param shippingCharges
     */
    public void setShippingCharges(java.math.BigDecimal shippingCharges) {
        this.shippingCharges = shippingCharges;
    }


    /**
     * Gets the shippingMethodCharges value for this SaleOrderItem.
     * 
     * @return shippingMethodCharges
     */
    public java.math.BigDecimal getShippingMethodCharges() {
        return shippingMethodCharges;
    }


    /**
     * Sets the shippingMethodCharges value for this SaleOrderItem.
     * 
     * @param shippingMethodCharges
     */
    public void setShippingMethodCharges(java.math.BigDecimal shippingMethodCharges) {
        this.shippingMethodCharges = shippingMethodCharges;
    }


    /**
     * Gets the cashOnDeliveryCharges value for this SaleOrderItem.
     * 
     * @return cashOnDeliveryCharges
     */
    public java.math.BigDecimal getCashOnDeliveryCharges() {
        return cashOnDeliveryCharges;
    }


    /**
     * Sets the cashOnDeliveryCharges value for this SaleOrderItem.
     * 
     * @param cashOnDeliveryCharges
     */
    public void setCashOnDeliveryCharges(java.math.BigDecimal cashOnDeliveryCharges) {
        this.cashOnDeliveryCharges = cashOnDeliveryCharges;
    }


    /**
     * Gets the discount value for this SaleOrderItem.
     * 
     * @return discount
     */
    public java.math.BigDecimal getDiscount() {
        return discount;
    }


    /**
     * Sets the discount value for this SaleOrderItem.
     * 
     * @param discount
     */
    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }


    /**
     * Gets the giftWrapCharges value for this SaleOrderItem.
     * 
     * @return giftWrapCharges
     */
    public java.math.BigDecimal getGiftWrapCharges() {
        return giftWrapCharges;
    }


    /**
     * Sets the giftWrapCharges value for this SaleOrderItem.
     * 
     * @param giftWrapCharges
     */
    public void setGiftWrapCharges(java.math.BigDecimal giftWrapCharges) {
        this.giftWrapCharges = giftWrapCharges;
    }


    /**
     * Gets the voucherCode value for this SaleOrderItem.
     * 
     * @return voucherCode
     */
    public java.lang.String getVoucherCode() {
        return voucherCode;
    }


    /**
     * Sets the voucherCode value for this SaleOrderItem.
     * 
     * @param voucherCode
     */
    public void setVoucherCode(java.lang.String voucherCode) {
        this.voucherCode = voucherCode;
    }


    /**
     * Gets the voucherValue value for this SaleOrderItem.
     * 
     * @return voucherValue
     */
    public java.math.BigDecimal getVoucherValue() {
        return voucherValue;
    }


    /**
     * Sets the voucherValue value for this SaleOrderItem.
     * 
     * @param voucherValue
     */
    public void setVoucherValue(java.math.BigDecimal voucherValue) {
        this.voucherValue = voucherValue;
    }


    /**
     * Gets the storeCredit value for this SaleOrderItem.
     * 
     * @return storeCredit
     */
    public java.math.BigDecimal getStoreCredit() {
        return storeCredit;
    }


    /**
     * Sets the storeCredit value for this SaleOrderItem.
     * 
     * @param storeCredit
     */
    public void setStoreCredit(java.math.BigDecimal storeCredit) {
        this.storeCredit = storeCredit;
    }


    /**
     * Gets the packetNumber value for this SaleOrderItem.
     * 
     * @return packetNumber
     */
    public java.math.BigInteger getPacketNumber() {
        return packetNumber;
    }


    /**
     * Sets the packetNumber value for this SaleOrderItem.
     * 
     * @param packetNumber
     */
    public void setPacketNumber(java.math.BigInteger packetNumber) {
        this.packetNumber = packetNumber;
    }


    /**
     * Gets the onHold value for this SaleOrderItem.
     * 
     * @return onHold
     */
    public java.lang.Boolean getOnHold() {
        return onHold;
    }


    /**
     * Sets the onHold value for this SaleOrderItem.
     * 
     * @param onHold
     */
    public void setOnHold(java.lang.Boolean onHold) {
        this.onHold = onHold;
    }


    /**
     * Gets the facilityCode value for this SaleOrderItem.
     * 
     * @return facilityCode
     */
    public java.lang.String getFacilityCode() {
        return facilityCode;
    }


    /**
     * Sets the facilityCode value for this SaleOrderItem.
     * 
     * @param facilityCode
     */
    public void setFacilityCode(java.lang.String facilityCode) {
        this.facilityCode = facilityCode;
    }


    /**
     * Gets the shippingAddress value for this SaleOrderItem.
     * 
     * @return shippingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getShippingAddress() {
        return shippingAddress;
    }


    /**
     * Sets the shippingAddress value for this SaleOrderItem.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(com.unicommerce.uniware.services.AddressRef shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SaleOrderItem)) return false;
        SaleOrderItem other = (SaleOrderItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.itemSKU==null && other.getItemSKU()==null) || 
             (this.itemSKU!=null &&
              this.itemSKU.equals(other.getItemSKU()))) &&
            ((this.shippingMethodCode==null && other.getShippingMethodCode()==null) || 
             (this.shippingMethodCode!=null &&
              this.shippingMethodCode.equals(other.getShippingMethodCode()))) &&
            ((this.giftWrap==null && other.getGiftWrap()==null) || 
             (this.giftWrap!=null &&
              this.giftWrap.equals(other.getGiftWrap()))) &&
            ((this.giftMessage==null && other.getGiftMessage()==null) || 
             (this.giftMessage!=null &&
              this.giftMessage.equals(other.getGiftMessage()))) &&
            ((this.totalPrice==null && other.getTotalPrice()==null) || 
             (this.totalPrice!=null &&
              this.totalPrice.equals(other.getTotalPrice()))) &&
            ((this.sellingPrice==null && other.getSellingPrice()==null) || 
             (this.sellingPrice!=null &&
              this.sellingPrice.equals(other.getSellingPrice()))) &&
            ((this.shippingCharges==null && other.getShippingCharges()==null) || 
             (this.shippingCharges!=null &&
              this.shippingCharges.equals(other.getShippingCharges()))) &&
            ((this.shippingMethodCharges==null && other.getShippingMethodCharges()==null) || 
             (this.shippingMethodCharges!=null &&
              this.shippingMethodCharges.equals(other.getShippingMethodCharges()))) &&
            ((this.cashOnDeliveryCharges==null && other.getCashOnDeliveryCharges()==null) || 
             (this.cashOnDeliveryCharges!=null &&
              this.cashOnDeliveryCharges.equals(other.getCashOnDeliveryCharges()))) &&
            ((this.discount==null && other.getDiscount()==null) || 
             (this.discount!=null &&
              this.discount.equals(other.getDiscount()))) &&
            ((this.giftWrapCharges==null && other.getGiftWrapCharges()==null) || 
             (this.giftWrapCharges!=null &&
              this.giftWrapCharges.equals(other.getGiftWrapCharges()))) &&
            ((this.voucherCode==null && other.getVoucherCode()==null) || 
             (this.voucherCode!=null &&
              this.voucherCode.equals(other.getVoucherCode()))) &&
            ((this.voucherValue==null && other.getVoucherValue()==null) || 
             (this.voucherValue!=null &&
              this.voucherValue.equals(other.getVoucherValue()))) &&
            ((this.storeCredit==null && other.getStoreCredit()==null) || 
             (this.storeCredit!=null &&
              this.storeCredit.equals(other.getStoreCredit()))) &&
            ((this.packetNumber==null && other.getPacketNumber()==null) || 
             (this.packetNumber!=null &&
              this.packetNumber.equals(other.getPacketNumber()))) &&
            ((this.onHold==null && other.getOnHold()==null) || 
             (this.onHold!=null &&
              this.onHold.equals(other.getOnHold()))) &&
            ((this.facilityCode==null && other.getFacilityCode()==null) || 
             (this.facilityCode!=null &&
              this.facilityCode.equals(other.getFacilityCode()))) &&
            ((this.shippingAddress==null && other.getShippingAddress()==null) || 
             (this.shippingAddress!=null &&
              this.shippingAddress.equals(other.getShippingAddress())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getItemSKU() != null) {
            _hashCode += getItemSKU().hashCode();
        }
        if (getShippingMethodCode() != null) {
            _hashCode += getShippingMethodCode().hashCode();
        }
        if (getGiftWrap() != null) {
            _hashCode += getGiftWrap().hashCode();
        }
        if (getGiftMessage() != null) {
            _hashCode += getGiftMessage().hashCode();
        }
        if (getTotalPrice() != null) {
            _hashCode += getTotalPrice().hashCode();
        }
        if (getSellingPrice() != null) {
            _hashCode += getSellingPrice().hashCode();
        }
        if (getShippingCharges() != null) {
            _hashCode += getShippingCharges().hashCode();
        }
        if (getShippingMethodCharges() != null) {
            _hashCode += getShippingMethodCharges().hashCode();
        }
        if (getCashOnDeliveryCharges() != null) {
            _hashCode += getCashOnDeliveryCharges().hashCode();
        }
        if (getDiscount() != null) {
            _hashCode += getDiscount().hashCode();
        }
        if (getGiftWrapCharges() != null) {
            _hashCode += getGiftWrapCharges().hashCode();
        }
        if (getVoucherCode() != null) {
            _hashCode += getVoucherCode().hashCode();
        }
        if (getVoucherValue() != null) {
            _hashCode += getVoucherValue().hashCode();
        }
        if (getStoreCredit() != null) {
            _hashCode += getStoreCredit().hashCode();
        }
        if (getPacketNumber() != null) {
            _hashCode += getPacketNumber().hashCode();
        }
        if (getOnHold() != null) {
            _hashCode += getOnHold().hashCode();
        }
        if (getFacilityCode() != null) {
            _hashCode += getFacilityCode().hashCode();
        }
        if (getShippingAddress() != null) {
            _hashCode += getShippingAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SaleOrderItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemSKU");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemSKU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingMethodCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingMethodCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftWrap");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GiftWrap"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GiftMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "TotalPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sellingPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SellingPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingCharges");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingCharges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingMethodCharges");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingMethodCharges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cashOnDeliveryCharges");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CashOnDeliveryCharges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("discount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Discount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("giftWrapCharges");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GiftWrapCharges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voucherCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "VoucherCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voucherValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "VoucherValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("storeCredit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "StoreCredit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("packetNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PacketNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("onHold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "OnHold"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facilityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "FacilityCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddressRef"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SaleOrderItem");
        sb.append("{\n\t code=").append(code).append("\n");
        sb.append("\t discount=").append(discount).append("\n");
        sb.append("\t facilityCode=").append(facilityCode).append("\n");
        sb.append("\t itemSKU=").append(itemSKU).append("\n");
        sb.append("\t sellingPrice=").append(sellingPrice).append("\n");
        sb.append("\t shippingAddress=").append(shippingAddress).append("\n");
        sb.append("\t shippingMethodCode=").append(shippingMethodCode).append("\n");
        sb.append("\t totalPrice=").append(totalPrice).append("\n");
        sb.append("\t voucherCode=").append(voucherCode).append("\n");
        sb.append("\t voucherValue=").append(voucherValue).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
