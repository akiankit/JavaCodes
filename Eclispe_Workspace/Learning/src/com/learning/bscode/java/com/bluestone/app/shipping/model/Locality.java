package com.bluestone.app.shipping.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@NamedQuery(name="locality.findDetailsByLocality",query="select l from Locality l join l.pincode p where p.pincode =:pincode")
@Table(name="locality")
public class Locality extends BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false, updatable = false)
    private City cityId;
    
    @ManyToOne
    @JoinColumn(name="pincode_id",nullable=false)
    private Pincode pincode;
    
    
    @Column(name="name", columnDefinition="VARCHAR(60)", nullable=false)
    private String name;


    public City getCityId() {
        return cityId;
    }


    public void setCityId(City cityId) {
        this.cityId = cityId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


	public Pincode getPincode() {
		return pincode;
	}


	public void setPincode(Pincode pincode) {
		this.pincode = pincode;
	}
    
    
}
