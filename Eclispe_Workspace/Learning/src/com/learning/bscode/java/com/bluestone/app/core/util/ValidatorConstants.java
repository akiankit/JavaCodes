package com.bluestone.app.core.util;

import java.util.HashMap;
import java.util.Map;

public class ValidatorConstants {
    
    public static String DEFAULT_CLARITY = "SI";
    public static String DEFAULT_COLOR = "GH";
    
    public static Map<String, String> stoneColors     = new HashMap<String, String>();
    static {
        stoneColors.put("diamond", DEFAULT_COLOR);
        stoneColors.put("emerald", "Green");
        stoneColors.put("mother of pearl", "Off White");
        stoneColors.put("black onyx", "Black");
        stoneColors.put("white sapp", "White");
        stoneColors.put("amethyst", "Purple");
        stoneColors.put("aquamarine", "Blue");
        stoneColors.put("citrine", "Yellow");
        stoneColors.put("fresh water pearl", "Off White");
        stoneColors.put("green amethyst", "Green");
        stoneColors.put("iolite", "Blue");
        stoneColors.put("lemon quartz", "Yellow");
        stoneColors.put("mozambique garnet", "Red");
        stoneColors.put("peridot", "Green");
        stoneColors.put("smoky quartz", "Brown");
        stoneColors.put("swiss blue topaz", "Blue");
        stoneColors.put("tsavorite", "Green");
        stoneColors.put("ruby", "Red");
        stoneColors.put("pink sapphire", "Pink");
        stoneColors.put("yellow sapphire", "Yellow");
        stoneColors.put("blue sapphire", "Blue");
        stoneColors.put("white sapphire", "White");
        stoneColors.put("white sapp", "White");
        stoneColors.put("ruby & pink sapphire graduation", "Red");
        stoneColors.put("blue sapphire graduation", "Blue");
        stoneColors.put("blue sapphier graduation", "Blue");
        stoneColors.put("pink sapp", "Pink");
        stoneColors.put("blue sap", "Blue");
        stoneColors.put("psap", "Pink");
        stoneColors.put("black pearl", "Black");
        stoneColors.put("white pearl", "White");
        stoneColors.put("blue topaz", "Blue");
        stoneColors.put("green peridot", "Green");
        stoneColors.put("pink tourmaline", "Pink");
        stoneColors.put("yellow topaz", "Yellow");
        stoneColors.put("garnet", "Red");
        stoneColors.put("blue tourmaline", "Blue");
        stoneColors.put("green tourmaline", "Green");
        stoneColors.put("pearls", "Off White");
        
    }
    public static Map<String, String> stoneTypes      = new HashMap<String, String>();
    static {
    	stoneTypes.put("diamond_si_gh","Diamond_SI_GH");
    	stoneTypes.put("diamond_vs_gh","Diamond_VS_GH");
    	stoneTypes.put("diamond_vvs_ef","Diamond_VVS_EF");
        stoneTypes.put("diamond", "Diamond");
        stoneTypes.put("emerald", "Emerald");
        stoneTypes.put("mother of pearl", "Mother of Pearl");
        stoneTypes.put("black onyx", "Black Onyx");
        stoneTypes.put("white sapp", "White Sapphire");
        stoneTypes.put("amethyst", "Amethyst");
        stoneTypes.put("aquamarine", "Aquamarine");
        stoneTypes.put("citrine", "Citrine");
        stoneTypes.put("fresh water pearl", "Fresh Water Pearl");
        stoneTypes.put("green amethyst", "Green Amethyst");
        stoneTypes.put("iolite", "Iolite");
        stoneTypes.put("lemon quartz", "Lemon Quartz");
        stoneTypes.put("mozambique garnet", "Mozambique Garnet");
        stoneTypes.put("peridot", "Peridot");
        stoneTypes.put("smoky quartz", "Smoky Quartz");
        stoneTypes.put("swiss blue topaz", "Swiss Blue Topaz");
        stoneTypes.put("tsavorite", "Tsavorite");
        stoneTypes.put("ruby", "Ruby");
        stoneTypes.put("sapphire", "Sapphire");
        stoneTypes.put("pink sapphire", "Pink Sapphire");
        stoneTypes.put("yellow sapphire", "Yellow Sapphire");
        stoneTypes.put("blue sapphire", "Blue Sapphire");
        stoneTypes.put("white sapphire", "White Sapphire");
        stoneTypes.put("white sapp", "White Sapphire");
        stoneTypes.put("ruby & pink sapphire graduation", "Ruby & Pink Sapphire Graduation");
        stoneTypes.put("blue sapphire graduation", "Blue Sapphire Graduation");
        stoneTypes.put("blue sapphier graduation", "Blue Sapphire Graduation");
        stoneTypes.put("pink sapp", "Pink Sapphire");
        stoneTypes.put("blue sap", "Blue Sapphire");
        stoneTypes.put("psap", "Pink Sapphire");
        stoneTypes.put("black pearl", "Black Pearl");
        stoneTypes.put("white pearl", "White Pearl");
        stoneTypes.put("blue topaz", "Blue Topaz");
        stoneTypes.put("green peridot", "Green Peridot");
        stoneTypes.put("pink tourmaline", "Pink Tourmaline");
        stoneTypes.put("yellow topaz", "Yellow Topaz");
        stoneTypes.put("garnet", "Garnet");
        stoneTypes.put("blue tourmaline", "Blue Tourmaline");
        stoneTypes.put("green tourmaline", "Green Tourmaline");
        stoneTypes.put("pearls", "Pearls");
        stoneTypes.put("tanzanite", "Tanzanite");
        stoneTypes.put("solitaire" , "Solitaire");
        stoneTypes.put("rhodolite", "Rhodolite");
        
    }
    public static Map<String, String> stoneShapes     = new HashMap<String, String>();
    static {
        stoneShapes.put("oval", "Oval");
        stoneShapes.put("oct", "Octagon");
        stoneShapes.put("oct", "Emerald Cut");
        stoneShapes.put("pear", "Pear");
        stoneShapes.put("rnd", "Round");
        stoneShapes.put("round", "Round");
        stoneShapes.put("mix", "Mix");
        stoneShapes.put("mqs", "Marquise");
        stoneShapes.put("octogun", "Octagon");
        stoneShapes.put("octagon", "Emerald Cut");
        stoneShapes.put("octogun", "Emerald Cut");
        stoneShapes.put("emerald cut", "Emerald Cut");
        stoneShapes.put("cushion cut", "Cushion Cut");
        stoneShapes.put("sq", "Square");
        stoneShapes.put("sqr", "Square");
        stoneShapes.put("square", "Square");
        stoneShapes.put("triangle", "Triangle");
        stoneShapes.put("plain cut", "Plain Cut");
        stoneShapes.put("prn", "Princess");
        stoneShapes.put("princess", "Princess");
        stoneShapes.put("bugg", "Baguette");
        stoneShapes.put("pearl", "Pearl");
        stoneShapes.put("marquise", "Marquise");
        stoneShapes.put("default", "Mix");
        stoneShapes.put("heart", "Heart");
        stoneShapes.put("ascher", "Ascher");
        stoneShapes.put("trillion", "Trillion");
        stoneShapes.put("straight baguette", "Straight Baguette");
        stoneShapes.put("cushion", "Cushion");
        stoneShapes.put("drop faceted", "Drop Faceted");
    }
    
    public static Map<String, String> qualityStones   = new HashMap<String, String>();
    static {
        qualityStones.put("pink sapphire", "Pink Sapphire");
        qualityStones.put("yellow sapphire", "Yellow Sapphire");
        qualityStones.put("blue sapphire", "Blue Sapphire");
        qualityStones.put("ruby", "Ruby");
        qualityStones.put("emerald", "Emerald");
    }
    public static Map<String, String> stoneQualities  = new HashMap<String, String>();
    static {
        
        stoneQualities.put("a", "A");
        stoneQualities.put("b", "B");
        stoneQualities.put("c", "C");
        stoneQualities.put("", "");
    }
    
    public static Map<String, String> metalColors     = new HashMap<String, String>();
    static {
        metalColors.put("white", "White");
        metalColors.put("rose/white", "Rose/White");
        metalColors.put("white/rose", "White/Rose");
        metalColors.put("yellow/white", "Yellow/White");
        metalColors.put("white/yellow", "White/Yellow");
        metalColors.put("rose/yellow", "Rose/Yellow");
        metalColors.put("yellow/rose", "Yellow/Rose");
        metalColors.put("white/pink", "White/Pink");
        metalColors.put("rose", "Rose");
        metalColors.put("yellow", "Yellow");
        metalColors.put("yellow/white/rose", "Yellow/White/Rose");// italian
                                                                  // collection
        metalColors.put("rose", "Rose"); // After filtering prod list
        metalColors.put("pink", "Pink"); // After filtering prod list
        metalColors.put("rose gold", "Rose");
    }
    public static Map<String, String> settingTypes    = new HashMap<String, String>();
    static {
        settingTypes.put("half channel", "Half Channel");
        settingTypes.put("bezel", "Bezel");
        settingTypes.put("channel", "Channel");
        settingTypes.put("flush", "Flush");
        settingTypes.put("invisible", "Invisible");
        settingTypes.put("pave", "Pave");
        settingTypes.put("pressure", "Pressure");
        settingTypes.put("prong", "Prong");
        settingTypes.put("channel bezel", "Channel Bezel");
        settingTypes.put("channel prong", "Channel Prong");
        settingTypes.put("flush", "Flush");
        settingTypes.put("half bezel", "Half Bezel");
        settingTypes.put("illusion", "Illusion");
        settingTypes.put("itching", "Itching");
        settingTypes.put("micro pave", "Micro Pave");
        settingTypes.put("unique", "Unique");
        settingTypes.put("pressure / prong", "Pressure / Prong");
        settingTypes.put("pressure/prong", "Pressure / Prong");
        settingTypes.put("pressure/bezel", "Pressure/Bezel");
        settingTypes.put("channel/bezel", "Channel/Bezel");
        settingTypes.put("prong/channel", "Prong/Channel");
        settingTypes.put("prong/bezel", "Prong/Bezel");
        settingTypes.put("bezel/pave", "Bezel/Pave");
        settingTypes.put("pave/bezel", "Bezel/Pave");
        settingTypes.put("prong/bezel", "Prong/Bezel");
        settingTypes.put("cap", "Cap");
        settingTypes.put("channel/prong", "Channel/Prong");
        settingTypes.put("clip", "Clip");
        settingTypes.put("lip", "Lip");
        settingTypes.put("plate prong", "Plate Prong");
        settingTypes.put("prong/flush", "Prong/Flush");
        settingTypes.put("micro pave/plate prong", "Micro Pave/Plate Prong");
        settingTypes.put("pave/plate prong", "Pave/Plate Prong");
        settingTypes.put("prong/plate prong", "Prong/Plate Prong");
        settingTypes.put("prong/pave", "Prong/Pave");
        settingTypes.put("plate prong/bezel", "Plate Prong/Bezel");
        settingTypes.put("bar", "Bar");
        settingTypes.put("Pressure/Prong","Pressure/Prong");
        settingTypes.put("Pave/Bezel","Pave/Bezel");
        settingTypes.put("Channel/Prong","Channel/Prong");
        settingTypes.put("Tension","Tension");
        settingTypes.put("Pave/Flush","Pave/Flush");
        settingTypes.put("Pave/Plate Prong","Pave/Plate Prong");
        settingTypes.put("Micro Pave/ Plate Prong","Micro Pave/ Plate Prong");
        settingTypes.put("Pressure/Pave","Pressure/Pave");
        settingTypes.put("Plate Prong/Bezel","Plate Prong/Bezel");
        settingTypes.put("Channel/Invisible","Channel/Invisible");
        settingTypes.put("Pressure/Plate Prong","Pressure/Plate Prong");
        settingTypes.put("Prong/Pave/Plate Prong","Prong/Pave/Plate Prong");
        settingTypes.put("Pressure/Prong/Plate Prong","Pressure/Prong/Plate Prong");
        settingTypes.put("Pressure/Plate Prong","Pressure/Plate Prong");
        settingTypes.put("Prong/Pave/Pressure","Prong/Pave/Pressure");
        settingTypes.put("Prong/Pave/Plate Prong","Prong/Pave/Plate Prong");
        settingTypes.put("Channel/Plate prong","Channel/Plate Prong");
        

    }
    public static Map<String, String> findings        = new HashMap<String, String>();
    static {
        
        findings.put("butterfly", "Butterfly");
        findings.put("chain", "Chain");
        findings.put("lobster", "Lobster");
        findings.put("post", "Post");
        findings.put("russian lock", "Russian Lock");
    }
    
    public static Map<String, String> noSettingStones = new HashMap<String, String>();
    static {
        
        noSettingStones.put("fresh water pearl", "fresh water pearl");
        noSettingStones.put("mother of pearl", "mother of pearl");
        noSettingStones.put("black pearl", "black pearl");
        noSettingStones.put("white pearl", "white pearl");
        
    }
}
