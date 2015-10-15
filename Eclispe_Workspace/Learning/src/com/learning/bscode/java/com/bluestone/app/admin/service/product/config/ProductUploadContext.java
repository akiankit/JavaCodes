package com.bluestone.app.admin.service.product.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Rahul Agrawal
 *         Date: 3/22/13
 */
@Component
public class ProductUploadContext {

    private static final Logger log = LoggerFactory.getLogger(ProductUploadContext.class);

    @Value("${product-upload-path}")
    private String productUploadPath;

    private String rootDir = null;
    private static final String DESIGN_IMAGES = File.separator + "Render";

    @PostConstruct
    public void init() {
        log.info("ImageProcessorContext.init(): productUploadPath=[{}]", productUploadPath);
        Assert.notNull(productUploadPath, "ProductUpload Path should not be null");
        rootDir = productUploadPath + File.separator;
    }

    public String getImagesDirPath(String designCode) {
        return (rootDir + designCode + DESIGN_IMAGES);
    }

    public String getDesignBundleDirPath(String designCode) {
        return (rootDir + designCode);
    }

    public String getProductUploadPath() {
        return productUploadPath;
    }
}
