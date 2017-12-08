package com.bluestone.app.design.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.QueryTimer;
import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.CssClassEnum;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.SearchQuerySanitizer;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.ParentChildCategoryDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.search.filter.FilterPageService;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.seo.service.BrowsePageSEOService;
import com.googlecode.ehcache.annotations.Cacheable;


@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CustomizationService {

    private static final Logger  log = LoggerFactory.getLogger(CustomizationService.class);

    @Autowired
    private CustomizationDao     customizationDao;
    
    @Autowired
    private DesignService     designService;

    @Autowired
    private FilterPageService    filterPageService;
    
    @Autowired
    private BrowsePageSEOService browsePageSEOService;

    @Autowired
    private ProductService       productService;
    
    @Autowired
    private ParentChildCategoryDao parentChildCategoryDao;
    
    @Autowired
    private PaginationService paginationService;

    private int BROWSE_PAGE_ITEMS_PER_PAGE;

    @PostConstruct
    public void init() {
        final String propertyItemsPerPage = ApplicationProperties.getProperty("browsepage.itemsPerPage");
        BROWSE_PAGE_ITEMS_PER_PAGE = Integer.parseInt(propertyItemsPerPage);
    }
    
    private boolean isSEOEnabled = true;

    public boolean isSEOEnabled() {
        return isSEOEnabled;
    }

    public void setSEOEnabled(boolean SEOEnabled) {
        isSEOEnabled = SEOEnabled;
    }
    
    public Customization getBriefCustomizationByCustomizationId(Long customizationId, boolean findAny) {
        Customization customization = null;
        if(findAny) {
            customization = customizationDao.findAny(Customization.class, customizationId);
        } else {
            customization = customizationDao.find(Customization.class,customizationId, true);
        }
        setExpectedDeliveryDate(customization);
        return customization;
    }
    
    public Customization getDetailedCustomizationByCustomizationId(Long customizationId, boolean findAny) {
        Customization customization = null;
        if(findAny) {
            customization = customizationDao.findAny(Customization.class, customizationId);
        } else {
            customization = customizationDao.find(Customization.class,customizationId, true);
        }
        setExpectedDeliveryDate(customization);
        return customization;
    }
    
    public Customization getDetailedCustomizationByDesignId(Long designId, boolean findAny, int priority) {
        Customization customization = null;
        if(findAny) {
            customization = customizationDao.getCustomizationWithoutIsActiveFilter(designId, priority);
        } else {
            customization = customizationDao.getCustomizationsByDesign(designId, priority);
        }
        setExpectedDeliveryDate(customization);
        return customization; 
    }
    
    public Customization getBriefCustomizationByDesignId(Long designId, boolean findAny, int priority) {
        Customization customization = null;
        if(findAny) {
            customization = customizationDao.getCustomizationWithoutIsActiveFilter(designId, priority);
        } else {
            customization = customizationDao.getCustomizationsByDesign(designId, priority);
        }
        setExpectedDeliveryDate(customization);
        return customization;
    }

	private void setExpectedDeliveryDate(Customization customization) {
		if(customization != null) {
			String expectedDeliveryDate = productService.getFormattedExpectedDeliveryDate(customization);
	        customization.setExpectedDeliveryDate(expectedDeliveryDate);
		}
	}

    public Map<Object, Object> getCustomizationDetailsFromSearchQuery(Integer currentPageNumber, String searchQuery, String sortBy, String urlQueryString, String[] tags) {
        int startingFrom = (currentPageNumber - 1) * BROWSE_PAGE_ITEMS_PER_PAGE;

        final List<String> cleanSearchTerms = SearchQuerySanitizer.getCleanSearchTerms(searchQuery);

        Map<String, Object> customizationsListAndTotalCount = customizationDao.getCustomizationsAndTotalCountForSearchQuery(cleanSearchTerms,
                                                                                                                            startingFrom,
                                                                                                                            BROWSE_PAGE_ITEMS_PER_PAGE,
                                                                                                                            sortBy,
                                                                                                                            tags);
        Map<Object, Object> retVal = null;
        if (customizationsListAndTotalCount != null) {
            Map<String, String> searchBreadCrumbs = new LinkedHashMap<String, String>();
            searchBreadCrumbs.put("Home", "");
            searchBreadCrumbs.put("Search results for \"" + StringUtils.join(cleanSearchTerms, ' ') + "\"", "last");
            retVal = generateDataForView(customizationsListAndTotalCount, BROWSE_PAGE_ITEMS_PER_PAGE, currentPageNumber, startingFrom,
                                         Constants.SEARCH_BASE_PATH + urlQueryString, Constants.SEARCH_BASE_PATH, searchBreadCrumbs);
        }
        return retVal;
    }

    public Map<Object, Object> getCustomizationDetails(Integer currentPageNumber,
                                                       List<String> taglist,
                                                       String sortBy,
                                                       String urlQueryString,
                                                       String tagsName,
                                                       String preferredProducts) {
        log.debug("CustomizationService.getCustomizationDetails()");
        long start = QueryTimer.getCurrentTime();
        // Fetches the customization list with total count searched according to
        // filter crtiteria
        
        String filterDataURL = "";
        String appendQueryString = "";
        if(urlQueryString != null){
        	appendQueryString = "?"+urlQueryString;
        }
        
        if (taglist.isEmpty()) {
            filterDataURL =Constants.JEWELLERY_BASE_PATH + ".html";
        } else {
            filterDataURL = Constants.JEWELLERY_BASE_PATH + "/" + tagsName + ".html"+appendQueryString;
        }
        
        List<Long> preferredProductsId = new ArrayList<Long>(); 
        if(StringUtils.isNotBlank(preferredProducts)){
        	String[] preferredProductIds = preferredProducts.split(Constants.PREFERRED_PRODUCTS_SEPRATOR);
        	for (String productId : preferredProductIds) {
                try {
                    final Long longValue = Long.valueOf(productId);
                    preferredProductsId.add(longValue);
                } catch (NumberFormatException e) {
                    log.warn("Error: CustomizationService.getCustomizationDetails():Preferred ProductId=[{}] was not a number.", productId, e.toString());
                }
            }
        }
        // load properties file to read properties defined
        // Fetch list according to tags

        int startingFrom = (currentPageNumber - 1) * BROWSE_PAGE_ITEMS_PER_PAGE;

        Set<Tag> tagSet = filterPageService.getTagListOrderedByUrlPriority(taglist);
        
        Map<String, Object> customizationListAndTotalCount = customizationDao.getCustomizationsAndTotalCount(taglist, tagSet, startingFrom, BROWSE_PAGE_ITEMS_PER_PAGE, sortBy,preferredProductsId);
        
        String newQueryString = getNewURLQueryStringForBrowsePage(urlQueryString);
        
        String baseURLForPagination = filterPageService.getFilterLink(tagSet).concat(newQueryString);
        Map<String, String> breadCrumbs;
        breadCrumbs = filterPageService.getBreadCrumbs(tagSet,appendQueryString);
        
        Map<Object, Object> dataForView = generateDataForView(customizationListAndTotalCount, BROWSE_PAGE_ITEMS_PER_PAGE, currentPageNumber,
                                                              startingFrom, baseURLForPagination, filterDataURL, breadCrumbs);
        
        long end = QueryTimer.getCurrentTime();
        if(QueryTimer.isOn()) {
            QueryTimer.logger.debug("Total time to fetch customizations/filter/breadcrumb : {}", (end-start));
        }
        
        start = QueryTimer.getCurrentTime();
        if (isSEOEnabled()) {
            Map<String, Object> seoData = browsePageSEOService.getFilterSelectionData(taglist);
            seoData.put("canonicalLink", browsePageSEOService.getCanonicalLink(filterPageService.getFilterLink(taglist)));
            dataForView.put("seoData", seoData);
        } else {
            log.info("CustomizationService.getCustomizationDetails(): SEO is disabled");
        }

        end = QueryTimer.getCurrentTime();
        if(QueryTimer.isOn()) {
           QueryTimer.logger.debug("Total time to fetch seo data : {}", (end-start));
        }
        
        return dataForView;
    }


    private Map<Object, Object> generateDataForView(Map<String, Object> customizationListAndTotalCount, int itemsPerPage, int currentPageNumber, int startingFrom, String baseURLForPagination, String filterDataURL, Map<String, String> breadCrumbs) {
        int totalCustomizationCount = (Integer) customizationListAndTotalCount.get("totalCustomizationCount");
        List<Object> customizationList = (List<Object>) customizationListAndTotalCount.get("customizationList");
        List<Object> customizationTotalList = (List<Object>) customizationListAndTotalCount.get("customizationTotalList");
        int numberOfPages = (int) Math.ceil((double) (totalCustomizationCount) / (double) (itemsPerPage));

        Map<Object, Object> paginationDataWithPrevAndNextLinks = paginationService.getPagination(currentPageNumber, numberOfPages, baseURLForPagination);

        Map<Object, Object> customizationDetails = new HashMap<Object, Object>();
        customizationDetails.put("customizationList", customizationList);
        customizationDetails.put("customizationTotalList", customizationTotalList);
        customizationDetails.put("paginationData", paginationDataWithPrevAndNextLinks.get("paginationData"));
        customizationDetails.put("prevLink", paginationDataWithPrevAndNextLinks.get("prevLink"));
        customizationDetails.put("nextLink", paginationDataWithPrevAndNextLinks.get("nextLink"));
        customizationDetails.put("currentPageNumber", currentPageNumber);
        customizationDetails.put("numberOfPages", numberOfPages);
        customizationDetails.put("totalCustomizationCount", totalCustomizationCount);
        customizationDetails.put("showingFrom", startingFrom + 1);
        customizationDetails.put("showingTo", startingFrom + customizationList.size());
        customizationDetails.put("breadCrumbs", breadCrumbs);
        customizationDetails.put("filterDataURL", filterDataURL);
        return customizationDetails;
    }

    public String getNewURLQueryStringForBrowsePage(String urlQueryString) {
        HashMap<String, String> queryStringMap = new HashMap<String, String>();
        queryStringMap.put(Constants.ID_CATEGORY, "2");
        return getNewURLQueryString(queryStringMap, urlQueryString);
    }

    private String getNewURLQueryString(Map<String, String> queryStringMap, String urlQueryString) {
        if (urlQueryString != null) {
            String[] queryParts = urlQueryString.split(Constants.URL_QUERY_STRING_DELIMITER);
            for (String queryPart : queryParts) {
                String[] paramAndValue = queryPart.split(Constants.EQUALS_SIGN);
                queryStringMap.put(paramAndValue[0], paramAndValue.length > 1 ? paramAndValue[1]:"");
            }
        }

        if (queryStringMap.containsKey(Constants.PAGE_NUMBER_PARAM)) {
            queryStringMap.remove(Constants.PAGE_NUMBER_PARAM);
        }
        String newQueryString = "";
        for (Map.Entry<String, String> entry : queryStringMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value.length() > 0) {
                newQueryString = newQueryString + Constants.URL_QUERY_STRING_DELIMITER + key + Constants.EQUALS_SIGN
                        + value;
            }
        }

        if (newQueryString.length() > 1 && newQueryString.charAt(0) == '&') {
            newQueryString = newQueryString.substring(1);
        }

        if (newQueryString.length() > 0) {
            newQueryString = Constants.QUESTION_MARK + newQueryString;
        }
        return newQueryString;
    }

    @Cacheable(cacheName="allBaseCustomizationsCache")
    public List<Customization> getAllCustomizationWithPriority(int priority) {
        return customizationDao.getAllCustomizationWithPriority(priority);
    }
    
    @Cacheable(cacheName="allBaseParentCustomizationsCache")
    public List<Customization> getAllCustomizationsofParentCategoryWithPriority(int priority) {
        return customizationDao.getAllCustomizationsofParentCategoryWithPriority(priority);
    }

    // check if 'child category' is present as a part of tags
    @Cacheable(cacheName="childTagsCache")
    public boolean isChildTagsInTags(String tags) {
        List<String> taglist = Arrays.asList(LinkUtils.splitTags(tags));
        List<Object[]> childCategories = parentChildCategoryDao.getChildDesignCategoriesIdAndName();
        for (String tag : taglist) {
            for (Object[] childCategory : childCategories) {
                String categoryName = childCategory[0].toString();
                if (!StringUtils.isBlank(categoryName) && categoryName.equalsIgnoreCase(tag)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<Integer, ArrayList<Integer>> getSupportedMetals(Customization customization) {
        if (customization.getDesign().getIsCustomizationSupported() == 0) {
            return null;
        }
        Map<Integer, ArrayList<Integer>> supportedMetalDetails = new HashMap<Integer, ArrayList<Integer>>();
        // Fetching all possible metals for customization
        for (CustomizationMetalSpecification customizationMetalSpecification : customization.getCustomizationMetalSpecification()) {
            int metalFamily = customizationMetalSpecification.getDesignMetalFamily().getFamily();
            if (!supportedMetalDetails.containsKey(metalFamily)) {
                ArrayList<Integer> metalClassList = new ArrayList<Integer>();
                String metalName = customizationMetalSpecification.getMetalSpecification().getFullname();
                String formatedMetalName = metalName.substring(6).toUpperCase().trim().replace(' ', '_');
                int metalClass = CssClassEnum.valueOf(formatedMetalName).getClassIndex();
                metalClassList.add(metalClass);
                supportedMetalDetails.put(metalFamily, metalClassList);
            } else {
                ArrayList<Integer> metalClassList = supportedMetalDetails.get(metalFamily);
                String metalName = customizationMetalSpecification.getMetalSpecification().getFullname();
                String formatedStoneName = metalName.substring(6).toUpperCase().trim().replace(' ', '_');
                int metalClass = CssClassEnum.valueOf(formatedStoneName).getClassIndex();
                if (!metalClassList.contains(metalClass)) {
                    metalClassList.add(metalClass);
                    supportedMetalDetails.put(metalFamily, metalClassList);
                }
            }
        }
        Design designForCustomization = customization.getDesign();
        List<Customization> allCustomizationListForDesign = designForCustomization.getCustomizations();
        for (Customization possibleCustomization : allCustomizationListForDesign) {
            for (CustomizationMetalSpecification customizationMetalSpecification : possibleCustomization.getCustomizationMetalSpecification()) {
                int metalFamily = customizationMetalSpecification.getDesignMetalFamily().getFamily();
                if (supportedMetalDetails.containsKey(metalFamily)) {
                    ArrayList<Integer> metalClassList = supportedMetalDetails.get(metalFamily);
                    String metalName = customizationMetalSpecification.getMetalSpecification().getFullname();
                    String formatedMetalName = metalName.substring(6).toUpperCase().trim().replace(' ', '_');
                    int metalClass = CssClassEnum.valueOf(formatedMetalName).getClassIndex();
                    // if metalClassList does not contain metalClass then add
                    // otherwise skip
                    if (!metalClassList.contains(metalClass)) {
                        metalClassList.add(metalClass);
                        supportedMetalDetails.put(metalFamily, metalClassList);
                    }
                }
            }
        }
        return supportedMetalDetails;
    }

    public Map<Integer, ArrayList<Integer>> getSupportedStones(Customization customization) {
        if (customization.getDesign().getIsCustomizationSupported() == 0) {
            return null;
        }
        Map<Integer, ArrayList<Integer>> supportedStoneDetails = new HashMap<Integer, ArrayList<Integer>>();
        for (CustomizationStoneSpecification customizationStoneSpecification : customization.getCustomizationStoneSpecification()) {
            int stoneFamily = customizationStoneSpecification.getDesignStoneSpecification().getFamily();
            if (!supportedStoneDetails.containsKey(stoneFamily)) {
                ArrayList<Integer> stoneClassList = new ArrayList<Integer>();
                String stoneType = customizationStoneSpecification.getStoneSpecification().getStoneType();
                String formatedStoneName = stoneType.toUpperCase().replace(' ', '_');
                int stoneClass = CssClassEnum.valueOf(formatedStoneName).getClassIndex();
                stoneClassList.add(stoneClass);
                supportedStoneDetails.put(stoneFamily, stoneClassList);
            } else {
                ArrayList<Integer> stoneClassList = supportedStoneDetails.get(stoneFamily);
                String stoneType = customizationStoneSpecification.getStoneSpecification().getStoneType();
                String formatedStoneName = stoneType.toUpperCase().replace(' ', '_');
                int stoneClass = CssClassEnum.valueOf(formatedStoneName).getClassIndex();
                if (!stoneClassList.contains(stoneClass)) {
                    stoneClassList.add(stoneClass);
                    supportedStoneDetails.put(stoneFamily, stoneClassList);
                }
            }
        }

        // Fetching all possible stones for customization
        Design designForCustomization = customization.getDesign();
        List<Customization> allCustomizationListForDesign = designForCustomization.getCustomizations();
        for (Customization possibleCustomization : allCustomizationListForDesign) {
            for (CustomizationStoneSpecification customizationStoneSpecification : possibleCustomization.getCustomizationStoneSpecification()) {
                int stoneFamily = customizationStoneSpecification.getDesignStoneSpecification().getFamily();
                if (supportedStoneDetails.containsKey(stoneFamily)) {
                    ArrayList<Integer> stoneClassList = supportedStoneDetails.get(stoneFamily);
                    String stoneType = customizationStoneSpecification.getStoneSpecification().getStoneType();
                    String formatedStoneName = stoneType.toUpperCase().replace(' ', '_');
                    int stoneClass = CssClassEnum.valueOf(formatedStoneName).getClassIndex();
                    if (!stoneClassList.contains(stoneClass)) {
                        stoneClassList.add(stoneClass);
                        supportedStoneDetails.put(stoneFamily, stoneClassList);
                    }
                }
            }
        }
        return supportedStoneDetails;
    }

    public Map<String, Map<Integer, Integer>> getDefaultStonesAndMetalClasses(Customization customization) {
        Map<String, Map<Integer, Integer>> metalAndStoneFamilyDetails = new HashMap<String, Map<Integer, Integer>>();
        Map<Integer, Integer> metalDetails = new HashMap<Integer, Integer>();
        Map<Integer, Integer> stoneDetails = new HashMap<Integer, Integer>();
        for (CustomizationMetalSpecification customizationMetalSpecification : customization.getCustomizationMetalSpecification()) {
            int metalFamily = customizationMetalSpecification.getDesignMetalFamily().getFamily();
            if (!metalDetails.containsKey(metalFamily)) {
                String metalName = customizationMetalSpecification.getMetalSpecification().getFullname();
                String formatedMetalName = metalName.substring(5).toUpperCase().trim().replace(' ', '_');
                int metalClass = CssClassEnum.valueOf(formatedMetalName).getClassIndex();
                metalDetails.put(metalFamily, metalClass);
            }
        }
        for (CustomizationStoneSpecification customizationStoneSpecification : customization.getCustomizationStoneSpecification()) {
            int stoneFamily = customizationStoneSpecification.getDesignStoneSpecification().getFamily();
            if (!stoneDetails.containsKey(stoneFamily)) {
                String stoneType = customizationStoneSpecification.getStoneSpecification().getStoneType();
                String formatedStoneType = stoneType.toUpperCase().trim().replace(' ', '_');
                int stoneClass = CssClassEnum.valueOf(formatedStoneType).getClassIndex();
                stoneDetails.put(stoneFamily, stoneClass);
            }
        }
        metalAndStoneFamilyDetails.put("defaultStoneDetails", stoneDetails);
        metalAndStoneFamilyDetails.put("defaultMetalDetails", metalDetails);
        return metalAndStoneFamilyDetails;
    }

    public Customization getNewCustomization(long customizationId, String[] stones, String[] metals) {
        Customization oldCustomization = getBriefCustomizationByCustomizationId(customizationId, false);
        for (int i = 0; i < metals.length; i++) {
            if (!StringUtils.isBlank(stones[i])) {
                metals[i] = LinkUtils.getStoneAndMetalImageText(Integer.parseInt(metals[i]), false);
            }
        }
        for (int i = 0; i < stones.length; i++) {
            if (!StringUtils.isBlank(stones[i])) {
                stones[i] = LinkUtils.getStoneAndMetalImageText(Integer.parseInt(stones[i]), true);
            }
        }
        Design designForCustomization = oldCustomization.getDesign();

        List<Customization> allPossibleCustomizations = designForCustomization.getCustomizations();
        for (Customization possibleCustomization : allPossibleCustomizations) {
            List<CustomizationMetalSpecification> customizationMetalSpecifications = possibleCustomization.getCustomizationMetalSpecification();
            List<CustomizationStoneSpecification> customizationStoneSpecifications = possibleCustomization.getCustomizationStoneSpecification();
            String possibleMetals[] = new String[metals.length];
            possibleMetals[0] = "";
            for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
                possibleMetals[customizationMetalSpecification.getDesignMetalFamily().getFamily()] = customizationMetalSpecification.getMetalSpecification().getFullname();
            }
            if (areArraysEqual(metals, possibleMetals)) {
                String possibleStones[] = new String[stones.length];
                possibleStones[0] = "";
                for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
                    possibleStones[customizationStoneSpecification.getDesignStoneSpecification().getFamily()] = customizationStoneSpecification.getStoneSpecification().getStoneType();
                }
                if (areArraysEqual(stones, possibleStones)) {
                    setExpectedDeliveryDate(possibleCustomization);
                    return possibleCustomization;
                }
            }
        }
        oldCustomization.setExpectedDeliveryDate(productService.getFormattedExpectedDeliveryDate(oldCustomization));
        return oldCustomization;
    }

    private boolean areArraysEqual(String[] array1, String[] array2) {
        boolean isSame = true;
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!((StringUtils.isBlank(array1[i]) && StringUtils.isBlank(array2[i])) || (array1[i].equalsIgnoreCase(array2[i])))) {
                return false;
            }
        }
        return isSame;
    }

    public Set<Integer> getSupportedMetalPurity(Customization customization){
    	Design design = customization.getDesign();
    	if (design.getIsCustomizationSupported() == 0) {
            return null;
        }
    	Set<Integer> supportedMetalPurity = new TreeSet<Integer>();
    	List<Customization> customizations = design.getCustomizations();
    	for (Customization eachCustomization : customizations) {
    	    if(eachCustomization.isActive()) {
    	        List<CustomizationMetalSpecification> metalSpecification = eachCustomization.getCustomizationMetalSpecification();
                for (CustomizationMetalSpecification customizationMetalSpecification : metalSpecification) {
                    Integer purity = customizationMetalSpecification.getMetalSpecification().getPurity();
                    if(!supportedMetalPurity.contains(purity)){
                        supportedMetalPurity.add(purity);
                    }
                }
    	    }
		}
    	return supportedMetalPurity;
    }
    
	public Set<String> getSupportedDiamondQuality(Customization customization) {
		Design design = customization.getDesign();
		if (design.getIsCustomizationSupported() == 0) {
			return null;
		}
		TreeSet<String> supportedDiamondQuality = new TreeSet<String>();
		List<Customization> customizations = design.getCustomizations();
		for (Customization eachCustomization : customizations) {
		    if(eachCustomization.isActive()) {
		        Map<String, Object> map = eachCustomization.getStoneFamiliesDetails().get("Diamond");
	            if (map != null) {
	                Map<String, Object> diamondDetails = eachCustomization.getDiamondDetails();
	                if (diamondDetails != null) {
	                    String quality = (String) diamondDetails.get("Quality");
	                    if (!supportedDiamondQuality.contains(quality)) {
	                        supportedDiamondQuality.add(quality);
	                    }
	                }
	            }
		    }
		}
		return supportedDiamondQuality;
	}
	
	/*public Customization getNewCustomizationOnMetalPurity(long customizationId, String newMetalPurity) {
		Customization customization = customizationDao.find(Customization.class, customizationId, true);
		String customizationSku = customization.getSkuCode();
		String designCode = customization.getDesign().getDesignCode();
		int designCodeSize = designCode.length();
		char[] charArray = customizationSku.toCharArray();
		charArray[designCodeSize + 4] = newMetalPurity.charAt(0);
		charArray[designCodeSize + 5] = newMetalPurity.charAt(1);
		String newCustomizationSku = new String(charArray);
		Customization newCustomization = customizationDao.getCustomizationBySku(newCustomizationSku);
		newCustomization.setExpectedDeliveryDate(productService.getFormattedExpectedDeliveryDate(newCustomization));
		return newCustomization;
	}

	public Customization getNewCustomizationOnDiamondQuality(long customizationId, String newDiamondQuality) {
		Customization customization = customizationDao.find(Customization.class, customizationId, true);
		String skuCode = customization.getSkuCode();
		String[] clarityColor = newDiamondQuality.split(" ");
		String diamondStoneCode = "diamond_".concat(clarityColor[0]).concat("_").concat(clarityColor[1]);
		newDiamondQuality = DesignConstants.stoneCodes.get(diamondStoneCode);
		int indexOfDiamond = skuCode.indexOf("DI");
		CharSequence diamondQuality = skuCode.subSequence(indexOfDiamond, indexOfDiamond+4);
		String newCustomizationSku = skuCode.replace(diamondQuality,newDiamondQuality);
		Customization newCustomization = customizationDao.getCustomizationBySku(newCustomizationSku);
		newCustomization.setExpectedDeliveryDate(productService.getFormattedExpectedDeliveryDate(newCustomization));
		return newCustomization;
	}
*/
    public Map<String, Long> getAllPossibleCustomizations(Customization customization) {
        Map<String, Long> allPossibleCustomization = new HashMap<String, Long>();
        long designId = customization.getDesign().getId();
        Design design = designService.findByPrimaryKey(designId);
        List<Customization> customizations = design.getCustomizations();
        for (Customization eachCustomization : customizations) {
            if(eachCustomization.isActive()) {
                long customizationId = eachCustomization.getId();
                List<CustomizationMetalSpecification> customizationMetalSpecificationList = eachCustomization.getCustomizationMetalSpecification();
                CustomizationMetalSpecification customizationMetalSpecification = customizationMetalSpecificationList.get(0); // as of now assuming there is only 1 metal type
                int purity = customizationMetalSpecification.getMetalSpecification().getPurity();
                
                String key = String.valueOf(purity).intern();
                List<CustomizationStoneSpecification> customizationStoneSpecificationList = eachCustomization.getCustomizationStoneSpecification();
                for (CustomizationStoneSpecification eachCustomizationStoneSpecification : customizationStoneSpecificationList) {
                    StoneSpecification stoneSpecification = eachCustomizationStoneSpecification.getStoneSpecification();
                    if(stoneSpecification.isDiamond()) {
                        String clarity = stoneSpecification.getClarity();
                        String color = stoneSpecification.getColor();
                        key = key + "_" + clarity.intern() + " " + color.intern();
                        break;
                    }
                }
                allPossibleCustomization.put(key, customizationId);
            }
        }
        return allPossibleCustomization;
    }

}
