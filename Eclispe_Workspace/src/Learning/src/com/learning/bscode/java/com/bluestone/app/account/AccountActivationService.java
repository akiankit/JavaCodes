package com.bluestone.app.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;

/**
 * @author Rahul Agrawal
 *         Date: 4/14/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class AccountActivationService {

    private static final Logger log = LoggerFactory.getLogger(AccountActivationService.class);

    @Autowired
    private CustomerService customerService;

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean activateAccount(String password, String email) {
        log.debug("AccountActivationService.activateAccount(): for email={}", email);
        String hashedPassword = PasswordProcessors.getMD5HashedPassword(password);
        Customer customer = customerService.getCustomerByEmail(email);
        return customerService.activateAccountAndSetPassword(customer, hashedPassword);
    }
}
