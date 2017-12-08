package com.bluestone.app.admin.controller;

import com.google.common.base.Throwables;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.admin.service.AdminOrderMailService;
import com.bluestone.app.integration.uniware.QService;

@Controller
@RequestMapping("/admin/replay*")
public class AdminOrderReplayController {

    private static final Logger log = LoggerFactory.getLogger(AdminOrderReplayController.class);

    @Autowired
    private AdminOrderMailService adminOrderMailService;

    @Autowired
    private QService qService;

    @RequestMapping(value = "/email/{orderId}", method = RequestMethod.GET)
    @RequiresPermissions("order:edit")
    public
    @ResponseBody
    String sendOrderEmailToCustomer(@PathVariable long orderId) {
        log.info("AdminOrderReplayController.sendOrderEmailToCustomer() for OrderId=[{}]", orderId);
        try {
            adminOrderMailService.reSendOrderMail(orderId);
            return ("*** Success **** ReSend the email for order id =" + orderId);
        } catch (Exception e) {
            log.error("Error: AdminOrderReplayController.sendOrderEmail(): for OrderId=[{}]", e);
            return Throwables.getStackTraceAsString(e);
        }
    }

    @RequestMapping(value = "/uniware/{orderId}", method = RequestMethod.GET)
    @RequiresPermissions("order:edit")
    public
    @ResponseBody
    String sendOrderToUniware(@PathVariable long orderId) {
        log.info("AdminOrderReplayController.sendOrderToUniware()- OrderId=[{}]", orderId);
        try {
            qService.send(orderId);
            return ("*** Success **** ReSend to Uniware. OrderId=" + orderId);
        } catch (Exception e) {
            log.error("Error: AdminOrderReplayController.sendOrderToUniware(): for OrderId=[{}]", e);
            return Throwables.getStackTraceAsString(e);
        }
    }
}

