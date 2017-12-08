package com.bluestone.app.admin.service.product.config.update.postprocessors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;
import com.bluestone.app.design.model.Customization;

@Service
@Transactional(readOnly=true)
public class UpdateCustomizationInUniware implements PostProductUpdateProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateCustomizationInUniware.class);
    
    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;
    

    @Override
    public void execute(UpdateMetaData updateMetaData) throws ProductUploadException {
        Customization customization = updateMetaData.getCustomization();
        updateUniware(customization);
        for (Customization eachDerivedCustomization : customization.getDerivedCustomizations()) {
            updateUniware(eachDerivedCustomization);
        }
    }

    private void updateUniware(Customization customization) throws ProductUploadException {
        uniwareCreateUpdateCustomizationService.execute(customization);
        log.info("design {} customization {} pushed to uniware", customization.getDesign().getId(), customization.getId());
    }
}
