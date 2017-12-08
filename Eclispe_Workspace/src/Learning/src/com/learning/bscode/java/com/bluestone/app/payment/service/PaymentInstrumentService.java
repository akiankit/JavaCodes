package com.bluestone.app.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.payment.dao.PaymentDao;
import com.bluestone.app.payment.spi.model.PaymentInstrument;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class PaymentInstrumentService {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentInstrumentService.class);
    
    @Autowired
    private PaymentDao paymentDao;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, readOnly = false)
    public PaymentInstrument createOrGetPaymentInstrument(String instrumentType, String issuingAuthority, String cardPaymentNetwork) {
        log.debug("PaymentInstrumentService.createOrGetPaymentInstrument(): for instrumentType={} , issuingAuthority={}, cardPaymentNetwork={}",
                  instrumentType, issuingAuthority, cardPaymentNetwork);
        PaymentInstrument paymentInstrument = paymentDao.getPaymentInstrument(instrumentType, issuingAuthority, cardPaymentNetwork);
        if(paymentInstrument == null) {
            paymentInstrument = new PaymentInstrument();
            paymentInstrument.setInstrumentType(instrumentType);
            paymentInstrument.setCardPaymentNetwork(cardPaymentNetwork);
            paymentInstrument.setIssuingAuthority(issuingAuthority);
            paymentDao.create(paymentInstrument);
        }
        return paymentInstrument;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, readOnly = false)
    public PaymentInstrument createOrGetPaymentInstrument(PaymentInstrument paymentInstrument) {
        PaymentInstrument paymentInstrumentFromDb = paymentDao.getPaymentInstrument(paymentInstrument.getInstrumentType(),
                                                                                    paymentInstrument.getIssuingAuthority(),
                                                                                    paymentInstrument.getCardPaymentNetwork());
        if(paymentInstrumentFromDb == null) {
            paymentDao.create(paymentInstrument);
            return paymentInstrument;
        } else {
            return paymentInstrumentFromDb;
        }
    }
    
}
