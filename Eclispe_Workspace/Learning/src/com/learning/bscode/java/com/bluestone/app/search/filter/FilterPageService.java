package com.bluestone.app.search.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.QueryTimer;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DataSheetConstants;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.TagConstants;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.dao.ParentChildCategoryDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.search.tag.TagCategoryDao;
import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagCategory;
import com.googlecode.ehcache.annotations.Cacheable;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FilterPageService {
    
    private static final Logger log = LoggerFactory.getLogger(FilterPageService.class);
    
    @Autowired
    private TagDao              tagDao;
    
    @Autowired
    private TagCategoryDao      tagCategoryDao;
    
    @Autowired
    private DesignDao           designDao;

    @Autowired
    private ParentChildCategoryDao    parentChildCategoryDao;
    
    private List<Tag> getDesignIdList(List<Customization> customization) {
        List<Tag> designs = new ArrayList<Tag>();
        for (Customization custom : customization) {
            designs.addAll(custom.getDesign().getTags());
        }
        return designs;
    }
    
    
    public Set<TagCategory> generateCategoryList(List<String> selectedTagNamesList, List<Customization> searchResults, String queryString) {
    	
        log.debug("generateCategoryList(): for :" + selectedTagNamesList.toString());
        boolean search = false;
        List<Tag> selectedTagList = new ArrayList<Tag>();
        List<Tag> tags;
        List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
        List<Long> finalChildDesignCateogories = parentChildCategoryDao.getFinalChildDesignCateogories(selectedTagNamesList, childDesignCategories);
        
        if (searchResults == null) {
            // browse page flow(through ajax call)
            log.debug("generateCategoryList(): browse page flow(through ajax call)");
            tags = tagDao.getAllTagsForDesigns(selectedTagNamesList, finalChildDesignCateogories);
        } else {
            log.debug("generateCategoryList(): search page flow for query string {}", queryString);
            tags = this.getDesignIdList(searchResults);
            search = true;
        }
        
        if (!selectedTagNamesList.isEmpty()) {
            selectedTagList = tagDao.getTagListFromNames(selectedTagNamesList);
        }
        
        Set<Tag> selectedTagSet = new HashSet<Tag>();
        selectedTagSet.addAll(selectedTagList);
        if (searchResults != null) {
	        Set<Tag> allTagSet = new HashSet<Tag>();
	        allTagSet.addAll(tags);
	        for (Tag tag : allTagSet) {
				tag.setTagCount(0);
			}
        }
        if (!selectedTagNamesList.isEmpty()) {
            List<TagCategory> tagCategory = tagCategoryDao.getTagCategoryList(selectedTagNamesList);
            for (TagCategory eachTagCategory : tagCategory) {
                eachTagCategory.setSelected(true);
            }
        }
        
        Set<TagCategory> tagCategoryList = getTagCategories(selectedTagSet, tags, queryString, search, false);
        return tagCategoryList;
    }

    private Set<TagCategory> getTagCategories(Set<Tag> selectedTagSet, List<Tag> tags, String queryString, boolean search, boolean forSeo) {
        Set<TagCategory> tagCategoryList = new TreeSet<TagCategory>();
        Boolean giftPage = false;
       for(Tag tag: selectedTagSet){
    	   if(TagConstants.giftTags.containsKey(tag.getName().toLowerCase())){
    		   if("gift".equalsIgnoreCase(TagConstants.giftTags.get(tag.getName().toLowerCase()))){
    			   	giftPage = true;
    		   }
    	   }
       }
        
      for (Tag eachtag : tags) {
            TagCategory tagCategory = eachtag.getTagCategory();
            if (tagCategory != null && eachtag.getTagFilter() != null) {
                String tagCategoryNameInLowerCase = tagCategory.getName().toLowerCase();
                eachtag.setTagCount(eachtag.getTagCount() + 1);
                if (!selectedTagSet.contains(eachtag)) {
                    if(!forSeo) {
                        eachtag.setUrl(this.getFilterLink(selectedTagSet, eachtag, false, search, queryString));
                    }
                } else {
                    eachtag.setSelected(true);
                    tagCategory.setSelected(true);
                    if(!forSeo) {
                        eachtag.setUrl(this.getFilterLink(selectedTagSet, eachtag, true, search, queryString));
                    }
                }
                if (TagConstants.tagGroups.containsKey(tagCategoryNameInLowerCase)) {
                    if(tagCategory.isSelected()) {
                       tagCategoryList.add(tagCategory);
                    }
                } else if(giftPage && TagConstants.giftTagCategories.containsKey(tagCategoryNameInLowerCase)){
                	String tagCatPermission= TagConstants.giftTagCategories.get(tagCategoryNameInLowerCase);
                	if(tagCatPermission.equals("show_on_gift") || tagCatPermission.equals("show_on_all")){
                		tagCategoryList.add(tagCategory);
                	}
                }else if(!giftPage && !TagConstants.giftTagCategories.containsKey(tagCategoryNameInLowerCase)){
                	tagCategoryList.add(tagCategory);
                }else if(!giftPage && TagConstants.giftTagCategories.containsKey(tagCategoryNameInLowerCase)){
                	String tagCatPermission= TagConstants.giftTagCategories.get(tagCategoryNameInLowerCase);
                	if(tagCatPermission.equals("show_on_all")){
                		tagCategoryList.add(tagCategory);
                	}
                }
                if(TagConstants.giftTags.containsKey(eachtag.getName().toLowerCase())){
                	String tagPermission = TagConstants.giftTags.get(eachtag.getName().toLowerCase());
                	if((!giftPage && tagPermission.equals("show_on_gift")) || (giftPage && tagPermission.equals("hide_on_gift"))){
                		eachtag.setTagCount(0);
                	}
                }
            }
        }
        return tagCategoryList;
    }
    
    //@Cacheable(cacheName = "seoCategoryListCache")
    public Set<TagCategory> generateCategoryListForSeo(List<String> selectedTagNamesList) {
        log.debug("generateCategoryListForSeo(): for :" + selectedTagNamesList.toString());
        List<Tag> selectedTagList = new ArrayList<Tag>();
        List<Object[]> childDesignCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
        
        List<Long> finalChildDesignCateogories = parentChildCategoryDao.getFinalChildDesignCateogories(selectedTagNamesList, childDesignCategories);
        List<Tag> tags = tagDao.getAllTagsForDesigns(selectedTagNamesList, finalChildDesignCateogories);
        
        if (!selectedTagNamesList.isEmpty()) {
            selectedTagList = tagDao.getTagListFromNames(selectedTagNamesList);
        }
        
        Set<Tag> selectedTagSet = new HashSet<Tag>();
        selectedTagSet.addAll(selectedTagList);
        
        if (!selectedTagNamesList.isEmpty()) {
            List<TagCategory> tagCategory = tagCategoryDao.getTagCategoryList(selectedTagNamesList);
            for (TagCategory eachTagCategory : tagCategory) {
                eachTagCategory.setSelected(true);
            }
        }
        
        Set<TagCategory> tagCategoryList = getTagCategories(selectedTagSet, tags, null, false, true);
        return tagCategoryList;
    }
    
    public Set<TagCategory> getSortByCount(Set<TagCategory> tagCategories) {
        log.debug("getSortByCount(): Sort tags by count");
        for (TagCategory tagCategory : tagCategories) {
            Set<Tag> tags = tagCategory.getTags();
            List<Tag> tempTags = new ArrayList<Tag>();
            if (!tagCategory.getName().toLowerCase().contains("price")) {
                for (Tag tag : tags) {
                    Tag tempTag = null;
                    for (Tag innerTag : tags) {
                        if (innerTag.getTagCount() > 0 && !tempTags.contains(innerTag)) {
                            if (tempTag == null) {
                                tempTag = innerTag;
                            } else if (innerTag.getTagCount() > tempTag.getTagCount()) {
                                tempTag = innerTag;
                            }
                        }
                    }
                    if (tempTag != null) {
                        tempTags.add(tempTag);
                        tempTag = null;
                    }
                    
                }
                
            } else {
                tempTags.addAll(tags);
            }
            tagCategory.setSortedByCountTags(tempTags);
        }
        return tagCategories;
    }
    
    public Map<String, String> getRefineRelatedSearch(Set<TagCategory> tagCategoryList) {
        log.debug("getRefineRelatedSearch(): Related Search for browse page");
        Map<String, String> relatedSearch = new LinkedHashMap<String, String>();
        String catName = null;
        for (TagCategory tagCategory : tagCategoryList) {
            if (tagCategory.getName().equalsIgnoreCase("type") && tagCategory.isSelected()) {
                for (Tag eachTag : tagCategory.getTags()) {
                    if (eachTag.isSelected()) {
                        catName = eachTag.getName();
                    }
                }
            }
        }
        int count = 0;
        for (TagCategory tagCategory : tagCategoryList) {
            for (Tag eachTag : tagCategory.getTags()) {
                if (eachTag.isSelected()) {
                    int outerCount = 0;
                    for (TagCategory tagCategoryOuter : tagCategoryList) {
                        if (outerCount >= 2) {
                            for (TagCategory tagCategoryInner : tagCategoryList) {
                                for (Tag tag : tagCategoryInner.getTags()) {
                                    if (!tag.getName().equalsIgnoreCase(eachTag.getName())
                                            && !eachTag.getTagCategory().getName().equalsIgnoreCase("price")
                                            && !tag.getTagCategory().getName().equalsIgnoreCase("price")
                                            && tag.getTagCount() > 0 && !tag.getTagCategory().isSelected()) {
                                        String jewelleryType = "";
                                        List<String> tempList = new ArrayList<String>();
                                        if (eachTag.getTagCategory().getName().equalsIgnoreCase("type")) {
                                            jewelleryType = "";
                                        } else if (catName != null) {
                                            jewelleryType = catName;
                                        } else {
                                            jewelleryType = "Jewellery";
                                        }
                                        if (tagCategoryInner.getName().equalsIgnoreCase("type")) {
                                            continue;
                                        }
                                        if (!tag.getTagCategory().getName().equalsIgnoreCase("type")) {
                                            if (tag.getDesign().size() > 0 && eachTag.getDesign().size() > 0
                                                    && tag.getTagCategory() != null && tag.getTagCategory() != null) {
                                                String name = tag.getName() + ' ' + eachTag.getName() + ' '
                                                        + jewelleryType;
                                                tempList.add(0,tag.getName());
                                                tempList.add(1,eachTag.getName());
                                                tempList.add(2,jewelleryType);
                                                relatedSearch.put(name, this.getFilterLink(tempList));
                                                count++;
                                            }
                                        }
                                    }
                                    if (count >= 10) {
                                        break;
                                    }
                                }
                                if (count >= 10) {
                                    break;
                                }
                            }
                        }
                        if (count >= 10) {
                            break;
                        }
                        outerCount++;
                    }
                    
                }
            }
        }
        if (count == 0) {
            log.debug("getRefineRelatedSearch(): Empty refine related search");
            return null;
        }
        log.debug("getRefineRelatedSearch(): " + relatedSearch.toString());
        return relatedSearch;
    }
    
    public Map<String, String> getRelatedSearch(Long designid,String queryString) {
        log.debug("getRelatedSearch(): Related Search for product page");
        Map<String, String> relatedSearch = new LinkedHashMap<String, String>();
        Design design = designDao.find(Design.class, designid, true);
        int count = 0;
        if (design != null) {
            Set<Tag> tags = design.getTags();
            String category = design.getDesignCategory().getCategoryType();
            Tag[] firstArray = new Tag[tags.size()];
            firstArray = tags.toArray(firstArray);
            
            for (int i = 0; i < firstArray.length - 1; i++) {
                if (firstArray[i].getTagCategory() != null) {
                    if (firstArray[i].getTagCategory().getName().compareToIgnoreCase("type") != 0
                            && !firstArray[i].getTagCategory().getName().equalsIgnoreCase("price")) {
                        for (int j = i + 1; j < firstArray.length; j++) {
                            List<String> tempList = new ArrayList<String>();
                            if (firstArray[j].getTagCategory() != null) {
                                if (firstArray[j].getTagCategory().getName().compareToIgnoreCase("type") != 0
                                        && !firstArray[i].getTagCategory().getName().equalsIgnoreCase("price")
                                        && !firstArray[j].getTagCategory().getName().equalsIgnoreCase("price")) {
/*                                    if (tagDao.getCountOfDesignForATag(firstArray[i].getId()) > 0 && tagDao.getCountOfDesignForATag(firstArray[j].getId()) > 0
                                            && firstArray[i].getTagCategory() != null
                                            && firstArray[j].getTagCategory() != null) {
*/                                        
                                        
                                        String temp = firstArray[i].getName() + " " + firstArray[j].getName() + " "
                                                + category;
                                        tempList.add(0, firstArray[i].getName());
                                        tempList.add(1, firstArray[j].getName());
                                        tempList.add(2, category);
                                        relatedSearch.put(temp, this.getFilterLink(tempList)+queryString);
                                        count++;
//                                    }
                                }
                            }
                            if (count == 10) {
                                break;
                            }
                        }
                    }
                }
                if (count == 10) {
                    break;
                }
            }
        }
        log.debug("getRelatedSearch(): " + relatedSearch.toString());
        return relatedSearch;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<Tag> getTagListOrderedByUrlPriority(List<String> tagList) {
        log.debug("getTagListOrderedByUrlPriority(): for tags ");
        Set<Tag> tagSet = new TreeSet<Tag>();
        if (tagList != null) {
            List<Tag> list = tagDao.getTagListFromNames(tagList);
            for (Tag tag : list) {
                 tagSet.add(tag);
            }
        }
        return tagSet;
    }
    
    public String getFilterLink(List<String> tagList) {
        String output = "";
        Set<Tag> tagSet = new TreeSet<Tag>();
        if (!tagList.isEmpty()) {
            output = Constants.JEWELLERY_BASE_PATH + "/";
            tagSet = this.getTagListOrderedByUrlPriority(tagList);
        } else {
            output = Constants.JEWELLERY_BASE_PATH;
        }
        output = output.concat(LinkUtils.concateTags(tagSet));
        // log.debug("getFilterLink() For String array Link==="+output+".html");
        return output.concat(".html");
    }
    
    public String getFilterLink(Set<Tag> tags) {
        String output = "";
        if (!tags.isEmpty()) {
            output = Constants.JEWELLERY_BASE_PATH + "/";
        } else {
            output = Constants.JEWELLERY_BASE_PATH;
        }
        output = output.concat(LinkUtils.concateTags(tags));
        // log.debug("getFilterLink() For tag set Link==="+output+".html");
        return output.concat(".html");
    }
    
    public String getSearchPageFilterLink(Set<Tag> tags, String queryString) {
        String output = "";
        String tag = "";
        output = "search";
        tag = LinkUtils.concateTags(tags);
        if (tag.length() > 1) {
            tag = "tags=" + tag;
        }
        output = output.concat(LinkUtils.findAndReplaceTagQuery(tag, queryString));
        log.debug("getSearchPageFilterLink() For tag set and query string Link===" + output);
        return output;
    }
    
    /*public String getFilterLink(String tags) {
        return this.getFilterLink(LinkUtils.splitTags(tags));
    }*/
    
    public String getFilterLink(Set<Tag> tags, Tag tag, boolean isSearch, boolean searchPage, String queryString) {
        //List<Tag> newList = new ArrayList<Tag>(tags);
        Set<Tag> tagSet = new TreeSet<Tag>(tags);
        if (!isSearch) {
            tagSet.add(tag);
        } else {
            tagSet.remove(tag);
        }
        //tagSet.addAll(newList);
        String output;
        if (!searchPage) {
            output = this.getFilterLink(tagSet);
            output =output.concat(queryString);
        } else {
            output = this.getSearchPageFilterLink(tagSet, queryString);
            if(!output.contains("search?tags")){
            	output = output.replace("searchtags", "search?tags");
            }
        }
        
        return (output);
    }
    
    public Map<String, String> getBreadCrumbs(Set<Tag> tagSet,String urlString) {
        Map<String, String> breadCrumbs = new LinkedHashMap<String, String>();
        log.debug("getBreadCrumbs(): for browse page");
        boolean last = true;
        if (tagSet != null && tagSet.size() > 0) {
            Set<Tag> tempTagSet = new TreeSet<Tag>(tagSet);
            Set<Tag> revTagSet = new TreeSet<Tag>(tagSet);
            revTagSet = ((TreeSet<Tag>) revTagSet).descendingSet();
            
            for (Tag tag : revTagSet) {
                String title = "";
                String url = Constants.JEWELLERY_BASE_PATH + "/";
                boolean isCatagoryExists = false;
                for (Tag eachtag : tempTagSet) {
                    title = title.concat(LinkUtils.getFormattedTagName(eachtag.getName()) + " ");
                    if (DataSheetConstants.categories.get((eachtag.getName().replace("s", "")).toLowerCase()) != null) {
                        isCatagoryExists = true;
                    }
                }
                if (!isCatagoryExists) {
                    title = title.concat("Jewellery");
                }
                title = title.replace("Tanmaniya","Mangalsutra/Tanmaniya");
                title = title.replace("Yellow Gold","Gold");
                url = url.concat(LinkUtils.concateTags(tempTagSet));
                if (last) {
                    breadCrumbs.put(title, "last");
                    last = false;
                } else {
                    breadCrumbs.put(title, url.concat(".html")+urlString);
                }
                tempTagSet.remove(tag);
            }
        } else {
            breadCrumbs.put("Refine your Jewellery Selection", "last");
        }
        breadCrumbs.put("Jewellery", Constants.JEWELLERY_BASE_PATH + ".html");
        breadCrumbs.put("Home", "");
        log.debug("getBreadCrumbs(): Map :" + breadCrumbs.toString());
        return LinkUtils.reverseHash(breadCrumbs);
        
    }
    
    public Map<String, String> getBreadCrumbs(List<String> tagList) {
        Set<Tag> tagSet = getTagListOrderedByUrlPriority(tagList);
        return this.getBreadCrumbs(tagSet,"");
    }
    
    public Map<String, String> getBreadCrumbs() {
        return this.getBreadCrumbs(new TreeSet<Tag>(),"");
    }
    
    public List<TagCategory> getAllTagCategories() {
        return tagCategoryDao.getTagCategoryAllList();
    }
    
    @Cacheable(cacheName = "filtersCache")
    public Map<String, Object> getFiltersData(List<String> taglist,String queryString) {
        Map<String, Object> filters = new HashMap<String, Object>();
        long start = QueryTimer.getCurrentTime();
        Set<TagCategory> tagCategoryList = generateCategoryList(taglist, null, queryString);
        long end = QueryTimer.getCurrentTime();
        QueryTimer.logger.debug("Total time to fetch filter category list: {}", (end - start));
        start = QueryTimer.getCurrentTime();
        filters.put("tagCategoryList", getSortByCount(tagCategoryList));
        if (QueryTimer.isOn()) {
            end = QueryTimer.getCurrentTime();
            QueryTimer.logger.debug("Total time to fetch filter sort by count list: {}", (end-start));
            //start = System.currentTimeMillis();
        }
        filters.put("refineRelatedSearch", "false");
        /*Map<String, String> refineRelatedSearch = null;
        filterPageService.getRefineRelatedSearch(tagCategoryList);
        end = System.currentTimeMillis();
        QueryTimer.logger.warn("Total time to fetch filter refined related search: {}", (end-start));
        
        if(refineRelatedSearch == null){
            filters.put("refineRelatedSearch", "false");
        }*/
        /*}else{
            filters.put("refineRelatedSearch", refineRelatedSearch);
        }*/
        filters.put("selected", "true");
        return filters;
    }
    
}
