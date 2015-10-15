package com.bluestone.app.account;

public class UnauthorizedAccessException extends Exception {

	private static final long serialVersionUID = -7968827024448182863L;

	public UnauthorizedAccessException(String message) {
		super(message);
	}
	
	public UnauthorizedAccessException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public UnauthorizedAccessException(Throwable cause) {
		super(cause);
	}

}
