package com.bluestone.app.rmi;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "rmi_measurement_unit",uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class MeasurementUnit extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
		
	@NotNull
	@Column(nullable = false)
	private String name;
		
	private String description;
		
	@OneToOne	
	private MeasurementUnit subUnit;
	
	@Column(nullable = true)
	private double subUnitMultiplier;

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

	public MeasurementUnit getSubUnit() {
		return subUnit;
	}

	public void setSubUnit(MeasurementUnit subUnit) {
		this.subUnit = subUnit;
	}

	public double getSubUnitMultiplier() {
		return subUnitMultiplier;
	}

	public void setSubUnitMultiplier(double subUnitMultiplier) {
		this.subUnitMultiplier = subUnitMultiplier;
	}	

}
