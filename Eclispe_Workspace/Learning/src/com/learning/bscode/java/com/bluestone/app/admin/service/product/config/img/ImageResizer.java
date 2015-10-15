package com.bluestone.app.admin.service.product.config.img;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.design.model.Customization;

/**
 * @author Rahul Agrawal
 *         Date: 2/8/13
 */
public interface ImageResizer {

    String resizeAndRenameImages(String designCode, Customization customization) throws ProductUploadException;
}
