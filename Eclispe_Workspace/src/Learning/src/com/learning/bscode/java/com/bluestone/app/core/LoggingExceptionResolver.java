package com.bluestone.app.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @author Rahul Agrawal
 *         Date: 12/26/12
 */
public class LoggingExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(LoggingExceptionResolver.class);

    private ProductionAlert productionAlert;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("LoggingExceptionResolver.doResolveException()");
        return super.doResolveException(request, response, handler, ex);
    }


    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        log.debug("LoggingExceptionResolver.logException()\n");
        log.error(buildLogMessage(ex, request), ex);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n\nException intercepted at LoggingExceptionResolver.\n");
        stringBuilder.append(HttpRequestParser.dumpHeadersForLogging(request));
        stringBuilder.append("\n");
        Object object = request.getAttribute("messageContext");
        if (object != null) {
        }

        String msg = stringBuilder.toString();
        productionAlert.send(msg, ex);
        log.error("Error: LoggingExceptionResolver.logException():Send this as email:{}", msg , ex);
        super.logException(ex, request);
    }

    /*public String buildLogMessage(Exception ex, HttpServletRequest request){

    }*/

    public ProductionAlert getProductionAlert() {
        return productionAlert;
    }

    @Autowired
    public void setProductionAlert(@Qualifier("emailAlertService") ProductionAlert productionAlert) {
        this.productionAlert = productionAlert;
    }
}
