package com.bluestone.app.admin.service.product.config;

import java.io.File;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.customization.ParentCustomizationIntegrationProcessor;
import com.bluestone.app.admin.service.product.config.customization.derived.DerivedCustomizationIntegrationProcessor;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.service.CustomizationService;

/**
 * @author Rahul Agrawal
 *         Date: 2/5/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = true)
public class DesignImageUpdateService {

    private static final Logger log = LoggerFactory.getLogger(DesignImageUpdateService.class);

    @Autowired
    private ProductUploadContext productUploadContext;

    @Autowired
    private DesignDao designdao;

    @Autowired
    private DesignViewHelper designViewHelper;

    @Autowired
    private CustomizationService customizationService;

    @Autowired
    private DerivedCustomizationIntegrationProcessor derivedCustomizationsImageProcessor;

    @Autowired
    private ParentCustomizationIntegrationProcessor parentCustomizationIntegrationProcessor;

    //@TriggersRemove(cacheName = {"browsePageProductsCache", "searchPageCustomizationCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public void execute(String eachDesignCode) throws ProductUploadException {
        log.info("DesignImageUpdateService.execute(): DesignCode={}", eachDesignCode);
        Design design = designdao.getDesignByCode(eachDesignCode);
        if (design == null) {
            throw new ProductUploadException("Could not get designs for selected Design Code=" + eachDesignCode);
        }
        File designFile = getDesignFile(design);
        Design updatedDesign = null;
        if (designFile.exists()) {
            log.info("DesignCode=[{}] Processing file=[{}] for upload of images", design.getDesignCode(), designFile.getAbsolutePath());

            designViewHelper.updateDesignView(design);
            Customization parentCustomization = customizationService.getBriefCustomizationByDesignId(design.getId(), true, Customization.DEFAULT_PRIORITY);
            parentCustomizationIntegrationProcessor.pushToS3AndUniware(parentCustomization);

            Set<Customization> derivedCustomizations = parentCustomization.getDerivedCustomizations();
            derivedCustomizationsImageProcessor.pushToS3AndUniware(parentCustomization, derivedCustomizations);

            updatedDesign = designdao.update(design);
            cleanUp(updatedDesign.getDesignCode());
        } else {
            log.error("Error: DesignImageUpdateService: Could not find the Render folder=[{}] for Design Code=[{}]", designFile.getAbsolutePath(), design.getDesignCode());
            throw new ProductUploadException(
                    "Could not find Render folder=" + designFile.getAbsolutePath() + " for design code=" + design.getDesignCode());
        }
    }


    private File getDesignFile(Design eachDesign) {
        return new File(productUploadContext.getImagesDirPath(eachDesign.getDesignCode()));
    }

    private void cleanUp(String designCode) {
        log.info("DesignImageUpdateService.cleanUp(): Dir=[{}]", productUploadContext.getProductUploadPath());
        Cleaner.backupAndDelete(designCode, productUploadContext.getProductUploadPath());
    }
}

