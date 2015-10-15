package com.bluestone.app.reporting.generic;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;

@Repository
public class GenericReportDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(GenericReportDao.class);

    public List<Object[]> getRecords(String query,int batchSize,int start) {
        Query listQuery = getEntityManagerForActiveEntities().createNativeQuery(query);
        listQuery.setFirstResult(start);
        listQuery.setMaxResults(batchSize);
        return  listQuery.getResultList();
    }

    public int getCountOfRecords(String query) {
        Query countQuery = getEntityManagerForActiveEntities().createNativeQuery(query);
        return ((BigInteger) countQuery.getSingleResult()).intValue();
    }
    
}
