package com.bluestone.app.design.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
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
@Table(name = "customization_stone_specification")
public class CustomizationStoneSpecification extends BaseEntity {
    
    private static final long         serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "customization_id", nullable = false)
    private Customization             customization;
    
    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "stone_specification_id", nullable = false)
    private StoneSpecification       stoneSpecification;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "design_stone_specification_id", nullable = false)
    private DesignStoneSpecification designStoneSpecification;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "customizationStoneSpecification", cascade=CascadeType.PERSIST)
    @BatchSize(size=20)
    private List<CustomizationSizeStoneSpecification> customizationSizeStoneSpecifications;
    
    public Customization getCustomization() {
        return customization;
    }
    
    public void setCustomization(Customization customization) {
        this.customization = customization;
    }
    
    public StoneSpecification getStoneSpecification() {
        return stoneSpecification;
    }
    
    public void setStoneSpecification(StoneSpecification stoneSpecification) {
        this.stoneSpecification = stoneSpecification;
    }
    
    public BigDecimal getTotalWeight() {
        BigDecimal stoneWeight = stoneSpecification.getStoneWeight().multiply(new BigDecimal(this.designStoneSpecification.getNoOfStones()));
        return stoneWeight;
    }
    
    public DesignStoneSpecification getDesignStoneSpecification() {
        return designStoneSpecification;
    }
    
    public void setDesignStoneSpecification(DesignStoneSpecification designStoneSpecification) {
        this.designStoneSpecification = designStoneSpecification;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" CustomizationStoneSpecification ");
        sb.append("{\n\t designStoneSpecification=").append(designStoneSpecification).append("\n");
        sb.append("\t stoneSpecification=").append(stoneSpecification).append("\n");
        sb.append("\n}");
        return sb.toString();
    }

    public List<CustomizationSizeStoneSpecification> getCustomizationSizeStoneSpecifications() {
        return customizationSizeStoneSpecifications;
    }

    public void setCustomizationSizeStoneSpecifications(List<CustomizationSizeStoneSpecification> customizationSizeStoneSpecifications) {
        this.customizationSizeStoneSpecifications = customizationSizeStoneSpecifications;
    }
}
