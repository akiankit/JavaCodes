package com.bluestone.app.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "carrier_zip", uniqueConstraints = @UniqueConstraint(columnNames = { "carrier_id", "zipcode" }))
public class CarrierZipcode extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "zipcode", columnDefinition = "VARCHAR(6)")
    private String            zipcode;
    
    @Column(name = "is_cod", columnDefinition = "tinyint(1) default 1")
    private short             isCOD;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "carrier_id", nullable = false)
    private Carrier           carrier;
    
    public String getZipcode() {
        return zipcode;
    }
    
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    
    public short getIsCOD() {
        return isCOD;
    }
    
    public void setIsCOD(short isCOD) {
        this.isCOD = isCOD;
    }
    
    public Carrier getCarrier() {
        return carrier;
    }
    
    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }
    
}
