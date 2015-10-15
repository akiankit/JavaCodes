package com.bluestone.app.core.util;

import java.util.Iterator;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.UserService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Role;
import com.bluestone.app.account.model.User;

@Component
public class SessionUtils {
	
	private static CustomerService customerService;
	private static UserService userService;

	
	public static String getCustomerAttribute(String attributeName){
		Customer customer = getAuthenticatedCustomer();
		String value="";
		Object attributeValue=LocalReflectionUtils.getAttribute(attributeName, customer);
		if(attributeValue!=null){
		    return attributeValue.toString();
		}else{
		    return value;
		}
	}

	public static Customer getAuthenticatedCustomer() {
		User user = getAuthenticatedUser();
		Customer customer = customerService.getCustomerByEmail(user.getEmail());
		return customer;
	}

	public static User getAuthenticatedUser() {
		Subject subject = SecurityUtils.getSubject();
		User user = (User)subject.getPrincipal();
		return user;
	}
	
	public static String getUserAttribute(String attributeName){
		User user = getAuthenticatedUser();
		user = userService.getUserByEmailId(user.getEmail());
		return LocalReflectionUtils.getAttribute(attributeName, user).toString();
	}
	
	public static Boolean userHasRole(String roleName){
	    User user= getAuthenticatedUser();
	    Set<Role> userRoles= user.getRoles();
	    Iterator<Role> userRolesIterator = userRoles.iterator();
	    while(userRolesIterator.hasNext()){
	        if(userRolesIterator.next().getName().equalsIgnoreCase(roleName)){
	            return true;
	        }
	    }
	    return false;
	}
	
	@Autowired
    public void setUserService(UserService userService) {
		//hack for autowiring userService
		SessionUtils.userService = userService;
	}
	
	@Autowired
    public void setCustomerService(CustomerService customerService) {
		//hack for autowiring customerservice
		SessionUtils.customerService = customerService;
	}
}