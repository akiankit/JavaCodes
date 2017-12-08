package com.bluestone.app.design.model;

import java.io.Serializable;
import java.util.Map;

public class Image implements Serializable {

    private static final long serialVersionUID = 3671376576464962193L;
    private Map<String, String> sizeVsUrl;

    private String imageViewType;

    public Map<String, String> getSizeVsUrl() {
        return sizeVsUrl;
    }

    public void setSizeVsUrlMap(Map<String, String> sizeVsUrl) {
        this.sizeVsUrl = sizeVsUrl;
    }

    public String getImageViewType() {
        return imageViewType;
    }

    public void setImageViewType(String imageViewType) {
        this.imageViewType = imageViewType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Image").append("[imageViewType=").append(imageViewType).append("]\n");
        sb.append("{sizeVsUrl=").append(sizeVsUrl);
        sb.append('}');
        return sb.toString();
    }

    public String getImageOfSize(String imageSize) {
        return sizeVsUrl.get(imageSize);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((imageViewType == null) ? 0 : imageViewType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Image)) {
            return false;
        }
        Image other = (Image) obj;
        if (imageViewType == null) {
            if (other.imageViewType != null) {
                return false;
            }
        } else if (!imageViewType.equals(other.imageViewType)) {
            return false;
        }
        return true;
    }
    
    
}
