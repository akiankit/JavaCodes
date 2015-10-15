package com.bluestone.app.core;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.Banner.PageTypeForBanner;
import com.bluestone.app.design.service.HomePageService;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/")
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private HomePageService homePageService;

    /**
     * Simply selects the home view to render by returning its name.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home(Locale locale, Model model) {
        log.debug("HomeController.home()");
        Map<String, Object> home = homePageService.getHomePageProducts();
        String bannersHtml = homePageService.getBannersHtml(PageTypeForBanner.HOME);
        home.put("bannersHtml", bannersHtml);
        home.put("pageType", "home");
        return new ModelAndView("home", "home", home);
    }

}
