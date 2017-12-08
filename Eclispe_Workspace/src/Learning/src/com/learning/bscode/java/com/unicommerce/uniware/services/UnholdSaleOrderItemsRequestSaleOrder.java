/**
 * UnholdSaleOrderItemsRequestSaleOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class UnholdSaleOrderItemsRequestSaleOrder  implements java.io.Serializable {
    private java.lang.String code;

    private com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems;

    public UnholdSaleOrderItemsRequestSaleOrder() {
    }

    public UnholdSaleOrderItemsRequestSaleOrder(
           java.lang.String code,
           com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems) {
           this.code = code;
           this.saleOrderItems = saleOrderItems;
    }


    /**
     * Gets the code value for this UnholdSaleOrderItemsRequestSaleOrder.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this UnholdSaleOrderItemsRequestSaleOrder.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the saleOrderItems value for this UnholdSaleOrderItemsRequestSaleOrder.
     * 
     * @return saleOrderItems
     */
    public com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[] getSaleOrderItems() {
        return saleOrderItems;
    }


    /**
     * Sets the saleOrderItems value for this UnholdSaleOrderItemsRequestSaleOrder.
     * 
     * @param saleOrderItems
     */
    public void setSaleOrderItems(com.unicommerce.uniware.services.UnholdSaleOrderItemsRequestSaleOrderSaleOrderItemsSaleOrderItem[] saleOrderItems) {
        this.saleOrderItems = saleOrderItems;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UnholdSaleOrderItemsRequestSaleOrder)) return false;
        UnholdSaleOrderItemsRequestSaleOrder other = (UnholdSaleOrderItemsRequestSaleOrder) obj;
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
            ((this.saleOrderItems==null && other.getSaleOrderItems()==null) || 
             (this.saleOrderItems!=null &&
              java.util.Arrays.equals(this.saleOrderItems, other.getSaleOrderItems())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UnholdSaleOrderItemsRequestSaleOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>UnholdSaleOrderItemsRequest>SaleOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrderItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>UnholdSaleOrderItemsRequest>SaleOrder>SaleOrderItems>SaleOrderItem"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrderItem"));
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
