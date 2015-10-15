/**
 * GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage  implements java.io.Serializable {
    private java.lang.String shipmentCode;

    private java.lang.String statusCode;

    private java.lang.String shippingPackageType;

    private java.lang.String shippingProvider;

    private java.lang.String trackingNumber;

    private java.lang.Integer estimatedWeight;

    private java.lang.Integer actualWeight;

    private java.util.Calendar dispatchedOn;

    private java.util.Calendar deliveredOn;

    private java.util.Calendar createdOn;

    public GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage() {
    }

    public GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage(
           java.lang.String shipmentCode,
           java.lang.String statusCode,
           java.lang.String shippingPackageType,
           java.lang.String shippingProvider,
           java.lang.String trackingNumber,
           java.lang.Integer estimatedWeight,
           java.lang.Integer actualWeight,
           java.util.Calendar dispatchedOn,
           java.util.Calendar deliveredOn,
           java.util.Calendar createdOn) {
           this.shipmentCode = shipmentCode;
           this.statusCode = statusCode;
           this.shippingPackageType = shippingPackageType;
           this.shippingProvider = shippingProvider;
           this.trackingNumber = trackingNumber;
           this.estimatedWeight = estimatedWeight;
           this.actualWeight = actualWeight;
           this.dispatchedOn = dispatchedOn;
           this.deliveredOn = deliveredOn;
           this.createdOn = createdOn;
    }


    /**
     * Gets the shipmentCode value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return shipmentCode
     */
    public java.lang.String getShipmentCode() {
        return shipmentCode;
    }


    /**
     * Sets the shipmentCode value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param shipmentCode
     */
    public void setShipmentCode(java.lang.String shipmentCode) {
        this.shipmentCode = shipmentCode;
    }


    /**
     * Gets the statusCode value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return statusCode
     */
    public java.lang.String getStatusCode() {
        return statusCode;
    }


    /**
     * Sets the statusCode value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param statusCode
     */
    public void setStatusCode(java.lang.String statusCode) {
        this.statusCode = statusCode;
    }


    /**
     * Gets the shippingPackageType value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return shippingPackageType
     */
    public java.lang.String getShippingPackageType() {
        return shippingPackageType;
    }


    /**
     * Sets the shippingPackageType value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param shippingPackageType
     */
    public void setShippingPackageType(java.lang.String shippingPackageType) {
        this.shippingPackageType = shippingPackageType;
    }


    /**
     * Gets the shippingProvider value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return shippingProvider
     */
    public java.lang.String getShippingProvider() {
        return shippingProvider;
    }


    /**
     * Sets the shippingProvider value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param shippingProvider
     */
    public void setShippingProvider(java.lang.String shippingProvider) {
        this.shippingProvider = shippingProvider;
    }


    /**
     * Gets the trackingNumber value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return trackingNumber
     */
    public java.lang.String getTrackingNumber() {
        return trackingNumber;
    }


    /**
     * Sets the trackingNumber value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param trackingNumber
     */
    public void setTrackingNumber(java.lang.String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }


    /**
     * Gets the estimatedWeight value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return estimatedWeight
     */
    public java.lang.Integer getEstimatedWeight() {
        return estimatedWeight;
    }


    /**
     * Sets the estimatedWeight value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param estimatedWeight
     */
    public void setEstimatedWeight(java.lang.Integer estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }


    /**
     * Gets the actualWeight value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return actualWeight
     */
    public java.lang.Integer getActualWeight() {
        return actualWeight;
    }


    /**
     * Sets the actualWeight value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param actualWeight
     */
    public void setActualWeight(java.lang.Integer actualWeight) {
        this.actualWeight = actualWeight;
    }


    /**
     * Gets the dispatchedOn value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return dispatchedOn
     */
    public java.util.Calendar getDispatchedOn() {
        return dispatchedOn;
    }


    /**
     * Sets the dispatchedOn value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param dispatchedOn
     */
    public void setDispatchedOn(java.util.Calendar dispatchedOn) {
        this.dispatchedOn = dispatchedOn;
    }


    /**
     * Gets the deliveredOn value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return deliveredOn
     */
    public java.util.Calendar getDeliveredOn() {
        return deliveredOn;
    }


    /**
     * Sets the deliveredOn value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param deliveredOn
     */
    public void setDeliveredOn(java.util.Calendar deliveredOn) {
        this.deliveredOn = deliveredOn;
    }


    /**
     * Gets the createdOn value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @return createdOn
     */
    public java.util.Calendar getCreatedOn() {
        return createdOn;
    }


    /**
     * Sets the createdOn value for this GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.
     * 
     * @param createdOn
     */
    public void setCreatedOn(java.util.Calendar createdOn) {
        this.createdOn = createdOn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage)) return false;
        GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage other = (GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.shipmentCode==null && other.getShipmentCode()==null) || 
             (this.shipmentCode!=null &&
              this.shipmentCode.equals(other.getShipmentCode()))) &&
            ((this.statusCode==null && other.getStatusCode()==null) || 
             (this.statusCode!=null &&
              this.statusCode.equals(other.getStatusCode()))) &&
            ((this.shippingPackageType==null && other.getShippingPackageType()==null) || 
             (this.shippingPackageType!=null &&
              this.shippingPackageType.equals(other.getShippingPackageType()))) &&
            ((this.shippingProvider==null && other.getShippingProvider()==null) || 
             (this.shippingProvider!=null &&
              this.shippingProvider.equals(other.getShippingProvider()))) &&
            ((this.trackingNumber==null && other.getTrackingNumber()==null) || 
             (this.trackingNumber!=null &&
              this.trackingNumber.equals(other.getTrackingNumber()))) &&
            ((this.estimatedWeight==null && other.getEstimatedWeight()==null) || 
             (this.estimatedWeight!=null &&
              this.estimatedWeight.equals(other.getEstimatedWeight()))) &&
            ((this.actualWeight==null && other.getActualWeight()==null) || 
             (this.actualWeight!=null &&
              this.actualWeight.equals(other.getActualWeight()))) &&
            ((this.dispatchedOn==null && other.getDispatchedOn()==null) || 
             (this.dispatchedOn!=null &&
              this.dispatchedOn.equals(other.getDispatchedOn()))) &&
            ((this.deliveredOn==null && other.getDeliveredOn()==null) || 
             (this.deliveredOn!=null &&
              this.deliveredOn.equals(other.getDeliveredOn()))) &&
            ((this.createdOn==null && other.getCreatedOn()==null) || 
             (this.createdOn!=null &&
              this.createdOn.equals(other.getCreatedOn())));
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
        if (getShipmentCode() != null) {
            _hashCode += getShipmentCode().hashCode();
        }
        if (getStatusCode() != null) {
            _hashCode += getStatusCode().hashCode();
        }
        if (getShippingPackageType() != null) {
            _hashCode += getShippingPackageType().hashCode();
        }
        if (getShippingProvider() != null) {
            _hashCode += getShippingProvider().hashCode();
        }
        if (getTrackingNumber() != null) {
            _hashCode += getTrackingNumber().hashCode();
        }
        if (getEstimatedWeight() != null) {
            _hashCode += getEstimatedWeight().hashCode();
        }
        if (getActualWeight() != null) {
            _hashCode += getActualWeight().hashCode();
        }
        if (getDispatchedOn() != null) {
            _hashCode += getDispatchedOn().hashCode();
        }
        if (getDeliveredOn() != null) {
            _hashCode += getDeliveredOn().hashCode();
        }
        if (getCreatedOn() != null) {
            _hashCode += getCreatedOn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetSaleOrderResponseSaleOrderShippingPackagesShippingPackage.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>GetSaleOrderResponse>SaleOrder>ShippingPackages>ShippingPackage"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shipmentCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShipmentCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("statusCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "StatusCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingPackageType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingPackageType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shippingProvider");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShippingProvider"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("trackingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "TrackingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("estimatedWeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "EstimatedWeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("actualWeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ActualWeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dispatchedOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DispatchedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deliveredOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DeliveredOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreatedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
