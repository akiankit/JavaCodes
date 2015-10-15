package com.bluestone.app.admin.service;

import java.util.List;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.DesignConstants;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;

/**
 * @author Rahul Agrawal
 *         Date: 3/13/13
 */
public class SkuCodeFactory {

    private static final Logger log = LoggerFactory.getLogger(SkuCodeFactory.class);

    public static String getSkuCodeWithSize(DesignCategory designCategory, final String baseSkuCode, String size) {
        String skuCode = baseSkuCode;
        String categoryType = designCategory.getCategoryType();
        log.trace("SkuCodeFactory.getSkuCodeWithSize(): DesignCategory=[{}] BaseSku[{}] , Size=[{}]", categoryType, baseSkuCode, size);
        if (designCategory.isRings()) {
            if (Integer.parseInt(size) < 10) {
                log.trace("Customization.getSkuCode(): pre appending size={} with '0'", size);
                size = "0" + size;
            }
            skuCode = skuCode.substring(0, skuCode.length() - 2) + size;
        } else if (designCategory.isTanmaniyaChains() || designCategory.isChains()) {
            skuCode = skuCode.substring(0, skuCode.length() - 2) + size;
        } else if (designCategory.isBangles()) {
            if (StringUtils.isNotBlank(size)) {
                String[] spaceSplit = size.split("\\s+");
                String[] dashSplit = spaceSplit[1].split("/");
                String bangleSize = null;
                if (Integer.parseInt(dashSplit[0]) < 10) {
                    bangleSize = "0" + dashSplit[0];
                } else {
                    bangleSize = dashSplit[0];
                }
                skuCode = skuCode.substring(0, skuCode.length() - 2) + bangleSize;
            }
        }
        log.debug("SkuCodeFactory.getSkuCodeWithSize():CategoryType=[{}] Size=[{}] Sku=[{}]", categoryType, size, skuCode);
        return skuCode;
    }


    public static String getSKuCode(final Customization customization) {
        log.info("SkuCodeFactory.getSKuCode() for customizationSKU={}", customization.getSkuCode());
        String skuCode = "AAA18STF1STF2STF3_ABCD00";
        // Metal Codes - AAA, Stone Families - STF1 - STF2 - STF3, all metals have same purity

        int purity = 18;
        List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
        if (customizationMetalSpecifications != null) {
            for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
                MetalSpecification metalSpecification = customizationMetalSpecification.getMetalSpecification();
                skuCode = StringUtils.replaceOnce(skuCode, "A", getMetalCode(metalSpecification.getColor().toLowerCase()
                                                                             + " " + metalSpecification.getType().toLowerCase()));
                purity = metalSpecification.getPurity();
            }
            skuCode = StringUtils.replaceOnce(skuCode, "18", purity + "");
            log.info("CustomizationFactory.getSKuCode(): Derived SKU={}", skuCode);
        }

        List<CustomizationStoneSpecification> customizationStoneSpecifications = customization.getCustomizationStoneSpecification();
        if (customizationStoneSpecifications != null) {
            for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
                short family = customizationStoneSpecification.getDesignStoneSpecification().getFamily();
                StoneSpecification stoneSpecification = customizationStoneSpecification.getStoneSpecification();
                if (family == 1) {
                    skuCode = StringUtils.replaceOnce(skuCode, "STF1", getStoneCode(stoneSpecification));
                } else if (family == 2) {
                    skuCode = StringUtils.replaceOnce(skuCode, "STF2", getStoneCode(stoneSpecification));
                } else if (family == 3) {
                    skuCode = StringUtils.replaceOnce(skuCode, "STF3", getStoneCode(stoneSpecification));
                }
            }
            skuCode = StringUtils.replaceOnce(skuCode, "STF1", "XXXX");
            skuCode = StringUtils.replaceOnce(skuCode, "STF2", "XXXX");
            skuCode = StringUtils.replaceOnce(skuCode, "STF3", "XXXX");
        }
        skuCode = customization.getDesign().getDesignCode() + "_" + skuCode;
        log.info("SkuCodeFactory.getSKuCode() returned SkuCode=[{}]", skuCode);
        return new String(skuCode);
    }

    @VisibleForTesting
    static String getStoneCode(final StoneSpecification stoneSpecification) {
        String stoneCode = "XXXX";
        String stoneType = stoneSpecification.getStoneType();
        if (StringUtils.isBlank(stoneType)) {
            return stoneCode;
        }
        if (stoneType.toLowerCase().contains("diamond")) {
            stoneCode = DesignConstants.stoneCodes.get("diamond_"
                                                       + stoneSpecification.getClarity()
                                                       + "_"
                                                       + stoneSpecification.getColor());
        } else {
            stoneCode = DesignConstants.stoneCodes.get(stoneType.toLowerCase());
        }
        stoneCode = stoneCode == null ? "XXXX" : stoneCode;
        log.debug("SkuCodeFactory.getStoneCode(): gave=[{}]", stoneCode);
        return stoneCode;
    }

    @VisibleForTesting
    static String getMetalCode(String metalType) {
        log.debug("SkuCodeFactory.getMetalCode(): [{}]", metalType);
        String metalCode = "A";// Default Character for Metal Code

        if (StringUtils.isBlank(metalType)) {
            return metalCode;
        } else {
            metalCode = DesignConstants.metalCodes.get(metalType.trim());
            metalCode = metalCode == null ? "A" : metalCode;
        }
        return metalCode;
    }


}
