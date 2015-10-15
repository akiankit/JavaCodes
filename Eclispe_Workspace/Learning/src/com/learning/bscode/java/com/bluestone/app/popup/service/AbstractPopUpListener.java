package com.bluestone.app.popup.service;

import static com.bluestone.app.popup.PopUpConstant.EMPTY_STRING;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.PropertyService;
import com.bluestone.app.popup.PopUpListenerResponse;
import com.bluestone.app.session.SessionConstants;

public abstract class AbstractPopUpListener implements PopUpListener {
    
    private static final Logger log = LoggerFactory.getLogger(AbstractPopUpListener.class);
    
    @Autowired
    private PropertyService propertyService;
    
    @Override
    public boolean isEligible(HttpSession httpSession, PopUpListenerResponse previousPopUpListenerResponse, String requestedURI) {
        boolean checkDefaultEligiblity = checkDefaultEligiblity(previousPopUpListenerResponse);
        if(checkDefaultEligiblity) {
            return checkEligibility(httpSession,previousPopUpListenerResponse, requestedURI);
        } 
        return false;
    }
    
    protected boolean isCityEligible(HttpSession httpSession, String propertyName) {
        String city = (String) httpSession.getAttribute(SessionConstants.CITY_FROM_IP);
        log.trace("Identified city from ip = {}", city);
        Property property = getProperty(propertyName);
        List<String> eligibleCities = property.getStringValues();
        if(eligibleCities.contains("*")) {  // if eligibile cities is * , all cities will be eligible
            return true;
        }
        // if the string is empty then no need of calculation
        if (EMPTY_STRING.equals(city)) {
            return false;
        }
        // if not empty then going for calculation
        return eligibleCities.contains(city);
    }
    
    protected boolean isPageEligible(String propertyName, String requestUri) {
        Property property = getProperty(propertyName);
        List<String> eligiblePages = property.getStringValues();
        
        if(eligiblePages.contains("*")) {  // if eligibile pages is * , all pages will be eligible
            return true;
        }
        
        for (String page : eligiblePages) {
            if (requestUri.contains(page)) {
                return true;
            }
        }
        return false;
    }
    
    protected Property getProperty(String propertyName) {
        return propertyService.getPropertyByName(propertyName);
    }
    
    //  check whether initial delay is lapsed
    // function will set a flag whether initial delay is lapsed , after that no need to do this time based processing
    private boolean checkInitalDelayEligibility(PopUpListenerResponse popUpListenerResponse) {
        log.trace("Is isInitalDelayLapsed = {}", popUpListenerResponse.isInitialDelayLapsed());
        if(popUpListenerResponse.isInitialDelayLapsed()) {
            return true;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long initialDelayInMillis = popUpListenerResponse.getInitialDelayInMillis();
        long creationTimeInMillis = popUpListenerResponse.getCreationTimeInMillis();
        
        log.trace("currentTimeMillis = {}, initialDelayInMillis={}, creationTimeInMillis={}", currentTimeMillis, initialDelayInMillis, creationTimeInMillis);
        
        boolean isInitailDelayLapsed = (creationTimeInMillis + initialDelayInMillis) <= currentTimeMillis;
        log.trace("Is isInitalDelayLapsed = {}", isInitailDelayLapsed);
        popUpListenerResponse.setInitialDelayLapsed(isInitailDelayLapsed);
        return isInitailDelayLapsed;
    }
    
    private boolean checkDefaultEligiblity(PopUpListenerResponse popUpListenerResponse) {
        if(!popUpListenerResponse.isFurtherPopUpProcessingRequired()) {
            log.trace("further processing is not required, Skipping further eligibility checks");
            return false;
        }
        
        boolean isInitalDelayLapsed = checkInitalDelayEligibility(popUpListenerResponse);
        log.trace("Is isInitalDelayLapsed = {}", isInitalDelayLapsed);
        if(!isInitalDelayLapsed) {
            return false;  // if initial delay is not lapsed , return false, as this popup is still not eligible
        }
        
        boolean isNoOfTimesLapsed = checkNoOfTimesEligibility(popUpListenerResponse);
        log.trace("Is isNoOfTimesLapsed = {}", isNoOfTimesLapsed);
        if(isNoOfTimesLapsed) {
            return false;// if no Of times this pop up is shown is elapses, return false, as this popup is not eligible anymore.
        }
        return checkFrequencyEligibility(popUpListenerResponse);
        
    }
    
    private boolean checkNoOfTimesEligibility(PopUpListenerResponse popUpListenerResponse) {
        return popUpListenerResponse.getNoOfTimesPopUpShown().get() >= popUpListenerResponse.getPopUpLimit();
    }
    
    private boolean checkFrequencyEligibility(PopUpListenerResponse popUpListenerResponse) {
        return true; // TODO    
    }
    
/*    protected PopUpListenerResponse getPopUpListenerResponse(HttpSession httpSession) {
        Map<String, PopUpListenerResponse> popUpListenersResponseMap = (Map<String, PopUpListenerResponse>) httpSession.getAttribute(SessionConstants.POP_UP_METADATA);
        String listenerName = getName();
        PopUpListenerResponse popUpListenerResponse = popUpListenersResponseMap.get(listenerName);
        if(popUpListenerResponse == null) {
            popUpListenerResponse = createInitalPopUpResponse();
            popUpListenersResponseMap.put(listenerName, popUpListenerResponse);
        }
        return popUpListenerResponse;
    }*/

    protected abstract boolean checkEligibility(HttpSession httpSession, PopUpListenerResponse popUpListenerResponse, String requestedURI);
}
