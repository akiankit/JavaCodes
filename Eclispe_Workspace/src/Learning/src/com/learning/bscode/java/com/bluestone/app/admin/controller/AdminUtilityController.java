package com.bluestone.app.admin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.service.AdminUtilityService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DownloadUtil;
import com.bluestone.app.core.util.FileUtil;
import com.bluestone.app.csv.CsvStatementLogger;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.shipping.service.HolidayService;
import com.bluestone.app.uploadFile.UploadItem;

@Controller
@RequestMapping(value = "/admin/utility*")
public class AdminUtilityController {

    private static final Logger log = LoggerFactory.getLogger(AdminUtilityController.class);
    
    @Autowired
    private CustomizationService        customizationService;
    
    @Autowired
    private AdminUtilityService         adminUtilityService;
    
    @Autowired
    private HolidayService      holidayService;
    
    public static final String CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";
    
    @RequestMapping(value = "holiday", method = RequestMethod.GET)
	@RequiresPermissions("holiday:add")
    public ModelAndView uploadHoliday(Model model) {
        log.info("AdminUtilityController.uploadHoliday()");
        model.addAttribute(new UploadItem());
        Map<String, Object> viewData = new HashMap<String, Object>();
        viewData.put("uploadHoliday", true);
        return new ModelAndView("uploadHoliday", "viewData", viewData);
    }
    
    @RequiresPermissions("holiday:view")
    @RequestMapping(value = "holiday/downloadSampleFile", method = RequestMethod.GET)
    public void downloadSampleHolidayFile(HttpServletRequest request,HttpServletResponse response){
    	log.info("AdminUtilityController.downloadSampleHolidayFile()");
    	String sessionId = request.getSession().getId();
    	File file;
		try {
			file = adminUtilityService.writeHolidayToFile(sessionId);
			DownloadUtil.copyFileDataToHttpResponse(file, response,"text/csv");
		} catch (Exception e) {
			log.error("Error occured while writing products score to file",e);
		}
    	
    }
    
    @RequestMapping(value = "uploadholidaylist", method = RequestMethod.POST)
	@RequiresPermissions("holiday:edit")
    public ModelAndView updateHolidayList(UploadItem uploadItem, BindingResult result) {
        log.info("AdminUtilityController.updateHolidayList()");
        Map<String, Object> viewData = new HashMap<String, Object>();
        String message = "Holiday list updated successfully.";
        CommonsMultipartFile fileData = uploadItem.getFileData();
        try {
            if (result.hasErrors()) {
                message = "File not uploaded successfully.";
            } else if (StringUtils.isBlank(fileData.getOriginalFilename())) {
                message = "Please select file";
            } else if (!fileData.getFileItem().getName().contains(".csv")) {
                message = "Please upload a CSV file";
            } else {
                holidayService.deleteHolidayList();
                String content = new String(fileData.getBytes());
                String contentLines[] = content.split(Constants.NEWLINE_REGEX);
                holidayService.insertHolidayList(contentLines);
            }
        } catch (Exception e) {
            message = "Error occurred while updating holiday list:" + e.getMessage();
            log.error(message, e);
        }
        viewData.put("message", message);
        return new ModelAndView("uploadHoliday", "viewData", viewData);
    }
    
    @RequestMapping(value = "gettagslist*", method = RequestMethod.GET)
	@RequiresPermissions("tags:view")
    public void getTags(HttpServletRequest request, HttpServletResponse response) {
        log.info("AdminUtilityController.getTags()");
        String sessionId= request.getSession().getId();
        log.debug("AdminUtilityController.getTags()");
        List<Customization> customizationsList = customizationService.getAllCustomizationWithPriority(Customization.DEFAULT_PRIORITY);
        File file;
        try {
            file = adminUtilityService.writeTagsDataToFile(customizationsList,sessionId);
			DownloadUtil.copyFileDataToHttpResponse(file, response,"text/csv");
        } catch (Exception e) {
            log.error("Error occurred while fetching tags ", e);
        }
        //@todo P1 : Rahul Agrawal :  what would be displayed when some error happens...???
    }
    
    @RequestMapping(value = "getproductslist*", method = RequestMethod.GET)
	@RequiresPermissions("products:view")
    public ModelAndView getProductsPage(HttpServletRequest request) {
    	Map<String, Object> viewData = new HashMap<String, Object>();
        viewData.put("Page Title", "Product List Download");
        return new ModelAndView("downloadProduct","viewData", viewData);
    }
    
    @RequestMapping(value = "exportProduct", method = RequestMethod.POST)
    public ModelAndView exportProducts(HttpServletRequest request,HttpServletResponse response) {
    	log.debug("AdminUtilityController.exportProducts()");
    	Enumeration<String> parameterNames = request.getParameterNames();
    	Map<String, Object> viewData = new HashMap<String, Object>();
    	String fileName = FileUtil.generateUniqueFileName("ProductList", ".xls");
    	try {
    		File file = adminUtilityService.getProductListFile(parameterNames,fileName);
			DownloadUtil.copyFileDataToHttpResponse(file, response, CONTENT_TYPE_EXCEL);
		} catch (IOException e) {
			log.error("Error during AdminUtilityController.exportProducts: Reason:{}", e.toString(), e);
		}
        viewData.put("Page Title", "Product List Download");
        return new ModelAndView("downloadProduct","viewData", viewData);
    }

    @RequiresPermissions("ProductScore:add")
    @RequestMapping(value = "productScore", method = RequestMethod.GET)
    public ModelAndView uploadProductScorePage(Model model) {
        log.info("AdminUtilityController.uploadProductScorePage()");
        model.addAttribute(new UploadItem());
        Map<String, Object> viewData = new HashMap<String, Object>();
        viewData.put("uploadProductScore", true);
        return new ModelAndView("uploadProductScore", "viewData", viewData);
    }
    
    @RequiresPermissions("ProductScore:edit")
    @RequestMapping(value = "productScore/uploadProductScoreList", method = RequestMethod.POST)
    public ModelAndView uploadProductScoreFile(UploadItem uploadItem, BindingResult result) {
        log.info("AdminUtilityController.uploadProductScoreFile()");
        Map<String, Object> viewData = new HashMap<String, Object>();
        String message = "Product scored updated successfully.";
        CommonsMultipartFile fileData = uploadItem.getFileData();
        try {
            if (result.hasErrors()) {
                message = "File not uploaded successfully.";
            } else if (StringUtils.isBlank(fileData.getOriginalFilename())) {
                message = "Please select file";
            } else if (!fileData.getFileItem().getName().endsWith(".csv")) {
                message = "Please upload a CSV file";
            } else {
                String content = new String(fileData.getBytes());
                String contentLines[] = content.split(Constants.NEWLINE_REGEX);
                adminUtilityService.uploadProductScoreFile(contentLines);
            }
        } catch (Exception e) {
            message = "Error occurred while updating product scores:" + e.getMessage();
            log.error(message, e);
        }
        viewData.put("message", message);
        return new ModelAndView("uploadProductScore", "viewData", viewData);
    }
    
    @RequiresPermissions("ProductScore:view")
    @RequestMapping(value = "productScore/downloadSample", method = RequestMethod.GET)
    public void downloadSampleProductScoreFile(HttpServletRequest request,HttpServletResponse response){
    	log.info("AdminUtilityController.downloadSampleProductScoreFile()");
    	String sessionId = request.getSession().getId();
    	File file;
		try {
			file = adminUtilityService.writeProductScoreToFile(sessionId);
			DownloadUtil.copyFileDataToHttpResponse(file, response,"text/csv");
		} catch (Exception e) {
			log.error("Error occured while writing products score to file",e);
		}
    	
    }
        
}
