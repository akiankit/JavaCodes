/**
 * SearchSaleOrderRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SearchSaleOrderRequest  implements java.io.Serializable {
    private java.lang.String displayOrderCode;

    private java.lang.String status;

    private java.lang.String customerEmailOrMobile;

    private java.lang.String customerName;

    private java.lang.Boolean cashOnDelivery;

    private java.util.Calendar fromDate;

    private java.util.Calendar toDate;

    private com.unicommerce.uniware.services.SearchSaleOrderRequestSearchOptions searchOptions;

    public SearchSaleOrderRequest() {
    }

    public SearchSaleOrderRequest(
           java.lang.String displayOrderCode,
           java.lang.String status,
           java.lang.String customerEmailOrMobile,
           java.lang.String customerName,
           java.lang.Boolean cashOnDelivery,
           java.util.Calendar fromDate,
           java.util.Calendar toDate,
           com.unicommerce.uniware.services.SearchSaleOrderRequestSearchOptions searchOptions) {
           this.displayOrderCode = displayOrderCode;
           this.status = status;
           this.customerEmailOrMobile = customerEmailOrMobile;
           this.customerName = customerName;
           this.cashOnDelivery = cashOnDelivery;
           this.fromDate = fromDate;
           this.toDate = toDate;
           this.searchOptions = searchOptions;
    }


    /**
     * Gets the displayOrderCode value for this SearchSaleOrderRequest.
     * 
     * @return displayOrderCode
     */
    public java.lang.String getDisplayOrderCode() {
        return displayOrderCode;
    }


    /**
     * Sets the displayOrderCode value for this SearchSaleOrderRequest.
     * 
     * @param displayOrderCode
     */
    public void setDisplayOrderCode(java.lang.String displayOrderCode) {
        this.displayOrderCode = displayOrderCode;
    }


    /**
     * Gets the status value for this SearchSaleOrderRequest.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this SearchSaleOrderRequest.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the customerEmailOrMobile value for this SearchSaleOrderRequest.
     * 
     * @return customerEmailOrMobile
     */
    public java.lang.String getCustomerEmailOrMobile() {
        return customerEmailOrMobile;
    }


    /**
     * Sets the customerEmailOrMobile value for this SearchSaleOrderRequest.
     * 
     * @param customerEmailOrMobile
     */
    public void setCustomerEmailOrMobile(java.lang.String customerEmailOrMobile) {
        this.customerEmailOrMobile = customerEmailOrMobile;
    }


    /**
     * Gets the customerName value for this SearchSaleOrderRequest.
     * 
     * @return customerName
     */
    public java.lang.String getCustomerName() {
        return customerName;
    }


    /**
     * Sets the customerName value for this SearchSaleOrderRequest.
     * 
     * @param customerName
     */
    public void setCustomerName(java.lang.String customerName) {
        this.customerName = customerName;
    }


    /**
     * Gets the cashOnDelivery value for this SearchSaleOrderRequest.
     * 
     * @return cashOnDelivery
     */
    public java.lang.Boolean getCashOnDelivery() {
        return cashOnDelivery;
    }


    /**
     * Sets the cashOnDelivery value for this SearchSaleOrderRequest.
     * 
     * @param cashOnDelivery
     */
    public void setCashOnDelivery(java.lang.Boolean cashOnDelivery) {
        this.cashOnDelivery = cashOnDelivery;
    }


    /**
     * Gets the fromDate value for this SearchSaleOrderRequest.
     * 
     * @return fromDate
     */
    public java.util.Calendar getFromDate() {
        return fromDate;
    }


    /**
     * Sets the fromDate value for this SearchSaleOrderRequest.
     * 
     * @param fromDate
     */
    public void setFromDate(java.util.Calendar fromDate) {
        this.fromDate = fromDate;
    }


    /**
     * Gets the toDate value for this SearchSaleOrderRequest.
     * 
     * @return toDate
     */
    public java.util.Calendar getToDate() {
        return toDate;
    }


    /**
     * Sets the toDate value for this SearchSaleOrderRequest.
     * 
     * @param toDate
     */
    public void setToDate(java.util.Calendar toDate) {
        this.toDate = toDate;
    }


    /**
     * Gets the searchOptions value for this SearchSaleOrderRequest.
     * 
     * @return searchOptions
     */
    public com.unicommerce.uniware.services.SearchSaleOrderRequestSearchOptions getSearchOptions() {
        return searchOptions;
    }


    /**
     * Sets the searchOptions value for this SearchSaleOrderRequest.
     * 
     * @param searchOptions
     */
    public void setSearchOptions(com.unicommerce.uniware.services.SearchSaleOrderRequestSearchOptions searchOptions) {
        this.searchOptions = searchOptions;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchSaleOrderRequest)) return false;
        SearchSaleOrderRequest other = (SearchSaleOrderRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.displayOrderCode==null && other.getDisplayOrderCode()==null) || 
             (this.displayOrderCode!=null &&
              this.displayOrderCode.equals(other.getDisplayOrderCode()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            ((this.customerEmailOrMobile==null && other.getCustomerEmailOrMobile()==null) || 
             (this.customerEmailOrMobile!=null &&
              this.customerEmailOrMobile.equals(other.getCustomerEmailOrMobile()))) &&
            ((this.customerName==null && other.getCustomerName()==null) || 
             (this.customerName!=null &&
              this.customerName.equals(other.getCustomerName()))) &&
            ((this.cashOnDelivery==null && other.getCashOnDelivery()==null) || 
             (this.cashOnDelivery!=null &&
              this.cashOnDelivery.equals(other.getCashOnDelivery()))) &&
            ((this.fromDate==null && other.getFromDate()==null) || 
             (this.fromDate!=null &&
              this.fromDate.equals(other.getFromDate()))) &&
            ((this.toDate==null && other.getToDate()==null) || 
             (this.toDate!=null &&
              this.toDate.equals(other.getToDate()))) &&
            ((this.searchOptions==null && other.getSearchOptions()==null) || 
             (this.searchOptions!=null &&
              this.searchOptions.equals(other.getSearchOptions())));
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
        if (getDisplayOrderCode() != null) {
            _hashCode += getDisplayOrderCode().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        if (getCustomerEmailOrMobile() != null) {
            _hashCode += getCustomerEmailOrMobile().hashCode();
        }
        if (getCustomerName() != null) {
            _hashCode += getCustomerName().hashCode();
        }
        if (getCashOnDelivery() != null) {
            _hashCode += getCashOnDelivery().hashCode();
        }
        if (getFromDate() != null) {
            _hashCode += getFromDate().hashCode();
        }
        if (getToDate() != null) {
            _hashCode += getToDate().hashCode();
        }
        if (getSearchOptions() != null) {
            _hashCode += getSearchOptions().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchSaleOrderRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SearchSaleOrderRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayOrderCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DisplayOrderCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerEmailOrMobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomerEmailOrMobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cashOnDelivery");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CashOnDelivery"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fromDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "FromDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("toDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ToDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("searchOptions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SearchOptions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>SearchSaleOrderRequest>SearchOptions"));
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
