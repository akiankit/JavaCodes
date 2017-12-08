package com.bluestone.app.admin.service.product.config.update.postprocessors;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.pricing.PricingEngineService;

@Service
@Transactional(readOnly=true)
public class UpdateCustomizationPrice implements PostProductUpdateProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateCustomizationPrice.class);
    
    @Autowired
    private PricingEngineService pricingEngineService;
    

    @Override
    public void execute(UpdateMetaData updateMetaData) throws ProductUploadException {
        Customization customization = updateMetaData.getCustomization();
        updatePrice(customization);
        for (Customization eachDerivedCustomization : customization.getDerivedCustomizations()) {
             updatePrice(eachDerivedCustomization);
        }
    }


    private void updatePrice(Customization customization) {
        double price = pricingEngineService.getPrice(customization);
        customization.setPrice(BigDecimal.valueOf(price));
        log.info("Update price {} for design {} customization {}", price, customization.getDesign().getId(), customization.getId());
    }
}
