package com.bluestone.app.account;

import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;

public class NoPasswordAuthenticationInfo extends SimpleAuthenticationInfo{
	
	private static final long serialVersionUID = 1L;

	public NoPasswordAuthenticationInfo(Object principal, String realmName) {
        this.principals = new SimplePrincipalCollection(principal, realmName);
        this.credentials = "";
    }

}
