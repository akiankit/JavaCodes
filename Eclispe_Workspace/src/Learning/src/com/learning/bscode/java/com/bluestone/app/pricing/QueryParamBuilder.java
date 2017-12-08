package com.bluestone.app.pricing;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;

/**
 * @author Rahul Agrawal
 *         Date: 2/13/13
 */
class QueryParamBuilder {

    private QueryParamBuilder() {
    }

    private static final Logger log = LoggerFactory.getLogger(QueryParamBuilder.class);

    static void addStoneQueryParamForWeight(StoneSpecification stoneSpecification, Map<String, Object> queryParams) {
        queryParams.put("stonetype0", stoneSpecification.getStoneType());
        queryParams.put("stonenumber0", 1);
        queryParams.put("stonesize0", stoneSpecification.getSize());
        queryParams.put("stonecut0", stoneSpecification.getShape());
    }

    static void addStoneQueryParamsForPrice(Customization customization, Map<String, Object> queryParams) {
        log.debug("QueryParamBuilder.addStoneQueryParams()");
        int stoneFamily = 0;
        for (CustomizationStoneSpecification eachSpec : customization.getCustomizationStoneSpecification()) {

            queryParams.put("stonetype" + stoneFamily, eachSpec.getStoneSpecification().getStoneType());
            queryParams.put("stonenumber" + stoneFamily, eachSpec.getDesignStoneSpecification().getNoOfStones());
            queryParams.put("stonesize" + stoneFamily, eachSpec.getStoneSpecification().getSize());
            queryParams.put("stonecut" + stoneFamily, eachSpec.getStoneSpecification().getShape());
            queryParams.put("stonecolor" + stoneFamily, eachSpec.getStoneSpecification().getColor());
            queryParams.put("stoneclarity" + stoneFamily, eachSpec.getStoneSpecification().getClarity());
            stoneFamily++;
        }
        logAddedQueryParams(queryParams, "stone");
    }

    static void addMetalQueryParamsForPrice(Customization customization, Map<String, Object> queryParams) {
        log.debug("QueryParamBuilder.addMetalQueryParams()");
        List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
        for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
            MetalSpecification metalSpecifications = customizationMetalSpecification.getMetalSpecification();
            int family = customizationMetalSpecification.getDesignMetalFamily().getFamily() - 1;
            queryParams.put(("metaltype" + family), getMetalTypeValue(metalSpecifications));
            queryParams.put(("metalpurity" + family), metalSpecifications.getPurity());
            final BigDecimal metalWeight = customizationMetalSpecification.getMetalWeight();
//            DecimalFormat decimalFormat = new DecimalFormat("#.##########"); // 10 decimal places
//            queryParams.put("metalweight" + family, decimalFormat.format(metalWeight));
            queryParams.put(("metalweight" + family), metalWeight);
        }
        logAddedQueryParams(queryParams, "metal");
    }

    private static String getMetalTypeValue(MetalSpecification metalSpecifications) {
        return metalSpecifications.getColor() + " " + metalSpecifications.getType();
    }

    static void logAddedQueryParams(Map<String, Object> queryParams, String prefix) {
        if (log.isTraceEnabled()) {
            Set<String> keys = queryParams.keySet();
            for (String key : keys) {
                if (key.startsWith(prefix)) {
                    log.trace("[{}]:[{}]", key, queryParams.get(key));
                }
            }
        }
    }
}
