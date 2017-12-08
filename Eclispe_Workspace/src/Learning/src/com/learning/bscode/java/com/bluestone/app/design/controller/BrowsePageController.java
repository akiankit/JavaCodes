package com.bluestone.app.design.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.design.service.DesignCategoryService;
import com.bluestone.app.design.service.ReadyToShipService;
import com.bluestone.app.search.filter.FilterPageService;

@Controller
@RequestMapping(value = {"/refinesearch*", "/jewellery*", "/jewellery.html*"})
public class BrowsePageController extends SortByController {

	private static final Logger log = LoggerFactory.getLogger(BrowsePageController.class);

	@Autowired
	private CustomizationService customizationService;

	@Autowired
	private FilterPageService filterPageService;

	@Autowired
	private DesignCategoryService designCategoryService;

	@Autowired
	private ReadyToShipService readyToShipService;

	@RequestMapping(value = "/{tags}", method = RequestMethod.GET)
	public ModelAndView showBrowsePage(
			@RequestParam(value = Constants.FETCH_FILTER_DATA_PARAM, required = false, defaultValue = "false") String fetchFilterData,
			@RequestParam(value = Constants.PAGE_NUMBER_PARAM, required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = Constants.SORTBY, required = false, defaultValue = "") String sortBy,
			@RequestParam(value = Constants.PREFERED_PRODUCTS, required = false, defaultValue = "") String preferredProducts,
			@PathVariable("tags") String tags, HttpServletRequest request,HttpSession session) throws Exception {
		try {
			pageNumber = pageNumber < 0 ? 1 : pageNumber;
			sortBy = getSortBy(sortBy,session);
			List<String> taglist = Arrays.asList(LinkUtils.splitTags(tags));
			String parentCusotmizationId = request.getParameter("parentId");
			String urlQueryString = "";
			if (parentCusotmizationId != null) {
				urlQueryString = "?parentId=" + parentCusotmizationId;
			}
			if (fetchFilterData.compareToIgnoreCase("true") == 0) {
				Map<String, Object> filters = filterPageService.getFiltersData(taglist, urlQueryString);
				return new ModelAndView("filters", "viewData", filters);
			} else {
				boolean isChildTaginTags = customizationService.isChildTagsInTags(tags);
				if (isChildTaginTags && (urlQueryString == null || !urlQueryString.contains("parentId"))) {
					return showBrowsePage("0",fetchFilterData, pageNumber, sortBy, "",request,session);
				} else {
					Map<Object, Object> customizationDetails = new HashMap<Object, Object>();
					String queryString = request.getQueryString();
					if (tags.equalsIgnoreCase("3-day-delivery")) {
						customizationDetails = readyToShipService.getReadyToShipProductPageDetails();
						customizationDetails.put("readytoship", "true");
					} else{
                		if(preferredProducts == null){
                        	preferredProducts = "";
                        }
						customizationDetails = customizationService.getCustomizationDetails(pageNumber, taglist,sortBy, request.getQueryString(), tags, preferredProducts);
	                    customizationDetails.put("readytoship", "false");
                	}
					String[] selectedTags = LinkUtils.splitTags(tags);
					customizationDetails.put("search", "false");
					customizationDetails.put("sortBy", sortBy);
					customizationDetails.put("pageType", "browse");
					customizationDetails.put("requestURL", request.getRequestURL());
					customizationDetails.put("selectedTags", selectedTags);
					customizationDetails.put("selectedCategoryId", getSelectedCategoryId(selectedTags));
					if (queryString != null && queryString.toLowerCase().contains("scroll")) {
						customizationDetails.put("columnOnly", "true");
						return new ModelAndView("jewelleryColumns", "viewData", customizationDetails);
					} else {
						return new ModelAndView("jewellery", "viewData", customizationDetails);
					}
				}
			}
		} catch (Exception e) {
			log.error("Error while fetching data Error {} ", e);
			throw e;
		}
	}

	private int getSelectedCategoryId(String[] selectedTags) {
		List<DesignCategory> designCategoryFromList = designCategoryService.getDesignCategoryFromList(Arrays
				.asList(selectedTags));
		if (!designCategoryFromList.isEmpty()) {
			return (int) designCategoryFromList.get(0).getId();
		}
		return 0;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showBrowsePage(
			@RequestParam(value = Constants.SEARCHED_STRING, required = false, defaultValue = "0") String searchedString,
			@RequestParam(value = Constants.FETCH_FILTER_DATA_PARAM, required = false, defaultValue = "false") String fetchFilterData,
			@RequestParam(value = Constants.PAGE_NUMBER_PARAM, required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = Constants.SORTBY, required = false, defaultValue = "") String sortBy,
			@RequestParam(value = Constants.PREFERED_PRODUCTS, required = false, defaultValue = "") String preferredProducts,
			HttpServletRequest request,HttpSession session) throws Exception {
		try {
			sortBy = getSortBy(sortBy,session);
			pageNumber = pageNumber < 0 ? 1 : pageNumber;
			List<String> taglist = Collections.emptyList();
			if (fetchFilterData.compareToIgnoreCase("true") == 0) {
				String urlQueryString = "";
				Map<String, Object> filters = filterPageService.getFiltersData(taglist, urlQueryString);
				return new ModelAndView("filters", "viewData", filters);
			} else {
				String urlQueryString = request.getQueryString();;
				if(preferredProducts == null){
                	preferredProducts = "";
                }
				Map customizationDetails = customizationService.getCustomizationDetails(pageNumber, taglist, sortBy,urlQueryString, "",preferredProducts);
				if (searchedString == null) {
					searchedString = "";
				}
				if (urlQueryString != null && urlQueryString.contains(Constants.SEARCHED_STRING + "=")) {
					customizationDetails.put("searchedStringParamPresent", "1");
					customizationDetails.put("searchedString", searchedString);
				}
				customizationDetails.put("sortBy", sortBy);
				customizationDetails.put("readytoship", "false");
				customizationDetails.put("search", "false");
				customizationDetails.put("requestURL", request.getRequestURL());
				customizationDetails.put("pageType", "browse");
				if (urlQueryString != null && urlQueryString.toLowerCase().contains("scroll")) {
					customizationDetails.put("columnOnly", "true");
					return new ModelAndView("jewelleryColumns", "viewData", customizationDetails);
				} else {
					return new ModelAndView("jewellery", "viewData", customizationDetails);
				}
			}
		} catch (Exception e) {
			log.error("Error while fetching filters Error {} ", e);
			throw e;
		}
	}
}
