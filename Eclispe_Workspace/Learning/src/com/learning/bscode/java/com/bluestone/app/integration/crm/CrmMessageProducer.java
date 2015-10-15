package com.bluestone.app.integration.crm;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;

public class CrmMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(CrmMessageProducer.class);

    private JmsTemplate jmsTemplate;

    public void queueMessage(final CRMMessage crmMessage) {
        log.debug("CrmMessageProducer.queueMessage()");
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) {
                try {
                    ObjectMessage message = session.createObjectMessage();
                    message.setObject(crmMessage);
                    return message;
                } catch (JMSException jmsAPIException) {
                    log.error("Error at CrmMessageProducer.queueMessage(): Code={} Cause ={} ", jmsAPIException.getErrorCode(),
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
