package com.bluestone.app.design.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "metal_specification")
public class MetalSpecification extends BaseEntity implements Cloneable {
    
    private static final long serialVersionUID = 1L;

    private int purity;  // 18 K
    
    private String color;   // Yellow
    
    private String type;    // Gold
    
    private String code;    // 18KY
    
    private String fullname; // 18 K Yellow Gold
    
    public int getPurity() {
        return purity;
    }

    public void setPurity(int purity) {
        this.purity = purity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "MetalSpecification [purity=" + purity + ", color=" + color + ", type=" + type + ", code=" + code
                + ", fullname=" + fullname + "]";
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
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((fullname == null) ? 0 : fullname.hashCode());
        result = prime * result + purity;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        if (!(obj instanceof MetalSpecification)) {
            return false;
        }
        MetalSpecification other = (MetalSpecification) obj;
        if (code == null) {
            if (other.code != null) {
                return false;
            }
        } else if (!code.equals(other.code)) {
            return false;
        }
        if (color == null) {
            if (other.color != null) {
                return false;
            }
        } else if (!color.equals(other.color)) {
            return false;
        }
        if (fullname == null) {
            if (other.fullname != null) {
                return false;
            }
        } else if (!fullname.equals(other.fullname)) {
            return false;
        }
        if (purity != other.purity) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
    
    
    
}
