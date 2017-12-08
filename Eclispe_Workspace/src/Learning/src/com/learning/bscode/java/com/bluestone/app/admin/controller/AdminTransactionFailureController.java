package com.bluestone.app.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.admin.service.payment.FailedTransactions;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.model.PaymentTransaction;

@Controller
@RequestMapping(value = "/admin/failuretransaction/")
public class AdminTransactionFailureController {
    private static final Logger log = LoggerFactory.getLogger(AdminTransactionFailureController.class);

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private FailedTransactions failedTransactions;

    @RequestMapping(value = "history", method = RequestMethod.GET)
    @RequiresPermissions("transaction:view")
    public ModelAndView getFailedTransactionHistory(
            @ModelAttribute("ListFilterCriteria") ListFilterCriteria filterCriteria,
            @RequestParam(defaultValue = "", required = false, value = "fromdate") String from,
            @RequestParam(defaultValue = "", required = false, value = "todate") String to,
            HttpServletRequest request) {
        log.debug("AdminTransactionFailureController.getFailedTransactionHistory() filterCriteria {}, from {}, to {}, request {}", filterCriteria, from, to,
                  request);
        Map<Object, Object> transactionDetails = new HashMap<Object, Object>();
        try {
            Date toDate = DateTimeUtil.getDateFromString(to, false);
            Date fromDate = DateTimeUtil.getDateFromStringForTransactionList(from);
            transactionDetails = failedTransactions.getDetails(filterCriteria, fromDate, toDate);
            transactionDetails.put("requestURL", request.getRequestURL());
        } catch (Exception e) {
            log.error("Error during AdminTransactionFailureController.getFailedTransactionHistory(): Reason: {}", e.toString(), e);
        }
        return new ModelAndView("failedTransactionList", "viewData", transactionDetails);
    }

    @RequestMapping(value = "detail/{txnId}", method = RequestMethod.GET)
    @RequiresPermissions("transaction:view")
    public ModelAndView getTransactionDetails(@PathVariable("txnId") String txnId, HttpServletRequest request) {
        log.debug("AdminTransactionFailureController.getTransactionDetails() txnId = {}, request = {}", txnId, request);
        Map<Object, Object> transactionDetails = new HashMap<Object, Object>();
        String queryString = request.getQueryString();
        String requestURL = queryString != null ? queryString.substring(queryString.indexOf('=') + 1) : "";
        PaymentTransaction paymentTransactionFromEncryptedTxnId = null;
        try {
            paymentTransactionFromEncryptedTxnId = paymentTransactionService.getPaymentTransactionFromEncryptedTxnId(txnId);
        } catch (Exception e) {
            log.error("Error during AdminTransactionFailureController.getTransactionDetails(): Reason: {}", e.toString(), e);
        }
        transactionDetails.put("transaction", paymentTransactionFromEncryptedTxnId);
        transactionDetails.put("transactionListURL", requestURL);
        return new ModelAndView("transactionDetails", "viewData", transactionDetails);
    }
}
