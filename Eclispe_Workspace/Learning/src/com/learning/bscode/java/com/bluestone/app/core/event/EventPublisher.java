package com.bluestone.app.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher implements ApplicationEventPublisherAware {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);

    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void fireEvent(ApplicationEvent event) {
        try {
            eventPublisher.publishEvent(event);
        } catch (Throwable e) {
            // not thowing exception back , as dont want the application behaviour gets impacted due to errors in event handling 
            if (log.isErrorEnabled()) {
                log.error("Error occurred while executing fireEvent for event:{}", event, e);
            }
        }

    }
}
