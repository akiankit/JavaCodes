package com.bluestone.app.admin.service.payment;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.payment.dao.PaymentDao;

/**
 * @author Rahul Agrawal
 *         Date: 5/20/13
 */
@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class FailedTransactions {

    private static final Logger log = LoggerFactory.getLogger(FailedTransactions.class);

    @Autowired
    private PaymentDao paymentDao;

    private static final String BASE_URL_FOR_PAGINATION = "/admin/failuretransaction/history";

    public Map<Object, Object> getDetails(ListFilterCriteria filterCriteria, Date fromDate, Date toDate) {
        log.debug("FailedTransactions.getDetails(): Date Range = [{}] - [{}]", DateTimeUtil.formatDate(fromDate), DateTimeUtil.formatDate(toDate));

        int startingFrom = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
        Map<Object, Object> orderListAndTotalCount = paymentDao.getFailedTransactionsAndTotalCount(startingFrom, filterCriteria, fromDate, toDate);
        List<Order> ordersList = (List<Order>) orderListAndTotalCount.get("list");
        Map<Object, Object> dataForView = PaginationUtil.generateDataForView(orderListAndTotalCount, filterCriteria.getItemsPerPage(),
                                                                             filterCriteria.getP(), startingFrom, BASE_URL_FOR_PAGINATION, ordersList.size());
        Map<String, String> searchFieldMap = new HashMap<String, String>();
        searchFieldMap.put("Transaction Id", "bluestoneTxnId");
        searchFieldMap.put("Payment Gateway", "paymentGateway");
        searchFieldMap.put("Customer Name", "subPayment.masterPayment.cart.customer.userName");
        searchFieldMap.put("Customer Email", "subPayment.masterPayment.cart.customer.email");

        dataForView.put("searchFieldMap", searchFieldMap);

        return dataForView;
    }
}
