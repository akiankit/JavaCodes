package com.bluestone.app.payment.spi.model;

public enum PaymentError {

    COMMUNICATION_ERROR_CODE(-1000, ""),

    PREAUTHFAILED(-1001, "Could not initiate payment request"),

    TRANSACTION_NOT_FOUND(-1002, "Not able to find corresponding transaction"),

    TRANSACTION_STATE_MISMATCHED(-1003, "Illegal Payment status"),

    MALFORMED_RESPONSE(-1004, "Malformed Payment Response"),

    MALFORMED_RESPONSE_HASH(-1005, "Hash mismatch in response from Gateway"),

    BAD_TRANSACTION_ID(-1006, "Bad Transaction Id Received"),

    REDIRECT_ERROR(-1007, "Unable to redirect Payment Response to Checkout Flow"),

    INTERNAL_ERROR(-1008, "Internal Error"),

    PREAUTH_REQUEST_GENERATION_FAILED(-1009, "Failure while creating payment request"),

    CAPTURE_ERROR(-1010, "Capture Failure"),

    TRANSACTION_ERROR(-1011, "Transaction Failure"),

    AMEX_AVS_ERROR(-1012, "AMEX Address Verification Failure"),

    AMEX_3DS_ERROR(-1013, "AMEX 3DS Failure"),

    AMEX_INTERNAL_ERROR(-1014, "Amex Internal Error"),
    
    EMPTY_ORDER_ID(-1015, "Order Id is Empty"),
    
    ORDER_NOT_FOUND(-1016, "Not able to find corresponding order"),
    
    ATOM_MALFORMED_FIRST_RESPONSE(-1017, "Atom Malformed First Response");

    private final int errorCode;
    private final String errorMsg;

    private PaymentError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }


    @Override
    public String toString() {
        return getErrorMsg();
    }

    private String getMessage() {
        return "Error code=" + getErrorCode()
               + " , Error Message=" + getErrorMsg();
    }
}
