package com.bluestone.app.core.service;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;

@Service
public class TryAtHomeService {

    @Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(TryAtHomeService.class);

    @Value("${tryOnHomeMail.to}")
    private String tryOnHomeMailTo;

    private static final String TRY_ON_HOME_MAIL_SUBJECT = "New enquiry for Try At Home";

    @PostConstruct
    private void validate() {
        if (StringUtils.isBlank(tryOnHomeMailTo)) {
            throw new RuntimeException("There is no receiver for the emails directed for \" " + TRY_ON_HOME_MAIL_SUBJECT + " \"");
        } else {
            log.info("TryAtHomeService: All email enquiries will be sent to [{}]", tryOnHomeMailTo);
        }
    }

    public void sendInternalMail(String userName, String contactNumber) {
        log.info("TryOnHomeService.sendInternalMail: UserName=[{}] ContactNumber=[{}]", userName, contactNumber);

        final TemplateVariables templateVariables = emailService.getTemplateVariables("tryAtHome_internal.vm");
        templateVariables.setToField(tryOnHomeMailTo);
        templateVariables.setSubject(TRY_ON_HOME_MAIL_SUBJECT);
        templateVariables.put("userName", userName);
        templateVariables.put("contactNumber", contactNumber);
        emailService.sendInternalEmailsWithoutImages(templateVariables);

        log.info("TryOnHomeService.sendInternalMail: Successfully Queued for UserName=[{}] ContactNumber=[{}]", userName, contactNumber);
    }
}
