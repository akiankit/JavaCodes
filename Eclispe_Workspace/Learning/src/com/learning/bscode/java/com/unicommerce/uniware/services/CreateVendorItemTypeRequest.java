/**
 * CreateVendorItemTypeRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class CreateVendorItemTypeRequest  implements java.io.Serializable {
    private java.lang.String vendorCode;

    private java.lang.String itemSKU;

    private java.lang.String vendorItemSKU;

    private java.math.BigDecimal unitPrice;

    private java.lang.Integer priority;

    public CreateVendorItemTypeRequest() {
    }

    public CreateVendorItemTypeRequest(
           java.lang.String vendorCode,
           java.lang.String itemSKU,
           java.lang.String vendorItemSKU,
           java.math.BigDecimal unitPrice,
           java.lang.Integer priority) {
           this.vendorCode = vendorCode;
           this.itemSKU = itemSKU;
           this.vendorItemSKU = vendorItemSKU;
           this.unitPrice = unitPrice;
           this.priority = priority;
    }


    /**
     * Gets the vendorCode value for this CreateVendorItemTypeRequest.
     * 
     * @return vendorCode
     */
    public java.lang.String getVendorCode() {
        return vendorCode;
    }


    /**
     * Sets the vendorCode value for this CreateVendorItemTypeRequest.
     * 
     * @param vendorCode
     */
    public void setVendorCode(java.lang.String vendorCode) {
        this.vendorCode = vendorCode;
    }


    /**
     * Gets the itemSKU value for this CreateVendorItemTypeRequest.
     * 
     * @return itemSKU
     */
    public java.lang.String getItemSKU() {
        return itemSKU;
    }


    /**
     * Sets the itemSKU value for this CreateVendorItemTypeRequest.
     * 
     * @param itemSKU
     */
    public void setItemSKU(java.lang.String itemSKU) {
        this.itemSKU = itemSKU;
    }


    /**
     * Gets the vendorItemSKU value for this CreateVendorItemTypeRequest.
     * 
     * @return vendorItemSKU
     */
    public java.lang.String getVendorItemSKU() {
        return vendorItemSKU;
    }


    /**
     * Sets the vendorItemSKU value for this CreateVendorItemTypeRequest.
     * 
     * @param vendorItemSKU
     */
    public void setVendorItemSKU(java.lang.String vendorItemSKU) {
        this.vendorItemSKU = vendorItemSKU;
    }


    /**
     * Gets the unitPrice value for this CreateVendorItemTypeRequest.
     * 
     * @return unitPrice
     */
    public java.math.BigDecimal getUnitPrice() {
        return unitPrice;
    }


    /**
     * Sets the unitPrice value for this CreateVendorItemTypeRequest.
     * 
     * @param unitPrice
     */
    public void setUnitPrice(java.math.BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }


    /**
     * Gets the priority value for this CreateVendorItemTypeRequest.
     * 
     * @return priority
     */
    public java.lang.Integer getPriority() {
        return priority;
    }


    /**
     * Sets the priority value for this CreateVendorItemTypeRequest.
     * 
     * @param priority
     */
    public void setPriority(java.lang.Integer priority) {
        this.priority = priority;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateVendorItemTypeRequest)) return false;
        CreateVendorItemTypeRequest other = (CreateVendorItemTypeRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.vendorCode==null && other.getVendorCode()==null) || 
             (this.vendorCode!=null &&
              this.vendorCode.equals(other.getVendorCode()))) &&
            ((this.itemSKU==null && other.getItemSKU()==null) || 
             (this.itemSKU!=null &&
              this.itemSKU.equals(other.getItemSKU()))) &&
            ((this.vendorItemSKU==null && other.getVendorItemSKU()==null) || 
             (this.vendorItemSKU!=null &&
              this.vendorItemSKU.equals(other.getVendorItemSKU()))) &&
            ((this.unitPrice==null && other.getUnitPrice()==null) || 
             (this.unitPrice!=null &&
              this.unitPrice.equals(other.getUnitPrice()))) &&
            ((this.priority==null && other.getPriority()==null) || 
             (this.priority!=null &&
              this.priority.equals(other.getPriority())));
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
        if (getVendorCode() != null) {
            _hashCode += getVendorCode().hashCode();
        }
        if (getItemSKU() != null) {
            _hashCode += getItemSKU().hashCode();
        }
        if (getVendorItemSKU() != null) {
            _hashCode += getVendorItemSKU().hashCode();
        }
        if (getUnitPrice() != null) {
            _hashCode += getUnitPrice().hashCode();
        }
        if (getPriority() != null) {
            _hashCode += getPriority().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateVendorItemTypeRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateVendorItemTypeRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "VendorCode"));
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
        elemField.setFieldName("vendorItemSKU");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "VendorItemSKU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnitPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("priority");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Priority"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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

}
