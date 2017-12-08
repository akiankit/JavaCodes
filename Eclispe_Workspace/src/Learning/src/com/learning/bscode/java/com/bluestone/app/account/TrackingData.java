package com.bluestone.app.account;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.account.model.VisitorHistory;
import com.bluestone.app.core.util.DateTimeUtil;

/**
 * @author Rahul Agrawal
 *         Date: 3/6/13
 */
class TrackingData {

    private static final Logger log = LoggerFactory.getLogger(TrackingData.class);
    static String VISITORHISTORY_SEPARATOR = "#@#";

    String source = null;
    String gclid = null;
    String timeStamp = null;


    String referrer = null;
    String searchQuery = null;
    String utmSource = null;
    String utmMedium = null;
    String utmCampaign = null;
    String utmTerm = null;
    String utmContent = null;
    String keyword = null;
    String matchedSearchQuery = null;

    // if interested read this - http://en.wikipedia.org/wiki/HTTP_referer
    String httpReferrer = null;

    //The returned URL contains a protocol, server name, port number, and server path, but it does not include query string parameters.
    String requestUrl = null;

    TrackingData(HttpServletRequest request) {
        source = request.getParameter("utm_source");
        gclid = request.getParameter("gclid");
        httpReferrer = request.getHeader("referer");
        requestUrl = request.getRequestURL().toString();
        timeStamp = DateTimeUtil.formatDate(DateTimeUtil.getDate(), "yyyyMMddHHmmss");
        if (gclid != null) {
            source = "Google";
        }
    }


    VisitorHistory getVisitorHistory(Visitor visitor) {
        return new VisitorHistory(visitor, referrer, utmSource, utmMedium, utmCampaign,
                                  utmTerm, utmContent, keyword, matchedSearchQuery);
    }

    String getHistoryString() {
        return new StringBuilder(1000)
                .append("timeStamp=").append(timeStamp).append(VISITORHISTORY_SEPARATOR)
                .append("referrer=").append(referrer).append(VISITORHISTORY_SEPARATOR)
                .append("utmSource=").append(utmSource).append(VISITORHISTORY_SEPARATOR)
                .append("utmMedium=").append(utmMedium).append(VISITORHISTORY_SEPARATOR)
                .append("utmCampaign=").append(utmCampaign).append(VISITORHISTORY_SEPARATOR)
                .append("utmTerm=").append(utmTerm).append(VISITORHISTORY_SEPARATOR)
                .append("utmContent=").append(utmContent).append(VISITORHISTORY_SEPARATOR)
                .append("keyword=").append(keyword).append(VISITORHISTORY_SEPARATOR)
                .append("matchedSearchQuery=").append(matchedSearchQuery)
                .toString();
    }


    /*boolean isNavigatingWithinOurSite() throws MalformedURLException {
        URL referrerURL = new URL(httpReferrer);
        URL requestURL = new URL(requestUrl);
        if (referrerURL.getHost().equals(requestURL.getHost())) {
            return true;
        }
        return false;
    }*/

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TrackingData");
        sb.append("{\n\t gclid=").append(gclid).append("\n");
        sb.append("\t httpReferrer=").append(httpReferrer).append("\n");
        sb.append("\t referrer=").append(referrer).append("\n");
        sb.append("\t requestUrl=").append(requestUrl).append("\n");
        sb.append("\t source=").append(source).append("\n");
        sb.append("\t timeStamp=").append(timeStamp).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
