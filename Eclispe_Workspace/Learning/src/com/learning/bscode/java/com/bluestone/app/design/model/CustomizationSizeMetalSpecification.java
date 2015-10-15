package com.bluestone.app.design.model;

import java.math.BigDecimal;

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
@Table(name = "design_size_metal_specification")
public class CustomizationSizeMetalSpecification extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "size", nullable = false)
    private String            size;
    
    @Column(name = "metal_unit", nullable = false)
    private String            metalUnit;
    
    @Column(name = "delta", nullable = false, scale = 10)
    private BigDecimal        delta;
    
    @ManyToOne(fetch = FetchType.EAGER,cascade=CascadeType.PERSIST)
    @JoinColumn(name = "customization_metal_spec_id", nullable = false, updatable = false)
    private CustomizationMetalSpecification customizationMetalSpecification;
    
    @Column(name = "price_delta",nullable=false, scale=2)
    private BigDecimal                                priceDelta;
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getMetalUnit() {
        return metalUnit;
    }
    
    public void setMetalUnit(String metalUnit) {
        this.metalUnit = metalUnit;
    }
    
    public BigDecimal getDelta() {
        return delta;
    }
    
    public void setDelta(BigDecimal delta) {
        this.delta = delta;
    }
    
    @Override
    public String toString(){
        final StringBuilder sb = new StringBuilder();
        sb.append("CustomizationSizeMetalSpecification");
        sb.append("{Size='").append(size).append('\'');
        sb.append(", MetalUnit").append(metalUnit).append('\'');
        sb.append("delta=").append(delta).append('\'');
        sb.append("CustomizationMetalSpecification=").append(getCustomizationMetalSpecification());
        return sb.toString();
    }

    public BigDecimal getPriceDelta() {
        return priceDelta;
    }

    public void setPriceDelta(BigDecimal priceDelta) {
        this.priceDelta = priceDelta;
    }

    public CustomizationMetalSpecification getCustomizationMetalSpecification() {
        return customizationMetalSpecification;
    }

    public void setCustomizationMetalSpecification(CustomizationMetalSpecification customizationMetalSpecification) {
        this.customizationMetalSpecification = customizationMetalSpecification;
    }
}
