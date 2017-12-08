package com.bluestone.app.payment.spi.exception;

import com.bluestone.app.payment.spi.model.PaymentError;

public class PaymentException extends Exception {

    private static final long serialVersionUID = -5250910367844579769L;

    private final PaymentError paymentError;

    private String encryptedTransactionId = "Not yet initialised";

    private String paymentResponse = null;

    private String detailedReason = null;

    public PaymentException(PaymentError paymentError, String encryptedTransactionId) {
        super(paymentError.toString() + " , TransactionId=" + encryptedTransactionId);
        this.paymentError = paymentError;
        this.encryptedTransactionId = encryptedTransactionId;
    }

    public PaymentException(PaymentError paymentError, Throwable throwable, String encryptedTransactionId) {
        super(paymentError.toString() + " , TransactionId=" + encryptedTransactionId, throwable);
        this.paymentError = paymentError;
        this.encryptedTransactionId = encryptedTransactionId;
    }

    public PaymentException(PaymentError paymentError) {
        super(paymentError.toString());
        this.paymentError = paymentError;
    }

    public PaymentError getPaymentError() {
        return paymentError;
    }

    public String getEncryptedTransactionId() {
        return encryptedTransactionId;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    public void setDetailedReason(String detailedReason) {
        this.detailedReason = detailedReason;
    }

    @Override
    public String toString() {
        if (detailedReason == null) {
            return super.toString();
        } else {
            return super.toString() + " : " + detailedReason;
        }
    }
}
