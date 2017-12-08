package com.bluestone.app.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.payment.dao.PaymentDao;
import com.bluestone.app.payment.spi.model.MasterPayment;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class MasterPaymentService {
    
    private static final Logger log = LoggerFactory.getLogger(MasterPaymentService.class);
    
    @Autowired
    private PaymentDao paymentDao;
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, readOnly = false)
    public MasterPayment createOrGetMasterPayment(Long masterPaymentId, final Cart cart) {
        log.debug("MasterPaymentService.createOrGetMasterPayment() for masterPaymentId={} , cartId={}", masterPaymentId, cart.getId());
        MasterPayment masterPayment = null;
        if (masterPaymentId == null) {
            masterPayment = new MasterPayment();
            masterPayment.setCart(cart);
            paymentDao.create(masterPayment);
        } else {
            masterPayment = getMasterPayment(masterPaymentId);
        }
        return masterPayment;
    }
    
    public MasterPayment getMasterPayment(long id) {
        return paymentDao.find(MasterPayment.class, id, true);
    }
    
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor=Exception.class, readOnly = false)
    public void updateMasterPayment(MasterPayment masterPayment) {
        paymentDao.update(masterPayment);
    }


}
