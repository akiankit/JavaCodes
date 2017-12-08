package com.bluestone.app.account;

import org.apache.shiro.authc.AuthenticationException;

public class AccountNotActivatedException extends AuthenticationException {

    private static final long serialVersionUID = -3067975210756173300L;

    private String userName;


    public AccountNotActivatedException(String userName, String message) {
        super(message);
        this.userName = userName;
    }

    public AccountNotActivatedException(String userName, String message, Throwable throwable) {
        super(message, throwable);
        this.userName = userName;
    }

    public AccountNotActivatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getUserName() {
        return userName;
    }
}
