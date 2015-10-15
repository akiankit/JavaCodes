package com.bluestone.app.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationProperties {

    private static Properties applicationProperties = new Properties();
    private static final Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

    static {
        loadProperties();
    }

    private static void loadProperties() {
        log.info("ApplicationProperties.loadProperties() from file=application.properties");
        InputStream inputStream = null;
        try {
            inputStream = ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties");
            applicationProperties.load(inputStream);
            if(log.isInfoEnabled()){
                log.info("Properties loaded from the application.properties file");
                Set<String> keys = applicationProperties.stringPropertyNames();
                for (String eachKey : keys) {
                    log.info("\t{}={}", eachKey, applicationProperties.getProperty(eachKey));
                }
            }
        } catch (Exception e) {
            log.error("Error in reading application.properties file", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("Error in closing stream for application.properties", e);
                }
            }
        }
    }

    public static String getProperty(String propertyName) {
        return applicationProperties.getProperty(propertyName);
    }
}
