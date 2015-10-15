package com.bluestone.app.account;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.bluestone.app.account.model.User;

@Component(value = "shiroDbRealm")
public class ShiroDbRealm extends BSAuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(ShiroDbRealm.class);


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        final String email = token.getUsername();
        log.debug("ShiroDbRealm.doGetAuthenticationInfo() for [{}]", email);
        User existingUser = userDao.getUserFromEmail(email);
        if (existingUser == null) {
            log.info("No user with username=[{}] found in the system.", email);
            throw new UnknownAccountException("User does not exist in the system");
        }
        if (!existingUser.isAccountActivated()) {
            throw new AccountNotActivatedException(email, "User account is not activated.");
        }
        return new SimpleAuthenticationInfo(existingUser, existingUser.getPassword(), getName());
    }


    @Override
    public boolean supports(AuthenticationToken token) {
        if (token != null) {
            return (token instanceof UsernamePasswordToken);
        }
        return false;
    }

}
