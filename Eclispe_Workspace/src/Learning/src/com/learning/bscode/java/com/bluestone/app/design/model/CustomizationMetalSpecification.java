package com.bluestone.app.design.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="customization_metal_specification")
public class CustomizationMetalSpecification extends BaseEntity {
    
    private static final long serialVersionUID = 1L;

    // makes it 19 significant digits and 10 decimal points. i 9 on the left of decimal and 10 after decimal
    @Column(nullable=false, precision=19, scale=10)
    private BigDecimal metalWeight;

    @ManyToOne
    @JoinColumn(name="customization_id",nullable=false)
    private Customization customization;
    
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name="metal_specification_id",nullable=false)
    private MetalSpecification metalSpecification;
    
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name="design_metal_family_id",nullable=false)
    private DesignMetalFamily designMetalFamily;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "customizationMetalSpecification",cascade=CascadeType.PERSIST)
    @BatchSize(size=20)
    private List<CustomizationSizeMetalSpecification> customizationSizeMetalSpecifications;

    public Customization getCustomization() {
        return customization;
    }

    public void setCustomization(Customization customization) {
        this.customization = customization;
    }

    public MetalSpecification getMetalSpecification() {
        return metalSpecification;
    }

    public void setMetalSpecification(MetalSpecification metalSpecification) {
        this.metalSpecification = metalSpecification;
    }

    public BigDecimal getMetalWeight() {
        return metalWeight;
    }
    
    public BigDecimal getMetalWeight(String size){
        BigDecimal metalWeight = this.metalWeight;
        for (CustomizationSizeMetalSpecification eachCustomizationSizeMetalSpecification : customizationSizeMetalSpecifications) {
            if(eachCustomizationSizeMetalSpecification.getSize().equalsIgnoreCase(size)){
                metalWeight = metalWeight.add(eachCustomizationSizeMetalSpecification.getDelta());
            }
        }
        return metalWeight;
    }

    public void setMetalWeight(BigDecimal metalWeight) {
        this.metalWeight = metalWeight;
    }

    public DesignMetalFamily getDesignMetalFamily() {
        return designMetalFamily;
    }

    public void setDesignMetalFamily(DesignMetalFamily designMetalFamily) {
        this.designMetalFamily = designMetalFamily;
    }
    
    @Override
    public String toString() {
        return "CustomizationMetalSpecification [metalWeight=" + metalWeight + ", metalFamily=" + designMetalFamily.getFamily() + "]";
    }

    public List<CustomizationSizeMetalSpecification> getCustomizationSizeMetalSpecifications() {
        return customizationSizeMetalSpecifications;
    }

    public void setCustomizationSizeMetalSpecifications(List<CustomizationSizeMetalSpecification> customizationSizeMetalSpecifications) {
        this.customizationSizeMetalSpecifications = customizationSizeMetalSpecifications;
    }
    
}
