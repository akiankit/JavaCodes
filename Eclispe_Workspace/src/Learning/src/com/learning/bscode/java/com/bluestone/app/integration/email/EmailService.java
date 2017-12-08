package com.bluestone.app.integration.email;

import java.util.Collections;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.ApplicationProperties;
import com.bluestone.app.core.util.Util;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private EmailMessageProducer emailMessageProducer;

    @Value("${mail.bcc}")
    private String bcc;

    private String fixedPart;

    @PostConstruct
    private void init() {
        fixedPart = ApplicationProperties.getProperty("theme_base_path").trim() + "/"
                    + ApplicationProperties.getProperty("theme_name").trim()
                    + ApplicationProperties.getProperty("theme_images_relative_path");
        log.info("EmailService.init(): fixedPart=[{}]", fixedPart);

        if (StringUtils.isBlank(bcc)) {
            throw new RuntimeException("Please set the bcc value to some valid email address");
        } else {
            log.info("EmailService.init(): bcc=[{}]", bcc);
        }

    }

    public TemplateVariables getTemplateVariables(String templateName) {
        return new TemplateVariablesImpl(templateName);
    }

    public void send(TemplateVariables templateVariables) {
        log.debug("EmailService.send()");
        setDefaultEmailParams(templateVariables);
        emailMessageProducer.queueMessage(templateVariables, getDefaultImageMap());
    }

    public void sendInternalEmailsWithoutImages(final TemplateVariables templateVariables) {
        setDefaultEmailParams(templateVariables);
        emailMessageProducer.queueMessage(templateVariables, Collections.<String, String>emptyMap());
    }
    
    public void sendOrderFeedbackEmail(TemplateVariables templateVariables) {
    	setDefaultEmailParams(templateVariables);
    	String imageLocation = Util.getSiteUrlWithContextPath() + fixedPart;        

        Map<String, String> emailImages = new ImmutableMap.Builder<String, String>()
                .put("bluestoneLogo", imageLocation + "/logo_mail.jpg")                
                .put("orderFeedbackImage", imageLocation + "/feedback.jpg")
                .build();
        emailMessageProducer.queueMessage(templateVariables, emailImages);
    }

    void setDefaultEmailParams(TemplateVariables emailVariables) {
        emailVariables.put("shop_name", Util.getSiteUrlWithContextPath());
        emailVariables.put("customer_care_number", ApplicationProperties.getProperty("shop_phone"));
        emailVariables.put("customer.bottom_link", Util.getSiteUrlWithContextPath());
        emailVariables.setBccField(bcc); // for archiving all emails sent to outside world in a single email id.
    }

    @VisibleForTesting
    Map<String, String> getDefaultImageMap() {
        String imageLocation = Util.getSiteUrlWithContextPath() + fixedPart;
        log.debug("EmailService.getDefaultEmailImageMap(): image location=[{}]", imageLocation);

        Map<String, String> emailImages = new ImmutableMap.Builder<String, String>()
                .put("bluestoneLogo", imageLocation + "/logo_mail.jpg")
                .put("rupee", imageLocation + "/icon_rupee.gif")                
                .build();
        return emailImages;
    }
}
