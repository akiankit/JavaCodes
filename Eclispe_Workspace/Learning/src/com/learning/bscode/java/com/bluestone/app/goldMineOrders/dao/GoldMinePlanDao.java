package com.bluestone.app.goldMineOrders.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan.GoldMinePlanStatus;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment.GoldMinePlanPaymentStatus;

@Repository("goldMinePlanDao")
public class GoldMinePlanDao extends BaseDao{
	
	private static final Logger log = LoggerFactory.getLogger(GoldMinePlanDao.class);
	
	private final String AMOUNT_COLUMN = "amount";
	
	public List<GoldMinePlan> getAllGoldMinesForCustomer(Customer loggedInCustomer) {
    	log.debug("GoldMinePlanDao.getAllGoldMinesForCustomer() loggedInCustomer {}", loggedInCustomer.getEmail());
		Query getAllOrdersForCustomerQuery = getEntityManagerWithoutFilter().createQuery(
																			"select gm from GoldMinePlan gm " +
																			"where gm.customer=:customerId " +
																			"order by gm.createdAt DESC");
        getAllOrdersForCustomerQuery.setParameter("customerId", loggedInCustomer);
        return getAllOrdersForCustomerQuery.getResultList();
    }
	
	public Map<Object, Object> getGoldMinePlanListAndTotalCount(int start, ListFilterCriteria filterCriteria,Date fromDate, Date toDate,boolean export) {

		EntityManager entityManager = getEntityManagerWithoutFilter();

		String columnToSearch = filterCriteria.getColumn();
		String valueToSearch = filterCriteria.getValueToSearch();
		Query goldMinePlanCountQuery = null;
		Query goldMinePlanListQuery = null;
		String defaultQueryString = "from GoldMinePlan c where c.createdAt BETWEEN :fromDate AND :toDate and c.";
		if (columnToSearch.equalsIgnoreCase(AMOUNT_COLUMN) && StringUtils.isNotBlank(valueToSearch)) {
			goldMinePlanCountQuery = entityManager.createQuery("select count(c.id) " + defaultQueryString
																+ columnToSearch + " ='" + valueToSearch + "'");

			goldMinePlanListQuery = entityManager.createQuery("select c " + defaultQueryString + columnToSearch
																+ " = '" + valueToSearch + "' order by " + filterCriteria.getSortBy() + " "
																+ filterCriteria.getSortOrder());
		} else {
			goldMinePlanCountQuery = entityManager.createQuery("select count(c.id) "+defaultQueryString
																+ columnToSearch + " like '%" + valueToSearch + "%'");

			goldMinePlanListQuery = entityManager.createQuery("select c " + defaultQueryString + columnToSearch
																+ " like '%" + valueToSearch + "%' order by " + filterCriteria.getSortBy() + " "
																+ filterCriteria.getSortOrder());
		}

		goldMinePlanListQuery.setParameter("fromDate", fromDate);
		goldMinePlanListQuery.setParameter("toDate", toDate);
		
		if(export==false){
			goldMinePlanListQuery.setFirstResult(start);
			goldMinePlanListQuery.setMaxResults(filterCriteria.getItemsPerPage());
		}

		List<GoldMinePlan> goldMinePlanList = goldMinePlanListQuery.getResultList();
		Map<Object, Object> goldMinePlanDetails = new HashMap<Object, Object>();

		goldMinePlanDetails.put("list", goldMinePlanList);
		
		goldMinePlanCountQuery.setParameter("fromDate", fromDate);
		goldMinePlanCountQuery.setParameter("toDate", toDate);
		
		goldMinePlanDetails.put("totalCount", ((Long) goldMinePlanCountQuery.getSingleResult()).intValue());
		return goldMinePlanDetails;
	}

	public Map<Object, Object> overDuePaymentsListAndTotalCount(int start, ListFilterCriteria filterCriteria,Date fromDate, Date toDate,boolean export) {
		EntityManager entityManager = getEntityManagerWithoutFilter();

		String columnToSearch = filterCriteria.getColumn();
		String valueToSearch = filterCriteria.getValueToSearch();
		Query overDuePaymentsCountQuery = null;
		Query overDuePaymentsListQuery = null;
		String defaultQueryString = "from GoldMinePlanPayment c where c.dueDate BETWEEN :fromDate AND :toDate and " +
									"c.goldMinePlanPaymentStatus = :paymentStatus and c.goldMinePlan.goldMinePlanStatus = :goldMinePlanStatus and c.";
		overDuePaymentsCountQuery = entityManager.createQuery("select count(c.id) "+defaultQueryString
															+ columnToSearch + " like '%" + valueToSearch + "%'");

		overDuePaymentsListQuery = entityManager.createQuery("select c " + defaultQueryString + columnToSearch
															+ " like '%" + valueToSearch + "%' order by " + filterCriteria.getSortBy() + " "
															+ filterCriteria.getSortOrder());
	
		overDuePaymentsListQuery.setParameter("fromDate", fromDate);
		overDuePaymentsListQuery.setParameter("toDate", toDate);
		if(export == false){
			overDuePaymentsListQuery.setFirstResult(start);
			overDuePaymentsListQuery.setMaxResults(filterCriteria.getItemsPerPage());
		}
		overDuePaymentsListQuery.setParameter("paymentStatus", GoldMinePlanPaymentStatus.OVERDUE);
		overDuePaymentsListQuery.setParameter("goldMinePlanStatus", GoldMinePlanStatus.RUNNING);
		

		List<GoldMinePlanPayment> overDuePaymentsList = overDuePaymentsListQuery.getResultList();
		Map<Object, Object> overDuePaymentsDetails = new HashMap<Object, Object>();

		overDuePaymentsDetails.put("list", overDuePaymentsList);
		
		overDuePaymentsCountQuery.setParameter("fromDate", fromDate);
		overDuePaymentsCountQuery.setParameter("toDate", toDate);
		overDuePaymentsCountQuery.setParameter("paymentStatus", GoldMinePlanPaymentStatus.OVERDUE);
		overDuePaymentsCountQuery.setParameter("goldMinePlanStatus", GoldMinePlanStatus.RUNNING);
		overDuePaymentsDetails.put("totalCount", ((Long) overDuePaymentsCountQuery.getSingleResult()).intValue());
		return overDuePaymentsDetails;
	}
	
	public GoldMinePlan getGoldMinePlanByCode(String planCode){
		log.debug("GoldMinePlanDao.getGoldMinePlanByCode() for planCode {}", planCode);
		Query query = getEntityManagerWithoutFilter().createQuery("select gm from GoldMinePlan gm where gm.code =:planCode");
		query.setParameter("planCode", planCode);
		try{
			GoldMinePlan goldMinePlan = (GoldMinePlan) query.getSingleResult();
			return goldMinePlan;
		}catch(Exception e){
			log.error("No plan found for planCode: {}",planCode);
			return null;
		}
	}
	
}
