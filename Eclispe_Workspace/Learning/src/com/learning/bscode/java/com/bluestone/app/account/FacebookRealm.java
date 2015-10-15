package com.bluestone.app.account;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.bluestone.app.account.dao.UserDao;
import com.bluestone.app.account.model.User;

@Component(value = "facebookRealm")
public class FacebookRealm extends AuthenticatingRealm {

    private static final Logger log = LoggerFactory.getLogger(FacebookRealm.class);

    @Autowired
    private UserDao userDao;

    @PostConstruct
    private void validate() {
        log.debug("FacebookRealm.validate()");
        Assert.notNull(userDao, "FacebookRealm: Instance of userDao should not be null.");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        NoPasswordToken token = (NoPasswordToken) authenticationToken;
        log.debug("FacebookRealm.doGetAuthenticationInfo()", token.getUserName());
        User existingUser = userDao.getUserFromEmail(token.getUserName());
        if (existingUser == null) {
            log.warn("No user with username=[{}] found in the system.", token.getUserName());
            throw new UnknownAccountException("User " + token.getUserName() + " does not exist in the system");
        }
        return new NoPasswordAuthenticationInfo(existingUser, getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        if (token != null) {
            return (token instanceof NoPasswordToken);
        }
        return false;
    }

}
