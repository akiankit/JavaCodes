package com.bluestone.app.image;

import java.util.Map;

public class Image {

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

}
