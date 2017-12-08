package com.bluestone.app.uniware;

import java.rmi.RemoteException;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.ops.RuntimeService;
import com.google.common.base.Throwables;
import com.unicommerce.uniware.services.ItemTypeRequest;

/**
 * @author Rahul Agrawal
 *         Date: 10/29/12
 */
@Service
public class UniwareItemService {

    private static final Logger log = LoggerFactory.getLogger(UniwareItemService.class);

    @Autowired
    private WebService webService;

    public boolean createEditItemType(Customization customization, String skuWithSize) throws RemoteException {
        RuntimeService.enableFullProductConfigurationLogging(Level.DEBUG);
        log.info("UniwareItemService.createEditItemType() for CustomizationId={} , SKU={}", customization.getId(), skuWithSize);
        UniwareItemType uniwareItemType = UniwareItemTypeFactory.create(customization, skuWithSize);
        return createEdit(uniwareItemType);
    }

    boolean createEdit(UniwareItemType uniwareItemType) {
        log.debug("UniwareItemService.createEditItem() for {}", uniwareItemType);
        boolean result = false;
        ItemTypeRequestBuilder requestBuilder = new ItemTypeRequestBuilder(uniwareItemType);
        ItemTypeRequest itemTypeRequest = requestBuilder.create();
        try {
            result = webService.createEditItem(itemTypeRequest);
        } catch (Exception e) {
            log.error("Error: UniwareItemService.createEditItem(): Reason=", e.toString(), Throwables.getRootCause(e));
        } finally {
            RuntimeService.enableFullProductConfigurationLogging(Level.INFO);
        }
        return result;
    }

}


