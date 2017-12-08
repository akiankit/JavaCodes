package com.bluestone.app.core.util;

public enum ImageEnum {
	BAND("bd", "Band Image"),   
	PERSPECTIVE("pr", "Perspective"),
	TOP("tp", "Top"),        
	FRONT("fr", "Front"),
	FRONTONE("f1", "Front One"),
	CLOSEUP("cu", "Close Up"),     
	PERSPECTIVE2NOS("p2", "Perspective2Nos"),
    SIDE("sd", "Side"),         
    BACK("bk", "Back"),         
    CLOSELAYDOWN("cd", "Close Laydown"),
    SCALE("sc", "Scale"),        
    HAND("hd", "Hand Image"),
    NECK("nc", "Neck Image"),
    EARRING("er", "Earring Image"),
	BANGLEHAND("bh", "Bangle Image");
	
	private final String shortName;
	private final String fullName;

	private ImageEnum(String shortName, String fullName) {
		this.shortName = shortName;
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public String getFullName() {
		return fullName;
	}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ImageEnum");
        sb.append("{shortName='").append(shortName).append('\'');
        sb.append(", fullName='").append(fullName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
