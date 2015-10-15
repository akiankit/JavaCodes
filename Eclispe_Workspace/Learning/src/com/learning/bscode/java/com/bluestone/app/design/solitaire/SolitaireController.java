package com.bluestone.app.design.solitaire;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.ProductService;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

@Controller
@RequestMapping("/solitaire*")
public class SolitaireController {
    
    private static final Logger log = LoggerFactory.getLogger(SolitaireController.class);
    
    private static List<String> shapesList = new ArrayList<String>();

	static {
		shapesList.add("round");
		shapesList.add("pear");
		shapesList.add("heart");
		shapesList.add("princess");
		shapesList.add("asscher");
		shapesList.add("marquise");
		shapesList.add("oval");
		shapesList.add("emerald");
		shapesList.add("radiant");
		shapesList.add("cushion");
	}
    
    @Autowired
    protected SolitaireService solitaireService;
    
    @Autowired
    protected ProductService    productService;
    
    @RequestMapping(value = "/list/{shape}", method = RequestMethod.GET)
    public ModelAndView getView(@PathVariable("shape") String search, HttpServletRequest httpServletRequest, HttpSession session) {
        Map<String, Object> responseData = solitaireService.getSolitareFilterData(session, "solitairelist",false);        
        ModelAndView modelAndView = new ModelAndView("solitaire");
        modelAndView.addAllObjects(responseData);
        return modelAndView;
    }
    
    @RequestMapping(value = "/list/{shape}/{pageType}", method = RequestMethod.GET)
    public ModelAndView getViewFromLanding(@PathVariable("shape") String search,@PathVariable("pageType") String pageType, HttpServletRequest httpServletRequest,HttpServletResponse response, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("solitaire");        
        String path = search+"-solitaires.html";
        
        //Reset Session Params         
        solitaireService.resetSolitaireSessionParameters(session, search,"solitairelist",false);
        
        try {        	
            response.sendRedirect(Util.getSiteUrlWithContextPath()+"/"+path);
        } catch (IOException ioe) {
            if (log.isErrorEnabled()) {
                log.error("Error while redirecting to " + path + "from solitaire landing page" + ioe.getMessage(), ioe);
            }
        }
        return modelAndView;
    }
    
    @RequestMapping(value = "/filter/{sessionKey}", method = RequestMethod.GET)
    public @ResponseBody String getAll(@PathVariable("sessionKey") String sessionKey, HttpServletRequest httpServletRequest, HttpSession session) {
        Map<String, List<String>> searchData = new HashMap<String, List<String>>();
        Map<String, Number[]> rangeData = new HashMap<String, Number[]>();
        String offset = httpServletRequest.getParameter("iDisplayStart");
        String limit = httpServletRequest.getParameter("iDisplayLength");
        String sortColumnIndex = httpServletRequest.getParameter("iSortCol_0");
        String sortColumnDirection = httpServletRequest.getParameter("sSortDir_0");
        
        String sortColumnDbName = "";                
                
        Map<String,Map<String,String>> solitairePropsMap = new HashMap<String, Map<String,String>>();
        Map<String,String> sliderPropsMap = new HashMap<String, String>();
        Map<String,String> nonsliderPropsMap = new HashMap<String, String>();
        SolitaireColumnEnum[] values = SolitaireColumnEnum.values();
        Map<String, SolitaireColumnMetaData> solitaireColumnMetaDatas = null;
        if(sessionKey.equalsIgnoreCase("solitairelist")){
        	solitaireColumnMetaDatas = solitaireService.getColumnMetaData(false,session, sessionKey);
        }else{
        	solitaireColumnMetaDatas = solitaireService.getColumnMetaData(true,session, sessionKey);
        }               
        for (SolitaireColumnEnum solitaireColumnEnum : values) {
            String searchParameter = httpServletRequest.getParameter("sSearch_" + solitaireColumnEnum.getColumnIndex());            
            String dbColumnName = solitaireColumnEnum.getDbColumnName();
			if (StringUtils.isNotBlank(searchParameter)) {
                String[] allSearchValues = StringUtils.split(searchParameter, ",");
                if (solitaireColumnEnum.isSliderFilter()) {
                    BigDecimal[] minAndMax = new BigDecimal[2];
                    minAndMax[0] = new BigDecimal(allSearchValues[0]);
                    minAndMax[1] = new BigDecimal(allSearchValues[1]);
                    sliderPropsMap.put(dbColumnName + "dataindex0", allSearchValues[0]);
                    sliderPropsMap.put(dbColumnName + "dataindex1", allSearchValues[1]);                    
                    rangeData.put(dbColumnName, minAndMax);
                } else {                	
                    searchData.put(dbColumnName, Arrays.asList(allSearchValues));                                    
                    if(!solitaireColumnEnum.isSliderFilter()){                    	
                    	nonsliderPropsMap.put(dbColumnName, StringUtils.strip(searchParameter,","));
                    }
                }
            }else{            	
                if (solitaireColumnEnum.isSliderFilter()) {                	                    
                	sliderPropsMap.put(dbColumnName + "dataindex0", solitaireColumnMetaDatas.get(dbColumnName).getMinRange().toPlainString());
                	sliderPropsMap.put(dbColumnName + "dataindex1", solitaireColumnMetaDatas.get(dbColumnName).getMaxRange().toPlainString());
                	BigDecimal[] minAndMax = new BigDecimal[2];
                    minAndMax[0] = solitaireColumnMetaDatas.get(dbColumnName).getMinRange();
                    minAndMax[1] = solitaireColumnMetaDatas.get(dbColumnName).getMaxRange();
                	rangeData.put(dbColumnName, minAndMax);
                } else {                    
                    nonsliderPropsMap.put(dbColumnName, "");
                }
            }
            if (solitaireColumnEnum.getColumnIndex() == Integer.parseInt(sortColumnIndex)) {
                sortColumnDbName = dbColumnName;
            }
        }        
        solitairePropsMap.put("sliderProps", sliderPropsMap);
        solitairePropsMap.put("nonSliderProps", nonsliderPropsMap);
        session.setAttribute(sessionKey, solitairePropsMap);        
        List<Solitaire> solitaireList = solitaireService.getSolitaireList(offset, limit, sortColumnDbName, sortColumnDirection, searchData, rangeData);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("sEcho", httpServletRequest.getParameter("sEcho"));
        
        long count = solitaireService.getSolitaireListCount(searchData, rangeData);
                
        result.put("iTotalRecords", count);
        result.put("iTotalDisplayRecords", count);
        result.put("aaData", solitaireList);
        return new Gson().toJson(result);
    }
    
    @RequestMapping(value = "/detail/{itemId}", method = RequestMethod.GET)
    public ModelAndView getSolitaireDetails(@PathVariable("itemId") String itemId, HttpServletRequest httpServletRequest) {
        Solitaire solitaire = solitaireService.getByItemId(itemId);
        return new ModelAndView("solitaireDetails", "solitaire", solitaire);
    }
    
    @RequestMapping(value = "/{productId}", method = RequestMethod.GET)
    public ModelAndView getSolitaireDetails(@PathVariable("productId") long productId, HttpServletRequest httpServletRequest,HttpSession session) {        
    	ModelAndView modelAndView = new ModelAndView("solitaireDetails");
    	Product product = productService.getProduct(productId);
    	String solitaireBreadcrumbURL = Util.getSiteUrlWithContextPath()+"/round-solitaires.html";
        String httpReferrer = httpServletRequest.getHeader("referer");
        if(StringUtils.isNotBlank(httpReferrer)){
        	try {
        		String requestUrl = httpServletRequest.getRequestURL().toString();
				URL referrerURL = new URL(httpReferrer);
				URL requestURL = new URL(requestUrl);
				 if (referrerURL.getHost().equals(requestURL.getHost())) {// If navigating within our site
					 //Explicity not checking for error conditions as it will be caught in the exception 
					 String path = StringUtils.substringAfterLast(referrerURL.getPath(), "/");						 						
					 String[] params = StringUtils.split(path, "-");					 
					 if(shapesList.contains(params[0])){
						 solitaireBreadcrumbURL = Util.getSiteUrlWithContextPath()+"/"+params[0]+"-solitaires.html";
					 }else{
						 session.setAttribute("solitairePriority", "landing");
					 }
				 }else{
					 session.setAttribute("solitairePriority", "landing");
				 }
			} catch (MalformedURLException e) {
				session.setAttribute("solitairePriority", "landing");
				log.error("Solitare Detail Page - Malformed Referrer URL - Referrer URL=[{}] Cause={} ",
                        httpReferrer, e.getMessage(), Throwables.getRootCause(e));
			} catch (Exception e) {
				session.setAttribute("solitairePriority", "landing");
				log.error("Solitare Detail Page - Error Parsing Referrer URL for shape - Referrer URL=[{}] Cause={} ",
                        httpReferrer, e.getMessage(), Throwables.getRootCause(e));
			}
        }        
        modelAndView.addObject("solitaire", product);
        modelAndView.addObject("solitaireListURL", solitaireBreadcrumbURL);
        return modelAndView; 
    }
    
    @RequestMapping(value = "/internal/{productId}", method = RequestMethod.GET)
    public ModelAndView getInternalSolitaireDetails(@PathVariable("productId") long productId, HttpServletRequest httpServletRequest) {
        Product product = productService.getProductWithoutActiveFilter(productId);
        return new ModelAndView("internalSolitaireDetails", "solitaire", product);
    }
    
    @RequestMapping(value = "/advanced-checks*", method = RequestMethod.POST)
    public ModelAndView saveAdvanceCriteriaChecks(HttpServletRequest httpServletRequest,HttpSession session) {        
        String checkName = httpServletRequest.getParameter("checkName");
        String checked = httpServletRequest.getParameter("checked");        
        checked = Boolean.valueOf(checked)?"true":"";
        session.setAttribute(checkName, checked);
        return null;
    }
}
