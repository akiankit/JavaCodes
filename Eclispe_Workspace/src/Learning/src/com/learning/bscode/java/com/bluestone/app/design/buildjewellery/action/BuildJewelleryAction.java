package com.bluestone.app.design.buildjewellery.action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.ProductPageService;
import com.bluestone.app.design.service.ProductService;
import com.bluestone.app.design.solitaire.Solitaire;
import com.bluestone.app.design.solitaire.SolitaireService;
import com.bluestone.app.design.solitairemount.SolitaireMountService;

@Component
public class BuildJewelleryAction extends MultiAction {
    
    private static final Logger log = LoggerFactory.getLogger(BuildJewelleryAction.class);
    
    @Autowired 
    private SolitaireService solitaireService;
    
    @Autowired 
    private CustomizationService customizationService;
    
    @Autowired 
    private  SolitaireMountService solitaireMountService;
    
    @Autowired
    private ProductPageService productPageService;
    
    @Autowired 
    private ProductService productService;

    public Event getSolitaireMetaData(RequestContext requestContext) throws Exception {
        log.debug("BuildJewelleryAction.getSolitaireMetaData()");
        try {
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            HttpServletRequest nativeRequest = (HttpServletRequest) requestContext.getExternalContext().getNativeRequest();
            
            String category = getDesignCategory(flowScope);
            HttpSession session = nativeRequest.getSession();
            session.setAttribute("buildFlowCategory", category);
			Map<String, Object> responseData = solitaireService.getSolitareFilterData(session,"solitairelist"+category.toLowerCase(),true);
            
            for (String eachKey : responseData.keySet()) {
                flowScope.put(eachKey, responseData.get(eachKey));
            }
            setSummaryDetails(flowScope);
            return success();
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while executing getSolitaireMetaData ",e); 
            }
            return error();
        }
    }

	private String getDesignCategory(MutableAttributeMap flowScope) {
		String category = (String) flowScope.get("category");
		return category;
	}
    
    public Event getSolitaireDetails(RequestContext requestContext) throws Exception {
        log.debug("BuildJewelleryAction.getSolitaireDetails()");
        try {
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            HttpServletRequest nativeRequest = (HttpServletRequest) requestContext.getExternalContext().getNativeRequest();
            String solitaireId = nativeRequest.getParameter("id");
            Solitaire product = (Solitaire) productService.getProduct(Long.parseLong(solitaireId));
            String designCategory = getDesignCategory(flowScope);
            List<Customization> solitaireMountList =  solitaireMountService.getSolitaireMountListing(designCategory, product.getCarat(),product.getCut());
            if(solitaireMountList==null || solitaireMountList.isEmpty()){
            	flowScope.put("disableAdd", true);
            }else{
            	flowScope.put("disableAdd", false);
            }
            flowScope.put("solitaire", product);
            setSummaryDetails(flowScope);
            return success();
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while executing getSolitaireMetaData ",e); 
            }
            return error();
        }
    }
    
    public Event addSolitare(RequestContext requestContext) throws Exception {
        log.debug("BuildJewelleryAction.getSolitaireDetails()");
        try {
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            HttpServletRequest nativeRequest = (HttpServletRequest) requestContext.getExternalContext().getNativeRequest();

            String soliataireProductId = nativeRequest.getParameter("solitaireProductId");
            if(StringUtils.isNotBlank(soliataireProductId)) {
                Product product = productService.getProduct(Long.parseLong(soliataireProductId));
                if(product != null) {
                    flowScope.put("selectedSolitaire", product);
                    if(flowScope.get("selectedStyle") != null){
                    	flowScope.remove("selectedStyle");
                    }
                } else {
                    throw new Exception("Solitaire not found for id :" + soliataireProductId);
                }
            } else {
                throw new Exception("Solitaire not found for id :" + soliataireProductId);
            }
            setSummaryDetails(flowScope);
            return success();
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while adding solitaire ",e); 
            }
            return error();
        }
    }
    
    public Event getDesignList(RequestContext requestContext) throws Exception {
        log.debug("BuildJewelleryAction.getSolitaireDetails()");
        try {
        	MutableAttributeMap flowScope = requestContext.getFlowScope();
            MutableAttributeMap flashScope = requestContext.getFlashScope();
            Solitaire selectedSolitaire = (Solitaire) flowScope.get("selectedSolitaire");
            String category = getDesignCategory(flowScope);
            if(selectedSolitaire != null) {
                Map<Object, Object> customizationDetails = new HashMap<Object, Object>();
                List<Customization> solitaireMountList =  solitaireMountService.getSolitaireMountListing(category, selectedSolitaire.getCarat(),selectedSolitaire.getCut());
                customizationDetails.put("customizationList", solitaireMountList);

/*              customizationDetails.put("readytoship", "true");
                customizationDetails.put("search", "false");
                customizationDetails.put("pageType", "browse");
*/                flashScope.put("viewData", customizationDetails);
            }
            setSummaryDetails(flowScope);
            return success();
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while executing getDesignList ",e); 
            }
            return error();
        }
    }
    
    public Event getStyleDetails(RequestContext requestContext) throws Exception {
        log.debug("BuildJewelleryAction.getStyleDetails()");
        try {
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            HttpServletRequest nativeRequest = (HttpServletRequest) requestContext.getExternalContext().getNativeRequest();
            String designId = nativeRequest.getParameter("id");
            Customization customization = customizationService.getBriefCustomizationByDesignId(Long.parseLong(designId), false, Customization.DEFAULT_PRIORITY);
            Map<String, Object> productPageDetails = new HashMap<String, Object>();
            if (customization != null) {
                productPageDetails = productPageService.getProductPageDetails(customization.getDesign().getId(), customization, "", false);
                productPageDetails.put("customization", customization);
            }
            flowScope.put("viewData", productPageDetails);
            setSummaryDetails(flowScope);
            return success();
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while getting design list",e); 
            }
            return error();
        }
    }
    
    public Event addStyle(RequestContext requestContext) throws Exception {
        log.debug("BuildJewelleryAction.addStyle()");
        try {
            MutableAttributeMap flowScope = requestContext.getFlowScope();
            HttpServletRequest nativeRequest = (HttpServletRequest) requestContext.getExternalContext().getNativeRequest();

            String customizationId = nativeRequest.getParameter("customizationId");
            String size = nativeRequest.getParameter("chainselect");
            if(StringUtils.isNotBlank(customizationId)) {
                Product product = customizationService.getBriefCustomizationByCustomizationId(Long.parseLong(customizationId), false);
                if(product != null) {
                    flowScope.put("selectedStyle", product);
                    flowScope.put("selectedSize", size);
                } else {
                    throw new Exception("Style not found for id :" + customizationId);
                }
            } else {
                throw new Exception("Style not found for id :" + customizationId);
            }
            setSummaryDetails(flowScope);
            return success();
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while adding style",e); 
            }
            return error();
        }
    }
    
    private void setSummaryDetails(MutableAttributeMap flowScope){
    	 Product selectedStyle =(Product)flowScope.get("selectedStyle");
    	 Product selectedSolitaire = (Product)flowScope.get("selectedSolitaire");
    	 BigDecimal totalPrice = BigDecimal.ZERO;
    	 if((selectedStyle != null) || (selectedSolitaire != null)){
 			flowScope.put("showSummary", true);
 		}else{
 			flowScope.put("showSummary", false);
 		}
    	if(selectedStyle != null){
    		totalPrice = totalPrice.add(selectedStyle.getPrice());
    	}
    	if(selectedSolitaire != null){
    		totalPrice = totalPrice.add(selectedSolitaire.getPrice());
    	}
    	flowScope.put("totalPrice", NumberUtil.formatPriceIndian(totalPrice, ","));
    }
}
