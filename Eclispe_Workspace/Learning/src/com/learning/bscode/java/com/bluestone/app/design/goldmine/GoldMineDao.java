package com.bluestone.app.design.goldmine;

import java.math.BigDecimal;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;

@Repository("goldMineDao")
public class GoldMineDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(GoldMineDao.class);
    
    public GoldMine getGoldMineProduct(BigDecimal amount) {
        Query createNamedQuery = getEntityManagerForActiveEntities().createQuery("Select gm from GoldMine gm where amount=:amount");
        createNamedQuery.setParameter("amount", amount);
        return (GoldMine) createNamedQuery.getSingleResult();
    }
}
