package com.bluestone.app.reporting;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.bluestone.app.QueryTimer;
import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.FileUtil;

public abstract class AbstractReportService<E extends Report> {
    
    private static final Logger log = LoggerFactory.getLogger(AbstractReportService.class);
    
    private int   exportBatchSize = 200;
    
    @PostConstruct
    public void init() {
        String property = ApplicationProperties.getProperty("export.batch.size");
        if (StringUtils.isNotBlank(property)) {
            try {
                exportBatchSize = Integer.parseInt(property);
                log.info("export batch size {}" , exportBatchSize);
            } catch (NumberFormatException e) {
                log.error("Error occured while parsing property export.batch.size ", e);
            }
        }
    }
    
    public File getCSVReport(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate, String[] columnNames,String fileName) throws Exception {
		log.debug("AbstractReportService.getCSVReport() listFilterCriteria, fromDate, toDate, columnNames, fileName",
										listFilterCriteria, fromDate, toDate, columnNames, fileName);
        
        int countOfRecords = getCountOfRecords(listFilterCriteria, fromDate, toDate);
        log.info("Writing {} number of records to filename:{}",countOfRecords,fileName);
        FileWriter fileWriter = null;
        CSVWriter csvWriter = null;
        File file = new File(fileName);
        try {
            fileWriter = new FileWriter(file);
            csvWriter = new CSVWriter(fileWriter);
            csvWriter.writeNext(columnNames);
            listFilterCriteria.setItemsPerPage(exportBatchSize);
            int offset = 0;
            while (offset < countOfRecords) {
                long startTime = QueryTimer.getCurrentTime();
            	List<? extends Report> listOfRecords = getListOfRecords(listFilterCriteria, fromDate, toDate, offset);
                long endTime = QueryTimer.getCurrentTime();
                if(QueryTimer.isOn()) {
                    QueryTimer.logger.debug("Total time to fetch {} records report {}", exportBatchSize, (endTime - startTime));
                }

            	for (Report report : listOfRecords) {
                    String[] asCSVRecord = report.asCSVRecord();
                    csvWriter.writeNext(asCSVRecord);
                }
                offset += exportBatchSize;
                listOfRecords.clear();
                listOfRecords = null;
			}
            csvWriter.flush();
            return file;
        } catch (Exception exception) {
            log.error("Error getting CSV Records {}", (file != null ? file.getAbsolutePath() : ""), exception);
            throw exception;
        } finally {
            FileUtil.closeStream(csvWriter);
            FileUtil.closeStream(fileWriter);
        }
    }
    
    public abstract List<? extends Report> getListOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate, int offset);
    
    public abstract int getCountOfRecords(ListFilterCriteria listFilterCriteria, Date fromDate, Date toDate);
}
