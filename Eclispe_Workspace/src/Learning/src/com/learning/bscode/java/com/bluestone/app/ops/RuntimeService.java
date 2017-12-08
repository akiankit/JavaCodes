package com.bluestone.app.ops;

import java.util.List;

import com.google.common.collect.ImmutableList;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 1/29/13
 */
public class RuntimeService {

    private static final Logger log = LoggerFactory.getLogger(RuntimeService.class);

    private final static ImmutableList<String> PRODUCT_CONFIG = ImmutableList.of("com.bluestone.app.design.model.Customization",
                                                                                 "com.bluestone.app.core.util.LinkUtils",
                                                                                 "com.bluestone.app.design.service.CustomizationService",
                                                                                 //"com.bluestone.app.search.filter.FilterPageService",
                                                                                 "com.bluestone.app.seo.service.BrowsePageSEOService",
                                                                                 "com.bluestone.app.shipping.service.HolidayService",
                                                                                 "com.bluestone.app.pricing.PricingEngineService",
                                                                                 "com.bluestone.app.integration.RestConnection");

    private final static ImmutableList<String> COOKIES = ImmutableList.of("com.bluestone.app.core.tracking.BSCookieParser",
                                                                          "com.bluestone.app.core.tracking.BluestoneCookie",
                                                                          "com.bluestone.app.account.VisitorHistoryServiceV2",
                                                                          "com.bluestone.app.core.tracking.BSCookieEncryptor",
                                                                          "com.bluestone.app.core.tracking.VisitorCookieService",
                                                                          "com.bluestone.app.core.tracking.CookieContentValidator",
                                                                          "org.springframework.web.util.CookieGenerator",
                                                                          "com.bluestone.app.core.web.RequestInterceptor");


    public static void enableLogging(Level level, String... loggerName) {
        log.info("RuntimeService.enableLogging() to level={}", level.toString());
        for (String eachName : loggerName) {
            org.apache.log4j.Logger requestedLogger = LogManager.getLogger(eachName);
            requestedLogger.setLevel(level);
        }
    }

    public static void enableFullProductConfigurationLogging(Level level) {
        if (Level.DEBUG.equals(level) && log.isDebugEnabled()) {
            enableLogging(level, PRODUCT_CONFIG);
        } else {
            enableLogging(level, PRODUCT_CONFIG);
        }
    }

    public static List<String> enableCookiesDebugging(Level level) {
        enableLogging(level, COOKIES);
        return COOKIES;
    }

    private static void enableLogging(Level level, ImmutableList<String> logNames) {
        log.info("RuntimeService.enableLogging() to Level={}", level.toString());
        for (String eachName : logNames) {
            org.apache.log4j.Logger requestedLogger = LogManager.getLogger(eachName);
            requestedLogger.setLevel(level);
        }
    }
}
