package com.bluestone.app.popup;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class PopUpListenerResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private final String popupPageUrl;
    
    private final int popUpLimit;

    private final long popUpFrequencyInSec;

    private final long initialDelayInMillis;

    private final long creationTimeInMillis;
    
    private AtomicInteger noOfTimesPopUpShown;
    
    private long popUpLastShownInMillis = 0l;

    private boolean isInitialDelayLapsed = false; // TODO need to verify whether volatile is needed

    private boolean isFurtherPopUpProcessingRequired = true; // TODO need to verify whether volatile is needed
    
    public boolean isInitialDelayLapsed() {
        return isInitialDelayLapsed;
    }

    public PopUpListenerResponse(int popUpLimit, String popupPageUrl, long popUpFrequencyInSec, long initialDelayInSec) {
        this.popUpLimit = popUpLimit;
        this.popupPageUrl = popupPageUrl;
        this.popUpFrequencyInSec = popUpFrequencyInSec;
        this.initialDelayInMillis = initialDelayInSec * 1000;
        this.noOfTimesPopUpShown = new AtomicInteger();
        this.creationTimeInMillis = System.currentTimeMillis();
    }
    
    public int getPopUpLimit() {
        return popUpLimit;
    }

    public String getPopupPageUrl() {
        return popupPageUrl;
    }

    public AtomicInteger getNoOfTimesPopUpShown() {
        return noOfTimesPopUpShown;
    }

    public long getPopUpFrequencyInSec() {
        return popUpFrequencyInSec;
    }

    public long getInitialDelayInMillis() {
        return initialDelayInMillis;
    }

    public long getPopUpLastShownInMillis() {
        return popUpLastShownInMillis;
    }

    public void setPopUpLastShownInMillis(long popUpLastShownInMillis) {
        this.popUpLastShownInMillis = popUpLastShownInMillis;
    }

    public long getCreationTimeInMillis() {
        return creationTimeInMillis;
    }

    public void setInitialDelayLapsed(boolean isInitialDelayLapsed) {
        this.isInitialDelayLapsed = isInitialDelayLapsed;
    }

    public void setFurtherPopUpProcessingRequired(boolean isFurtherPopUpProcessingRequired) {
        this.isFurtherPopUpProcessingRequired = isFurtherPopUpProcessingRequired;
    }
    
    public boolean isFurtherPopUpProcessingRequired() {
        return isFurtherPopUpProcessingRequired;
    }

}
