package com.bluestone.app.admin.service;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import com.unicommerce.uniware.services.CreateExportJobRequest;
import com.unicommerce.uniware.services.CreateExportJobRequestExportFiltersExportFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.uniware.JobStatusPoller;
import com.bluestone.app.uniware.WebService;

import static com.bluestone.app.uniware.util.SaleOrderExportJobColumn.getExportColumns;

/**
 * @author Rahul Agrawal
 *         Date: 11/29/12
 */
@Service
public class SaleOrderExportJobService {

    private static final Logger log = LoggerFactory.getLogger(SaleOrderExportJobService.class);

    @Autowired
    private WebService webService;

    private String fileUrl;

    private long waitTimeBeforePollingResult = 3 * 60 * 1000l;
    private int sinceDaysCount = 2;

    public enum Frequency {
        ONETIME, HOURLY, DAILY, WEEKLY
    }

    public String execute(Frequency frequency, long waitTimeMins, int daysCount) throws RemoteException {

        //For example, to convert 10 minutes to milliseconds, use: TimeUnit.MILLISECONDS.convert(10L, TimeUnit.MINUTES)

        if (waitTimeBeforePollingResult > 0) {
            waitTimeBeforePollingResult = getTimeInMilliSecs(waitTimeMins);
        }
        if (daysCount > 0) {
            sinceDaysCount = computeSinceDays(daysCount);
        }
        log.info("SaleOrderExportJobService.execute(): SaleOrder Export Job To be run {}. Job is about to start now ({}).", frequency.name(),
                 DateTimeUtil.getCurrentDateWithTime());
        CreateExportJobRequest exportJobRequest = new CreateExportJobRequest();
        exportJobRequest.setExportColumns(getExportColumns());
        exportJobRequest.setExportJobTypeName("Sale Orders");
        exportJobRequest.setFrequency(frequency.name());

        CreateExportJobRequestExportFiltersExportFilter[] exportFilters = new CreateExportJobRequestExportFiltersExportFilter[1];
        CreateExportJobRequestExportFiltersExportFilter exportFilter = new CreateExportJobRequestExportFiltersExportFilter();
        Calendar now = Calendar.getInstance();

        now.add(Calendar.DAY_OF_MONTH, sinceDaysCount);
        Date sinceWhen = now.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        exportFilter.setId("updatedSince");
        String since = dateFormat.format(sinceWhen);
        exportFilter.setText(since);
        exportFilters[0] = exportFilter;

        log.info("SaleOrderExportJobService.execute(): Firing Export Requests for changed SaleOrders since {}",
                 DateTimeUtil.getSimpleDateAndTime(sinceWhen));

        exportJobRequest.setExportFilters(exportFilters);
        String jobId = null;
        try {
            jobId = webService.createJob(exportJobRequest);
            log.info("SaleOrderExportJobService.execute(): Resulting Job Code={}", jobId);
        } catch (RemoteException e) {
            log.debug("Error: SaleOrderExportJobService.execute(): {}", DateTimeUtil.getSimpleDateAndTime(sinceWhen));
        }

        Object lock = new Object();

        JobStatusPoller jobStatusPoller = new JobStatusPoller(webService, jobId, lock);

        Timer timer = null;
        try {
            synchronized (lock) {
                if (jobId != null) {
                    timer = new Timer("JobStatus-Poller");
                    timer.schedule(jobStatusPoller, waitTimeBeforePollingResult);
                }
                lock.wait();
            }
        } catch (InterruptedException e) {
            log.warn("Error: SaleOrderExportJobService.execute(): {} ", e);
        }
        fileUrl = jobStatusPoller.getCsvFileUrl();
        log.info("SaleOrderExportJobService.execute(): Gives the URL of the csv file : {}", fileUrl);
        return fileUrl;
    }

    private long getTimeInMilliSecs(long waitTimeMins) {
        return TimeUnit.MILLISECONDS.convert(waitTimeMins, TimeUnit.MINUTES);
    }

    public int computeSinceDays(int daysCount) {
        return (daysCount * (-1));
    }
}


