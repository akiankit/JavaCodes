package com.bluestone.app.core;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageContext {

    private static final Logger log = LoggerFactory.getLogger(MessageContext.class);

    public static final String PAYMENT_TRANSACTION_ID = "paymentTransactionId";

    public static final String CART_ID = "cartId";

    public static final String VISITOR_ID = "visitorId";

    public static final String PAGE_NUMBER = "pageNumber";

    public static final String VISITOR_TAG_ID = "visitorTagId";

    public static final String ORDERID = "orderId";

    public static final String GOLD_MINE_ENROLLMENT_ID = "goldMineEnrollmentId";

    public static final String GOLD_MINE_PAYMENT_ID = "goldMinePaymentId";

    public static final String ORDER_UPDATE_HISTORY = "orderUpdateHistoryId";

    public static final String SHIPPINGITEM_UPDATE_HISTORY = "shippingItemUpdateHistoryId";

    public static final String ORDER_PAYMENT_TRANSACTION_STATUS = "orderPaymentTransactionStatus";

    public static final String ORDERITEM_UPDATE_HISTORY = "orderitemUpdateHistoryId";

    public static final String ORDERITEM = "orderItemId";

    public static final String CUSTOMERID = "customerId";

    public static final String MESSAGE_CONTEXT_ID = "messageContextId";

    public static final String SOLITAIRE_ORDER = "solitaireOrder";

    private final String id;

    private Map<String, Serializable> messageContextMap = new HashMap<String, Serializable>();

    private LinkedList<Serializable> orderItemIds = new LinkedList<Serializable>();
    private LinkedList<Serializable> orderItemHistoryIds = new LinkedList<Serializable>();

    private String contextPath;
    private String siteUrl;
    private String siteUrlWithContextPath;

    private String host;
    final private String clientIP;

    MessageContext(HttpServletRequest httpServletRequest) {
        this.id = UUID.randomUUID().toString();
        messageContextMap.put(MESSAGE_CONTEXT_ID, id);
        messageContextMap.put(ORDERITEM_UPDATE_HISTORY, orderItemHistoryIds);
        messageContextMap.put(ORDERITEM, orderItemIds);
        clientIP = HttpRequestParser.getClientIp(httpServletRequest);
        getSiteUrl(httpServletRequest);
        messageContextMap.put(HttpRequestParser.CLIENT_IP, clientIP);
    }

    private void getSiteUrl(HttpServletRequest request) {
        setHost(HttpRequestParser.getHost(request));
        //The returned URL contains a protocol, server name, port number, and server path, but it does not include query string parameters.
        String requestUrl = request.getRequestURL().toString();
        log.debug("MessageContext:[request.getRequestURL()]={}", requestUrl);
        log.debug("MessageContext:[request.getRequestURI()]={}", request.getRequestURI());
        log.debug("MessageContext:[request.getQueryString()]={}", request.getQueryString());

        URI uri = URI.create(requestUrl);
        StringBuilder sb = new StringBuilder(uri.getScheme()).append("://").append(getHost());
        siteUrl = sb.toString();
        contextPath = request.getContextPath();
        log.debug("MessageContext:[SiteUrl]={}  [ContextPath]={}", siteUrl, contextPath);
        siteUrlWithContextPath = siteUrl + contextPath;
    }

    public String getId() {
        return id;
    }

    public void destroy() {
        messageContextMap.clear();
        orderItemHistoryIds.clear();
        orderItemIds.clear();
        messageContextMap = null;
        orderItemHistoryIds = null;
        orderItemIds = null;
    }

    public void put(String key, String value) {
        messageContextMap.put(key, value);
    }

    public void put(String key, Number value) {
        messageContextMap.put(key, value);
    }

    public Serializable get(String key) {
        return messageContextMap.get(key);
    }

    public LinkedList<Serializable> getOrderItemIds() {
        return orderItemIds;
    }

    public void addOrderItemIds(Serializable orderItemId) {
        orderItemIds.add(orderItemId);
    }

    public LinkedList<Serializable> getOrderItemHistoryIds() {
        return orderItemHistoryIds;
    }

    public void addOrderItemHistoryId(Serializable orderItemHistoryId) {
        orderItemHistoryIds.add(orderItemHistoryId);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MessageContext : ");
        sb.append(" Id='").append(id).append("\n");
        final Map<String, Serializable> contextMap = getMessageContextMap();
        final Set<String> keys = contextMap.keySet();
        for (String key : keys) {
            Serializable value = contextMap.get(key);
            sb.append(key).append("=").append((value == null) ? "null" : value.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public Map<String, Serializable> getMessageContextMap() {
        return messageContextMap;
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getSiteUrlWithContextPath() {
        return siteUrlWithContextPath;
    }

    public String getHost() {
        return host;
    }

    //making this private as no one uses this and we should not allow it to be set more than once.
    private void setHost(String host) {
        log.debug("MessageContext: HttpRequest.Header.Host=[{}]", host);
        if (host == null) {
            this.host = "";
            return;
        }
        final String[] parts = host.split(":");
        if (parts != null) {
            this.host = parts[0];
        } else {
            this.host = "";
        }
    }

    public String getClientIP() {
        return clientIP;
    }
}
