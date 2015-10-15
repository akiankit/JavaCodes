package com.bluestone.app.error.controller;

import org.apache.shiro.util.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/errors*")
public class HTTPErrorController {
/* Sources : 
 * 1. http://jira.grails.org/browse/GPSHIRO-15?page=com.atlassian.jira.plugin.system.issuetabpanels:all-tabpanel
 * 2. http://stackoverflow.com/questions/1196569/custom-404-using-spring-dispatcherservlet 
 */
    @Autowired
    ApplicationContext applicationContext;
    
    @RequestMapping(value = "/404*", method = RequestMethod.GET)
    public ModelAndView showPageNotFound() {
    	
    	Object securityManager = ThreadContext.get(ThreadContext.SECURITY_MANAGER_KEY);
    			if (securityManager == null) {
    		 		securityManager = applicationContext.getBean("securityManager");
    		 		ThreadContext.put(ThreadContext.SECURITY_MANAGER_KEY, securityManager);
    			}
    	
        return new ModelAndView("page_404");
    }
}
