package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.design.model.Customization;

/**
 * @author Rahul Agrawal
 *         Date: 2/5/13
 */
class UniwareItemTypeFactory {

    private static final Logger log = LoggerFactory.getLogger(UniwareItemTypeFactory.class);

    private static Map<String, String> uniwareCategioryMap = new HashMap<String, String>();

    private UniwareItemTypeFactory() {
    }

    static {
        uniwareCategioryMap.put("pendants", "PEND");
        uniwareCategioryMap.put("rings", "RING");
        uniwareCategioryMap.put("earrings", "EARR");
        uniwareCategioryMap.put("bangles", "BANG");
        uniwareCategioryMap.put("bracelets", "BRAC");
        uniwareCategioryMap.put("chains", "chains");
        uniwareCategioryMap.put("free gift", "freeg");
        uniwareCategioryMap.put("tanmaniya chains", "tanc");
        uniwareCategioryMap.put("tanmaniya", "tanmaniya");
        uniwareCategioryMap.put("solitaire ring mount", "solitaire-ring-mount");
        uniwareCategioryMap.put("solitaire pendant mount", "solitaire-pendant-mount");
    }

    static ItemFeatureBuilder itemFeatureBuilder = new ItemFeatureBuilder();

    static UniwareItemType create(Customization customization, String skuWithSize) {
        log.info("UniwareItemTypeFactory.create(): DesignId=[{}] DesignCode=[{}], CustomizationId=[{}] size based Sku=[{}]",
                 customization.getDesign().getId(), customization.getDesign().getDesignCode(), customization.getId(), skuWithSize);

        final String features = buildFeature(customization);

        UniwareItemType uniwareItemType = new UniwareItemType();
        uniwareItemType.setDescription(customization.getShortDescription());
        uniwareItemType.setCategoryCode(getUniwareCategoryCode(customization));
        uniwareItemType.setHeight((int) customization.getDesign().getHeight());
        uniwareItemType.setWidth((int) customization.getDesign().getWidth());
        uniwareItemType.setImageURL(getImageURL(customization));
        uniwareItemType.setName(customization.getDesign().getDesignName());
        uniwareItemType.setMRP(customization.getPrice());
        uniwareItemType.setItemSKU(skuWithSize);
        uniwareItemType.setFeatures(features);
        return uniwareItemType;
    }

    static String getUniwareCategoryCode(Customization customization) {
        String bluestoneCategoryCode = customization.getDesign().getDesignCategory().getCategoryType().toLowerCase();
        return uniwareCategioryMap.get(bluestoneCategoryCode);
    }

    static String getImageURL(Customization customization) {
        //@todo : Rahul Agrawal : fix this dependency on first element from the list
        return LinkUtils.getProductImageLink(customization.getImageUrls().get(0).getSizeVsUrl().get("home"));
    }

    static String buildFeature(Customization customization) {
        return itemFeatureBuilder.execute(customization);
    }
}
