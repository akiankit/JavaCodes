package com.bluestone.app.seo.service;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.core.util.TagConstants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.service.TagTextPriorityComparator;
import com.bluestone.app.design.service.TagURLPriorityComparator;
import com.bluestone.app.search.filter.FilterPageService;
import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagCategory;
import com.bluestone.app.search.tag.model.TagSEO;
import com.googlecode.ehcache.annotations.Cacheable;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BrowsePageSEOService {

    private static final String SEOINDEX = "seoIndex";

    private static final String PRICE_PLACEHOLDER = "__PRICE_PRODUCT_STR__";

    private static final String YEAR_PLACEHOLDER  = "__YEAR__";

    private static final Logger log = LoggerFactory.getLogger(BrowsePageSEOService.class);

    @Autowired
    private FilterPageService   filterPageService;

    @Autowired
    private CustomizationDao    customizationDao;

    @Autowired
    private TagDao              tagDao;

    private String shopName = ApplicationProperties.getProperty("shop_name");

    private final PageIndexProcessor pageIndexProcessor = new PageIndexProcessor();

    @PostConstruct
    public void postConstruct(){
        log.debug("BrowsePageSEOService.postConstruct()");
        log.info("BrowsePageSEOService.postConstruct(): ShopName={}", shopName);
    }

    private String getShopName() {
        return shopName;
    }

    private String getDate() {
        Calendar cal = Calendar.getInstance();
        String dateStr = new Integer(cal.get(Calendar.DATE)).toString();
        String month = new DateFormatSymbols().getMonths()[cal.get(Calendar.MONTH)];
        String year = new Integer(cal.get(Calendar.YEAR)).toString();
        return dateStr + "-" + month + "-" + year;
    }

    private Map<String, String> fixSEOData(Map<String, Object> seoDetails, TagSEO tagSEO, int customizationCount, int lowestPrice) {
        HashMap<String, String> fixedData = new HashMap<String, String>();
        fixedData.put("htmlPageTitle", tagSEO.getHtmlPageTitle().replaceAll(YEAR_PLACEHOLDER, DateTimeUtil.getSEOYear().toString()));
        Boolean metaDescriptionFixed = false;
        if (customizationCount > 0 && lowestPrice > 0) {
            fixedData.put("metaDescription", tagSEO.getMetaDescription().replaceAll(PRICE_PLACEHOLDER, getSubPartOfMetaDescription(customizationCount, lowestPrice)));
            metaDescriptionFixed = true;
        }
        if (!metaDescriptionFixed) {
            fixedData.put("metaDescription", tagSEO.getMetaDescription().replaceAll(PRICE_PLACEHOLDER, ""));
        }
        return fixedData;
    }

    private String getHtmlPageTitle(String filterSelectionText) {
        return "Buy " + filterSelectionText + " Online in India with Latest Designs " + DateTimeUtil.getSEOYear()
                + " | " + getShopName();
    }

    private String getMetaKeywords(String typeText, String filterSelectionText) {
        String metaKeywordsCore = "";
        metaKeywordsCore = filterSelectionText + (typeText != "Jewellery" ? ' ' + typeText : "");
        metaKeywordsCore = metaKeywordsCore.replaceAll("(?i)" + TagConstants.TAG_ONLY_DIAMOND, "Diamond");
        return filterSelectionText + ", buy " + filterSelectionText + ", buy " + metaKeywordsCore + " in India";
    }

    private String getMetaDescription(int count, int lowestPrice, String filterSelectionText) {
        return getShopName() + " - Buy " + filterSelectionText + " online in India with latest designs - "
                + getSubPartOfMetaDescription(count, lowestPrice);
    }

    private String getSubPartOfMetaDescription(int count, int lowestPrice) {
        return "Starting at price of Rs. " + NumberUtil.formatPriceIndian((double) lowestPrice, ",") + " as on "
                + getDate() + ". " + count + " product" + (count > 1 ? "s" : "")
                + " available for online shopping with free shipping, lifetime exchange, 30-day free returns.";
    }

    private String getSelectedTagsDescription() {
        return " Prices are valid across India including all major cities - Delhi, Mumbai, Chennai, Bangalore, Hyderabad, Pune and kolkata for online purchases.";

    }

    public String getCanonicalLink(String filterLink) {
        String canonicalUrl = "";
/*
        MessageContext messageContext = MessageContextFactory.getCurrentMessageContext();

        String pageNumber = (String) messageContext.get(MessageContext.PAGE_NUMBER);
*/        canonicalUrl = filterLink;
/*        if (!StringUtils.isBlank(pageNumber)) {
            canonicalUrl = canonicalUrl + "?p=" + pageNumber;
        }*/
        return canonicalUrl;
    }

    private boolean isNoIndexPage(List<Tag> tagList) {
        return pageIndexProcessor.isNoIndexPage(tagList);
    }

    private TagSEO getOverrideSEODetails(List<String> selectedTagNamesList) {
        return tagDao.getOverrideSEODetails(selectedTagNamesList);
    }

    @Cacheable(cacheName = "seoCategoryListCache")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> getFilterSelectionData(final List<String> selectedTagNamesList) {
        // ------------------------ setFilterSelectionText
        // ------------------------

        String tagSelectionText = "";
        String typeText = "";

        Boolean isTypeSelected = false;
        Boolean isMetalSelected = false;
        Boolean isStyleSelected = false;

        Boolean isStoneAvailable = false;

        Set<Tag> availableStoneDetails = null;
        Set<Tag> availableOccasionDetails = null;
        Set<Tag> availableTypeDetails = null;
        Set<Tag> availableMetalDetails = null;

        //final List<String> selectedTagNamesList = selectedTagNamesList != null ? new ArrayList<String>(selectedTagNamesList) : new ArrayList<String>();
        final List<Tag> selectedTagsList = tagDao.getTagListFromNames(selectedTagNamesList);
        List<Tag> tagListWithNonZeroTextPriority = new ArrayList<Tag>();

        Set<TagCategory> tagCategoryList = filterPageService.generateCategoryListForSeo(selectedTagNamesList);

        for (TagCategory tagCategory : tagCategoryList) {
            String categoryName = tagCategory.getName().toLowerCase();
            if (categoryName.equals(TagConstants.TAG_CATEGORY_STONES)) {
                isStoneAvailable = true;
                availableStoneDetails = tagCategory.getTags();
                removeTagsWithZeroProductCount(availableStoneDetails);
            } else if (categoryName.equals(TagConstants.TAG_CATEGORY_OCCASSION)) {
                availableOccasionDetails = tagCategory.getTags();
            } else if (categoryName.equals(TagConstants.TAG_CATEGORY_TYPE)) {
                availableTypeDetails = tagCategory.getTags();
                removeTagsWithZeroProductCount(availableTypeDetails);
            } else if (categoryName.equals(TagConstants.TAG_CATEGORY_METAL)) {
                availableMetalDetails = tagCategory.getTags();
                removeTagsWithZeroProductCount(availableMetalDetails);
            }
        }

        if (selectedTagNamesList != null && selectedTagNamesList.size() > 0) {
            tagListWithNonZeroTextPriority = removeTagsWithoutTextPriority(selectedTagsList);
            List<Object> tagSelectionTextAndTagCategoryAvailability = getTagSelectionTextAndTagCategoryAvailability(tagListWithNonZeroTextPriority);
            tagSelectionText = (String) tagSelectionTextAndTagCategoryAvailability.get(0);
            isTypeSelected = (Boolean) tagSelectionTextAndTagCategoryAvailability.get(1);
            isMetalSelected = (Boolean) tagSelectionTextAndTagCategoryAvailability.get(2);
            isStyleSelected = (Boolean) tagSelectionTextAndTagCategoryAvailability.get(3);
            typeText = (String) tagSelectionTextAndTagCategoryAvailability.get(4);
        } else {
            tagSelectionText = "Jewellery";
        }
        Map<String, Object> seoDetails = new HashMap<String, Object>();
        seoDetails.put("isStoneAvailable", isStoneAvailable);
        seoDetails.put("isMetalNotSelected", !isMetalSelected);
        seoDetails.put("isStyleNotSelected", !isStyleSelected);
        seoDetails.put("isTypeNotSelected", !isTypeSelected);
        seoDetails.put("h1PageTitle", tagSelectionText);
        seoDetails.put("htmlTitle", getHtmlPageTitle(tagSelectionText));
        Boolean noIndexPage = isNoIndexPage(selectedTagsList);
        if(noIndexPage) {
            seoDetails.put(SEOINDEX, "noindex,nofollow");
        } else {
            seoDetails.put(SEOINDEX, "index,follow");
        }

        Long[] result = customizationDao.getMinAndMaxPriceFromTags(selectedTagNamesList);
        int minPriceForSelectedTags = result[0].intValue();
        int countForSelectedTags = result[2].intValue();

        seoDetails.put("metaDescription", getMetaDescription(countForSelectedTags, minPriceForSelectedTags, tagSelectionText));
        seoDetails.put("metaKeywords", getMetaKeywords(typeText, tagSelectionText));

        seoDetails.put("tagSelectionText", WordUtils.capitalize(tagSelectionText));

        if (!noIndexPage) {
            if (!isTypeSelected) {
                setTemplateVarsIfTypeNotSelected(seoDetails, availableTypeDetails, selectedTagNamesList, tagCategoryList);
            }
            if (!isMetalSelected) {
                setTemplateVarsIfMetalNotSelected(seoDetails, selectedTagNamesList, tagCategoryList);
            }
            if (!isStyleSelected) {
                setTemplateVarsIfStyleNotSelected(seoDetails, selectedTagNamesList, tagCategoryList);
            }
            if (isStoneAvailable) {
                setTemplateVarIfStonesAvailable(seoDetails, availableStoneDetails, availableMetalDetails, selectedTagNamesList, tagCategoryList);
            }
        }


        if (selectedTagNamesList != null && selectedTagNamesList.size() > 0) {
            TagSEO tagSEO = getOverrideSEODetails(selectedTagNamesList);
            if (tagSEO != null) {
                Map<String, String> fixedSEOData = fixSEOData(seoDetails, tagSEO, countForSelectedTags, minPriceForSelectedTags);
                seoDetails.put("h1PageTitle", tagSEO.getH1PageTitle());
                seoDetails.put("htmlTitle", fixedSEOData.get("htmlPageTitle"));
                seoDetails.put("metaDescription", fixedSEOData.get("metaDescription"));
                seoDetails.put("metaKeywords", tagSEO.getMetaKeywords());
                seoDetails.put("summaryTextHeader", tagSEO.getSummaryTextHeader());
            }
        }
        return seoDetails;
    }

    private void removeTagsWithZeroProductCount(Set<Tag> tags) {
        for (Iterator<Tag> iterator = tags.iterator(); iterator.hasNext();) {
            Tag tag = iterator.next();
            if (tag.getTagCount() == 0) {
                iterator.remove();
            }
        }
    }

    private List<Tag> removeTagsWithoutTextPriority(final List<Tag> tagList) {
        List<Tag> returnList = new ArrayList<Tag>(tagList);
        for (Iterator<Tag> iterator = returnList.iterator(); iterator.hasNext();) {
            Tag tag = iterator.next();
            if (tag.getTextPriority() == null || tag.getTextPriority() == 0) {
                iterator.remove();
            }
        }
        return returnList;
    }

    private List<Object> getTagSelectionTextAndTagCategoryAvailability(List<Tag> tagList) {
        String typeText = "Jewellery";
        String genderText = "";
        String filterSelectionText = "";
        Boolean isTypeSelected = false;
        Boolean isMetalSelected = false;
        Boolean isStyleSelected = false;
        int stoneCount = 0;
        Collections.sort(tagList, new TagTextPriorityComparator());

        for (Tag tag : tagList) {
            TagCategory tagCategory = tag.getTagCategory();
            if(tagCategory != null) {
                String categoryName = tagCategory.getName().toLowerCase().trim();
                if (categoryName.equals(TagConstants.TAG_CATEGORY_STONES)) {
                    if (stoneCount > 0) {
                        filterSelectionText += ',' + LinkUtils.getFormattedTagName(tag.getName()).trim() + ' ';
                    } else {
                        filterSelectionText += LinkUtils.getFormattedTagName(tag.getName()).trim() + ' ';
                        stoneCount++;
                    }
                } else if (categoryName.equals(TagConstants.TAG_CATEGORY_GENDER)) {
                    genderText = " for " + LinkUtils.getFormattedTagName(tag.getName()).trim();
                } else if (categoryName.equals(TagConstants.TAG_CATEGORY_TYPE)) {
                    typeText = LinkUtils.getFormattedTagName(tag.getName()).trim().replace("Tanmaniya","Mangalsutra/Tanmaniya");
                    isTypeSelected = true;
                } else if (categoryName.equals(TagConstants.TAG_CATEGORY_METAL)) {
                    filterSelectionText += LinkUtils.getFormattedTagName(tag.getName()).trim().replace("Yellow Gold", "Gold") + ' ';
                    isMetalSelected = true;
                } else if (categoryName.equals(TagConstants.TAG_CATEGORY_STYLE)) {
                    filterSelectionText += LinkUtils.getFormattedTagName(tag.getName()).trim() + ' ';
                    isStyleSelected = true;
                } else {
                    filterSelectionText += LinkUtils.getFormattedTagName(tag.getName()).trim() + ' ';
                }
            }
        }

        //typeText =LinkUtils.removeDuplicateWords(typeText);
        //genderText =LinkUtils.removeDuplicateWords(genderText);
        filterSelectionText += typeText + genderText;
        filterSelectionText =LinkUtils.removeDuplicateWords(filterSelectionText);


        ArrayList<Object> returnList = new ArrayList<Object>();
        returnList.add(WordUtils.capitalize(filterSelectionText.replaceAll("(?i)" + TagConstants.TAG_ONLY_DIAMOND, Constants.DIAMOND)));
        returnList.add(isTypeSelected);
        returnList.add(isMetalSelected);
        returnList.add(isStyleSelected);
        returnList.add(typeText);
        return returnList;
    }

    private void setTemplateVarsIfTypeNotSelected(Map<String, Object> seoDetails, Set<Tag> availableTypeDetails, List<String> selectedTagNamesList, Set<TagCategory> tagCategoryList) {
        seoDetails.put("list_type_csv", getCommaSeparatedTagHrefs(availableTypeDetails));
        if (isTagAvailable(tagCategoryList, Constants.RINGS)) {

            Set<String> mergedTagNamesSetWithRings = new TreeSet<String>(selectedTagNamesList);
            mergedTagNamesSetWithRings.add(Constants.RINGS);

            List<String> mergedTagNamesList = addTagNameAndGetNewList(selectedTagNamesList, Constants.RINGS);

            Set<TagCategory> tagCategoriesAvailableWithRings = filterPageService.generateCategoryListForSeo(mergedTagNamesList);

            seoDetails.put("if_ring_available", (Boolean) true);
            seoDetails.put("filter_selection_ring_url", filterPageService.getFilterLink(mergedTagNamesList));
            seoDetails.put("is_noindex_filter_selection_ring_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesList)));


            Long[] result = customizationDao.getMinAndMaxPriceFromTags(mergedTagNamesList);
            int minPriceForSelectedTags = result[0].intValue();
            int maxPriceFromSelectedTags = result[1].intValue();
            int countForSelectedTags = result[2].intValue();
            seoDetails.put("if_num_listing", countForSelectedTags);
            if (maxPriceFromSelectedTags > 0) {
                seoDetails.put("lowest_price", minPriceForSelectedTags);
                seoDetails.put("highest_price", maxPriceFromSelectedTags);
                }
            seoDetails.put("list_of_occassions_csv", getCommaSeparatedTagsWithNonZeroCount(findTagCategory(TagConstants.TAG_CATEGORY_OCCASSION, tagCategoryList)));
            seoDetails.put("filter_selection_text_ring", WordUtils.capitalize(getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesList)).get(0).toString()));

            if (isTagAvailable(tagCategoriesAvailableWithRings, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }

            Set<Tag> tagsOfCateogryStone = getTagsOfCategory(TagConstants.TAG_CATEGORY_STONES, tagCategoriesAvailableWithRings);
            if (tagsOfCateogryStone != null && tagsOfCateogryStone.size() > 0) {
                seoDetails.put("if_stones_available", (Boolean) true);
                seoDetails.put("list_stones_with_rings_csv", getCommaSeparatedTagHrefsWithTag(Constants.RINGS, tagsOfCateogryStone));
            }

            if (isTagAvailable(tagCategoriesAvailableWithRings, TagConstants.TAG_ONLY_DIAMOND)) {
                Set<String> mergedTagSetWithRingsAndOnlyDiamonds = new TreeSet<String>(mergedTagNamesSetWithRings);
                mergedTagSetWithRingsAndOnlyDiamonds.add(TagConstants.TAG_ONLY_DIAMOND);

                Set<TagCategory> tagCategoriesAvailableWithRingsAndOnlyDiamonds = filterPageService.generateCategoryListForSeo(new ArrayList<String>(mergedTagSetWithRingsAndOnlyDiamonds));
                if (isTagAvailable(tagCategoriesAvailableWithRingsAndOnlyDiamonds, TagConstants.TAG_ENGAGEMENT))
                    seoDetails.put("if_diamond_engagement_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithRings, TagConstants.TAG_WEDDING)) {
                seoDetails.put("if_wedding_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithRings, TagConstants.TAG_COCKTAIL)) {
                seoDetails.put("if_cocktail_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithRings, TagConstants.TAG_YELLOW_GOLD)
                    || isTagAvailable(tagCategoriesAvailableWithRings, TagConstants.TAG_WHITE_GOLD)) {
                seoDetails.put("if_gold_available", (Boolean) true);
            }

        }

        if (isTagAvailable(tagCategoryList, Constants.PENDANTS)) {

            List<String> mergedTagNamesListWithPendant = addTagNameAndGetNewList(selectedTagNamesList, Constants.PENDANTS);

            Set<TagCategory> tagCategoriesAvailableWithPendants = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithPendant);

            List<Tag> tagListFromNames = tagDao.getTagListFromNames(mergedTagNamesListWithPendant);

            seoDetails.put("if_pendant_available", (Boolean) true);
            seoDetails.put("filter_selection_pendant_url", filterPageService.getFilterLink(mergedTagNamesListWithPendant));
            seoDetails.put("is_noindex_filter_selection_pendant_url", isNoIndexPage(tagListFromNames));
            seoDetails.put("num_listing", customizationDao.getCustomizationCountFromTags(mergedTagNamesListWithPendant));
            seoDetails.put("filter_selection_text_pendant", getTagSelectionTextAndTagCategoryAvailability(tagListFromNames).get(0));

            if (isTagAvailable(tagCategoriesAvailableWithPendants, TagConstants.TAG_YELLOW_GOLD)
                    || isTagAvailable(tagCategoriesAvailableWithPendants, TagConstants.TAG_WHITE_GOLD)) {
                seoDetails.put("if_gold_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithPendants, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithPendants, TagConstants.TAG_HEART)) {
                seoDetails.put("if_heart_available", (Boolean) true);
            }

        }

        if (isTagAvailable(tagCategoryList, Constants.EARRINGS)) {

            List<String> mergedTagNamesListWithEarrings = addTagNameAndGetNewList(selectedTagNamesList, Constants.EARRINGS);

            Set<TagCategory> tagCategoriesAvailableWithEarRings = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithEarrings);
            List<Tag> tagListFromNames = tagDao.getTagListFromNames(mergedTagNamesListWithEarrings);

            seoDetails.put("if_earrings_available", (Boolean) true);
            seoDetails.put("filter_selection_earring_url", filterPageService.getFilterLink(mergedTagNamesListWithEarrings));
            seoDetails.put("is_noindex_filter_selection_earring_url", isNoIndexPage(tagListFromNames));
            seoDetails.put("filter_selection_text_earrings", getTagSelectionTextAndTagCategoryAvailability(tagListFromNames).get(0));

            if (isTagAvailable(tagCategoriesAvailableWithEarRings, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithEarRings, TagConstants.TAG_STUDS)) {
                seoDetails.put("if_studs_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithEarRings, TagConstants.TAG_HOOPS)) {
                seoDetails.put("if_hoops_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithEarRings, TagConstants.TAG_YELLOW_GOLD)
                    || isTagAvailable(tagCategoriesAvailableWithEarRings, TagConstants.TAG_WHITE_GOLD)) {
                seoDetails.put("if_gold_available", (Boolean) true);
            }
        }
    }

    private void setTemplateVarsIfMetalNotSelected(Map<String, Object> seoDetails, List<String> selectedTagNamesList, Set<TagCategory> tagCategoryList) {
        seoDetails.put("if_metal_not_selected", (Boolean) true);

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_YELLOW_GOLD)) {

            List<String> mergedTagNamesListWithYellowGold = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_YELLOW_GOLD);
            List<String> mergedTagNamesListWithWhiteGold = addTagNameAndGetNewList(selectedTagNamesList , TagConstants.TAG_WHITE_GOLD);

            Set<TagCategory> tagCategoriesAvailableWithYellowGold = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithYellowGold);


            seoDetails.put("if_yellow_gold_available", (Boolean) true);
            seoDetails.put("num_listing", customizationDao.getCustomizationCountFromTags(selectedTagNamesList));
            seoDetails.put("yellow_gold_filter_selection_url", filterPageService.getFilterLink(mergedTagNamesListWithYellowGold));
            seoDetails.put("is_noindex_yellow_gold_filter_selection_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesListWithYellowGold)));


            Long[] result = customizationDao.getMinAndMaxPriceFromTags(mergedTagNamesListWithYellowGold);
            int minPriceForSelectedTags = result[0].intValue();
            int maxPriceFromSelectedTags = result[1].intValue();
            int countForSelectedTags = result[2].intValue();
            seoDetails.put("if_num_listing_yellow_gold", countForSelectedTags);
            if (maxPriceFromSelectedTags > 0) {
                seoDetails.put("lowest_price_filter_selection_yellow_gold", minPriceForSelectedTags);
                seoDetails.put("highest_price_filter_selection_yellow_gold", maxPriceFromSelectedTags);
            }

            seoDetails.put("filter_selection_text_yellow_gold", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithYellowGold)).get(0));

            if (isTagAvailable(tagCategoryList, TagConstants.TAG_WHITE_GOLD)) {
                seoDetails.put("if_white_gold_available", (Boolean) true);
                seoDetails.put("white_gold_filter_selection_url", filterPageService.getFilterLink(mergedTagNamesListWithWhiteGold));
                seoDetails.put("filter_selection_text_white_gold", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithWhiteGold)).get(0));
            }

            if (isTagAvailable(tagCategoriesAvailableWithYellowGold, Constants.RINGS)) {

                List<String> mergedTagNamesListWithYellowGoldRings = addTagNameAndGetNewList(mergedTagNamesListWithYellowGold, Constants.RINGS);
                List<String> mergedTagNamesListWithYellowGoldMen = addTagNameAndGetNewList(mergedTagNamesListWithYellowGold, TagConstants.TAG_MEN);

                Set<TagCategory> tagCategoriesAvailableWithYellowGoldRings = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithYellowGoldRings);
                seoDetails.put("if_rings_available", (Boolean) true);
                seoDetails.put("filter_selection_text_ring_yellow_gold", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithYellowGoldRings)).get(0));

                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldRings, TagConstants.TAG_WEDDING)) {
                    seoDetails.put("if_occassion_wedding_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldRings, TagConstants.TAG_MEN)) {
                    seoDetails.put("if_gender_available", (Boolean) true);
                    seoDetails.put("filter_selection_text_men_yellow_gold", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithYellowGoldMen)).get(0));
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldRings, TagConstants.TAG_ONLY_DIAMOND)) {
                    seoDetails.put("if_diamond_available", (Boolean) true);
                }
            }

            if (isTagAvailable(tagCategoriesAvailableWithYellowGold, Constants.PENDANTS)) {
                List<String> mergedTagNamesListWithYellowGoldPendants = addTagNameAndGetNewList(mergedTagNamesListWithYellowGold, Constants.PENDANTS);

                Set<TagCategory> tagCategoriesAvailableWithYellowGoldPendants = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithYellowGoldPendants);

                seoDetails.put("if_pendants_available", (Boolean) true);
                seoDetails.put("filter_selection_text_pendant_yellow_gold", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithYellowGoldPendants)).get(0));

                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldPendants, TagConstants.TAG_ONLY_DIAMOND)) {
                    seoDetails.put("if_diamond_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldPendants, TagConstants.TAG_WHITE_GOLD)) {
                    seoDetails.put("if_white_gold_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldPendants, TagConstants.TAG_18K)) {
                    seoDetails.put("if_gold_purity_available", (Boolean) true);
                    seoDetails.put("gold_purity", "18k");
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldPendants, TagConstants.TAG_HEART)) {
                    seoDetails.put("if_heart_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldPendants, TagConstants.TAG_RELIGIOUS)) {
                    seoDetails.put("if_religious_available", (Boolean) true);
                }
            }

            if (isTagAvailable(tagCategoriesAvailableWithYellowGold, Constants.EARRINGS)) {
                List<String> mergedTagNamesListWithYellowGoldEarRings = addTagNameAndGetNewList(mergedTagNamesListWithYellowGold, Constants.EARRINGS);

                Set<TagCategory> tagCategoriesAvailableWithYellowGoldEarRings = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithYellowGoldEarRings);

                seoDetails.put("if_earrings_available", (Boolean) true);
                seoDetails.put("filter_selection_text_earrings_yellow_gold", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithYellowGoldEarRings)).get(0));

                if (isTagAvailable(tagCategoriesAvailableWithYellowGold, TagConstants.TAG_ONLY_DIAMOND)) {
                    seoDetails.put("if_diamond_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldEarRings, TagConstants.TAG_HOOPS)) {
                    seoDetails.put("if_hoops_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGoldEarRings, TagConstants.TAG_STUDS)) {
                    seoDetails.put("if_studs_available", (Boolean) true);
                }
                if (isTagAvailable(tagCategoriesAvailableWithYellowGold, TagConstants.TAG_WHITE_GOLD)) {
                    seoDetails.put("if_white_gold_available", (Boolean) true);
                }
            }

        }
    }

    private List<String> addTagNameAndGetNewList(Collection<String> tagNameList, String... tagName) {
        Set<String> mergedTagNamesSet = new TreeSet<String>(tagNameList);
        for (String eachTagName : tagName) {
            mergedTagNamesSet.add(eachTagName);
        }
        return new ArrayList<String>(mergedTagNamesSet);
    }

    private void setTemplateVarsIfStyleNotSelected(Map<String, Object> seoDetails, List<String> selectedTagNamesList, Set<TagCategory> tagCategoryList) {
        seoDetails.put("if_style_not_selected", (Boolean) true);

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_HOOPS)) {

            List<String> mergedTagNamesListWithHoops = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_HOOPS);

            List<String> mergedTagNamesListWithDiamondHoops = addTagNameAndGetNewList(mergedTagNamesListWithHoops, TagConstants.TAG_ONLY_DIAMOND);

            List<String> mergedTagNamesListWithGoldHoops = addTagNameAndGetNewList(mergedTagNamesListWithHoops, TagConstants.TAG_YELLOW_GOLD);

            Set<TagCategory> tagCategoriesAvailableWithHoops = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithHoops);

            seoDetails.put("is_hoops_available", (Boolean) true);
            seoDetails.put("filter_selection_hoops_url", filterPageService.getFilterLink(mergedTagNamesListWithHoops));
            seoDetails.put("is_noindex_filter_selection_hoops_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesListWithHoops)));
            seoDetails.put("filter_selection_text_hoops", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithHoops)).get(0));
            seoDetails.put("filter_selection_hoops_listing", customizationDao.getCustomizationCountFromTags(mergedTagNamesListWithHoops));
            seoDetails.put("filter_selection_text_diamond_hoops", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithDiamondHoops)).get(0));
            seoDetails.put("filter_selection_text_gold_hoops", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithGoldHoops)).get(0));

            if (isTagAvailable(tagCategoriesAvailableWithHoops, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithHoops, TagConstants.TAG_YELLOW_GOLD)
                    || isTagAvailable(tagCategoriesAvailableWithHoops, TagConstants.TAG_WHITE_GOLD)) {
                seoDetails.put("if_yellow_gold_or_white_gold_available", (Boolean) true);
            }

        }

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_STUDS)) {

            List<String> mergedTagNamesListWithStuds = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_STUDS);

            List<String> mergedTagNamesListWithDiamondStuds = addTagNameAndGetNewList(mergedTagNamesListWithStuds, TagConstants.TAG_ONLY_DIAMOND);

            Set<TagCategory> tagCategoriesAvailableWithStuds = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithStuds);

            seoDetails.put("is_studs_available", (Boolean) true);
            seoDetails.put("filter_selection_text_studs_url", filterPageService.getFilterLink(mergedTagNamesListWithStuds));
            seoDetails.put("is_noindex_filter_selection_text_studs_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesListWithStuds)));
            seoDetails.put("filter_selection_text_studs", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithStuds)).get(0));
            seoDetails.put("filter_selection_studs_listing", customizationDao.getCustomizationCountFromTags(mergedTagNamesListWithStuds));
            seoDetails.put("filter_selection_text_diamond_studs", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithDiamondStuds)).get(0));

            if (isTagAvailable(tagCategoriesAvailableWithStuds, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }

        }

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_COCKTAIL)) {

            List<String> mergedTagNamesListWithCocktail = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_COCKTAIL);

            Set<TagCategory> tagCategoriesAvailableWithCocktail = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithCocktail);

            seoDetails.put("if_cocktail_available", (Boolean) true);
            List<Tag> tagListFromNames = tagDao.getTagListFromNames(mergedTagNamesListWithCocktail);
            seoDetails.put("filter_selection_text_cocktail", getTagSelectionTextAndTagCategoryAvailability(tagListFromNames).get(0));

            seoDetails.put("filter_selection_text_cocktail_url", filterPageService.getFilterLink(mergedTagNamesListWithCocktail));
            seoDetails.put("is_noindex_filter_selection_text_cocktail_url", isNoIndexPage(tagListFromNames));

            Long[] result = customizationDao.getMinAndMaxPriceFromTags(mergedTagNamesListWithCocktail);
            int minPriceForSelectedTags = result[0].intValue();
            int maxPriceFromSelectedTags = result[1].intValue();
            int countFromSelectedTags = result[2].intValue();
            seoDetails.put("filter_selection_cocktail_listing", countFromSelectedTags);

            if (isTagAvailable(tagCategoriesAvailableWithCocktail, Constants.RINGS)) {
                if (maxPriceFromSelectedTags > 0) {
                    seoDetails.put("lowest_price_cocktail_filter_selection", minPriceForSelectedTags);
                    seoDetails.put("highest_price_cocktail_filter_selection", maxPriceFromSelectedTags);
                    }
                }

        }

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_RELIGIOUS)) {

            List<String> mergedTagNamesListWithReligious = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_RELIGIOUS);

            List<String> mergedTagNamesListWithReligiousPendants = addTagNameAndGetNewList(mergedTagNamesListWithReligious, Constants.PENDANTS);

            Set<TagCategory> tagCategoriesAvailableWithReligious = filterPageService.generateCategoryListForSeo(new ArrayList<String>(mergedTagNamesListWithReligious));

            seoDetails.put("if_religious_available", (Boolean) true);
            seoDetails.put("filter_selection_religious_url", filterPageService.getFilterLink(mergedTagNamesListWithReligious));
            seoDetails.put("is_noindex_filter_selection_religious_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesListWithReligious)));
            seoDetails.put("filter_selection_text_religious_pendant", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithReligiousPendants)).get(0));

            if (isTagAvailable(tagCategoriesAvailableWithReligious, TagConstants.TAG_OM)) {
                seoDetails.put("if_om_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithReligious, TagConstants.TAG_LAKSHMI)) {
                seoDetails.put("if_lakshmi_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoriesAvailableWithReligious, TagConstants.TAG_GANESHA)) {
                seoDetails.put("if_ganesha_available", (Boolean) true);
            }
        }

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_DROPS)) {

            List<String> mergedTagNamesListWithDrops = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_DROPS);
            List<String> mergedTagNamesListWithDiamondDrops = addTagNameAndGetNewList(mergedTagNamesListWithDrops, TagConstants.TAG_ONLY_DIAMOND);

            Set<TagCategory> tagCategoriesAvailableWithDrops = filterPageService.generateCategoryListForSeo(mergedTagNamesListWithDrops);

            seoDetails.put("if_drops_available", (Boolean) true);
            seoDetails.put("filter_selection_text_drops", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithDrops)).get(0));
            seoDetails.put("filter_selection_drops_listing", customizationDao.getCustomizationCountFromTags(mergedTagNamesListWithDrops));
            seoDetails.put("filter_selection_drops_url", filterPageService.getFilterLink(mergedTagNamesListWithDrops));
            seoDetails.put("is_noindex_filter_selection_drops_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesListWithDrops)));

            if (isTagAvailable(tagCategoriesAvailableWithDrops, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
                seoDetails.put("filter_selection_text_diamond_drops", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesListWithDiamondDrops)).get(0));
            }

        }

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_ALPHABET_JEWELLERY)) {

            List<String> mergedTagNamesSetListAlphabetJewelleryPendants = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_ALPHABET_JEWELLERY, Constants.PENDANTS);

            Set<TagCategory> tagCategoriesAvailableWithAlphabetJewellery = filterPageService.generateCategoryListForSeo(mergedTagNamesSetListAlphabetJewelleryPendants);

            seoDetails.put("if_alphabet_available", (Boolean) true);
            seoDetails.put("filter_selection_alphabet_pendant_url", filterPageService.getFilterLink(mergedTagNamesSetListAlphabetJewelleryPendants));
            seoDetails.put("is_noindex_filter_selection_alphabet_pendant_url", isNoIndexPage(tagDao.getTagListFromNames(mergedTagNamesSetListAlphabetJewelleryPendants)));
            seoDetails.put("filter_selection_text_alphabet_pendant", getTagSelectionTextAndTagCategoryAvailability(tagDao.getTagListFromNames(mergedTagNamesSetListAlphabetJewelleryPendants)).get(0));

            if (isTagAvailable(tagCategoriesAvailableWithAlphabetJewellery, TagConstants.TAG_18K)) {
                seoDetails.put("if_gold_purity_available", (Boolean) true);
            }
            if (isTagAvailable(tagCategoriesAvailableWithAlphabetJewellery, TagConstants.TAG_WHITE_GOLD)) {
                seoDetails.put("if_white_gold_available", (Boolean) true);
            }
            if (isTagAvailable(tagCategoriesAvailableWithAlphabetJewellery, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }
        }
    }

    private void setTemplateVarIfStonesAvailable(Map<String, Object> seoDetails, Set<Tag> availableStoneDetails, Set<Tag> availableMetalDetails, List<String> selectedTagNamesList, Set<TagCategory> tagCategoryList) {
        seoDetails.put("if_stones_available", (Boolean) true);
        String availableStonesCommaSeperatedTagNames = getCommaSeparatedTagNames(availableStoneDetails);
        seoDetails.put("list_stones_csv", availableStonesCommaSeperatedTagNames);

        if (isTagAvailable(tagCategoryList, TagConstants.TAG_ONLY_DIAMOND)) {

            List<String> mergedTagNameListWithOnlyDiamond = addTagNameAndGetNewList(selectedTagNamesList, TagConstants.TAG_ONLY_DIAMOND);

            seoDetails.put("is_diamond_available", (Boolean) true);
            seoDetails.put("filter_selection_diamond", filterPageService.getFilterLink(mergedTagNameListWithOnlyDiamond));
            List<Tag> tagListFromNames = tagDao.getTagListFromNames(mergedTagNameListWithOnlyDiamond);
            seoDetails.put("is_noindex_filter_selection_diamond", isNoIndexPage(tagListFromNames));


            Long[] result = customizationDao.getMinAndMaxPriceFromTags(mergedTagNameListWithOnlyDiamond);
            int minPriceForSelectedTags = result[0].intValue();
            int maxPriceFromSelectedTags = result[1].intValue();
            int count = result[2].intValue();
            seoDetails.put("num_listing_diamond", count);

            if (maxPriceFromSelectedTags > 0) {
                seoDetails.put("lowest_price_filter_selection_diamond", minPriceForSelectedTags);
                seoDetails.put("highest_price_filter_selection_diamond", maxPriceFromSelectedTags);
            }
            seoDetails.put("filter_selection_text_diamond", WordUtils.capitalize(getTagSelectionTextAndTagCategoryAvailability(tagListFromNames).get(0).toString()));
        }

        if (selectedTagNamesList.contains(Constants.RINGS)) {
            seoDetails.put("if_rings_selected", (Boolean) true);
            if (isTagAvailable(tagCategoryList, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            }
            if (isTagAvailable(tagCategoryList, TagConstants.TAG_RUBY)) {
                seoDetails.put("if_ruby_available", (Boolean) true);
            }

            if (isTagAvailable(tagCategoryList, TagConstants.TAG_EMERALD)) {
                seoDetails.put("if_emerald_available", (Boolean) true);
            }
            if (isTagAvailable(tagCategoryList, TagConstants.TAG_SAPPHIRE)) {
                seoDetails.put("if_sapphire_available", (Boolean) true);
            }
            if (isTagAvailable(tagCategoryList, TagConstants.TAG_AMETHYS)) {
                seoDetails.put("if_amethys_available", (Boolean) true);
            }
            if (isTagAvailable(tagCategoryList, TagConstants.TAG_GARNET)) {
                seoDetails.put("if_garnet_available", (Boolean) true);
            }

        }

        if (selectedTagNamesList.contains(Constants.PENDANTS)) {

            //String[] selectedTagNamesArray = Arrays.copyOf(selectedTagNamesList.toArray(), selectedTagNamesList.toArray().length, String[].class);
            seoDetails.put("if_pendants_selected", (Boolean) true);

            Long[] minAndMaxPriceFromTags = customizationDao.getMinAndMaxPriceFromTags(selectedTagNamesList);
            int minPriceForSelectedTags = minAndMaxPriceFromTags[0].intValue();
            int maxPriceFromSelectedTags = minAndMaxPriceFromTags[1].intValue();
            seoDetails.put("lowest_price_diamond_pendants", minPriceForSelectedTags);
            seoDetails.put("highest_price_filter_selection_diamond", maxPriceFromSelectedTags);

            if (isTagAvailable(tagCategoryList, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            } else {
                seoDetails.put("list_stones_csv", availableStonesCommaSeperatedTagNames);
            }
        }

        if (selectedTagNamesList.contains(Constants.EARRINGS)) {
            seoDetails.put("if_earrings_selected", (Boolean) true);
            if (isTagAvailable(tagCategoryList, TagConstants.TAG_ONLY_DIAMOND)) {
                seoDetails.put("if_diamond_available", (Boolean) true);
            } else {
                seoDetails.put("list_stones_csv", availableStonesCommaSeperatedTagNames);
            }

            if (isTagAvailable(tagCategoryList, TagConstants.TAG_18K)) {
                seoDetails.put("gold_purity", "18k");
            }
            if (availableMetalDetails != null) {
                seoDetails.put("if_metal_available", (Boolean) true);
                seoDetails.put("list_metal_csv", getCommaSeparatedTagNames(availableMetalDetails));
            }
        }

    }

    private String getCommaSeparatedTagNames(Set<Tag> tags) {
        List<String> nameList = new ArrayList<String>();
        if (tags != null) {
            for (Tag tag : tags) {
                nameList.add(tag.getName());
            }
        }
        return StringUtils.join(nameList, ",");
    }

    private String getCommaSeparatedTagHrefsWithTag(String tagName, Set<Tag> tags) {

        List<String> listOfHrefs = new ArrayList<String>();
        for (Tag tag : tags) {
            List<String> tagList = new ArrayList<String>();
            tagList.add(tagName);
            tagList.add(tag.getName());
            listOfHrefs.add(getFilterLinkWithAnchorText(tagDao.getTagListFromNames(tagList)));
        }
        return StringUtils.join(listOfHrefs, ',');
    }

    private String getFilterLinkWithAnchorText(List<Tag> tagList) {
        String url = Util.getContextPath()+"/"+filterPageService.getFilterLink(new HashSet<Tag>(tagList));
        Collections.sort(tagList, new TagURLPriorityComparator());
        String anchorText = "";
        for (Tag tag : tagList) {
            anchorText += tag.getName() + " ";
        }
        return "<a href=\"" + url+ "\" title=\"" + anchorText + "\">" + anchorText.toLowerCase().trim() + "</a>";
    }

    private Set<Tag> getTagsOfCategory(String tagCategoryName, Set<TagCategory> tagCategoryList) {
        TagCategory tagCategory = findTagCategory(tagCategoryName, tagCategoryList);
        if (tagCategory != null) {
            return tagCategory.getTags();
        }
        return null;
    }

    private TagCategory findTagCategory(String categoryName, Set<TagCategory> tagCategoryList) {
        for (TagCategory tagCategory : tagCategoryList) {
            if (tagCategory.getName().toLowerCase().equals(categoryName.toLowerCase())) {
                return tagCategory;
            }
        }
        return null;
    }

    private String getCommaSeparatedTagsWithNonZeroCount(TagCategory tagCategory) {

        List<String> tagNamesList = new ArrayList<String>();
        if (tagCategory != null) {
            for (Tag tag : tagCategory.getTags()) {
                if (tag.getTagCount() > 0) tagNamesList.add(tag.getName());
            }
        }

        return StringUtils.join(tagNamesList, ',');
    }

    private boolean isTagAvailable(Set<TagCategory> tagCategoryList, String tagName) {
        boolean result = false;
        for (TagCategory tagCategory : tagCategoryList) {
            for (Tag tag : tagCategory.getTags()) {
                if (tag.getName().toLowerCase().equals(tagName.toLowerCase()) && tag.getTagCount() > 0) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    private String getCommaSeparatedTagHrefs(Set<Tag> tags) {
        ArrayList<String> listOfLinks = new ArrayList<String>();
        if (tags != null) {
            for (Tag tag : tags) {
                if (tag.getTagCount() > 0) {
                    List<String> tagList = new ArrayList<String>();
                    tagList.add(tag.getName());
                    String filterLink = Util.getContextPath()+"/"+filterPageService.getFilterLink(tagList);
                    listOfLinks.add("<a href=\"" + filterLink + "\" title=\"" + tag.getName() + "\">"
                            + tag.getName().toLowerCase() + "</a>");
                }
            }
        }
        return StringUtils.join(listOfLinks, ',');
    }
}
