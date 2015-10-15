package com.bluestone.app.design.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Table(name = "design_metal_family")
@Entity
public class DesignMetalFamily extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "family", columnDefinition = "tinyint ", nullable = false)
    private short             family;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "design_id", nullable = false, updatable = false)
    private Design            design;
    
    public short getFamily() {
        return family;
    }
    
    public void setFamily(short family) {
        this.family = family;
    }
    
    public Design getDesign() {
        return design;
    }
    
    public void setDesign(Design design) {
        this.design = design;
    }
    
    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("DesignMetalFamily");
        sb.append("{family='").append(family).append('\'');
        sb.append(", Design").append(design);
        sb.append(super.toString());
        return sb.toString();
    }

}
