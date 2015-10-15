package com.bluestone.app.account;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.User;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.core.util.Util;

/**
 * @author Rahul Agrawal
 *         Date: 4/12/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = Exception.class)
public class AccountLoginService {

    private static final Logger log = LoggerFactory.getLogger(AccountLoginService.class);

    private static final String INVALID_EMAIL_OR_PASSWORD_MESSAGE = "Invalid email address and password combination";
    private static final String LOG_IN_ACTIVATION_ERROR_MESSAGE = "Please activate your account before logging in. We have sent you an activation mail at : ";

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountManagementEmailService accountManagementEmailService;

    @Qualifier("emailAlertService")
    @Autowired
    private ProductionAlert productionAlert;


    public void logout() {
        log.debug("AccountLoginService.logout()");
        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.logout();
        } catch (Exception exception) {
            User authenticatedUser = SessionUtils.getAuthenticatedUser();
            if (authenticatedUser == null) {
                log.warn("Failed:AccountLoginService.logout(): There is no authenticated user. Principal={} Cause={}", currentUser.getPrincipal(), exception.getLocalizedMessage(),
                         Throwables.getRootCause(exception));
            } else {
                log.warn("Failed at AccountLoginService.logout(): {} CurrentUser's principal={}", authenticatedUser, currentUser.getPrincipal(), exception);
            }
        }
    }
    @RaiseEvent(eventType = EventType.CUSTOMER_LOGIN)
    public String login(String email, String password) {
        // kept the log statements at info level, so that if there is some unusual activity, atleast it will be logged.
        final String clientIP = Util.getMessageContext().getClientIP();
        log.info("AccountLoginService.login():using email={} from IP Address=[{}]", email, clientIP);
        String md5Password = PasswordProcessors.getMD5HashedPassword(password);
        UsernamePasswordToken token = new UsernamePasswordToken(email, md5Password, clientIP);
        token.setRememberMe(false);
        Subject subject = SecurityUtils.getSubject();
        String error = "";
        try {
            subject.login(token);
        } catch (UnknownAccountException unknownAccountException) {
            error = SharedConstants.EMAIL_ID_NOT_EXISTS + " " + email;
            log.info("Account does not exist for email=[{}] Root Cause={}", email, Throwables.getRootCause(unknownAccountException).toString());
        } catch (AccountNotActivatedException anae) {
            error = LOG_IN_ACTIVATION_ERROR_MESSAGE + email;
            log.info("Account not activated for {} : {}", email, anae.toString());
            //log.trace("Account not yet activated for {} : {}", email, anae.getMessage(), anae);
            try {
                Customer customer = customerService.getCustomerByEmail(email);
                if (customer != null) {
                    accountManagementEmailService.sendWelcomeEmail(customer);
                } else {
                    //ideally we shd not land here, but as we saw QA got some tests and we landed here, so find more such cases.
                    log.error("Error: AccountLoginService.login(): *** This needs investigation *** Customer=[{}] does not exist in the system and we are trying to activate his account", email);
                    throw new RuntimeException("This needs investigation. Customer account does not exist so why activating " + email);
                }
            } catch (Exception exception) {
                log.error("Some Error while sending activation mail for user=[{}]", email, exception);
                productionAlert.send("Failed to enqueue activation email for " + email, exception);
            }
        } catch (IncorrectCredentialsException ice) {
            error = INVALID_EMAIL_OR_PASSWORD_MESSAGE;
            log.info("Wrong password for email={} . {}", email, ice.getMessage());
            log.trace("Wrong password for email={} . {}", email, ice.getMessage(), ice);
        } catch (AuthenticationException authenticationException) {
            error = "Unknown Error";
            log.error("Error: AccountLoginService.login(): UnExpected Error {}", authenticationException.getMessage(), authenticationException);
        } catch (Exception e) {
            error = "Unknown Error";
            log.error("Error: AccountLoginService.login(): Unknown Error ={}", e.getMessage(), e);
        } finally {
            token.clear(); // we shd not leave the token uncleared. It is a security loophole.
        }

        if (StringUtils.isNotBlank(error)) {
            log.info("ERROR at CustomerService.login() for user=[{}] from IP=[{}] Reason=[{}]", email, clientIP, error);
        } else {
            log.info("**Success**:AccountLoginService.login(): using email=[{}] from IP Address=[{}]", email, clientIP);
        }
        return error;
    }
}
