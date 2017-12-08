/**
 * GetSaleOrderResponseSaleOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class GetSaleOrderResponseSaleOrder  implements java.io.Serializable {
    private java.lang.String code;

    private java.lang.String displayOrderCode;

    private java.util.Calendar displayOrderDateTime;

    private java.lang.String notificationEmail;

    private java.lang.String notificationMobile;

    private java.util.Calendar createdOn;

    private java.util.Calendar updatedOn;

    private boolean cashOnDelivery;

    private java.lang.String additionalInfo;

    private com.unicommerce.uniware.services.Address[] addresses;

    private com.unicommerce.uniware.services.AddressRef billingAddress;

    private com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems;

    private com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[] shippingPackages;

    public GetSaleOrderResponseSaleOrder() {
    }

    public GetSaleOrderResponseSaleOrder(
           java.lang.String code,
           java.lang.String displayOrderCode,
           java.util.Calendar displayOrderDateTime,
           java.lang.String notificationEmail,
           java.lang.String notificationMobile,
           java.util.Calendar createdOn,
           java.util.Calendar updatedOn,
           boolean cashOnDelivery,
           java.lang.String additionalInfo,
           com.unicommerce.uniware.services.Address[] addresses,
           com.unicommerce.uniware.services.AddressRef billingAddress,
           com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems,
           com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[] shippingPackages) {
           this.code = code;
           this.displayOrderCode = displayOrderCode;
           this.displayOrderDateTime = displayOrderDateTime;
           this.notificationEmail = notificationEmail;
           this.notificationMobile = notificationMobile;
           this.createdOn = createdOn;
           this.updatedOn = updatedOn;
           this.cashOnDelivery = cashOnDelivery;
           this.additionalInfo = additionalInfo;
           this.addresses = addresses;
           this.billingAddress = billingAddress;
           this.saleOrderItems = saleOrderItems;
           this.shippingPackages = shippingPackages;
    }


    /**
     * Gets the code value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the displayOrderCode value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return displayOrderCode
     */
    public java.lang.String getDisplayOrderCode() {
        return displayOrderCode;
    }


    /**
     * Sets the displayOrderCode value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param displayOrderCode
     */
    public void setDisplayOrderCode(java.lang.String displayOrderCode) {
        this.displayOrderCode = displayOrderCode;
    }


    /**
     * Gets the displayOrderDateTime value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return displayOrderDateTime
     */
    public java.util.Calendar getDisplayOrderDateTime() {
        return displayOrderDateTime;
    }


    /**
     * Sets the displayOrderDateTime value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param displayOrderDateTime
     */
    public void setDisplayOrderDateTime(java.util.Calendar displayOrderDateTime) {
        this.displayOrderDateTime = displayOrderDateTime;
    }


    /**
     * Gets the notificationEmail value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return notificationEmail
     */
    public java.lang.String getNotificationEmail() {
        return notificationEmail;
    }


    /**
     * Sets the notificationEmail value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param notificationEmail
     */
    public void setNotificationEmail(java.lang.String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }


    /**
     * Gets the notificationMobile value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return notificationMobile
     */
    public java.lang.String getNotificationMobile() {
        return notificationMobile;
    }


    /**
     * Sets the notificationMobile value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param notificationMobile
     */
    public void setNotificationMobile(java.lang.String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }


    /**
     * Gets the createdOn value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return createdOn
     */
    public java.util.Calendar getCreatedOn() {
        return createdOn;
    }


    /**
     * Sets the createdOn value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param createdOn
     */
    public void setCreatedOn(java.util.Calendar createdOn) {
        this.createdOn = createdOn;
    }


    /**
     * Gets the updatedOn value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return updatedOn
     */
    public java.util.Calendar getUpdatedOn() {
        return updatedOn;
    }


    /**
     * Sets the updatedOn value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param updatedOn
     */
    public void setUpdatedOn(java.util.Calendar updatedOn) {
        this.updatedOn = updatedOn;
    }


    /**
     * Gets the cashOnDelivery value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return cashOnDelivery
     */
    public boolean isCashOnDelivery() {
        return cashOnDelivery;
    }


    /**
     * Sets the cashOnDelivery value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param cashOnDelivery
     */
    public void setCashOnDelivery(boolean cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }


    /**
     * Gets the additionalInfo value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return additionalInfo
     */
    public java.lang.String getAdditionalInfo() {
        return additionalInfo;
    }


    /**
     * Sets the additionalInfo value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param additionalInfo
     */
    public void setAdditionalInfo(java.lang.String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    /**
     * Gets the addresses value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return addresses
     */
    public com.unicommerce.uniware.services.Address[] getAddresses() {
        return addresses;
    }


    /**
     * Sets the addresses value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param addresses
     */
    public void setAddresses(com.unicommerce.uniware.services.Address[] addresses) {
        this.addresses = addresses;
    }


    /**
     * Gets the billingAddress value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return billingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getBillingAddress() {
        return billingAddress;
    }


    /**
     * Sets the billingAddress value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param billingAddress
     */
    public void setBillingAddress(com.unicommerce.uniware.services.AddressRef billingAddress) {
        this.billingAddress = billingAddress;
    }


    /**
     * Gets the saleOrderItems value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return saleOrderItems
     */
    public com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem[] getSaleOrderItems() {
        return saleOrderItems;
    }


    /**
     * Sets the saleOrderItems value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param saleOrderItems
     */
    public void setSaleOrderItems(com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems) {
        this.saleOrderItems = saleOrderItems;
    }


    /**
     * Gets the shippingPackages value for this GetSaleOrderResponseSaleOrder.
     * 
     * @return shippingPackages
     */
    public com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[] getShippingPackages() {
        return shippingPackages;
    }


    /**
     * Sets the shippingPackages value for this GetSaleOrderResponseSaleOrder.
     * 
     * @param shippingPackages
     */
    public void setShippingPackages(com.unicommerce.uniware.services.GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage[] shippingPackages) {
        this.shippingPackages = shippingPackages;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSaleOrderResponseSaleOrder)) return false;
        GetSaleOrderResponseSaleOrder other = (GetSaleOrderResponseSaleOrder) obj;
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
            ((this.displayOrderCode==null && other.getDisplayOrderCode()==null) || 
             (this.displayOrderCode!=null &&
              this.displayOrderCode.equals(other.getDisplayOrderCode()))) &&
            ((this.displayOrderDateTime==null && other.getDisplayOrderDateTime()==null) || 
             (this.displayOrderDateTime!=null &&
              this.displayOrderDateTime.equals(other.getDisplayOrderDateTime()))) &&
            ((this.notificationEmail==null && other.getNotificationEmail()==null) || 
             (this.notificationEmail!=null &&
              this.notificationEmail.equals(other.getNotificationEmail()))) &&
            ((this.notificationMobile==null && other.getNotificationMobile()==null) || 
             (this.notificationMobile!=null &&
              this.notificationMobile.equals(other.getNotificationMobile()))) &&
            ((this.createdOn==null && other.getCreatedOn()==null) || 
             (this.createdOn!=null &&
              this.createdOn.equals(other.getCreatedOn()))) &&
            ((this.updatedOn==null && other.getUpdatedOn()==null) || 
             (this.updatedOn!=null &&
              this.updatedOn.equals(other.getUpdatedOn()))) &&
            this.cashOnDelivery == other.isCashOnDelivery() &&
            ((this.additionalInfo==null && other.getAdditionalInfo()==null) || 
             (this.additionalInfo!=null &&
              this.additionalInfo.equals(other.getAdditionalInfo()))) &&
            ((this.addresses==null && other.getAddresses()==null) || 
             (this.addresses!=null &&
              java.util.Arrays.equals(this.addresses, other.getAddresses()))) &&
            ((this.billingAddress==null && other.getBillingAddress()==null) || 
             (this.billingAddress!=null &&
              this.billingAddress.equals(other.getBillingAddress()))) &&
            ((this.saleOrderItems==null && other.getSaleOrderItems()==null) || 
             (this.saleOrderItems!=null &&
              java.util.Arrays.equals(this.saleOrderItems, other.getSaleOrderItems()))) &&
            ((this.shippingPackages==null && other.getShippingPackages()==null) || 
             (this.shippingPackages!=null &&
              java.util.Arrays.equals(this.shippingPackages, other.getShippingPackages())));
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
        if (getDisplayOrderCode() != null) {
            _hashCode += getDisplayOrderCode().hashCode();
        }
        if (getDisplayOrderDateTime() != null) {
            _hashCode += getDisplayOrderDateTime().hashCode();
        }
        if (getNotificationEmail() != null) {
            _hashCode += getNotificationEmail().hashCode();
        }
        if (getNotificationMobile() != null) {
            _hashCode += getNotificationMobile().hashCode();
        }
        if (getCreatedOn() != null) {
            _hashCode += getCreatedOn().hashCode();
        }
        if (getUpdatedOn() != null) {
            _hashCode += getUpdatedOn().hashCode();
        }
        _hashCode += (isCashOnDelivery() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getAdditionalInfo() != null) {
            _hashCode += getAdditionalInfo().hashCode();
        }
        if (getAddresses() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAddresses());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAddresses(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBillingAddress() != null) {
            _hashCode += getBillingAddress().hashCode();
        }
        if (getSaleOrderItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSaleOrderItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSaleOrderItems(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getShippingPackages() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getShippingPackages());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getShippingPackages(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSaleOrderResponseSaleOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>GetSaleOrderResponse>SaleOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrderCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DisplayOrderCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrderDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DisplayOrderDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "NotificationEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationMobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "NotificationMobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cashOnDelivery");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CashOnDelivery"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AdditionalInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addresses");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Addresses"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Address"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("billingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "BillingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddressRef"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrderItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>SaleOrderItems>SaleOrderItem"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingPackages");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingPackages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>ShippingPackages>ShippingPackage"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingPackage"));
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
