package com.bluestone.app.account;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.bluestone.app.account.dao.UserDao;
import com.bluestone.app.account.model.Permission;
import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.account.security.RolesCache;

/**
 * @author Rahul Agrawal
 *         Date: 5/2/13
 */
@Component(value = "bsAuthorizingRealm")
public abstract class BSAuthorizingRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(BSAuthorizingRealm.class);

    @Autowired
    protected UserDao userDao;

    @Autowired
    private RolesCache rolesCache;

    @PostConstruct
    private void validate() {
        log.debug("BSAuthorizingRealm.validate()");
        Assert.notNull(rolesCache, "BSAuthorizingRealm: Instance of RolesCache should not be null.");
        Assert.notNull(userDao, "BSAuthorizingRealm: Instance of userDao should not be null.");
    }

    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link org.apache.shiro.authz.SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see org.apache.shiro.authz.SimpleAuthorizationInfo
     */
    @Override
    public AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("BSAuthorizingRealm.doGetAuthorizationInfo(): for Realm Name=[{}]", getName());
        //@todo : Rahul Agrawal :  have caching enabled

        SimpleAuthorizationInfo simpleAuthorizationInfo;
        if (principals.isEmpty()) {
            log.debug("BSAuthorizingRealm.doGetAuthorizationInfo(): principals is empty");
            throw new UnauthorizedException();
        } else {
            final User user = (User) principals.getPrimaryPrincipal();
            simpleAuthorizationInfo = new SimpleAuthorizationInfo();
            Set<Role> userRolesSet = rolesCache.getAllImpliedRoles(user);
            for (Role role : userRolesSet) {
                if (isAdminRole(role)) {
                    assignAllPermissions(simpleAuthorizationInfo);
                    break;
                } else {
                    for (Permission permission : role.getPermissions()) {
                        simpleAuthorizationInfo.addStringPermission(permission.getEntity() + ":" + permission.getType());
                    }
                    simpleAuthorizationInfo.addRole(role.getName());
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    private void assignAllPermissions(SimpleAuthorizationInfo simpleAuthorizationInfo) {
        Set<String> existingPermissions = simpleAuthorizationInfo.getStringPermissions();
        if (existingPermissions != null) {
            existingPermissions.clear();
        }
        Set<String> existingRoles = simpleAuthorizationInfo.getRoles();
        if (existingRoles != null) {
            existingRoles.clear();
        }
        simpleAuthorizationInfo.addStringPermission("*");// Providing all the permissions
        simpleAuthorizationInfo.addRole("Administrator");
        log.debug("BSAuthorizingRealm: Providing all permissions to the administrator role");
    }

    private boolean isAdminRole(Role role) {
        return "administrator".equalsIgnoreCase(role.getName());
    }
}
