package com.bluestone.app.core.service;

import java.io.Serializable;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.googlecode.ehcache.annotations.Cacheable;

@Component
public class VelocityTemplateService {
    
    private static final Logger log = LoggerFactory.getLogger(VelocityTemplateService.class);
    
    @Autowired
    private VelocityEngine velocityEngine;
    
    public String getText(String templateName, Map<String, Serializable> templateVariables) {
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                                  "velocity/" + templateName,
                                                                  templateVariables);
        
        return text;
    }

    @Cacheable(cacheName="productsFeedCache")
    public String convertProductsToText(String templateName, Map<String, Object> templateVariables) {
        String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine,
                                                                  "velocity/" + templateName,
                                                                  templateVariables);
        
        return text;
    }
}
