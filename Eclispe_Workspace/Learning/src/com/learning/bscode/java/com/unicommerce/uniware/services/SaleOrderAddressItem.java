/**
 * SaleOrderAddressItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SaleOrderAddressItem  implements java.io.Serializable {
    private java.lang.String saleOrderItemCode;

    private com.unicommerce.uniware.services.AddressRef shippingAddress;

    public SaleOrderAddressItem() {
    }

    public SaleOrderAddressItem(
           java.lang.String saleOrderItemCode,
           com.unicommerce.uniware.services.AddressRef shippingAddress) {
           this.saleOrderItemCode = saleOrderItemCode;
           this.shippingAddress = shippingAddress;
    }


    /**
     * Gets the saleOrderItemCode value for this SaleOrderAddressItem.
     * 
     * @return saleOrderItemCode
     */
    public java.lang.String getSaleOrderItemCode() {
        return saleOrderItemCode;
    }


    /**
     * Sets the saleOrderItemCode value for this SaleOrderAddressItem.
     * 
     * @param saleOrderItemCode
     */
    public void setSaleOrderItemCode(java.lang.String saleOrderItemCode) {
        this.saleOrderItemCode = saleOrderItemCode;
    }


    /**
     * Gets the shippingAddress value for this SaleOrderAddressItem.
     * 
     * @return shippingAddress
     */
    public com.unicommerce.uniware.services.AddressRef getShippingAddress() {
        return shippingAddress;
    }


    /**
     * Sets the shippingAddress value for this SaleOrderAddressItem.
     * 
     * @param shippingAddress
     */
    public void setShippingAddress(com.unicommerce.uniware.services.AddressRef shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SaleOrderAddressItem)) return false;
        SaleOrderAddressItem other = (SaleOrderAddressItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.saleOrderItemCode==null && other.getSaleOrderItemCode()==null) || 
             (this.saleOrderItemCode!=null &&
              this.saleOrderItemCode.equals(other.getSaleOrderItemCode()))) &&
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
        if (getSaleOrderItemCode() != null) {
            _hashCode += getSaleOrderItemCode().hashCode();
        }
        if (getShippingAddress() != null) {
            _hashCode += getShippingAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SaleOrderAddressItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddressItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrderItemCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItemCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "AddressRef"));
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
