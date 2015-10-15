package com.bluestone.app.account.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.account.AccountActivationService;
import com.bluestone.app.account.CipherUtil;
import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.PasswordManagementService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.Util;

/**
 * @author Rahul Agrawal
 *         Date: 4/13/13
 */
@Controller
public class AccountActivationController {

    private static final Logger log = LoggerFactory.getLogger(AccountActivationController.class);

    private static final String INVALID_PASSWORD_MESSAGE = "Invalid Password";
    private static final String ACTIVATION_ERROR_MESSAGE = "Some unexpected error happened during the account activation.";

    private static final String PRESTASHOP_ACCOUNT_ACTIVATION_SEPARATOR = "|-|";
    private static final String PRESTASHOP_COMPATIBLE = "backward";

    @Autowired
    private AccountActivationService accountActivationService;

    @Autowired
    private PasswordManagementService passwordManagementService;

    @Autowired
    private CustomerService customerService;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;


    @RequestMapping(value = "/activation*", method = RequestMethod.GET)
    public ModelAndView showActivateAccount(@RequestParam(value = "activation-id") String activationId, HttpServletResponse response) {
        log.info("AccountActivationController.showActivateAccount(): activationId=[{}]", activationId);
        Map<Object, Object> viewData = new HashMap<Object, Object>();
        final String siteLink = LinkUtils.getSiteLink("");
        String email = "Not yet initialized";
        try {
            email = CipherUtil.decrypt(activationId, Constants.key);
            Customer customer = customerService.getCustomerByEmail(email);
            if (customer != null && customer.isAccountActivated()) {
                log.info("Redirecting to home page as account is already activated for user email=[{}]", email);
                response.sendRedirect(siteLink);
                return null;
            } else {
                log.info("AccountActivationController.showActivateAccount(): Proceeding to activate the account for email=[{}] ActivationId=[{}}",
                         email, activationId);
            }
            viewData.put("emailId", email);
            viewData.put("activationId", activationId);
            viewData.put("mode", "current");
        } catch (EncryptionException e) {
            log.error("ERROR: Decrypting the activation id=[{}] from IP=[{}]", activationId, Util.getMessageContext().getClientIP(), e);
        } catch (IOException e) {
            log.error("Error: AccountActivationController.showActivateAccount(): Failed to redirect to [{}] for activationId=[{}] emailId=[{}]",
                      siteLink, activationId, email, e);
        } catch (Exception e) {
            log.error("Error: AccountActivationController.showActivateAccount():Some error happened during account activation for activationId=[{}} email=[{}] ",
                      activationId, email, e);
            raiseAlert("AccountActivationController.activateAccount():Some error happened during activation of the account " + email +
                       " ActivationId= " + activationId, e);
        }
        return new ModelAndView("account-activation", "viewData", viewData);
    }

    /*
      * Backward Compatibility
      */
    @RequestMapping(value = "/account-activation.php*", method = RequestMethod.GET)
    public ModelAndView showActivateAccountPrestashop(@RequestParam(value = "id_activation") String activationId) {
        log.info("AccountActivationController.showActivateAccountPrestashop()");
        Map<Object, Object> viewData = new HashMap<Object, Object>();
        try {
            String email = "";
            String decText = CipherUtil.decryptPrestashopActivationId(activationId);
            String decArr[] = StringUtils.split(decText, PRESTASHOP_ACCOUNT_ACTIVATION_SEPARATOR);
            if (decArr.length == 2) {
                email = decArr[1];
                email = email.trim();
            }
            viewData.put("emailId", email);
            viewData.put("activationId", activationId);
            viewData.put("mode", PRESTASHOP_COMPATIBLE);
        } catch (Exception e) {
            log.error("ERROR: Decrypting the PrestaShop php world activation id=[{}] from IP=[{}]",
                      activationId, Util.getMessageContext().getClientIP(), e);
        }
        return new ModelAndView("account-activation", "viewData", viewData);
    }

    @RequestMapping(value = "/activate-account*", method = RequestMethod.POST)
    public ModelAndView activateAccount(HttpServletRequest request, HttpServletResponse response) {
        String activationId = request.getParameter("activationId");
        log.info("AccountActivationController.activateAccount(): for activationId=[{}]", activationId);
        String password = request.getParameter("passwd");
        String confirmPassword = request.getParameter("passwdretype");
        Map<Object, Object> viewData = new HashMap<Object, Object>();

        String mode = request.getParameter("mode");
        viewData.put("activationId", activationId);
        String email = "Not yet initialised";
        try {
            if (PRESTASHOP_COMPATIBLE.equals(mode)) {
                log.info("AccountActivationController.activateAccount(): Registration for [{} is from Prestashop world]", email);
                String decText = CipherUtil.decryptPrestashopActivationId(activationId);
                String decArr[] = StringUtils.split(decText, PRESTASHOP_ACCOUNT_ACTIVATION_SEPARATOR);
                if (decArr.length == 2) {
                    email = decArr[1];
                    email = email.trim();
                }
            } else {
                email = CipherUtil.decrypt(activationId, Constants.key);
            }
            log.info("AccountActivationController.activateAccount() for emailId={}", email);
            viewData.put("emailId", email);

            if (!passwordManagementService.validatePassword(password, confirmPassword)) {
                viewData.put("error", INVALID_PASSWORD_MESSAGE);
                return new ModelAndView("account-activation", "viewData", viewData);
            }
            if (!accountActivationService.activateAccount(password, email)) {
                viewData.put("error", ACTIVATION_ERROR_MESSAGE);
                return new ModelAndView("account-activation", "viewData", viewData);
            }
            response.sendRedirect(Util.getSiteUrlWithContextPath());
        } catch (EncryptionException e) {
            log.error("AccountActivationController.activateAccount(): Failed during Decryption of activationId=[{}] emailId=[{}] from IP=[{}]",
                      activationId, email, Util.getMessageContext().getClientIP(), e);
        } catch (IOException e) {
            log.error("Error: AccountActivationController.activateAccount(): Failed during redirection to [{}}",
                      Util.getSiteUrlWithContextPath(), e);
        } catch (Exception e) {
            log.error("Error: AccountActivationController.activateAccount():Some error happened during activation of the account for email=[{}] activationId=[{}] IP address=[{]} ",
                      email, activationId, Util.getMessageContext().getClientIP(), e);
            raiseAlert("AccountActivationController.activateAccount():Some error happened during activation of the account " + email +
                       " ActivationId= " + activationId, e);
        }

        return new ModelAndView("home");
    }

    private void raiseAlert(String message, Exception e) {
        productionAlert.send(message, e);
    }
}
