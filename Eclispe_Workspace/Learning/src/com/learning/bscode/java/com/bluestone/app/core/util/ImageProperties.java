package com.bluestone.app.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageProperties {

	public static final String[] imageSizes = {"large","home", "medium", "small","original"};
	public static final Integer[] imageSizeDimension = {375,200,66,45,0};
	public static final String imageExtension = ".jpg";
	public static final String resizeImageExtension = "jpg";
	
	// category vs sorted list of image priorities
	public static Map<String, List<ImageEnum>> imagePriorityMap = new HashMap<String, List<ImageEnum>>();
	
	// category vs required list of image priorities
    public static Map<String, List<ImageEnum>> reqImageMap = new HashMap<String, List<ImageEnum>>();
	
	// image view extension vs ImageEnum
	public static Map<String, ImageEnum> imageViewNames = new HashMap<String, ImageEnum>();
	// TODO move to properties file
	static String pendants = "fr,f1,nc,sc,pr,sd,cu,bk,cd,bd";
	static String tanmaniya = "fr,f1,nc,sc,pr,sd,cu,bk,cd,bd";
	static String freeGift = "fr,f1,nc,sc,pr,sd,cu,bk,cd,bd";
	static String rings = "pr,hd,sc,tp,fr,f1,sd,cu";
	static String earrings = "p2,sc,er,fr,f1,sd,bk,cd";
	static String bangles =  "p2,pr,bh,sc,fr,f1,sd,cd,cu";
	static String bracelets = "fr,f1,sc,cu";
	static String chains = "pr,fr,f1,nc,sc,sd,cu,bk,cd,bd";
	static String tanmaniyaChains = "pr,fr,f1,nc,sc,sd,cu,bk,cd,bd";
	
	static String reqImagePendants = "fr";
	static String reqImageFreeGift = "fr";
    static String reqImageRings = "fr";
    static String reqImageEarrings = "fr";
    static String reqImageBangles =  "fr";
    static String reqImageBracelets = "fr";
    static String reqImageChains = "fr";

	static {
		imageViewNames.put("bd", ImageEnum.BAND);
		imageViewNames.put("pr", ImageEnum.PERSPECTIVE);
		imageViewNames.put("tp", ImageEnum.TOP);
		imageViewNames.put("fr", ImageEnum.FRONT);
		imageViewNames.put("f1", ImageEnum.FRONTONE);
		imageViewNames.put("cu", ImageEnum.CLOSEUP);
		imageViewNames.put("p2", ImageEnum.PERSPECTIVE2NOS);
		imageViewNames.put("sd", ImageEnum.SIDE);
		imageViewNames.put("bk", ImageEnum.BACK);
		imageViewNames.put("cd", ImageEnum.CLOSELAYDOWN);
		imageViewNames.put("sc", ImageEnum.SCALE);
		imageViewNames.put("hd", ImageEnum.HAND);
		imageViewNames.put("nc", ImageEnum.NECK);
		imageViewNames.put("er", ImageEnum.EARRING);
		imageViewNames.put("bh", ImageEnum.BANGLEHAND);
		
		
		imagePriorityMap.put("pendants", addImageViews(pendants));
		imagePriorityMap.put("rings", addImageViews(rings));
		imagePriorityMap.put("earrings", addImageViews(earrings));
		imagePriorityMap.put("bangles", addImageViews(bangles));
		imagePriorityMap.put("bracelets", addImageViews(bracelets));
		imagePriorityMap.put("chains", addImageViews(chains));
		imagePriorityMap.put("free gift", addImageViews(freeGift));
		imagePriorityMap.put("tanmaniya chains", addImageViews(tanmaniyaChains));
		imagePriorityMap.put("tanmaniya", addImageViews(tanmaniya));
		imagePriorityMap.put("solitaire ring mount", addImageViews(rings));
		imagePriorityMap.put("solitaire pendant mount", addImageViews(pendants));
		
		
		
		reqImageMap.put("pendants", addImageViews(reqImagePendants));
		reqImageMap.put("rings", addImageViews(reqImageRings));
		reqImageMap.put("earrings", addImageViews(reqImageEarrings));
		reqImageMap.put("bangles", addImageViews(reqImageBangles));
		reqImageMap.put("bracelets", addImageViews(reqImageBracelets));
		reqImageMap.put("chains", addImageViews(reqImageChains));
		reqImageMap.put("free gift", addImageViews(reqImageFreeGift));

	}

	private static List<ImageEnum> addImageViews(String categoryType) {
		String[] priorities = categoryType.split(",");
		List<ImageEnum> imageViews = new ArrayList<ImageEnum>() ;
		for (String eachPriority : priorities) {
			imageViews.add(imageViewNames.get(eachPriority));
		}
		return imageViews;
	}
	
	
}