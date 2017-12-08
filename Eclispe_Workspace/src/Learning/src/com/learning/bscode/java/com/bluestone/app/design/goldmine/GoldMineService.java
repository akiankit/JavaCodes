package com.bluestone.app.design.goldmine;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class GoldMineService {
    
    private static final Logger log = LoggerFactory.getLogger(GoldMineService.class);
    
    @Autowired
    private GoldMineDao goldMineDao;
    
    public GoldMine getGoldMineProduct(BigDecimal amount) {
        return goldMineDao.getGoldMineProduct(amount);
    }
    
    
}
