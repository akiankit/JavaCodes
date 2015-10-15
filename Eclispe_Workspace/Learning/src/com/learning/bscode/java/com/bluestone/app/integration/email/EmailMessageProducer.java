package com.bluestone.app.integration.email;

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.support.JmsUtils;

class EmailMessageProducer {

    private static final Logger log = LoggerFactory.getLogger(EmailMessageProducer.class);

    private JmsTemplate jmsTemplate;

    void queueMessage(TemplateVariables templateVariables, Map<String, String> embeddedImages) {
        log.info("EmailMessageProducer.queueMessage(): To=[{}] Subject=[{}]", templateVariables.get("to"), templateVariables.getSubject());
        final EmailMessage emailMessage = new EmailMessage(templateVariables, embeddedImages);
        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) {
                log.debug("EmailMessageProducer.MessageCreator.createMessage()");
                try {
                    ObjectMessage message = session.createObjectMessage();
                    message.setObject(emailMessage);
                    return message;
                } catch (JMSException jmsAPIException) {
                    log.error("Error at EmailMessageProducer.queueMessage(): Code={} - Cause={}", jmsAPIException.getErrorCode(),
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
