package com.bluestone.app.core.servletfilters;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import com.bluestone.app.core.util.AppContext;

public class CustomOpenEntityManagerInViewFilter extends OpenEntityManagerInViewFilter {

    private static final Logger log = LoggerFactory.getLogger(CustomOpenEntityManagerInViewFilter.class);

    private String[] excludedUrlsArray = new String[0];

    private String[] fileExtensionWithDot = new String[0];

    /**
     * Subclasses may override this to perform custom initialization.
     * All bean properties of this filter will have been set before this
     * method is invoked.
     * <p>Note: This method will be called from standard filter initialization
     * as well as filter bean initialization in a Spring application context.
     * Filter name and ServletContext will be available in both cases.
     * <p>This default implementation is empty.
     *
     * @throws javax.servlet.ServletException if subclass initialization fails
     * @see #getFilterName()
     * @see #getServletContext()
     */
    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
        final FilterConfig filterConfig = getFilterConfig();
        getURLPatterns(filterConfig);
        getFileExtensions(filterConfig);
    }

    private void getFileExtensions(FilterConfig filterConfig) {
        String excludedFileExtensionsForLazyLoading = filterConfig.getInitParameter("excluded-file-extensions-for-lazy-loading");
        log.debug("CustomOpenEntityManagerInViewFilter.getFileExtensions(): excludedFileExtensionsForLazyLoading=[{}]", excludedFileExtensionsForLazyLoading);
        String[] excludedExtensionsArray = StringUtils.split(excludedFileExtensionsForLazyLoading, ',');
        fileExtensionWithDot = new String[excludedExtensionsArray.length];
        for (int i = 0; i < excludedExtensionsArray.length; i++) {
            String eachFileExt = excludedExtensionsArray[i];
            String dotExtension = "." + eachFileExt.trim();
            fileExtensionWithDot[i] = dotExtension;
            log.info("CustomOpenEntityManagerInViewFilter excluding : File extension=[{}]", dotExtension);
        }
    }

    private void getURLPatterns(FilterConfig filterConfig) {
        String excludedUrlPatternsForLazyLoading = filterConfig.getInitParameter("excluded-url-patterns-for-lazy-loading");
        log.debug("CustomOpenEntityManagerInViewFilter.getURLPatterns(): excludedUrlPatternsForLazyLoading=[{}]", excludedUrlPatternsForLazyLoading);

        if (StringUtils.isNotEmpty(excludedUrlPatternsForLazyLoading)) {
            String[] urls = StringUtils.split(excludedUrlPatternsForLazyLoading, ',');
            this.excludedUrlsArray = new String[urls.length];
            for (int i = 0; i < urls.length; i++) {
                String eachEntry = urls[i].trim();
                if (StringUtils.startsWith(eachEntry, "/")) {
                    excludedUrlsArray[i] = eachEntry;
                } else {
                    log.info("CustomOpenEntityManagerInViewFilter.getURLPatterns(): *** appending the leading '/' to {}", eachEntry);
                    excludedUrlsArray[i] = "/" + eachEntry;
                }

                log.info("CustomOpenEntityManagerInViewFilter excluding urls [{}]", excludedUrlsArray[i]);
            }
        }
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws javax.servlet.ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("CustomOpenEntityManagerInViewFilter.shouldNotFilter():[request.getRequestURI()]={}", requestURI);

        for (String eachUrl : excludedUrlsArray) {
            String urlWithContextPath = request.getContextPath() + eachUrl;
            if (StringUtils.startsWith(requestURI, urlWithContextPath)) {
                log.debug("Not opening session in view entity manager for for request uri [{}]:", requestURI);
                markAlreadyFiltered(request);
                return true;
            }
        }

        for (String dotExtension : fileExtensionWithDot) {
            if (StringUtils.endsWith(requestURI, dotExtension)) {
                log.debug("Not opening session in view entity manager for request uri [{}] file extension=[{}]:", requestURI, dotExtension);
                markAlreadyFiltered(request);
                request.setAttribute(AppContext.IGNORE_COOKIES_PARSING, true);
                return true;
            }
        }
        return false;
    }

    private void markAlreadyFiltered(HttpServletRequest request) {
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        log.debug("CustomOpenEntityManagerInViewFilter.markAlreadyFiltered(): {}", alreadyFilteredAttributeName);
        request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
    }
}
