package com.bluestone.app.admin.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.admin.service.CustomizationsPerDesign;
import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.customization.derived.DerivedCustomizationService;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.DesignService;

@Controller
@RequestMapping("/admin/tools/customization/*")
public class AdminCustomizationUtilityController {

    private static final Logger log = LoggerFactory.getLogger(AdminCustomizationUtilityController.class);

    @Autowired
    private CustomizationsPerDesign customizationsPerDesign;
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private DerivedCustomizationService derivedCustomizationService;

    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;

    @Autowired
    private DesignService designService;

    @RequestMapping(value = "/upload/design/{designId}", method = RequestMethod.GET)
    @RequiresPermissions("customizationutility:edit")
    public
    @ResponseBody
    String uploadAllCustomizationOfDesign(@PathVariable long designId, HttpServletRequest httpServletRequest) {
        log.info("****** Start ***** Upload of images for design id [{}]...........", designId);
        StringBuffer result = new StringBuffer("<p><br>Upload Result</br>");
        Design design = designService.findByPrimaryKey(designId);
        List<Customization> oldCustomizationList = design.getCustomizations();
        Customization baseCustomization = null;
        for (Customization eachCustomization : oldCustomizationList) {
            log.info("DesignId={} Each Customization : Id=[{}]  Priority=[{}]  Sku=[{}]", baseCustomization.getId(), eachCustomization.getPriority(), eachCustomization.getSkuCode());
            if (eachCustomization.hasDefaultPriority()) {
                baseCustomization = eachCustomization;
            } else {
                customizationsPerDesign.uploadToS3(baseCustomization, eachCustomization);
                log.info("Upload of images for Design Id=[{}] CustomizationId={} to S3 done........", designId, eachCustomization.getId());
            }

            try {
                uniwareCreateUpdateCustomizationService.execute(eachCustomization);
                log.info("Upload for design {} to uniware done........", designId);
                result.append("<br>CustomizationId=").append(eachCustomization.getId()).append(" pushed to uniware.").append("</br>");
            } catch (ProductUploadException e) {
                String errorMessage = e.generateErrorMessage();
                log.error("Upload for design {} to uniware failed........{} ", designId, errorMessage);
                result.append("<br>").append(errorMessage).append("</br>");
            }
        }
        log.info("Upload for Design to uniware and s3 done........");
        return result.toString();
    }

    @RequestMapping(value = "/uniware/design/{designId}", method = RequestMethod.GET)
    @RequiresPermissions("customizationutility:edit")
    public  @ResponseBody String pushADesignToUniware(@PathVariable long designId, HttpServletRequest httpServletRequest) {
        log.info("****** Start ***** Upload to uniware for design id {}...........", designId);
        StringBuffer result = new StringBuffer("<p><br>Upload Result</br>");
        Design design = designService.findByPrimaryKey(designId);
        List<Customization> oldCustomizationList = design.getCustomizations();
        for (Customization eachCustomization : oldCustomizationList) {
            try {
                uniwareCreateUpdateCustomizationService.execute(eachCustomization);
                log.info("Upload for design {} to uniware done........", designId);
                result.append("<br>CustomizationId=").append(eachCustomization.getId()).append(" pushed to uniware.").append("</br>");
            } catch (ProductUploadException e) {
                String errorMessage = e.generateErrorMessage();
                log.error("Upload for design {} to uniware failed........{} ", designId, errorMessage);
                result.append("<br>").append(errorMessage).append("</br>");
            }
        }
        log.info("Upload for Design to uniware done........");
        return result.toString();
    }
    
    @RequestMapping(value = "/upload/derived/{parentCustomizationId}", method = RequestMethod.GET)
    @RequiresPermissions("customizationutility:edit")
    public  @ResponseBody String generateDerivedCustomizations(@PathVariable long parentCustomizationId, HttpServletRequest httpServletRequest) {
        log.info("****** Start ***** Generation of derived customizations for parent customization id {}...........", parentCustomizationId);
        StringBuffer result = new StringBuffer("<p><br>Creation Of Derived Customization Result</br>");
        Customization parentCustomization = customizationService.getBriefCustomizationByCustomizationId(parentCustomizationId, true);
        try {
            Set<Customization> derivedCustomizationList = derivedCustomizationService.createDerivedCustomization(parentCustomization);
            log.info("Generation of derived customizations for parent customization id {} SUCCESSFUL", parentCustomizationId);
            result.append("<br>For Parent CustomizationId=").append(parentCustomizationId).append(" all derived customizations generated.").append("</br>");
            for (Customization derivedCustomization : derivedCustomizationList) {
                result.append("<br>Generated Derived CustomizationId=").append(derivedCustomization.getId()).append("</br>");
            }
        } catch (ProductUploadException e) {
            String errorMessage = e.generateErrorMessage();
            log.error("Generation of derived customizations for parent customization {} failed........{} ", parentCustomizationId, errorMessage);
            result.append("<br>").append(errorMessage).append("</br>");
        }
        log.info("****** END ***** Generation of derived customizations for parent customization id {}...........", parentCustomizationId);
        return result.toString();
    }
}
