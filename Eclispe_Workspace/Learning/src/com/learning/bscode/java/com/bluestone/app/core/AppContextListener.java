package com.bluestone.app.core;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppContextListener implements ServletContextListener {
    
    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("AppContextListener.contextInitialized()");
        setDefaultTimeZone();
    }
    
    private void setDefaultTimeZone() {
        log.debug("AppContextListener.setDefaultTimeZone");
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+5:30"));
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("AppContextListener.contextDestroyed()");
    }
    
}
