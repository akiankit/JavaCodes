package com.bluestone.app.integration;

import com.google.common.base.Throwables;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.account.model.User;
import com.bluestone.app.core.ProductionAlert;
import com.bluestone.app.core.util.SessionUtils;

/**
 * @author Rahul Agrawal
 *         Date: 1/24/13
 */
@Service(value = "emailAlertService")
public class EmailAlertService implements ProductionAlert {

    private static final Logger log = LoggerFactory.getLogger(EmailAlertService.class);

    @Value("${build.mode}")
    private String buildMode;

    @Override
    public void send(String message) {
        StringBuilder logMsg = new StringBuilder("\n----------------------------------\n");

        // not sure abt the exact behaviour of these so playing defensive.
        // it seems to be giving this error -
        /*
        org.apache.shiro.UnavailableSecurityManagerException: No SecurityManager accessible to the calling code,
        either bound to the org.apache.shiro.util.ThreadContext or as a vm static singleton.
        This is an invalid application configuration.
        at org.apache.shiro.SecurityUtils.getSecurityManager(SecurityUtils.java:123)
        at org.apache.shiro.subject.Subject$Builder.<init>(Subject.java:627)
        at org.apache.shiro.SecurityUtils.getSubject(SecurityUtils.java:56)
        at com.bluestone.app.integration.EmailAlertService.send(EmailAlertService.java:33)
         */
        try {
            Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated()) {
                User user = SessionUtils.getAuthenticatedUser();
                if (user != null) {
                    logMsg.append("\nUser Logged in {}").append(user.toString()).append("\n");
                }
            }
        } catch (Exception exception) {
            log.info("Error: EmailAlertService.send(): ", exception);
        }

        logMsg.append(message);

        if ("dev".equalsIgnoreCase(buildMode)) {
            log.info("EmailAlert.send(): As the build mode is [{}], disabling the email notifications", buildMode);
        } else {
            if ("test".equalsIgnoreCase(buildMode)) {
                log.error("Alert from TEST BED ***\n\nDetails=" + message);
            } else {
                log.error("\n[Alert From Production] Something went wrong:\n" + message);
            }

        }
    }

    @Override
    public void send(String message, Throwable throwable) {
        this.send(new StringBuilder(message)
                          .append("\n")
                          .append(throwable.toString())
                          .append("\n-----------Stacktrace--------------\n")
                          .append(Throwables.getStackTraceAsString(throwable))
                          .toString());
    }

    public String getBuildMode() {
        return buildMode;
    }

    public void setBuildMode(String buildMode) {
        this.buildMode = buildMode;
    }
}
