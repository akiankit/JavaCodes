package com.bluestone.app.design.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "stone_specification")
public class StoneSpecification extends BaseEntity implements Cloneable {
    
    private static final long serialVersionUID = 3939803274367754997L;
    
    @Column(nullable = false)
    private String            size;
    
    @Column(nullable = false)
    private String            shape;
    
    private String            stoneType;
    
    @Column(nullable=false, precision=19, scale=16)
    private BigDecimal             stoneWeight; // weight in carats
    
    @Column(nullable = true, scale = 3)
    private BigDecimal maxCarat;

    @Column(nullable = true, scale = 3)
    private BigDecimal minCarat;
    
    @Column(nullable = true)
    private String            clarity;
    
    @Column(nullable = true)
    private String            color;
    
    public String getStoneType() {
        return stoneType;
    }
    
    public void setStoneType(String stoneType) {
        this.stoneType = stoneType;
    }

    /**
     * This weight is in carats.
     * @return
     */
    public BigDecimal getStoneWeight() {
        return stoneWeight;
    }
    
    public void setStoneWeight(BigDecimal stoneWeightInCarats) {
        this.stoneWeight = stoneWeightInCarats;
    }
    
    public String getClarity() {
        return clarity;
    }
    
    public void setClarity(String clarity) {
        this.clarity = clarity;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getSize() {
        return size;
    }
    
    public void setSize(String size) {
        this.size = size;
    }
    
    public String getShape() {
        return shape;
    }
    
    public void setShape(String shape) {
        this.shape = shape;
    }
    

    public boolean isDiamond() {
        if ("Diamond".equalsIgnoreCase(stoneType)) {
            return true;
        }
        return false;
    }
    
    public boolean isSolitaire() {
        if ("Solitaire".equalsIgnoreCase(stoneType)) {
            return true;
        }
        return false;
    }
    
    public boolean isHeartShape() {
        if ("Heart".equalsIgnoreCase(shape)) {
            return true;
        }
        return false;
    }
    
    public boolean isStraightBaguetteShape() {
        if ("Straight Baguette".equalsIgnoreCase(shape)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "StoneSpecification [size=" + size + ", shape=" + shape + ", stoneType=" + stoneType + ", stoneWeight="
                + stoneWeight + ", clarity=" + clarity + ", color=" + color + "]";
    }
    
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((clarity == null) ? 0 : clarity.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((shape == null) ? 0 : shape.hashCode());
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((stoneType == null) ? 0 : stoneType.hashCode());
        result = prime * result + ((stoneWeight == null) ? 0 : stoneWeight.hashCode());
        result = prime * result + ((minCarat == null) ? 0 : minCarat.hashCode());
        result = prime * result + ((maxCarat == null) ? 0 : maxCarat.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof StoneSpecification)) {
            return false;
        }
        StoneSpecification other = (StoneSpecification) obj;
        if (clarity == null) {
            if (other.clarity != null) {
                return false;
            }
        } else if (!clarity.equals(other.clarity)) {
            return false;
        }
        if (color == null) {
            if (other.color != null) {
                return false;
            }
        } else if (!color.equals(other.color)) {
            return false;
        }
        if (shape == null) {
            if (other.shape != null) {
                return false;
            }
        } else if (!shape.equals(other.shape)) {
            return false;
        }
        if (size == null) {
            if (other.size != null) {
                return false;
            }
        } else if (!size.equals(other.size)) {
            return false;
        }
        if (stoneType == null) {
            if (other.stoneType != null) {
                return false;
            }
        } else if (!stoneType.equals(other.stoneType)) {
            return false;
        }
        if (stoneWeight == null) {
            if (other.stoneWeight != null) {
                return false;
            }
        } else if (!stoneWeight.equals(other.stoneWeight)) {
            return false;
        }

        if (minCarat == null) {
            if (other.minCarat != null) {
                return false;
            }
        } else if (!(minCarat.equals(other.minCarat))) {
            return false;
        }

        if (maxCarat == null) {
            if (other.maxCarat != null) {
                return false;
            }
        } else if (!(maxCarat.equals(other.maxCarat))) {
            return false;
        }

        return true;
    }

    public BigDecimal getMaxCarat() {
        return maxCarat;
    }

    public void setMaxCarat(BigDecimal maxCarat) {
        this.maxCarat = maxCarat;
    }

    public BigDecimal getMinCarat() {
        return minCarat;
    }

    public void setMinCarat(BigDecimal minCarat) {
        this.minCarat = minCarat;
    }
    
    public BigDecimal getStoneWeightInCarats(){
        return getStoneWeight();
    }
}
