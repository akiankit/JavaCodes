package com.bluestone.app.admin.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.TagFilter;
import com.bluestone.app.admin.service.FilterTagService;
import com.bluestone.app.core.util.RuleUtil;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.DesignService;
import com.bluestone.app.ops.RuntimeService;

@Controller
@RequestMapping(value = { "/admin/filter*" })
public class FilterController {
    
    private static final Logger  log = LoggerFactory.getLogger(FilterController.class);
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private DesignService        designService;
    
    @Autowired
    private FilterTagService     FilterService;
    
    @RequestMapping(value = "/add*", method = RequestMethod.GET)
    @RequiresPermissions("filter:add")
    public ModelAndView addRule() {
        log.info("FilterController.addRule()");
        Map<String, Object> filterPage = new HashMap<String, Object>();
        filterPage.put("tagCategoryList", FilterService.getAllTagCategories());
        filterPage.put("type", "add");
        /*
         * List<Design> designs = designService.getDesignList("1014");
         * List<Customization> customization = new ArrayList(); for (Design
         * design : designs) {
         * customization.add(customizationService.getCustomizationsByDesign
         * (design.getId(), 1)); }
         * FilterService.generateFilterTags(customization);
         */
        
        return new ModelAndView("filterRule", "filterPage", filterPage);
    }
    
    @RequestMapping(value = "/add*", method = RequestMethod.POST)
    @RequiresPermissions("filter:add")
    public ModelAndView createRule(MultipartHttpServletRequest request) {
        log.info("FilterController.createRule()");
        String category = request.getParameter("groupname");
        String filtername = request.getParameter("filtername");
        String urlPriority = request.getParameter("url_priority");
        String inputGroupName = request.getParameter("input_groupname");
        String inputPriority = request.getParameter("input_priority");
        String[] left = request.getParameterValues("mfield[]");
        String[] compare = request.getParameterValues("comType[]");
        String[] right = request.getParameterValues("mvalue[]");
        String[] logical = request.getParameterValues("addType[]");
        String type = request.getParameter("type");
        String id = request.getParameter("id");
        String rule = RuleUtil.createRule(left, right, logical, compare);
        log.info("FilterController.createRule() - {}", rule);
        Map<String, Object> filterPage = new HashMap<String, Object>();
        filterPage.put("tagCategoryList", FilterService.getAllTagCategories());
        filterPage.put("type", "add");
        try {
            RuntimeService.enableFullProductConfigurationLogging(Level.DEBUG);
            if ("add".equalsIgnoreCase(type)) {
                FilterService.addNewFilterRule(category, urlPriority, filtername, rule, inputGroupName, inputPriority);
                filterPage.put("message", "Added new rule successfully");
            } else {
                FilterService.updateFilterRule(id, category, urlPriority, filtername, rule, inputGroupName, inputPriority);
                filterPage.put("message", "Updated rule successfully");
            }
        } catch (Exception e) {
            log.warn("FilterController.createRule(): Error {}", e.getMessage(), e);
            filterPage.put("message", "Error occurred while performing add/update");
        } finally {
            RuntimeService.enableFullProductConfigurationLogging(Level.INFO);
        }
        return new ModelAndView("filterRule", "filterPage", filterPage);
    }
    
    @RequestMapping(value = "/list*", method = RequestMethod.GET)
    @RequiresPermissions("filter:view")
    public ModelAndView listFilters() {
        log.info("FilterController.listFilters()");
        Map<String, Object> filterPage = new HashMap<String, Object>();
        List<TagFilter> filterList = FilterService.getFilterList();
        filterPage.put("filterList", filterList);
        return new ModelAndView("filterList", "filterPage", filterPage);
    }
    
    @RequestMapping(value = "/edit/{filterid}", method = RequestMethod.GET)
    @RequiresPermissions("filter:edit")
    public ModelAndView editFilters(@PathVariable("filterid") Long filterId) {
        log.info("FilterController.editFilters(): FilterId={}", filterId);
        Map<String, Object> filterPage = new HashMap<String, Object>();
        TagFilter filter = FilterService.getFilter(filterId);
        filterPage.put("tagCategoryList", FilterService.getAllTagCategories());
        filterPage.put("filter", filter);
        filterPage.put("type", "edit");
        return new ModelAndView("filterRule", "filterPage", filterPage);
    }
    
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    @RequiresPermissions("filter:edit")
    public void generateFilter(@RequestParam(value = "ids", required = false, defaultValue = "") String ids, HttpServletResponse response)
            throws IOException {
        log.info("FilterController.generateFilter()");
        List<Design> designs = designService.getDesignList(ids);
        List<Customization> customization = new ArrayList<Customization>();
        for (Design design : designs) {
            try {
                customization.add(customizationService.getBriefCustomizationByDesignId(design.getId(), false, Customization.DEFAULT_PRIORITY));
            } catch (Exception e) {
                log.error("Error {} fetching customization ", e.getMessage(), e);
            }
        }
        response.getWriter().write(FilterService.generateFilterTags(customization));
    }
    
    @RequestMapping(value = "/delete/{filterid}", method = RequestMethod.GET)
    public ModelAndView deleteFilters(@PathVariable("filterid") Long filterId) {
        log.info("FilterController.deleteFilters()");
        Map<String, Object> filterPage = new HashMap<String, Object>();
        try {
            FilterService.deleteFilterRule(filterId);
        } catch (Exception e) {
            log.error("FilterController.deleteFilters(): Error {}", e.getMessage(), e);
            filterPage.put("message", "Error occurred while performing add/update");
        }
        List<TagFilter> filterList = FilterService.getFilterList();
        filterPage.put("filterList", filterList);
        return new ModelAndView("filterList", "filterPage", filterPage);
    }
}
