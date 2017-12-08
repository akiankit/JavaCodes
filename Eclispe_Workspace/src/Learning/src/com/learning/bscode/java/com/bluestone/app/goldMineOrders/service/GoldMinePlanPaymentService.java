package com.bluestone.app.goldMineOrders.service;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment.GoldMinePlanPaymentStatus;
import com.bluestone.app.payment.spi.model.MasterPayment;

@Service
@Transactional(readOnly=true)
public class GoldMinePlanPaymentService {

	private static final Logger log = LoggerFactory.getLogger(GoldMinePlanPaymentService.class);

	@Autowired
	private BaseDao baseDao;

	public GoldMinePlanPayment getGoldMinePlanPaymentById(long paymentId) {
		log.debug("GoldMinePlanPaymentsService.getGoldMinePlanPaymentById() paymentId {}", paymentId);
		return baseDao.findAny(GoldMinePlanPayment.class, paymentId);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void update(GoldMinePlanPayment goldMinePlanPayments) {
		log.debug("GoldMinePlanPaymentsService.update()");
		baseDao.update(goldMinePlanPayments);
	}
	
	
	@Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public GoldMinePlanPayment createGoldMinePayment(GoldMinePlan goldMinePlan, Cart cart,GoldMinePlanPaymentStatus goldMinePlanPaymentStatus, Calendar dueDate,
	                                                 MasterPayment masterPayment) {
        log.debug("GoldMinePlanPaymentsService.createGoldMinePayment() for plan {}  status{}", goldMinePlan, goldMinePlanPaymentStatus);

        GoldMinePlanPayment goldPlanPayment = new GoldMinePlanPayment();
        if(cart!=null){
        	goldPlanPayment.setCart(cart);
        	goldPlanPayment.setMasterPayment(masterPayment);
        }
        goldPlanPayment.setGoldMinePlanPaymentStatus(goldMinePlanPaymentStatus);
        goldPlanPayment.setDueDate(dueDate.getTime());
        goldPlanPayment.setGoldMinePlanPaymentStatus(goldMinePlanPaymentStatus);
        goldPlanPayment.setGoldMinePlan(goldMinePlan);
        
        baseDao.create(goldPlanPayment);
        goldPlanPayment.setCode(CryptographyUtil.encrypt(goldPlanPayment.getId() + "", Constants.ORDER_ID_PADDING));
        return baseDao.update(goldPlanPayment);        
    }

}
