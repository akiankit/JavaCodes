package com.bluestone.app.integration.uniware;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;

public class UniwareJMSProducer {

    private static final Logger log = LoggerFactory.getLogger(UniwareJMSProducer.class);

    private JmsTemplate jmsTemplate;

    public void queueMessage(final String orderId) {
        log.info("UniwareJMSProducer.queueMessage() for OrderId={}", orderId);
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) {
                try {
                    TextMessage message = session.createTextMessage(orderId);
                    return message;
                } catch (JMSException jmsAPIException) {
                    log.error("Error at UniwareJMSProducer.queueMessage(): Code={} Cause ={} ", jmsAPIException.getErrorCode(),
                              JmsUtils.buildExceptionMessage(jmsAPIException),
                              jmsAPIException);
                    throw JmsUtils.convertJmsAccessException(jmsAPIException);
                }
            }
        });
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
}
