package com.bluestone.app.account;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.UserSocialData;
import com.bluestone.app.account.model.UserSocialData.SocialType;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.DateTimeUtil;

/**
 * @author Rahul Agrawal
 *         Date: 4/14/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class NewAccountRegistrationServiceV2 {

    private static final Logger log = LoggerFactory.getLogger(NewAccountRegistrationServiceV2.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountManagementEmailService accountManagementEmailService;
    
    @Autowired
    private PasswordManagementService passwordManagementService;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;


    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createAccount(Customer newCustomer) throws Exception {
        log.info("NewAccountRegistrationServiceV2.createAccount(): {} {}", newCustomer.getEmail(), newCustomer.getCustomerPhone());

        Customer existingCustomer = customerService.getCustomerByEmail(newCustomer.getEmail());
        if (existingCustomer == null) {
            customerService.createNewCustomer(newCustomer);
            initiateAccountActivationProcess(newCustomer);
        }

        if (existingCustomer != null) {
            if (existingCustomer.isAccountActivated()) {
                // already a customer
                throw AccountCreationException.accountAlreadyExists();
            } else {
                throw AccountCreationException.accountNotActivated();
            }
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Customer createAccountForFacebookUser(String emailFromFacebookResponse, String userName, String facebookId, String gender, String birthday) throws Exception {
    	log.info("NewAccountRegistrationServiceV2.createAccountForFacebookUser(): Email=[{}] Facebook ID=[{}]", emailFromFacebookResponse, facebookId);
    	
    	Customer existingCustomer = customerService.getCustomerByFacebookId(facebookId);
    	
    	//Move forward only if no customer exists - not updating user details as of now
		if (existingCustomer == null) {
			existingCustomer = customerService.getCustomerByEmail(emailFromFacebookResponse);
			if (existingCustomer == null) {
				// New Customer - First time login using facebook
				existingCustomer = new Customer();
				existingCustomer.setEmail(emailFromFacebookResponse);
				existingCustomer.setUserName(userName);				
			}
			List<UserSocialData> userSocialDatas = new ArrayList<UserSocialData>();
			userSocialDatas.add(new UserSocialData(SocialType.FACEBOOK,facebookId, existingCustomer));
			existingCustomer.setUsersocialData(userSocialDatas);
			existingCustomer.activateAccount();
			existingCustomer.setGender(gender);
			existingCustomer.setBirthDay(DateTimeUtil.parseDateString(birthday));
			
			if(existingCustomer.getId()==0){
				String plainPassword = passwordManagementService.generateRandomPassword(8);
				String hashedPassword = PasswordProcessors.getMD5HashedPassword(plainPassword);
				existingCustomer.setPassword(hashedPassword);
	    		customerService.createNewCustomer(existingCustomer);
	            sendWelcomeLetterForFBUser(existingCustomer, plainPassword);
	            log.info("NewAccountRegistrationServiceV2.createAccountForFacebookUser(): new account created for FacebookId=[{}] with Email=[{}]",facebookId, emailFromFacebookResponse);
	    	}else{
	    		existingCustomer = customerService.updateCustomer(existingCustomer);
	    		log.info("NewAccountRegistrationServiceV2.createAccountForFacebookUser(): associated the FacebookId=[{}] with Email=[{}]",facebookId, emailFromFacebookResponse);
	    	}
		}
		
		return existingCustomer;        
    }

    public void initiateAccountActivationProcess(Customer newCustomer) throws AccountCreationException {
        log.info("NewAccountRegistrationServiceV2.initiateAccountActivationProcess() for {}", newCustomer.getEmail());
        try {
            accountManagementEmailService.sendWelcomeEmail(newCustomer);
        } catch (Exception e) {
            productionAlert.send("New Account Registration: Failed to Enqueue the activation email for " + newCustomer.getEmail(), e);
            log.error("Error: NewAccountRegistrationService.initiateAccountActivationProcess(): for [{}] ", newCustomer.getEmail(), e);
            throw AccountCreationException.failedToSendAccountActivationEmail();
        }
    }
    
    public void sendWelcomeLetterForFBUser(Customer newCustomer, String plainPassword) throws AccountCreationException {
        log.info("NewAccountRegistrationServiceV2.sendWelcomeLetterForFBUser() for {}", newCustomer.getEmail());
        try {
            accountManagementEmailService.sendWelcomeEmailForFacebookUser(newCustomer, plainPassword);
        } catch (Exception e) {
            productionAlert.send("New Account Registration: Failed to Enqueue the Facebook User Welcome email for " + newCustomer.getEmail(), e);
            log.error("Error: NewAccountRegistrationService.sendWelcomeLetterForFBUser(): for [{}] ", newCustomer.getEmail(), e);
            throw AccountCreationException.failedToSendAccountActivationEmail();
        }
    }
}
