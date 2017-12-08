package com.bluestone.app.design.solitaire;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolitaireColumnMetaData {
    
    private static final Logger log            = LoggerFactory.getLogger(SolitaireColumnMetaData.class);
        
    private int                 columnIndex    = 0;
    private boolean             isSliderFilter = false;
    private String              displayColumnName;
    private String              dbColumnName;
    private boolean             isVisible      = false;
    private BigDecimal          minRange       = new BigDecimal(0.0);
    private BigDecimal          maxRange       = new BigDecimal(0.0);
    private String            filterValues;    
    
    private float               step;

//    private String prefix;

    //private String formatName;

    private int decimalPlaces;
    
    public SolitaireColumnMetaData(SolitaireColumnEnum solitaireColumnEnum) {
//        this.isNumberRange = solitaireColumnEnum.isNumberRange();
        this.columnIndex = solitaireColumnEnum.getColumnIndex();
        this.isSliderFilter = solitaireColumnEnum.isSliderFilter();
        this.displayColumnName = solitaireColumnEnum.getDisplayColumnName();
        this.dbColumnName = solitaireColumnEnum.getDbColumnName();
        this.isVisible = solitaireColumnEnum.isVisible();
        this.filterValues = solitaireColumnEnum.getFilterValues();
        this.step = solitaireColumnEnum.getStep();
/*        this.prefix = solitaireColumnEnum.getPrefix();
*/        //this.formatName = solitaireColumnEnum.getFormatName();
        this.decimalPlaces = solitaireColumnEnum.getDecimalPlaces();
    }    
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public boolean isSliderFilter() {
        return isSliderFilter;
    }
    
    public String getDisplayColumnName() {
        return displayColumnName;
    }
    
    public String getDbColumnName() {
        return dbColumnName;
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public BigDecimal getMinRange() {
        return minRange;
    }
    
    public BigDecimal getMaxRange() {
        return maxRange;
    }
    
    public String getFilterValues() {
        return filterValues;
    }
    
    public void setFilterValues(String filterValues) {
		this.filterValues = filterValues;
	}

	public void setMinRange(BigDecimal minRange) {
        this.minRange = minRange;
    }
    
    public void setMaxRange(BigDecimal maxRange) {
        this.maxRange = maxRange;
    }
    
    public float getStep() {
        return step;
    }
    
   /* public String getPrefix() {
        return prefix;
    }*/

  /*  public String getFormatName() {
        return formatName;
    }*/

    public int getDecimalPlaces() {
        return decimalPlaces;
    }
    
}
