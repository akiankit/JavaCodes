package com.bluestone.app.admin;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.service.CustomizationService;

@Controller
@RequestMapping("/admin/*")
public class UniwareCustomizationDataSyncController {

    private static final Logger log = LoggerFactory.getLogger(UniwareCustomizationDataSyncController.class);

    @Autowired
    private CustomizationService customizationService;

    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;

    @RequestMapping(value = "/custid/{startId}/{endId}", method = RequestMethod.GET)
    public
    @ResponseBody
    String pushCustomizationSKUToUniware(@PathVariable String startId, @PathVariable String endId) {
        log.info("UniwareCustomizationDataSyncController.pushCustomizationSKUToUniware(): StartId={} , EndId={}", startId, endId);
        StringBuilder response = new StringBuilder();
        for (int i = Integer.parseInt(startId); i <= Integer.parseInt(endId); i++) {
            String value = String.valueOf(i);
            response.append("<p>");
            response.append(i).append(" ");
            response.append("------------------------------------");
            response.append("<br></br>");
            response.append(execute(value));
            //response.append("<br></br>");
            response.append("</p>");
        }

        return response.toString();
    }

    @RequestMapping(value = "/custid/{value}", method = RequestMethod.GET)
    public
    @ResponseBody
    String pushCustomizationSKUToUniware(@PathVariable String value) {
        log.info("UniwareCustomizationDataSyncController.pushCustomizationSKUToUniware(): Customization id={}", value);
        return execute(value);
    }

    private String execute(String custId) {
        log.info("UniwareCustomizationDataSyncController.pushCustomizationSKUToUniware(): Customization id={}", custId);
        List<String> errors = new LinkedList<String>();
        Customization customization = null;
        try {
            long customizationId = Long.parseLong(custId);
            customization = customizationService.getBriefCustomizationByCustomizationId(customizationId, true);
            if (customization != null) {
                log.info("Starting upload to uniware {}", customizationId);
                uniwareCreateUpdateCustomizationService.execute(customization);
                log.info("Upload to uniware success for CustomizationId=[{}] SkuCode=[{}]", customizationId, customization.getSkuCode());
            } else {
                log.info("Customization Id [{}] not found in database. Ignoring it for upload", customizationId);
                errors.add("Customization Id " + custId + " not found in the database");
            }
        } catch (NumberFormatException e) {
            log.error("Error: UniwarePO.pushCustomizationSKUToUniware(): customization id=[{}]", custId, e);
            errors.add("Error: Customization Id should be numeric. " + e.toString());
        } catch (ProductUploadException exception) {
            log.error("Error: UniwareCustomizationDataSyncController.execute(): for customizationId={} ", custId);
            errors.add(exception.generateErrorMessage());
        } catch (Exception exception) {
            log.error("Error: UniwareCustomizationDataSyncController.execute(): for customizationId={} ", custId);
            errors.add("Error: UniwareCustomizationDataSyncController.execute(): for customizationId=" + custId);
        }


        StringBuilder response = new StringBuilder();
        for (String error : errors) {
            response.append(error).append("<br></br>");
        }
        if (errors.isEmpty()) {
            return "** Successful ** Upload to Uniware for Customization id=[" + custId + "] Sku=[" + customization.getSkuCode() + "]";
        } else {
            return response.toString();
        }
    }
}
