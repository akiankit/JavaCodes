package com.bluestone.app.admin.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.dao.PropertyDao;
import com.bluestone.app.admin.model.Property;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
class CacheablePropertyService implements IProperty {
    private static final Logger log = LoggerFactory.getLogger(CacheablePropertyService.class);

    @Autowired
    private PropertyDao propertydao;

    @PostConstruct
    void init(){
        log.debug("CacheablePropertyService.init()");
    }

    @Cacheable(cacheName = "propertyListCache")
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Property> getPropertyList() {
        log.info("CacheablePropertyService.getPropertyList()");
        Map<String, Property> propertyMap = new HashMap<String, Property>();
        List<Property> propertyList = propertydao.getPropertyList();
        for (Property property : propertyList) {
            getParsedValueAndSetList(property);
            propertyMap.put(property.getName(), property);
        }
        return propertyMap;
    }

    private void getParsedValueAndSetList(Property property) {
        log.info("Parsing property [{}] Value=[{}]", property.getName(), property.getValue());
        String[] values = property.getValue().split(",");

        if (log.isDebugEnabled()) {
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                log.debug("{}.){}", (i + 1), value);
            }
        }
        PropertyDataTypes propertyDataType = PropertyDataTypes.valueOf(property.getType().toUpperCase());
        switch (propertyDataType) {
            case LONG:
                Long intarray[] = new Long[values.length];
                for (int i = 0; i < values.length; i++) {
                    intarray[i] = Long.parseLong(values[i].trim());
                }
                property.setLongValues(Arrays.asList(intarray));

                break;
            case BOOLEAN:
                Boolean boolArray[] = new Boolean[values.length];
                for (int i = 0; i < values.length; i++) {
                    boolArray[i] = Boolean.parseBoolean(values[i].trim());
                }
                property.setBooleanValues(Arrays.asList(boolArray));

                break;
            case STRING:
                property.setStringValues(Arrays.asList(values));
                break;
            case DOUBLE:
                Double doublearray[] = new Double[values.length];
                for (int i = 0; i < values.length; i++) {
                    doublearray[i] = Double.parseDouble(values[i].trim());
                }
                property.setDoubleValues(Arrays.asList(doublearray));

                break;
        }
    }


    @TriggersRemove(cacheName = "propertyListCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    void updateProperty(Property property) {
        if (property != null) {
            property.setValue(property.getValue());
            property.setType(property.getType());
            final Property updatedProperty = propertydao.update(property);
            log.debug("CacheablePropertyService.updateProperty(): updated to [{}]", updatedProperty);
        }
    }

    @TriggersRemove(cacheName = "propertyListCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    void addProperty(Property property) {
        propertydao.create(property);
    }

    @TriggersRemove(cacheName = "propertyListCache", when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    void deleteProperty(Property property) {
        propertydao.remove(property);
    }

}
