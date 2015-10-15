package com.bluestone.app.admin.service.product.config.update.postprocessors;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;


public interface PostProductUpdateProcessor {
 
    public void execute(UpdateMetaData updateMetaData) throws ProductUploadException;
}
