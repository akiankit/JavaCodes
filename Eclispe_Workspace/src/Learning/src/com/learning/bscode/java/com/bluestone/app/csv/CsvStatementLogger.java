package com.bluestone.app.csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CsvStatementLogger {
	 public static final Logger log = LoggerFactory.getLogger(CsvStatementLogger.class);

	    static {
	        log.info("CsvStatementLogger.static initializer()");
	    }
}
