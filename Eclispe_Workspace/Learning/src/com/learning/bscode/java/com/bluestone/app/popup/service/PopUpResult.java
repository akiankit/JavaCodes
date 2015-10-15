package com.bluestone.app.popup.service;

import java.io.Serializable;

public class PopUpResult implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private boolean showPopUp;
    
    private String popUpPageUrl;

    public boolean isShowPopUp() {
        return showPopUp;
    }

    public String getPopUpPageUrl() {
        return popUpPageUrl;
    }

    public void setPopUpPageUrl(String popUpPageUrl) {
        this.popUpPageUrl = popUpPageUrl;
    }

    public void setShowPopUp(boolean showPopUp) {
        this.showPopUp = showPopUp;
    }
    
    public boolean getShowPopUp() {
        return showPopUp;
    }
    
    
}
