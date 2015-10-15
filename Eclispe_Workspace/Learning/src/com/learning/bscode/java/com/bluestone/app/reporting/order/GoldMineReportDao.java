package com.bluestone.app.reporting.order;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.dao.BaseDao;

@Repository
public class GoldMineReportDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(GoldMineReportDao.class);
    
    private static final String GOLD_MINE_REPORT_SELECT       = " SELECT  gmp.id," 
                                                            + " gmp.code,"
                                                            + " gmp.enrolledDate,"
                                                            + " u.user_name, "
                                                            + " u.email,"
                                                            + " gmp.amount,"
                                                            + " gmp.schemeName,"
                                                            + " gmp.goldMinePlanStatus,"
                                                            + " gmp.maturityDate, "
                                                            + " temp.paidCount*gmp.amount AS paidAmount ";
            
    private static final String GOLD_MINE_REPORT_FROM  = " FROM goldmineplan gmp ";
    
	private static final String GOLD_MINE_REPORT_WHERE = " INNER JOIN (SELECT COUNT(*) AS paidCount,gold_mine_plan_id as id " +
														" FROM goldmineplanpayment " +
														" WHERE goldmineplanpayment.`goldMinePlanPaymentStatus` = 'paid'" +
														" GROUP BY gold_mine_plan_id) temp ON temp.id = gmp.id" +
														" inner join user u on gmp.customer_id=u.id"+ 
														" where gmp.createdAt BETWEEN :fromDate AND :toDate ";
    
    
    public List<GoldMineReport> getList(int start, ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
        String column = filterCriteria.getColumn();
        String sortBy = filterCriteria.getSortBy();
        String listQueryString = buildListQuery(filterCriteria, column, sortBy);
        Query listQuery = getEntityManagerForActiveEntities().createNativeQuery(listQueryString);
        listQuery.setParameter("fromDate", fromDate);
        listQuery.setParameter("toDate", toDate);
        listQuery.setFirstResult(start);
        listQuery.setMaxResults(filterCriteria.getItemsPerPage());
        
        List<GoldMineReport> reportList = new ArrayList<GoldMineReport>();
        
        List<Object[]> resultList = listQuery.getResultList();
        for (Object[] eachRecord : resultList) {
            GoldMineReport report = new GoldMineReport(eachRecord[0].toString(),// id
                                                eachRecord[1].toString(),// code
                                                eachRecord[3].toString(),// customerName
                                                eachRecord[4].toString(),// customerEmail
                                                eachRecord[5].toString(),// installmentAmount
                                                eachRecord[6].toString(),// scheme name
                                                eachRecord[7].toString(),// staus
                                                ((Date)eachRecord[2]), // enrollement date
                                                ((Date)eachRecord[8]),
                                                eachRecord[9].toString()); // Maturity date
            
            reportList.add(report);
        }

        return reportList;
    }

                                        

    private String buildListQuery(ListFilterCriteria filterCriteria, String column, String sortBy) {
        StringBuilder listQueryString = new StringBuilder();
        listQueryString.append(GOLD_MINE_REPORT_SELECT);
        listQueryString.append(GOLD_MINE_REPORT_FROM);
        buildWhereClause(filterCriteria, listQueryString);
        listQueryString.append("order by ").append(sortBy);
        listQueryString.append(" ").append(filterCriteria.getSortOrder());
        return listQueryString.toString();
    }

    private void buildWhereClause(ListFilterCriteria filterCriteria, StringBuilder queryBuilder) {
        queryBuilder.append(GOLD_MINE_REPORT_WHERE);
        boolean prefixSearch = filterCriteria.getPrefixSearch(); 
        String valueToSearch = filterCriteria.getValueToSearch(); 
        String likeQuery = "";
        if(prefixSearch == true) { 
           likeQuery = " like '" + valueToSearch + "%' "; 
        } else { 
           likeQuery = " like '%" + valueToSearch + "%' ";
        }
        if(StringUtils.isNotBlank(filterCriteria.getColumn())) {
            queryBuilder.append(" and ").append("gmp."+filterCriteria.getColumn()).append(likeQuery);
        }
    }
    
    public int getCountOfRecords(ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
        StringBuilder countQueryString = new StringBuilder();
        countQueryString.append("select count(*) ");
        countQueryString.append(GOLD_MINE_REPORT_FROM);
        buildWhereClause(filterCriteria, countQueryString);
        Query countQuery = getEntityManagerForActiveEntities().createNativeQuery(countQueryString.toString());
        countQuery.setParameter("fromDate", fromDate);
        countQuery.setParameter("toDate", toDate);
        
        int intValue = ((BigInteger) countQuery.getSingleResult()).intValue();
        log.info("Gold Mine Report Count Query {} \n total count {}", countQuery, intValue);
        return intValue;
    }

}
