package com.bluestone.app.integration.email;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.mail.internet.MimeMessage;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.JmsUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.service.VelocityTemplateService;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
class EmailMessageConsumer implements SessionAwareMessageListener {

    private static final Logger log = LoggerFactory.getLogger(EmailMessageConsumer.class);

    @Autowired
    private VelocityTemplateService velocityTemplateService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${mail.from}")
    private String from;

    @Value("${build.mode}")
    private String buildMode;

    @Value("${allmails.to}")
    private String allMailsTo;


    private void send(final EmailMessage emailMessage) throws Throwable {
        log.debug("EmailMessageConsumer.send()");
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                Map<String, Serializable> map = emailMessage.getMap();
                Map<String, String> embeddedImages = emailMessage.getEmbeddedImages();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

                final TemplateVariables templateVariables = emailMessage.getTemplateVariables();
                if (buildMode.equalsIgnoreCase("dev") || buildMode.equalsIgnoreCase("test")) {
                    message.setTo(allMailsTo);
                    message.setBcc(allMailsTo);
                    log.warn("EmailMessageConsumer.prepare(): Directing all the emails to {} as the build mode is {}", allMailsTo, buildMode);
                } else {
                    message.setTo(templateVariables.getToField());
                    String[] bccField = templateVariables.getBccField();
                    if (bccField != null) {
                        message.setBcc(bccField);
                    }
                }
                message.setFrom(from);

                String subject = templateVariables.getSubject();
                message.setSubject(subject);
                String text = velocityTemplateService.getText(emailMessage.getTemplateName(), map);
                message.setText(text, true);
                Set<String> keySet = embeddedImages.keySet();
                for (String eachImage : keySet) {
                    UrlResource image = new UrlResource(embeddedImages.get(eachImage));
                    message.addInline(eachImage, image);
                }
                log.debug("EmailMessageConsumer.send(): Mail to = [{}] : Subject={}",
                          Arrays.toString(templateVariables.getToField()), subject);
                log.trace("Text = {}", text);
            }
        };
        mailSender.send(preparator);
    }

    @Override
    public void onMessage(Message message, Session session) {
        String jmsMessageID = "Not yet initialized";
        String mailTo = "Not yet initialized";
        boolean isJmsRedelivered = false;
        String subject = "Not yet initialized";
        try {
            jmsMessageID = message.getJMSMessageID();
            isJmsRedelivered = message.getJMSRedelivered();
            log.debug("EmailMessageConsumer.onMessage(): Is ReDelivered={}, MessageID={}", isJmsRedelivered, jmsMessageID);
            if (message instanceof ObjectMessage) {
                Serializable object = ((ObjectMessage) message).getObject();
                final EmailMessage emailMessage = (EmailMessage) object;
                final String[] toField = emailMessage.getTemplateVariables().getToField();
                //this variable has been extracted just for logging purposes when error happens, we need this.
                subject = emailMessage.getTemplateVariables().getSubject();
                mailTo = Arrays.toString(toField);
                log.info("EmailMessageConsumer.onMessage(): Is ReDelivered={}, MessageID={} email=[{}] subject=[{}]",
                         isJmsRedelivered, jmsMessageID, mailTo, subject);
                send(emailMessage);
                message.acknowledge();
            } else {
                log.error("EmailMessageConsumer.onMessage() JMSMessageID={} is not an ObjectMessage. *******", jmsMessageID);
            }
        } catch (JMSException jmsAPIException) {
            log.error("EmailMessageConsumer.onMessage():JMS Error for JMSMessageId=[{}] IsRedelivered=[{}] Receiver-Email=[{}] Subject=[{}] \nJMSErrorCode=[{}] Cause={}",
                      jmsMessageID,
                      isJmsRedelivered,
                      mailTo,
                      subject,
                      jmsAPIException.getErrorCode(),
                      JmsUtils.buildExceptionMessage(jmsAPIException), jmsAPIException);
            attemptRecovery(session, jmsMessageID);
        } catch (Throwable throwable) {
            log.error("EmailMessageConsumer.onMessage():Error processing for JMSMessageId=[{}] Subject=[{}] Receiver-Email=[{}] Is Redelivered=[{}:",
                      jmsMessageID, subject, mailTo, isJmsRedelivered, Throwables.getRootCause(throwable));
            final String errorMsg = throwable.getLocalizedMessage();
            if (isBlackListed(errorMsg)) {
                log.info("EmailMessageConsumer.onMessage():Is Blacklisted/Rejected=true for [{}] . Hence not going to retry.", mailTo);
                try {
                    message.acknowledge();
                } catch (JMSException e) {
                    log.error("Error: EmailMessageConsumer.onMessage(): Error during acknowledge of jms messageID={}", jmsMessageID);
                }
            } else {
                attemptRecovery(session, jmsMessageID);
            }
        }
    }

    static boolean isBlackListed(String errorMsg) {
        boolean blackListed = false;
        if (errorMsg != null) {
            blackListed = StringUtils.containsIgnoreCase(errorMsg, "Address blacklisted");
            blackListed = blackListed || StringUtils.containsIgnoreCase(errorMsg, "Message rejected");
        }
        return blackListed;
    }

    private void attemptRecovery(Session session, String jmsMessageId) {
        log.debug("EmailMessageConsumer.attemptRecovery() for jms message id={}", jmsMessageId);
        try {
            session.recover();
        } catch (JMSException e) {
            log.error("Error while executing session recovery after the EmailMessageConsumer failed.", e);
        }
    }
}