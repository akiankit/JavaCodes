package com.bluestone.app.account.security;

import java.util.Set;

import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.core.util.Constants;

/**
 * @author Rahul Agrawal
 *         Date: 6/6/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class CustomerSpecificCheckService {

    //@todo : Rahul Agrawal : Need a better name for this class.
    //Initial idea here is to have customer specific checks..

    private static final Logger log = LoggerFactory.getLogger(CustomerSpecificCheckService.class);

    @Autowired
    private RolesCache rolesCache;

    public boolean hasOnlyACustomerRole(Subject subject) {
        log.debug("UserRoleCheckService.isACustomer() : Subject={}", subject);
        boolean isLoggedInCustomer = false;
        if (subject.isAuthenticated()) {
            User user = (User) subject.getPrincipal();
            Set<Role> allImpliedRoles = rolesCache.getAllImpliedRoles(user);
            // users with just customer role should be barred.
            isLoggedInCustomer = (allImpliedRoles.size() == 1) && (subject.hasRole(Constants.CUSTOMER_ROLE));
        }

        return isLoggedInCustomer;
    }
}
