package com.bluestone.app.core.util;

import java.util.HashMap;
import java.util.Map;

public class DesignConstants {

	public static Map<String, String> metalCodes = new HashMap<String, String>();
	public static Map<String, String> vendorCodes = new HashMap<String, String>();
	public static Map<String, String> stoneCodes = new HashMap<String, String>();
	public static Map<String, String> diamondClarity = new HashMap<String, String>();
	public static Map<String, String> diamondColor = new HashMap<String, String>();
	public static String[] metalPurities = {"14","18","22"};
	public static String[] diamondQualities = {"DIG4","DIE4","DIC2"};
	static {
		// METAL CODES
		metalCodes.put("white gold", "W");
		metalCodes.put("rose gold", "R");
		metalCodes.put("pink gold", "R");
		metalCodes.put("yellow gold", "Y");
		metalCodes.put("platinum", "P");
		metalCodes.put("silver", "S");
		metalCodes.put("two tone gold", "T");

		// VENDOR CODES
		vendorCodes.put("kgk", "KG");
		vendorCodes.put("p&s", "PS");
		vendorCodes.put("ishaan", "IS");
		vendorCodes.put("sambhav", "SB");

		//DIAMOND CLARITY
				diamondClarity.put("Diamond","SI");
				diamondClarity.put("Diamond_SI_GH","SI");
				diamondClarity.put("Diamond_VS_GH","VS");
				diamondClarity.put("Diamond_VVS_EF","VVS");
				
				//DIAMOND COLOR
				diamondColor.put("Diamond","GH");
				diamondColor.put("Diamond_SI_GH","GH");
				diamondColor.put("Diamond_VS_GH","GH");
				diamondColor.put("Diamond_VVS_EF","EF");
				
		//STONE CODES
		stoneCodes.put("diamond_SI_GH","DIG4");
        stoneCodes.put("diamond_VS_GH","DIE4");
        stoneCodes.put("diamond_VVS_EF","DIC2");
		stoneCodes.put("diamond", "DI");
		stoneCodes.put("emerald", "EMER");
		stoneCodes.put("mother of pearl", "MOPE");
		stoneCodes.put("black onyx", "BLON");
		stoneCodes.put("white sapp", "WHSP");
		stoneCodes.put("amethyst", "AMET");
		stoneCodes.put("aquamarine", "AQUA");
		stoneCodes.put("citrine", "CITR");
		stoneCodes.put("fresh water pearl", "FRWP");
		stoneCodes.put("green amethyst", "GRAM");
		stoneCodes.put("iolite", "IOLI");
		stoneCodes.put("lemon quartz", "LEQU");
		stoneCodes.put("mozambique garnet", "MOZA");
		stoneCodes.put("peridot", "PERI");
		stoneCodes.put("smoky quartz", "SMQU");
		stoneCodes.put("swiss blue topaz", "SWBT");
		stoneCodes.put("tsavorite", "TSAV");
		stoneCodes.put("ruby", "RUBY");
		stoneCodes.put("sapphire", "SAPP");
		stoneCodes.put("pink sapphire", "PISP");
		stoneCodes.put("yellow sapphire", "YLSP");
		stoneCodes.put("blue sapphire", "BLSP");
		stoneCodes.put("white sapphire", "WHSP");
		stoneCodes.put("white sapp", "WHSP");
		stoneCodes.put("ruby & pink sapphire graduation", "RPSG");
		stoneCodes.put("blue sapphire graduation", "BLSG");
		stoneCodes.put("pink sapp", "PISP");
		stoneCodes.put("blue sap", "BLSP");
		stoneCodes.put("psap", "PISP");
		stoneCodes.put("black pearl", "BLPR");
		stoneCodes.put("white pearl", "WHPR");
		stoneCodes.put("blue topaz", "BLTO");
		stoneCodes.put("green peridot", "GRPD");
		stoneCodes.put("pink tourmaline", "PITR");
		stoneCodes.put("yellow topaz", "YLTO");
		stoneCodes.put("garnet", "GARN");
		stoneCodes.put("blue tourmaline", "BLTR");
		stoneCodes.put("green tourmaline", "GRTR");
		stoneCodes.put("pearls", "PRLS");
		stoneCodes.put("tanzanite", "TANZ");
		stoneCodes.put("rhodolite", "RHOD");
		stoneCodes.put("pearl", "PERL");
		stoneCodes.put("coral", "CORL");
		stoneCodes.put("cats eye", "CAEY");
		stoneCodes.put("hessonite", "HESS");
	}

}
