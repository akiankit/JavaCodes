package com.bluestone.app.admin.service.product.config;

import java.util.LinkedList;
import java.util.List;

import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.customization.CustomizationWtChangeProcessor;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class ProductWeightUpdateService {

    private static final Logger log = LoggerFactory.getLogger(ProductWeightUpdateService.class);

    @Autowired
    private CustomizationWtChangeProcessor customizationWtChangeProcessor;

    @Autowired
    private DesignDao designdao;

    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;

    @Autowired
    private CustomizationDao customizationDao;

    @TriggersRemove(cacheName = {"browsePageProductsCache",
            "searchPageCustomizationCache", "filtersCache",
            "searchPageCustomizationCache", "seoCategoryListCache",
            "childTagsCache", "tagListByNames", "seoMinMaxPriceCache",
            "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void uploadCustomizationWeight(String sku, Double weight) throws Exception {
        log.info("ProductWeightUpdateService.uploadCustomizationWeight(): Sku=[{}] Weight[{}]", sku, weight);
        try {
            List<Customization> rowsToBeUpdated = new LinkedList<Customization>();
            try {
                Customization customizationBySku = customizationDao.getCustomizationBySku(sku);
                if (customizationBySku != null) {
                    rowsToBeUpdated.add(customizationBySku);
                    rowsToBeUpdated.addAll(customizationBySku.getDerivedCustomizations());
                    for (Customization customization : rowsToBeUpdated) {
                        customizationWtChangeProcessor.updateWeightForCustomization(weight, customization, false, true);
                    }
                } else {
                    throw new RuntimeException("Invalid Sku Code=" + sku);
                }
            } catch (Exception e) {
                log.error("Error while updating weight for sku {}= {}", sku, e.getMessage());
                throw e;
            }
            List<Customization> updateCustomizationWeight = rowsToBeUpdated;
            // pushing to uniware
            for (Customization customization : updateCustomizationWeight) {
                uniwareCreateUpdateCustomizationService.execute(customization);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private Double getPecentageValue(String weight) {
        double percentage = 1.0d;
        percentage = percentage + (Double.parseDouble(weight) / 100);
        return percentage;
    }

    @TriggersRemove(cacheName = {"browsePageProductsCache",
            "searchPageCustomizationCache", "filtersCache",
            "searchPageCustomizationCache", "seoCategoryListCache",
            "childTagsCache", "tagListByNames", "seoMinMaxPriceCache",
            "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void uploadCustomizationWeightByPecentage(String designId, String value, boolean isMetalWeightInPercent, boolean isTotalWeight)
            throws Exception {
        try {
            Design design = designdao.findAny(Design.class,
                                              Long.parseLong(designId));
            List<Customization> customizations = design.getCustomizations();
            Double valueDouble;
            for (Customization customization : customizations) {
                if (isMetalWeightInPercent) {
                    valueDouble = getPecentageValue(value);
                } else {
                    valueDouble = Double.parseDouble(value);
                }
                customizationWtChangeProcessor.updateWeightForCustomization(valueDouble, customization, isMetalWeightInPercent, isTotalWeight);
                // pushing to uniware
                uniwareCreateUpdateCustomizationService.execute(customization);
            }

        } catch (Exception e) {
            throw e;
        }
    }

}
