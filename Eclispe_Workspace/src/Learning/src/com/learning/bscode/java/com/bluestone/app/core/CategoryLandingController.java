package com.bluestone.app.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.PropertyService;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.service.CustomizationService;

@Controller
@RequestMapping(value = "/category")
public class CategoryLandingController {

    private static final Logger log = LoggerFactory.getLogger(CategoryLandingController.class);

    @Autowired
    private CustomizationService customizationService;

    @Autowired
    private PropertyService propertyService;

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView showCategoryLandingPage(@PathVariable("page") String page, ModelMap viewData) {
        log.debug("CategoryLandingController.showCategoryLandingPage() for {}", page);
        Property property = null;
        if (page.equalsIgnoreCase("rings")) {
            property = propertyService.getPropertyByName("CATEGORY_RINGS_IDS");
        } else if (page.equalsIgnoreCase("earrings")) {
            property = propertyService.getPropertyByName("CATEGORY_EARRINGS_IDS");
        } else if (page.equalsIgnoreCase("pendants")) {
            property = propertyService.getPropertyByName("CATEGORY_PENDANTS_IDS");
        } else if (page.equalsIgnoreCase("bangles")) {
            property = propertyService.getPropertyByName("CATEGORY_BANGLES_IDS");
        } else if (page.equalsIgnoreCase("tanmaniya")) {
            property = propertyService.getPropertyByName("CATEGORY_TANMANIYA_IDS");
        } else if (page.equalsIgnoreCase("gifts")) {
            property = propertyService.getPropertyByName("GIFT_IDS");
        }

        List<Customization> products = new ArrayList<Customization>();
        if (property != null) {
            List<Long> prodIds = property.getLongValues();
            for (Long designId : prodIds) {
                Customization customization =
                        customizationService.getBriefCustomizationByDesignId(designId, false, Customization.DEFAULT_PRIORITY);
                if (customization != null) {
                    String finalDesc = getLongDescriptionForCategoryLandingPage(customization.getDesign().getLongDescription().trim());
                    customization.getDesign().setLongDescription(finalDesc);
                    products.add(customization);
                }
            }
        }
        viewData.put("products", products);
        viewData.put("landingPage", page.toLowerCase());
        return new ModelAndView(page, viewData);
    }

    static String getLongDescriptionForCategoryLandingPage(String desc) {
        String finalDesc = "";
        String[] split = StringUtils.splitByWholeSeparator(desc, ".");
        for (String str : split) {
            if (finalDesc.equals("")) {
                finalDesc += str;
                continue;
            }
            String addedStr = finalDesc + str;
            if (finalDesc.length() < 140 && addedStr.length() < 140) {
                if (!finalDesc.equals("")) {
                    finalDesc += ". ";
                }
                finalDesc += str;
            }
        }
        return finalDesc;
    }

}
