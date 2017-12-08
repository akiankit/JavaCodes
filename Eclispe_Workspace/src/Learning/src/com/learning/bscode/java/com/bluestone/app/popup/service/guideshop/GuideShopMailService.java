package com.bluestone.app.popup.service.guideshop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.integration.email.EmailService;
import com.bluestone.app.integration.email.TemplateVariables;

@Service
public class GuideShopMailService
{
	@Autowired
    private EmailService emailService;

    private static final Logger log = LoggerFactory.getLogger(GuideShopMailService.class);
    
    @Value("${guideShopMail.to}")
    private String guideShopMail;

    private static final String GUIDE_SHOP_MAIL_SUBJECT = "New enquiry for Guide Shop";
    
    public void sendInternalMail(String contactNumber) 
    {
        log.info("GuideShopMailService.sendInternalMail: ContactNumber=[{}]", contactNumber);

        final TemplateVariables templateVariables = emailService.getTemplateVariables("guideShopMail_internal.vm");
        templateVariables.setToField(guideShopMail);
        templateVariables.setSubject(GUIDE_SHOP_MAIL_SUBJECT);
        templateVariables.put("contactNumber", contactNumber);
        emailService.sendInternalEmailsWithoutImages(templateVariables);

        log.info("GuideShopMailService.sendInternalMail: Successfully Queued for ContactNumber=[{}]", contactNumber);
    }
}
