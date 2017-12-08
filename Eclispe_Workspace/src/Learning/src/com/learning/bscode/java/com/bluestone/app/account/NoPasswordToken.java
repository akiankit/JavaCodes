package com.bluestone.app.account;

import org.apache.shiro.authc.AuthenticationToken;

public class NoPasswordToken implements AuthenticationToken{

	private static final long serialVersionUID = 1L;
	
	private String userName;
	
	public NoPasswordToken(String userName) {
		this.userName = userName;
	}

	@Override
	public String getPrincipal() {
		return getUserName();
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
