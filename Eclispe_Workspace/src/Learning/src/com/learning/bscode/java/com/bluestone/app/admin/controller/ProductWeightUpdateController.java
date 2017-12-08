package com.bluestone.app.admin.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.service.product.config.ProductWeightUpdateService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.csv.CsvStatementLogger;
import com.bluestone.app.uploadFile.UploadItem;

@Controller
@RequestMapping(value = "/admin/weightupdate*")
public class ProductWeightUpdateController {
    private static final Logger log = LoggerFactory.getLogger(ProductWeightUpdateController.class);

    @Autowired
    private ProductWeightUpdateService productWeightUpdateService;

    @RequiresPermissions("upload:edit")
    @RequestMapping(value = {"uploadweightlist", "uploadweightpercentlist"}, method = RequestMethod.GET)
    public ModelAndView updateProductWeight(Model model) {
        log.info("AdminUtilityController.updateProductWeight()");
        model.addAttribute(new UploadItem());
        Map<String, Object> viewData = new HashMap<String, Object>();
        viewData.put("uploadProductWeight", true);
        return new ModelAndView("uploadProductWeight", "viewData", viewData);
    }

    @RequestMapping(value = "uploadweightlist", method = RequestMethod.POST)
    @RequiresPermissions("upload:edit")
    public ModelAndView updateProductWeight(UploadItem uploadItem, BindingResult result) {
        log.info("AdminUtilityController.updateProductWeight()");
        Map<String, Object> viewData = new HashMap<String, Object>();
        String message = "Weight updated successfully.";
        StringBuilder errorMsg = new StringBuilder();
        CommonsMultipartFile fileData = uploadItem.getFileData();
        try {
            if (result.hasErrors()) {
                message = "File not uploaded successfully.";
            } else if (StringUtils.isBlank(fileData.getOriginalFilename())) {
                message = "Please select file ";
            } else if (!fileData.getFileItem().getName().contains(".csv")) {
                message = "Please upload a CSV file ";
            } else {
                String content = new String(fileData.getBytes());
                String contentLines[] = content.split(Constants.NEWLINE_REGEX);
                if (contentLines != null && contentLines.length > 0) {
                    CsvStatementLogger.log.warn("DesignId,Sku Code,Old Weight,New Weight,Old Price,New Price");
                    for (int i = 0; i < contentLines.length; i++) {
                        // assuming there is no header
                        // Assuming first column is sku code and second is the weight
                        String[] row = contentLines[i].split(",");
                        String sku = row[0].trim();
                        try {
                            Double wt = Double.valueOf(row[1].trim());
                            productWeightUpdateService.uploadCustomizationWeight(sku, wt);
                        } catch (Exception e) {
                            errorMsg.append(sku + " (Error: " + e.getMessage() + "), ");
                            log.error("Error while updating weight for sku=[{}]", sku);
                        }
                    }
                } else {
                    log.info("ProductWeightUpdateController.updateProductWeight(): Did not find any content in the uploaded file.");
                }

                if (errorMsg.length() > 0) {
                    message = "Error while updating products for following sku codes: " + errorMsg.toString();
                }
            }
        } catch (Exception e) {
            message = "Error occurred while updating product weight:" + e.getMessage();
            log.error(message, e);
        }
        viewData.put("message", message);
        return new ModelAndView("uploadProductWeight", "viewData", viewData);
    }

    @RequestMapping(value = "uploadweightpercentlist", method = RequestMethod.POST)
    @RequiresPermissions("upload:edit")
    public ModelAndView updateProductWeightPercentage(UploadItem uploadItem, BindingResult result) {
        log.info("AdminUtilityController.updateProductWeightPercentage()");
        Map<String, Object> viewData = new HashMap<String, Object>();
        String message = "Weight updated successfully.";
        StringBuilder errorMsg = new StringBuilder();
        CommonsMultipartFile fileData = uploadItem.getFileData();
        try {
            if (result.hasErrors()) {
                message = "File not uploaded successfully.";
            } else if (StringUtils.isBlank(fileData.getOriginalFilename())) {
                message = "Please select file";
            } else if (!fileData.getFileItem().getName().contains(".csv")) {
                message = "Please upload a CSV file";
            } else {
                String content = new String(fileData.getBytes());
                String contentLines[] = content.split(Constants.NEWLINE_REGEX);
                if (contentLines != null && contentLines.length > 0) {
                    CsvStatementLogger.log.warn("DesignId,Sku Code,Old Weight,New Weight,Old Price,New Price");
                    for (int i = 0; i < contentLines.length; i++) {
                        // assuming there is no header
                        // Assuming first column is design id and second is
                        // percentage, actual metal weigh.actual total weight
                        String[] col = contentLines[i].split(",");
                        try {
                            if (col.length > 1 && col[1].length() > 0) {
                                productWeightUpdateService.uploadCustomizationWeightByPecentage(
                                        col[0], col[1], true, false);
                            } else if (col.length > 2 && col[2].length() > 0) {
                                productWeightUpdateService.uploadCustomizationWeightByPecentage(
                                        col[0], col[2], false, false);
                            } else if (col.length > 3 && col[3].length() > 0) {
                                productWeightUpdateService.uploadCustomizationWeightByPecentage(
                                        col[0], col[3], false, true);
                            } else {
                                log.warn("Ignoring the row - " + contentLines[i]);
                            }
                        } catch (Exception e) {
                            errorMsg.append("(Error: " + e.getMessage() + "), ");
                            log.error("Error while updating weight. Row - ", contentLines[i]);
                        }
                    }
                }

                if (errorMsg.length() > 0) {
                    message = "Error while updating products for following design ids : " + errorMsg.toString();
                }
            }
        } catch (Exception e) {
            message = "Error occurred while updating product weight:" + e.getMessage();
            log.error(message, e);
        }
        viewData.put("message", message);
        return new ModelAndView("uploadProductWeight", "viewData", viewData);
    }

}
