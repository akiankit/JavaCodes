package com.bluestone.app.social;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.NoPasswordToken;
import com.bluestone.app.account.SharedConstants;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.Util;

/**
 * @author Rahul Agrawal
 *         Date: 4/12/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class FacebookLoginService {

    private static final Logger log = LoggerFactory.getLogger(FacebookLoginService.class);


    @RaiseEvent(eventType = EventType.CUSTOMER_LOGIN)
    public String facebookLogin(String email, String facebookId) {
        // kept the log statements at info level, so that if there is some unusual activity, atleast it will be logged.
        log.info("FacebookAccountLoginService.facebookLogin():using email=[{}] facebookId=[{}] from IP Address=[{}]", email, facebookId, Util.getMessageContext().getClientIP());
        NoPasswordToken noPasswordToken = new NoPasswordToken(email);
        Subject subject = SecurityUtils.getSubject();
        String error = "";
        try {
            subject.login(noPasswordToken);
        } catch (UnknownAccountException unknownAccountException) {
            error = SharedConstants.EMAIL_ID_NOT_EXISTS + " " + email;
            log.info("FacebookAccountLoginService.facebookLogin(): Account does not exist for email=[{}] Root Cause={}", email,
                     Throwables.getRootCause(unknownAccountException).toString());
        } catch (IncorrectCredentialsException ice) {
            //TODD: should never happen
            error = SharedConstants.INVALID_EMAIL_OR_PASSWORD_MESSAGE;
            log.info("FacebookAccountLoginService.facebookLogin(): Wrong password for email={} . {}", email, ice.getMessage());
            log.trace("FacebookAccountLoginService.facebookLogin(): Wrong password for email={} . {}", email, ice.getMessage(), ice);
        } catch (AuthenticationException authenticaionException) {
            error = "Unknown Error";
            log.error("Error: FacebookAccountLoginService.facebookLogin(): UnExpected Error {} ", authenticaionException.getMessage(), authenticaionException);
        } catch (Exception e) {
            error = "Unknown Error";
            log.error("Error: FacebookAccountLoginService.facebookLogin(): UnknownError ={} ", e.getLocalizedMessage(), e);
        }

        if (StringUtils.isNotEmpty(error)) {
            log.info("ERROR at FacebookAccountLoginService.facebookLogin() for user=[{}] from IP=[{}] Reason=[{}]", email, Util.getMessageContext().getClientIP(), error);
        }
        return error;
    }
}
