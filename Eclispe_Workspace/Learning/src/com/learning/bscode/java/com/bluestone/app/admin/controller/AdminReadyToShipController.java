package com.bluestone.app.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.ReadyToShipConfigurationService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.model.ReadyToShip;
import com.bluestone.app.design.service.ReadyToShipSkuCache;

@Controller
@RequestMapping(value = {"/admin/readyToShip*"})
public class AdminReadyToShipController {

    private static final Logger log = LoggerFactory.getLogger(AdminReadyToShipController.class);

    @Autowired
    private ReadyToShipConfigurationService readyToShipConfigurationService;

    @Autowired
    private ReadyToShipSkuCache readyToShipSkuCache;

    @RequiresPermissions("ReadyToShip:view")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public ModelAndView getReadyToShipListPage(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria, HttpServletRequest request) {
        log.debug("AdminReadyToShipController.getReadyToShipListPage() ");
        Map<Object, Object> readyToShipDetails = getReadyToShipDetails(filterCriteria, request);
        return new ModelAndView("readyToShipList", "viewData", readyToShipDetails);
    }

    @RequiresPermissions("ReadyToShip:add")
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView getReadyToShipAddPage(HttpServletRequest request) {
        log.debug("AdminReadyToShipController.getReadyToShipAddPage() ");
        Map<String, Object> viewData = new HashMap<String, Object>();
        viewData.put("pageTitle", "Ready To Ship");
        return new ModelAndView("addReadyToShipSku", "viewData", viewData);
    }

    @RequiresPermissions("ReadyToShip:add")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView saveReadyToShipSku(@ModelAttribute("ReadyToShip") ReadyToShip readyToShip, HttpServletRequest request) {
        log.info("AdminReadyToShipController.saveReadyToShipSku() : [{}]", readyToShip);
        Map<Object, Object> readyToShipDetails = new HashMap<Object, Object>();
        readyToShipDetails.put("pageTitle", "Ready To Ship");
        String message = "SkuCode has been added successfully";
        try {
            readyToShip.setSkuCode(readyToShip.getSkuCode().trim());
            String validationMessage = readyToShipConfigurationService.validateReadyToShipSkuCode(readyToShip);
            if (StringUtils.isNotBlank(validationMessage)) {
                log.info("Could not add the ready to ship sku [{}] because {}", readyToShip.getSkuCode(), validationMessage);
                message = validationMessage;
                readyToShipDetails.put("message", message);
                return new ModelAndView("addReadyToShipSku", "viewData", readyToShipDetails);
            } else {
                log.info("Adding new ready to ship sku code=[{}]", readyToShip.getSkuCode());
                readyToShipSkuCache.put(readyToShip);
            }
            log.info("AdminReadyToShipController.saveReadyToShipSku(): Success for {}", readyToShip);
        } catch (Exception e) {
            message = e.getLocalizedMessage();
            log.error("Error during AdminReadyToShipController.saveReadyToShipSku: Reason:{}", e.toString(), e);
            return new ModelAndView("addReadyToShipSku", "viewData", readyToShipDetails);
        }
        readyToShipDetails = getReadyToShipDetails(request);
        readyToShipDetails.put("message", message);
        return new ModelAndView("readyToShipList", "viewData", readyToShipDetails);
    }

    @RequiresPermissions("ReadyToShip:delete")
    @RequestMapping(value = "/delete/{readyToShipId}", method = RequestMethod.POST)
    public
    @ResponseBody
    String deleteReadyToShipSku(@PathVariable("readyToShipId") Long readyToShipId, HttpServletRequest request) {
        log.info("AdminReadyToShipController.deleteReadyToShipSku(): readyToShipId=[{}]", readyToShipId);
        boolean hasError = false;
        String message = "Sku Code has been deleted successfully";
        ReadyToShip readyToShip = new ReadyToShip();
        try {
            readyToShip = readyToShipConfigurationService.find(readyToShipId);
            log.info("Deleting ready to ship with Skucode=[{}]", readyToShip.getSkuCode());
            readyToShipSkuCache.remove(readyToShip);
        } catch (Exception exception) {
            hasError = true;
            message = exception.getLocalizedMessage();
            log.error("Error during AdminReadyToShipController.deleteReadyToShipSku: ReadyToShipId=[{}] Sku=[{}] Reason:{}",
                      readyToShipId, readyToShip, exception.toString(), Throwables.getRootCause(exception));
        }
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("redirectUrl", Util.getSiteUrlWithContextPath() + "/admin/readyToShip/list");
        response.put(Constants.HAS_ERROR, hasError);
        response.put(Constants.MESSAGE, message);
        return new Gson().toJson(response);
    }

    private Map<Object, Object> getReadyToShipDetails(ListFilterCriteria filterCriteria, HttpServletRequest request) {
        Map<Object, Object> readyToShipDetails = new HashMap<Object, Object>();
        try {
            readyToShipDetails = readyToShipConfigurationService.getReadyToShipDetails(filterCriteria, true);
            readyToShipDetails.put("requestURL", request.getRequestURL());
        } catch (Exception exception) {
            log.error("Error during AdminReadyToShipController.getReadyToShipListPage: Reason:{}", exception.toString(), Throwables.getRootCause(exception));
        }
        readyToShipDetails.put("pageTitle", "Ready To Ship");
        return readyToShipDetails;
    }

    private Map<Object, Object> getReadyToShipDetails(HttpServletRequest request) {
        return getReadyToShipDetails(new ListFilterCriteria(), request);
    }
}
