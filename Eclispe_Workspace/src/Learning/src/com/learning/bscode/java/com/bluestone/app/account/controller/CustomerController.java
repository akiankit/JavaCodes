package com.bluestone.app.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.PasswordManagementService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.User;
import com.bluestone.app.core.HttpRequestParser;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanService;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderHistory;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.model.State;
import com.bluestone.app.shipping.service.ShippingService;

@Controller
@RequestMapping("/account*")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordManagementService passwordManagementService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private GoldMinePlanService goldMinePlanService;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;

    @RequestMapping(value = "/checksession", method = RequestMethod.POST)
    public
    @ResponseBody
    String checkSession(HttpServletRequest request, ModelMap modelMap) {
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        log.debug("CustomerController.checkSession()");
        Customer loggedCustomer = (Customer) SecurityUtils.getSubject().getPrincipal();
        if (loggedCustomer != null) {
            responseMap.put("session", true);
        } else {
            responseMap.put("redirectUrl", LinkUtils.getSiteLink("account"));
        }
        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }

    @RequestMapping(value = "/identity*", method = RequestMethod.GET)
    public ModelAndView showMyAccountPage() {
        log.debug("CustomerController.showMyAccountPage()");
        Map<String, Object> viewData = new HashMap<String, Object>();
        viewData.put("selectedTab", "identity");
        viewData.put("pageType", "myAccount");
        viewData.put("breadCrumb", customerService.getBreadCrumbForMyAccount("Your Personal Information"));
        return new ModelAndView("identity", "viewData", viewData);
    }

    @RequestMapping(value = "/orderhistory*", method = RequestMethod.GET)
    public ModelAndView showOrderHistoryPage() {
        log.debug("CustomerController.showOrderHistoryPage()");
        Customer loggedInCustomer = (Customer) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> viewData = new HashMap<String, Object>();
        List<Order> orderList = orderService.getAllOrdersForCustomer(loggedInCustomer);
        viewData.put("orderList", orderList);
        viewData.put("selectedTab", "history");
        viewData.put("pageType", "myAccount");
        viewData.put("breadCrumb", customerService.getBreadCrumbForMyAccount("Order History"));
        return new ModelAndView("orderhistory", "viewData", viewData);
    }

    @RequestMapping(value = "/orderdetail", method = RequestMethod.GET)
    public ModelAndView getOrderDetails(@RequestParam(value = "code_order") String orderCode, HttpServletRequest request, ModelMap response) {
        log.info("CustomerController.getOrderDetails() for OrderCode={}", orderCode);
        Map<String, Object> viewData = new HashMap<String, Object>();
        User user = SessionUtils.getAuthenticatedUser();
        String orderId = "";
        Order order = null;

        // in any case, don't let the malicious caller understand the exact reason for failure.
        try {
            order = orderService.getOrderFromOrderCode(orderCode);
            if (order.getCustomer().getId() != user.getId()) {
                productionAlert.send("User=" + user.getEmail() + " attempted to view the Order Id =" + order.getId() + " OrderCode=" + orderCode);
                return new ModelAndView("authorizationExceptionPage");
            }
        } catch (Exception exception) {
            log.error("Error: CustomerController.getOrderDetails():Malicious activity by [{}] to access OrderCode=[{}]. \nDetails={} ",
                      user.getEmail(), orderCode, HttpRequestParser.dumpHeadersForLogging(request), exception);
            productionAlert.send("User=" + user.getEmail() + " attempted to guess/cook an Order Code=" + orderCode);
            return new ModelAndView("authorizationExceptionPage");
        }

        orderId = Long.toString(order.getId());
        try {
            orderService.syncWithUniwareStatus(orderId);
            orderService.reComputeOrderStatus(order.getId(), null);
        } catch (Exception exception) {
            log.error("CustomerController.getOrderDetails(): Error in updating the latest status from Uniware for OrderId={} . Showing whatever is in database ", orderId, exception);
            return new ModelAndView("orderDetail", "viewData", viewData);
        }
        order = orderService.getOrder(order.getId());
        viewData.put("order", order);
        viewData.put("discountVoucher", order.getDiscountVoucher());
        viewData.put("totalDiscount", order.getCart().getDiscountPrice());
        // viewData.put("orderStatesHistory",
        // orderService.getOrderStatesHistory(order.getId()));
        List<OrderHistory> fullHistory = orderService.getOrderStatesHistory(order.getId());
        viewData.put("orderStatesHistory", fullHistory.isEmpty() ? fullHistory : fullHistory.subList(0, 1));
        viewData.put("pageType", "myAccount");
        viewData.put("breadCrumb", customerService.getBreadCrumbForMyAccount("Order History"));
        return new ModelAndView("orderDetail", "viewData", viewData);
    }

    @RequestMapping(value = "/address*", method = RequestMethod.GET)
    public ModelAndView showAddressesPage() {
        log.debug("CustomerController.showAddressesPage()");
        Customer loggedInCustomer = (Customer) SecurityUtils.getSubject().getPrincipal();
        Map<Object, Object> viewData = new HashMap<Object, Object>();
        loggedInCustomer = customerService.getCustomerByEmail(loggedInCustomer.getEmail());
        List<State> stateList = shippingService.getStateList();
        List<Address> addressList = new ArrayList<Address>();
        Address latestAddress = shippingService.getLatestShippingAddress(loggedInCustomer);
        if (latestAddress != null) {
            addressList.add(latestAddress);
        }
        viewData.put("stateList", stateList);
        viewData.put("addressList", addressList);
        viewData.put("selectedTab", "addresses");
        viewData.put("pageType", "myAccount");
        viewData.put("breadCrumb", customerService.getBreadCrumbForMyAccount("Addresses"));
        return new ModelAndView("addresses", "viewData", viewData);
    }

    @RequestMapping(value = "/identity*", method = RequestMethod.POST)
    public
    @ResponseBody
    String updateCustomerAttribute(@RequestParam(value = "attributeName", defaultValue = "") String attributeName,
                                   @RequestParam(value = "attributeValue", defaultValue = "") Object attributeValue,
                                   ModelMap modelMap, HttpServletRequest request) {
        log.debug("CustomerController.updateCustomerAttribute()");
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        boolean hasError = false;
        ArrayList<String> errors = new ArrayList<String>();
        Customer customerFromPrincipal = ((Customer) SecurityUtils.getSubject().getPrincipal());

        if (customerFromPrincipal != null) {
            String email = customerFromPrincipal.getEmail();
            Customer customerFromEmail = customerService.getCustomerByEmail(email);
            if (!attributeName.equals("password")) {
                hasError = !customerService.updateCustomer(attributeName, attributeValue, customerFromEmail);
                if (!hasError) {
                    if (attributeName.equalsIgnoreCase("userName")) {
                        responseMap.put("success", "Your name has been successfully updated.");
                    } else if (attributeName.equalsIgnoreCase("customerPhone")) {
                        responseMap.put("success", "Your contact number has been successfully updated.");
                    } else if (attributeName.equalsIgnoreCase("newsletter")) {
                        if (Boolean.valueOf((String) attributeValue) == true) {
                            responseMap.put("success", "You have successfully subscribed to offers.");
                        } else {
                            responseMap.put("success", "You have successfully unsubscribed from offers.");
                        }
                    }
                }

            } else {
                String oldPassword = request.getParameter("oldPassword");
                String confirmPassword = request.getParameter("confirmPassword");
                String hashedPassword = new Md5Hash(Constants.PASSWORD_SALT + oldPassword).toString();
                // validation if current password entered by user is correct or
                // not
                boolean isOldPasswordCorrect = hashedPassword.equals(customerFromEmail.getPassword());
                if (!isOldPasswordCorrect) {
                    hasError = true;
                    errors.add("Your password is incorrect.");
                }
                if (!hasError) {
                    if (passwordManagementService.validatePassword((String) attributeValue, confirmPassword)) {
                        passwordManagementService.updatePassword((String) attributeValue, email);
                    } else {
                        hasError = true;
                        errors.add("Invalid Password");
                    }
                }
                if (!hasError) {
                    responseMap.put("success", "Your password has been successfully updated.");
                }
            }
            responseMap.put("selectedTab", "identity");
        } else {
            hasError = true;
            errors.add("You need to be logged in to be able to change your personal details.");
        }
        responseMap.put(Constants.HAS_ERROR, hasError);
        responseMap.put(Constants.ERRORS, errors);
        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }

    @RequestMapping(value = "/goldminehistory*", method = RequestMethod.GET)
    public ModelAndView showGoldMineHistoryPage() {
        log.debug("CustomerController.showGoldMineHistoryPage()");
        Customer loggedInCustomer = (Customer) SecurityUtils.getSubject().getPrincipal();
        Map<String, Object> viewData = new HashMap<String, Object>();
        List<GoldMinePlan> goldMineList = goldMinePlanService.getAllGoldMinesForCustomer(loggedInCustomer);
        viewData.put("goldMineList", goldMineList);
        viewData.put("selectedTab", "gold mine");
        viewData.put("pageType", "myAccount");
        viewData.put("breadCrumb", customerService.getBreadCrumbForMyAccount("Gold Mine History"));
        return new ModelAndView("goldminehistory", "viewData", viewData);
    }

}
