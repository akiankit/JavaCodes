package com.bluestone.app.admin.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.model.User;
import com.bluestone.app.account.security.CustomerSpecificCheckService;
import com.bluestone.app.core.HttpRequestParser;


/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminHomeController {

    private static final Logger log = LoggerFactory.getLogger(AdminHomeController.class);

    @Autowired
    private CustomerSpecificCheckService customerSpecificCheckService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home(Locale locale, Model model, HttpServletResponse response, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            // users with just customer role should be barred.
            boolean isLoggedInCustomer = customerSpecificCheckService.hasOnlyACustomerRole(subject);

            if (isLoggedInCustomer) {
                User user = (User) subject.getPrincipal();
                log.info("AdminHomeController: User={} with Role={} tried to access /admin [{}]",
                         user, HttpRequestParser.dumpHeadersForLogging(request));
                throw new UnauthorizedException();
            }
        } else {
            log.info("AdminHomeController.home(): Unknown User tried to access /admin. {}", HttpRequestParser.dumpHeadersForLogging(request));
            throw new UnauthenticatedException();
        }
        log.info("AdminHomeController.home() for {} {}", subject.getPrincipal(), HttpRequestParser.dumpBriefHeadersForLogging(request));
        return new ModelAndView("adminHome");
    }

}
