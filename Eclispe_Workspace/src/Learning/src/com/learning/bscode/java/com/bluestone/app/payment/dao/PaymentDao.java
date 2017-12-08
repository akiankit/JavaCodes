package com.bluestone.app.payment.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.google.common.base.Throwables;

@Repository("paymentDao")
public class PaymentDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(PaymentDao.class);
    
    public PaymentInstrument getPaymentInstrument(String instrumentType, String issuingAuthority, String cardPaymentNetwork) {
        try {
            log.debug("PaymentDao.getPaymentInstrument(): instrumentType={} , issuingAuthority={} , cardPaymentNetwork={}",
                      instrumentType, issuingAuthority, cardPaymentNetwork);

            Query createNamedQuery = getEntityManagerForActiveEntities().createNamedQuery("PaymentInstrument.find");
            
            if (StringUtils.isBlank(instrumentType)) {
                instrumentType = Constants.NONE;
            }
            
            if (StringUtils.isBlank(issuingAuthority)) {
                issuingAuthority = Constants.NONE;
            }
            
            if (StringUtils.isBlank(cardPaymentNetwork)) {
                cardPaymentNetwork = Constants.NONE;
            }
            
            createNamedQuery.setParameter("instrumentType", instrumentType);
            createNamedQuery.setParameter("issuingAuthority", issuingAuthority);
            createNamedQuery.setParameter("cardPaymentNetwork", cardPaymentNetwork);
            PaymentInstrument paymentInstrument = (PaymentInstrument) createNamedQuery.getSingleResult();
            return paymentInstrument;
        } catch (NoResultException nre) {
            log.warn("PaymentDao.getPaymentInstrument() resulted in null for  instrumentType={} , issuingAuthority={} , cardPaymentNetwork={}",
                      new Object[]{instrumentType, issuingAuthority, cardPaymentNetwork});
            return null;
        }
    }
    
    public Map<Object, Object> getFailedTransactionsAndTotalCount(int start, ListFilterCriteria filterCriteria,Date fromDate, Date toDate) {
		String subQuery = "from PaymentTransaction o where o.createdAt BETWEEN :fromDate AND :toDate" 
											+ " and paymentTransactionStatus = 'FAILURE' and o.";
		String transactionListQueryString =  "select o "+subQuery + filterCriteria.getColumn() + " like '%" + filterCriteria.getValueToSearch() + "%' order by o." +
											filterCriteria.getSortBy() + " " + filterCriteria.getSortOrder();
		
		
		Query transactionListQuery = getEntityManagerForActiveEntities().createQuery(transactionListQueryString);
		transactionListQuery.setParameter("fromDate", fromDate);
		transactionListQuery.setParameter("toDate", toDate);
		transactionListQuery.setFirstResult(start);
		transactionListQuery.setMaxResults(filterCriteria.getItemsPerPage());
		
		List<PaymentTransaction> transactionList = transactionListQuery.getResultList();
		Map<Object, Object> transactionDetails = new HashMap<Object, Object>();

		transactionDetails.put("list", transactionList);
		transactionDetails.put("totalCount", getTotalCount(filterCriteria,fromDate,toDate));
		return transactionDetails;
	}

	private int getTotalCount(ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
		Query transactionCountQuery = getEntityManagerForActiveEntities().createQuery("select count(o.id) from PaymentTransaction o where " +
				"o.createdAt BETWEEN :fromDate AND :toDate " +
				"and paymentTransactionStatus = 'FAILURE' and o." + filterCriteria.getColumn() + " like '%" + 
				filterCriteria.getValueToSearch() + "%'");
		transactionCountQuery.setParameter("fromDate", fromDate);
		transactionCountQuery.setParameter("toDate", toDate);
		return ((Long)transactionCountQuery.getSingleResult()).intValue();
	}
	
	public PaymentTransaction getPaymentTransactionWithRowLock(long paymentTransactionId) {
		PaymentTransaction paymentTransaction = null;
		EntityManager forActiveEntities = getEntityManagerForActiveEntities();
		Query createQuery = forActiveEntities.createQuery("select pt from PaymentTransaction pt where pt.id=:id");
		createQuery.setParameter("id", paymentTransactionId);
		createQuery.setLockMode(LockModeType.PESSIMISTIC_WRITE);
		try{
			paymentTransaction = (PaymentTransaction) createQuery.getSingleResult();
		}catch(NoResultException nre){
			log.error("Exception while executing PaymentDao.getPaymentTransactionWithRowLock():No payment transaction found for id ={}",
					paymentTransactionId, Throwables.getRootCause(nre));
		}catch(NonUniqueResultException  nue){
			log.error("Exception while executing PaymentDao.getPaymentTransactionWithRowLock():Multiple payment transactions found for id ={}",
					paymentTransactionId, Throwables.getRootCause(nue));
		}
		return paymentTransaction;
	}
}
