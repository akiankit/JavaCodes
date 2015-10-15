package com.bluestone.app.rmi;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.design.model.StoneSpecification;

@Audited
@Entity
@Table(name = "rmi_item_master")
public class StoneInventory extends BaseEntity{

	private static final long serialVersionUID = 1L;	
	
	@OneToOne
	private ItemMaster itemMaster;
	
	@OneToOne
	private StoneSpecification stoneSpecification;
	
	private int noOfStones;
	
	private String packetCode;
	
	private double totalWeight;
	
	private String status;

	public ItemMaster getItemMaster() {
		return itemMaster;
	}

	public void setItemMaster(ItemMaster itemMaster) {
		this.itemMaster = itemMaster;
	}

	public StoneSpecification getStoneSpecification() {
		return stoneSpecification;
	}

	public void setStoneSpecification(StoneSpecification stoneSpecification) {
		this.stoneSpecification = stoneSpecification;
	}

	public int getNoOfStones() {
		return noOfStones;
	}

	public void setNoOfStones(int noOfStones) {
		this.noOfStones = noOfStones;
	}

	public String getPacketCode() {
		return packetCode;
	}

	public void setPacketCode(String packetCode) {
		this.packetCode = packetCode;
	}

	public double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
