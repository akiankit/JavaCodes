package com.bluestone.app.shipping.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "holiday")
public class Holiday extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Date holiday;

	public Date getHoliday() {
		return holiday;
	}

	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}

}
