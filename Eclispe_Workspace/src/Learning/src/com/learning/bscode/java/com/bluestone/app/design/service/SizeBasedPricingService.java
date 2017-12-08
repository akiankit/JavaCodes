package com.bluestone.app.design.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.DesignCategory;

/**
 * @author Rahul Agrawal
 *         Date: 2/25/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SizeBasedPricingService {

    private static final Logger log = LoggerFactory.getLogger(SizeBasedPricingService.class);

    public Map<String, Map<String, Object>> getPriceForSizes(Customization customization) {
        log.debug("SizeBasedPricingService.getPriceForSizes(): CustomizationId={} , DesignId={} , DesignCode=[{}]",
                  customization.getId(), customization.getDesign().getId(), customization.getDesign().getDesignCode());
        Map<String, Map<String, Object>> weightAndPriceDetails = new HashMap<String, Map<String, Object>>();

        final DesignCategory designCategory = customization.getDesign().getDesignCategory();
        if (designCategory.isChains() || designCategory.isTanmaniyaChains() || designCategory.isRings()) {
            String[] sizes = new String[0];
            if (designCategory.isTanmaniyaChains()) {
                sizes = ApplicationProperties.getProperty("tanmaniyaChains_sizes").split(",");
            }
            if (designCategory.isChains()) {
                sizes = ApplicationProperties.getProperty("Chains_sizes").split(",");
            }
            if (designCategory.isRings()) {
                sizes = ApplicationProperties.getProperty("ring_sizes").split(",");
            }
            List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
            for (String size : sizes) {
                Map<String, Object> weightAndPriceForSpecificSize = new HashMap<String, Object>();
                weightAndPriceForSpecificSize.put("price", customization.getPrice(size));
                weightAndPriceForSpecificSize.put("weight", customizationMetalSpecifications.get(0).getMetalWeight(size));
                weightAndPriceDetails.put(size, weightAndPriceForSpecificSize);
            }
        }
        return weightAndPriceDetails;
    }
}
