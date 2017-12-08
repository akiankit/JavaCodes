package com.bluestone.app.account;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.dao.CustomerDao;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Role;
import com.bluestone.app.authentication.RoleDao;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.LocalReflectionUtils;
import com.bluestone.app.core.util.Util;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private RoleDao roleDao;

    public Map<String, String> getBreadCrumbForMyAccount(String lastLink) {
        Map<String, String> breadCrumb = new LinkedHashMap<String, String>();
        breadCrumb.put("Home", "");
        breadCrumb.put("My Account", "account/goldminehistory");
        breadCrumb.put(lastLink, "last");
        return breadCrumb;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @RaiseEvent(eventType = EventType.CUSTOMER_REGISTERED)
    public void createNewCustomer(Customer newCustomer) throws Exception {
        log.info("CustomerService.createNewCustomer()");
        Set<Role> roleSet = new HashSet<Role>();
        Role role = roleDao.getEnabledRoleByRoleName(Constants.CUSTOMER_ROLE);
        if (role != null) {
            roleSet.add(role);
        }
        newCustomer.setRoles(roleSet);
        if (newCustomer.getNewsletter()) {
            Calendar currentDate = Calendar.getInstance();
            newCustomer.setNewsLetterDateAdd(currentDate.getTime());
            newCustomer.setIpRegistrationNewsLetter(Util.getMessageContext().getClientIP());
        }

        customerDao.create(newCustomer);
        MessageContext messageContext = Util.getMessageContext();
        messageContext.put(MessageContext.CUSTOMERID, newCustomer.getId());
    }


    public Customer getCustomerByEmail(String email) {
        log.debug("CustomerService.getCustomerByEmail(): {}", email);
        Customer existingCustomer = customerDao.getCustomerFromEmail(email);
        return existingCustomer;
    }

    public Customer getCustomerByFacebookId(String facebookId) {
        log.debug("CustomerService.getCustomerByFacebookId(): {}", facebookId);
        return customerDao.getCustomerByFacebookId(facebookId);
    }

    public Customer getCustomerById(Long id) {
        return customerDao.find(Customer.class, id, true);
    }


    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public boolean updateCustomer(String attributeName, Object attributeValue, Customer customer) {
        log.info("CustomerService.updateCustomer() : attributeName={} attributeValue={}", attributeName, attributeValue);
        if ("newsletter".equalsIgnoreCase(attributeName)) {
            boolean attribValue = Boolean.parseBoolean((String) attributeValue);
            if (attribValue == true) {
                Calendar currentDate = Calendar.getInstance();
                LocalReflectionUtils.setAttribute("newsLetterDateAdd", currentDate.getTime(), customer);
                LocalReflectionUtils.setAttribute("ipRegistrationNewsLetter", Util.getMessageContext().getClientIP(), customer);
            }
        }
        return LocalReflectionUtils.setAttribute(attributeName, attributeValue, customer);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Customer updateCustomer(Customer customer) {
        log.debug("CustomerService.updateCustomer()");
        return customerDao.update(customer);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean activateAccountAndSetPassword(Customer customer, String hashedPassword) {
        return customerDao.activateAccountAndSetPassword(customer, hashedPassword);
    }
}
