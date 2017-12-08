/**
 * SearchSaleOrderResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class SearchSaleOrderResponse  implements java.io.Serializable {
    private boolean successful;

    private com.unicommerce.uniware.services.Error[] errors;

    private com.unicommerce.uniware.services.Warning[] warnings;

    private long totalRecords;

    private com.unicommerce.uniware.services.SearchSaleOrderResponseSaleOrdersSaleOrder[] saleOrders;

    public SearchSaleOrderResponse() {
    }

    public SearchSaleOrderResponse(
           boolean successful,
           com.unicommerce.uniware.services.Error[] errors,
           com.unicommerce.uniware.services.Warning[] warnings,
           long totalRecords,
           com.unicommerce.uniware.services.SearchSaleOrderResponseSaleOrdersSaleOrder[] saleOrders) {
           this.successful = successful;
           this.errors = errors;
           this.warnings = warnings;
           this.totalRecords = totalRecords;
           this.saleOrders = saleOrders;
    }


    /**
     * Gets the successful value for this SearchSaleOrderResponse.
     * 
     * @return successful
     */
    public boolean isSuccessful() {
        return successful;
    }


    /**
     * Sets the successful value for this SearchSaleOrderResponse.
     * 
     * @param successful
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }


    /**
     * Gets the errors value for this SearchSaleOrderResponse.
     * 
     * @return errors
     */
    public com.unicommerce.uniware.services.Error[] getErrors() {
        return errors;
    }


    /**
     * Sets the errors value for this SearchSaleOrderResponse.
     * 
     * @param errors
     */
    public void setErrors(com.unicommerce.uniware.services.Error[] errors) {
        this.errors = errors;
    }


    /**
     * Gets the warnings value for this SearchSaleOrderResponse.
     * 
     * @return warnings
     */
    public com.unicommerce.uniware.services.Warning[] getWarnings() {
        return warnings;
    }


    /**
     * Sets the warnings value for this SearchSaleOrderResponse.
     * 
     * @param warnings
     */
    public void setWarnings(com.unicommerce.uniware.services.Warning[] warnings) {
        this.warnings = warnings;
    }


    /**
     * Gets the totalRecords value for this SearchSaleOrderResponse.
     * 
     * @return totalRecords
     */
    public long getTotalRecords() {
        return totalRecords;
    }


    /**
     * Sets the totalRecords value for this SearchSaleOrderResponse.
     * 
     * @param totalRecords
     */
    public void setTotalRecords(long totalRecords) {
        this.totalRecords = totalRecords;
    }


    /**
     * Gets the saleOrders value for this SearchSaleOrderResponse.
     * 
     * @return saleOrders
     */
    public com.unicommerce.uniware.services.SearchSaleOrderResponseSaleOrdersSaleOrder[] getSaleOrders() {
        return saleOrders;
    }


    /**
     * Sets the saleOrders value for this SearchSaleOrderResponse.
     * 
     * @param saleOrders
     */
    public void setSaleOrders(com.unicommerce.uniware.services.SearchSaleOrderResponseSaleOrdersSaleOrder[] saleOrders) {
        this.saleOrders = saleOrders;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchSaleOrderResponse)) return false;
        SearchSaleOrderResponse other = (SearchSaleOrderResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.successful == other.isSuccessful() &&
            ((this.errors==null && other.getErrors()==null) || 
             (this.errors!=null &&
              java.util.Arrays.equals(this.errors, other.getErrors()))) &&
            ((this.warnings==null && other.getWarnings()==null) || 
             (this.warnings!=null &&
              java.util.Arrays.equals(this.warnings, other.getWarnings()))) &&
            this.totalRecords == other.getTotalRecords() &&
            ((this.saleOrders==null && other.getSaleOrders()==null) || 
             (this.saleOrders!=null &&
              java.util.Arrays.equals(this.saleOrders, other.getSaleOrders())));
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
        _hashCode += (isSuccessful() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getErrors() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getErrors());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getErrors(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getWarnings() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getWarnings());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getWarnings(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += new Long(getTotalRecords()).hashCode();
        if (getSaleOrders() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getSaleOrders());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getSaleOrders(), i);
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
        new org.apache.axis.description.TypeDesc(SearchSaleOrderResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">SearchSaleOrderResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("successful");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Successful"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errors");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Errors"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Error"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("warnings");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warnings"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Warning"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "TotalRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("saleOrders");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrders"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>SearchSaleOrderResponse>SaleOrders>SaleOrder"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SaleOrder"));
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
