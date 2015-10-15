/**
 * PurchaseOrderItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class PurchaseOrderItem  implements java.io.Serializable {
    private java.lang.String itemSKU;

    private java.math.BigInteger quantity;

    private java.math.BigDecimal unitPrice;

    private java.math.BigDecimal maxRetailPrice;

    private java.math.BigDecimal discount;

    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(
           java.lang.String itemSKU,
           java.math.BigInteger quantity,
           java.math.BigDecimal unitPrice,
           java.math.BigDecimal maxRetailPrice,
           java.math.BigDecimal discount) {
           this.itemSKU = itemSKU;
           this.quantity = quantity;
           this.unitPrice = unitPrice;
           this.maxRetailPrice = maxRetailPrice;
           this.discount = discount;
    }


    /**
     * Gets the itemSKU value for this PurchaseOrderItem.
     * 
     * @return itemSKU
     */
    public java.lang.String getItemSKU() {
        return itemSKU;
    }


    /**
     * Sets the itemSKU value for this PurchaseOrderItem.
     * 
     * @param itemSKU
     */
    public void setItemSKU(java.lang.String itemSKU) {
        this.itemSKU = itemSKU;
    }


    /**
     * Gets the quantity value for this PurchaseOrderItem.
     * 
     * @return quantity
     */
    public java.math.BigInteger getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this PurchaseOrderItem.
     * 
     * @param quantity
     */
    public void setQuantity(java.math.BigInteger quantity) {
        this.quantity = quantity;
    }


    /**
     * Gets the unitPrice value for this PurchaseOrderItem.
     * 
     * @return unitPrice
     */
    public java.math.BigDecimal getUnitPrice() {
        return unitPrice;
    }


    /**
     * Sets the unitPrice value for this PurchaseOrderItem.
     * 
     * @param unitPrice
     */
    public void setUnitPrice(java.math.BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }


    /**
     * Gets the maxRetailPrice value for this PurchaseOrderItem.
     * 
     * @return maxRetailPrice
     */
    public java.math.BigDecimal getMaxRetailPrice() {
        return maxRetailPrice;
    }


    /**
     * Sets the maxRetailPrice value for this PurchaseOrderItem.
     * 
     * @param maxRetailPrice
     */
    public void setMaxRetailPrice(java.math.BigDecimal maxRetailPrice) {
        this.maxRetailPrice = maxRetailPrice;
    }


    /**
     * Gets the discount value for this PurchaseOrderItem.
     * 
     * @return discount
     */
    public java.math.BigDecimal getDiscount() {
        return discount;
    }


    /**
     * Sets the discount value for this PurchaseOrderItem.
     * 
     * @param discount
     */
    public void setDiscount(java.math.BigDecimal discount) {
        this.discount = discount;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PurchaseOrderItem)) return false;
        PurchaseOrderItem other = (PurchaseOrderItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.itemSKU==null && other.getItemSKU()==null) || 
             (this.itemSKU!=null &&
              this.itemSKU.equals(other.getItemSKU()))) &&
            ((this.quantity==null && other.getQuantity()==null) || 
             (this.quantity!=null &&
              this.quantity.equals(other.getQuantity()))) &&
            ((this.unitPrice==null && other.getUnitPrice()==null) || 
             (this.unitPrice!=null &&
              this.unitPrice.equals(other.getUnitPrice()))) &&
            ((this.maxRetailPrice==null && other.getMaxRetailPrice()==null) || 
             (this.maxRetailPrice!=null &&
              this.maxRetailPrice.equals(other.getMaxRetailPrice()))) &&
            ((this.discount==null && other.getDiscount()==null) || 
             (this.discount!=null &&
              this.discount.equals(other.getDiscount())));
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
        if (getItemSKU() != null) {
            _hashCode += getItemSKU().hashCode();
        }
        if (getQuantity() != null) {
            _hashCode += getQuantity().hashCode();
        }
        if (getUnitPrice() != null) {
            _hashCode += getUnitPrice().hashCode();
        }
        if (getMaxRetailPrice() != null) {
            _hashCode += getMaxRetailPrice().hashCode();
        }
        if (getDiscount() != null) {
            _hashCode += getDiscount().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PurchaseOrderItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemSKU");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemSKU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UnitPrice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxRetailPrice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "MaxRetailPrice"));
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
