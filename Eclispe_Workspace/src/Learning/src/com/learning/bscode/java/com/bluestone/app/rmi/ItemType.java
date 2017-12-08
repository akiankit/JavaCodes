package com.bluestone.app.rmi;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "rmi_item_type")
public class ItemType extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	private String name;
	
	private String description;
	
	@OneToOne
	private ItemType parentItemType;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ItemType getParentItemType() {
		return parentItemType;
	}

	public void setParentItemType(ItemType parentItemType) {
		this.parentItemType = parentItemType;
	}
	
}
