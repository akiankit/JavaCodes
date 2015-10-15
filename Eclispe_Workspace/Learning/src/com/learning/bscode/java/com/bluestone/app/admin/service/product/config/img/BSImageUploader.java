package com.bluestone.app.admin.service.product.config.img;

import com.bluestone.app.admin.service.product.config.ProductUploadException;

/**
 * @author Rahul Agrawal
 *         Date: 2/6/13
 */
public interface BSImageUploader {

    void execute(String imageDirName) throws ProductUploadException;
}
