package com.bluestone.app.core.event.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import com.bluestone.app.core.event.Event;
import com.bluestone.app.core.event.RaiseEvent;

public abstract class EventListener<E extends Event> implements ApplicationListener<E> {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onApplicationEvent(E event) {
        log.debug("EventListener.onApplicationEvent() for {}", event);
        RaiseEvent raiseEvent = event.getRaiseEvent();
        if (raiseEvent.isAsync()) {
            handleAsyncEvent(event);
        } else {
            handleSyncEvent(event);
        }
    }

    protected abstract void handleSyncEvent(E event);

    protected abstract void handleAsyncEvent(E event);

}
