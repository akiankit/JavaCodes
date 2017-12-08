package com.bluestone.app.design.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Field;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.DataSheetConstants;

@Audited
@Entity
@Table(name="design_category")
public class DesignCategory extends BaseEntity {

    private static final long serialVersionUID = 4962703021199295824L;

    @Field
	@Column(name="category_type", unique=true,nullable=false)
	private String categoryType;
	
	@Column(name="base_size", nullable= true)
	private String baseSize;

	public String getCategoryType() {
		return categoryType;
	}
	
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}

    public String getBaseSize() {
        return baseSize;
    }

    public void setBaseSize(String baseSize) {
        this.baseSize = baseSize;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Design Category");
        sb.append("{categoryType=").append(categoryType);
        sb.append(", baseSize=").append(baseSize).append("}");
        return sb.toString();
    }

    /*public boolean isChainsOrTanmanyiaChains() {
        boolean result = false;
        String categoryType = getCategoryType();
        if (isTanmaniyaChains() || isChains()) {
            result = true;
        }
        return result;
    }*/

    public boolean isChains() {
        return DataSheetConstants.DESIGN_CATEGORYTYPE.CHAINS.getName().equalsIgnoreCase(getCategoryType());
    }

    public boolean isTanmaniyaChains() {
        return DataSheetConstants.DESIGN_CATEGORYTYPE.TANMANIYA_CHAINS.getName().equalsIgnoreCase(getCategoryType());
    }

    public boolean isRings() {
        String categoryType = getCategoryType();
        boolean isRing = DataSheetConstants.DESIGN_CATEGORYTYPE.RINGS.getName().equalsIgnoreCase(categoryType);
        boolean isSolitaireRingMount = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_RING_MOUNT.getName().equalsIgnoreCase(categoryType);
        return isRing || isSolitaireRingMount;
    }

    public boolean isBangles() {
        return DataSheetConstants.DESIGN_CATEGORYTYPE.BANGLES.getName().equalsIgnoreCase(getCategoryType());
    }

    public boolean iSolitaireMountCategroy() {
        String categoryType = getCategoryType();
        boolean isSolitaireRingMount = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_RING_MOUNT.getName().equalsIgnoreCase(categoryType);
        boolean isSolitairePendantMount = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_PENDANT_MOUNT.getName().equalsIgnoreCase(categoryType);
        return isSolitaireRingMount || isSolitairePendantMount;
    }


}
