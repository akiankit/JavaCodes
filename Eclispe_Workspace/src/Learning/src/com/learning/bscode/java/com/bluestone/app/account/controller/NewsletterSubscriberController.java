package com.bluestone.app.account.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.account.NewsletterSubscriberService;
import com.bluestone.app.account.model.NewsletterSubscriber;
import com.bluestone.app.core.HttpRequestParser;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;

@Controller
@RequestMapping("/newsletter*")
public class NewsletterSubscriberController {

    private static final Logger log = LoggerFactory.getLogger(NewsletterSubscriberController.class);

    @Autowired
    private NewsletterSubscriberService subscriberService;

    @Qualifier(value = "emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;

    @RequestMapping(value = "/subscribe*", method = RequestMethod.POST)
    public
    @ResponseBody
    String subscribe(HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
        log.debug("NewsletterSubscriberController.subscribe()");
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        responseMap.put(Constants.HAS_ERROR, true);

        String email = request.getParameter("email");
        if (StringUtils.isNotBlank(email)) {
            try {
                String trimmedEmail = email.trim();
                NewsletterSubscriber existingSubscriber = subscriberService.getSubscriberByEmailId(trimmedEmail);
                if (existingSubscriber == null) {
                    NewsletterSubscriber subscriber = new NewsletterSubscriber();
                    subscriber.setEmail(trimmedEmail);
                    subscriber.setIpAddress(Util.getMessageContext().getClientIP());
                    subscriberService.addSubscriber(subscriber);
                    responseMap.put(Constants.HAS_ERROR, false);
                    responseMap.put("success", "Thank you for subscribing to our newsletter.");
                } else {
                    responseMap.put("error", "You have already subscribed to our newsletter");
                }
            } catch (Exception exception) {
                log.error("Error: NewsletterSubscriberController.subscribe(): for email=[{}] ", email, exception);
                responseMap.put("error", "Sorry some error happened. Please try again.");
                productionAlert.send("Failed to subscribe the user " + email + " for the newsletter.", exception);

            }
        } else {
            responseMap.put("error", "Invalid email id. It should not be empty.");
            log.error("Error: NewsletterSubscriberController.subscribe(): Email Id =[{}] should not be empty. ", email);
        }

        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }
}
