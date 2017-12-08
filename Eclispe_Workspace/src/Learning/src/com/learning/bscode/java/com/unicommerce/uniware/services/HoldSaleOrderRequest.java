/**
 * HoldSaleOrderRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class HoldSaleOrderRequest  implements java.io.Serializable {
    private com.unicommerce.uniware.services.HoldSaleOrderRequestSaleOrder saleOrder;

    public HoldSaleOrderRequest() {
    }

    public HoldSaleOrderRequest(
           com.unicommerce.uniware.services.HoldSaleOrderRequestSaleOrder saleOrder) {
           this.saleOrder = saleOrder;
    }


    /**
     * Gets the saleOrder value for this HoldSaleOrderRequest.
     * 
     * @return saleOrder
     */
    public com.unicommerce.uniware.services.HoldSaleOrderRequestSaleOrder getSaleOrder() {
        return saleOrder;
    }


    /**
     * Sets the saleOrder value for this HoldSaleOrderRequest.
     * 
     * @param saleOrder
     */
    public void setSaleOrder(com.unicommerce.uniware.services.HoldSaleOrderRequestSaleOrder saleOrder) {
        this.saleOrder = saleOrder;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof HoldSaleOrderRequest)) return false;
        HoldSaleOrderRequest other = (HoldSaleOrderRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.saleOrder==null && other.getSaleOrder()==null) || 
             (this.saleOrder!=null &&
              this.saleOrder.equals(other.getSaleOrder())));
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
        if (getSaleOrder() != null) {
            _hashCode += getSaleOrder().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(HoldSaleOrderRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">HoldSaleOrderRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrder");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrder"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>HoldSaleOrderRequest>SaleOrder"));
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
