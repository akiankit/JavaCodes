package com.bluestone.app.admin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.goldMineOrders.event.GoldMineCustomerNotification;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanPaymentService;
import com.google.common.base.Throwables;

@Controller
@RequestMapping("/admin/goldmine*")
public class AdminGoldMineOrderReplayController {

    private static final Logger log = LoggerFactory.getLogger(AdminGoldMineOrderReplayController.class);

    @Autowired
    private GoldMineCustomerNotification goldMineCustomerNotification;
    
    @Autowired
    private GoldMinePlanPaymentService goldMinePlanPaymentService;
    
    @RequiresPermissions("GoldMinePlan:edit")
    @RequestMapping(value = "/plan/email/{goldMinePlanId}", method = RequestMethod.GET)
    public @ResponseBody String sendGoldMineConfirmationEmailToCustomer(@PathVariable long goldMinePlanId) {
        log.info("AdminGoldMineOrderReplayController.sendGoldMineConfirmationEmailToCustomer() goldMinePlanId",goldMinePlanId);
        try {
        	goldMineCustomerNotification.resendGoldMineMail(goldMinePlanId);
            return ("*** Success **** ReSend the email for gold mine plan id =" + goldMinePlanId);
        } catch (Exception e) {
        	log.error(
					"Error during AdminGoldMineOrderReplayController.sendGoldMineConfirmationEmailToCustome for planId:[{}] Reason:{}",
					goldMinePlanId,e.toString(), e);
            log.error("Error: AdminOrderReplayController.sendOrderEmail(): for OrderId=[{}]", e);
            return Throwables.getStackTraceAsString(e);
        }
    }
    
    @RequiresPermissions("GoldMinePlan:edit")
    @RequestMapping(value = "/payment/email/{goldMinePlanPaymentId}", method = RequestMethod.GET)
    public @ResponseBody String sendGoldMinePaymentConfirmationEmailToCustomer(@PathVariable long goldMinePlanPaymentId) {
    	log.info("AdminGoldMineOrderReplayController.sendGoldMinePaymentConfirmationEmailToCustomer() goldMinePlanPaymentId",goldMinePlanPaymentId);
    	try {
    		GoldMinePlanPayment goldMinePlanPayment = goldMinePlanPaymentService.getGoldMinePlanPaymentById(goldMinePlanPaymentId);
        	goldMineCustomerNotification.installmentPaymentReceived(goldMinePlanPayment);
            return ("*** Success **** ReSend the email for gold mine plan payment id =" + goldMinePlanPaymentId);
        } catch (Exception e) {
			log.error(
					"Error during AdminGoldMineOrderReplayController.sendGoldMinePaymentConfirmationEmailToCustomer: for paymentId :[{}] Reason:{}",
					goldMinePlanPaymentId,e.toString(), e);
            return Throwables.getStackTraceAsString(e);
        }
    }

}

