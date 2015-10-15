package com.bluestone.app.account;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.dao.CustomerDao;
import com.bluestone.app.account.model.Customer;

/**
 * @author Rahul Agrawal
 *         Date: 4/12/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
public class PasswordManagementService {

    private static final Logger log = LoggerFactory.getLogger(PasswordManagementService.class);

    private static final String RANDOM_PASSWORD_GENERATOR_STRING = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String PASSWORD_GEN_TIMEDIFF = "You can regenerate your password only every 360 minute(s).";
    private static final String INVALID_EMAIL = "Invalid Email";

    @Autowired
    private CustomerDao customerDao;

    public String generateRandomPassword(int len) {
        log.debug("PasswordManagementService.generateRandomPassword()");
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(RANDOM_PASSWORD_GENERATOR_STRING.charAt(rnd.nextInt(RANDOM_PASSWORD_GENERATOR_STRING.length())));
        }
        return sb.toString();
    }

    public boolean validatePassword(String password, String confirmPassword) {
        log.debug("PasswordManagementService.validatePassword()");
        if (password == null || confirmPassword == null || password.length() < 6 || !password.equals(confirmPassword)) {
            return false;
        }
        return true;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Customer updatePassword(String plainPassword, String email) {
        log.debug("PasswordManagementService.updatePassword(): for email={}", email);
        String hashedPassword = PasswordProcessors.getMD5HashedPassword(plainPassword);
        Customer customer = customerDao.getCustomerFromEmail(email);
        return customerDao.updatePassword(customer, hashedPassword);
        //return LocalReflectionUtils.setAttribute("password", hashedPassword, customer);
    }


    public List<String> isPasswordGenerationAllowed(String email) throws AccountCreationException {
        log.debug("PasswordManagementService.isPasswordGenerationAllowed(): Check for email=[{}]", email);
        List<String> errorList = new LinkedList<String>();
        if (StringUtils.isBlank(email)) {
            errorList.add(INVALID_EMAIL);
        } else {
            Customer existingCustomer = customerDao.getCustomerFromEmail(email);
            if (existingCustomer == null) {
                errorList.add(SharedConstants.EMAIL_ID_NOT_EXISTS);
            } else {
                if (existingCustomer.isAccountActivated()) {
                    if (isTooFrequent(existingCustomer)) {
                        errorList.add(PASSWORD_GEN_TIMEDIFF);
                    }
                }

                if (!existingCustomer.isAccountActivated()) {
                    throw AccountCreationException.accountNotActivated(); //@todo : Rahul Agrawal : Fix this later
                    //errorList.add(SharedConstants.ACCOUNT_NOT_ACTIVATED_MESSAGE + email);
                }

            }
            if (errorList.isEmpty()) {
                log.info("PasswordManagementService.isPasswordGenerationAllowed() for emailId=[{}] = Yes", email);
            } else {
                StringBuilder stringBuilder = new StringBuilder("\n");
                for (String eachError : errorList) {
                    stringBuilder.append(eachError);
                    stringBuilder.append("\n");
                }
                log.info("PasswordManagementService.isPasswordGenerationAllowed(): Not allowed for [{}] Reasons=[{}]", email, stringBuilder.toString());
            }
        }
        return errorList;
    }

    private boolean isTooFrequent(Customer existingCustomer) {
        Timestamp lastPasswordGenTime = existingCustomer.getLastPasswdGen();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long diff = Math.abs(lastPasswordGenTime.getTime() - currentTimestamp.getTime());
        int timeDiffInMinutes = (int) ((diff / 1000) / 60);
        log.debug("PasswordManagementService: Last Reset was {} min ago", timeDiffInMinutes);
        if (timeDiffInMinutes < 360) {
            log.info("PasswordManagementService: Last Password Reset for {} was {} min ago", existingCustomer.getEmail(), timeDiffInMinutes);
            return true;
        } else {
            return false;
        }
    }
}
