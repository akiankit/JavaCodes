package com.bluestone.app.design.controller;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.Constants;

@Component
public abstract class SortByController {
	 
	final String DEFAULT_SORTING_ORDER=ApplicationProperties.getProperty("browsepage.defaultSortingOrder");

    private static final Logger log = LoggerFactory.getLogger(SortByController.class);

    @PostConstruct()
    private void init(){
        log.debug("SortByController.init()");
        Assert.isTrue(StringUtils.isNotBlank(DEFAULT_SORTING_ORDER), "Property [browsepage.defaultSortingOrder] should not be empty.");
    }
    
	 protected String getSortBy(String sortBy, HttpSession session) {
			String sortByFromSession = (String) session.getAttribute("sortby");
			if (StringUtils.isBlank(sortBy)) {
				if(sortByFromSession == null){
					sortBy = DEFAULT_SORTING_ORDER;
				}else{
					sortBy = sortByFromSession;
				}
			} else if (Constants.DATE_ADD.equalsIgnoreCase(sortBy) || Constants.PRICEHIGH.equalsIgnoreCase(sortBy)
					|| Constants.PRICELOW.equalsIgnoreCase(sortBy) || Constants.MOSTPOPULAR.equalsIgnoreCase(sortBy)) {
				// do nothing , take value as it is
			} else {
				// this is done as we only support these three sortBy values. If
				// anything else than these shows up , default to most popular
				sortBy = DEFAULT_SORTING_ORDER;
			}
			session.setAttribute("sortby", sortBy);
			return sortBy;
		}
}
