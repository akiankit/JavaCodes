package com.bluestone.app.shipping.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.shipping.dao.HolidayDao;
import com.bluestone.app.shipping.model.Holiday;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class HolidayService {

    private static final Logger log = LoggerFactory.getLogger(HolidayService.class);

    @Autowired
    private HolidayDao holidayDao;

    Integer noOfBizDays;

    @PostConstruct
    void init() {
        String noOfBusinessDays = ApplicationProperties.getProperty("noOfBusinessDays");
        noOfBizDays = Integer.valueOf(noOfBusinessDays);
    }

    public Integer getStandardBizDaysCount() {
        return noOfBizDays;
    }

    public Date getStandardDeliveryDate() {
        return getDeliveryDate(getStandardBizDaysCount());
    }

    /**
     * If seen after 2 PM, next day is considered as starting day.
     */
    public Date getDeliveryDate(int noOfBusinessDays) {
        Calendar calendar = Calendar.getInstance();
        Date startDate = calendar.getTime();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour > 14) {
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }
        return getDeliveryDate(startDate, noOfBusinessDays);
    }

    /**
     * If starting day is sunday, next monday is considered
     * as the starting day and no. of businessDays is added to the result to get
     * delivery date. Bluestone holidays and weekends are excluded for
     * calculating delivery date.
     */
    public Date getDeliveryDate(Date startDate, int noOfBusinessDays) {

        List<Date> holidays = holidayDao.getHolidayList();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        if (currentDay == 1) {// Sunday - add 1 days
            cal.add(Calendar.DATE, 1);
        }
        Date actualStartDate = cal.getTime();

        while (noOfBusinessDays > 0) {
            if (cal.get(Calendar.DAY_OF_WEEK) == 1) {//sunday
                cal.add(Calendar.DATE, 2);
            } else {
                cal.add(Calendar.DATE, 1);
            }
            noOfBusinessDays--;
        }

        // If due date lands on sunday
        cal = this.compensateForSunday(cal);
        Date dueDate = cal.getTime();

        // Compensate for holidays
        for (Date holiday : holidays) {
            if ((!DateTimeUtil.isWeekend(holiday)) && DateTimeUtil.fallsOnOrBetween(holiday, actualStartDate, dueDate)) {
                cal.add(Calendar.DATE, 1);
                // If due date falls on sunday, compensate for it
                cal = this.compensateForSunday(cal);
                dueDate = cal.getTime();
            }
        }
        return dueDate;
    }

    public int getWorkingDays(Date startDate, Date endDate) {
        List<Date> holidays = holidayDao.getHolidayList();
        Calendar calStartDate = Calendar.getInstance();
        calStartDate.setTime(startDate);
        Calendar calEndDate = Calendar.getInstance();
        calEndDate.setTime(endDate);

        // making time part of date same for both date because for the same date
        // if time was different no of working days was coming different
        calStartDate.set(Calendar.HOUR, calEndDate.get(Calendar.HOUR));
        calStartDate.set(Calendar.MINUTE, calEndDate.get(Calendar.MINUTE));
        calStartDate.set(Calendar.SECOND, calEndDate.get(Calendar.SECOND));
        int workingDays = DateTimeUtil.isWeekend(endDate) ? 0 : 1;
        int startDay = calStartDate.get(Calendar.DAY_OF_WEEK);
        int endDay = calEndDate.get(Calendar.DAY_OF_WEEK);
        while (startDay != endDay) {
            if (startDay != 7 && startDay != 1) {
                workingDays++;
            }
            calStartDate.add(Calendar.DATE, 1);
            startDay = calStartDate.get(Calendar.DAY_OF_WEEK);
        }

        if (startDay == 7) {// Saturday - add 2 days
            calStartDate.add(Calendar.DATE, 2);
        } else if (startDay == 1) {// Sunday - add 1 days
            calStartDate.add(Calendar.DATE, 1);
        }

        if (endDay == 7) {// Saturday - subtract 1 day
            calEndDate.add(Calendar.DATE, 1);
        } else if (endDay == 1) {// Sunday - add 1 days
            calEndDate.add(Calendar.DATE, 1);
        }

        if (!calStartDate.after(calEndDate)) {
            for (Date holiday : holidays) {
                if (!holiday.after(calEndDate.getTime()) && !holiday.before(calStartDate.getTime())) {
                    if ((!DateTimeUtil.isWeekend(holiday))
                        && DateTimeUtil.fallsOnOrBetween(holiday, calStartDate.getTime(), calEndDate.getTime())) {
                        calEndDate.add(Calendar.DATE, -1);
                        // If due date falls on weekend, compensate for it
                    }
                }
            }
        }
        long diff = Math.abs(calEndDate.getTime().getTime() - calStartDate.getTime().getTime());
        int totalDays = (int) ((diff / 1000) / 60 / 60 / 24);
        int weekendDays = (totalDays / 7) * 2;

        return totalDays - weekendDays + workingDays;
    }

    public String formatDate(Date dueDate) {
        String deliveryDateFormat = ApplicationProperties.getProperty("deliverydate_format");
        if (StringUtils.isBlank(deliveryDateFormat)) {
            deliveryDateFormat = "EEEE, MMM d, yyyy";
        }
        DateFormat formatter = new SimpleDateFormat(deliveryDateFormat);
        return formatter.format(dueDate);
    }

    private Calendar compensateForSunday(Calendar cal) {
        int currentDay = cal.get(Calendar.DAY_OF_WEEK);
        if (currentDay == 1) {
            cal.add(Calendar.DATE, 1);
        }
        return cal;
    }

    public String getFormattedDeliveryDate(int noOfBusinessDays) {
        Date dueDate = getDeliveryDate(noOfBusinessDays);
        return formatDate(dueDate);
    }

    public String getFormattedStandardDeliveryDate() {
        Date dueDate = getStandardDeliveryDate();
        return formatDate(dueDate);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteHolidayList() {
        holidayDao.deleteHolidayList();
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void insertHolidayList(String[] contentLines) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(contentLines != null && contentLines.length > 0){
        	for(int i=0;i<contentLines.length;i++){
        		if(i==0){
        			//Assuming first row belongs to header.
        			continue;
        		}
        		String line = contentLines[i];
        		if (!StringUtils.isBlank(line)) {
                    Holiday holiday = new Holiday();
                    Date date = dateFormat.parse(line);
                    holiday.setHoliday(date);
                    holidayDao.create(holiday);
                }
        	}
        }
    }
}
