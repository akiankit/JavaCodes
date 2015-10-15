package com.bluestone.app.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.SubPayment;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class SubPaymentService {
    
    private static final Logger log = LoggerFactory.getLogger(SubPaymentService.class);
    
    public SubPayment createSubPayment(MasterPayment masterPayment) {
        SubPayment subPayment = new SubPayment();
        subPayment.setMasterPayment(masterPayment);
        return subPayment;
    }
}


