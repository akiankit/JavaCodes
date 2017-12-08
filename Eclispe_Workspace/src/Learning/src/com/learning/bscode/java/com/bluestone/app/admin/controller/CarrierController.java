package com.bluestone.app.admin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.Carrier;
import com.bluestone.app.admin.model.CarrierZipcode;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.CarrierService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.core.util.Util;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = { "/admin/carrier*", "/carrier*" })
public class CarrierController {
	private static final Logger log = LoggerFactory.getLogger(CarrierController.class);

	@Autowired
	private CarrierService carrierService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		log.trace("CarrierController.initBinder()");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@RequiresPermissions("carrier:add")
	public ModelAndView getCarrierMainPage(HttpServletRequest request) {
		log.debug("CarrierController.getCarrierMainPage()");
		Map<String,Object> viewData = new HashMap<String, Object>();
		viewData.put("pageTitle", "Carrier Management");
		return new ModelAndView("addUpdateCarrier","viewData",viewData);
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@RequiresPermissions("carrier:view")
	public ModelAndView getCarrierListPage(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
        log.debug("CarrierController.getCarrierListPage()");
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();
		try {
			carrierDetails = carrierService.getCarrierDetails(filterCriteria, true); 
			carrierDetails.put("requestURL", request.getRequestURL());
		} catch (Exception e) {
			log.error("Error during CarrierController.getCarrierListPage(): Reason: {}", e.toString(), e);
		}
		carrierDetails.put("pageTitle", "Carrier Management");
		return new ModelAndView("carrierlist", "viewData", carrierDetails);
	}

	@RequestMapping(value = "disabledlist", method = RequestMethod.GET)
	@RequiresPermissions("carrier:view")
	public ModelAndView getCarrierDisabledListPage(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
        log.debug("CarrierController.getCarrierDisabledListPage()");
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();
		try {
			carrierDetails = carrierService.getCarrierDetails(filterCriteria , false); 
			carrierDetails.put("requestURL", request.getRequestURL());
		} catch (Exception e) {
			log.error("Error during CarrierController.getCarrierListPage(): Reason: {}", e.toString(), e);
		}
		carrierDetails.put(Constants.MESSAGE, "List of disabled carriers");
		carrierDetails.put("isDisabledList", true);
		carrierDetails.put("pageTitle", "Carrier Management");
		return new ModelAndView("carrierlist", "viewData", carrierDetails);
	}

	@RequestMapping(value = "/enable/carrier/{carrierid}", method = RequestMethod.POST)
	@RequiresPermissions("carrier:edit")
	public @ResponseBody String enableCarrier(@PathVariable("carrierid") Long carrierId) {
        log.debug("CarrierController.enableCarrier() : CarrierId={}", carrierId);
		boolean hasError = false;
		String message = "Carrier enabled successfully.";
		try {
			carrierService.enableCarrier(carrierId);
		} catch (Exception e) {
			hasError = true;
			message = "An error occurred while enabling carrier.";
			log.error("{} : Reason {} ", message, e.getMessage(), e);
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put(Constants.HAS_ERROR, hasError);
		response.put(Constants.MESSAGE, message);
		response.put("redirectUrl", Util.getSiteUrlWithContextPath() + "/admin/carrier/list");
		return new Gson().toJson(response);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresPermissions("carrier:add")
	public ModelAndView saveCarrier(@ModelAttribute("carrier") Carrier newCarrier, HttpServletRequest request) {
		log.debug("CarrierController.saveCarrier()");
		String message = "Carrier added successfully.";
		if (newCarrier.getId() == 0) {
			try {
				carrierService.createCarrier(newCarrier);
			} catch (Exception e) {
				message = "An error occurred while creating carrier.";
				log.error("Error occurred while creating carrier={}  {}", newCarrier, e.toString(), e);
			}
		} else {
			message = "Carrier updated successfully.";
			try {
				carrierService.updateCarrier(newCarrier);
			} catch (Exception e) {
				message = "An error occurred while updating carrier.";
				log.error("Error occured while updating carrier={} {} : Reason : {} ",newCarrier, message, e.getMessage(), e);
			}
		}
		
		Map<Object, Object> carrierDetails = new HashMap<Object, Object>();
		ListFilterCriteria filterCriteria = new ListFilterCriteria();
		carrierDetails = carrierService.getCarrierDetails(filterCriteria, true); 
		carrierDetails.put("requestURL", Util.getSiteUrlWithContextPath()+"/admin/carrier/list");
		carrierDetails.put(Constants.MESSAGE, message);
		carrierDetails.put("pageTitle", "Carrier Management");
		return new ModelAndView("carrierlist", "viewData", carrierDetails);
	}

	@RequestMapping(value = "/edit/{carrierid}", method = RequestMethod.GET)
	@RequiresPermissions("carrier:edit")
	public ModelAndView editCarrier(@PathVariable("carrierid") Long carrierId) {
		log.debug("CarrierController.editCarrier() id={}", carrierId);
		Map<String, Object> viewData = new HashMap<String, Object>();
		Carrier carrierById = carrierService.getCarrierById(carrierId, true);
		viewData.put("carrier", carrierById);
		viewData.put("pageTitle", "Carrier Management");
		return new ModelAndView("addUpdateCarrier", "viewData", viewData);
	}

	@RequestMapping(value = "/disable/{carrierid}", method = RequestMethod.POST)
	@RequiresPermissions("carrier:delete")
	public @ResponseBody String disablecarrier(@PathVariable("carrierid") Long carrierId) {
		log.debug("CarrierController.disablecarrier() id={}", carrierId);
		boolean hasError = false;
		String message = "Carrier disabled successfully.";
		try {
			carrierService.disableCarrier(carrierId);
		} catch (Exception e) {
			hasError = true;
			message = "An error occurred while disabling carrier.";
			log.error("{} : Reason {}", message, e.getMessage(), e);
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put(Constants.HAS_ERROR, hasError);
		response.put(Constants.MESSAGE, message);
		response.put("redirectUrl", Util.getSiteUrlWithContextPath() + "/admin/carrier/list");
        return new Gson().toJson(response);
	}

	@RequestMapping(value = "/pincodes/list", method = RequestMethod.GET)
	@RequiresPermissions("carrier:view")
	public ModelAndView getPincodesList(@ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,HttpServletRequest request) {
		
		log.debug("CarrierController.getPincodesList()");
		Map<Object, Object> pincodesDetails = new HashMap<Object, Object>();
		try {
			pincodesDetails = carrierService.getPincodeDetails(filterCriteria, true);
			pincodesDetails.put("requestURL", request.getRequestURL());
		} catch (Exception e) {
			log.error("Error during CarrierController.getPincodesList: Reason: {}", e.toString(), e);
		}
		pincodesDetails.put("pageTitle", "Carrier Management");
		return new ModelAndView("carrierPincodes", "viewData", pincodesDetails);
	}

	@RequestMapping(value = "/zipcode/showaddpage", method = RequestMethod.GET)
	@RequiresPermissions("carrier:add")
	public ModelAndView addCarrierZipcode() {
		log.debug("CarrierController.addCarrierZipcode()");
		Map<String, Object> viewData = new HashMap<String, Object>();
		List<Carrier> carrierList = carrierService.getCarrierList(true);
		viewData.put("unSelectedCarrierList", carrierList);
		viewData.put("pageTitle", "Carrier Management");
		return new ModelAndView("addUpdatePincode", "viewData", viewData);
	}

	@RequestMapping(value = "/zipcode/showupdatepage/{carrierzipid}", method = RequestMethod.GET)
	@RequiresPermissions("carrier:edit")
	public ModelAndView updateCarrierZipcode(@PathVariable("carrierzipid") Long carrierZipId) {
		log.debug("CarrierController.updateCarrierZipcode()");
		Map<String, Object> viewData = new HashMap<String, Object>();
		CarrierZipcode carrierZipcode = carrierService.getCarrierZipcodeById(carrierZipId, true);
		viewData.put("carrierZipcode", carrierZipcode);
		viewData.put("selectedCarrierZipcodeList",
				carrierService.getSelectedCarrierZipcodeListByPincode(carrierZipcode.getZipcode()));
		viewData.put("unSelectedCarrierList",
				carrierService.getUnselectedCarrierListByPincode(carrierZipcode.getZipcode()));
		return new ModelAndView("addUpdatePincode", "viewData", viewData);
	}

	@RequestMapping(value = "/zipcode/delete/{carrierzipid}", method = RequestMethod.POST)
	@RequiresPermissions("carrier:delete")
	public @ResponseBody String deleteCarrierZipcode(@PathVariable("carrierzipid") Long carrierZipcodeId, HttpServletRequest request) {
		log.debug("CarrierController.deleteCarrierZipcode()");
		boolean hasError = false;
		String message = "Pincode deleted successfully";
		String redirectUrl = request.getHeader("referer");
		try {
			carrierService.deleteCarrierZipcodeByCarrierZipcodeId(carrierZipcodeId);

		} catch (Exception e) {
			hasError = false;
			log.error("Error in deleting the carrier zipcode {} : Reason={}", Long.toString(carrierZipcodeId),
					e.toString(), e);
			message = "Some problem occured while deleting pincode.";
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("redirectUrl", redirectUrl);
		response.put(Constants.MESSAGE, message);
		response.put(Constants.HAS_ERROR, hasError);
        return new Gson().toJson(response);
	}

	@RequestMapping(value = "/zipcode/save", method = RequestMethod.POST)
	@RequiresPermissions("carrier:add")
	public @ResponseBody String saveCarrierZipcode(HttpServletRequest request) {
		log.debug("CarrierController.saveCarrierZipcode()");
		boolean hasError = false;
		String message;
		String zipcode = request.getParameter("zipcode");
		int id = Integer.parseInt(request.getParameter("id"));
		String isCarrierSupported[] = request.getParameterValues("isCarrierSupported[]");
		String carrierIds[] = request.getParameterValues("carrierIds[]");
		String isCodAllowed[] = request.getParameterValues("isCodAllowed[]");
		String redirectUrl = request.getHeader("referer");
		try {
			if (id != 0) {
				carrierService.deleteCarrierZipcodesByZipcode(zipcode);
			}
			carrierService.saveCarrierZipcode(isCarrierSupported, carrierIds, isCodAllowed, zipcode);
		} catch (Exception e) {
			hasError = true;
			log.error("Error while saving carrier pincode info for {}. Reason={}", zipcode, e.getMessage(), e);
			message = "Some Error occured while saving pincode."; 
		}
		if (id == 0) { // @todo rahul check with team , if the id wud come as
						// zero ??????
			message = "Pincode added successfully";
		} else {
			message = "Pincode updated successfully";
		}
		Map<String, Object> response = new HashMap<String, Object>();
		response.put(Constants.HAS_ERROR, hasError);
		response.put(Constants.MESSAGE, message);
		response.put("redirectUrl", redirectUrl);
        return new Gson().toJson(response);
	}

	@RequestMapping(value = "/availability*", method = RequestMethod.POST)
	public @ResponseBody String checkCarrierAvailabilty(HttpServletRequest request, HttpServletResponse response) {
		log.debug("CarrierController.checkCarrierAvailability");
	    Map<String,Object> responseMap = new HashMap<String,Object>();
		String pincode = request.getParameter("pincode");
		String mode = request.getParameter("mode");
		double cartTotal = Double.parseDouble(request.getParameter("cartTotal"));
		String errorStr = "";
		String successStr = "";
		if ("prepaid".equals(mode)) {
			Carrier applicableCarrier = carrierService.getApplicableCarrier(pincode, cartTotal);
			Double maxPrepaidLimit = carrierService.getMaxPrepaidLimit(pincode);
			if (applicableCarrier == null) {
				responseMap.put(Constants.HAS_ERROR, true);
				if (maxPrepaidLimit != null) {
					errorStr = "Maximum order amount for the pincode (" + pincode + ") is Rs. "
							+ NumberUtil.formatPriceIndian(maxPrepaidLimit, ",");
				} else {
					errorStr = "Sorry! Our third party delivery services do not deliver to the pincode ("
							+ pincode
							+ "). We are working on it. Please try an alternative pincode where the shipment can be delivered.";
				}
				responseMap.put("errorStr", errorStr);
			} else {
				if (maxPrepaidLimit != null) {
					if (maxPrepaidLimit == -1) {
						successStr = "Yes we deliver to the pincode (" + pincode + ").";
						responseMap.put(Constants.HAS_ERROR, false);
						responseMap.put("successStr", successStr);
					} else {
						if (cartTotal > maxPrepaidLimit) {
							errorStr = "Maximum order amount for the pincode (" + pincode + ") is Rs. "
									+ NumberUtil.formatPriceIndian(maxPrepaidLimit, ",");
							responseMap.put("errorStr", errorStr);
							responseMap.put(Constants.HAS_ERROR, true);
						} else {
							successStr = "Yes we deliver to the pincode (" + pincode + ").";
							responseMap.put(Constants.HAS_ERROR, false);
							responseMap.put("successStr", successStr);
						}
					}
				} else {
					responseMap.put(Constants.HAS_ERROR, true);
					errorStr = "Sorry! Our third party delivery services do not deliver to the pincode ("
							+ pincode
							+ "). We are working on it. Please try an alternative pincode where the shipment can be delivered.";
					responseMap.put("errorStr", errorStr);
				}
			}
		} else if ("cod".equals(mode)) {
			Carrier applicableCarrierForCOD = carrierService.getApplicableCarrierForCOD(pincode, cartTotal);
			Double maxCODLimit = carrierService.getMaxCODLimit(pincode, cartTotal);
			if (applicableCarrierForCOD == null) {
				if (maxCODLimit != null) {
					errorStr = "Maximum COD order amount for the pincode (" + pincode + ") is "
							+ NumberUtil.formatPriceIndian(maxCODLimit, ",");
				} else {
					errorStr = "We are sorry, we do not deliver to this pincode (" + pincode
							+ "). Please try another pincode.";
				}
				responseMap.put("hasCODError", true);
				responseMap.put("errorStr", errorStr);
			} else {
				if (maxCODLimit != null) {
					if (cartTotal > maxCODLimit) {
						responseMap.put("hasCODError", true);
						errorStr = "Maximum COD order amount for the pincode (" + pincode + ") is "
								+ NumberUtil.formatPriceIndian(maxCODLimit, ",");
						responseMap.put("errorStr", errorStr);
					} else {
						responseMap.put("hasCODError", false);
						successStr = "Yes we deliver to this pincode (" + pincode + ").";
						responseMap.put("successStr", successStr);
					}
				} else {
					responseMap.put("hasCODError", true);
					errorStr = "We are sorry, we do not deliver to this pincode (" + pincode
							+ "). Please try another pincode.";
					responseMap.put("errorStr", errorStr);
				}
			}
		}
		Gson gson = new Gson();
		return gson.toJson(responseMap);
	}

	@RequestMapping(value = "/availability-faq*", method = RequestMethod.POST)
	public @ResponseBody String checkCarrierAvailabiltyFaq(HttpServletRequest request, HttpServletResponse response) {
		log.debug("CarrierController.checkCarrierAvailability()");
	    Map<String,Object> responseMap = new HashMap<String, Object>();
	    
		String pincode = request.getParameter("pincode");
		String mode = request.getParameter("mode");
		double cartTotal = 0;
		String errorStr = "";
		String successStr = "";
		if ("prepaid".equals(mode)) {
			Carrier applicableCarrier = carrierService.getApplicableCarrier(pincode, cartTotal);
			Double maxPrepaidLimit = carrierService.getMaxPrepaidLimit(pincode);
			if (applicableCarrier == null) {
				errorStr = "We are sorry, we do not deliver here. Please try another pincode.";
				responseMap.put(Constants.HAS_ERROR, true);
				responseMap.put("errorStr", errorStr);
			} else {
				if (maxPrepaidLimit != null) {
					if (maxPrepaidLimit == -1) {
						successStr = "Yes we deliver to the pincode (" + pincode + ").";
					} else {
						successStr = "Delivery available for this location (" + pincode + ") for upto Rs. "
								+ NumberUtil.formatPriceIndian(maxPrepaidLimit, ",") + "/-";
					}
					responseMap.put(Constants.HAS_ERROR, false);
					responseMap.put("successStr", successStr);
				} else {
					errorStr = "We are sorry, we do not deliver to this pincode (" + pincode
							+ "). Please try another pincode.";
					responseMap.put(Constants.HAS_ERROR, true);
					responseMap.put("errorStr", errorStr);
				}
			}
		} else if ("cod".equals(mode)) {
			Carrier applicableCarrierForCOD = carrierService.getApplicableCarrierForCOD(pincode, cartTotal);
			Double maxCODLimit = carrierService.getMaxCODLimit(pincode, cartTotal);
			if (applicableCarrierForCOD == null) {
				errorStr = "We are sorry, we do not deliver to this pincode (" + pincode
						+ "). Please try another pincode.";
				responseMap.put("hasCODError", true);
				responseMap.put("errorStr", errorStr);
			} else {
				if (maxCODLimit != null) {
					successStr = "Cash on delivery is available for this location (" + pincode + ") for upto Rs. "
							+ NumberUtil.formatPriceIndian(maxCODLimit, ",") + "/-";
					responseMap.put("hasCODError", false);
					responseMap.put("successStr", successStr);
				} else {
					errorStr = "We are sorry, we do not deliver to this pincode (" + pincode
							+ "). Please try another pincode.";
					responseMap.put("hasCODError", true);
					responseMap.put("errorStr", errorStr);
				}
			}
		}
		Gson gson = new Gson();
		return gson.toJson(responseMap); 
	}
}
