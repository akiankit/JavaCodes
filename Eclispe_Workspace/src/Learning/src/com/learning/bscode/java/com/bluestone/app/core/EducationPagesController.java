package com.bluestone.app.core;

import java.util.ArrayList;
import java.util.List;

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
@RequestMapping(value = "/education")
public class EducationPagesController {

    private static final Logger log = LoggerFactory.getLogger(EducationPagesController.class);

    @Autowired
    private CustomizationService customizationService;

    @Autowired
    private PropertyService propertyService;

    private final String EDUCATION_PAGE_PRODUCTS = "EDUCATION_PAGE_PRODUCTS";
    private final int PRIORITY_ONE = 1;

    @RequestMapping(value = "/category/{page}/{id}", method = RequestMethod.GET)
    public ModelAndView showCategoryPage(@PathVariable("page") String page, @PathVariable("id") String id, ModelMap viewData) {
        String category = page + "-cat-" + id;
        viewData.put("products", getEducationPageProducts());
        return new ModelAndView("education." + category, viewData);
    }

    @RequestMapping(value = "/article/{page}/{id}", method = RequestMethod.GET)
    public ModelAndView showArticlePage(@PathVariable("page") String page, @PathVariable("id") String id, ModelMap viewData) {
        String category = page + "-" + id;
        viewData.put("products", getEducationPageProducts());
        return new ModelAndView("education-article." + category, viewData);
    }

    private List<Customization> getEducationPageProducts() {
        //TODO: Get the final list of products from anshul before going live
        //String[] prodIds = ApplicationProperties.getProperty("EDUCATION_PAGE_PRODUCTS").split(",");
        Property property = propertyService.getPropertyByName(EDUCATION_PAGE_PRODUCTS);
        List<Customization> customizationList = new ArrayList<Customization>();
        if (property != null) {
            List<Long> designIdList = property.getLongValues();
            for (Long designId : designIdList) {
                Customization customization = customizationService.getBriefCustomizationByDesignId(designId, false, Customization.DEFAULT_PRIORITY);
                if (customization != null) {
                    String getLongDescription = ellipsize(customization.getDesign().getLongDescription(), 160);
                    customization.getDesign().setLongDescription(getLongDescription);
                    customizationList.add(customization);
                }
            }
        }
        return customizationList;
    }

    private static String ellipsize(String text, int max) {
        log.debug("EducationPagesController.ellipsize(): Text=[{}] max=[{}]", text, max);
        if (textWidth(text) <= max) {
            return text;
        }
        // Start by chopping off at the word before max
        // This is an over-approximation due to thin-characters...
        int end = text.lastIndexOf(' ', max - 3);

        // Just one long word. Chop it off.
        if (end == -1) {
            return text.substring(0, max - 3) + "...";
        }

        // Step forward as long as textWidth allows.
        int newEnd = end;
        do {
            end = newEnd;
            newEnd = text.indexOf(' ', end + 1);
            // No more spaces.
            if (newEnd == -1) {
                newEnd = text.length();
            }
        } while (textWidth(text.substring(0, newEnd) + "...") < max);

        return text.substring(0, end) + "...";
    }

    private static int textWidth(String str) {
        return (str.length() - str.replaceAll(NON_THIN, "").length() / 2);
    }

    private final static String NON_THIN = "[^iIl1\\.,']";

}
