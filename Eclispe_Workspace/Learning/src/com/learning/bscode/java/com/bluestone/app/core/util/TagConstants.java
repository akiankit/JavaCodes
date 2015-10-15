package com.bluestone.app.core.util;

import java.util.HashMap;
import java.util.Map;

public class TagConstants {

	public static Map<String, String> tagGroups = new HashMap<String, String>();
	public static Map<String, String> formatedTags = new HashMap<String, String>();
	public static Map<String, String> giftTags = new HashMap<String, String>();
	public static Map<String, String> giftTagCategories = new HashMap<String, String>();
	public static final String TAG_CATEGORY_NUMBER_OF_STONE = "# of stones";
	public static final String TAG_CATEGORY_ALPHABET_JEWELLERY = "alphabet jewellery";
	public static final String TAG_CATEGORY_CHARACTERISTICS = "characteristics";
	public static final String TAG_CATEGORY_CLUSTERS = "clusters";
	public static final String TAG_CATEGORY_DESIGN = "design";
	public static final String TAG_CATEGORY_DROPS = "drops";
	public static final String TAG_CATEGORY_GENDER = "gender";
	public static final String TAG_CATEGORY_GOLD_PURITY = "gold purity";
	public static final String TAG_CATEGORY_HOOPS = "hoops";
	public static final String TAG_CATEGORY_METAL = "metal";
	public static final String TAG_CATEGORY_OCCASSION = "occassion";
	public static final String TAG_CATEGORY_PEARLS = "pearls";
	public static final String TAG_CATEGORY_PRICE = "price";
	public static final String TAG_CATEGORY_RELIGIOUS = "religious";
	public static final String TAG_CATEGORY_STONE_COLOR = "stone color";
	public static final String TAG_CATEGORY_STONE_SHAPE = "stone shape";
	public static final String TAG_CATEGORY_STONES = "stones";
	public static final String TAG_CATEGORY_STUDS = "studs";
	public static final String TAG_CATEGORY_STYLE = "style";
	public static final String TAG_CATEGORY_TANMANIYA = "tanmaniya";
	public static final String TAG_CATEGORY_TRINITY = "trinity";
	public static final String TAG_CATEGORY_TYPE = "type";
	public static final String TAG_CATEGORY_ZODIAC = "zodiac";
	public static final String TAG_ONLY_DIAMOND = "only diamond"; 
	public static final String TAG_RUBY = "ruby"; 
	public static final String TAG_GARNET = "garnet"; 
	public static final String TAG_AMETHYS = "amethys"; 
	public static final String TAG_SAPPHIRE = "sapphire"; 
	public static final String TAG_EMERALD = "emerald"; 
	public static final String TAG_ENGAGEMENT = "engagement"; 
	public static final String TAG_WEDDING = "wedding"; 
	public static final String TAG_COCKTAIL = "cocktail"; 
	public static final String TAG_YELLOW_GOLD = "yellow gold"; 
	public static final String TAG_WHITE_GOLD = "white gold"; 
	public static final String TAG_HEART = "heart"; 
	public static final String TAG_STUDS = "studs"; 
	public static final String TAG_HOOPS = "hoops"; 
	public static final String TAG_18K = "18k"; 
	public static final String TAG_RELIGIOUS = "religious"; 
	public static final String TAG_OM = "om"; 
	public static final String TAG_LAKSHMI = "lakshmi"; 
	public static final String TAG_GANESHA = "ganesha"; 
	public static final String TAG_DROPS = "drops"; 
	public static final String TAG_ALPHABET_JEWELLERY = "alphabet jewellery"; 
	public static final String TAG_MEN = "men"; 

	static {
		giftTags.put("gift", "gift");
	}
	
	static {
		giftTagCategories.put("by occasion", "show_on_gift");
		giftTagCategories.put("by relationship","show_on_gift");
		giftTagCategories.put("by price","show_on_gift");
	}
	
	static {
		tagGroups.put("cluster", "Cluster");
		tagGroups.put("studs", "Studs");
		tagGroups.put("drops", "Drops");
		tagGroups.put("hoops", "Hoops");
		tagGroups.put("pearls", "Pearls");
		tagGroups.put("trinity", "Trinity");
		tagGroups.put("religious", "Religious");
		tagGroups.put("two tone", "Two Tone");
		tagGroups.put("tanmaniya", "Tanmaniya");
		tagGroups.put("alphabet", "Alphabet");
		
	}
	static {
	formatedTags.put("below rs 10000","Below <span class=\"WebRupee\">Rs.</span> 10,000");        
	formatedTags.put("rs 10000 to rs 20000","<span class=\"WebRupee\">Rs.</span> 10,000 - <span class=\"WebRupee\">Rs.</span> 20,000"); 
	formatedTags.put("rs 20000 to rs 30000","<span class=\"WebRupee\">Rs.</span> 20,000 - <span class=\"WebRupee\">Rs.</span> 30,000 ");
	formatedTags.put("rs 30000 to rs 40000","<span class=\"WebRupee\">Rs.</span> 30,000 - <span class=\"WebRupee\">Rs.</span> 40,000");
	formatedTags.put("rs 50000 and above","<span class=\"WebRupee\">Rs.</span> 50,000 and Above");    
	formatedTags.put("rs 40000 to rs 50000","<span class=\"WebRupee\">Rs.</span> 40,000 - <span class=\"WebRupee\">Rs.</span> 50,000"); 
	formatedTags.put("tanmaniya", "Mangalsutra/Tanmaniya");
	formatedTags.put("yellow gold", "Gold");
	formatedTags.put("for birthday", "Birthday");
	formatedTags.put("for anniversary", "Anniversary");
	formatedTags.put("for engagement", "Engagement");
	formatedTags.put("for valentines day", "Valentines day");
	}
	
}