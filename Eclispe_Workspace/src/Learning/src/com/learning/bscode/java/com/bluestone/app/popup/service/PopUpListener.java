package com.bluestone.app.popup.service;

import javax.servlet.http.HttpSession;

import com.bluestone.app.popup.PopUpListenerResponse;

public interface PopUpListener {

    public String getName();
    
    public boolean isEligible(HttpSession httpSession, PopUpListenerResponse previousPopUpListenerResponse, String requestedURI);

    public PopUpListenerResponse createInitalPopUpResponse();
    
}
