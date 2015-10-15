package com.bluestone.app.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.ProductService;
import com.bluestone.app.search.tag.model.Tag;

/**
 * @author admin
 */
@Component
public class LinkUtils {

    private static final Logger log = LoggerFactory.getLogger(LinkUtils.class);

    private static String[] cdnHosts;
    private static String[] staticCdnHosts;
    private static CustomizationService customizationService;
    private static ProductService productService;

    @PostConstruct
    public void init(){
        log.debug("LinkUtils.init()");
        Assert.notNull(customizationService, "field customizationService could be autowired.");
        Assert.notNull(productService, "field productService could be autowired.");
    }

    @Autowired
    @Required
    public void setCustomizationService(CustomizationService customizationService) {
        // hack for autowiring customizationService
        LinkUtils.customizationService = customizationService;
    }

    @Autowired
    @Required
    public void setProductService(ProductService productService) {
        // hack for autowiring cartService
        LinkUtils.productService = productService;
    }

    // Used for fetching image urls and product urls for testimonials
    public static Customization getCustomizationByDesignId(String designId) {
         Customization customization = customizationService.getBriefCustomizationByDesignId(Long.valueOf(designId),
                 false, Customization.DEFAULT_PRIORITY);
         return customization;
    }

    public static Boolean isCustomizationCategoryChild(Product product) {
         return productService.isCustomizationCategoryChild(product);
    }


    static {
        String cdnHostNames = ApplicationProperties.getProperty("cdn.product.cnames");
        cdnHosts = cdnHostNames.split(",");

        String staticCdnHostNames = ApplicationProperties.getProperty("cdn.static.cnames");
        staticCdnHosts = staticCdnHostNames.split(",");
    }


    public static String getStoneAndMetalImageText(Integer index, Boolean isStone) {
        CssClassEnum[] values = CssClassEnum.values();
        for (CssClassEnum cssClassEnum : values) {
            if (isStone == false) {
                if (cssClassEnum.getClassIndex() == index && cssClassEnum.getIsStone() == false) {
                    return cssClassEnum.getStoneOrMetalName();
                }
            } else {
                if (cssClassEnum.getClassIndex() == index && cssClassEnum.getIsStone() == true) {
                    return cssClassEnum.getStoneOrMetalName();
                }
            }
        }
        return null;
    }

    public static String getTagNameFromMap(String tag) {
        if (TagConstants.formatedTags.get(tag) != null) {
            return TagConstants.formatedTags.get(tag);
        }
        return getFormattedTagName(tag);
    }

    public static String getFormattedTagName(String tag) {
        String name = tag;
        if (name.contains("-")) {
            name = name.replace(" s ", "'s ");
        } else {
            name = name.toLowerCase().replace(" s ", "'s ");
        }
        final StringBuilder result = new StringBuilder(name.length());
        if (tag.length() > 0) {
            String[] words = name.split("\\s+");
            for (int i = 0, l = words.length; i < l; ++i) {
                if (words[i].contains("/")) {
                    if (i > 0) {
                        result.append(" ");
                    }
                    String[] slashWords = words[i].split("/");
                    for (int j = 0; j < slashWords.length; ++j) {
                        if (j > 0) {
                            result.append("/");
                        }
                        result.append(Character.toUpperCase(slashWords[j].charAt(0)))
                                .append(slashWords[j].substring(1));
                    }

                } else {
                    if (i > 0) {
                        result.append(" ");
                    }
                    result.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1));
                }
            }
            return result.toString();
        } else {
            return name;
        }

    }

    public static String removeDuplicateWords(String sentance) {
        if (sentance.length() > 0) {
            List<String> words = Arrays.asList(sentance.toLowerCase().split("\\s+"));
            Set<String> uniqueWords = new LinkedHashSet<String>();
            for (String string : words) {
                if (string.length() > 0 && string.charAt(string.length() - 1) == 's') {
                    String tempString = string.substring(0, string.length() - 1);
                    if (!uniqueWords.contains(tempString)) {
                        uniqueWords.add(string);
                    }
                } else {
                    uniqueWords.add(string);
                }
            }
            return getFormattedTagName(uniqueWords.toString().replaceAll("(^\\[|\\]$)", "").replace(", ", " "));
        } else {
            return sentance;
        }
    }

    public static String getFilterLink(String[] tags) {
        // todo tolani sort tags on url_priority before creating a link
        StringBuilder output = new StringBuilder();
        output.append(Constants.JEWELLERY_BASE_PATH);

        if (tags.length > 0 && tags[0].length() > 0) {
            output.append("/");
        }

        String prefix = "";
        for (int i = 0; i < tags.length; i++) {
            output.append(prefix);
            prefix = "-";
            output.append(tags[i].replace(" ", "+"));
        }
        output.append(".html");
        return output.toString();
    }

    public static String getFilterLink(String tags) {
        return LinkUtils.getFilterLink(LinkUtils.splitTags(tags));
    }

    public static String[] addElement(String[] array, String add) {
        String[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = add;
        return result;
    }

    public static String[] removeElement(String[] tags, String remove) {
        List<String> taglist = new LinkedList<String>(Arrays.asList(tags));
        taglist.remove(remove);
        return (String[]) taglist.toArray(new String[0]);
    }

    public static String[] splitTags(String tags) {
        String[] tagArr = tags.split("-");
        for (int i = 0; i < tagArr.length; i++) {
            tagArr[i] = tagArr[i].replace('+', ' ');
        }
        return tagArr;
    }

    public static String findAndReplaceTagQuery(String tag, String queryString) {
        String[] query = queryString.split("&");
        String returnString = "";
        boolean found = false;
        for (int i = 0; i < query.length; i++) {
            String temp = query[i].toLowerCase();
            if (temp.contains("tags=")) {
                query[i] = tag;
                found = true;
            }
            returnString = returnString.concat(query[i]).concat("&");
        }
        if (!found) {
            returnString = returnString.concat(tag).concat("&");
        }
        if (returnString.length() > 0) {
            if (returnString.charAt(returnString.length() - 1) == '&') {
                returnString = returnString.substring(0, returnString.length() - 1);
            }
        }

        return returnString;

    }

    public static String concateTags(Set<Tag> tagSet) {
        String output = "";
        for (Tag tag : tagSet) {
            output = output.concat(tag.getName().replace(" ", "+")).concat("-");
        }
        if (output.length() > 0) {
            if (output.charAt(output.length() - 1) == '-') {
                output = output.substring(0, output.length() - 1);
            }
        }
        return output;
    }

    public static String getProductaTagLink(String tag, String category) {
        String[] tags = new String[2];
        tags[0] = tag;
        tags[1] = category;
        String url = LinkUtils.getFilterLink(tags);
        return url;
    }

    public static Map<String, String> reverseHash(Map<String, String> map) {
        log.trace("LinkUtils.reverseHash()");
        Map<String, String> revMap = new LinkedHashMap<String, String>();
        List<String> keys = new ArrayList<String>(map.keySet());
        for (int i = keys.size() - 1; i >= 0; i--) {
            revMap.put(keys.get(i), map.get(keys.get(i)));
        }
        return revMap;
    }

    public static String getProductImageLink(String url) {
        log.debug("LinkUtils.getProductImageLink() for {}", url);
        int hashCode = url.hashCode();
        int mod = Math.abs(hashCode % cdnHosts.length);
        String cdnHostName = cdnHosts[mod];
        StringBuilder stringBuilder = new StringBuilder(cdnHostName.trim());
        stringBuilder.append(url);
        String result = stringBuilder.toString();
        log.debug("LinkUtils.getProductImageLink() resulted in {}", result);
        return result;
    }

    public static String getProductImageLinkForCart(Product product, String imageSize) {
        String imageUrl = product.getImageUrl(imageSize);
        return getProductImageLink(imageUrl);
    }

    public static String getStaticCdnLink(String url) {
        log.debug("LinkUtils.getStaticCdnLink() for {}", url);
        int hashCode = url.hashCode();
        int mod = Math.abs(hashCode % staticCdnHosts.length);
        String cdnHostName = staticCdnHosts[mod];
        StringBuilder stringBuilder = new StringBuilder(cdnHostName.trim());
        stringBuilder.append(Util.getContextPath()).append("/").append(url);
        String result = stringBuilder.toString();
        log.debug("LinkUtils.getStaticCdnLink() resulted in {}", result);
        return result;
    }

    public static String getStaticCdnLink(String themePath, String imagePath) {
        String url = themePath + imagePath;
        log.debug("LinkUtils.getStaticCdnHost() for themePath: {} , iamgePaht: {}", themePath, imagePath);
        int hashCode = url.hashCode();
        int mod = Math.abs(hashCode % staticCdnHosts.length);
        String cdnHostName = staticCdnHosts[mod];
        StringBuilder stringBuilder = new StringBuilder(cdnHostName.trim());
        stringBuilder.append(url);
        String result = stringBuilder.toString();
        log.debug("LinkUtils.getStaticCdnLink() resulted in {}", result);
        return result;
    }

    /**
     * Don't use this function at server side. Use of this can lead to host name
     * being cached, which can be dangerous. Use case where it can lead to wrong
     * host name being cached - Suppose for testing purpose somebody is
     * navigating individual nodes (instead of lb), host name that would get
     * cached would be of node but not bluestone.com. It could lead to wrong seo
     * getting generated, cross domain errors if url is being used in ajax
     * request, etc So please refrain from using this function This function
     * ideally would be used by tag library, urls for sending emails, redirect
     * urls if redirect is happening from controller.
     */
    @Deprecated
    public static String getSiteLink(String url) {
        log.debug("LinkUtils.getSiteLink() for {}", url);
        StringBuilder stringBuilder = new StringBuilder(Util.getSiteUrlWithContextPath());
        if (!StringUtils.startsWith(url, "/")) {
            stringBuilder.append("/");
        }
        stringBuilder.append(url);
        String result = stringBuilder.toString();
        log.debug("LinkUtils.getSiteLink() for {} returns {}", url, result);
        return result;
    }

    /**
     * Don't use this function at server side. Use of this can lead to host name
     * being cached, which can be dangerous. Use case where it can lead to wrong
     * host name being cached - Suppose for testing purpose somebody is
     * navigating individual nodes (instead of lb), host name that would get
     * cached would be of node but not bluestone.com. It could lead to wrong seo
     * getting generated, cross domain errors if url is being used in ajax
     * request, etc So please refrain from using this function This function
     * ideally would be used by tag library, urls for sending emails, redirect
     * urls if redirect is happening from controller.
     */
    @Deprecated
    public static String getCannonicalLink(String url) {
        log.debug("LinkUtils.getCannonicalLink() for {}", url);
        StringBuilder stringBuilder = new StringBuilder(Util.getSiteUrl());
        stringBuilder.append(url);
        String result = stringBuilder.toString();
        log.debug("LinkUtils.getCannonicalLink() for {} returns {}", url, result);
        return result;
    }


    public static Map<String, String> getProductPageBreadCrumbs(String category, String title) {
        log.debug("LinkUtils.getProductPageBreadCrumbs() for Category={} Title={}", category, title);
        Map<String, String> productBreadCrumbs = new LinkedHashMap<String, String>();
        productBreadCrumbs.put(title, "last");
        productBreadCrumbs.put(category, Constants.JEWELLERY_BASE_PATH + "/" + category.toLowerCase() + ".html");
        productBreadCrumbs.put("Jewellery", "jewellery.html");
        productBreadCrumbs.put("Home", "");
        return LinkUtils.reverseHash(productBreadCrumbs);
    }

}
