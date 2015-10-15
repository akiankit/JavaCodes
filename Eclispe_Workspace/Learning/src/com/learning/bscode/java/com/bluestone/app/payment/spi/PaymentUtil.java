package com.bluestone.app.payment.spi;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.model.PaymentTransaction;

@Component
public class PaymentUtil {

    public enum BLUESTONE_FIELDS {
        ENCRYPTED_PAYMENT_TXN_ID
    }

    private static final Logger log = LoggerFactory.getLogger(PaymentUtil.class);

    private static PaymentTransactionService paymentTransactionService;

    @PostConstruct
    private void init() {
        log.debug("PaymentUtil.init()");
        Assert.notNull(paymentTransactionService, "paymentTransactionService could not be injected successfully");
    }



    @Required
    @Autowired
    public void setPaymentService(PaymentTransactionService paymentTransactionService) {
        PaymentUtil.paymentTransactionService = paymentTransactionService;
    }

    public static PaymentTransaction getPaymentTransaction(String encryptedTxnId) {
        return paymentTransactionService.getPaymentTransactionFromEncryptedTxnId(encryptedTxnId);
    }

}
