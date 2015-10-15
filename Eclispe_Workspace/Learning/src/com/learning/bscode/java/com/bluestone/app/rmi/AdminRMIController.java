package com.bluestone.app.rmi;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/admin/rmi*")
public class AdminRMIController {
	
	private static final Logger log = LoggerFactory.getLogger(AdminRMIController.class);

	@Autowired
	private RMIService rmiService;
	
	@RequestMapping(value = "/manage*", method = RequestMethod.GET)
	@RequiresPermissions("admin:rmi")
    public String manageRMI(ModelMap modelMap) {		
		modelMap.put("measureMentUnitsList", rmiService.getAllMeasurementUnits());
        return "manageRMI";
    }
	
	@RequestMapping(value = "/add/measurementUnit*", method = RequestMethod.POST)
	@RequiresPermissions("admin:rmi")
    public ModelAndView addMeasurementUnit(@ModelAttribute("MeasurementUnit") @Valid MeasurementUnit measurementUnit, BindingResult validationResult, ModelMap response) {
		rmiService.saveMeasurementUnit(measurementUnit);
        return new ModelAndView("manageRMI", "", "");
    }

	@RequestMapping(value = "/delete/measurementUnit", method = RequestMethod.POST)
	@RequiresPermissions("admin:rmi")
    public ModelAndView deleteMeasurementUnit(HttpServletRequest request,ModelMap response) {
		rmiService.deleteMeasurementUnit(Long.parseLong(request.getParameter("muid")));
        return new ModelAndView("manageRMI", "", "");
    }
	
}
