package com.bluestone.app.rmi;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "rmi_item_master")
public class ItemMaster extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	
	private String itemCode;
	
	private ItemType itemType;
	
	@OneToOne
	private MeasurementUnit measurementUnit;
	
	private double defaultUnitCount;
	
	private double thresholdLevel;
	
	private double pricePerUnit;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}

	public MeasurementUnit getMeasurementUnit() {
		return measurementUnit;
	}

	public void setMeasurementUnit(MeasurementUnit measurementUnit) {
		this.measurementUnit = measurementUnit;
	}

	public double getDefaultUnitCount() {
		return defaultUnitCount;
	}

	public void setDefaultUnitCount(double defaultUnitCount) {
		this.defaultUnitCount = defaultUnitCount;
	}

	public double getThresholdLevel() {
		return thresholdLevel;
	}

	public void setThresholdLevel(double thresholdLevel) {
		this.thresholdLevel = thresholdLevel;
	}

	public double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}		

}
