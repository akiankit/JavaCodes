package com.bluestone.app.design.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.ProductPageService;
import com.bluestone.app.design.service.ProductService;
import com.bluestone.app.design.service.SizeBasedPricingService;
import com.google.gson.Gson;


@Controller
@RequestMapping(value={"/design*"})
public class CustomizationController {

	private static final String DESIGN_VIEW_NAME = "design";

    private static final Logger log = LoggerFactory.getLogger(CustomizationController.class);	
	
	@Autowired
	private CustomizationService customizationService;
	
	@Autowired
    private ProductService productService;
	
    @Autowired
    private SizeBasedPricingService sizeBasedPricingService;
    
    @Autowired
    private ProductPageService productPageService;
	
	@RequestMapping(value="/{designid}", method = RequestMethod.GET)
	public ModelAndView showDesigns(@PathVariable("designid") Long designId,HttpServletRequest request,HttpServletResponse httpServletResponse) {
		Customization customization = customizationService.getDetailedCustomizationByDesignId(designId, false, Customization.DEFAULT_PRIORITY);
		return getProductPageView(designId, request, customization,httpServletResponse);
	}

    private ModelAndView getProductPageView(Long designId, HttpServletRequest request, Customization customization, HttpServletResponse response) {
        if(customization == null) {
            try {
                response.sendError(404);
                // need to do this , as not sure what happens if null is returned from controller
                return new ModelAndView("page_404");
            } catch (IOException e) {
            	log.error("IOError while sending error response", e);
            	// need to do this , as not sure what happens if null is returned from controller
                return new ModelAndView("page_404"); 
            }
        } else {
            long parentId = 0;
            String parentCusotmizationId = request.getParameter("parentId");
            String urlQueryString = "";
            if(parentCusotmizationId!= null){
                parentId = Long.valueOf(parentCusotmizationId);
                urlQueryString = "?parentId="+parentCusotmizationId;
            }
            
            Map<String, Object> productPageDetails = productPageService.getProductPageDetails(designId, customization, urlQueryString, true);
            productPageDetails.put("customization", customization);
            productPageDetails.put("parentId", parentId);
            setProductInRecentlyViewedList(designId);
            
            ModelAndView modelAndView = new ModelAndView(DESIGN_VIEW_NAME);
            modelAndView.addObject("formActionUrl", Util.getContextPath() + "/order");
            modelAndView.addObject("viewData", productPageDetails);
            return modelAndView;
        }
    }

    private void setProductInRecentlyViewedList(Long designId) {
        Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		List<Long> list = (List<Long>) session.getAttribute("viewed");
		log.debug("Adding design {} to recently viewd list",designId);
		if(list !=null && list.contains(designId)){
			list.remove(designId);
			list.add(0,designId);
		} else {
			if(list !=null && list.size() < Constants.RECENTLY_VIEWED_LIST_SIZE ){
				list.add(designId);
			}else{
				if(list == null){
					list = new ArrayList<Long>();
					list.add(designId);
				}else{
					list.add(0, designId);
					if(list.size()>=Constants.RECENTLY_VIEWED_LIST_SIZE)
					    list.remove(Constants.RECENTLY_VIEWED_LIST_SIZE);
				}
			}
		}
		session.setAttribute("viewed", list);
    }

	@RequestMapping(value="/{designid}/{designName}", method = RequestMethod.GET)
    public ModelAndView showDesignsWithName(@PathVariable("designid") Long designId,
                                            @PathVariable("designName") String designName,
                                            HttpServletRequest request,
                                            HttpServletResponse httpServletResponse) {
	    Customization customization = customizationService.getDetailedCustomizationByDesignId(designId, false, Customization.DEFAULT_PRIORITY);
	    if(customization != null) {
	        String replacedDesignName = designName.replace("-", " ");
	        String actualDesignName = customization.getDesign().getDesignName();
	        String replacedActualDesignName = actualDesignName.replace("-", " ").replace('\'', ' ');
	        log.debug("comparing design names after replacement  {} {}", replacedActualDesignName, replacedDesignName);
	        
            if(!replacedDesignName.equalsIgnoreCase(replacedActualDesignName)) {
                StringBuilder redirectUrl = new StringBuilder(LinkUtils.getSiteLink(customization.getUrl()));
                String queryString = request.getQueryString();
                if(queryString != null) {
                    redirectUrl = redirectUrl.append("?").append(queryString);
                }
                
                log.info("Design Name {} for design id {} doesnt match the name in url {}. redirecting it to {}", actualDesignName, designId, designName, redirectUrl);
                RedirectView redirectView = new RedirectView(redirectUrl.toString());
                redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                return new ModelAndView(redirectView); // TODO append any query params if exists
	        }
	    } 

	    return getProductPageView(designId, request, customization, httpServletResponse);
    }
	
	@RequestMapping(value="/getcustomization", method = RequestMethod.GET)
	public @ResponseBody String getCustomization(HttpServletRequest request) {
	    Map<String, Object> result = new HashMap<String, Object>();
		String customizationIdAsString = request.getParameter("customizationId");
		long customizationId = 0l;
        try {
            customizationId = Long.valueOf(customizationIdAsString);
        } catch (NumberFormatException e) {
            log.error("CustomizationController.getCustomization() customization id {} in request params in not a number", customizationIdAsString);
            result.put("isError", true);
            return new Gson().toJson(result);
        }
        
		Customization newCustomization = customizationService.getBriefCustomizationByCustomizationId(customizationId, false);
        BigDecimal price = newCustomization.getPrice();
        BigDecimal emi = price.divide(BigDecimal.valueOf(6.0),RoundingMode.UP);

        result.put("customizationId", newCustomization.getId());
        result.put("productCode", newCustomization.getCode());
        result.put("shortDescription", newCustomization.getShortDescription());
        result.put("price", NumberUtil.formatPriceIndian(price, ","));
        result.put("emiPrice", NumberUtil.formatPriceIndian(emi.doubleValue(), ","));
        result.put("totalWeight", newCustomization.getTotalWeightOfProduct());
        result.put("metalWeight", newCustomization.getCustomizationMetalSpecification().get(0).getMetalWeight());
        
        result.put("weightAndPriceDetails",sizeBasedPricingService.getPriceForSizes(newCustomization));
        
        result.put("expectedDeliveryDate", productService.getFormattedExpectedDeliveryDate(newCustomization));
        
        result.put("newCustomization", true);
        
        return new Gson().toJson(result);
	}
	
}

