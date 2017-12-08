package com.bluestone.app.admin.service;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.Property;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class PropertyService {

    private static final Logger log = LoggerFactory.getLogger(PropertyService.class);

    @Autowired
    private CacheablePropertyService cacheablePropertyService;

    @PostConstruct
    void init(){
        log.debug("PropertyService.init()");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public boolean addUpdateProperty(Property property) {
        if (property.getName().length() > 0) {
            log.info("PropertyService.addUpdateProperty(): [{}]=[{}]", property.getName(), property.getValue());
            Property propertyByName = getPropertyByName(property.getName());
            if (propertyByName != null) {
                propertyByName.setValue(property.getValue());
                propertyByName.setType(property.getType());
                cacheablePropertyService.updateProperty(propertyByName);
                return true;
            } else {
                cacheablePropertyService.addProperty(property);
                return true;
            }
        } else {
            return false;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String deleteProperty(String propertyName) {
        log.info("PropertyService.deleteProperty():[{}}", propertyName);
        final Property propertyByName = getPropertyByName(propertyName);
        if (propertyByName == null) {
            return "Unable to find property with Name=" + propertyByName.getName();
        } else {
            cacheablePropertyService.deleteProperty(propertyByName);
            return "Successfully deleted";
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Property getPropertyByName(String propertyName) {
        log.debug("PropertyService.getPropertyByName(): [{}]", propertyName);
        //Map<String, Property> propertyList = cacheablePropertyService.getPropertyList();
        Map<String, Property> propertyList = getPropertyList();
        if (propertyList.containsKey(propertyName)) {
            return propertyList.get(propertyName);
        }
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Property> getPropertyList() {
        return cacheablePropertyService.getPropertyList();
    }
}
