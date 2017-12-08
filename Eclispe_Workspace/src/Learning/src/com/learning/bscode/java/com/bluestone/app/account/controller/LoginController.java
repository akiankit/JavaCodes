package com.bluestone.app.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.bluestone.app.account.AccountLoginService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.account.security.CustomerSpecificCheckService;
import com.bluestone.app.account.security.RolesCache;
import com.bluestone.app.checkout.cart.CartUtil;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.core.util.Util;

@Controller
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AccountLoginService accountLoginService;

    @Autowired
    private CartService cartService;

    @Autowired
    private RolesCache rolesCache;

    @Autowired
    private CustomerSpecificCheckService customerSpecificCheckService;

    @RequestMapping(value = "/signin*", method = RequestMethod.POST)
    public
    @ResponseBody
    String signIn(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        log.info("LoginController.signIn(): using [{}]", email);
        String password = request.getParameter("password");
        String error = accountLoginService.login(email, password);
        boolean hasError = true;

        String redirectUrl = request.getHeader("referer");
        Map<String, Object> viewData = new HashMap<String, Object>();
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            hasError = false;
            boolean isACustomerOnly = customerSpecificCheckService.hasOnlyACustomerRole(subject);

            if (isACustomerOnly) {
                // set the cart
                Customer authenticatedCustomer = SessionUtils.getAuthenticatedCustomer();
                if (authenticatedCustomer != null) {
                    try {
                        cartService.setCustomerCartInSession(authenticatedCustomer, request.getSession());
                    } catch (Exception e) {
                        log.error("Error while setting customer=[{}] cart in session ", authenticatedCustomer, e);
                    }
                }

                SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
                if (savedRequest != null) {
                    redirectUrl = savedRequest.getRequestUrl();
                }
            } else {
                //check if implied role is not empty
                User user = (User) subject.getPrincipal();
                Set<Role> allImpliedRoles = rolesCache.getAllImpliedRoles(user);
                if (!allImpliedRoles.isEmpty()) {
                    redirectUrl = Util.getSiteUrlWithContextPath() + "/admin";
                }
            }

            if (redirectUrl == null) {
                redirectUrl = Util.getSiteUrlWithContextPath();
            }
        }
        /*SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
        if(savedRequest != null) {
            redirectUrl = savedRequest.getRequestUrl();
        }*/

        ArrayList<String> errorList = new ArrayList<String>();
        errorList.add(error);
        viewData.put(Constants.HAS_ERROR, hasError);
        viewData.put(Constants.REDIRECT_URL, redirectUrl);
        viewData.put(Constants.ERRORS, errorList);
        Gson gson = new Gson();
        return gson.toJson(viewData);
    }


    @RequestMapping(value = "/showLoginPageBody", method = RequestMethod.GET)
    public ModelAndView showLoginPageBody() {
        log.debug("LoginController.showLoginPageBody()");
        return new ModelAndView("authenticationPageWithoutHeaderAndFooter");
    }

    @RequestMapping(value = "/showLoginPage*", method = RequestMethod.GET)
    public ModelAndView showLoginPage() {
        log.debug("LoginController.showLoginPage()");
        Map<Object, Object> viewData = new HashMap<Object, Object>();
        viewData.put("enableLoginFancyBox", 0);
        return new ModelAndView("authenticationPageWithHeaderAndFooter", "data", viewData);
    }

    @RequestMapping(value = "/signout*", method = RequestMethod.GET)
    public ModelAndView signout(HttpServletRequest request, HttpServletResponse response) {
        log.debug("LoginController.signout()");
        accountLoginService.logout();
        CartUtil.removeCartFromSession(request.getSession());
        String referer = request.getHeader("referer");
        String redirectUrl = referer;
        if (redirectUrl == null || redirectUrl.contains("/account/") || redirectUrl.contains("/admin")) {
            redirectUrl = Util.getSiteUrlWithContextPath();
        }
        RedirectView redirectView = new RedirectView(redirectUrl.toString());
        return new ModelAndView(redirectView);
    }

}
