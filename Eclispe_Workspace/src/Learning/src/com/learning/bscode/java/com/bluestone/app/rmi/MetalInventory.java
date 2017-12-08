package com.bluestone.app.rmi;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "rmi_item_master")
public class MetalInventory extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@OneToOne
	private ItemMaster itemMaster;
	
	private double totalWeight;
	
	private String packetCode;
	
	private double unitCount;
	
	private int usageCount;
	
	private String status;

	public ItemMaster getItemMaster() {
		return itemMaster;
	}

	public void setItemMaster(ItemMaster itemMaster) {
		this.itemMaster = itemMaster;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getPacketCode() {
		return packetCode;
	}

	public void setPacketCode(String packetCode) {
		this.packetCode = packetCode;
	}

	public double getUnitCount() {
		return unitCount;
	}

	public void setUnitCount(double unitCount) {
		this.unitCount = unitCount;
	}

	public int getUsageCount() {
		return usageCount;
	}

	public void setUsageCount(int usageCount) {
		this.usageCount = usageCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
