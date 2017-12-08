package com.bluestone.app.shipping.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.account.controller.CustomerController;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.model.Locality;
import com.bluestone.app.shipping.service.ShippingService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/address*")
public class ShippingController {
    
    @Autowired
    private ShippingService     shippingService;
    
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    String getCityAndStateFromPincode(HttpServletRequest request, ModelMap modelMap) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        Gson gson = new Gson();
        
        String pincodeAsString = request.getParameter("pincode");
        Integer pincode = null; 
        try {
            pincode = Integer.parseInt(pincodeAsString);
        } catch (Exception e) {
            return gson.toJson(responseMap);
        }
        
        List<Locality> list = shippingService.getLocalityDetails(pincode);
        if (list.size() > 0) {
            responseMap.put("city", list.get(0).getCityId().getCityName());
            responseMap.put("id_state", list.get(0).getCityId().getStateId().getId());
            responseMap.put("statename", list.get(0).getCityId().getStateId().getStateName());
        }
        
        return gson.toJson(responseMap);
    }
    
    @RequestMapping(value = "saveAddress", method = RequestMethod.POST)
    public @ResponseBody
    String createOrUpdateAddress(@ModelAttribute("address") @Valid Address newAddress, BindingResult validationResult, HttpServletRequest request, ModelMap modelMap) {
        log.debug("ShippingController.createOrUpdateAddress()");
        Map<String, Object> responseMap = new HashMap<String, Object>();
        ArrayList<String> errorList = new ArrayList<String>();
        responseMap.put(Constants.HAS_ERROR, false);
        
        long stateId = Long.parseLong(request.getParameter("stateId"));
        newAddress.setState(shippingService.getStateById(stateId));
        
        if (validationResult.hasErrors()) { // check for address data validation
                                            // here
            responseMap.put(Constants.HAS_ERROR, true);
            for (ObjectError error : validationResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
        }
        Customer loggedInCustomer = (Customer) SecurityUtils.getSubject().getPrincipal();
        if (loggedInCustomer == null) {
                errorList.add("You need to be logged in to be able to change your address.");
        } else {
            Address latestShippingAddress = shippingService.getLatestShippingAddress(loggedInCustomer);
            if (latestShippingAddress == null || !newAddress.equals(latestShippingAddress)) {
                // create new address
                newAddress.setId(0);
                newAddress.setCustomerId(loggedInCustomer);
                log.debug("ShippingController:Create a new Shipping address for CustomerId={} using Address", loggedInCustomer.getId(), newAddress);
                shippingService.createAddress(newAddress);
            }
            // shippingService.createAddress(newAddress);
        }
        responseMap.put(Constants.ERRORS, errorList);
        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }
}
