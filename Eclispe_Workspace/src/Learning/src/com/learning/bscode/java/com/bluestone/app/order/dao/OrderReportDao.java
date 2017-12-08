package com.bluestone.app.order.dao;

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
import com.bluestone.app.reporting.order.OrderReport;

@Repository
public class OrderReportDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(OrderReportDao.class);
    
    private static final String ORDER_REPORT_SELECT       = " SELECT  orders.id," 
                                                            + " orders.expectedDeliveryDate,"
                                                            + " orders.code,"
                                                            + " orders.createdAt,"
                                                            + " u.user_name, "
                                                            + " orders.totalOrderAmount,"
                                                            + " orderstatus.statusName,"
                                                            + " pt.paymentGateway, "
                                                            + " pi.instrumentType,"
                                                            + " pt.bluestoneTxnId, " 
                                                            + " dv.name, "
                                                            + " orders.discountPrice";
            
    private static final String ORDER_REPORT_FROM  = " FROM orders orders ";
    
    private static final String ORDER_REPORT_WHERE = "  inner join masterpayment mp on orders.master_payment_id=mp.id " +
                                                      " inner join " +
                                                          " (select sp.master_payment_id as mpid," +
                                                              " sp.payment_transaction_id as ptid " +
                                                              " from subpayment sp" +
                                                              " inner join paymenttransaction pt on sp.payment_transaction_id=pt.id " +
                                                              " group by sp.master_payment_id order by sp.id desc )" +
                                                          " paymentData on paymentData.mpid = mp.id " +
                                                      " inner join paymenttransaction pt on paymentData.ptid=pt.id " +     
                                                      " inner join paymentinstrument pi on pt.paymentInstrument_id=pi.id" +
                                                      " inner join user u on orders.customer_id=u.id " + 
                                                      " inner join orderstatus orderstatus on orders.latestOrderStatus_id=orderstatus.id " +
                                                      " left join discountvoucher dv on orders.discount_voucher_id=dv.id " +
                                                      " where orders.createdAt BETWEEN :fromDate AND :toDate ";
    
    
    public List<OrderReport> getOrders(int start, ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
        String column = filterCriteria.getColumn();
        String sortBy = filterCriteria.getSortBy();
        String orderListQueryString = buildOrderListQuery(filterCriteria, column, sortBy);
        Query orderListQuery = getEntityManagerForActiveEntities().createNativeQuery(orderListQueryString);
        orderListQuery.setParameter("fromDate", fromDate);
        orderListQuery.setParameter("toDate", toDate);
        orderListQuery.setFirstResult(start);
        orderListQuery.setMaxResults(filterCriteria.getItemsPerPage());
        
        List<OrderReport> reportList = new ArrayList<OrderReport>();
        
        List<Object[]> resultList = orderListQuery.getResultList();
        for (Object[] eachRecord : resultList) {
            OrderReport report = new OrderReport(eachRecord[0].toString(),// id
                                                ((Date)eachRecord[1]),// expectedDeliveryDate
                                                eachRecord[2].toString(),// code
                                                ((Date)eachRecord[3]), // orderDate
                                                eachRecord[4].toString(),// customerName
                                                eachRecord[5] == null ? "0" : eachRecord[5].toString(),// orderTotal
                                                eachRecord[6].toString(),// orderStatus
                                                eachRecord[7] == null ? "" : eachRecord[7].toString(),// paymentGateway
                                                eachRecord[8] == null ? "" : eachRecord[8].toString(),// paymentInstrument
                                                eachRecord[9] == null ? "" : eachRecord[9].toString(),// bluestoneTxnId
                                                eachRecord[10] == null ? "" : eachRecord[10].toString(),// discountVoucherName can be null
                                                eachRecord[11].toString());// discountPrice);
            
            reportList.add(report);
        }

        return reportList;
    }

                                        

    private String buildOrderListQuery(ListFilterCriteria filterCriteria, String column, String sortBy) {
        StringBuilder orderListQueryString = new StringBuilder();
        orderListQueryString.append(ORDER_REPORT_SELECT);
        orderListQueryString.append(ORDER_REPORT_FROM);
        buildWhereClause(filterCriteria, orderListQueryString);
        orderListQueryString.append("order by ").append(sortBy);
        orderListQueryString.append(" ").append(filterCriteria.getSortOrder());
        return orderListQueryString.toString();
    }

    private void buildWhereClause(ListFilterCriteria filterCriteria, StringBuilder queryBuilder) {
        queryBuilder.append(ORDER_REPORT_WHERE);
        boolean prefixSearch = filterCriteria.getPrefixSearch(); 
        String valueToSearch = filterCriteria.getValueToSearch(); 
        String likeQuery = "";
        if(prefixSearch == true) { 
           likeQuery = " like '" + valueToSearch + "%' "; 
        } else { 
           likeQuery = " like '%" + valueToSearch + "%' ";
        }
        if(StringUtils.isNotBlank(filterCriteria.getColumn())) {
            queryBuilder.append(" and ").append(filterCriteria.getColumn()).append(likeQuery);
        }
    }
    
    public int getCountOfRecords(ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
        StringBuilder orderCountQueryString = new StringBuilder();
        orderCountQueryString.append("select count(*) ");
        orderCountQueryString.append(ORDER_REPORT_FROM);
        buildWhereClause(filterCriteria, orderCountQueryString);
        String countQuery = orderCountQueryString.toString();
        Query orderCountQuery = getEntityManagerForActiveEntities().createNativeQuery(countQuery);
        orderCountQuery.setParameter("fromDate", fromDate);
        orderCountQuery.setParameter("toDate", toDate);
        
        int intValue = ((BigInteger) orderCountQuery.getSingleResult()).intValue();
        log.info("Order Report Count Query {} \n total count {}", countQuery, intValue);
        return intValue;
    }

}
