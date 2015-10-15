package com.bluestone.app.admin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.uniware.UniwareItemService;
import com.google.common.base.Throwables;

/**
 * @author Rahul Agrawal
 *         Date: 2/8/13
 */
@Service
public class UniwareCreateUpdateCustomizationService {

    @Autowired
    private UniwareItemService uniwareItemService;

    private static final Logger log = LoggerFactory.getLogger(UniwareCreateUpdateCustomizationService.class);

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public void execute(Customization customization) throws ProductUploadException {
        final String skuCode = customization.getSkuCode();
        String designCode = customization.getDesign().getDesignCode();
        log.info("UniwareCreateUpdateCustomizationService.execute():CustomizationId=[{}] DesignCode=[{}] SkuCode=[{}]",
                 customization.getId(), designCode, skuCode);

        List<String> skuCodeList = SizeBasedSKUCodeGenerator.execute(customization);

        for (String sku : skuCodeList) {
            try {
                if (uniwareItemService.createEditItemType(customization, sku)) {
                    log.info("UploadToUniware: ** Success ** for CustomizationId=[{}] , SKU=[{}]", customization.getId(), sku);
                } else {
                    throw new ProductUploadException("Unable to Create/Update product in uniware for SkuCode=" + sku);
                }
            } catch (Exception e) {
                log.error("Error : UniwareCreateUpdateCustomizationService.execute():Customization={}, DesignCode={} , SKU={} , Reason={}",
                          customization.getId(), designCode, sku, e.toString(), e);
                throw new ProductUploadException("UploadToUniware failed for customizationId=" + customization.getId()
                          + " Design=" + designCode +
                          " SKU=" + sku + " Reason=" + Throwables.getRootCause(e).toString());
            }
        }
        if (skuCodeList.size() > 10) {
            try {
                log.info("UniwareCreateUpdateCustomizationService.execute(): Sleeping for 5 sec ..........................");
                Thread.sleep(5000);
            } catch (Exception e) {
                log.info("UniwareCreateUpdateCustomizationService.execute(): Interrupted from sleep");
            }
        }
    }

}
