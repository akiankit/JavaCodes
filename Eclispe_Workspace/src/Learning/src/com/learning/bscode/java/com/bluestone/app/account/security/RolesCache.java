package com.bluestone.app.account.security;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Permission;
import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.authentication.RoleDao;

/**
 * @author Rahul Agrawal
 *         Date: 5/27/13
 */
@Service(value = "rolesCache")
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class RolesCache {

    private static final Logger log = LoggerFactory.getLogger(RolesCache.class);

    @Autowired
    private RoleDao roleDao;

    //@Cacheable(cacheName = "userRoles")
    public Set<Role> getAllImpliedRoles(User user) {
        log.debug("RolesCache.getAllImpliedRoles() for {}", user.getEmail());

        List<Role> childRoles = new LinkedList<Role>();
        Set<Role> userRoles = user.getRoles();
        if (userRoles != null) {
            childRoles = roleDao.getAllChildRoles(userRoles); // distinct roles are returned.
            childRoles.addAll(userRoles);
        }
        return new HashSet<Role>(childRoles);
    }

    //@Cacheable(cacheName = "roles_permission")
    public Set<Permission> getAllPermissions(Set<Role> rolesSet) {
        log.info("RolesCache.getAllPermissions()");
        Set<Permission> result = new HashSet<Permission>();
        /*Role adminRole = roleDao.getEnabledRoleByRoleName("administrator");
        if rolesSet.contains(adminRole){
            result.add()
        }*/
        for (Role eachRole : rolesSet) {
            Set<Permission> permissions = eachRole.getPermissions();
            result.addAll(permissions);
        }
        return result;
    }
}
