package com.bluestone.app.payment.spi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.payment.gateways.amex.AmexPaymentService;
import com.bluestone.app.payment.gateways.atom.AtomPaymentService;
import com.bluestone.app.payment.gateways.innoviti.InnovitiPaymentService;
import com.bluestone.app.payment.gateways.payu.PayuPaymentService;
import com.bluestone.app.payment.spi.model.PaymentGateway;
//import com.bluestone.app.payment.gateways.amex.AmexPaymentService;

@Service
public class PaymentGatewayFinder {

    private static final Logger log = LoggerFactory.getLogger(PaymentGatewayFinder.class);

    @Autowired
    protected PayuPaymentService payuPaymentService;

    @Autowired
    protected InnovitiPaymentService innovitiPaymentService;
    
    @Autowired
    protected AmexPaymentService amexPaymentService;
    
    @Autowired
    protected AtomPaymentService atomPaymentService;    

    public PaymentGatewayService getPaymentGatewayService(PaymentGateway paymentGateway) {
        log.debug("PaymentGatewayFinder.getPaymentGatewayService() for {}", paymentGateway.name());
        switch (paymentGateway) {
            case PAYU:
                return payuPaymentService;
            case INNOVITI:
                return innovitiPaymentService;
            case AMEX:
            	return amexPaymentService;
            case ATOM:
            	return atomPaymentService;
            default:
                log.warn("No Payment Gateway service found for gateway={}", paymentGateway.name());
                throw new RuntimeException("No payment gateway service configured for " + paymentGateway.name());
        }
    }

    public PaymentGateway findPaymentGateway(String instrumentType, String cardPaymentNetwork, String issuingAuthority) {
        log.debug("PaymentGatewayFinder.findPaymentGateway() for instrumentType={}", instrumentType);
        PaymentGateway paymentGateway = null;

        if (Constants.CASHONDELIVERY.equalsIgnoreCase(instrumentType)) {
            paymentGateway = PaymentGateway.CASH_ON_DELIVERY;
        } else if(Constants.CREDITCARD.equalsIgnoreCase(instrumentType) && Constants.AMEXCARD.equalsIgnoreCase(cardPaymentNetwork)){
        	paymentGateway = PaymentGateway.AMEX;
        } else if (instrumentType.startsWith("emi")) {
        	if(issuingAuthority.equals("citi_bank") || issuingAuthority.equals("hdfc_bank")) {
                paymentGateway = PaymentGateway.PAYU;
            } else {
                paymentGateway = PaymentGateway.ATOM;
            }
        } else if (Constants.POSTDATEDCHQ.equalsIgnoreCase(instrumentType)) {
            paymentGateway = PaymentGateway.PDC;
        } else if (Constants.REPLACEMENT_ORDER.equalsIgnoreCase(instrumentType)) {
            paymentGateway = PaymentGateway.REPLACEMENT_ORDER;
        } else if (Constants.NET_BANKING_TRANSFER.equalsIgnoreCase(instrumentType)) {
            paymentGateway = PaymentGateway.NET_BANKING_TRANSFER;
        } else if (Constants.CHEQUE_PAID.equalsIgnoreCase(instrumentType)) {
            paymentGateway = PaymentGateway.CHEQUE_PAID;
        } else if (Constants.PAYMENT_ACCEPTED.equalsIgnoreCase(instrumentType)) {
            paymentGateway = PaymentGateway.PAYMENT_ACCEPTED;
        } else {
            paymentGateway = PaymentGateway.ATOM;
        }
        log.debug("PaymentGateway={} selected for InstrumentType={} , CardPaymentNetwork={} IssuingAuthority={}",
                  paymentGateway, instrumentType, cardPaymentNetwork, issuingAuthority);

        return paymentGateway;

    }
}
