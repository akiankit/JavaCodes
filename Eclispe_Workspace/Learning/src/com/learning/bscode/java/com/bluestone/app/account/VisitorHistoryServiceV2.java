package com.bluestone.app.account;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.account.model.VisitorHistory;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.core.util.Util;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class VisitorHistoryServiceV2 {

    private static final String DIRECT = "DIRECT";

    private static String VISITORHISTORY_SEPARATOR = TrackingData.VISITORHISTORY_SEPARATOR;

    private static final Logger log = LoggerFactory.getLogger(VisitorHistoryServiceV2.class);

    @Autowired
    private BaseDao baseDao;

    private void createRecord(VisitorHistory visitorHistory) {
        baseDao.create(visitorHistory);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createVisitorHistory(String visitorHistoryStr, Visitor visitor) {
        log.debug("VisitorHistoryServiceV2.createVisitorHistory() for Visitor={}", visitor);
        Map<String, String> paramMap = new HashMap<String, String>();
        if (StringUtils.isEmpty(visitorHistoryStr) || visitor == null) {
            return;
        }
        String[] allParams = visitorHistoryStr.split(VISITORHISTORY_SEPARATOR);
        for (String eachParam : allParams) {
            String[] param = StringUtils.split(eachParam, "=");
            if (param.length == 2) {
                param[1] = Util.isEmptyString(param[1]) ? null : param[1];
                paramMap.put(param[0], param[1]);
            }
        }
        VisitorHistory visitorHistory = new VisitorHistory(visitor, paramMap);
        createRecord(visitorHistory);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String saveOrGetVisitorHistory(HttpServletRequest request, Visitor visitor, boolean save) {
        String result = null;
        final TrackingData trackingData = new TrackingData(request);
        log.debug("VisitorHistoryServiceV2.saveOrGetVisitorHistory():{}", trackingData);

        if (StringUtils.isNotEmpty(trackingData.httpReferrer)) {
            try {
                URL referrerURL = new URL(trackingData.httpReferrer);
                URL requestURL = new URL(trackingData.requestUrl);
                if (referrerURL.getHost().equals(requestURL.getHost())) {
                    //Navigating within our site - do not log
                    if (!save) {
                        trackingData.referrer = DIRECT;
                        trackingData.utmSource = DIRECT;
                        return trackingData.getHistoryString();
                    }
                    return null;
                } else {
                    trackingData.referrer = referrerURL.getHost().replaceFirst("www.", "");
                    trackingData.searchQuery = referrerURL.getQuery();
                    if (StringUtils.containsIgnoreCase(trackingData.httpReferrer, "google")) {
                        trackingData.source = "Google";
                    }
                }
            } catch (MalformedURLException e) {
                log.error("ERROR: Referer URL has issues. Request URL=[{}] Header(referer)=[{}] Cause={} ",
                          trackingData.requestUrl, trackingData.httpReferrer, e.getMessage(), Throwables.getRootCause(e));
            } catch (Exception e) {
                log.error("Error parsing referrer header for Request URL=[{}] Header(referer)=[{}]  Error Cause=[{}] ",
                          trackingData.requestUrl, trackingData.httpReferrer, e.toString(), Throwables.getRootCause(e));
            }

        } else {
            log.debug("VisitorHistoryServiceV2.saveOrGetVisitorHistory(): httpReferrer is either 'null' or 'empty' for URL={}", trackingData.requestUrl);
        }

        log.debug("VisitorHistoryServiceV2.saveOrGetVisitorHistory(): source={}", trackingData.source);

        if (trackingData.source != null || trackingData.referrer != null) {
            if ("Google".equals(trackingData.source)) {// Google
                String googleKeywordQuery = null;
                trackingData.utmSource = trackingData.source;
                if (StringUtils.isNotEmpty(trackingData.searchQuery)) {
                    String[] qParams = trackingData.searchQuery.split("&");
                    for (String query : qParams) {
                        String[] qValues = query.split("=");
                        if (qValues.length == 2) {
                            if (qValues[0].equals("q")) {
                                googleKeywordQuery = qValues[1];
                            }
                        }
                    }
                }
                if (trackingData.gclid != null) {//Adwords
                    trackingData.utmMedium = "cpc";
                    trackingData.matchedSearchQuery = googleKeywordQuery;
                } else {
                    trackingData.utmMedium = "organic";
                    trackingData.keyword = googleKeywordQuery;
                }
            } else {//Others
                trackingData.utmSource = request.getParameter("utm_source");
                trackingData.utmMedium = request.getParameter("utm_medium");
                trackingData.utmCampaign = request.getParameter("utm_campaign");
                trackingData.utmTerm = request.getParameter("utm_term");
                trackingData.utmContent = request.getParameter("utm_content");
            }
            if (StringUtils.isEmpty(trackingData.utmSource)) {
                trackingData.utmSource = "REFERRAL";
            }
        } else {
            //Direct traffic
            trackingData.referrer = DIRECT;
            trackingData.utmSource = DIRECT;
            log.debug("VisitorHistoryServiceV2.saveOrGetVisitorHistory(): It is a DIRECT Traffic.");
        }

        if (save) {
            VisitorHistory visitorHistory = trackingData.getVisitorHistory(visitor);
            createRecord(visitorHistory);
        } else {
            result = trackingData.getHistoryString();
        }
        log.debug("VisitorHistoryServiceV2.saveOrGetVisitorHistory(): Result={}", result);
        return result;
    }
}

