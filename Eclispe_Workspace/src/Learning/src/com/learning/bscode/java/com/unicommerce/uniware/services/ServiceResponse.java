/**
 * ServiceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class ServiceResponse  implements java.io.Serializable {
    private boolean successful;

    private com.unicommerce.uniware.services.Error[] errors;

    private com.unicommerce.uniware.services.Warning[] warnings;

    public ServiceResponse() {
    }

    public ServiceResponse(
           boolean successful,
           com.unicommerce.uniware.services.Error[] errors,
           com.unicommerce.uniware.services.Warning[] warnings) {
           this.successful = successful;
           this.errors = errors;
           this.warnings = warnings;
    }


    /**
     * Gets the successful value for this ServiceResponse.
     * 
     * @return successful
     */
    public boolean isSuccessful() {
        return successful;
    }


    /**
     * Sets the successful value for this ServiceResponse.
     * 
     * @param successful
     */
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }


    /**
     * Gets the errors value for this ServiceResponse.
     * 
     * @return errors
     */
    public com.unicommerce.uniware.services.Error[] getErrors() {
        return errors;
    }


    /**
     * Sets the errors value for this ServiceResponse.
     * 
     * @param errors
     */
    public void setErrors(com.unicommerce.uniware.services.Error[] errors) {
        this.errors = errors;
    }


    /**
     * Gets the warnings value for this ServiceResponse.
     * 
     * @return warnings
     */
    public com.unicommerce.uniware.services.Warning[] getWarnings() {
        return warnings;
    }


    /**
     * Sets the warnings value for this ServiceResponse.
     * 
     * @param warnings
     */
    public void setWarnings(com.unicommerce.uniware.services.Warning[] warnings) {
        this.warnings = warnings;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceResponse)) return false;
        ServiceResponse other = (ServiceResponse) obj;
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
              java.util.Arrays.equals(this.warnings, other.getWarnings())));
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ServiceResponse"));
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
