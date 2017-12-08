package com.bluestone.app.design.goldmine;

import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentError;

public class GoldMinePaymentException extends PaymentException {

    private static final long serialVersionUID = -5250910367844579769L;
    
    public GoldMinePaymentException(PaymentError paymentError, String encryptedTransactionId) {
    	super(paymentError,encryptedTransactionId);
    }

    public GoldMinePaymentException(PaymentError paymentError, Throwable throwable, String encryptedTransactionId) {
        super(paymentError,throwable,encryptedTransactionId);
    }

}
