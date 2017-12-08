package com.bluestone.app.reporting.order;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.reporting.AbstractReportService;

@Service
@Transactional(readOnly=true)
public class GoldMineReportService extends AbstractReportService<OrderReport> {
    
    private static final Logger log = LoggerFactory.getLogger(GoldMineReportService.class);
    
    @Autowired
    private GoldMineReportDao goldMineReportDao;
    
    @Override
    public int getCountOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate) {
    	log.debug("GoldMineReportService.getCountOfRecords() listFilterCriteria, fromDate, toDate", listFilterCriteria,fromDate, toDate);
        return goldMineReportDao.getCountOfRecords(listFilterCriteria, fromDate, toDate);
    }
    
    @Override
    public List<GoldMineReport> getListOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate, int offset) {
    	log.debug("GoldMineReportService.getListOfRecords() listFilterCriteria, fromDate, toDate, offset",listFilterCriteria, fromDate, toDate, offset);
        return goldMineReportDao.getList(offset, listFilterCriteria, fromDate, toDate);
    }
    
}
