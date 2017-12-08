package com.bluestone.app.account;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link org.apache.shiro.authc.pam.AuthenticationStrategy} implementation that throws the first exception it gets
 * and ignores all subsequent realms. If there is no exceptions it works as the {@link FirstSuccessfulStrategy}
 * <p/>
 * WARN: This approach works fine as long as there is ONLY ONE Realm per Token type.
 */
public class FirstExceptionStrategy extends FirstSuccessfulStrategy {

    private static final Logger log = LoggerFactory.getLogger(FirstExceptionStrategy.class);

    @Override
    public AuthenticationInfo afterAttempt(Realm realm,
                                           AuthenticationToken token,
                                           AuthenticationInfo singleRealmInfo,
                                           AuthenticationInfo aggregateInfo,
                                           Throwable t) throws AuthenticationException {
        log.debug("FirstExceptionStrategy.afterAttempt(): Realm=[{}] AuthenticationToken.principal=[{}] ", realm.getName(), token.getPrincipal(), t);
        if ((t != null) && (t instanceof AuthenticationException)) {
            throw (AuthenticationException) t;
        }
        return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
    }

}