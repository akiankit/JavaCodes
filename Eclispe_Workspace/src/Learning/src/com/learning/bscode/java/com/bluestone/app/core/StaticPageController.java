package com.bluestone.app.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/static")
public class StaticPageController {

    private static final Logger log = LoggerFactory.getLogger(StaticPageController.class);

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ModelAndView showStaticPage(@PathVariable("page") String page) {
        log.debug("StaticPageController.showStaticPage() - {}", page);
        return new ModelAndView(page, "staticPage", page);
    }
    

}
