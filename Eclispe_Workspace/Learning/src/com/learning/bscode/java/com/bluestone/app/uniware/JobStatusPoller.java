package com.bluestone.app.uniware;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 11/29/12
 */
public class JobStatusPoller extends TimerTask {

    private static final Logger log = LoggerFactory.getLogger(JobStatusPoller.class);
    private WebService webService;
    final private String jobCode;
    private String csvFileUrl;
    private Object lock;

    public JobStatusPoller(WebService webService, String jobCode, Object lock) {
        this.webService = webService;
        this.jobCode = jobCode;
        this.lock = lock;

    }

    @Override
    public void run() {
        log.debug("JobStatusPoller.run()");
        synchronized (lock) {
            try {
                csvFileUrl = webService.getJobStatus(jobCode);
            } catch (Throwable e) {
                log.error("Error: SaleOrderExportJobService.geFileUrl()for jobCode={} ", jobCode, e);
            } finally {
                try {
                    lock.notify();
                } catch (Exception exception) {
                    log.error("Error in releasing the lock");
                }
            }
        }
    }

    public String getCsvFileUrl() {
        return csvFileUrl;
    }

    /* public String call() throws Exception {
        log.debug("JobStatusPoller.call()");
        return csvFileUrl;
    }*/
}
