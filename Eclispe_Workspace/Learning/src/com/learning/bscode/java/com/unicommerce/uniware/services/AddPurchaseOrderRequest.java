/**
 * AddPurchaseOrderRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class AddPurchaseOrderRequest  implements java.io.Serializable {
    private java.lang.String vendorCode;

    private java.lang.String vendorAgreementName;

    private com.unicommerce.uniware.services.PurchaseOrderItem[] purchaseOrderItems;

    public AddPurchaseOrderRequest() {
    }

    public AddPurchaseOrderRequest(
           java.lang.String vendorCode,
           java.lang.String vendorAgreementName,
           com.unicommerce.uniware.services.PurchaseOrderItem[] purchaseOrderItems) {
           this.vendorCode = vendorCode;
           this.vendorAgreementName = vendorAgreementName;
           this.purchaseOrderItems = purchaseOrderItems;
    }


    /**
     * Gets the vendorCode value for this AddPurchaseOrderRequest.
     * 
     * @return vendorCode
     */
    public java.lang.String getVendorCode() {
        return vendorCode;
    }


    /**
     * Sets the vendorCode value for this AddPurchaseOrderRequest.
     * 
     * @param vendorCode
     */
    public void setVendorCode(java.lang.String vendorCode) {
        this.vendorCode = vendorCode;
    }


    /**
     * Gets the vendorAgreementName value for this AddPurchaseOrderRequest.
     * 
     * @return vendorAgreementName
     */
    public java.lang.String getVendorAgreementName() {
        return vendorAgreementName;
    }


    /**
     * Sets the vendorAgreementName value for this AddPurchaseOrderRequest.
     * 
     * @param vendorAgreementName
     */
    public void setVendorAgreementName(java.lang.String vendorAgreementName) {
        this.vendorAgreementName = vendorAgreementName;
    }


    /**
     * Gets the purchaseOrderItems value for this AddPurchaseOrderRequest.
     * 
     * @return purchaseOrderItems
     */
    public com.unicommerce.uniware.services.PurchaseOrderItem[] getPurchaseOrderItems() {
        return purchaseOrderItems;
    }


    /**
     * Sets the purchaseOrderItems value for this AddPurchaseOrderRequest.
     * 
     * @param purchaseOrderItems
     */
    public void setPurchaseOrderItems(com.unicommerce.uniware.services.PurchaseOrderItem[] purchaseOrderItems) {
        this.purchaseOrderItems = purchaseOrderItems;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddPurchaseOrderRequest)) return false;
        AddPurchaseOrderRequest other = (AddPurchaseOrderRequest) obj;
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
            ((this.vendorAgreementName==null && other.getVendorAgreementName()==null) || 
             (this.vendorAgreementName!=null &&
              this.vendorAgreementName.equals(other.getVendorAgreementName()))) &&
            ((this.purchaseOrderItems==null && other.getPurchaseOrderItems()==null) || 
             (this.purchaseOrderItems!=null &&
              java.util.Arrays.equals(this.purchaseOrderItems, other.getPurchaseOrderItems())));
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
        if (getVendorAgreementName() != null) {
            _hashCode += getVendorAgreementName().hashCode();
        }
        if (getPurchaseOrderItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPurchaseOrderItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPurchaseOrderItems(), i);
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
        new org.apache.axis.description.TypeDesc(AddPurchaseOrderRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">AddPurchaseOrderRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "VendorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("vendorAgreementName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "VendorAgreementName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("purchaseOrderItems");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItem"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PurchaseOrderItem"));
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
