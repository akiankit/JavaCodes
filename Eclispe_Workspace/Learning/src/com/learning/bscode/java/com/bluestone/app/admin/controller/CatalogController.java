package com.bluestone.app.admin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.CatalogService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.ProductUploadService;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.DesignService;

@Controller
@RequestMapping(value = { "/admin/catalog*" })
public class CatalogController {
    
    private static final Logger  log = LoggerFactory.getLogger(CatalogController.class);
    
    @Autowired
    private CustomizationService customizationService;
    
    @Autowired
    private ProductUploadService uploadService;

    @Autowired
    private DesignService designService;

    @Autowired
    private CatalogService catalogService;

    @RequestMapping(value = "/list*", method = RequestMethod.GET)
	@RequiresPermissions("catalog:view")
    public ModelAndView listCatalog(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) throws Exception {
        log.info("CatalogController.listCatalog()");
        Map<String, Object> catalogPage = new HashMap<String, Object>();
        Map<String, Object> catalogDetails = catalogService.getCatalogDetails(filterCriteria, false);
        ModelAndView catalogModel = new ModelAndView("catalogList");
        catalogModel.addAllObjects(catalogDetails);
        return catalogModel;
    }
    
    
    @RequestMapping(value = "/listCustomization/{designid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:view")
    public ModelAndView listCatalogCustomization(@PathVariable("designid") Long designId) throws Exception {
        log.info("CatalogController.listCustomizationCatalog()");
        //Design design = customizationService.findAny(Design.class, designId);
        Design design = designService.findByPrimaryKey(designId);

        ModelAndView catalogModel = new ModelAndView("customizationList");
        List<Customization> cutomizationList = design.getCustomizations();
        catalogModel.addObject("cutomizationList",cutomizationList);
        return catalogModel;
    }
    
    @RequestMapping(value = "/edit/design/{designid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:edit")
    public ModelAndView editDesign(@PathVariable("designid") Long designId) {
        log.info("CatalogController.editDesign(): Design={}", designId);
        Customization customization = customizationService.getDetailedCustomizationByDesignId(designId, true, Customization.DEFAULT_PRIORITY);
        customization.getDesign().setEdit((short)1);
        Map <String , Object> viewData = new HashMap<String, Object>();
        viewData.put("customization", customization);
        return new ModelAndView("design", "viewData", viewData);
        
    }
    
    @RequestMapping(value = "/view/design/{designid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:view")
    public ModelAndView viewDesign(@PathVariable("designid") Long designId) {
        log.info("CatalogController.viewDesign(): DesignId={}", designId);
        Customization customization = customizationService.getDetailedCustomizationByDesignId(designId, true, Customization.DEFAULT_PRIORITY);
        customization.getDesign().setEdit((short)0);
        Map <String , Object> viewData = new HashMap<String, Object>();
        viewData.put("customization", customization);
        return new ModelAndView("design", "viewData", viewData);
        
    }
    
    @RequestMapping(value = "/edit/customization/{customizationid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:edit")
    public ModelAndView editCustomization(@PathVariable("customizationid") Long customizationid) {
        Customization customization = customizationService.getDetailedCustomizationByCustomizationId(customizationid, true);
        customization.setEdit((short)1);
        Map <String , Object> viewData = new HashMap<String, Object>();
        viewData.put("customization", customization);
        return new ModelAndView("design", "viewData", viewData);
    }
    
    @RequestMapping(value = "/view/customization/{customizationid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:view")
    public ModelAndView viewCustomization(@PathVariable("customizationid") Long customizationid) {
        Customization customization = customizationService.getDetailedCustomizationByCustomizationId(customizationid, true);
        customization.setEdit((short)0);
        Map <String , Object> viewData = new HashMap<String, Object>();
        viewData.put("customization", customization);
        return new ModelAndView("design", "viewData", viewData);
    }
    
    @RequestMapping(value = "/customization/update*", method = RequestMethod.POST)
	@RequiresPermissions("catalog:edit")
    public ModelAndView updateCustomization(MultipartHttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("design");
        long customizationId = Long.parseLong(request.getParameter("customizationId"));
        int i=1;
        Map<String, String> metalWeightsMap = new HashMap<String, String>();
        while(request.getParameter("metalWeight"+i)!=null){
            metalWeightsMap.put("metalWeight"+i, request.getParameter("metalWeight"+i));
            i=i+1;
        }
        try {
            Customization customization = uploadService.updateCustomization(customizationId, metalWeightsMap);
            customization = customizationService.getDetailedCustomizationByCustomizationId(customizationId, true);
            customization.setEdit((short)0);
            Map <String , Object> viewData = new HashMap<String, Object>();
            viewData.put("customization", customization);
            modelAndView.addObject("viewData", viewData);
        } catch (ProductUploadException e) {
            log.error("Error while updating customization error {} ", e.generateErrorMessage(), e);
            modelAndView.addObject("errors", e.getErrorList());
        }
        return modelAndView;
    }
    
    @RequestMapping(value = "/design/update*", method = RequestMethod.POST)
	@RequiresPermissions("catalog:edit")
    public ModelAndView updateDesign(MultipartHttpServletRequest request) {
        Map<String, Object> designInput = new HashMap<String, Object>();
        designInput.put("noOfStone", request.getParameterValues("noofStone[]"));
        designInput.put("stoneSize", request.getParameterValues("stoneSize[]"));
        designInput.put("stoneSetting", request.getParameterValues("stoneSetting[]"));
        designInput.put("stoneId", request.getParameterValues("stoneId[]"));
        designInput.put("custStoneId", request.getParameterValues("custStoneId[]"));
        designInput.put("designId", request.getParameter("designId"));
        designInput.put("designName", request.getParameter("designName"));
        designInput.put("designDesc", request.getParameter("designDesc"));
        designInput.put("tags", request.getParameter("tags"));
        designInput.put("property", request.getParameter("propertyUpdate"));
        Customization customization = customizationService.getDetailedCustomizationByDesignId(Long.parseLong((String) designInput.get("designId")), true, Customization.DEFAULT_PRIORITY);
        Map <String , Object> viewData = new HashMap<String, Object>();
        try {
			catalogService.updateDesign(designInput);
			customization = customizationService.getDetailedCustomizationByDesignId(Long.parseLong((String) designInput.get("designId")), true, Customization.DEFAULT_PRIORITY);
			customization.getDesign().setEdit((short)0);
		} catch (ProductUploadException e) {
			customization.getDesign().setEdit((short)1);
			log.error("Error occurred while executing Product Update{} ", e.generateErrorMessage(), e);
			viewData.put("uploadError", e.getErrorList());
		}
        
        viewData.put("customization", customization);
        return new ModelAndView("design", "viewData", viewData);
    }
    
    /*@RequestMapping(value = "/delete/design/{designid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:delete")
    public ModelAndView deleteDesign(@PathVariable("designid") Long designId,@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
        catalogService.deleteDesign(designId);
        Map<String, Object> catalogDetails = catalogService.getCatalogDetails(filterCriteria, false);
        ModelAndView catalogModel = new ModelAndView("catalogList");
        catalogModel.addAllObjects(catalogDetails);
        return catalogModel;
        
    }*/
    
    @RequestMapping(value = "/customizable/design/{designid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:edit")
    public ModelAndView setDesignCustomizable(@PathVariable("designid") Long designId) {
        ModelAndView catalogModel = new ModelAndView("catalogList");
        try {
            catalogService.setCustomizableFlag(designId);
        } catch (ProductUploadException e) {
            catalogModel.addObject("errors", e.getErrorList());
            log.error("Error while updating design customizable flag for design {}", designId, e);
        }
        Map<String, Object> catalogPage = new HashMap<String, Object>();
        Map<Object, Map<String, Object>> productList = catalogService.getCatalogList();
        catalogPage.put("productList", productList);
        catalogModel.addObject("catalogPage", catalogPage);
        return catalogModel;
    }
    
    @RequestMapping(value = "/status/design/{designid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:edit")
    public ModelAndView setDesignStatus(@PathVariable("designid") Long designId,@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
        ModelAndView catalogModel = new ModelAndView("catalogList");
        try {
            catalogService.setActiveFlag(designId);
        } catch (ProductUploadException e) {
            catalogModel.addObject("errors", e.getErrorList());
            log.error("Error while updating design active/inactive status for design {}", designId, e);
        }
        Map<String, Object> catalogDetails = catalogService.getCatalogDetails(filterCriteria, false);
        catalogModel.addAllObjects(catalogDetails);
        return catalogModel;
    }
    
    @RequestMapping(value = "/status/customization/{customizationid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:edit")
    public ModelAndView setCustomizationStatus(@PathVariable("customizationid") Long customizationid,@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
        ModelAndView catalogModel = new ModelAndView("catalogList");
        try {
            catalogService.setCustomizationActiveFlag(customizationid);
        } catch (ProductUploadException e) {
            catalogModel.addObject("errors", e.getErrorList());
        	log.error("Error while updating customization active/inactive status for customization {}", customizationid, e);
        }
        Map<String, Object> catalogDetails = catalogService.getCatalogDetails(filterCriteria, false);
        catalogModel.addAllObjects(catalogDetails);
        return catalogModel;
    }
    
    /*@RequestMapping(value = "/delete/customization/{customizationid}", method = RequestMethod.GET)
	@RequiresPermissions("catalog:delete")
    public ModelAndView deleteCustomization(@PathVariable("customizationid") Long customizationid,@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) throws Exception {
        try {
            catalogService.deleteCustomization(customizationid);
            Map<String, Object> catalogDetails = catalogService.getCatalogDetails(filterCriteria, false);
            ModelAndView catalogModel = new ModelAndView("catalogList");
            catalogModel.addAllObjects(catalogDetails);
            return catalogModel;
        } catch (Exception e) {
            log.error("Error while deleting customization {} ", customizationid,  e);
            throw e;
        }
        
    }*/
}
