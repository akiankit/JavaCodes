package com.bluestone.app.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;

@Repository("userDao")
public class UserDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(UserDao.class);
    
    public User getUserFromEmail(String email) {
        log.debug("UserDao.getUserFromEmail():[{}]", email);
        Query getByEmailIdQuery = getEntityManagerForActiveEntities().createNamedQuery("User.findByEmailId");
        getByEmailIdQuery.setParameter("email", email);
        List<User> userList = getByEmailIdQuery.getResultList();
        if (userList.size() == 0) {
            return null;
        } else {
            return userList.get(0);
        }
    }
    
    /*private List<User> getAllUsersNonCustomers(EntityManager entityManager) {
        String customerRoleName = "customer";
        Query firstQuery = getEntityManagerForActiveEntities().createNamedQuery("user.getUsersWithMoreThanOneRole");
        List<User> firstList = firstQuery.getResultList();
        Query secondQuery = getEntityManagerForActiveEntities().createNamedQuery("user.getUsersWithOneRoleNotCustomer");
        secondQuery.setParameter("customerRoleName", customerRoleName);
        List<User> secondList = secondQuery.getResultList();
        if ((secondList == null || secondList.size() == 0) && (firstList == null || firstList.size() == 0)) {
            return null;
        } else if ((secondList == null || secondList.size() == 0)) {
            return firstList;
        } else if ((firstList == null || firstList.size() == 0)) {
            return secondList;
        } else {
            secondList.addAll(firstList);
            return secondList;
        }
    }
    
    public List<User> getAllEnabledOnlyCustomersList() {
        EntityManager entityManager = getEntityManagerForActiveEntities();
        return getUsersWithOnlyCustomerRole(entityManager);
        
    }
    
    public List<User> getAllDisabledOnlyCustomersList() {
        EntityManager entityManager = getEntityManagerForNonActiveEntities();
        return getUsersWithOnlyCustomerRole(entityManager);
    }
    
    private List<User> getUsersWithOnlyCustomerRole(EntityManager entityManager) {
        Role role = roleDao.getEnabledRoleByRoleName("customer");
        Query getAllUsersNonCustomersListQuery = entityManager.createNativeQuery("create table temp as (select user.id as id from user inner join user_role on user.id = user_role.user_id group by user.id having count(user_role.role_id) =1)");
        getAllUsersNonCustomersListQuery.executeUpdate();
        Query tempQuery = entityManager.createNativeQuery("select user.* from user inner join user_role on user.id = user_role.user_id inner join temp on temp.id=user.id where user_role.role_id =:customerRoleId group by user.id having count(user_role.role_id) =1;");
        tempQuery.setParameter("customerRoleId", role.getId());
        List<User> userList = (List<User>) tempQuery.getResultList();
        Query deleteQuery = entityManager.createNativeQuery("drop table temp");
        deleteQuery.executeUpdate();
        return userList;
    }*/
    
    public void markUserAsDisabled(String userId) {
        User user = find(User.class, Long.parseLong(userId), true);
        user.setIsActive((short) 0);
    }
    
    public void markUserAsEnabled(String userId) {
        User user = find(User.class, Long.parseLong(userId), false);
        user.setIsActive((short) 1);;
    }
    
    public List<Role> getAllChildRoles(List<Long> rolesIds) {
        Query getAllChildRolesQuery = getEntityManagerForActiveEntities().createNamedQuery("role.getAllChildRoles");
        getAllChildRolesQuery.setParameter("rolesIds", rolesIds);
        return getAllChildRolesQuery.getResultList();
    }
    
	public Map<Object, Object> getUsersListAndTotalCount(int start, ListFilterCriteria filterCriteria, boolean forActive) {

		EntityManager entityManager = forActive == true ? getEntityManagerForActiveEntities() : getEntityManagerForNonActiveEntities();

		Query usersCountQuery;
		Query usersListQuery;
		String sortBy = filterCriteria.getSortBy();
		String column = filterCriteria.getColumn();
		if (column.equalsIgnoreCase("role.name") && sortBy.equalsIgnoreCase("role.name")) {
			usersListQuery = entityManager.createQuery("select u from User u join u.roles role  " 
														+ "where role.name like '%" + filterCriteria.getValueToSearch() + "%' " 
														+ "group by u order by " + sortBy + " " + filterCriteria.getSortOrder());
			
			usersCountQuery = entityManager.createQuery("select count(u.id) from User u join u.roles role"
														+ " where role.name" + " like '%" + filterCriteria.getValueToSearch() + "%'");
			
		} else if (column.equalsIgnoreCase("role.name") && !sortBy.equalsIgnoreCase("role.name")) {
			usersListQuery = entityManager.createQuery("select u from User u join u.roles role  " 
														+ "where role.name like '%" + filterCriteria.getValueToSearch() + "%' " 
														+ "group by u order by u." + sortBy + " " + filterCriteria.getSortOrder());
			usersCountQuery = entityManager.createQuery("select count(u.id) from User u join u.roles role " +
														"where role.name" + " like '%" + filterCriteria.getValueToSearch() + "%'");
			
		} else if (!column.equalsIgnoreCase("role.name") && sortBy.equalsIgnoreCase("role.name")) {
			usersListQuery = entityManager.createQuery("select u from User u join u.roles role  " +
														"where u." + column	+ " like '%" + filterCriteria.getValueToSearch() + "%' " +
														"group by u order by " + sortBy + " " + filterCriteria.getSortOrder());
			usersCountQuery = entityManager.createQuery("select count(u.id) from User u " +
														"where u." + column + " like '%" + filterCriteria.getValueToSearch() + "%'");
		} else {
			usersListQuery = entityManager.createQuery("select u from User u " +
														"where u." + column + " like '%"+ filterCriteria.getValueToSearch() + "%' " +
														"order by " + sortBy + " " + filterCriteria.getSortOrder());
			usersCountQuery = entityManager.createQuery("select count(u.id) from User u " +
														"where u." + column + " like '%" + filterCriteria.getValueToSearch() + "%'");
		}

		usersListQuery.setFirstResult(start);
		usersListQuery.setMaxResults(filterCriteria.getItemsPerPage());

		List<Role> usersList = usersListQuery.getResultList();
		Map<Object, Object> usersDetails = new HashMap<Object, Object>();

		usersDetails.put("list", usersList);
		usersDetails.put("totalCount", ((Long) usersCountQuery.getSingleResult()).intValue());
		return usersDetails;
	}
}
