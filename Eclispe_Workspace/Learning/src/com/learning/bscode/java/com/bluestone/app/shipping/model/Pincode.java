package com.bluestone.app.shipping.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="pincode")
public class Pincode extends BaseEntity{

    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "pincode")
    @BatchSize(size=50)
    private Set<Locality> locality;
    
    @Column(name="pincode",columnDefinition="int",nullable=false)
    private int pincode;

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public Set<Locality> getLocality() {
        return locality;
    }

    public void setLocality(Set<Locality> locality) {
        this.locality = locality;
    }
    
}
