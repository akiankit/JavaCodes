package com.bluestone.app.account;

/**
 * @author Rahul Agrawal
 *         Date: 4/21/13
 */
public class AccountCreationException extends Exception {

    private static final long serialVersionUID = -5829399127755437938L;

    public static final String ACCOUNT_NOT_ACTIVATED_MESSAGE = "An account is already registered with this e-mail but still not activated. " +
                                                               "An account activation e-mail has been sent to your address.";

    public static final String ACCOUNT_ALREADY_EXISTS_ERROR_MESSAGE =
            "An account is already registered with this e-mail, please fill in the password or request a new one";

    public static final String ACTIVATION_EMAIL_SEND_FAILURE = "Failed to send the account activation email. Please try again.";

    public static final String ACCOUNT_CREATION_FAILED_MESSAGE = "An error occurred while creating your account.";


    private AccountCreationException(String message) {
        super(message);
    }

    private AccountCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static AccountCreationException accountNotActivated() {
        return new AccountCreationException(ACCOUNT_NOT_ACTIVATED_MESSAGE);
    }

    public static AccountCreationException accountAlreadyExists() {
        return new AccountCreationException(ACCOUNT_ALREADY_EXISTS_ERROR_MESSAGE);
    }

    public static AccountCreationException failedToSendAccountActivationEmail() {
        return new AccountCreationException(ACTIVATION_EMAIL_SEND_FAILURE);
    }
}
