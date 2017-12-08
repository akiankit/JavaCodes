package com.bluestone.app.admin.service.product.config.update.listeners;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateConfigRegistry.UpdateProperty;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;


public interface ProductUpdateListener<E extends UpdateMetaData> {
    
    public void validate(UpdateProperty updateProperty, E updateMetaData) throws ProductUploadException;
    
    public void update(UpdateProperty updateProperty, E updateMetaData) throws ProductUploadException;
    
}
