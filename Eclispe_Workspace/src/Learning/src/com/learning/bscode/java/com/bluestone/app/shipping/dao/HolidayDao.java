package com.bluestone.app.shipping.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;

@Repository("holidayDao")
public class HolidayDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(HolidayDao.class);
    
    public List<Date> getHolidayList() {
        Query createNamedQuery = getEntityManagerForActiveEntities().createNamedQuery("Holiday.allHolidays");
        List<Date> holidayList = createNamedQuery.getResultList();
        return holidayList;
    }
    
    public void deleteHolidayList() {
        Query deleteHolidayListQuery = getEntityManagerWithoutFilter().createNamedQuery("Holiday.deleteAllHolidays");
        deleteHolidayListQuery.executeUpdate();
    }
    
}
