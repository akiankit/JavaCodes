package com.bluestone.app.authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;

@Repository
public class RoleDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(RoleDao.class);
    
    public Role getEnabledRoleByRoleName(String roleName) {
        EntityManager entityManager = getEntityManagerForActiveEntities();
        return getByRoleName(roleName, entityManager);
    }

    public Role getDisabledRoleByRoleName(String roleName) {
        EntityManager entityManager = getEntityManagerForNonActiveEntities();
        return getByRoleName(roleName, entityManager);
    }
    
    private Role getByRoleName(String roleName, EntityManager entityManager) {
        Query getRoleByRoleNameQuery = entityManager.createNamedQuery("role.getRoleByRoleName");
        getRoleByRoleNameQuery.setParameter("roleName", roleName);
        List<Role> rolesList = getRoleByRoleNameQuery.getResultList();
        if (rolesList == null || (rolesList != null && rolesList.size() == 0)) {
            return null;
        } else {
            return rolesList.get(0);
        }
    }
    
    
    public void markRoleAsDisabled(String roleName) {
        Role role = getEnabledRoleByRoleName(roleName);
        markEntityAsDisabled(role);
    }
    
    public void markRoleAsEnabled(String roleName) {
        Role role = getDisabledRoleByRoleName(roleName);
        markEntityAsEnabled(role);
    }
        
    public List<Role> getUnSelectedRolesListByUserId(User user) {
        Query getUnSelectedRolesListByUserIdQuery = getEntityManagerForActiveEntities().createNamedQuery("role.getUnSelectedRolesListByUserId");
        getUnSelectedRolesListByUserIdQuery.setParameter("rolesList", user.getRoles());
        return getUnSelectedRolesListByUserIdQuery.getResultList();
    }
    
    public List<Role> getRolesListByRolesId(Set<Long> rolesIdValues) {
        Query getRolesByRolesIdQuery = getEntityManagerForActiveEntities().createNamedQuery("role.getRolesByRolesId");
        getRolesByRolesIdQuery.setParameter("rolesId", rolesIdValues);
        return getRolesByRolesIdQuery.getResultList();
    }
    
    public List<Role> getRolesList(boolean forActive){
    	EntityManager entityManager = forActive == true ? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
    	Query rolesListQuery = entityManager.createNamedQuery("role.getAllRolesList");
    	return rolesListQuery.getResultList();
    }
    
    public Map<Object, Object> getRolesListAndTotalCount(int start, ListFilterCriteria filterCriteria, boolean forActive) {

		EntityManager entityManager = forActive == true	? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();
		Query rolesCountQuery = entityManager.createQuery("select count(r.id) from Role r where r." + filterCriteria.getColumn()
															+ " like '%" + filterCriteria.getValueToSearch() + "%'");
		
		Query rolesListQuery = entityManager.createQuery("select r from Role r  where r." + filterCriteria.getColumn() + " like '%"
															+ filterCriteria.getValueToSearch() + "%' order by " + 
															  filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder());
		rolesListQuery.setFirstResult(start);
		rolesListQuery.setMaxResults(filterCriteria.getItemsPerPage());
		
		List<Role> rolesList = rolesListQuery.getResultList();
		Map<Object, Object> rolesDetails = new HashMap<Object, Object>();
		
		rolesDetails.put("list", rolesList);
		rolesDetails.put("totalCount", ((Long) rolesCountQuery.getSingleResult()).intValue());
		return rolesDetails;
	}

    /*public List<Role> getAllChildRoles(Role role) {
        log.debug("RoleDao.getAllChildRoles() for Role=[{}]", role);
        final long roleId = role.getId();
        final EntityManager entityManager = getEntityManagerForActiveEntities();
        Query query = entityManager.createNativeQuery("select * from Role r where r.parentRole_id =  " + roleId);
        List resultList = query.getResultList();
        return resultList;
    }*/

    public List<Role> getAllChildRoles(Set<Role> role) {
        log.debug("RoleDao.getAllChildRoles() for Roles=[{}]", role);
        Query query = getEntityManagerForActiveEntities().createNamedQuery("role.getAllChildRoles2");
        query.setParameter("roles", role);
        List resultList = query.getResultList();
        return resultList;
    }


}
