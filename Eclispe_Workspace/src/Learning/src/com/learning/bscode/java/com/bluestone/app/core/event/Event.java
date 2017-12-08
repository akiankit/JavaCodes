package com.bluestone.app.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;

import com.bluestone.app.core.MessageContext;

public class Event extends ApplicationEvent {
    private static final long serialVersionUID = -2021326312147962649L;
    //private Map<String, Serializable> messageContextMap;
    private static final Logger log = LoggerFactory.getLogger(Event.class);
    private MessageContext messageContext;

    public Event(RaiseEvent source) {
        super(source);
    }

    public RaiseEvent getRaiseEvent() {
        return (RaiseEvent) source;
    }

    @Override
    public String toString() {
        return "Event [getRaiseEvent()=" + getRaiseEvent() + "] - MessageContext Id=" + getMessageContextId();
    }

    /*public Map<String, Serializable> getMessageContextMap() {
        return messageContextMap;
    }

    public void setMessageContextMap(Map<String, Serializable> messageContextMap) {
        log.debug("Event.setMessageContextMap()");
        this.messageContextMap = messageContextMap;
    }*/


    public void setMessageContext(MessageContext messageContext) {
        log.debug("Event.setMessageContext()");
        this.messageContext = messageContext;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public String getMessageContextId() {
        String result = "";
        if (messageContext != null) {
            result = messageContext.getId();
        }
        return result;
    }
}
