package com.bluestone.app.admin;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.ops.RuntimeService;

@Controller
@RequestMapping("/admin/*")
public class LogController {


    private static final Logger log = LoggerFactory.getLogger(LogController.class);
    @Qualifier("customizationService")

    @Autowired
    private CustomizationService customizationService;

    @RequestMapping(value = "/log/{levelAskedFor}/{loggerName:.+}", method = RequestMethod.GET)
    @RequiresPermissions("loglevel:delete")
    public
    @ResponseBody
    String changeLogLevel(@PathVariable String levelAskedFor, @PathVariable String loggerName) {
        log.info("\n\nLogController.changeLogLevel()");
        log.warn("***** Got request to change the log level for logger {} to {} **", loggerName, levelAskedFor);
        String response = "--- Something went wrong ---";
        if (SecurityUtils.getSubject().isAuthenticated()) {
            org.apache.log4j.Logger requestedLogger = getApachelog4JLogger(loggerName);
            if (requestedLogger == null) {
                log.error("Error: LogController.changeLogLevel(): Requested to change logging for {}, but this logger is not available. ", requestedLogger);
            } else {
                Level currentLevel = requestedLogger.getLevel();
                final Level newLevel = Level.toLevel(levelAskedFor, currentLevel);
                requestedLogger.setLevel(newLevel);

                Level latestLevel = getApachelog4JLogger(loggerName).getLevel();
                if (latestLevel.toString().equals(newLevel.toString())) {
                    response = "Success : Now " + getApachelog4JLogger(loggerName).getName() + " is at Level = " + latestLevel.toString();
                }
            }
            log.info("LogController : {} ", response);
        }
        return response;
    }

    private org.apache.log4j.Logger getApachelog4JLogger(String loggerName) {
        return LogManager.getLogger(loggerName);
    }

    @RequestMapping(value = "/cookie/{level}", method = RequestMethod.GET)
    @RequiresPermissions("cookielogging:delete")
    public
    @ResponseBody
    String debugCookies(@PathVariable String level) {
        Level level1 = Level.INFO;
        try {
            level1 = Level.toLevel(level.trim().toUpperCase());
        } catch (Exception exception) {
            log.error("Error: LogController.debugCookies(): ", exception);
            return " Failed " + exception.toString();
        }
        List<String> classNames = RuntimeService.enableCookiesDebugging(level1);
        StringBuilder sb = new StringBuilder("<p>");
        for (String className : classNames) {
            sb.append("<br></br>");
            sb.append(className);
        }
        sb.append("</p>");

        return "Success in changing the level to " + level + sb.toString();
    }

    @RequestMapping(value = "/seo/{isDisabled}", method = RequestMethod.GET)
    @RequiresPermissions("seo:delete")
    public
    @ResponseBody
    String seoControl(@PathVariable String isDisabled) {
        log.info("LogController.seoControl(): Path variable value ={}", isDisabled);
        if (SecurityUtils.getSubject().isAuthenticated()) {
            if ("Off".equalsIgnoreCase(isDisabled)) {
                log.info("LogController.seoControl(): Request to disable SEO");
                customizationService.setSEOEnabled(false);
            } else {
                log.info("LogController.seoControl(): Request to Enable SEO");
                customizationService.setSEOEnabled(true);
            }
        } else {
            log.warn("LogController.seoControl(): Authentication Failed.");
        }
        return "Done";
    }
}
