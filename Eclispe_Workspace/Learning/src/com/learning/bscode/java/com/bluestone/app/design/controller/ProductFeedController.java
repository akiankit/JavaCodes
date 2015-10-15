package com.bluestone.app.design.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bluestone.app.core.service.VelocityTemplateService;
import com.bluestone.app.core.util.DownloadUtil;
import com.bluestone.app.core.util.GoogleProductsFeedUtil;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.CustomizationService;

@Controller
@RequestMapping(value={"/feed/product*"})
public class ProductFeedController {
    
    private static final Logger log = LoggerFactory.getLogger(ProductFeedController.class);
    
    private static final String FILE_NAME_PREFIX = "ProductList";
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private VelocityTemplateService velocityTemplateService;
    
    @RequestMapping(value="/{designid}", method = RequestMethod.GET)
    public ResponseEntity<String> showDesignAsXml(@PathVariable("designid") Long designId,HttpServletRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
        
        Product product = customizationService.getBriefCustomizationByDesignId(designId, false, Customization.DEFAULT_PRIORITY);
        
        
        Map<String, Serializable> templateVariables = new HashMap<String, Serializable>();
        templateVariables.put("product", product);
        templateVariables.put("productUrl", LinkUtils.getSiteLink(product.getUrl()));
        
        if(product != null) {
            BigDecimal price = product.getPrice();
            BigDecimal emi = price.divide(BigDecimal.valueOf(6.0),RoundingMode.UP);
            templateVariables.put("imageurl", LinkUtils.getProductImageLink(product.getImageUrl("original")));
            templateVariables.put("price", NumberUtil.formatPriceIndian(price.doubleValue(), ","));
            templateVariables.put("emi", NumberUtil.formatPriceIndian(emi.doubleValue(), ","));
        }
        
        String text = velocityTemplateService.getText("product.vm", templateVariables);
        return new ResponseEntity<String>(text, responseHeaders, HttpStatus.OK);
    }
    
    @RequestMapping(value="/all", method = RequestMethod.GET)
    public ResponseEntity<String> showAllDesignsAsXml() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
        List<Customization> allCustomizationWithDefaultPriority = customizationService.getAllCustomizationsofParentCategoryWithPriority(Customization.DEFAULT_PRIORITY);
        
        Map<String, Object> templateVariables = new HashMap<String, Object>();
        templateVariables.put("products", allCustomizationWithDefaultPriority);
        templateVariables.put("number", NumberUtil.class);
        templateVariables.put("link", LinkUtils.class);
        
        Customization customizationTobeShownAsGoldMineProduct = customizationService.getBriefCustomizationByDesignId(624l, false, Customization.DEFAULT_PRIORITY);
        templateVariables.put("goldmine", customizationTobeShownAsGoldMineProduct);

        
        String text = velocityTemplateService.convertProductsToText("products.vm", templateVariables);
        return new ResponseEntity<String>(text, responseHeaders, HttpStatus.OK);
    }
    
    @RequestMapping(value="/all/download", method = RequestMethod.GET)
    public void downloadAllDesignsAsXml(HttpServletRequest request,HttpServletResponse response) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
        try {
            List<Customization> allCustomizationWithDefaultPriority = customizationService.getAllCustomizationsofParentCategoryWithPriority(Customization.DEFAULT_PRIORITY);
            Map<String, Object> templateVariables = new HashMap<String, Object>();
            templateVariables.put("products", allCustomizationWithDefaultPriority);
            templateVariables.put("number", NumberUtil.class);
            templateVariables.put("link", LinkUtils.class);
            
            Customization customizationTobeShownAsGoldMineProduct = customizationService.getBriefCustomizationByDesignId(624l, false, Customization.DEFAULT_PRIORITY);
            templateVariables.put("goldmine", customizationTobeShownAsGoldMineProduct);
            
            String textAsXml = velocityTemplateService.convertProductsToText("products.vm", templateVariables);
            DownloadUtil.copyDataToHttpResponse(FILE_NAME_PREFIX, response, "text/xml", textAsXml, ".xml");
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error while downloading product data to file", e);
            }
        }
    }
    
    @RequestMapping(value="/google_product_feed.xml", method = RequestMethod.GET)
    public ResponseEntity<String> showAllParentDesignsAsXml() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/xml; charset=utf-8");
        List<Customization> allCustomizationWithDefaultPriority = customizationService.getAllCustomizationsofParentCategoryWithPriority(Customization.DEFAULT_PRIORITY);
        
        Map<String, Object> templateVariables = new HashMap<String, Object>();
        templateVariables.put("products", allCustomizationWithDefaultPriority);
        templateVariables.put("number", NumberUtil.class);
        templateVariables.put("link", LinkUtils.class);
        templateVariables.put("xmlUtil", StringEscapeUtils.class);
        templateVariables.put("googleFeedUtil", GoogleProductsFeedUtil.class);        
        
        String text = velocityTemplateService.convertProductsToText("google_product_feed.vm", templateVariables);
        return new ResponseEntity<String>(text, responseHeaders, HttpStatus.OK);
    }

}
