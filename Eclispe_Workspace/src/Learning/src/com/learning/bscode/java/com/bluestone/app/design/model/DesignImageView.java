package com.bluestone.app.design.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.ImageEnum;
import com.bluestone.app.core.util.ImageProperties;

@Audited
@Entity
@Table(name = "design_image_view")
public class DesignImageView extends BaseEntity {
    
    private static final long serialVersionUID   = 1L;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="design_id",nullable=false)
    @Fetch(FetchMode.JOIN)
    private Design            design;
    
    private boolean           hasBand            ;
    private boolean           hasPerspective     ;
    private boolean           hasTop             ;
    private boolean           hasFront           ;
    private boolean           hasCloseUp         ;
    private boolean           hasPerspective2Nos ;
    private boolean           hasSide            ;
    private boolean           hasBack            ;
    private boolean           hasCloseLaydown    ;
    private boolean           hasScale           ;
    private boolean           hasHand            ;
    private boolean           hasNeck            ;
    private boolean           hasEarring         ;
    private boolean           hasBangleHand      ;
    private boolean           hasFrontOne     	 ;
    
    
    public Design getDesign() {
        return design;
    }
    
    public void setDesign(Design design) {
        this.design = design;
    }
    
    public boolean isHasBand() {
        return hasBand;
    }
    
    public void setHasBand(boolean hasBand) {
        this.hasBand = hasBand;
    }
    
    public boolean isHasPerspective() {
        return hasPerspective;
    }
    
    public void setHasPerspective(boolean hasPerspective) {
        this.hasPerspective = hasPerspective;
    }
    
    public boolean isHasTop() {
        return hasTop;
    }
    
    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }
    
    public boolean isHasFront() {
        return hasFront;
    }
    
    public void setHasFront(boolean hasFront) {
        this.hasFront = hasFront;
    }
    
    public boolean isHasCloseUp() {
        return hasCloseUp;
    }
    
    public void setHasCloseUp(boolean hasCloseUp) {
        this.hasCloseUp = hasCloseUp;
    }
    
    public boolean isHasPerspective2Nos() {
        return hasPerspective2Nos;
    }
    
    public void setHasPerspective2Nos(boolean hasPerspective2Nos) {
        this.hasPerspective2Nos = hasPerspective2Nos;
    }
    
    public boolean isHasSide() {
        return hasSide;
    }
    
    public void setHasSide(boolean hasSide) {
        this.hasSide = hasSide;
    }
    
    public boolean isHasBack() {
        return hasBack;
    }
    
    public void setHasBack(boolean hasBack) {
        this.hasBack = hasBack;
    }
    
    public boolean isHasCloseLaydown() {
        return hasCloseLaydown;
    }
    
    public void setHasCloseLaydown(boolean hasCloseLaydown) {
        this.hasCloseLaydown = hasCloseLaydown;
    }
    
    public boolean isHasScale() {
        return hasScale;
    }
    
    public void setHasScale(boolean hasScale) {
        this.hasScale = hasScale;
    }
    
    public boolean isHasHand() {
        return hasHand;
    }
    
    public void setHasHand(boolean hasHand) {
        this.hasHand = hasHand;
    }
    
    public List<ImageEnum> getAllImagesViews(String categoryType) {
        List<ImageEnum> imageViews = new ArrayList<ImageEnum>();
        List<ImageEnum> imagePriorityList = ImageProperties.imagePriorityMap.get(categoryType.toLowerCase());
        if(imagePriorityList !=null){
            for (ImageEnum imageEnum : imagePriorityList) {
                switch (imageEnum) {
                    case BAND:
                        if (hasBand) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case BACK:
                        if (hasBack) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case CLOSELAYDOWN:
                        if (hasCloseLaydown) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case CLOSEUP:
                        if (hasCloseUp) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case FRONT:
                        if (hasFront) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case FRONTONE:
                        if (hasFrontOne) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case HAND:
                        if (hasHand) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case PERSPECTIVE:
                        if (hasPerspective) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case PERSPECTIVE2NOS:
                        if (hasPerspective2Nos) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case SCALE:
                        if (hasScale) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case SIDE:
                        if (hasSide) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case TOP:
                        if (hasTop) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case NECK:
                        if (hasNeck) {
                            imageViews.add(imageEnum);
                        }
                        break;
                    case EARRING:
                        if (hasEarring) {
                            imageViews.add(imageEnum);
                        }
                    break;
                    case BANGLEHAND:
                        if (hasBangleHand) {
                            imageViews.add(imageEnum);
                        }
                    break;
                    default:
                        break;
                }
            }
        }
        return imageViews;
    }
    
    public boolean isHasNeck() {
        return hasNeck;
    }
    
    public void setHasNeck(boolean hasNeck) {
        this.hasNeck = hasNeck;
    }

    public boolean isHasEarring() {
        return hasEarring;
    }

    public void setHasEarring(boolean hasEarring) {
        this.hasEarring = hasEarring;
    }

    public boolean isHasBangleHand() {
        return hasBangleHand;
    }

    public void setHasBangleHand(boolean hasBangleHand) {
        this.hasBangleHand = hasBangleHand;
    }

	public boolean isHasFrontOne() {
		return hasFrontOne;
	}

	public void setHasFrontOne(boolean hasFrontOne) {
		this.hasFrontOne = hasFrontOne;
	}

    public void clearAllViews(){
        setHasBand(false);
        setHasBack(false);
        setHasCloseLaydown(false);
        setHasCloseUp(false);
        setHasFront(false);
        setHasHand(false);
        setHasPerspective(false);
        setHasPerspective2Nos(false);
        setHasScale(false);
        setHasSide(false);
        setHasTop(false);
        setHasNeck(false);
        setHasEarring(false);
        setHasBangleHand(false);
        setHasFrontOne(false);
    }
    
}
