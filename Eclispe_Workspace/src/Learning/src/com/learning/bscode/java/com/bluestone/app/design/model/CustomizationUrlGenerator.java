package com.bluestone.app.design.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 3/18/13
 */
class CustomizationUrlGenerator {

    private static final Logger log = LoggerFactory.getLogger(CustomizationUrlGenerator.class);

    static String getUrl(final Design design) {
        log.trace("CustomizationUrlGenerator.getUrl(): Design={}", design.toShortString());
        StringBuilder sbCustomizationUrl = new StringBuilder();
        sbCustomizationUrl.append(design.getDesignCategory().getCategoryType().toLowerCase());
        sbCustomizationUrl.append("/").append((design.getDesignName().toLowerCase()).replaceAll("\\s+", "-"));
        sbCustomizationUrl.append("~");
        sbCustomizationUrl.append(design.getId());
        sbCustomizationUrl.append(".html");

        StringBuilder sb = new StringBuilder(sbCustomizationUrl.length());

        sb.append(sbCustomizationUrl.toString().replace(' ', '+').replace('\'', '-'));
        String result = sb.toString();
        log.trace("CustomizationUrlGenerator.getUrl(): DesignId=[{}] [{}]", design.getId(), result);
        return result;
    }

}
