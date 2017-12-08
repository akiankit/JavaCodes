package com.bluestone.app.admin.service.product.config.update;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateConfigRegistry.UpdateProperty;

public interface ProductUpdateService {
 
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
    public void update(UpdateProperty updateProperty, UpdateMetaData updateMetaData) throws ProductUploadException;
    
}
