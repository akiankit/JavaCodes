package com.bluestone.app.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.ReadyToShipDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.ReadyToShip;

/**
 * @author Rahul Agrawal
 *         Date: 2/21/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ReadyToShipConfigurationService {

    private static final Logger log = LoggerFactory.getLogger(ReadyToShipConfigurationService.class);

    @Autowired
    private CustomizationDao customizationDao;

    @Autowired
    private ReadyToShipDao readyToShipDao;

    private static final String BASE_URL_FOR_PAGINATION = "/admin/readyToShip/list";


    // used from admin
    public Map<Object, Object> getReadyToShipDetails(ListFilterCriteria filterCriteria, boolean b) {
        int start = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
        Map<Object, Object> readyToShipListAndTotalCount = readyToShipDao.getReadyToShipListAndTotalCount(start, filterCriteria);
        List<ReadyToShip> carriersList = (List<ReadyToShip>) readyToShipListAndTotalCount.get("list");
        Map<Object, Object> dataForView = PaginationUtil.generateDataForView(readyToShipListAndTotalCount, filterCriteria.getItemsPerPage(),
                                                                             filterCriteria.getP(), start, BASE_URL_FOR_PAGINATION, carriersList.size());
        Map<String, String> searchFieldMap = new HashMap<String, String>();
        searchFieldMap.put("SkuCode", "skuCode");

        dataForView.put("searchFieldMap", searchFieldMap);
        return dataForView;
    }

    public ReadyToShip find(long primaryKey) {
        return readyToShipDao.findAny(ReadyToShip.class, primaryKey);
    }

    public String validateReadyToShipSkuCode(ReadyToShip readyToShip) {
        log.info("ReadyToShipConfigurationService.validateReadyToShipSkuCode(): {}", readyToShip);
        String errorMessage = null;
        String skuCode = readyToShip.getSkuCode();
        ReadyToShip existingSkuCode = readyToShipDao.getReadyToShipBySkuCode(skuCode);
        if (existingSkuCode == null) {
            Customization customization = customizationDao.getCustomizationBySku(skuCode);
            if (customization == null) {
                errorMessage = "SkuCode [" + skuCode + "] does not exist in system.";
            } else {
                if (customization.isProductActive() == false) {
                    errorMessage = "Product for SkuCode [" + skuCode + "] is not active.";
                } else if (customization.getParentCustomization() != null) {
                    errorMessage = "As of now only parent customization can be shown as ready to ship products";
                }
            }
        } else {
            errorMessage = "SkuCode " + skuCode + " already exists under the ReadyToShip category.";
        }
        return errorMessage;
    }
}
