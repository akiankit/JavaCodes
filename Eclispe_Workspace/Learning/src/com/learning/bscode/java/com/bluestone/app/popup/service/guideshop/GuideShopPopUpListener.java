package com.bluestone.app.popup.service.guideshop;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.popup.PopUpListenerResponse;
import com.bluestone.app.popup.service.AbstractPopUpListener;

@Service
public class GuideShopPopUpListener extends AbstractPopUpListener {
    
    private static final String GUIDE_SHOP_CITY_NAMES_PROPERTY = "GuideShop_CityNames";
    private static final String GUIDE_SHOP_PAGE_NAMES_PROPERTY = "GuideShop_Pages";
    private static final String GUIDE_SHOP_POPUP_LIMIT = "GuideShop_Popup_Limit";
    private static final String GUIDE_SHOP_POPUP_FREQUENCY = "GuideShop_Popup_Frequency_Seconds";
    private static final String GUIDE_SHOP_POPUP_INITIAL_DELAY = "GuideShop_Popup_InitialDelay_Seconds";

    private static final String POPUP_GUIDESHOP_PAGE_URL = "/popup/guideshop";

    private static final String      GUIDE_SHOP_LISTENER_NAME               = "GUIDE_SHOP";
    
    private static final Logger      log                      = LoggerFactory.getLogger(GuideShopPopUpListener.class);
    
    @Override
    public String getName() {
        return GUIDE_SHOP_LISTENER_NAME;
    }

    public PopUpListenerResponse createInitalPopUpResponse() {
        Property popUpLimitProperty = getProperty(GUIDE_SHOP_POPUP_LIMIT);
        Property frequencyProperty =  getProperty(GUIDE_SHOP_POPUP_FREQUENCY);
        Property initalDelayProperty = getProperty(GUIDE_SHOP_POPUP_INITIAL_DELAY);
        int popUpLimit = 1;
        if(popUpLimitProperty != null) {
            popUpLimit = Integer.parseInt(popUpLimitProperty.getValue().trim());
        }
         
        long popUpFrequencyInSec = -1;
        if(frequencyProperty != null) {
            popUpFrequencyInSec = Long.parseLong(frequencyProperty.getValue().trim());
        }
        
        long initalDelayInSec = 0l;
        if(initalDelayProperty != null) {
            initalDelayInSec = Long.parseLong(initalDelayProperty.getValue().trim());
        }
        
        return new PopUpListenerResponse(popUpLimit, POPUP_GUIDESHOP_PAGE_URL, popUpFrequencyInSec, initalDelayInSec);
    }

    @Override
    protected boolean checkEligibility(HttpSession httpSession, PopUpListenerResponse popUpListenerResponse, String requestedURI) {
        boolean showPopUpForCurrentRequest = false;
        boolean cityEligible = isCityEligible(httpSession, GUIDE_SHOP_CITY_NAMES_PROPERTY);
        log.trace("GuideShopPopUpListener isCityEligible = {}", cityEligible);
        if(!cityEligible) {
            // if city is not eligible. no further processing required
            popUpListenerResponse.setFurtherPopUpProcessingRequired(false);
        } else {
            boolean pageEligible = isPageEligible(GUIDE_SHOP_PAGE_NAMES_PROPERTY, requestedURI);
            if(pageEligible && cityEligible)
            {
                log.trace("GuideShopPopUpListener page {} eligible for popup.", requestedURI);
                showPopUpForCurrentRequest = true;
            } 
        }
        return showPopUpForCurrentRequest;
    }
}
