/**
 * CreateExportJobRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class CreateExportJobRequest  implements java.io.Serializable {
    private java.lang.String exportJobTypeName;

    private java.lang.String[] exportColumns;

    private com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter[] exportFilters;

    private java.util.Calendar scheduleTime;

    private java.lang.String notificationEmail;

    private java.lang.String frequency;

    public CreateExportJobRequest() {
    }

    public CreateExportJobRequest(
           java.lang.String exportJobTypeName,
           java.lang.String[] exportColumns,
           com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter[] exportFilters,
           java.util.Calendar scheduleTime,
           java.lang.String notificationEmail,
           java.lang.String frequency) {
           this.exportJobTypeName = exportJobTypeName;
           this.exportColumns = exportColumns;
           this.exportFilters = exportFilters;
           this.scheduleTime = scheduleTime;
           this.notificationEmail = notificationEmail;
           this.frequency = frequency;
    }


    /**
     * Gets the exportJobTypeName value for this CreateExportJobRequest.
     * 
     * @return exportJobTypeName
     */
    public java.lang.String getExportJobTypeName() {
        return exportJobTypeName;
    }


    /**
     * Sets the exportJobTypeName value for this CreateExportJobRequest.
     * 
     * @param exportJobTypeName
     */
    public void setExportJobTypeName(java.lang.String exportJobTypeName) {
        this.exportJobTypeName = exportJobTypeName;
    }


    /**
     * Gets the exportColumns value for this CreateExportJobRequest.
     * 
     * @return exportColumns
     */
    public java.lang.String[] getExportColumns() {
        return exportColumns;
    }


    /**
     * Sets the exportColumns value for this CreateExportJobRequest.
     * 
     * @param exportColumns
     */
    public void setExportColumns(java.lang.String[] exportColumns) {
        this.exportColumns = exportColumns;
    }


    /**
     * Gets the exportFilters value for this CreateExportJobRequest.
     * 
     * @return exportFilters
     */
    public com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter[] getExportFilters() {
        return exportFilters;
    }


    /**
     * Sets the exportFilters value for this CreateExportJobRequest.
     * 
     * @param exportFilters
     */
    public void setExportFilters(com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter[] exportFilters) {
        this.exportFilters = exportFilters;
    }


    /**
     * Gets the scheduleTime value for this CreateExportJobRequest.
     * 
     * @return scheduleTime
     */
    public java.util.Calendar getScheduleTime() {
        return scheduleTime;
    }


    /**
     * Sets the scheduleTime value for this CreateExportJobRequest.
     * 
     * @param scheduleTime
     */
    public void setScheduleTime(java.util.Calendar scheduleTime) {
        this.scheduleTime = scheduleTime;
    }


    /**
     * Gets the notificationEmail value for this CreateExportJobRequest.
     * 
     * @return notificationEmail
     */
    public java.lang.String getNotificationEmail() {
        return notificationEmail;
    }


    /**
     * Sets the notificationEmail value for this CreateExportJobRequest.
     * 
     * @param notificationEmail
     */
    public void setNotificationEmail(java.lang.String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }


    /**
     * Gets the frequency value for this CreateExportJobRequest.
     * 
     * @return frequency
     */
    public java.lang.String getFrequency() {
        return frequency;
    }


    /**
     * Sets the frequency value for this CreateExportJobRequest.
     * 
     * @param frequency
     */
    public void setFrequency(java.lang.String frequency) {
        this.frequency = frequency;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateExportJobRequest)) return false;
        CreateExportJobRequest other = (CreateExportJobRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.exportJobTypeName==null && other.getExportJobTypeName()==null) || 
             (this.exportJobTypeName!=null &&
              this.exportJobTypeName.equals(other.getExportJobTypeName()))) &&
            ((this.exportColumns==null && other.getExportColumns()==null) || 
             (this.exportColumns!=null &&
              java.util.Arrays.equals(this.exportColumns, other.getExportColumns()))) &&
            ((this.exportFilters==null && other.getExportFilters()==null) || 
             (this.exportFilters!=null &&
              java.util.Arrays.equals(this.exportFilters, other.getExportFilters()))) &&
            ((this.scheduleTime==null && other.getScheduleTime()==null) || 
             (this.scheduleTime!=null &&
              this.scheduleTime.equals(other.getScheduleTime()))) &&
            ((this.notificationEmail==null && other.getNotificationEmail()==null) || 
             (this.notificationEmail!=null &&
              this.notificationEmail.equals(other.getNotificationEmail()))) &&
            ((this.frequency==null && other.getFrequency()==null) || 
             (this.frequency!=null &&
              this.frequency.equals(other.getFrequency())));
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
        if (getExportJobTypeName() != null) {
            _hashCode += getExportJobTypeName().hashCode();
        }
        if (getExportColumns() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExportColumns());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExportColumns(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getExportFilters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getExportFilters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getExportFilters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getScheduleTime() != null) {
            _hashCode += getScheduleTime().hashCode();
        }
        if (getNotificationEmail() != null) {
            _hashCode += getNotificationEmail().hashCode();
        }
        if (getFrequency() != null) {
            _hashCode += getFrequency().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreateExportJobRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CreateExportJobRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exportJobTypeName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportJobTypeName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exportColumns");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportColumns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportColumn"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("exportFilters");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportFilters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">>>CreateExportJobRequest>ExportFilters>ExportFilter"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ExportFilter"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("scheduleTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ScheduleTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("notificationEmail");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "NotificationEmail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("frequency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Frequency"));
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
