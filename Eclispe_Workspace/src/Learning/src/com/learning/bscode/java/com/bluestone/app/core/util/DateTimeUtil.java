package com.bluestone.app.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.google.common.base.Throwables;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    private static final Logger log = LoggerFactory.getLogger(DateTimeUtil.class);

    public static boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        if (day == 1 || day == 7) {
            return true;
        }
        return false;
    }

    public static boolean fallsOnOrBetween(Date currentDate, Date startDate, Date endDate) {
        if (!startDate.equals(endDate)) {
            Assert.isTrue(startDate.before(endDate), "Start date should be before end date");
        }
        if (DateUtils.isSameDay(currentDate, startDate) || DateUtils.isSameDay(currentDate, endDate)) {
            return true;
        }
        if (currentDate.compareTo(startDate) * currentDate.compareTo(endDate) <= 0) {
            return true;
        }
        return false;
    }

    public static Date getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    /**
     * @return
     * yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeNow() {
        return getCurrentDateWithTime();
    }

    public static String formatDate(Date date, String desiredFormat) {
        if (date != null) {
            if (StringUtils.isBlank(desiredFormat)) {
                desiredFormat = "EEEE, MMM d, yyyy";
            }
            DateFormat formatter = new SimpleDateFormat(desiredFormat);
            return formatter.format(date);
        } else {
            return null;
        }
    }
    
    public static String formatCurrentDate(String desiredFormat) {
        if (StringUtils.isBlank(desiredFormat)) {
            desiredFormat = "EEEE, MMM d, yyyy";
        }
        DateFormat formatter = new SimpleDateFormat(desiredFormat);
        return formatter.format(getDate());
    }

    /**
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getCurrentDateWithTime() {
        final String pattern = "yyyy-MM-dd HH:mm:ss";
        return formatDate(getDate(), pattern);
    }

    /**
     * @param date
     * @return String Date in format dd-MMM-yyyy
     */
    public static String formatDate(Date date) {
        return formatDate(date, "dd-MMM-yyyy");
    }

    /**
     * @param date
     * @return String in the format yyyy-MM-dd HH:mm:ss
     */
    public static String getSimpleDateAndTime(Date date) {
        return formatDate(date, "yyyy-MMM-dd HH:mm:ss");
    }

    public static Integer getSEOYear() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 2);
        /*
         * Around November, people start searching for next year. Hence adding 2 months to the date.
         */
        return new Integer(cal.get(Calendar.YEAR));
    }
    /*public static Date getTodaysMidNight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }*/

    public static Date getTodaysStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        return calendar.getTime();
    }


    public static Date getDateFromString(String date,boolean isDateStartDate) {
        Calendar c= Calendar.getInstance();
        final String dateRegex = "([0-9]{2})/([0-9]{2})/([0-9]{4})";
        if(date!=null && !StringUtils.isBlank(date) && date.matches(dateRegex)){
            SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date finalDate = dateFormat.parse(date);
                if(isDateStartDate == false){
                    c.setTime(finalDate);
                    c.add(Calendar.DATE, 1);
                    finalDate = c.getTime();
                }
                return finalDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(isDateStartDate == false){
            return c.getTime();
        }else{
            c.set(2000, 1, 1);
            return c.getTime();
        }
    }
    
    public static Date getDateFromStringForTransactionList(String date) {
        Calendar c= Calendar.getInstance();
        if(!StringUtils.isBlank(date)){
            SimpleDateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date finalDate = dateFormat.parse(date);
                    c.setTime(finalDate);
                    c.add(Calendar.DATE, 1);
                    finalDate = c.getTime();
                return finalDate;
            } catch (ParseException e) {
                log.error("Error while parsing date {}, Error {}",date,e);
            }
       
        }else{
            c.add(Calendar.DATE, -90);
            return c.getTime();
        }
		return null;
    }
    
	/**
	 * @param dateStr
	 *            - Expected Format - MM/dd/yyyy, example - 11/24/1967
	 * @return java.util.Date
	 */
	public static Date parseDateString(String dateStr) {
		Date date = null;
		try {
			final String dateRegex = "([0-9]{2})/([0-9]{2})/([0-9]{4})";
			if (dateStr != null && !StringUtils.isBlank(dateStr) && dateStr.matches(dateRegex)) {
				date = new SimpleDateFormat("MM/dd/yyyy").parse(dateStr);
			}
		} catch (ParseException e) {
			log.error("Date parse exception for string={} : {}", dateStr, Throwables.getRootCause(e).toString());
		}
		return date;
	}
	
	public static long getNumberOfDays(Date date){
        Calendar calStartDate = Calendar.getInstance();
        calStartDate.setTime(date);
        Calendar calEndDate = Calendar.getInstance();
        calStartDate.set(Calendar.HOUR, calEndDate.get(Calendar.HOUR));
        calStartDate.set(Calendar.MINUTE, calEndDate.get(Calendar.MINUTE));
        calStartDate.set(Calendar.SECOND, calEndDate.get(Calendar.SECOND));
    	long currentTime = calEndDate.getTimeInMillis();
    	long time = calStartDate.getTimeInMillis();
    	long diff = currentTime-time;
    	return Math.abs((diff/(24*60*60*1000)));
    }
	
	public static Date getDateWithZeroTime(Date date){
		Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}
}
