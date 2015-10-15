/**
 * ItemTypeRequestShipTogether.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.unicommerce.uniware.services;

public class ItemTypeRequestShipTogether implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ItemTypeRequestShipTogether(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _EXCLUSIVE = "EXCLUSIVE";
    public static final java.lang.String _INTER_CATEGORY = "INTER_CATEGORY";
    public static final java.lang.String _INTRA_CATEGORY = "INTRA_CATEGORY";
    public static final java.lang.String _INTRA_SKU = "INTRA_SKU";
    public static final ItemTypeRequestShipTogether EXCLUSIVE = new ItemTypeRequestShipTogether(_EXCLUSIVE);
    public static final ItemTypeRequestShipTogether INTER_CATEGORY = new ItemTypeRequestShipTogether(_INTER_CATEGORY);
    public static final ItemTypeRequestShipTogether INTRA_CATEGORY = new ItemTypeRequestShipTogether(_INTRA_CATEGORY);
    public static final ItemTypeRequestShipTogether INTRA_SKU = new ItemTypeRequestShipTogether(_INTRA_SKU);
    public java.lang.String getValue() { return _value_;}
    public static ItemTypeRequestShipTogether fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        ItemTypeRequestShipTogether enumeration = (ItemTypeRequestShipTogether)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ItemTypeRequestShipTogether fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ItemTypeRequestShipTogether.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://uniware.unicommerce.com/services/", ">ItemTypeRequest>ShipTogether"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
