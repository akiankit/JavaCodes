package com.bluestone.app.design.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.ImageEnum;
import com.bluestone.app.core.util.ImageProperties;

/**
 * @author Rahul Agrawal
 *         Date: 3/17/13
 */
public class CustomizationImages implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(CustomizationImages.class);

    private Customization customization;

    private List<Image> imageUrls = new LinkedList<Image>();
    private Design design;

    public CustomizationImages(Customization customization) {
        this.customization = customization;
        design = customization.getDesign();
    }


    public List<Image> getImageUrls() {
        log.trace("CustomizationImages.generateImageUrls()");
        if (imageUrls.isEmpty()) {
            DesignImageView designImageView = design.getDesignImageView();
            if (designImageView != null) {
                List<ImageEnum> allImagesViews = designImageView.getAllImagesViews(design.getDesignCategory().getCategoryType());
                setProductImageUrls(allImagesViews);
            }
        }
        return imageUrls;
    }

    private void setProductImageUrls(List<ImageEnum> allImagesViews) {
        final String skuCode = customization.getSkuCode();
        log.trace("CustomizationImages.setProductImageUrls() for Sku=[{}]", skuCode);

        final ImmutableList.Builder<Image> builder = ImmutableList.builder();

        StringBuilder baseUrl = new StringBuilder("/").append(skuCode).append("-");
        for (ImageEnum eachImageViewPriority : allImagesViews) {
            final String base = new StringBuilder(baseUrl).append(eachImageViewPriority.getShortName()).append("-").toString();
            log.trace("View Priority=[{}] : Base Url={}]", eachImageViewPriority.getFullName(), baseUrl.toString());
            Image eachImage = new Image();
            eachImage.setImageViewType(eachImageViewPriority.getFullName());
            Map<String, String> sizeVsUrlMap = new HashMap<String, String>();
            for (String eachImageSize : ImageProperties.imageSizes) {
                StringBuilder stringBuilder = new StringBuilder(base);
                stringBuilder.append(eachImageSize).append(ImageProperties.imageExtension).append("?version=").append(customization.getImageVersion());
                final String url = stringBuilder.toString();
                log.trace("\t[{}]=[{}]", eachImageSize, url);
                sizeVsUrlMap.put(eachImageSize, url);
            }
            eachImage.setSizeVsUrlMap(sizeVsUrlMap);
            builder.add(eachImage);
        }
        imageUrls = builder.build();
    }
}
