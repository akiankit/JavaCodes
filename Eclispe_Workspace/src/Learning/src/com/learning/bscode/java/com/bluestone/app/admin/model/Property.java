package com.bluestone.app.admin.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="property")
public class Property extends BaseEntity{
    
	@Column(name = "name",nullable=false)
	private String name;
	
	@Column(name = "type",nullable=false)
	private String type;
	
    @Column(name = "value",nullable=false, length = 20000)
	private String value;
	
	@Transient
	private List<Long> longValues;
	
	@Transient
	private List<String> stringValues;
	
	@Transient
	private List<Boolean> booleanValues;
	
	@Transient
	private List<Double> doubleValues;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<Long> getLongValues() {
		return longValues;
	}

	public void setLongValues(List<Long> longValues) {
		this.longValues = longValues;
	}

	public List<String> getStringValues() {
		return stringValues;
	}

	public void setStringValues(List<String> stringValues) {
		this.stringValues = stringValues;
	}

	public List<Boolean> getBooleanValues() {
		return booleanValues;
	}

	public void setBooleanValues(List<Boolean> booleanValues) {
		this.booleanValues = booleanValues;
	}

	public List<Double> getDoubleValues() {
		return doubleValues;
	}

	public void setDoubleValues(List<Double> doubleValues) {
		this.doubleValues = doubleValues;
	}


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Property");
        sb.append("{\n\t name=").append(name).append("\n");
        sb.append("\t type=").append(type).append("\n");
        sb.append("\t value=").append(value).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
