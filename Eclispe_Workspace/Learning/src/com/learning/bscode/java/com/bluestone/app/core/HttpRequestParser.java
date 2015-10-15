package com.bluestone.app.core;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 4/25/13
 */
public class HttpRequestParser {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestParser.class);

    public static final String CLIENT_IP = "clientIP";
    public static final String HOST = "Host";
    public static final String REQUEST_URL = "RequestUrl";
    public static final String REFERER = "Referer";
    public static final String USERAGENT = "User-Agent";
    public static final String X_FORWARDED_FOR = "X-Forwarded-For";

    public static String getClientIp(HttpServletRequest request) {
        // read this for more details - http://rod.vagg.org/2011/07/wrangling-the-x-forwarded-for-header/
        String result;
        String xForwardedFor = getXForwardedFor(request);
        if (StringUtils.isBlank(xForwardedFor)) {
            String remoteAddr = request.getRemoteAddr();
            log.debug("HttpRequestParser:[request.getRemoteAddr()]={}", remoteAddr);
            result = remoteAddr;
        } else {
            log.debug("HttpRequestParser: request.header[X-Forwarded-For]={}", xForwardedFor);
            String[] ipList = xForwardedFor.split(",");
            result = ipList[0];
        }
        request.setAttribute(CLIENT_IP, result);
        return result;
    }

    public static String getXForwardedFor(HttpServletRequest request) {
        return request.getHeader(X_FORWARDED_FOR);
    }

    public static String getHost(HttpServletRequest request) {
        return request.getHeader(HOST);
    }

    public static String getReferer(HttpServletRequest request) {
        return request.getHeader(REFERER);
    }

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader(USERAGENT);
    }

    public static String dumpHeadersForLogging(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(CLIENT_IP).append("=").append(request.getAttribute(CLIENT_IP)).append("\n");
        sb.append(HOST).append("=").append(getHost(request)).append("\n");
        sb.append(REQUEST_URL).append("=").append(request.getRequestURL().toString()).append("\n");
        sb.append(REFERER).append("=").append(getReferer(request)).append("\n");
        sb.append(USERAGENT).append("=").append(getUserAgent(request)).append("\n");
        extractQueryParams(request, sb);
        sb.append("\t");
        return sb.toString();
    }

    private static void extractQueryParams(HttpServletRequest request, StringBuilder stringBuilder) {
        String queryString = request.getQueryString();
        if (queryString != null) {
            String[] eachToken = StringUtils.split(queryString, '&');
            stringBuilder.append("Query String details:\n");
            for (int i = 0; i < eachToken.length; i++) {
                String s = eachToken[i];
                stringBuilder.append("\t").append(s).append("\n");
            }
        }
    }

    public static String dumpBriefHeadersForLogging(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("\n");
        sb.append(CLIENT_IP).append("=").append(request.getAttribute(CLIENT_IP)).append("\n");
        sb.append(HOST).append("=").append(getHost(request)).append("\n");
        sb.append(REQUEST_URL).append("=").append(request.getRequestURL().toString()).append("\n");
        sb.append(USERAGENT).append("=").append(getUserAgent(request)).append("\n");
        sb.append("\t");
        return sb.toString();
    }
}
