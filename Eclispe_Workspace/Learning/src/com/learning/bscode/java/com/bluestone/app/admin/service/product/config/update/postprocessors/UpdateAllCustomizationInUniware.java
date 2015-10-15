package com.bluestone.app.admin.service.product.config.update.postprocessors;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;

@Service
@Transactional(readOnly=true)
public class UpdateAllCustomizationInUniware implements PostProductUpdateProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateAllCustomizationInUniware.class);
    
    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;
    

    @Override
    public void execute(UpdateMetaData updateMetaData) throws ProductUploadException {
        Design design = updateMetaData.getDesign();
        List<Customization> customizations = design.getCustomizations();
        for (Customization customization : customizations) {
            uniwareCreateUpdateCustomizationService.execute(customization);
            log.info("design {} customization {} pushed to uniware", design.getId(), customization.getId());
        }
    }
}
