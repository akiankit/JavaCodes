package com.bluestone.app.admin.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.bluestone.app.admin.model.Property;
import com.bluestone.app.admin.service.PropertyService;

@Controller
@RequestMapping(value = { "/admin/property*" })
public class PropertyController {
	private static final Logger log = LoggerFactory.getLogger(PropertyController.class);

	@Autowired
	private PropertyService propertyService;

	@RequestMapping(value = "/list*", method = RequestMethod.GET)
	@RequiresPermissions("property:view")
	public ModelAndView listProperties() {
		log.info("PropertyController.listProperties()");
		Map<String, Object> propertyPage = new HashMap<String, Object>();
		try {
			Map<String, Property> propertyList = propertyService.getPropertyList();
			propertyPage.put("properties", propertyList);
		} catch (Exception e) {
			log.error("Error {} while listing properties " , e.getMessage(), e);
		}
		return new ModelAndView("propertyList", "propertyPage", propertyPage);
	}

	@RequestMapping(value = "/add*", method = RequestMethod.GET)
	@RequiresPermissions("property:add")
	public ModelAndView addProperty() {
		return new ModelAndView("property");
	}

	@RequestMapping(value = "/add*", method = RequestMethod.POST)
	@RequiresPermissions("property:add")
	public ModelAndView addUpdateProperty(@ModelAttribute("property") @Valid Property property) {
		Map<String, Object> propertyPage = new HashMap<String, Object>();
		try {
			if (propertyService.addUpdateProperty(property)) {
				propertyPage.put("message", "Add/Update successfull");
			} else {
				propertyPage.put("message", "Add/Update failed");
				propertyPage.put("property", property);
			}
		} catch (Exception e) {
			propertyPage.put("message", "Add/Update failed");
			propertyPage.put("property", property);
			log.error("Error {} while addUpdateProperty properties " , e.getMessage(), e);
		}
		return new ModelAndView("property", "propertyPage", propertyPage);

	}

	@RequestMapping(value = "/edit/{propertyname}", method = RequestMethod.GET)
	@RequiresPermissions("property:edit")
	public ModelAndView editProperty(@PathVariable("propertyname") String propertyName) {
		log.info("PropertyController.editProperty(): Property Name={}", propertyName);
		Map<String, Object> propertyPage = new HashMap<String, Object>();
		try {
			Property propertyByName = propertyService.getPropertyByName(propertyName);
			propertyPage.put("property", propertyByName);
		} catch (Exception e) {
			propertyPage.put("message", "Error while fetching property");
			log.error("Error {} while editProperty properties " , e.getMessage(), e);
		}
		return new ModelAndView("property", "propertyPage", propertyPage);
	}

	@RequestMapping(value = "/delete/{propertyname}", method = RequestMethod.GET)
	@RequiresPermissions("property:delete")
	public ModelAndView deleteProperty(@PathVariable("propertyname") String propertyName) {
		log.info("PropertyController.deleteProperty(): Property Name={}", propertyName);
		Map<String, Object> propertyPage = new HashMap<String, Object>();
		try {
			propertyPage.put("message", propertyService.deleteProperty(propertyName));
			Map<String, Property> propertyList = propertyService.getPropertyList();
			propertyPage.put("properties", propertyList);
		} catch (Exception e) {
			propertyPage.put("message", "Error while deleting property");
			log.error("Error {} while deleteProperty properties " , e.getMessage(), e);
		}
		return new ModelAndView("propertyList", "propertyPage", propertyPage);
	}

}
