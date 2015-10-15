/**
 * CreateExportJobRequestExportFiltersExportFilterSelectedValues.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class CreateExportJobRequestExportFiltersExportFilterSelectedValues  implements java.io.Serializable {
    private java.lang.String selectedValue;

    public CreateExportJobRequestExportFiltersExportFilterSelectedValues() {
    }

    public CreateExportJobRequestExportFiltersExportFilterSelectedValues(
           java.lang.String selectedValue) {
           this.selectedValue = selectedValue;
    }


    /**
     * Gets the selectedValue value for this CreateExportJobRequestExportFiltersExportFilterSelectedValues.
     * 
     * @return selectedValue
     */
    public java.lang.String getSelectedValue() {
        return selectedValue;
    }


    /**
     * Sets the selectedValue value for this CreateExportJobRequestExportFiltersExportFilterSelectedValues.
     * 
     * @param selectedValue
     */
    public void setSelectedValue(java.lang.String selectedValue) {
        this.selectedValue = selectedValue;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateExportJobRequestExportFiltersExportFilterSelectedValues)) return false;
        CreateExportJobRequestExportFiltersExportFilterSelectedValues other = (CreateExportJobRequestExportFiltersExportFilterSelectedValues) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.selectedValue==null && other.getSelectedValue()==null) || 
             (this.selectedValue!=null &&
              this.selectedValue.equals(other.getSelectedValue())));
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
        if (getSelectedValue() != null) {
            _hashCode += getSelectedValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateExportJobRequestExportFiltersExportFilterSelectedValues.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CreateExportJobRequest>ExportFilters>ExportFilter>SelectedValues"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectedValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SelectedValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
