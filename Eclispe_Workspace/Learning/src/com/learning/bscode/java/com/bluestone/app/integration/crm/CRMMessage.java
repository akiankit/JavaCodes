package com.bluestone.app.integration.crm;

import java.io.Serializable;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CRMMessage implements Serializable {
    private static final Logger log = LoggerFactory.getLogger(CRMMessage.class);

    private static final long serialVersionUID = 2664692843659044402L;

    private final Map<String, Serializable> messageContextMap;

    private final CRM_EVENT crmEvent;

    public enum CRM_EVENT {
        CART_CREATED_EVENT,
        CART_UPDATED_EVENT,
        PAYMENT_FAILURE,
        ORDER_CREATED,
        ORDER_ITEM_STATUS_UPDATE,
        ORDER_STATUS_UPDATE,
        ORDER_ITEM_SHIPPING_STATUS_UPDATE,
        ORDER_PAYMENT_STATUS_UPDATE,
        CUSTOMER_REGISTERED, 
        HAVE_A_QUESTION,
        GOLD_MINE,
    }
    
    public CRMMessage(CRM_EVENT crmEvent, Map<String, Serializable> messageContextMap) {
        this.crmEvent = crmEvent;
        this.messageContextMap = messageContextMap;
    }

    public Map<String, Serializable> getMessageContextMap() {
        return messageContextMap;
    }

    public CRM_EVENT getCrmEvent() {
        return crmEvent;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("crmEvent=").append(crmEvent.name()).append("\n");
        for (String eachKey : messageContextMap.keySet()) {
            sb.append("\t").append(eachKey).append(":").append(messageContextMap.get(eachKey));
        }
        sb.append("\n}");
        return sb.toString();
    }
}
