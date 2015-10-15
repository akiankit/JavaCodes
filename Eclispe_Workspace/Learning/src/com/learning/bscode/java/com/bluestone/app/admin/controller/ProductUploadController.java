package com.bluestone.app.admin.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Throwables;
import org.apache.log4j.Level;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.service.product.config.DesignImageUpdateService;
import com.bluestone.app.admin.service.product.config.ProductListingService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.ProductUploadService;
import com.bluestone.app.ops.RuntimeService;

@Controller
@RequestMapping(value = {"/admin/upload*"})
public class ProductUploadController {
    private static final Logger log = LoggerFactory.getLogger(ProductUploadController.class);

    @Autowired
    private ProductUploadService productUploadService;

    @Autowired
    private ProductListingService productListingService;

    @Autowired
    private DesignImageUpdateService designImageUpdateService;

    @RequestMapping(value = "/list*", method = RequestMethod.GET)
    @RequiresPermissions("upload:view")
    public ModelAndView listProducts() {
        log.info("ProductUploadController.listProducts()");
        Map<String, Object> productAndImageList = productListingService.getProductAndImageList();
        return new ModelAndView("upload", "lists", productAndImageList);
    }

    @RequestMapping(value = "/add*", method = RequestMethod.POST)
    @RequiresPermissions("upload:add")
    public ModelAndView uploadProductAndImages(MultipartHttpServletRequest request) {
        log.info("ProductUploadController.uploadProductAndImages()");
        Map<String, Object> productAndImageList = new HashMap<String, Object>();
        try {
            String[] upload = request.getParameterValues("upload[]");
            if (upload != null) {
                handleUploadProducts(upload);
            }
        } catch (ProductUploadException e) {
            log.error("Error occurred while executing uploadProduct {} ", e.generateErrorMessage(), e);
            productAndImageList.put("uploadError", e.getErrorList());
        }

        try {
            String[] image = request.getParameterValues("image[]");
            if (image != null) {
                handleImageUpdates(image);
            }
        } catch (ProductUploadException e) {
            log.error("Error occurred while executing uploadImages {} ", e.generateErrorMessage(), e);
            productAndImageList.put("imageError", e.getErrorList());
        }


        String[] customization = request.getParameterValues("customization[]");
        if (customization != null) {
            for (int i = 0; i < customization.length; i++) {
                try {
                    RuntimeService.enableFullProductConfigurationLogging(Level.DEBUG);
                    productUploadService.addParentCustomizationsToDesign(customization[i]);
                } catch (ProductUploadException exception) {
                    productAndImageList.put("customizationErrors", exception.getErrorList());
                    log.error("Error occurred while executing uploadCustomizations {} ", exception.generateErrorMessage(), exception);
                } finally {
                    RuntimeService.enableFullProductConfigurationLogging(Level.INFO);
                }
            }
        }


        try {
            productAndImageList.putAll(productListingService.getProductAndImageList());
        } catch (Exception exception) {
            log.error("Error: ProductUploadController.listing: {}", exception.getLocalizedMessage(), exception);
        }

        return new ModelAndView("upload", "lists", productAndImageList);


/*        String[] update = request.getParameterValues("update[]");
        List<String> updateErrors = new ArrayList<String>();
        if (update != null) {
            for (int i = 0; i < update.length; i++) {
                List<String> updateProductErrors = new LinkedList<String>();
                try {
                    RuntimeService.enableFullProductConfigurationLogging(Level.DEBUG);
                    updateProductErrors = productUploadServiceV2.validateAndUpdateProduct(update[i]);
                    if (!updateProductErrors.isEmpty()) {
                        uploadErrors.add("Error in updation of Product for Code: " + update[i]);
                        uploadErrors.addAll(updateProductErrors);
                    }
                } catch (Exception exception) {
                    updateErrors.add("Error in updation of product for Code: " + update[i]);
                    updateErrors.add(Throwables.getRootCause(exception).toString());
                    log.error("Error: ProductUploadController.uploadProductAndImages(): {}", exception.getLocalizedMessage(), exception);

                } finally {
                    RuntimeService.enableFullProductConfigurationLogging(Level.INFO);
                }
            }
        }*/
    }

    private void handleImageUpdates(String[] image) throws ProductUploadException {
        List<String> designCodes = Arrays.asList(image);
        try {
            RuntimeService.enableFullProductConfigurationLogging(Level.DEBUG);
            for (String eachDesignCode : designCodes) {
                designImageUpdateService.execute(eachDesignCode);
                log.info("Successfully updated image for DesignCode=[{}]", eachDesignCode);
            }
        } catch (Exception exception) {
            Throwables.propagateIfInstanceOf(exception, ProductUploadException.class);
            throw new ProductUploadException(Throwables.getRootCause(exception).toString(), exception);
        } finally {
            RuntimeService.enableFullProductConfigurationLogging(Level.INFO);
        }
    }

    private void handleUploadProducts(String[] upload) throws ProductUploadException {
        for (int i = 0; i < upload.length; i++) {
            try {
                RuntimeService.enableFullProductConfigurationLogging(Level.DEBUG);
                productUploadService.validateAndCreateProduct(upload[i]);
            } catch (Exception exception) {
                Throwables.propagateIfInstanceOf(exception, ProductUploadException.class);
                throw new ProductUploadException("Error in creation of product for DesignCode: " + upload[i] + " Exception "
                                                 + Throwables.getRootCause(exception).toString(), exception);
            } finally {
                RuntimeService.enableFullProductConfigurationLogging(Level.INFO);
            }
        }
    }

}
