package com.bluestone.app.popup.controller;

import static com.bluestone.app.popup.PopUpConstant.EMPTY_PHONE_NO_ERROR_MESSAGE;
import static com.bluestone.app.popup.PopUpConstant.EMPTY_STRING;
import static com.bluestone.app.popup.PopUpConstant.PATTERN_PHONE_NO;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.popup.service.guideshop.GuideShopMailService;
import com.google.gson.Gson;

@Controller
@RequestMapping("/popup")
public class PopupController {

	@Autowired
	private GuideShopMailService guideShopMailService;

	private static final Logger log = LoggerFactory
			.getLogger(PopupController.class);

	@RequestMapping(value = "/guideshop", method = RequestMethod.GET)
	public ModelAndView showGuideShopPopUp() {
		return new ModelAndView("guideshop");
	}

	@RequestMapping(value = "/guideshop", method = RequestMethod.POST)
	public @ResponseBody
	String submitGuideShopInterest(HttpServletRequest request,
			HttpServletResponse response) 
	{
		Map<String, Object> viewData = new HashMap<String, Object>();
		String mobileNo = null;
		try
		{
			mobileNo = request.getParameter("userMobile");
			if(mobileNo!= null && !EMPTY_STRING.equals(mobileNo))
			{
				if (PATTERN_PHONE_NO.matcher(mobileNo).matches()) 
				{
					guideShopMailService.sendInternalMail(mobileNo);
					viewData.put(Constants.HAS_ERROR, false);
				} 
				else 
				{
					viewData.put(Constants.HAS_ERROR, true);
					viewData.put(Constants.MESSAGE,
							Constants.INVALID_PHONE_NO_ERROR_MESSAGE);
				}
			}
			else
			{
				viewData.put(Constants.HAS_ERROR, true);
				viewData.put(Constants.MESSAGE, EMPTY_PHONE_NO_ERROR_MESSAGE);
			}
			
		} 
		catch (Exception e) 
		{
			viewData.put(Constants.HAS_ERROR, false);
			viewData.put(Constants.MESSAGE,
					"Sorry there was some error. Please try again.");
			log.error(
					"PopupController.submitGuideShopInterest(). Error occured while sending Guide shop enquiry mail from . {} ",
					mobileNo, e);
		}

		return new Gson().toJson(viewData);
	}
}
