package com.bluestone.app.account.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.AccountCreationException;
import com.bluestone.app.account.AccountManagementEmailService;
import com.bluestone.app.account.CipherUtil;
import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.NewAccountRegistrationServiceV2;
import com.bluestone.app.account.PasswordManagementService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.Util;

/**
 * @author Rahul Agrawal
 *         Date: 4/13/13
 */
@Controller
public class ForgotPasswordController {

    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordController.class);

    private static final String PASSWORD_SENT_MESSAGE = "A confirmation e-mail has been sent to your email address:";

    private static final int LENGTH_FOR_RANDOM_PASSWORD = 6;

    @Autowired
    private PasswordManagementService passwordManagementService;

    @Autowired
    private AccountManagementEmailService accountManagementEmailService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NewAccountRegistrationServiceV2 newAccountRegistrationService;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;


    @RequestMapping(value = "/forgotpassword*", method = RequestMethod.POST)
    public
    @ResponseBody
    String forgotPassword(@RequestParam(value = "email", defaultValue = "") String email) {
        log.info("ForgotPasswordController.forgotPassword() for email=[{}] from IPAddress=[{}]", email, Util.getMessageContext().getClientIP());
        String trimmedEmail = email.trim();
        HashMap<String, Object> responseMap = new HashMap<String, Object>();
        List<String> listOfErrors = new LinkedList<String>();

        try {
            listOfErrors = passwordManagementService.isPasswordGenerationAllowed(trimmedEmail);
        } catch (AccountCreationException e) {
            log.error("Error: ForgotPasswordController.forgotPassword(): {}", e.getMessage());
            try {
                newAccountRegistrationService.initiateAccountActivationProcess(customerService.getCustomerByEmail(email));
                listOfErrors.add(e.getMessage());
            } catch (AccountCreationException e1) {
                listOfErrors.add(e.getMessage());
            }
        }

        if (listOfErrors.isEmpty()) {
            try {
                accountManagementEmailService.sendResetPasswordMail(trimmedEmail);
                responseMap.put("message", PASSWORD_SENT_MESSAGE + " " + trimmedEmail);
            } catch (Exception e) {
                log.error("Exception occurred while sending the email containing the link to reset the password for user=[{}]: Reason={}", trimmedEmail,
                          e.getLocalizedMessage(), Throwables.getRootCause(e));
                listOfErrors.add("Sorry, there was some issue while resetting your password. Please try again.");
                productionAlert.send("Failed to send the email for password rest. User=" + trimmedEmail, e);

            }
        }
        responseMap.put(Constants.HAS_ERROR, (!listOfErrors.isEmpty()));
        if (!listOfErrors.isEmpty()) {
            responseMap.put(Constants.ERRORS, listOfErrors);
        }

        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }

    @RequestMapping(value = "/forgotpassword*", method = RequestMethod.GET)
    public ModelAndView resetPassword(@RequestParam(value = "token1", defaultValue = "") String emailToken,
                                      @RequestParam(value = "token2", defaultValue = "") String timeStampToken,
                                      HttpServletResponse response) {
        log.info("ForgotPasswordController.resetPassword() for emailToken=[{}] timestampToken=[{}] from IP=[{}}",
                 emailToken, timeStampToken, Util.getMessageContext().getClientIP());
        Map<Object, Object> viewData = new HashMap<Object, Object>();

        String email = null;
        try {
            email = CipherUtil.decrypt(emailToken, Constants.key);
            Customer customer = customerService.getCustomerByEmail(email);
            if (customer != null) {
                String timeStamp = CipherUtil.decrypt(timeStampToken, Constants.key);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date mailSentTime = dateFormat.parse(timeStamp);
                Timestamp lastPasswdGen = customer.getLastPasswdGen();
                if (lastPasswdGen.after(mailSentTime)) {
                    final String siteLink = LinkUtils.getSiteLink("");
                    log.info("Redirecting to [{}] because password has been already generated with the requested link for email=[{}] timestampToken=[{}]",
                             siteLink, email, DateTimeUtil.getSimpleDateAndTime(mailSentTime));
                    response.sendRedirect(siteLink);
                    return null;
                }
                String plainPassword = passwordManagementService.generateRandomPassword(LENGTH_FOR_RANDOM_PASSWORD);
                passwordManagementService.updatePassword(plainPassword, email);
                accountManagementEmailService.sendNewPasswordMail(email, plainPassword);
                viewData.put("confirmation", new Integer(1));
            }
        } catch (Exception e) {
            log.error("Failure occurred while updating the password and sending password reset mail. ErrorMessage={}",
                      e.getLocalizedMessage(), Throwables.getRootCause(e));
            productionAlert.send("Failed to honour the password reset request for " + email, e);

        }
        return new ModelAndView("forgotpassword", "viewData", viewData);
    }
}
