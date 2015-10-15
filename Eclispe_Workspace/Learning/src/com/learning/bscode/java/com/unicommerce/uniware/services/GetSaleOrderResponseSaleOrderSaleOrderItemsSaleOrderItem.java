/**
 * GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem  implements java.io.Serializable {
    private java.lang.String code;

    private java.lang.String statusCode;

    private java.lang.String itemSKU;

    private com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItemShippingMethodCode shippingMethodCode;

    private java.lang.Boolean giftWrap;

    private java.lang.String giftMessage;

    private java.math.BigDecimal totalPrice;

    private java.math.BigDecimal sellingPrice;

    private java.math.BigDecimal shippingCharges;

    private java.math.BigDecimal shippingMethodCharges;

    private java.math.BigDecimal cashOnDeliveryCharges;

    private java.math.BigDecimal discount;

    private java.lang.String shippingPackageCode;

    private java.lang.String facilityCode;

    private java.lang.String giftWrapCharges;

    private java.math.BigInteger packetNumber;

    private com.unicommerce.uniware.services.AddressRef shippingAddress;

    private boolean cancellable;

    private java.util.Calendar createdOn;

    private java.util.Calendar updatedOn;

    public GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem() {
    }

    public GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem(
           java.lang.String code,
           java.lang.String statusCode,
           java.lang.String itemSKU,
           com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItemShippingMethodCode shippingMethodCode,
           java.lang.Boolean giftWrap,
           java.lang.String giftMessage,
           java.math.BigDecimal totalPrice,
           java.math.BigDecimal sellingPrice,
           java.math.BigDecimal shippingCharges,
           java.math.BigDecimal shippingMethodCharges,
           java.math.BigDecimal cashOnDeliveryCharges,
           java.math.BigDecimal discount,
           java.lang.String shippingPackageCode,
           java.lang.String facilityCode,
           java.lang.String giftWrapCharges,
           java.math.BigInteger packetNumber,
           com.unicommerce.uniware.services.AddressRef shippingAddress,
           boolean cancellable,
           java.util.Calendar createdOn,
           java.util.Calendar updatedOn) {
           this.code = code;
           this.statusCode = statusCode;
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
           this.shippingPackageCode = shippingPackageCode;
           this.facilityCode = facilityCode;
           this.giftWrapCharges = giftWrapCharges;
           this.packetNumber = packetNumber;
           this.shippingAddress = shippingAddress;
           this.cancellable = cancellable;
           this.createdOn = createdOn;
           this.updatedOn = updatedOn;
    }


    /**
     * Gets the code value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the statusCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return statusCode
     */
    public java.lang.String getStatusCode() {
        return statusCode;
    }


    /**
     * Sets the statusCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param statusCode
     */
    public void setStatusCode(java.lang.String statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets the itemSKU value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return itemSKU
     */
    public java.lang.String getItemSKU() {
        return itemSKU;
    }


    /**
     * Sets the itemSKU value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param itemSKU
     */
    public void setItemSKU(java.lang.String itemSKU) {
        this.itemSKU = itemSKU;
    }


    /**
     * Gets the shippingMethodCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return shippingMethodCode
     */
    public com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItemShippingMethodCode getShippingMethodCode() {
        return shippingMethodCode;
    }


    /**
     * Sets the shippingMethodCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param shippingMethodCode
     */
    public void setShippingMethodCode(com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItemShippingMethodCode shippingMethodCode) {
        this.shippingMethodCode = shippingMethodCode;
    }


    /**
     * Gets the giftWrap value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return giftWrap
     */
    public java.lang.Boolean getGiftWrap() {
        return giftWrap;
    }


    /**
     * Sets the giftWrap value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param giftWrap
     */
    public void setGiftWrap(java.lang.Boolean giftWrap) {
        this.giftWrap = giftWrap;
    }


    /**
     * Gets the giftMessage value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return giftMessage
     */
    public java.lang.String getGiftMessage() {
        return giftMessage;
    }


    /**
     * Sets the giftMessage value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param giftMessage
     */
    public void setGiftMessage(java.lang.String giftMessage) {
        this.giftMessage = giftMessage;
    }


    /**
     * Gets the totalPrice value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return totalPrice
     */
    public java.math.BigDecimal getTotalPrice() {
        return totalPrice;
    }


    /**
     * Sets the totalPrice value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param totalPrice
     */
    public void setTotalPrice(java.math.BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    /**
     * Gets the sellingPrice value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return sellingPrice
     */
    public java.math.BigDecimal getSellingPrice() {
        return sellingPrice;
    }


    /**
     * Sets the sellingPrice value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param sellingPrice
     */
    public void setSellingPrice(java.math.BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    /**
     * Gets the shippingCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return shippingCharges
     */
    public java.math.BigDecimal getShippingCharges() {
        return shippingCharges;
    }


    /**
     * Sets the shippingCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param shippingCharges
     */
    public void setShippingCharges(java.math.BigDecimal shippingCharges) {
        this.shippingCharges = shippingCharges;
    }


    /**
     * Gets the shippingMethodCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return shippingMethodCharges
     */
    public java.math.BigDecimal getShippingMethodCharges() {
        return shippingMethodCharges;
    }


    /**
     * Sets the shippingMethodCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param shippingMethodCharges
     */
    public void setShippingMethodCharges(java.math.BigDecimal shippingMethodCharges) {
        this.shippingMethodCharges = shippingMethodCharges;
    }


    /**
     * Gets the cashOnDeliveryCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return cashOnDeliveryCharges
     */
    public java.math.BigDecimal getCashOnDeliveryCharges() {
        return cashOnDeliveryCharges;
    }


    /**
     * Sets the cashOnDeliveryCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param cashOnDeliveryCharges
     */
    public void setCashOnDeliveryCharges(java.math.BigDecimal cashOnDeliveryCharges) {
        this.cashOnDeliveryCharges = cashOnDeliveryCharges;
    }


    /**
     * Gets the discount value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return discount
     */
    public java.math.BigDecimal getDiscount() {
        return discount;
    }


    /**
     * Sets the discount value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param discount
     */
    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }


    /**
     * Gets the shippingPackageCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return shippingPackageCode
     */
    public java.lang.String getShippingPackageCode() {
        return shippingPackageCode;
    }


    /**
     * Sets the shippingPackageCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param shippingPackageCode
     */
    public void setShippingPackageCode(java.lang.String shippingPackageCode) {
        this.shippingPackageCode = shippingPackageCode;
    }


    /**
     * Gets the facilityCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return facilityCode
     */
    public java.lang.String getFacilityCode() {
        return facilityCode;
    }


    /**
     * Sets the facilityCode value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param facilityCode
     */
    public void setFacilityCode(java.lang.String facilityCode) {
        this.facilityCode = facilityCode;
    }


    /**
     * Gets the giftWrapCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return giftWrapCharges
     */
    public java.lang.String getGiftWrapCharges() {
        return giftWrapCharges;
    }


    /**
     * Sets the giftWrapCharges value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param giftWrapCharges
     */
    public void setGiftWrapCharges(java.lang.String giftWrapCharges) {
        this.giftWrapCharges = giftWrapCharges;
    }


    /**
     * Gets the packetNumber value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return packetNumber
     */
    public java.math.BigInteger getPacketNumber() {
        return packetNumber;
    }


    /**
     * Sets the packetNumber value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param packetNumber
     */
    public void setPacketNumber(java.math.BigInteger packetNumber) {
        this.packetNumber = packetNumber;
    }


    /**
     * Gets the shippingAddress value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return shippingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getShippingAddress() {
        return shippingAddress;
    }


    /**
     * Sets the shippingAddress value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(com.unicommerce.uniware.services.AddressRef shippingAddress) {
        this.shippingAddress = shippingAddress;
    }


    /**
     * Gets the cancellable value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return cancellable
     */
    public boolean isCancellable() {
        return cancellable;
    }


    /**
     * Sets the cancellable value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param cancellable
     */
    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }


    /**
     * Gets the createdOn value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return createdOn
     */
    public java.util.Calendar getCreatedOn() {
        return createdOn;
    }


    /**
     * Sets the createdOn value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param createdOn
     */
    public void setCreatedOn(java.util.Calendar createdOn) {
        this.createdOn = createdOn;
    }


    /**
     * Gets the updatedOn value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @return updatedOn
     */
    public java.util.Calendar getUpdatedOn() {
        return updatedOn;
    }


    /**
     * Sets the updatedOn value for this GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.
     * 
     * @param updatedOn
     */
    public void setUpdatedOn(java.util.Calendar updatedOn) {
        this.updatedOn = updatedOn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem)) return false;
        GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem other = (GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem) obj;
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
            ((this.statusCode==null && other.getStatusCode()==null) || 
             (this.statusCode!=null &&
              this.statusCode.equals(other.getStatusCode()))) &&
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
            ((this.shippingPackageCode==null && other.getShippingPackageCode()==null) || 
             (this.shippingPackageCode!=null &&
              this.shippingPackageCode.equals(other.getShippingPackageCode()))) &&
            ((this.facilityCode==null && other.getFacilityCode()==null) || 
             (this.facilityCode!=null &&
              this.facilityCode.equals(other.getFacilityCode()))) &&
            ((this.giftWrapCharges==null && other.getGiftWrapCharges()==null) || 
             (this.giftWrapCharges!=null &&
              this.giftWrapCharges.equals(other.getGiftWrapCharges()))) &&
            ((this.packetNumber==null && other.getPacketNumber()==null) || 
             (this.packetNumber!=null &&
              this.packetNumber.equals(other.getPacketNumber()))) &&
            ((this.shippingAddress==null && other.getShippingAddress()==null) || 
             (this.shippingAddress!=null &&
              this.shippingAddress.equals(other.getShippingAddress()))) &&
            this.cancellable == other.isCancellable() &&
            ((this.createdOn==null && other.getCreatedOn()==null) || 
             (this.createdOn!=null &&
              this.createdOn.equals(other.getCreatedOn()))) &&
            ((this.updatedOn==null && other.getUpdatedOn()==null) || 
             (this.updatedOn!=null &&
              this.updatedOn.equals(other.getUpdatedOn())));
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
        if (getStatusCode() != null) {
            _hashCode += getStatusCode().hashCode();
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
        if (getShippingPackageCode() != null) {
            _hashCode += getShippingPackageCode().hashCode();
        }
        if (getFacilityCode() != null) {
            _hashCode += getFacilityCode().hashCode();
        }
        if (getGiftWrapCharges() != null) {
            _hashCode += getGiftWrapCharges().hashCode();
        }
        if (getPacketNumber() != null) {
            _hashCode += getPacketNumber().hashCode();
        }
        if (getShippingAddress() != null) {
            _hashCode += getShippingAddress().hashCode();
        }
        _hashCode += (isCancellable() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getCreatedOn() != null) {
            _hashCode += getCreatedOn().hashCode();
        }
        if (getUpdatedOn() != null) {
            _hashCode += getUpdatedOn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>SaleOrderItems>SaleOrderItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "StatusCode"));
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>>GetSaleOrderResponse>SaleOrder>SaleOrderItems>SaleOrderItem>ShippingMethodCode"));
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
        elemField.setFieldName("shippingPackageCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingPackageCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("giftWrapCharges");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "GiftWrapCharges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("shippingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddressRef"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cancellable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Cancellable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreatedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatedOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UpdatedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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

}
