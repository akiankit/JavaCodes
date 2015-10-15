package com.bluestone.app.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.account.AccountCreationException;
import com.bluestone.app.account.NewAccountRegistrationServiceV2;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.util.Constants;

/**
 * @author Rahul Agrawal
 *         Date: 4/14/13
 */
@Controller
public class NewAccountRegistrationController {

    private static final Logger log = LoggerFactory.getLogger(NewAccountRegistrationController.class);

    private static final String SIGN_UP_SUCCESSFUL_MESSAGE = "An account activation e-mail has been sent to your email: ";

    @Autowired
    private NewAccountRegistrationServiceV2 newAccountRegistrationService;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public
    @ResponseBody
    String registerUser(@ModelAttribute("customer") @Valid Customer newCustomer, BindingResult validationResult) {
        log.info("NewAccountRegistrationController.registerUser(): {} {}", newCustomer.getEmail(), newCustomer.getCustomerPhone());
        Map<String, Object> response = new HashMap<String, Object>();
        ArrayList<String> errorList = new ArrayList<String>();
        if (validationResult.hasErrors()) { // check for customer data validation here
            for (ObjectError error : validationResult.getAllErrors()) {
                errorList.add(error.getDefaultMessage());
            }
        } else {
            try {
                newAccountRegistrationService.createAccount(newCustomer);
                response.put(Constants.CUSTOMER_ID, newCustomer.getId());
                response.put(Constants.MESSAGE, (SIGN_UP_SUCCESSFUL_MESSAGE + newCustomer.getEmail()));
            } catch (AccountCreationException accountCreationException) {
                String errorMessage = accountCreationException.getMessage();
                errorList.add(errorMessage);
                if (AccountCreationException.ACCOUNT_NOT_ACTIVATED_MESSAGE.equals(errorMessage)) {
                    log.error("ERROR While creating new account for [{}] Reason=[{}] System will resend the activation email.", newCustomer.getEmail(), errorMessage);
                    // resend the activation email.
                    try {
                        newAccountRegistrationService.initiateAccountActivationProcess(newCustomer);
                    } catch (AccountCreationException e) {
                        errorList.add(e.getMessage());
                    }
                } else {
                    log.error("ERROR While creating new account for [{}] : Reason=[{}]", newCustomer.getEmail(), accountCreationException.getMessage());
                }
            } catch (Exception exception) {
                log.error("Unexpected ERROR While creating account for {} : Reason=[{}]", newCustomer.getEmail(), exception.getLocalizedMessage(), exception);
                errorList.add(AccountCreationException.ACCOUNT_CREATION_FAILED_MESSAGE);
            }
        }
        if (errorList.size() > 0) {
            response.put(Constants.HAS_ERROR, true);
            response.put(Constants.ERRORS, errorList);
        } else {
            response.put(Constants.HAS_ERROR, false);

        }
        response.remove("customer");// since customer model was getting added to
        // response for NO REASON!!!!!
        return new Gson().toJson(response);
    }
}
