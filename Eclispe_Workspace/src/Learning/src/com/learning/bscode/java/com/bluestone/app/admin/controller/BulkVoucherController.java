package com.bluestone.app.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.core.util.DownloadUtil;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.service.BulkVoucherService;

@Controller
@RequestMapping(value = {"/admin/bulkVoucher*"})

public class BulkVoucherController {
	private static final String BULK_VOUCHERS_FILE_PREFIX = "BulkVouchers";
	private static final Logger log = LoggerFactory.getLogger(BulkVoucherController.class);
	@Autowired
	private BulkVoucherService bulkVoucherService;
	
	@RequestMapping(value = "/add*", method = RequestMethod.GET)
	@RequiresPermissions("bulkvoucher:add")
	public ModelAndView addBulkVoucher() {
		log.debug("VoucherController.addBulkVoucher()");
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();
		voucherDetails.put("pageTitle", "Bulk Vouchers");
		return new ModelAndView("addBulkVoucher","viewData",voucherDetails);
	}
	
	@RequestMapping(value = "/save*", method = RequestMethod.POST)
	@RequiresPermissions("bulkvoucher:add")
	public ModelAndView createBulkVoucher(
			@RequestParam(value = "noofvoucher", required = false, defaultValue = "0") Integer noOfVoucher,
			@RequestParam(value = "prefix", required = false, defaultValue = "") String prefix,
			@RequestParam(value = "vocherCodeLength", required = false, defaultValue = "0") Integer voucherCodeLenght,
			@ModelAttribute("discountVoucher") @Valid DiscountVoucher voucher,BindingResult result,HttpServletRequest request,HttpServletResponse response) {
		log.debug("VoucherController.createVoucher()");
		String message = voucher.getId()==0 ? "Voucher created successfully":"Voucher updated successfully";
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();
		voucherDetails.put("voucher", voucher);
		voucherDetails.put("noOfVoucher", noOfVoucher);
		voucherDetails.put("voucherCodeLenght", voucherCodeLenght);
		voucherDetails.put("prefix", prefix);
		List<String> validationErrors = bulkVoucherService.validateBulkVoucherForSave(voucher,voucherCodeLenght,noOfVoucher,prefix);
		if (result.hasErrors()) {
			voucherDetails.put("errorsList", result.getAllErrors());
			return new ModelAndView("addBulkVoucher", "viewData", voucherDetails);
		} else if (validationErrors != null && validationErrors.size() > 0) {
			voucherDetails.put("errorsList", validationErrors);
			return new ModelAndView("addBulkVoucher", "viewData", voucherDetails);
		}
		try {
			List<DiscountVoucher> createBulkVoucher = bulkVoucherService.createBulkVoucher(voucher, voucherCodeLenght, noOfVoucher, prefix);
			String bulkVochers = bulkVoucherService.writeVochersToFile(createBulkVoucher);
			DownloadUtil.copyDataToHttpResponse(BULK_VOUCHERS_FILE_PREFIX, response, "text/csv", bulkVochers, ".csv");
		} catch (Exception e) {
			message = "Some problem occurred while saving voucher reason=("+e.getMessage()+")";
			List<String> errorList = new ArrayList<String>();
			errorList.add(message);
			voucherDetails.put("errorsList", errorList);
			log.error("{} : Reason {} ", message, e.getMessage(), e);
			return new ModelAndView("addBulkVoucher", "viewData", voucherDetails);
		}
			return new ModelAndView("vouchersList");
	}

	
}
