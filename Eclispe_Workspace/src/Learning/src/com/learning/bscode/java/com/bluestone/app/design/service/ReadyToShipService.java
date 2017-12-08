package com.bluestone.app.design.service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.PropertyService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.shipping.service.HolidayService;

/**
 * @author Rahul Agrawal
 *         Date: 2/21/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ReadyToShipService {

    private static final Logger log = LoggerFactory.getLogger(ReadyToShipService.class);

    public static final String READY_TO_SHIP_PRODUCTS_DELIVERY_DAYS = "Ready To Ship Delivery Days";

    @Autowired
    private CustomizationDao customizationDao;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private ReadyToShipSkuCache readyToShipSkuCache;

    public Map<Object, Object> getReadyToShipProductPageDetails() {
        Map<Object, Object> customizationDetails = new HashMap<Object, Object>();
        Map<String, String> breadCrumbs = new LinkedHashMap<String, String>();
        breadCrumbs.put("Home", "");
        breadCrumbs.put("Jewellery", Constants.JEWELLERY_BASE_PATH + ".html");
        breadCrumbs.put("3-Day Delivery", "last");

        customizationDetails.put("customizationList", getReadyToShipProducts());
        customizationDetails.put("breadCrumbs", breadCrumbs);
        return customizationDetails;
    }

    public List<Customization> getReadyToShipProducts() {
        List<Customization> customizations = new LinkedList<Customization>();
        Set<String> readyToShipSKUs = readyToShipSkuCache.getReadyToShipSKUs();
        customizations = customizationDao.getCustomizationListBySkuGroupByCategory(readyToShipSKUs);
        return customizations;
    }

    public boolean isReadyToShipItem(String skuCode) {
        SortedSet<String> readyToShipSKUs = readyToShipSkuCache.getReadyToShipSKUs();
        if (readyToShipSKUs.contains(skuCode)) {
            return true;
        }
        return false;
    }

    private int getDeliveryDaysCount() {
        int count = 0;
        Property deliveryDaysProperty = null;
        try {
            deliveryDaysProperty = propertyService.getPropertyByName(READY_TO_SHIP_PRODUCTS_DELIVERY_DAYS);
            count = Integer.parseInt(deliveryDaysProperty.getValue());
        } catch (Throwable exception) {
            log.error("Error: ReadyToShipService.getDeliveryDaysCount():{} ", exception.toString(), Throwables.getRootCause(exception));
            count = holidayService.getStandardBizDaysCount();
        }
        return count;
    }

    public String getFormattedDeliveryDate() {
        int daysCount = getDeliveryDaysCount();
        return holidayService.getFormattedDeliveryDate(daysCount);
    }

    public Date getDeliveryDate() {
        int daysCount = getDeliveryDaysCount();
        return holidayService.getDeliveryDate(daysCount);
    }
}
