/**
 * SaleOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SaleOrder  implements java.io.Serializable {
    private java.lang.String code;

    private java.lang.String displayOrderCode;

    private java.util.Calendar displayOrderDateTime;

    private java.lang.String notificationEmail;

    private java.lang.String notificationMobile;

    private java.lang.Boolean cashOnDelivery;

    private java.lang.String additionalInfo;

    private com.unicommerce.uniware.services.Address[] addresses;

    private com.unicommerce.uniware.services.AddressRef shippingAddress;

    private com.unicommerce.uniware.services.AddressRef billingAddress;

    private com.unicommerce.uniware.services.SaleOrderItem[] saleOrderItems;

    private com.unicommerce.uniware.services.ShippingProvider[] shippingProviders;

    private com.unicommerce.uniware.services.CustomFieldsCustomField[] customFields;

    public SaleOrder() {
    }

    public SaleOrder(
           java.lang.String code,
           java.lang.String displayOrderCode,
           java.util.Calendar displayOrderDateTime,
           java.lang.String notificationEmail,
           java.lang.String notificationMobile,
           java.lang.Boolean cashOnDelivery,
           java.lang.String additionalInfo,
           com.unicommerce.uniware.services.Address[] addresses,
           com.unicommerce.uniware.services.AddressRef shippingAddress,
           com.unicommerce.uniware.services.AddressRef billingAddress,
           com.unicommerce.uniware.services.SaleOrderItem[] saleOrderItems,
           com.unicommerce.uniware.services.ShippingProvider[] shippingProviders,
           com.unicommerce.uniware.services.CustomFieldsCustomField[] customFields) {
           this.code = code;
           this.displayOrderCode = displayOrderCode;
           this.displayOrderDateTime = displayOrderDateTime;
           this.notificationEmail = notificationEmail;
           this.notificationMobile = notificationMobile;
           this.cashOnDelivery = cashOnDelivery;
           this.additionalInfo = additionalInfo;
           this.addresses = addresses;
           this.shippingAddress = shippingAddress;
           this.billingAddress = billingAddress;
           this.saleOrderItems = saleOrderItems;
           this.shippingProviders = shippingProviders;
           this.customFields = customFields;
    }


    /**
     * Gets the code value for this SaleOrder.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this SaleOrder.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the displayOrderCode value for this SaleOrder.
     * 
     * @return displayOrderCode
     */
    public java.lang.String getDisplayOrderCode() {
        return displayOrderCode;
    }


    /**
     * Sets the displayOrderCode value for this SaleOrder.
     * 
     * @param displayOrderCode
     */
    public void setDisplayOrderCode(java.lang.String displayOrderCode) {
        this.displayOrderCode = displayOrderCode;
    }


    /**
     * Gets the displayOrderDateTime value for this SaleOrder.
     * 
     * @return displayOrderDateTime
     */
    public java.util.Calendar getDisplayOrderDateTime() {
        return displayOrderDateTime;
    }


    /**
     * Sets the displayOrderDateTime value for this SaleOrder.
     * 
     * @param displayOrderDateTime
     */
    public void setDisplayOrderDateTime(java.util.Calendar displayOrderDateTime) {
        this.displayOrderDateTime = displayOrderDateTime;
    }


    /**
     * Gets the notificationEmail value for this SaleOrder.
     * 
     * @return notificationEmail
     */
    public java.lang.String getNotificationEmail() {
        return notificationEmail;
    }


    /**
     * Sets the notificationEmail value for this SaleOrder.
     * 
     * @param notificationEmail
     */
    public void setNotificationEmail(java.lang.String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }


    /**
     * Gets the notificationMobile value for this SaleOrder.
     * 
     * @return notificationMobile
     */
    public java.lang.String getNotificationMobile() {
        return notificationMobile;
    }


    /**
     * Sets the notificationMobile value for this SaleOrder.
     * 
     * @param notificationMobile
     */
    public void setNotificationMobile(java.lang.String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }


    /**
     * Gets the cashOnDelivery value for this SaleOrder.
     * 
     * @return cashOnDelivery
     */
    public java.lang.Boolean getCashOnDelivery() {
        return cashOnDelivery;
    }


    /**
     * Sets the cashOnDelivery value for this SaleOrder.
     * 
     * @param cashOnDelivery
     */
    public void setCashOnDelivery(java.lang.Boolean cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }


    /**
     * Gets the additionalInfo value for this SaleOrder.
     * 
     * @return additionalInfo
     */
    public java.lang.String getAdditionalInfo() {
        return additionalInfo;
    }


    /**
     * Sets the additionalInfo value for this SaleOrder.
     * 
     * @param additionalInfo
     */
    public void setAdditionalInfo(java.lang.String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    /**
     * Gets the addresses value for this SaleOrder.
     * 
     * @return addresses
     */
    public com.unicommerce.uniware.services.Address[] getAddresses() {
        return addresses;
    }


    /**
     * Sets the addresses value for this SaleOrder.
     * 
     * @param addresses
     */
    public void setAddresses(com.unicommerce.uniware.services.Address[] addresses) {
        this.addresses = addresses;
    }


    /**
     * Gets the shippingAddress value for this SaleOrder.
     * 
     * @return shippingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getShippingAddress() {
        return shippingAddress;
    }


    /**
     * Sets the shippingAddress value for this SaleOrder.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(com.unicommerce.uniware.services.AddressRef shippingAddress) {
        this.shippingAddress = shippingAddress;
    }


    /**
     * Gets the billingAddress value for this SaleOrder.
     * 
     * @return billingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getBillingAddress() {
        return billingAddress;
    }


    /**
     * Sets the billingAddress value for this SaleOrder.
     * 
     * @param billingAddress
     */
    public void setBillingAddress(com.unicommerce.uniware.services.AddressRef billingAddress) {
        this.billingAddress = billingAddress;
    }


    /**
     * Gets the saleOrderItems value for this SaleOrder.
     * 
     * @return saleOrderItems
     */
    public com.unicommerce.uniware.services.SaleOrderItem[] getSaleOrderItems() {
        return saleOrderItems;
    }


    /**
     * Sets the saleOrderItems value for this SaleOrder.
     * 
     * @param saleOrderItems
     */
    public void setSaleOrderItems(com.unicommerce.uniware.services.SaleOrderItem[] saleOrderItems) {
        this.saleOrderItems = saleOrderItems;
    }


    /**
     * Gets the shippingProviders value for this SaleOrder.
     * 
     * @return shippingProviders
     */
    public com.unicommerce.uniware.services.ShippingProvider[] getShippingProviders() {
        return shippingProviders;
    }


    /**
     * Sets the shippingProviders value for this SaleOrder.
     * 
     * @param shippingProviders
     */
    public void setShippingProviders(com.unicommerce.uniware.services.ShippingProvider[] shippingProviders) {
        this.shippingProviders = shippingProviders;
    }


    /**
     * Gets the customFields value for this SaleOrder.
     * 
     * @return customFields
     */
    public com.unicommerce.uniware.services.CustomFieldsCustomField[] getCustomFields() {
        return customFields;
    }


    /**
     * Sets the customFields value for this SaleOrder.
     * 
     * @param customFields
     */
    public void setCustomFields(com.unicommerce.uniware.services.CustomFieldsCustomField[] customFields) {
        this.customFields = customFields;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SaleOrder)) return false;
        SaleOrder other = (SaleOrder) obj;
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
            ((this.cashOnDelivery==null && other.getCashOnDelivery()==null) || 
             (this.cashOnDelivery!=null &&
              this.cashOnDelivery.equals(other.getCashOnDelivery()))) &&
            ((this.additionalInfo==null && other.getAdditionalInfo()==null) || 
             (this.additionalInfo!=null &&
              this.additionalInfo.equals(other.getAdditionalInfo()))) &&
            ((this.addresses==null && other.getAddresses()==null) || 
             (this.addresses!=null &&
              java.util.Arrays.equals(this.addresses, other.getAddresses()))) &&
            ((this.shippingAddress==null && other.getShippingAddress()==null) || 
             (this.shippingAddress!=null &&
              this.shippingAddress.equals(other.getShippingAddress()))) &&
            ((this.billingAddress==null && other.getBillingAddress()==null) || 
             (this.billingAddress!=null &&
              this.billingAddress.equals(other.getBillingAddress()))) &&
            ((this.saleOrderItems==null && other.getSaleOrderItems()==null) || 
             (this.saleOrderItems!=null &&
              java.util.Arrays.equals(this.saleOrderItems, other.getSaleOrderItems()))) &&
            ((this.shippingProviders==null && other.getShippingProviders()==null) || 
             (this.shippingProviders!=null &&
              java.util.Arrays.equals(this.shippingProviders, other.getShippingProviders()))) &&
            ((this.customFields==null && other.getCustomFields()==null) || 
             (this.customFields!=null &&
              java.util.Arrays.equals(this.customFields, other.getCustomFields())));
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
        if (getCashOnDelivery() != null) {
            _hashCode += getCashOnDelivery().hashCode();
        }
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
        if (getShippingAddress() != null) {
            _hashCode += getShippingAddress().hashCode();
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
        if (getShippingProviders() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getShippingProviders());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getShippingProviders(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCustomFields() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCustomFields());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCustomFields(), i);
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
        new org.apache.axis.description.TypeDesc(SaleOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrder"));
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
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrderDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DisplayOrderDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("cashOnDelivery");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CashOnDelivery"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("shippingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddressRef"));
        elemField.setNillable(false);
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
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingProviders");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProviders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CustomFields>CustomField"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomField"));
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
