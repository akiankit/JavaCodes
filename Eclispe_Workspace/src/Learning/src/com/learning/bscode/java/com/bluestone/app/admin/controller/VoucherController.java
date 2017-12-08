package com.bluestone.app.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.service.VoucherService;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = {"/admin/voucher*"})
public class VoucherController {

	private static final Logger log = LoggerFactory.getLogger(VoucherController.class);

	@Autowired
	private VoucherService voucherService;

	@InitBinder
	public void initBinder(WebDataBinder dataBinder) {
		dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		dateFormat.setLenient(false);
		dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(value = "/list*", method = RequestMethod.GET)
	@RequiresPermissions("voucher:view")
	public ModelAndView listVouchers(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
		log.debug("VoucherController.listVouchers()");
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();
		try {
			voucherDetails = voucherService.getVoucherDetails(filterCriteria, true);
			voucherDetails.put("requestURL", request.getRequestURL());
		} catch (Exception e) {
			log.error("Error during VoucherController.listVouchers(): Reason: {}", e.toString(), e);
		}
		voucherDetails.put("pageTitle", "Vouchers");
		return new ModelAndView("vouchersList", "viewData", voucherDetails);
	}

	@RequestMapping(value = "/disabledlist*", method = RequestMethod.GET)
	@RequiresPermissions("voucher:view")
	public ModelAndView DisabledListVouchers(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
		log.debug("VoucherController.disabledListVouchers()");
		
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();
		try {
			voucherDetails = voucherService.getVoucherDetails(filterCriteria,false);
			voucherDetails.put("requestURL", request.getRequestURL());
		} catch (Exception e) {
			log.error("Error during VoucherController.disabledlistVouchers(): Reason: {}", e.toString(), e);
		}
		voucherDetails.put("message", "List of disabled vouchers.");
		voucherDetails.put("pageTitle", "Vouchers");
		voucherDetails.put("isDisabledList", true);
		return new ModelAndView("vouchersList", "viewData", voucherDetails);
	}

	@RequestMapping(value = "/add*", method = RequestMethod.GET)
	@RequiresPermissions("voucher:add")
	public ModelAndView addVoucher() {
		log.debug("VoucherController.addVoucher()");
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();
		voucherDetails.put("pageTitle", "Vouchers");
		return new ModelAndView("addVoucher","viewData",voucherDetails);
	}

	@RequestMapping(value = "/save*", method = RequestMethod.POST)
	@RequiresPermissions("voucher:add")
	public ModelAndView createVoucher(@ModelAttribute("discountVoucher") @Valid DiscountVoucher voucher,BindingResult result,HttpServletRequest request) {
		log.debug("VoucherController.createVoucher()");
		String message = voucher.getId()==0 ? "Voucher created successfully":"Voucher updated successfully";
		Map<Object, Object> voucherDetails = new HashMap<Object, Object>();
		voucherDetails.put("voucher", voucher);
		List<String> validationErrors = voucherService.validateVoucherForSave(voucher);
		if (result.hasErrors()) {
			voucherDetails.put("errorsList", result.getAllErrors());
			return new ModelAndView("addVoucher", "viewData", voucherDetails);
		} else if (validationErrors != null && validationErrors.size() > 0) {
			voucherDetails.put("errorsList", validationErrors);
			return new ModelAndView("addVoucher", "viewData", voucherDetails);
		}
		try {
			voucherService.save(voucher);
            log.info("VoucherController.createVoucher(): [{}]", voucher.toString());
		} catch (Exception e) {
			message = "Some problem occurred while saving voucher";
			log.error("{} : Reason {} ", message, e.getMessage(), e);
			return new ModelAndView("addVoucher", "viewData", voucherDetails);
		}
		ListFilterCriteria filterCriteria = new ListFilterCriteria();
		Map<Object, Object> viewData = voucherService.getVoucherDetails(filterCriteria, true); 
		viewData.put("requestURL", Util.getSiteUrlWithContextPath()+"/admin/voucher/list");
		viewData.put(Constants.MESSAGE, message);
		viewData.put("pageTitle", "Vouchers");
		return new ModelAndView("vouchersList", "viewData", viewData);
	}

	@RequestMapping(value = "/edit/{voucherId}", method = RequestMethod.GET)
	@RequiresPermissions("voucher:edit")
	public ModelAndView editVoucher(@PathVariable("voucherId") Long voucherId) {
		DiscountVoucher voucher = voucherService.getVoucher(voucherId);
        log.info("VoucherController.editVoucher(): Voucher Id = {} Code=[{}]", voucherId, voucher.getName());
        Map<Object, Object> viewData = new HashMap<Object, Object>();
		viewData.put("voucher", voucher);
		viewData.put("pageTitle", "Vouchers");
		return new ModelAndView("addVoucher", "viewData", viewData);
	}

	@RequestMapping(value = "/disable/{voucherId}", method = RequestMethod.POST)
	@RequiresPermissions("voucher:edit")
	public @ResponseBody
	String disableVoucher(@PathVariable("voucherId") Long voucherId) {
		DiscountVoucher voucher = voucherService.getVoucher(voucherId);
        log.info("VoucherController.disableVoucher(): Voucher Id =[{}] Voucher Code=[{}]", voucherId, voucher.getName());
        boolean hasError = false;
		String message = "";
		try {
			message = voucherService.validateVoucherForDisable(voucher);
			if (StringUtils.isBlank(message)) {
				voucherService.disableVoucher(voucher);
				message = "Voucher disabled successfully";
			}
		} catch (Exception e) {
			log.error("Error occured while disabling voucher (" + voucher.toString() + ")" + e);
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put(Constants.HAS_ERROR, hasError);
		response.put(Constants.MESSAGE, message);
		response.put("redirectUrl", Util.getSiteUrlWithContextPath() + "/admin/voucher/list");
		return new Gson().toJson(response);
	}

}
