/**
 * SearchSaleOrderResponseSaleOrdersSaleOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SearchSaleOrderResponseSaleOrdersSaleOrder  implements java.io.Serializable {
    private java.lang.String code;

    private java.lang.String displayOrderCode;

    private java.util.Calendar displayOrderDateTime;

    private java.lang.String notificationMobile;

    private java.lang.String notificationEmail;

    private java.lang.String status;

    private java.util.Calendar createdOn;

    private java.util.Calendar updatedOn;

    public SearchSaleOrderResponseSaleOrdersSaleOrder() {
    }

    public SearchSaleOrderResponseSaleOrdersSaleOrder(
           java.lang.String code,
           java.lang.String displayOrderCode,
           java.util.Calendar displayOrderDateTime,
           java.lang.String notificationMobile,
           java.lang.String notificationEmail,
           java.lang.String status,
           java.util.Calendar createdOn,
           java.util.Calendar updatedOn) {
           this.code = code;
           this.displayOrderCode = displayOrderCode;
           this.displayOrderDateTime = displayOrderDateTime;
           this.notificationMobile = notificationMobile;
           this.notificationEmail = notificationEmail;
           this.status = status;
           this.createdOn = createdOn;
           this.updatedOn = updatedOn;
    }


    /**
     * Gets the code value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the displayOrderCode value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return displayOrderCode
     */
    public java.lang.String getDisplayOrderCode() {
        return displayOrderCode;
    }


    /**
     * Sets the displayOrderCode value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param displayOrderCode
     */
    public void setDisplayOrderCode(java.lang.String displayOrderCode) {
        this.displayOrderCode = displayOrderCode;
    }


    /**
     * Gets the displayOrderDateTime value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return displayOrderDateTime
     */
    public java.util.Calendar getDisplayOrderDateTime() {
        return displayOrderDateTime;
    }


    /**
     * Sets the displayOrderDateTime value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param displayOrderDateTime
     */
    public void setDisplayOrderDateTime(java.util.Calendar displayOrderDateTime) {
        this.displayOrderDateTime = displayOrderDateTime;
    }


    /**
     * Gets the notificationMobile value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return notificationMobile
     */
    public java.lang.String getNotificationMobile() {
        return notificationMobile;
    }


    /**
     * Sets the notificationMobile value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param notificationMobile
     */
    public void setNotificationMobile(java.lang.String notificationMobile) {
        this.notificationMobile = notificationMobile;
    }


    /**
     * Gets the notificationEmail value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return notificationEmail
     */
    public java.lang.String getNotificationEmail() {
        return notificationEmail;
    }


    /**
     * Sets the notificationEmail value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param notificationEmail
     */
    public void setNotificationEmail(java.lang.String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }


    /**
     * Gets the status value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the createdOn value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return createdOn
     */
    public java.util.Calendar getCreatedOn() {
        return createdOn;
    }


    /**
     * Sets the createdOn value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param createdOn
     */
    public void setCreatedOn(java.util.Calendar createdOn) {
        this.createdOn = createdOn;
    }


    /**
     * Gets the updatedOn value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @return updatedOn
     */
    public java.util.Calendar getUpdatedOn() {
        return updatedOn;
    }


    /**
     * Sets the updatedOn value for this SearchSaleOrderResponseSaleOrdersSaleOrder.
     * 
     * @param updatedOn
     */
    public void setUpdatedOn(java.util.Calendar updatedOn) {
        this.updatedOn = updatedOn;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchSaleOrderResponseSaleOrdersSaleOrder)) return false;
        SearchSaleOrderResponseSaleOrdersSaleOrder other = (SearchSaleOrderResponseSaleOrdersSaleOrder) obj;
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
            ((this.displayOrderCode==null && other.getDisplayOrderCode()==null) || 
             (this.displayOrderCode!=null &&
              this.displayOrderCode.equals(other.getDisplayOrderCode()))) &&
            ((this.displayOrderDateTime==null && other.getDisplayOrderDateTime()==null) || 
             (this.displayOrderDateTime!=null &&
              this.displayOrderDateTime.equals(other.getDisplayOrderDateTime()))) &&
            ((this.notificationMobile==null && other.getNotificationMobile()==null) || 
             (this.notificationMobile!=null &&
              this.notificationMobile.equals(other.getNotificationMobile()))) &&
            ((this.notificationEmail==null && other.getNotificationEmail()==null) || 
             (this.notificationEmail!=null &&
              this.notificationEmail.equals(other.getNotificationEmail()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.createdOn==null && other.getCreatedOn()==null) || 
             (this.createdOn!=null &&
              this.createdOn.equals(other.getCreatedOn()))) &&
            ((this.updatedOn==null && other.getUpdatedOn()==null) || 
             (this.updatedOn!=null &&
              this.updatedOn.equals(other.getUpdatedOn())));
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
        if (getDisplayOrderCode() != null) {
            _hashCode += getDisplayOrderCode().hashCode();
        }
        if (getDisplayOrderDateTime() != null) {
            _hashCode += getDisplayOrderDateTime().hashCode();
        }
        if (getNotificationMobile() != null) {
            _hashCode += getNotificationMobile().hashCode();
        }
        if (getNotificationEmail() != null) {
            _hashCode += getNotificationEmail().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getCreatedOn() != null) {
            _hashCode += getCreatedOn().hashCode();
        }
        if (getUpdatedOn() != null) {
            _hashCode += getUpdatedOn().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchSaleOrderResponseSaleOrdersSaleOrder.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>SearchSaleOrderResponse>SaleOrders>SaleOrder"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrderCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DisplayOrderCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrderDateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DisplayOrderDateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationMobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "NotificationMobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "NotificationEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createdOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CreatedOn"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("updatedOn");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "UpdatedOn"));
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
