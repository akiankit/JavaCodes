package com.bluestone.app.reporting.order;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.order.dao.OrderReportDao;
import com.bluestone.app.reporting.AbstractReportService;

@Service
@Transactional(readOnly=true)
public class OrderReportService extends AbstractReportService<OrderReport> {
    
    private static final Logger log = LoggerFactory.getLogger(OrderReportService.class);
    
    @Autowired
    private OrderReportDao orderReportDao;
    
    @Override
    public int getCountOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate) {
        return orderReportDao.getCountOfRecords(listFilterCriteria, fromDate, toDate);
    }
    
    @Override
    public List<OrderReport> getListOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate, int offset) {
    	log.debug("OrderReportService.getListOfRecords() listFilterCriteria {}, fromDate {}, toDate {}, offset {}",listFilterCriteria, fromDate, toDate, offset);
        return  orderReportDao.getOrders(offset, listFilterCriteria, fromDate, toDate);
    }
    
}
