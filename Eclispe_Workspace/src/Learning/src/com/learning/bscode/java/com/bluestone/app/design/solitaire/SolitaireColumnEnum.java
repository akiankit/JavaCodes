package com.bluestone.app.design.solitaire;

public enum SolitaireColumnEnum {
    
    SHAPE("Shape", "cut", 0, false, true, "round",1, 0), 
    CARAT("Carat", "carat", 1, true, true,  "",0.1f, 2),
    CLARITY("Clarity", "clarity", 2, false, true, "",1, 0),
    PRICE("Price", "totalPrice", 3, true, true, "",3000, 0),
    MAKE("Cut", "make", 4, false, true,"",1, 0),
    COLOR("Color", "color", 5, false, true,"",1, 0),
    POLISH("Polish", "polish", 6, false, false, "",1, 0),
    SYMMETRY("Symmetry", "symmetry", 7, false, false, "",1, 0),
    DEPTH("Depth", "depth", 8, true, false,  "",1, 2),
    FLUORESCENCE("Fluorescence", "fluorescenceIntensity", 9, false, false,"",1, 0),
    TABLE("Table", "table_data", 10, true, false,  "",1, 2);   
    
    private String  displayColumnName;
    private String  dbColumnName;
    //private boolean isNumberRange  = false;
    private int     columnIndex    = 0;
    private boolean isSliderFilter = false;
    private boolean isVisible      = false;
    private final String filterValues;
    private final float step;
    //private final String prefix;
    //private final String formatName;
    private final int decimalPlaces;
    
    private SolitaireColumnEnum(String displayColumnName, 
                                String dbColumnName,                                
                                int columnIndex,
                                boolean isSliderFilter,
                                boolean isVisible, 
                                String filterValues,
                                float step,
                                int decimalPlaces) {
        this.displayColumnName = displayColumnName;
        this.dbColumnName = dbColumnName;
//        this.isNumberRange = isNumberRange;
        this.columnIndex = columnIndex;
        this.isSliderFilter = isSliderFilter;
        this.isVisible = isVisible;
        this.filterValues = filterValues;
        this.step = step;
        //this.prefix = prefix;
        //this.formatName = formatName;
        this.decimalPlaces = decimalPlaces;
    }
    
    public boolean isSliderFilter() {
        return isSliderFilter;
    }
    
    public void setSliderFilter(boolean isSliderFilter) {
        this.isSliderFilter = isSliderFilter;
    }
    
    public String getDisplayColumnName() {
        return displayColumnName;
    }
    
    public String getDbColumnName() {
        return dbColumnName;
    }
    
    /*public boolean isNumberRange() {
        return isNumberRange;
    }*/
    
    public int getColumnIndex() {
        return columnIndex;
    }
    
    public boolean isVisible() {
        return isVisible;
    }

    public String getFilterValues() {
        return filterValues;
    }

    public float getStep() {
        return step;
    }

/*    public String getPrefix() {
        return prefix;
    }

    public String getFormatName() {
        return formatName;
    }
*/
    public int getDecimalPlaces() {
        return decimalPlaces;
    }
    
}
