package com.bluestone.app.admin.service.product.config.update.postprocessors;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.pricing.PricingEngineService;

@Service
@Transactional(readOnly=true)
public class UpdateAllCustomizationPrice implements PostProductUpdateProcessor {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateAllCustomizationPrice.class);
    
    @Autowired
    private PricingEngineService pricingEngineService;
    

    @Override
    public void execute(UpdateMetaData updateMetaData) throws ProductUploadException {
        Design design = updateMetaData.getDesign();
        List<Customization> customizations = design.getCustomizations();
        for (Customization customization : customizations) {
            double price = pricingEngineService.getPrice(customization);
            customization.setPrice(BigDecimal.valueOf(price));
            log.info("Update price {} for design {} customization {}", price, design.getId(), customization.getId());
        }
    }
}
