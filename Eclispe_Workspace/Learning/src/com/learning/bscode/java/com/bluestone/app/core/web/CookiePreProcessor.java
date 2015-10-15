package com.bluestone.app.core.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.AppContext;

/**
 * @author Rahul Agrawal
 *         Date: 5/9/13
 */
class CookiePreProcessor {

    private static final Logger log = LoggerFactory.getLogger(CookiePreProcessor.class);


    static boolean shouldSkipCookieProcessing(HttpServletRequest request) {
        //Rahul Agrawal : As this happens for each request, let us try to optimise the logic as much as possible.
        final String requestURI = request.getRequestURI();

        Object ignoreCookies = request.getAttribute(AppContext.IGNORE_COOKIES_PARSING);
        if (ignoreCookies != null) {
            return true;
        }

        boolean isHealthCheckUrl = StringUtils.endsWith(requestURI, "/healthcheck");
        if (isHealthCheckUrl) {
            return true;
        }

        boolean isErrorPage = StringUtils.endsWith(requestURI, "/errors/404");
        if (isErrorPage) {
            return true;
        }

        boolean isResourceUrl = StringUtils.startsWith(requestURI, "/resources");
        if (isResourceUrl) {
            return true;
        }

        String queryString = request.getQueryString();
        boolean isAjaxRequest = StringUtils.contains(queryString, "ajax");
        if (isAjaxRequest) {
            log.debug("RequestInterceptor.shouldSkipCookieProcessing():for ajax requests: RequestUrl={} QueryString={}", queryString);
            return true;
        }

        boolean isFilterDataRequest = StringUtils.contains(queryString, "fetchFilterData");
        if (isFilterDataRequest) {
            log.debug("RequestInterceptor.shouldSkipCookieProcessing():for ajax requests: RequestUrl={} QueryString={}", queryString);
            return true;
        }

        return false;
    }
}
