package com.bluestone.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 12/12/12
 */
public class QueryTimer {
    private static final long ZERO = 0l;

    private QueryTimer() {
    }

    public static final Logger logger = LoggerFactory.getLogger(QueryTimer.class);

    public static boolean isOn() {
        return logger.isDebugEnabled();
    }

    public static long getCurrentTime() {
        if (isOn()) {
            return System.currentTimeMillis();
        } else {
            return ZERO;
        }
    }
}
