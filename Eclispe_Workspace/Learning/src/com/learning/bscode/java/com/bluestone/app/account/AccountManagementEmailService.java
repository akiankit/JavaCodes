package com.bluestone.app.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;

/**
 * @author Rahul Agrawal
 *         Date: 4/12/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class AccountManagementEmailService {

    private static final Logger log = LoggerFactory.getLogger(AccountManagementEmailService.class);

    private static final String MAIL_REGISTRATION_SUBJECT = "[BlueStone] Welcome to the BlueStone!";
    private static final String RESET_PASSWORD_SUBJECT = "[BlueStone] Password Reset Request for your BlueStone Account.";
    private static final String NEW_PASSWORD_SUBJECT = "[BlueStone] BlueStone Account Password has been reset.";

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerService customerService;

    public void sendWelcomeEmail(Customer newCustomer) throws EncryptionException {
        log.info("AccountManagementEmailService.sendWelcomeEmail(): for Customer={}", newCustomer.getEmail());
        final TemplateVariables templateVariables = emailService.getTemplateVariables("welcome-email.vm");        
        if(!newCustomer.isAccountActivated()){
        	String email = newCustomer.getEmail();
        	templateVariables.put("confirm_link", PasswordProcessors.getActivationPageURL(email, Constants.key));
        }        
        setEmailParams(templateVariables, newCustomer, MAIL_REGISTRATION_SUBJECT);
        emailService.send(templateVariables);
        log.info("AccountEmailService.sendActivationMail(): done for Customer=[{}]", newCustomer.getEmail());
    }
    
    public void sendWelcomeEmailForFacebookUser(Customer newCustomer,String plainPassword) {
        log.info("AccountManagementEmailService.sendWelcomeEmailForFacebookUser(): for Customer={}", newCustomer.getEmail());
        final TemplateVariables templateVariables = emailService.getTemplateVariables("welcome-email.vm");
        templateVariables.put("email", newCustomer.getEmail());
        templateVariables.put("password", plainPassword);
        setEmailParams(templateVariables, newCustomer, MAIL_REGISTRATION_SUBJECT);
        emailService.send(templateVariables);
        log.info("AccountEmailService.sendWelcomeEmailForFacebookUser(): Welcome email sent for Customer=[{}]", newCustomer.getEmail());
    }

    public void sendResetPasswordMail(String email) throws EncryptionException {
        log.info("AccountManagementEmailService.sendResetPasswordMail(): for email={}", email);
        final TemplateVariables templateVariables = emailService.getTemplateVariables("forgetpasswordmail.vm");
        Customer customer = customerService.getCustomerByEmail(email);
        templateVariables.put("confirm_link", PasswordProcessors.getResetPasswordLink(email, Constants.key));
        setEmailParams(templateVariables, customer, RESET_PASSWORD_SUBJECT);
        emailService.send(templateVariables);
    }

    public void sendNewPasswordMail(String email, String password) throws Exception {
        log.info("AccountManagementEmailService.sendNewPasswordMail(): for email={}", email);
        final TemplateVariables templateVariables = emailService.getTemplateVariables("newpasswordmail.vm");
        Customer customer = customerService.getCustomerByEmail(email);
        setEmailParams(templateVariables, customer, NEW_PASSWORD_SUBJECT);
        templateVariables.put("password", password);
        templateVariables.put("site_url", Util.getSiteUrlWithContextPath());
        emailService.send(templateVariables);
    }


    private void setEmailParams(TemplateVariables emailVariables, Customer customer, String subject) {
        log.debug("AccountEmailService.setEmailParams()");
        emailVariables.setCustomer(customer);
        emailVariables.setToField(customer.getEmail());
        emailVariables.setSubject(subject);
    }

}
