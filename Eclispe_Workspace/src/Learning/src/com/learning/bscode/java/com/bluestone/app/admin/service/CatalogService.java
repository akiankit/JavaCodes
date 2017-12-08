package com.bluestone.app.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateConfigRegistry.UpdateProperty;
import com.bluestone.app.admin.service.product.config.update.listeners.StoneUpdateMetaData;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateService;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.service.CustomizationService;

/**
 * @author Rahul Agrawal
 *         Date: 2/25/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class CatalogService {

    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

    @Autowired
    private DesignDao designDao;

    @Autowired
    private CustomizationDao customizationDao;

    @Autowired
    private ProductUpdateService productUpdateService;
    
    @Autowired
   	private CustomizationService customizationService;
    
    public Map<Object, Map<String, Object>> getCatalogList() {
        Map<Object, Map<String, Object>> products = new LinkedHashMap<Object, Map<String, Object>>();
        List<Design> designs = designDao.listDesigns();
        for (Design design : designs) {
            List<Customization> customization = customizationDao.getProductByDesignId(design.getId());
            Map<String, Object> customMap = new LinkedHashMap<String, Object>();
            for (Customization custom : customization) {

                customMap.put(String.valueOf(custom.getId()), custom);
            }
            products.put(design, customMap);

        }
        return products;
    }

    public Map<String, Object> getCatalogDetails(ListFilterCriteria filterCriteria, boolean forActive) {
        int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
        Map<Object, Object> designListAndTotalCount = designDao.getCatalogListAndTotalCount(start, filterCriteria, forActive);
        String baseURLForPagination = forActive == true ? "/admin/catalog/list" : "";
        List<Design> designList = (List<Design>) designListAndTotalCount.get("list");
        Map<String, Object> dataForView = generateDataForView(designListAndTotalCount, filterCriteria.getItemsPerPage(),
                                                              filterCriteria.getP(), start, baseURLForPagination, designList.size());
        Map<String, String> searchFieldMap = new HashMap<String, String>();
        searchFieldMap.put("Id", "id");
        searchFieldMap.put("Design Name ", "designName");
        searchFieldMap.put("Design Category", "designCategory.categoryType");
        searchFieldMap.put("Design Code", "designCode");
        dataForView.put("searchFieldMap", searchFieldMap);
        return dataForView;
    }

    private Map<String, Object> generateDataForView(Map<Object, Object> listAndTotalCount, int itemsPerPage,
                                                    int currentPageNumber, int startingFrom, String baseURLForPagination, int listSize) {
        int totalCount = (Integer) listAndTotalCount.get("totalCount");
        int numberOfPages = (int) Math.ceil((double) (totalCount) / (double) (itemsPerPage));

        Map<Object, Object> paginationDataWithPrevAndNextLinks = PaginationUtil.getPagination(currentPageNumber,
                                                                                              numberOfPages, baseURLForPagination);
        Map<String, Object> itemsDetails = new HashMap<String, Object>();
        itemsDetails.put("list", listAndTotalCount.get("list"));
        itemsDetails.put("paginationData", paginationDataWithPrevAndNextLinks.get("paginationData"));
        itemsDetails.put("prevLink", paginationDataWithPrevAndNextLinks.get("prevLink"));
        itemsDetails.put("nextLink", paginationDataWithPrevAndNextLinks.get("nextLink"));
        itemsDetails.put("currentPageNumber", currentPageNumber);
        itemsDetails.put("numberOfPages", numberOfPages);
        itemsDetails.put("totalCount", totalCount);
        itemsDetails.put("showingFrom", startingFrom + 1);
        itemsDetails.put("showingTo", startingFrom + listSize);
        return itemsDetails;
    }

    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void setActiveFlag(Long designId) throws ProductUploadException {
        Design design = designDao.findAny(Design.class, designId);
        if (design == null) {
            throw new ProductUploadException("Unable to find design: " + designId);
        } else {
            if (design.getIsActive() == 0) {
                designDao.markEntityAsEnabled(design);
                int rowsUpdated = customizationDao.markAllCustomizationEnabled(designId);
                log.info("No of rows {} updated after marking all customization as active for design id {}" , rowsUpdated, designId);
            } else {
                designDao.markEntityAsDisabled(design);
                int rowsUpdated = customizationDao.markAllCustomizationDisabled(design.getId());
                log.info("No of rows {} updated after marking all customization as inactive for design id {}" , rowsUpdated, designId);
            }
            designDao.update(design);
        }
    }

    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void setCustomizationActiveFlag(Long customizationId) throws ProductUploadException{
        Customization customization = customizationDao.findAny(Customization.class, customizationId);
        if (customization == null) {
            throw new ProductUploadException("Unable to find customization for id: " + customizationId);
        } else {
            if (customization.getIsActive() == 0) {
                customizationDao.markEntityAsEnabled(customization);
                int rowsUpdated = customizationDao.markDerivedCustomizationAsActive(customizationId);
                log.info("No of rows {} updated after marking derived customization as active for customization id {}" , rowsUpdated, customizationId);
            } else {
                customizationDao.markEntityAsDisabled(customization);
                int rowsUpdated = customizationDao.markDerivedCustomizationAsInActive(customizationId);
                log.info("No of rows {} updated after marking derived customization as inActive for customization id {}" , rowsUpdated, customizationId);
            }
            
            customizationDao.update(customization);
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void setCustomizableFlag(Long designId) throws ProductUploadException {
        Design design = designDao.findAny(Design.class, designId);
        if (design == null) {
            throw new ProductUploadException("Unable to find design: " + designId);
        } else {
            if (design.getIsCustomizationSupported() == 0) {
                design.setIsCustomizationSupported((short) 1);
            } else {
                design.setIsCustomizationSupported((short) 0);
            }
            designDao.update(design);
        }
    }


    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public void updateDesign(Map<String, Object> designInput)throws ProductUploadException {
        if (designInput.get("designId") != null) {
            Design design = designDao.findAny(Design.class, Long.parseLong((String) designInput.get("designId")));
            Customization briefCustomizationByDesignId;
            if (design == null) {
            	log.error("Unable to find design: " + designInput.get("designId"));
            	throw new ProductUploadException("Unable to find design: " + designInput.get("designId"));
            } else {
            	briefCustomizationByDesignId= customizationService.getBriefCustomizationByDesignId(design.getId(), true, Customization.DEFAULT_PRIORITY);
            	UpdateProperty property = UpdateProperty.valueOf((String)designInput.get("property"));
                switch (property) {
                case Design_Name:
                case Design_Description:
                case Tag:
                	UpdateMetaData updateMetaData = new UpdateMetaData();
                    updateMetaData.setDesign(design);
                    updateMetaData.setCustomization(briefCustomizationByDesignId);
                    switch (property) {
                    	case Design_Name:
                    		updateMetaData.setUpdatedValue(designInput.get("designName"));
                    		break;
                    	case Design_Description:
                    		updateMetaData.setUpdatedValue(designInput.get("designDesc"));
                    		break;
                    	case Tag:
                    		updateMetaData.setUpdatedValue(designInput.get("tags"));
                    		break;
                    }
                   productUpdateService.update(property, updateMetaData);
                   break;
                case No_Of_Stones:
                case Stone_Setting:
                case Stone_Size:
                	StoneUpdateMetaData updateMetaDataStone = new StoneUpdateMetaData();
                	updateMetaDataStone.setDesign(design);
                	updateMetaDataStone.setCustomization(briefCustomizationByDesignId);
                	switch (property) {
                		case No_Of_Stones:
                			updateMetaDataStone.setDesignStoneSpecificationIds((String[])designInput.get("stoneId"));
                			updateMetaDataStone.setUpdatedValue(designInput.get("noOfStone"));
                			break;
                		case Stone_Setting:
                			updateMetaDataStone.setDesignStoneSpecificationIds((String[])designInput.get("stoneId"));
                			updateMetaDataStone.setUpdatedValue(designInput.get("stoneSetting"));
                			break;
                		case Stone_Size:
                			updateMetaDataStone.setDesignStoneSpecificationIds((String[])designInput.get("custStoneId"));
                			updateMetaDataStone.setUpdatedValue(designInput.get("stoneSize"));
                			break;
                	}
                	 productUpdateService.update(property, updateMetaDataStone);
                	break;
                default:
                    throw new ProductUploadException("Invalid Selection");
            } 
            }
        } else {
            log.error("Unable to find design: " + designInput.get("designId"));
            throw new ProductUploadException("Unable to find design: " + designInput.get("designId"));
        }
        
    }
    
/*    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void deleteDesign(Long designId) {
        designDao.deleteDesignTag(designId);
        designDao.remove(Design.class, designId);
    }
*/

/*
    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void deleteCustomization(Long customizationId) {
        customizationDao.remove(Customization.class, customizationId);
    }
*/
}
