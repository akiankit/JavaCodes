package com.bluestone.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthCheckController {
    
    private static final Logger log = LoggerFactory.getLogger(HealthCheckController.class);
    
    
    @RequestMapping(value = "/healthcheck", method = RequestMethod.GET)
    public @ResponseBody String checkHealth() {
        log.debug("In HealthCheck");
        return "Ok";
    }

}
