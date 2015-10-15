/**
 * CreateExportJobRequestExportFiltersExportFilter.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class CreateExportJobRequestExportFiltersExportFilter  implements java.io.Serializable {
    private com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterSelectedValues selectedValues;

    private java.lang.String text;

    private java.lang.String selectedValue;

    private java.util.Calendar dateTime;

    private com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterDateRange dateRange;

    private java.lang.String checked;

    private java.lang.String id;  // attribute

    public CreateExportJobRequestExportFiltersExportFilter() {
    }

    public CreateExportJobRequestExportFiltersExportFilter(
           com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterSelectedValues selectedValues,
           java.lang.String text,
           java.lang.String selectedValue,
           java.util.Calendar dateTime,
           com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterDateRange dateRange,
           java.lang.String checked,
           java.lang.String id) {
           this.selectedValues = selectedValues;
           this.text = text;
           this.selectedValue = selectedValue;
           this.dateTime = dateTime;
           this.dateRange = dateRange;
           this.checked = checked;
           this.id = id;
    }


    /**
     * Gets the selectedValues value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return selectedValues
     */
    public com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterSelectedValues getSelectedValues() {
        return selectedValues;
    }


    /**
     * Sets the selectedValues value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param selectedValues
     */
    public void setSelectedValues(com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterSelectedValues selectedValues) {
        this.selectedValues = selectedValues;
    }


    /**
     * Gets the text value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }


    /**
     * Gets the selectedValue value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return selectedValue
     */
    public java.lang.String getSelectedValue() {
        return selectedValue;
    }


    /**
     * Sets the selectedValue value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param selectedValue
     */
    public void setSelectedValue(java.lang.String selectedValue) {
        this.selectedValue = selectedValue;
    }


    /**
     * Gets the dateTime value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return dateTime
     */
    public java.util.Calendar getDateTime() {
        return dateTime;
    }


    /**
     * Sets the dateTime value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param dateTime
     */
    public void setDateTime(java.util.Calendar dateTime) {
        this.dateTime = dateTime;
    }


    /**
     * Gets the dateRange value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return dateRange
     */
    public com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterDateRange getDateRange() {
        return dateRange;
    }


    /**
     * Sets the dateRange value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param dateRange
     */
    public void setDateRange(com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilterDateRange dateRange) {
        this.dateRange = dateRange;
    }


    /**
     * Gets the checked value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return checked
     */
    public java.lang.String getChecked() {
        return checked;
    }


    /**
     * Sets the checked value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param checked
     */
    public void setChecked(java.lang.String checked) {
        this.checked = checked;
    }


    /**
     * Gets the id value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this CreateExportJobRequestExportFiltersExportFilter.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateExportJobRequestExportFiltersExportFilter)) return false;
        CreateExportJobRequestExportFiltersExportFilter other = (CreateExportJobRequestExportFiltersExportFilter) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.selectedValues==null && other.getSelectedValues()==null) || 
             (this.selectedValues!=null &&
              this.selectedValues.equals(other.getSelectedValues()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText()))) &&
            ((this.selectedValue==null && other.getSelectedValue()==null) || 
             (this.selectedValue!=null &&
              this.selectedValue.equals(other.getSelectedValue()))) &&
            ((this.dateTime==null && other.getDateTime()==null) || 
             (this.dateTime!=null &&
              this.dateTime.equals(other.getDateTime()))) &&
            ((this.dateRange==null && other.getDateRange()==null) || 
             (this.dateRange!=null &&
              this.dateRange.equals(other.getDateRange()))) &&
            ((this.checked==null && other.getChecked()==null) || 
             (this.checked!=null &&
              this.checked.equals(other.getChecked()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId())));
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
        if (getSelectedValues() != null) {
            _hashCode += getSelectedValues().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        if (getSelectedValue() != null) {
            _hashCode += getSelectedValue().hashCode();
        }
        if (getDateTime() != null) {
            _hashCode += getDateTime().hashCode();
        }
        if (getDateRange() != null) {
            _hashCode += getDateRange().hashCode();
        }
        if (getChecked() != null) {
            _hashCode += getChecked().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateExportJobRequestExportFiltersExportFilter.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>CreateExportJobRequest>ExportFilters>ExportFilter"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("id");
        attrField.setXmlName(new javax.xml.namespace.QName("", "id"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectedValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SelectedValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CreateExportJobRequest>ExportFilters>ExportFilter>SelectedValues"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("selectedValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "SelectedValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DateTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateRange");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "DateRange"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>>CreateExportJobRequest>ExportFilters>ExportFilter>DateRange"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checked");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Checked"));
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
