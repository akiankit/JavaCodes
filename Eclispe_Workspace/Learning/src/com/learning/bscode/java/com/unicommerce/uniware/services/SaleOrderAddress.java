/**
 * SaleOrderAddress.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SaleOrderAddress  implements java.io.Serializable {
    private java.lang.String saleOrderCode;

    private com.unicommerce.uniware.services.Address[] addresses;

    private com.unicommerce.uniware.services.AddressRef shippingAddress;

    private com.unicommerce.uniware.services.AddressRef billingAddress;

    private com.unicommerce.uniware.services.SaleOrderAddressItem[] saleOrderAddressItems;

    public SaleOrderAddress() {
    }

    public SaleOrderAddress(
           java.lang.String saleOrderCode,
           com.unicommerce.uniware.services.Address[] addresses,
           com.unicommerce.uniware.services.AddressRef shippingAddress,
           com.unicommerce.uniware.services.AddressRef billingAddress,
           com.unicommerce.uniware.services.SaleOrderAddressItem[] saleOrderAddressItems) {
           this.saleOrderCode = saleOrderCode;
           this.addresses = addresses;
           this.shippingAddress = shippingAddress;
           this.billingAddress = billingAddress;
           this.saleOrderAddressItems = saleOrderAddressItems;
    }


    /**
     * Gets the saleOrderCode value for this SaleOrderAddress.
     * 
     * @return saleOrderCode
     */
    public java.lang.String getSaleOrderCode() {
        return saleOrderCode;
    }


    /**
     * Sets the saleOrderCode value for this SaleOrderAddress.
     * 
     * @param saleOrderCode
     */
    public void setSaleOrderCode(java.lang.String saleOrderCode) {
        this.saleOrderCode = saleOrderCode;
    }


    /**
     * Gets the addresses value for this SaleOrderAddress.
     * 
     * @return addresses
     */
    public com.unicommerce.uniware.services.Address[] getAddresses() {
        return addresses;
    }


    /**
     * Sets the addresses value for this SaleOrderAddress.
     * 
     * @param addresses
     */
    public void setAddresses(com.unicommerce.uniware.services.Address[] addresses) {
        this.addresses = addresses;
    }


    /**
     * Gets the shippingAddress value for this SaleOrderAddress.
     * 
     * @return shippingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getShippingAddress() {
        return shippingAddress;
    }


    /**
     * Sets the shippingAddress value for this SaleOrderAddress.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(com.unicommerce.uniware.services.AddressRef shippingAddress) {
        this.shippingAddress = shippingAddress;
    }


    /**
     * Gets the billingAddress value for this SaleOrderAddress.
     * 
     * @return billingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getBillingAddress() {
        return billingAddress;
    }


    /**
     * Sets the billingAddress value for this SaleOrderAddress.
     * 
     * @param billingAddress
     */
    public void setBillingAddress(com.unicommerce.uniware.services.AddressRef billingAddress) {
        this.billingAddress = billingAddress;
    }


    /**
     * Gets the saleOrderAddressItems value for this SaleOrderAddress.
     * 
     * @return saleOrderAddressItems
     */
    public com.unicommerce.uniware.services.SaleOrderAddressItem[] getSaleOrderAddressItems() {
        return saleOrderAddressItems;
    }


    /**
     * Sets the saleOrderAddressItems value for this SaleOrderAddress.
     * 
     * @param saleOrderAddressItems
     */
    public void setSaleOrderAddressItems(com.unicommerce.uniware.services.SaleOrderAddressItem[] saleOrderAddressItems) {
        this.saleOrderAddressItems = saleOrderAddressItems;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SaleOrderAddress)) return false;
        SaleOrderAddress other = (SaleOrderAddress) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.saleOrderCode==null && other.getSaleOrderCode()==null) || 
             (this.saleOrderCode!=null &&
              this.saleOrderCode.equals(other.getSaleOrderCode()))) &&
            ((this.addresses==null && other.getAddresses()==null) || 
             (this.addresses!=null &&
              java.util.Arrays.equals(this.addresses, other.getAddresses()))) &&
            ((this.shippingAddress==null && other.getShippingAddress()==null) || 
             (this.shippingAddress!=null &&
              this.shippingAddress.equals(other.getShippingAddress()))) &&
            ((this.billingAddress==null && other.getBillingAddress()==null) || 
             (this.billingAddress!=null &&
              this.billingAddress.equals(other.getBillingAddress()))) &&
            ((this.saleOrderAddressItems==null && other.getSaleOrderAddressItems()==null) || 
             (this.saleOrderAddressItems!=null &&
              java.util.Arrays.equals(this.saleOrderAddressItems, other.getSaleOrderAddressItems())));
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
        if (getSaleOrderCode() != null) {
            _hashCode += getSaleOrderCode().hashCode();
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
        if (getSaleOrderAddressItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSaleOrderAddressItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSaleOrderAddressItems(), i);
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
        new org.apache.axis.description.TypeDesc(SaleOrderAddress.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddress"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrderCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("saleOrderAddressItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItem"));
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
