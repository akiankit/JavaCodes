/**
 * ItemTypeRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class ItemTypeRequest  implements java.io.Serializable {
    private java.lang.String categoryCode;

    private java.lang.String itemSKU;

    private java.lang.String name;

    private java.lang.String description;

    private int length;

    private int width;

    private int height;

    private int weight;

    private java.math.BigDecimal MRP;

    private java.lang.String taxTypeCode;

    private java.lang.String features;

    private java.lang.String imageURL;

    private java.lang.String productPageUrl;

    private com.unicommerce.uniware.services.ItemTypeRequestShipTogether shipTogether;

    private java.lang.Boolean traceable;

    private java.lang.Object[] tags;

    private com.unicommerce.uniware.services.CustomFieldsCustomField[] customFields;

    public ItemTypeRequest() {
    }

    public ItemTypeRequest(
           java.lang.String categoryCode,
           java.lang.String itemSKU,
           java.lang.String name,
           java.lang.String description,
           int length,
           int width,
           int height,
           int weight,
           java.math.BigDecimal MRP,
           java.lang.String taxTypeCode,
           java.lang.String features,
           java.lang.String imageURL,
           java.lang.String productPageUrl,
           com.unicommerce.uniware.services.ItemTypeRequestShipTogether shipTogether,
           java.lang.Boolean traceable,
           java.lang.Object[] tags,
           com.unicommerce.uniware.services.CustomFieldsCustomField[] customFields) {
           this.categoryCode = categoryCode;
           this.itemSKU = itemSKU;
           this.name = name;
           this.description = description;
           this.length = length;
           this.width = width;
           this.height = height;
           this.weight = weight;
           this.MRP = MRP;
           this.taxTypeCode = taxTypeCode;
           this.features = features;
           this.imageURL = imageURL;
           this.productPageUrl = productPageUrl;
           this.shipTogether = shipTogether;
           this.traceable = traceable;
           this.tags = tags;
           this.customFields = customFields;
    }


    /**
     * Gets the categoryCode value for this ItemTypeRequest.
     * 
     * @return categoryCode
     */
    public java.lang.String getCategoryCode() {
        return categoryCode;
    }


    /**
     * Sets the categoryCode value for this ItemTypeRequest.
     * 
     * @param categoryCode
     */
    public void setCategoryCode(java.lang.String categoryCode) {
        this.categoryCode = categoryCode;
    }


    /**
     * Gets the itemSKU value for this ItemTypeRequest.
     * 
     * @return itemSKU
     */
    public java.lang.String getItemSKU() {
        return itemSKU;
    }


    /**
     * Sets the itemSKU value for this ItemTypeRequest.
     * 
     * @param itemSKU
     */
    public void setItemSKU(java.lang.String itemSKU) {
        this.itemSKU = itemSKU;
    }


    /**
     * Gets the name value for this ItemTypeRequest.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ItemTypeRequest.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this ItemTypeRequest.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ItemTypeRequest.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the length value for this ItemTypeRequest.
     * 
     * @return length
     */
    public int getLength() {
        return length;
    }


    /**
     * Sets the length value for this ItemTypeRequest.
     * 
     * @param length
     */
    public void setLength(int length) {
        this.length = length;
    }


    /**
     * Gets the width value for this ItemTypeRequest.
     * 
     * @return width
     */
    public int getWidth() {
        return width;
    }


    /**
     * Sets the width value for this ItemTypeRequest.
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }


    /**
     * Gets the height value for this ItemTypeRequest.
     * 
     * @return height
     */
    public int getHeight() {
        return height;
    }


    /**
     * Sets the height value for this ItemTypeRequest.
     * 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }


    /**
     * Gets the weight value for this ItemTypeRequest.
     * 
     * @return weight
     */
    public int getWeight() {
        return weight;
    }


    /**
     * Sets the weight value for this ItemTypeRequest.
     * 
     * @param weight
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }


    /**
     * Gets the MRP value for this ItemTypeRequest.
     * 
     * @return MRP
     */
    public java.math.BigDecimal getMRP() {
        return MRP;
    }


    /**
     * Sets the MRP value for this ItemTypeRequest.
     * 
     * @param MRP
     */
    public void setMRP(java.math.BigDecimal MRP) {
        this.MRP = MRP;
    }


    /**
     * Gets the taxTypeCode value for this ItemTypeRequest.
     * 
     * @return taxTypeCode
     */
    public java.lang.String getTaxTypeCode() {
        return taxTypeCode;
    }


    /**
     * Sets the taxTypeCode value for this ItemTypeRequest.
     * 
     * @param taxTypeCode
     */
    public void setTaxTypeCode(java.lang.String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }


    /**
     * Gets the features value for this ItemTypeRequest.
     * 
     * @return features
     */
    public java.lang.String getFeatures() {
        return features;
    }


    /**
     * Sets the features value for this ItemTypeRequest.
     * 
     * @param features
     */
    public void setFeatures(java.lang.String features) {
        this.features = features;
    }


    /**
     * Gets the imageURL value for this ItemTypeRequest.
     * 
     * @return imageURL
     */
    public java.lang.String getImageURL() {
        return imageURL;
    }


    /**
     * Sets the imageURL value for this ItemTypeRequest.
     * 
     * @param imageURL
     */
    public void setImageURL(java.lang.String imageURL) {
        this.imageURL = imageURL;
    }


    /**
     * Gets the productPageUrl value for this ItemTypeRequest.
     * 
     * @return productPageUrl
     */
    public java.lang.String getProductPageUrl() {
        return productPageUrl;
    }


    /**
     * Sets the productPageUrl value for this ItemTypeRequest.
     * 
     * @param productPageUrl
     */
    public void setProductPageUrl(java.lang.String productPageUrl) {
        this.productPageUrl = productPageUrl;
    }


    /**
     * Gets the shipTogether value for this ItemTypeRequest.
     * 
     * @return shipTogether
     */
    public com.unicommerce.uniware.services.ItemTypeRequestShipTogether getShipTogether() {
        return shipTogether;
    }


    /**
     * Sets the shipTogether value for this ItemTypeRequest.
     * 
     * @param shipTogether
     */
    public void setShipTogether(com.unicommerce.uniware.services.ItemTypeRequestShipTogether shipTogether) {
        this.shipTogether = shipTogether;
    }


    /**
     * Gets the traceable value for this ItemTypeRequest.
     * 
     * @return traceable
     */
    public java.lang.Boolean getTraceable() {
        return traceable;
    }


    /**
     * Sets the traceable value for this ItemTypeRequest.
     * 
     * @param traceable
     */
    public void setTraceable(java.lang.Boolean traceable) {
        this.traceable = traceable;
    }


    /**
     * Gets the tags value for this ItemTypeRequest.
     * 
     * @return tags
     */
    public java.lang.Object[] getTags() {
        return tags;
    }


    /**
     * Sets the tags value for this ItemTypeRequest.
     * 
     * @param tags
     */
    public void setTags(java.lang.Object[] tags) {
        this.tags = tags;
    }


    /**
     * Gets the customFields value for this ItemTypeRequest.
     * 
     * @return customFields
     */
    public com.unicommerce.uniware.services.CustomFieldsCustomField[] getCustomFields() {
        return customFields;
    }


    /**
     * Sets the customFields value for this ItemTypeRequest.
     * 
     * @param customFields
     */
    public void setCustomFields(com.unicommerce.uniware.services.CustomFieldsCustomField[] customFields) {
        this.customFields = customFields;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ItemTypeRequest)) return false;
        ItemTypeRequest other = (ItemTypeRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.categoryCode==null && other.getCategoryCode()==null) || 
             (this.categoryCode!=null &&
              this.categoryCode.equals(other.getCategoryCode()))) &&
            ((this.itemSKU==null && other.getItemSKU()==null) || 
             (this.itemSKU!=null &&
              this.itemSKU.equals(other.getItemSKU()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.length == other.getLength() &&
            this.width == other.getWidth() &&
            this.height == other.getHeight() &&
            this.weight == other.getWeight() &&
            ((this.MRP==null && other.getMRP()==null) || 
             (this.MRP!=null &&
              this.MRP.equals(other.getMRP()))) &&
            ((this.taxTypeCode==null && other.getTaxTypeCode()==null) || 
             (this.taxTypeCode!=null &&
              this.taxTypeCode.equals(other.getTaxTypeCode()))) &&
            ((this.features==null && other.getFeatures()==null) || 
             (this.features!=null &&
              this.features.equals(other.getFeatures()))) &&
            ((this.imageURL==null && other.getImageURL()==null) || 
             (this.imageURL!=null &&
              this.imageURL.equals(other.getImageURL()))) &&
            ((this.productPageUrl==null && other.getProductPageUrl()==null) || 
             (this.productPageUrl!=null &&
              this.productPageUrl.equals(other.getProductPageUrl()))) &&
            ((this.shipTogether==null && other.getShipTogether()==null) || 
             (this.shipTogether!=null &&
              this.shipTogether.equals(other.getShipTogether()))) &&
            ((this.traceable==null && other.getTraceable()==null) || 
             (this.traceable!=null &&
              this.traceable.equals(other.getTraceable()))) &&
            ((this.tags==null && other.getTags()==null) || 
             (this.tags!=null &&
              java.util.Arrays.equals(this.tags, other.getTags()))) &&
            ((this.customFields==null && other.getCustomFields()==null) || 
             (this.customFields!=null &&
              java.util.Arrays.equals(this.customFields, other.getCustomFields())));
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
        if (getCategoryCode() != null) {
            _hashCode += getCategoryCode().hashCode();
        }
        if (getItemSKU() != null) {
            _hashCode += getItemSKU().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getLength();
        _hashCode += getWidth();
        _hashCode += getHeight();
        _hashCode += getWeight();
        if (getMRP() != null) {
            _hashCode += getMRP().hashCode();
        }
        if (getTaxTypeCode() != null) {
            _hashCode += getTaxTypeCode().hashCode();
        }
        if (getFeatures() != null) {
            _hashCode += getFeatures().hashCode();
        }
        if (getImageURL() != null) {
            _hashCode += getImageURL().hashCode();
        }
        if (getProductPageUrl() != null) {
            _hashCode += getProductPageUrl().hashCode();
        }
        if (getShipTogether() != null) {
            _hashCode += getShipTogether().hashCode();
        }
        if (getTraceable() != null) {
            _hashCode += getTraceable().hashCode();
        }
        if (getTags() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTags());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTags(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCustomFields() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCustomFields());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCustomFields(), i);
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
        new org.apache.axis.description.TypeDesc(ItemTypeRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemTypeRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("categoryCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CategoryCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemSKU");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ItemSKU"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("length");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Length"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("width");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Width"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("height");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Height"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("weight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Weight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MRP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "MRP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "TaxTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("features");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Features"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("imageURL");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ImageURL"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("productPageUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ProductPageUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("shipTogether");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "ShipTogether"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">ItemTypeRequest>ShipTogether"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("traceable");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Traceable"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tags");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Tags"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "Tag"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customFields");
        elemField.setXmlName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomFields"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">CustomFields>CustomField"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", "CustomField"));
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
