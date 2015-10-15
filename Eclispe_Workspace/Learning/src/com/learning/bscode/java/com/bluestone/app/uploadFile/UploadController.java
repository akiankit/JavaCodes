package com.bluestone.app.uploadFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.CarrierService;
import com.bluestone.app.core.util.Constants;

@Controller
@RequestMapping(value = "/admin/upload*")
public class UploadController {

	private static final Logger log = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private CarrierService carrierService;

	@RequestMapping(value = "pincode", method = RequestMethod.GET)
	@RequiresPermissions("carrier:add")
	public ModelAndView getUploadForm(Model model) {
		log.info("UploadController.getUploadForm()");
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();
		ListFilterCriteria filterCriteria = new ListFilterCriteria();
		carrierDetails = carrierService.getCarrierDetails(filterCriteria, true);
		carrierDetails.put("loadFileUploader", true);
		model.addAttribute(new UploadItem());
		carrierDetails.put("pageTitle", "Carrier Management");
		return new ModelAndView("carrierlist", "viewData", carrierDetails);
	}

	@RequestMapping(value = "submitpincode", method = RequestMethod.POST)
	@RequiresPermissions("carrier:add")
	public ModelAndView create(UploadItem uploadItem, BindingResult result) {
		log.info("UploadController.create()");
		ArrayList<String> errors = new ArrayList<String>();
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();
		ListFilterCriteria filterCriteria = new ListFilterCriteria();
		carrierDetails = carrierService.getCarrierDetails(filterCriteria, true);
		carrierDetails.put("loadFileUploader", true);
		CommonsMultipartFile fileData = uploadItem.getFileData();
		boolean isSuccess = false;
		if (result.hasErrors()) {
			carrierDetails.put("errors", result.getAllErrors());
		} else if (StringUtils.isBlank(fileData.getOriginalFilename())) {
			errors.add("Please select file");
		} else if (!fileData.getFileItem().getName().contains(".csv")) {
			errors.add("Please upload a CSV file");
		} else {
			String content = new String(fileData.getBytes());
			String contentLines[] = content.split(Constants.NEWLINE_REGEX);
			Set<Long> carrierIds = carrierService.getCarrierIdsFromFile(contentLines);
			String carrierIdValidationMsg = carrierService.validateCarrierIds(carrierIds);
			try {
				if (!StringUtils.isBlank(carrierIdValidationMsg)) {
					errors.add(carrierIdValidationMsg);
				} else if (uploadItem.isRefreshList() == true) {
					carrierService.refreshPincodesForCarrier(contentLines, carrierIds);
					isSuccess = true;
				} else {
					carrierService.savePincodesForCarrier(contentLines);
					isSuccess = true;
				}
			} catch (Exception e) {
				log.error("Error during pincode upload", e);
				errors.add(carrierService.getFormattedErrorMessage(e.getMessage()));
			}
		}
		if (isSuccess) {
			carrierDetails.put("message", "File has been uploaded successfully");
		}
		carrierDetails.put("pageTitle", "Carrier Management");
		carrierDetails.put("errors", errors);
		return new ModelAndView("carrierlist", "viewData", carrierDetails);
	}
}
