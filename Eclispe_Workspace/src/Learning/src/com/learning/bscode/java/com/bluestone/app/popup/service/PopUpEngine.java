package com.bluestone.app.popup.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.popup.PopUpListenerResponse;
import com.bluestone.app.popup.service.guideshop.GuideShopPopUpListener;
import com.bluestone.app.session.SessionConstants;

@Service
public class PopUpEngine {
    
    private static final Logger log = LoggerFactory.getLogger(PopUpEngine.class);
    
    @Autowired
    private GuideShopPopUpListener guideShopPopUpListener;

    private PopUpListener[] popUpListeners;
    
    @PostConstruct
    public void init () {
        popUpListeners = new PopUpListener[]{guideShopPopUpListener}; 
    }
    
    public PopUpResult execute(String requestedURI, MessageContext messageContext, HttpSession httpSession) {
        PopUpResult popUpResult = new PopUpResult();
        try {
            Map<String, PopUpListenerResponse> popUpListenersResponseMap = (Map<String, PopUpListenerResponse>) httpSession.getAttribute(SessionConstants.POP_UP_METADATA);
            if(popUpListenersResponseMap == null) {
                popUpListenersResponseMap = new ConcurrentHashMap<String, PopUpListenerResponse>();
                httpSession.setAttribute(SessionConstants.POP_UP_METADATA, popUpListenersResponseMap);
            }
            for (PopUpListener eachPopUpListener : popUpListeners) {
                PopUpListenerResponse popUpListenerResponse = getPopUpListenerResponse(httpSession, eachPopUpListener);
                boolean showPopUpForCurrentRequest = eachPopUpListener.isEligible(httpSession,popUpListenerResponse, requestedURI);
                if(showPopUpForCurrentRequest) {
                    log.info("Showing pop up for {}", eachPopUpListener.getName());
                    popUpListenerResponse.getNoOfTimesPopUpShown().incrementAndGet();
                    popUpListenerResponse.setPopUpLastShownInMillis(System.currentTimeMillis());
                    popUpResult.setShowPopUp(true);
                    popUpResult.setPopUpPageUrl(popUpListenerResponse.getPopupPageUrl());
                    break;
                }
            }
        } catch(Throwable th) {
            log.error("Error in executing PopUpEngine.execute() - " , th); // not throwing exception , as dont want customer to be affected , if popup processing fails
        }
        return popUpResult;
    }
    
    private PopUpListenerResponse getPopUpListenerResponse(HttpSession httpSession, PopUpListener popUpListener) {
        Map<String, PopUpListenerResponse> popUpListenersResponseMap = (Map<String, PopUpListenerResponse>) httpSession.getAttribute(SessionConstants.POP_UP_METADATA);
        PopUpListenerResponse popUpListenerResponse = popUpListenersResponseMap.get(popUpListener.getName());
        if(popUpListenerResponse == null) {
            popUpListenerResponse = popUpListener.createInitalPopUpResponse();
            popUpListenersResponseMap.put(popUpListener.getName(), popUpListenerResponse);
        }
        return popUpListenerResponse;
    }
}
