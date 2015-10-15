package com.bluestone.app.payment.spi.service;

import javax.servlet.http.HttpServletRequest;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.CaptureTransaction;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentRequest;

public interface PaymentGatewayService {
    
    public PaymentGateway getPaymentGateway();
    
    public PaymentRequest createPaymentRequest(PaymentTransaction paymentTransaction, Cart clonedCart, Cart originalCart) throws PaymentException;
    
    public PaymentResponse handlePaymentResponse(HttpServletRequest httpServletRequest) throws PaymentException;
    
    public CaptureTransaction capture(PaymentTransaction paymentTransaction) throws PaymentException;
    
    public void cancel(PaymentTransaction paymentTransaction) throws PaymentException;
    
    public void refund(PaymentTransaction paymentTransaction) throws PaymentException;

   

}
