package com.bluestone.app.search.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.SearchQuerySanitizer;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.controller.SortByController;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.search.filter.FilterPageService;
import com.bluestone.app.search.tag.model.TagCategory;

@Controller
@RequestMapping(value = { "/search*" })
public class SearchController extends SortByController {

	private static final Logger log = LoggerFactory.getLogger(SearchController.class);

	@Autowired
	private CustomizationService customizationService;

	@Autowired
	private FilterPageService filterPageService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView showBrowsePage(HttpServletResponse response,
			@RequestParam(value = Constants.PAGE_NUMBER_PARAM, required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = Constants.FETCH_FILTER_DATA_PARAM, required = false, defaultValue = "false") String fetchFilterData,
			@RequestParam(value = Constants.SORTBY, required = false, defaultValue = "") String sortBy,
			@RequestParam(value = Constants.SEARCH_QUERY, required = false, defaultValue = "") String searchQuery,
			@RequestParam(value = Constants.TAGS, required = false, defaultValue = "null") String tags, HttpServletRequest request,HttpSession session) {
		if ("null".equalsIgnoreCase(tags)) {
			tags = null;
		}
		String urlQueryString = request.getQueryString();
		try {
			sortBy = getSortBy(sortBy,session);
			pageNumber = pageNumber < 0 ? 1 : pageNumber;
			
			urlQueryString = customizationService.getNewURLQueryStringForBrowsePage(urlQueryString);
			Set<TagCategory> tagCategoryList = null;
			Map<Object, Object> customizationDetails = new HashMap<Object, Object>();
			if (tags == null) {
				customizationDetails = customizationService.getCustomizationDetailsFromSearchQuery(pageNumber, searchQuery, sortBy,
						urlQueryString, null);
				if (customizationDetails != null) {
					List<Customization> customizationTotalList = (List<Customization>) customizationDetails.get("customizationTotalList");
					List<String> taglist = Collections.emptyList();
					tagCategoryList = filterPageService.generateCategoryList(taglist, customizationTotalList, urlQueryString);
				}
			} else {
				customizationDetails = customizationService.getCustomizationDetailsFromSearchQuery(pageNumber, searchQuery, sortBy,
						urlQueryString, LinkUtils.splitTags(tags));
				if (customizationDetails != null) {
					List<Customization> customizationTotalList = (List<Customization>) customizationDetails.get("customizationTotalList");
					List<String> taglist = Arrays.asList(LinkUtils.splitTags(tags));
					tagCategoryList = filterPageService.generateCategoryList(taglist, customizationTotalList, urlQueryString);
				}
			}
			if (customizationDetails != null) {
				customizationDetails.put("tagCategoryList", filterPageService.getSortByCount(tagCategoryList));
				customizationDetails.put("selected", "true");
				customizationDetails.put("search", "true");
				customizationDetails.put("pageType", "browse");
				customizationDetails.put("requestURL", request.getRequestURL());
				customizationDetails.put("sortBy", sortBy);
			} else {
				redirectToDefaultBrowsePage(response, StringUtils.join(SearchQuerySanitizer.getCleanSearchTerms(searchQuery), ' '));
			}
			String queryString = request.getQueryString();
			if (queryString != null && queryString.toLowerCase().contains("scroll")) {
				customizationDetails.put("columnOnly", "true");
				return new ModelAndView("jewelleryColumns", "viewData", customizationDetails);
			} else {
				return new ModelAndView("jewellery", "viewData", customizationDetails);
			}
		} catch (Throwable e) {
			String redirectUrl = Util.getSiteUrlWithContextPath() + "/jewellery.html";
			log.error("Error while search query string  = {} redirecting to browse page url {} . Error {}",urlQueryString, redirectUrl,e);
			RedirectView redirectView = new RedirectView(redirectUrl);
			return new ModelAndView(redirectView);
		}
	}

	private void redirectToDefaultBrowsePage(HttpServletResponse response, String searchQuery) {
		String redirectUrl = Util.getSiteUrlWithContextPath() + "/jewellery.html?" + Constants.SEARCHED_STRING + "=" + searchQuery;
		try {
			response.sendRedirect(redirectUrl);
		} catch (IOException ioe) {
			/*
			 * if (log.isErrorEnabled()) {
			 * log.error("Error while redirecting to " + redirectUrl +
			 * "for no search results" + ioe.getMessage(), ioe); }
			 */
			log.error("Error while redirecting to [{}] for no search results. Error Details={} ", redirectUrl, ioe.getMessage(), ioe);
		}
	}
}
