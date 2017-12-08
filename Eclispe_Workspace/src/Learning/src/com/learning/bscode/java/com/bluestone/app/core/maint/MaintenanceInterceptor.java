package com.bluestone.app.core.maint;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.PropertyService;

/**
 * @author Rahul Agrawal
 *         Date: 2/10/13
 */

public class MaintenanceInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(MaintenanceInterceptor.class);

    private List<String> excludeURLList = new LinkedList<String>();

    final private String RESOURCE = "/resources";

    public static final String MAINTENANCE_MODE = "MAINTENANCE_MODE";
    public static final String MAINTENANCE_MODE_IPS = "MAINTENANCE_MODE_IPS";

    @Autowired
    private PropertyService propertyService;

    @PostConstruct
    void initialise() {
        excludeURLList.add("admin");
        excludeURLList.add("signin");
        excludeURLList.add("Login");
        log.info("MaintenanceInterceptor.initialise():{}", excludeURLList);
        log.info("MaintenanceInterceptor.initialise(): Maintenance Mode={}", isMaintenanceTurnedOn());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("MaintenanceInterceptor.preHandle()");
        boolean result = true;
        String queryString = request.getRequestURI();
        if (isMaintenanceTurnedOn()) {

        }

        return result;
    }

    boolean isMaintenanceTurnedOn() {
        boolean maintenanceMode = false;
        Property propertyValue = propertyService.getPropertyByName(MAINTENANCE_MODE);
        maintenanceMode = Boolean.valueOf(propertyValue.getValue());
        return maintenanceMode;
    }

    boolean isRequestForAdminPage(String queryStr) {
        for (String excludeURL : excludeURLList) {
            if (StringUtils.contains(queryStr, excludeURL)) {
                return true;
            }
        }
        return false;
    }

    boolean isRequestForResources(String query) {
        if (RESOURCE.equalsIgnoreCase(query)) {
            return true;
        } else {
            return false;
        }
    }

    void getAllowedIPAddresses() {
        Property allIpsProp = propertyService.getPropertyByName(MAINTENANCE_MODE_IPS);
        List<String> maintenanceIps = new LinkedList<String>();
        if (allIpsProp != null) {
            String[] values = StringUtils.isNotBlank(allIpsProp.getValue()) ? allIpsProp.getValue().split(",") : new String[0];
            maintenanceIps = Arrays.asList(values);
            log.info("RequestInterceptor.preHandle(): Allowed IP lists={}", Arrays.deepToString(values));
        }
    }

}
