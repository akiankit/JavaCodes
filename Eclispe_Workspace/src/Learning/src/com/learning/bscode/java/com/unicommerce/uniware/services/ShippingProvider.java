/**
 * ShippingProvider.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class ShippingProvider  implements java.io.Serializable {
    private java.math.BigInteger packetNumber;

    private java.lang.String code;

    private java.lang.String trackingNumber;

    public ShippingProvider() {
    }

    public ShippingProvider(
           java.math.BigInteger packetNumber,
           java.lang.String code,
           java.lang.String trackingNumber) {
           this.packetNumber = packetNumber;
           this.code = code;
           this.trackingNumber = trackingNumber;
    }


    /**
     * Gets the packetNumber value for this ShippingProvider.
     * 
     * @return packetNumber
     */
    public java.math.BigInteger getPacketNumber() {
        return packetNumber;
    }


    /**
     * Sets the packetNumber value for this ShippingProvider.
     * 
     * @param packetNumber
     */
    public void setPacketNumber(java.math.BigInteger packetNumber) {
        this.packetNumber = packetNumber;
    }


    /**
     * Gets the code value for this ShippingProvider.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this ShippingProvider.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the trackingNumber value for this ShippingProvider.
     * 
     * @return trackingNumber
     */
    public java.lang.String getTrackingNumber() {
        return trackingNumber;
    }


    /**
     * Sets the trackingNumber value for this ShippingProvider.
     * 
     * @param trackingNumber
     */
    public void setTrackingNumber(java.lang.String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ShippingProvider)) return false;
        ShippingProvider other = (ShippingProvider) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.packetNumber==null && other.getPacketNumber()==null) || 
             (this.packetNumber!=null &&
              this.packetNumber.equals(other.getPacketNumber()))) &&
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            ((this.trackingNumber==null && other.getTrackingNumber()==null) || 
             (this.trackingNumber!=null &&
              this.trackingNumber.equals(other.getTrackingNumber())));
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
        if (getPacketNumber() != null) {
            _hashCode += getPacketNumber().hashCode();
        }
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        if (getTrackingNumber() != null) {
            _hashCode += getTrackingNumber().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ShippingProvider.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("packetNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "PacketNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "integer"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trackingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "TrackingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
