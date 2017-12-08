package com.bluestone.app.design.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.search.filter.FilterPageService;
import com.googlecode.ehcache.annotations.Cacheable;

@Service
@Transactional(readOnly=true)
public class ProductPageService {

    private static final Logger log = LoggerFactory.getLogger(ProductPageService.class);
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private FilterPageService filterService;
    
    @Autowired
    private RelatedProductService relatedProductService;

    @Autowired
    private SizeBasedPricingService sizeBasedPricingService;

    @Cacheable(cacheName="productPageCache")
    public Map<String, Object> getProductPageDetails(Long designId, Customization customization, String urlQueryString, boolean relatedSearchRequired) {
        Map <String , Object> productPageDetails = new HashMap<String, Object>();
        if(relatedSearchRequired) {
            Map<String, String> relatedSearch = filterService.getRelatedSearch(designId, urlQueryString);
            productPageDetails.put("relatedSearch", relatedSearch);
        }
        /*viewData.put("supportedStones", customizationService.getSupportedStones(customization));
           viewData.put("supportedMetals", customizationService.getSupportedMetals(customization));
           viewData.put("defaultStonesAndMetals", customizationService.getDefaultStonesAndMetalClasses(customization));*/
        productPageDetails.put("isChildCategory", productService.isCustomizationCategoryChild(customization));
        productPageDetails.put("supportedMetalPurtiy", customizationService.getSupportedMetalPurity(customization));
        productPageDetails.put("supportedDiamondQuality", customizationService.getSupportedDiamondQuality(customization));
        productPageDetails.put("weightAndPriceDetails", sizeBasedPricingService.getPriceForSizes(customization));
        productPageDetails.put("allPossibleCustomizations", customizationService.getAllPossibleCustomizations(customization));
        if(customization.isActive() && customization.getDesign().isActive()) {
            productPageDetails.put("isAvailable", "true");
        } else {
            productPageDetails.put("isAvailable", "false");
        }
        productPageDetails.put("urlString", urlQueryString);
        productPageDetails.put("pageType", "product");
        List<Customization> relatedProductsByPrice = relatedProductService.getRelatedProductsByPrice(customization);
        productPageDetails.put("relatedProducts", relatedProductsByPrice);
        return productPageDetails;
    }
}
