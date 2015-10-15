package com.bluestone.app.admin.service.product.config.customization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.img.BSImageUploader;
import com.bluestone.app.admin.service.product.config.img.ImageResizer;
import com.bluestone.app.design.model.Customization;

@Service
public class ParentCustomizationIntegrationProcessor {

    private static final Logger log = LoggerFactory.getLogger(ParentCustomizationIntegrationProcessor.class);

    @Autowired
    BSImageUploader bsImageUploader;

    @Autowired
    private ImageResizer imageResizer;

    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false, rollbackFor = Exception.class)
    public void pushToS3AndUniware(Customization parentCustomization) throws ProductUploadException {
        final String designCode = parentCustomization.getDesign().getDesignCode();
        log.info("ParentCustomizationIntegrationProcessor.pushToS3AndUniware(): Design=[{}] CustomizationId=[{}] Sku=[{}]",
                 designCode, parentCustomization.getId(), parentCustomization.getSkuCode());

        String generatedImagesDirPath = imageResizer.resizeAndRenameImages(designCode, parentCustomization);

        bsImageUploader.execute(generatedImagesDirPath);

        final int originalImageVersionNo = parentCustomization.getImageVersion();
        int newImageVersion = originalImageVersionNo + 1;
        parentCustomization.setImageVersion(newImageVersion);
        log.info("ParentCustomizationIntegrationProcessor: DesignCode=[{}] CustomizationId=[{}] Setting Image Version to [{}]",
                 designCode, parentCustomization.getId(), newImageVersion);

        uniwareCreateUpdateCustomizationService.execute(parentCustomization);
    }
}
