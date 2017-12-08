package com.bluestone.app.core;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.core.service.TryAtHomeService;
import com.bluestone.app.core.util.Constants;

@Controller
@RequestMapping(value = "/tryAtHome")
public class TryAtHomeController {

    private static final Logger log = LoggerFactory.getLogger(TryAtHomeController.class);

    @Autowired
    private TryAtHomeService tryAtHomeService;

    @RequestMapping(method = RequestMethod.POST)
    public
    @ResponseBody
    String tryOnHome(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        String contactNumber = request.getParameter("contactNo");
        log.debug("TryOnHomeController.tryOnHome(): User=[{}] Contact=[{}]", userName, contactNumber);
        boolean hasError = false;
        String message = "";
        try {
            tryAtHomeService.sendInternalMail(userName, contactNumber);
        } catch (Exception e) {
            hasError = true;
            message = "Sorry there was some error. Please try again.";
            log.error("Error occurred while sending 'TRY ON HOME' mail to [{}]. Customer: Name={} Phone={}. Reason:{}", userName, contactNumber, e.toString(), e);
        }
        Map<String, Object> response = new HashMap<String, Object>();
        response.put(Constants.HAS_ERROR, hasError);
        response.put(Constants.MESSAGE, message);
        return new Gson().toJson(response);
    }

}
