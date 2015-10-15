package com.bluestone.app.seo.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagCategory;

/**
 * @author Rahul Agrawal
 *         Date: 2/3/13
 */
class PageIndexProcessor {

    private static final Logger log = LoggerFactory.getLogger(PageIndexProcessor.class);

    protected static final String ONLY_DIAMOND = "only diamond";

    PageIndexProcessor() {
    }

    boolean isSameCategory(List<Tag> tagList) {
        Set<TagCategory> tagCategories = new LinkedHashSet<TagCategory>();
        boolean diamondSelected = isDiamondSelected(tagList);
        Boolean isNoindex = false;
        int stoneTagCount = 1;
        for (Tag eachTag : tagList) {
            TagCategory tagCategory = eachTag.getTagCategory();
            if (tagCategory != null) {
                if (tagCategories.contains(tagCategory)) {
                    if (tagCategory.getName().equalsIgnoreCase("Stones")) {
                    	stoneTagCount++;
                    	if (diamondSelected && !tagNameIsOnlyDiamond(eachTag)){
                    		log.debug("isSameCategory: No index : more than 1 tag in stone category including only diamond ");
                    		isNoindex = true;
                    	}else if(diamondSelected && stoneTagCount > 1){
                    		log.debug("isSameCategory: No index : more than 1 tag in stone category including only diamond ");
                    		isNoindex = true;
                    	}
                    	if(stoneTagCount > 2){
                    		log.debug("isSameCategory: No index : more than 2 tags in stone category");
                    		isNoindex = true;
                    	}
                     
                    } else {
                        log.debug("isSameCategory: same category for two tags :");
                        isNoindex = true;
                    }
                } else {
                    tagCategories.add(tagCategory);
                }
            }
        }
        return isNoindex;
    }

    boolean isNoIndexPage(List<Tag> tagList) {
        if (tagList.size() > 5) {
            log.debug("No index page as count = {}", tagList.size());
            return true;
        }
        for (Tag tag : tagList) {
            if (!tag.isActive() || tag.getIsNoIndex() == 1) {
                log.debug("No index page tag ={} is no index", tag.getName());
                return true;
            }
        }

        if (tagList != null && tagList.size() > 0) {
            if (isSameCategory(tagList)) {
                log.debug("No index page as same two tags in same category");
                return true;
            }
        }

        return false;
    }


    boolean isDiamondSelected(List<Tag> tagList) {
        for (Tag tag : tagList) {
            if (tag.isSelected() && tagNameIsOnlyDiamond(tag)) {
                return true;
            }
        }
        return false;
    }

    boolean tagNameIsOnlyDiamond(Tag eachTag) {
        return ONLY_DIAMOND.equalsIgnoreCase(eachTag.getName());
    }
}
