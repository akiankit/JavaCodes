package com.bluestone.app.admin.service.product.config.customization;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.csv.CsvStatementLogger;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.MetalSpecificationDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.pricing.PricingEngineService;

/**
 * @author Rahul Agrawal
 *         Date: 3/28/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class CustomizationWtChangeProcessor {

    private static final Logger log = LoggerFactory.getLogger(CustomizationWtChangeProcessor.class);

    @Autowired
    private CustomizationDao customizationdao;

    @Autowired
    private MetalSpecificationDao metalSpecificationDao;

    @Autowired
    private PricingEngineService pricingEngineService;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateWeightForCustomization(Double weight, Customization customization, boolean isMetalWeightInPercent, boolean isTotalWeight) throws ProductUploadException {
        BigDecimal totalStoneWeight = customization.getTotalStoneWeight();
        List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
        //assuming only one metal family
        BigDecimal newWeight = BigDecimal.valueOf(weight);
        BigDecimal oldPrice = customization.getPrice();
        for (CustomizationMetalSpecification aCustomizationMetalSpecification : customizationMetalSpecifications) {
            log.debug("Old metal weight of customizaion with sku code ={} is {}", customization.getSkuCode(), aCustomizationMetalSpecification.getMetalWeight());
            BigDecimal oldWeight = aCustomizationMetalSpecification.getMetalWeight();
            if (!isMetalWeightInPercent) {
                if (isTotalWeight) {
                    log.debug("substracting total stone weight = {} from total weight={} ", totalStoneWeight, newWeight);
                    aCustomizationMetalSpecification.setMetalWeight(newWeight.subtract(totalStoneWeight));
                } else {
                    log.debug("Setting metal weight as ={} ", newWeight);
                    aCustomizationMetalSpecification.setMetalWeight(newWeight);
                }
            } else {
                BigDecimal metalWeight = aCustomizationMetalSpecification.getMetalWeight();
                log.debug("Multiplying {} to metal weight={} ", newWeight, metalWeight);
                metalWeight = metalWeight.multiply(newWeight);
                aCustomizationMetalSpecification.setMetalWeight(metalWeight);
            }
            customization.setPrice(getCustomizationPrice(customization));
            CsvStatementLogger.log.warn("{},{},{},{},{},{}", customization.getDesign().getId(), customization.getSkuCode(), oldWeight, aCustomizationMetalSpecification.getMetalWeight(), oldPrice, customization.getPrice());
            log.debug("updating metal weight for customizaion sku code ={} with new weight={}", customization.getSkuCode(), aCustomizationMetalSpecification.getMetalWeight());
            metalSpecificationDao.update(aCustomizationMetalSpecification);
        }
        customizationdao.update(customization);
    }

    public BigDecimal getCustomizationPrice(Customization customization) {
        return BigDecimal.valueOf(pricingEngineService.getPrice(customization));
    }
}
