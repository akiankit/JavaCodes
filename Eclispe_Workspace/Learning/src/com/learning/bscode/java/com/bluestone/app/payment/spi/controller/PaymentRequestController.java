package com.bluestone.app.payment.spi.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.payment.service.PaymentFormBuilder;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;

@Controller
@RequestMapping(value = "/paymentrequest*")
public class PaymentRequestController {

    @Autowired
    private PaymentTransactionService paymentTransactionService;

    @Autowired
    private PaymentFormBuilder paymentFormBuilder;
    
    @Autowired
    private CartService cartService;

    private static final Logger log = LoggerFactory.getLogger(PaymentRequestController.class);

    @RequestMapping(value = "/{encryptedTxnId}/{cartId}")
    public @ResponseBody String paymentRequest(@PathVariable("encryptedTxnId") String encryptedTxnId,
                                               @PathVariable("cartId") long cartId,
                                               HttpServletRequest request,
                                               HttpServletResponse response) throws PaymentException {
        log.debug("PaymentRequestController.paymentRequest() for encryptedTxnId={}", encryptedTxnId);
        try {
            String decryptTxnId = CryptographyUtil.decrypt(encryptedTxnId);
            long txnId = Long.parseLong(decryptTxnId);
            PaymentTransaction paymentTransaction = paymentTransactionService.getPaymentTransaction(txnId);
            if (paymentTransaction == null) {
                throw new PaymentException(PaymentError.BAD_TRANSACTION_ID, encryptedTxnId);
            }
            
            Cart originalCart = cartService.getCart(cartId);
            
            String paymentForm = paymentFormBuilder.getPaymentForm(paymentTransaction, originalCart);

            paymentTransaction.setPaymentTransactionStatus(PaymentTransactionStatus.PAYMENT_REQUEST_STARTED);

            paymentTransaction = paymentTransactionService.updatePaymentTransaction(paymentTransaction);

            log.debug("PaymentRequestController.paymentRequest() for encryptedTxnId={} started. PaymentTransactionStatus set to {}",
                      encryptedTxnId, PaymentTransactionStatus.PAYMENT_REQUEST_STARTED.name());
            return paymentForm;
        } catch (PaymentException paymentException) {
            log.error("Error occurred while creating paymentRequest form for encryptedTxnId={}", encryptedTxnId, paymentException.toString(), paymentException);
            throw paymentException;
        } catch (Exception exception) {
            log.error("Error occurred while creating paymentRequest form for encryptedTxnId={}", encryptedTxnId, exception.toString(), exception);
            throw new PaymentException(PaymentError.INTERNAL_ERROR, exception, encryptedTxnId);
        }
    }
}
