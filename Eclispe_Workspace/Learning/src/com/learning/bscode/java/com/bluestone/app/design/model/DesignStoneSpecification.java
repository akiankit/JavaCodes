package com.bluestone.app.design.model;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="design_stone_specification")
public class DesignStoneSpecification extends BaseEntity {

    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name="design_id",nullable=false)
    private Design design;
    
    @Column(nullable = false)
    private int noOfStones;
    
    @Column(nullable = false)
    private String settingType; // prong , flush
    
    @Column(nullable = false)
    private short family;

    
    @Transient
    private int sheetIndex;
    
    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }
    public int getNoOfStones() {
        return noOfStones;
    }

    public void setNoOfStones(int noOfStones) {
        this.noOfStones = noOfStones;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public short getFamily() {
        return family;
    }

    public void setFamily(short family) {
        this.family = family;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" DesignStoneSpecification ");
        sb.append("{\n\t family=").append(family).append("\n");
        sb.append("\t noOfStones=").append(noOfStones).append("\n");
        sb.append("\t settingType=").append(settingType).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
