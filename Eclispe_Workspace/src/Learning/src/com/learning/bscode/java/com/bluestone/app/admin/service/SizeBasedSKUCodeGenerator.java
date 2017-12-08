package com.bluestone.app.admin.service;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.DesignCategory;

/**
 * @author Rahul Agrawal
 *         Date: 2/5/13
 */

/**
 * This class is for generating the sku codes for all the sizes that are possible for a given customization.
 * We use this for pushing the item for all possible sizes to Uniware.
 */
class SizeBasedSKUCodeGenerator {

    private static final Logger log = LoggerFactory.getLogger(SizeBasedSKUCodeGenerator.class);

    private static final String BANGLES = "bangle_sizes";
    private static final String TANMANIYACHAINS = "tanmaniyaChains_sizes";
    private static final String CHAINS = "Chains_sizes";
    private static final String RINGS = "ring_sizes";

    private static final String[] POSSIBLE_RING_SIZES = getALLSizesFor(RINGS);
    private static final String[] POSSIBLE_BANGLE_SIZES = getALLSizesFor(BANGLES);
    private static final String[] POSSIBLE_TANMANIYACHAINS_SIZES = getALLSizesFor(TANMANIYACHAINS);
    private static final String[] POSSIBLE_CHAINS_SIZES = getALLSizesFor(CHAINS);


    static List<String> execute(Customization customization) {
        List<String> skuList = new LinkedList<String>();
        final DesignCategory designCategory = customization.getDesign().getDesignCategory();
        String categoryType = designCategory.getCategoryType();
        log.info("SizeBasedSKUCodeGenerator.getSKuCodeList(): CategoryType=[{}] : CustomizationSkuCode=[{}] ",
                 categoryType, customization.getSkuCode());

        String[] size = new String[0];
        if (designCategory.isRings()) {
            size = POSSIBLE_RING_SIZES;
            for (int i = 0; i < size.length; i++) {
                skuList.add(customization.getSkuCode(size[i]));
            }

        } else if (designCategory.isBangles() || designCategory.isChains() || designCategory.isTanmaniyaChains()) {
           //String[] sizes = new String[0];
            if (designCategory.isBangles()) {
                size = POSSIBLE_BANGLE_SIZES;
            }
            if (designCategory.isTanmaniyaChains()) {
                size = POSSIBLE_TANMANIYACHAINS_SIZES;
            }
            if (designCategory.isChains()) {
                size = POSSIBLE_CHAINS_SIZES;
            }
            for (int i = 0; i < size.length; i++) {
                skuList.add(customization.getSkuCode(size[i]));
            }
        } else {
            log.info("Category Type did not match from Rings, Bangles or Chains. No Size dependency for Category=[{}]", categoryType);
            skuList.add(customization.getSkuCode());
        }
        //@todo : Rahul Agrawal :  ensure that we return an immutable list and don't generate it each time.
        log.debug("Sizes = {}", skuList);
        return skuList;
    }

    private static String[] getALLSizesFor(final String variableName) {
        log.info("SizeBasedSKUCodeGenerator.getALLSizesFor():[{}]", variableName);
        final String property = ApplicationProperties.getProperty(variableName);
        log.info("[{}]", property);
        return property.split(",");
    }
}
