package com.bluestone.app.core.util;

public enum CssClassEnum {
    DIAMOND("Diamond",true,7),
    EMERALD("Emerald",true,11),
    BLUE_SAPPHIRE("Blue Sapphire",true,4),
    WHITE_SAPPHIRE("White Sapphire",true,25),
    PINK_SAPPHIRE("Pink Sapphire",true,12),
    YELLOW_SAPPHIRE("Yellow Sapphire",true,6),
    TSAVORITE("Tsavorite",true,17),
    PINK_TOURMALINE("Pink Tourmaline",true,12),
    GREEN_TOURMALINE("Green Tourmaline",true,9),
    BLUE_TOPAZ("Blue Topaz",true,5),
    AQUAMARINE("Aquamarine",true,2),
    IOLITE("Iolite",true,8),
    CITRINE("Citrine",true,13),
    AMETHYST("Amethyst",true,1),
    PERIDOT("Peridot",true,10),
    RHODOLITE("Rhodolite",true,16),
    GARNET("Garnet",true,17),
    RUBY("Ruby",true,14),
    LEMON_QUARTZ("Lemon Quartz",true,19),
    GREEN_AMETHYST("Green Amethyst",true,20),
    TANZANITE("Tanzanite",true,21),
    CORAL("Coral",true,22),
    PEARL("Pearl",true,23),
    CATS_EYE("Cats Eye",true,24),
    HESSONITE("Hessonite",true,3),
    //Metal Classes
    YELLOW_GOLD("18 Kt Yellow Gold",false,2),
    WHITE_GOLD("18 Kt White Gold",false,1),
    ROSE_GOLD("18 Kt Rose Gold",false,3);
    
    private String  stoneOrMetalName;
    private int     classIndex;
    private boolean isStone;
    
    private CssClassEnum(String stoneOrMetalName, boolean isStone, int classIndex) {
        this.stoneOrMetalName = stoneOrMetalName;
        this.classIndex = classIndex;
        this.isStone = isStone;
    }
    
    public String getStoneOrMetalName() {
        return stoneOrMetalName;
    }
    
    public int getClassIndex() {
        return classIndex;
    }
    
    public boolean getIsStone() {
        return isStone;
    }
}
