/**
 * EditSaleOrderAddressRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class EditSaleOrderAddressRequest  implements java.io.Serializable {
    private com.unicommerce.uniware.services.SaleOrderAddress saleOrderAddress;

    public EditSaleOrderAddressRequest() {
    }

    public EditSaleOrderAddressRequest(
           com.unicommerce.uniware.services.SaleOrderAddress saleOrderAddress) {
           this.saleOrderAddress = saleOrderAddress;
    }


    /**
     * Gets the saleOrderAddress value for this EditSaleOrderAddressRequest.
     * 
     * @return saleOrderAddress
     */
    public com.unicommerce.uniware.services.SaleOrderAddress getSaleOrderAddress() {
        return saleOrderAddress;
    }


    /**
     * Sets the saleOrderAddress value for this EditSaleOrderAddressRequest.
     * 
     * @param saleOrderAddress
     */
    public void setSaleOrderAddress(com.unicommerce.uniware.services.SaleOrderAddress saleOrderAddress) {
        this.saleOrderAddress = saleOrderAddress;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EditSaleOrderAddressRequest)) return false;
        EditSaleOrderAddressRequest other = (EditSaleOrderAddressRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.saleOrderAddress==null && other.getSaleOrderAddress()==null) || 
             (this.saleOrderAddress!=null &&
              this.saleOrderAddress.equals(other.getSaleOrderAddress())));
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
        if (getSaleOrderAddress() != null) {
            _hashCode += getSaleOrderAddress().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EditSaleOrderAddressRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">EditSaleOrderAddressRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrderAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderAddress"));
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
