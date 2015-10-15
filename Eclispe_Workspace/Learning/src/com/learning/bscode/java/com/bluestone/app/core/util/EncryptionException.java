package com.bluestone.app.core.util;

public class EncryptionException extends Exception {

    private static final long serialVersionUID = 5706084851799395096L;

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptionException(Throwable cause) {
        super(cause);
    }
}
