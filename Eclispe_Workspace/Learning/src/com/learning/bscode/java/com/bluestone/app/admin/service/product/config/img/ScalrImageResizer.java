package com.bluestone.app.admin.service.product.config.img;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.admin.service.product.config.ProductUploadContext;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.design.model.Customization;

/**
 * @author Rahul Agrawal
 *         Date: 2/8/13
 */
@Service("imageResizer")
public class ScalrImageResizer implements ImageResizer {

    private static final Logger log = LoggerFactory.getLogger(ScalrImageResizer.class);

    @Autowired
    private ProductUploadContext productUploadContext;

    @Override
    public String resizeAndRenameImages(String designCode, Customization customization) throws ProductUploadException {
        log.debug("ScalrImageResizer.resizeAndRenameImages(): DesignCode=[{}] CustomizationId=[{}]", designCode, customization.getId());
        String srcImageDir = productUploadContext.getImagesDirPath(designCode);
        return ImageResizeUtil.resizeAndRenameImages(designCode, customization, srcImageDir);
    }
}
