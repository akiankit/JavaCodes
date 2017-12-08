package com.bluestone.app.admin.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.dao.FilterDao;
import com.bluestone.app.admin.model.TagFilter;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.core.util.RuleUtil;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.search.tag.TagCategoryDao;
import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagCategory;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FilterTagService {

    private static final Logger log = LoggerFactory.getLogger(FilterTagService.class);

    @Autowired
    private TagDao              tagdao;

    @Autowired
    private FilterDao           filterdao;

    @Autowired
    private TagCategoryDao      tagcategorydao;

    @Autowired
    private DesignDao           designdao;

    private enum Types {
        PRODUCT, STONE, METAL, TAG
    };

    private enum ProductTypes {
        NAME, PRICE, CATEGORY
    };

    private enum StoneTypes {
        TYPE, SHAPE, COLOR, SETTING, NOOFSTONES
    };

    private enum MetalTypes {
        TYPE, COLOR, PURITY
    };

    private enum TagTypes {
        VALUE
    };

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    private Tag checkAndCreateTag(String name, TagCategory category, int priority) {
        Tag tag = tagdao.getTag(name.toLowerCase());
        if (tag != null) {
            log.debug("FilterTagService.checkAndCreateTag(): Updating tag = {}", tag);
            return updateTag(tag, category, priority, (short) 1);
        } else {
            log.debug("FilterTagService.checkAndCreateTag():adding new tag with name= {}",name);
            return addANewTag(name.toLowerCase(), category, priority, (short) 1, true);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Tag addANewTag(String tagname, TagCategory tagCategory, int urlPriority, short isActive, boolean isDerived){
        log.debug("FilterTagService.addANewTag(): name={}",tagname);
        Tag tag = new Tag();
        tagname = tagname.toLowerCase().replace("'s "," s ");
        tag.setName(tagname);
        TagCategory tagCat = tagCategory;
        tag.setUrlPriority(urlPriority);
        tag.setTagCategory(tagCat);
        tag.setIsActive(isActive);
        tag.setDerived(isDerived);
        tagdao.create(tag);
        log.debug("FilterTagService added A NewTag={}", tag);
        return tag;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Tag  updateTag(Tag tag,TagCategory tagCategory,int urlPriority,short isActive){
        log.debug("FilterTagService.updateTag() : name = {}",tag.getName());
        TagCategory tagCat = tagCategory;
        tag.setUrlPriority(urlPriority);
        tag.setTagCategory(tagCat);
        tag.setIsActive(isActive);
        tag.setDerived(true);
        tag = tagdao.update(tag);
        log.debug("FilterTagService updated Tag={}", tag);
        return tag;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void addNewFilterRule(String category, String urlPriority, String filtername, String rule, String inputGroupName, String tagCatPriority) {
        log.debug("FilterTagService.addNewFilterRule(): Adding new rule = {} for filtername = {}", rule, filtername);
        int priority = Integer.parseInt(urlPriority);
        if (category.equalsIgnoreCase("others")) {
            int CatPriority = Integer.parseInt(tagCatPriority);
            TagCategory newTagCat = addTagCategory(inputGroupName.toLowerCase(), CatPriority);
            addNewTagFilter(checkAndCreateTag(filtername, newTagCat, priority), rule);
        } else {
            TagCategory tagCategory = this.getTagCategory(category);
            Tag tag = checkAndCreateTag(filtername, tagCategory, priority);
            addNewTagFilter(tag, rule);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public TagCategory addTagCategory(String name,int priority){
        log.debug("FilterTagService.addTagCategory() for tag category name={}",name);
        TagCategory tagCategory = new TagCategory();
        tagCategory.setName(name);
        tagCategory.setPriority(priority);
        tagcategorydao.create(tagCategory);
        return tagCategory;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void addNewTagFilter(Tag tag, String rule) {
        TagFilter filter = new TagFilter();
        filter.setIsActive((short) 1);
        filter.setRule(rule);
        filter.setTag(tag);
        filterdao.create(filter);
    }

    public Set<Tag> processTags(List<String> tags, Design design) throws ProductUploadException {
        List<Tag> tagList = tagdao.getTagListFromNames(tags);
        Set<Tag> tagSet = new TreeSet<Tag>();
        for (Tag tag : tagList) {
            tagSet.add(tag);
            tags.remove(tag.getName());
        }
        for (String tag : tags) {
            Tag addTag = addANewTag(tag, null, 0, (short) 1, false);
            //addTag.addDesign(design);
            tagSet.add(addTag);
        }
        if (tagSet.isEmpty()) {
            return null;
        } else {
            return tagSet;
        }
    }

    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateFilterRule(String id, String category, String urlPriority, String filtername, String rule, String inputGroupName, String tagCatPriority) {
        log.debug("FilterTagService.updateFilterRule(): Adding new rule = {} for filtername = {}",rule,filtername);
        int priority = Integer.parseInt(urlPriority);
        long filterId = (long) Integer.parseInt(id);
        TagFilter filter = getFilterById(filterId);
        if (filter != null) {
            if (category.equalsIgnoreCase("others")) {
                int CatPriority = Integer.parseInt(tagCatPriority);
                TagCategory newTagCat = addTagCategory(inputGroupName.toLowerCase(), CatPriority);
                updateTagFilter(filter, checkAndCreateTag(filtername, newTagCat, priority), rule);
            } else {
                TagCategory tagCategory = this.getTagCategory(category);
                updateTagFilter(filter, checkAndCreateTag(filtername, tagCategory, priority), rule);

            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateTagFilter(TagFilter filter,Tag tag,String rule){
        filter.setIsActive(true);
        filter.setRule(rule);
        filter.setTag(tag);
        filterdao.update(filter);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void deleteFilterRule(Long filterId) {
        TagFilter filterToBeRemoved = filterdao.findAny(TagFilter.class, filterId);
        Tag tag = filterToBeRemoved.getTag();
        tag.setDerived(true);
        Tag updatedTag = tagdao.update(tag);
        filterToBeRemoved.getTag().setTagFilter(null);
        filterdao.remove(filterToBeRemoved);

    }

    public List<TagCategory> getAllTagCategories() {
        return tagcategorydao.getTagCategoryAllList();
    }

    public TagFilter getFilterById(long filterId) {
        return filterdao.find(TagFilter.class, filterId, true);
    }

    public TagFilter getFilter(Long filterId) {
        TagFilter filter = getFilterById(filterId);
        filter.setParsedRule(RuleUtil.parseRuleToken(filter.getRule()));
        return filter;
    }

    public TagCategory getTagCategory(String tagCat) {
        long tagCatId = Long.parseLong(tagCat);
        return tagcategorydao.find(TagCategory.class, tagCatId, true);
    }

    public List<TagFilter> getFilterList() {
        return filterdao.getFilterList();
    }

    private Set<Tag> processTagSet(Set<Tag> tags, List<TagFilter> TagFilter) {
        Set<Tag> tagSet = new TreeSet<Tag>();

        List<Tag> list = new ArrayList<Tag>();
        for (TagFilter filter : TagFilter) {
            list.add(filter.getTag());
        }
        for (Tag tag : list) {
            tagSet.add(tag);
        }
        if(tags != null && tags.size()>0){
	        for (Tag tag : tags) {
	            if (!tag.isDerived()) {
	                tagSet.add(tag);
	            }
	        }
        }
        return tagSet;

    }

    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public String generateFilterTags(List<Customization> customizations) {
        List<TagFilter> rules = filterdao.getFilterList();
        int success = 0, failed = 0;
        Set<Customization> customizationSet = new HashSet<Customization>();
        if(customizations!=null)
        	customizationSet.addAll(customizations);
        for (Customization customization : customizationSet) {
            if (customization != null) {
                log.info("FilterTagService.generateFilterTags() for customizationId={}", customization.getId());
                List<TagFilter> filterList = new ArrayList<TagFilter>();
                for (TagFilter filter : rules) {
                    boolean result = false;
                    final String rule = filter.getRule();
                    log.trace("FilterTagService: Iterating over Rule=[{}]", rule);
                    try {
                        //log.info("FilterTagService.generateFilterTags(): Processing rule=[{}] for customization [id={}]", rule, customization.getId());
                        Map<String, String[]> parsedRule = RuleUtil.parseRule(rule);
                        Map<Integer, List<String>> leftValues = parseLeft(parsedRule.get("left"), customization);
                        result = RuleUtil.evaluateRule(parsedRule, leftValues);
                        log.debug("FilterTagService: CustomizationId={}: Rule=[{}] : Result={}", customization.getId(), rule, result);
                    } catch (IllegalArgumentException e) {
                        log.error("Error: FilterTagService.generateFilterTags(): [CustomizationId={}] For Rule=[{}]: Reason={}",
                                  customization.getId(), rule, Throwables.getRootCause(e).toString());
                        failed++;
                    } catch (Exception e) {
                        //log.error("Error for Customization"+customization.getId()+" For Rule :"+filter.getRule(), e);
                        log.error("Error during FilterTagService.generateFilterTags(): [CustomizationId={}] For Rule=[{}]",
                                  customization.getId(), rule, Throwables.getRootCause(e));
                    }
                    if (result) {
                        success++;
                        filterList.add(filter);
                    }

                }
                Design design = customization.getDesign();
                Set<Tag> tagSet = this.processTagSet(design.getTags(), filterList);
                log.debug("FilterTagService.generateFilterTags(): CustomizationId={} : TagSet={}", customization.getId(), tagSet);
                design.setTags(tagSet);
                Design updatedDesign = designdao.update(design);
                Set<Tag> latestTags = updatedDesign.getTags();
                StringBuilder stringBuilder = new StringBuilder("[TagName=");
                for (Tag eachTag : latestTags) {
                    stringBuilder.append(eachTag.getName()).append(", ");
                    TagCategory tagCategory = eachTag.getTagCategory();
                    if(tagCategory !=null){
                        stringBuilder.append("TagCategoryName=").append(tagCategory.getName()).append(" | ");
                    }
                }
                stringBuilder.append("]");
                log.info("FilterTagService.generateFilterTags():CustomizationId={}: DesignId={} : Tags={}", customization.getId(), design.getId(), stringBuilder.toString());
            }
        }
        return "Success=" + success + " Failed=" + failed;
    }

    private Map<Integer, List<String>> parseLeft(String[] left, Customization customization) {
        Map<Integer, List<String>> leftValues = new LinkedHashMap<Integer, List<String>>();
        for (int i = 0; i < left.length; i++) {
            if (left[i] != null) leftValues.put(i, this.parseEachLeft(left[i], customization));

        }
        return leftValues;
    }

    private List<String> parseEachLeft(String token, Customization customization) {
        String[] tokens = token.split("\\.");
        List<String> value = new ArrayList<String>();
        Types type = Types.valueOf(tokens[0].toUpperCase());

        switch (type) {
            case PRODUCT:
                ProductTypes productType = ProductTypes.valueOf(tokens[1].toUpperCase());
                switch (productType) {
                    case PRICE:
                        value.add(customization.getPrice().toString());
                        break;
                    case NAME:
                        value.add(customization.getDesign().getDesignName());
                        break;
                    case  CATEGORY:
                        value.add(customization.getDesign().getDesignCategory().getCategoryType());
                        break;
                }
                break;
            case STONE:
                String shapeCase = "";
                if (tokens[1].equalsIgnoreCase("shape") || tokens[1].equalsIgnoreCase("color")
                        || tokens[1].equalsIgnoreCase("noofstones")) {
                    shapeCase = tokens[1].toLowerCase();
                    tokens[1] = "shape";

                }

                StoneTypes stoneType = StoneTypes.valueOf(tokens[1].toUpperCase());

                List<CustomizationStoneSpecification> customizationStoneSpecifications = customization.getCustomizationStoneSpecification();
                switch (stoneType) {
                    case TYPE:
                        for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
                            value.add(customizationStoneSpecification.getStoneSpecification().getStoneType());
                        }
                        break;
                    case SHAPE:
                        for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
                            if (shapeCase.equalsIgnoreCase("shape"))
                                value.add(customizationStoneSpecification.getStoneSpecification().getShape());
                            if (shapeCase.equalsIgnoreCase("noofstones"))
                                value.add(customizationStoneSpecification.getDesignStoneSpecification().getNoOfStones() + "");
                            if (shapeCase.equalsIgnoreCase("setting")) value.add(customizationStoneSpecification.getDesignStoneSpecification().getSettingType());
                        }
                        break;
                    case COLOR:
                        for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
                            value.add(customizationStoneSpecification.getStoneSpecification().getColor());
                        }
                        break;
                }
                break;
            case METAL:
                MetalTypes metalType = MetalTypes.valueOf(tokens[1].toUpperCase());
                List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
                switch (metalType) {
                    case TYPE:
                        for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
                            value.add(customizationMetalSpecification.getMetalSpecification().getType());
                        }
                        break;
                    case COLOR:
                        for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
                            value.add(customizationMetalSpecification.getMetalSpecification().getColor());
                        }
                        break;
                    case PURITY:
                        for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
                            value.add(customizationMetalSpecification.getMetalSpecification().getPurity() + "");
                        }
                        break;
                }
                break;
            case TAG:
                TagTypes tagTypes = TagTypes.valueOf(tokens[1].toUpperCase());
                switch (tagTypes) {
                    case VALUE:
                        Set<Tag> dtags = customization.getDesign().getTags();
                        if(dtags!=null && dtags.size()>0){
                        	for (Tag tag : dtags) {
                        		value.add(tag.getName());
                        	}
                        }
                        break;

                }
                break;
        }

        return value;
    }
}
