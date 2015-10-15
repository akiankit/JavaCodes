package com.bluestone.app.admin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.dao.UserDao;
import com.bluestone.app.account.model.Permission;
import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.authentication.PermissionDao;
import com.bluestone.app.authentication.RoleDao;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.PaginationUtil;

@Service
public class UserManagementService {
    
    private static final Logger log               = LoggerFactory.getLogger(UserManagementService.class);
    
    private static final String permissionTypes[] = { "view", "add", "edit", "delete" ,"export"};
    
    @Autowired
    private PermissionDao       permissionDao;
    
    @Autowired
    private UserDao             userDao;
    
    @Autowired
    private RoleDao             roleDao;
    
    // Permission operations
    public List<String> getPermissionsList(boolean isActiveValue) {
        return permissionDao.getPermissionsList(isActiveValue);
    }
    
    public boolean validatePermissionForDeactivation(String entity) {
        List<Permission> permissionsList = permissionDao.getEnabledPermissionListByEntityName(entity);
        for (int i = 0; i < permissionsList.size(); i++) {
            if (permissionsList.get(i).getRoles().size() > 0) {
                return false;
            }
        }
        return true;
    }
    
    public Permission getSinglePermissionByEntityName(String entity) {
        if (!StringUtils.isBlank(entity)) {
            return permissionDao.getSinglePermissionByEntityName(entity);
        } else {
            return null;
        }
    }
    
    public String validatePermissionForInsertion(String entity, String oldEntityValue) {
        String message = null;
        if (!StringUtils.isBlank(oldEntityValue)) {
            if (!oldEntityValue.equalsIgnoreCase(entity)) {
                oldEntityValue = null;
            }
        }
        if (StringUtils.isBlank(oldEntityValue)) {
            List<Permission> permissionList = permissionDao.getPermissionsListByEntityName(entity);
            if ((permissionList == null) || !(permissionList != null && permissionList.size() == 0)) {
                message = "Permission already exists";
            }
        }
        return message;
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void savePermissionsByEntityName(String entity) {
        for (String permissionType : permissionTypes) {
            Permission permission = new Permission();
            permission.setType(permissionType);
            permission.setEntity(entity);
            permissionDao.create(permission);
        }
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void disablePermission(String entity) {
        permissionDao.markPermissionAsDisabled(entity);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void enablePermission(String entity) {
        permissionDao.markPermissionAsEnabled(entity);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updatePermissionsByEntityName(String entity, String oldEntityValue) {
        permissionDao.updatePermissionsByEntityName(entity, oldEntityValue);
    }
    
    public Map<String, Map<String, Boolean>> getAllPermissionByRoleName(String roleName) {
        List<Permission> rolePermissionsList = new ArrayList<Permission>();
        if (roleName != null) {
            Role role = getRoleByRoleName(roleName, true);
            rolePermissionsList.addAll(role.getPermissions());
        }
        List<String> allPermissionsEntity = permissionDao.getPermissionsList(true);
        return getPermissionsToShow(rolePermissionsList, allPermissionsEntity);
    }
    
    public Map<String, Map<String, Boolean>> getPermissionsToShow(List<Permission> rolePermissionsList, List<String> allPermissionsEntity) {
        Map<String, Map<String, Boolean>> permissionsMap = new HashMap<String, Map<String, Boolean>>();
        for (String entity : allPermissionsEntity) {
            Map<String, Boolean> permissionTypesMap = new HashMap<String, Boolean>();
            for (int i = 0; i < permissionTypes.length; i++) {
                permissionTypesMap.put(permissionTypes[i], false);
            }
            permissionsMap.put(entity, permissionTypesMap);
        }
        for (Permission permission : rolePermissionsList) {
            Map<String, Boolean> permissionTypesMap = new HashMap<String, Boolean>();
            if (permissionsMap.containsKey(permission.getEntity())) {
                permissionTypesMap = permissionsMap.get(permission.getEntity());
            }
            permissionTypesMap.remove(permission.getType());
            permissionTypesMap.put(permission.getType(), true);
            permissionsMap.put(permission.getEntity(), permissionTypesMap);
        }
        
        return permissionsMap;
    }
    
    public List<Role> getRolesList(boolean forActive){
    	return roleDao.getRolesList(forActive);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void saveRole(String newRoleName, Long parentRoleId, String[] permissionsName, String[] viewValues, String[] addValues, String[] editValues, String[] deleteValues, String[] exportValues,Long roleId) {
        Set<Permission> totalPermissionsSet = new HashSet<Permission>();
        int loopCount = permissionsName.length;
        Role newRole = new Role();
        for (int i = 0; i < loopCount; i++) {
            Boolean[] permissionTypeValues = new Boolean[permissionTypes.length];
            permissionTypeValues[0] = Boolean.parseBoolean(viewValues[i]);
            permissionTypeValues[1] = Boolean.parseBoolean(addValues[i]);
            permissionTypeValues[2] = Boolean.parseBoolean(editValues[i]);
            permissionTypeValues[3] = Boolean.parseBoolean(deleteValues[i]);
            permissionTypeValues[4] = Boolean.parseBoolean(exportValues[i]);
            if (!permissionTypeValues[0] && !permissionTypeValues[1] && !permissionTypeValues[2]
                    && !permissionTypeValues[3] && !permissionTypeValues[4]) {
                continue;
            } else {
                Set<String> typesSet = new HashSet<String>();
                for (int j = 0; j < permissionTypes.length; j++) {
                    if (permissionTypeValues[j] == true) {
                        typesSet.add(permissionTypes[j]);
                    }
                }
                List<Permission> permissionListByEntity = permissionDao.getPermissionListByEntityNamesAndTypes(permissionsName[i], typesSet);
                for (int j = 0; j < permissionListByEntity.size(); j++) {
                    totalPermissionsSet.add(permissionListByEntity.get(j));
                }
            }
        }
        if (roleId != 0) {
            newRole = roleDao.find(Role.class, roleId, true);
        }
        newRole.setName(newRoleName);
        newRole.setParentRole(roleDao.find(Role.class, parentRoleId, true));
        newRole.setPermissions(totalPermissionsSet);
        roleDao.create(newRole);
    }
    
    public String validateRoleForDeactivation(String roleName) {
        Role role = getRoleByRoleName(roleName, true);
        Set<User> usersForRole = role.getUsers();
        for (User user : usersForRole) {
            if (user.getIsActive() == (short) 1) {
                return "Role is attached to one or more users";
            }
        }
        return null;
    }
    
    public String validateRoleForInsertion(String newRoleName, Long roleId) {
        Role role = null;
        if (StringUtils.isBlank(newRoleName)) {
            return "Role name cannot be empty";
        }
        if (roleId != 0) {
            role = getRoleByRoleId(roleId);
            if (!(role.getName().equalsIgnoreCase(newRoleName))) {
                roleId = (long) 0;
            }
        }
        if (roleId == 0) {
            role = getRoleByRoleName(newRoleName, true);
            if (role != null) {
                return "Role already exists";
            }
            role = getRoleByRoleName(newRoleName, false);
            if (role != null) {
                return "Role already exists";
            }
        }
        return null;
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void disableRole(String roleName) {
        roleDao.markRoleAsDisabled(roleName);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void enableRole(String roleName) {
        roleDao.markRoleAsEnabled(roleName);
    }
    
    public Role getRoleByRoleName(String roleName, boolean isActive) {
        if (isActive) {
            return roleDao.getEnabledRoleByRoleName(roleName);
        } else {
            return roleDao.getDisabledRoleByRoleName(roleName);
        }
    }
    
    public Role getRoleByRoleId(Long roleId) {
        return roleDao.findAny(Role.class, roleId);
    }
    
    public User getUserById(Long userId, boolean isActive) {
        return userDao.find(User.class, userId, isActive);
    }
    
    public List<Role> getUnSelectedRolesListByUserId(Long userId) {
        User user = getUserById(userId, true);
        if (user.getRoles().size() > 0) {
            return roleDao.getUnSelectedRolesListByUserId(user);
        } else {
            return (List<Role>) roleDao.getRolesList(true);
        }
    }
    
    public boolean isEmailAvailable(Long userId, String userEmail) {
        User user = userDao.findAny(User.class, userId);
        userEmail = userEmail.trim();
        if (user != null && user.getEmail().equalsIgnoreCase(userEmail)) {
            return true;
        }
        user = userDao.getUserFromEmail(userEmail);
        return (user == null ? true : false);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUserPassword(Long userId, String password) {
        User user = getUserById(userId, true);
        String hashedPassword = new Md5Hash(Constants.PASSWORD_SALT + password).toString();
        user.setPassword(hashedPassword);
        userDao.update(user);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void addUserData(String userEmail, String userName, String password, String[] rolesId) {
        User user = new User();
        user.activateAccount();
        String hashedPassword = new Md5Hash(Constants.PASSWORD_SALT + password).toString();
        user.setPassword(hashedPassword);
        saveUserData(userEmail, userName, rolesId, user);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void updateUserData(Long userId, String userEmail, String userName, String[] rolesId) {
        User user = getUserById(userId, true);
        saveUserData(userEmail, userName, rolesId, user);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void disableUser(String userId) {
        userDao.markUserAsDisabled(userId);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void enableUser(String userId) {
        userDao.markUserAsEnabled(userId);
    }
    
	private void saveUserData(String userEmail, String userName, String[] rolesId, User user) {
		user.setUserName(userName);
		user.setEmail(userEmail);
		Set<Role> rolesSet = new HashSet<Role>();
		Set<Role> existingRolesSet = user.getRoles();
		Set<Long> rolesIdValues = new HashSet<Long>();
		if(existingRolesSet != null && existingRolesSet.size() > 0){
			for (Role role : existingRolesSet) {
				if (role.getName().equalsIgnoreCase(Constants.CUSTOMER_ROLE)) {
					rolesIdValues.add(role.getId());
				}
			}
		}

		if (rolesId != null && rolesId.length > 0) {
			for (int i = 0; i < rolesId.length; i++) {
				rolesIdValues.add(Long.parseLong(rolesId[i]));
			}
			rolesSet.addAll(roleDao.getRolesListByRolesId(rolesIdValues));
		}
		user.setRoles(rolesSet);
		userDao.create(user);
	}
    
    public Set<User> getAllUsersListWithSpecificRole(String roleName) {
        Role role = getRoleByRoleName(roleName, true);
        return role.getUsers();
    }
    
    /*@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<User> getAllEnabledOnlyCustomersList() {
        return userDao.getAllEnabledOnlyCustomersList();
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public List<User> getAllDisabledOnlyCustomersList() {
        return userDao.getAllDisabledOnlyCustomersList();
    }*/
    
    public String validateUserForInsertion(Long userId, String userName, String userEmail, String[] rolesId) {
        if (!userName.matches(Constants.VALID_USER_NAME_REGEXP)) {
            return "User Name is not valid";
        } else if (!isEmailAvailable(userId, userEmail)) {
            return "Email is already registered";
        } else if (rolesId == null || (rolesId != null && rolesId.length == 0)) {
            return "At least one role should be selected";
        } else {
            return null;
        }
    }
    
    public Map<Object, Object> getRoleDetails(ListFilterCriteria filterCriteria, boolean forActive) {
    	int startingFrom = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> rolesListAndTotalCount = roleDao.getRolesListAndTotalCount(startingFrom, filterCriteria, forActive);
		String baseURLForPagination =   forActive == true ? "/admin/usermanagement/role/list" :"/admin/usermanagement/role/disabledlist";
		List<Role> rolesList = (List<Role>) rolesListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(rolesListAndTotalCount, filterCriteria.getItemsPerPage(),
												 filterCriteria.getP(), startingFrom, baseURLForPagination,rolesList.size());
		
		Map<String,String> searchFieldMap = new HashMap<String, String>();
		searchFieldMap.put("Id", "id");
		searchFieldMap.put("Name ","name");
		
		dataForView.put("searchFieldMap", searchFieldMap);
		
		return dataForView;
	}
    
    public Map<Object, Object> getUserDetails(ListFilterCriteria filterCriteria, boolean forActive) {
		int startingFrom = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
		Map<Object, Object> usersListAndTotalCount = userDao.getUsersListAndTotalCount(startingFrom, filterCriteria, forActive);
		String baseURLForPagination = forActive == true ? "/admin/usermanagement/user/list" : "/admin/usermanagement/user/disabledlist";
		List<User> usersList = (List<User>) usersListAndTotalCount.get("list");
		Map<Object, Object> dataForView = PaginationUtil.generateDataForView(usersListAndTotalCount, filterCriteria.getItemsPerPage(),
					 									filterCriteria.getP(), startingFrom, baseURLForPagination,usersList.size());
		Map<String,String> searchFieldMap = new HashMap<String, String>();
		searchFieldMap.put("Id", "id");
		searchFieldMap.put("Name ","userName");
		searchFieldMap.put("Email","email");
		searchFieldMap.put("Roles","role.name");
		
		dataForView.put("searchFieldMap", searchFieldMap);
		
		return dataForView;
	}
    
}
