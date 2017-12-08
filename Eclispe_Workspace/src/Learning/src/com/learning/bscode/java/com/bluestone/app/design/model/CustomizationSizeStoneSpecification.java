package com.bluestone.app.design.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "design_size_stone_specification")
public class CustomizationSizeStoneSpecification extends BaseEntity {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Column(name = "delta", nullable = false, scale = 2)
    private int        delta;
    
    @ManyToOne(fetch = FetchType.EAGER,cascade= CascadeType.PERSIST)
    @JoinColumn(name = "customization_stone_specification_id", nullable = false, updatable = false)
    private CustomizationStoneSpecification customizationStoneSpecification;

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
    
    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("CustomizationSizeStoneSpecification");
        sb.append("delta=").append(delta).append('\'');
        sb.append("CustomizationStoneSpecification=").append(getCustomizationStoneSpecification());
        return sb.toString();
    }

    public CustomizationStoneSpecification getCustomizationStoneSpecification() {
        return customizationStoneSpecification;
    }

    public void setCustomizationStoneSpecification(CustomizationStoneSpecification customizationStoneSpecification) {
        this.customizationStoneSpecification = customizationStoneSpecification;
    }
    
}
