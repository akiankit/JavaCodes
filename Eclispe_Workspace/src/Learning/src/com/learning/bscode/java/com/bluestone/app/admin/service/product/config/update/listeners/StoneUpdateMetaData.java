package com.bluestone.app.admin.service.product.config.update.listeners;

import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;

public class StoneUpdateMetaData extends UpdateMetaData {
    
	private String[] designStoneSpecificationIds;

	public String[] getDesignStoneSpecificationIds() {
		return designStoneSpecificationIds;
	}

	public void setDesignStoneSpecificationIds(
			String[] designStoneSpecificationIds) {
		this.designStoneSpecificationIds = designStoneSpecificationIds;
	}
    
     
}
