package com.bluestone.app.core.util;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bluestone.app.design.model.Design;
import com.bluestone.app.search.tag.model.Tag;

@Component
public class GoogleProductsFeedUtil {
	
	private static final Logger log = LoggerFactory.getLogger(GoogleProductsFeedUtil.class);
	
	public static String getGoogleProductCategory(String category){
		log.debug("GoogleProductsFeedUtil.getGoogleProductCategory() for category: {}", category);
		String productCategory = "Apparel & Accessories > Jewelry";
		if(category.equalsIgnoreCase("rings")){
			productCategory =  productCategory + " > Rings";
		}else if (category.equalsIgnoreCase("earrings")){
			productCategory = productCategory + " > Earrings";
		}else if (category.equalsIgnoreCase("pendants")){
			productCategory = productCategory + " > Charms & Pendants";
		}else if (category.equalsIgnoreCase("bangles") || category.equalsIgnoreCase("bracelets")){
			productCategory = productCategory + " > Bracelets";
		}else if (category.equalsIgnoreCase("tanmaniya")){
			productCategory = productCategory + " > Necklaces";
		}		
		log.debug("GoogleProductsFeedUtil.getGoogleProductCategory() resulted category: {}", productCategory);
		return productCategory;
	}
	
	public static String getTagsStringForProduct(Design design){
		StringBuilder tagString = new StringBuilder();
		Set<Tag> tags = design.getTags();
		for (Tag tag : tags) {
			tagString.append(LinkUtils.getFormattedTagName(tag.getName()));
	        tagString.append(",");
		}			    
	    return tagString.length() > 0 ? tagString.substring(0, tagString.length() - 1): "";
	}	

}
