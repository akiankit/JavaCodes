package com.bluestone.app.integration.email;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

import com.bluestone.app.account.PasswordProcessors;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;

/**
 * @author Rahul Agrawal
 *         Date: 4/10/13
 */
class TemplateVariablesImpl implements TemplateVariables {

    private String templateName;


    private static final long serialVersionUID = 3866957176959027089L;


    private Map<String, Serializable> emailVariables = new HashMap<String, Serializable>();

    TemplateVariablesImpl(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String[] getToField() {
        return getMultipleValuesIfAny((String) emailVariables.get("to"));
    }

    @Override
    public void setToField(String to) {
        emailVariables.put("to", to);
    }


    @Override
    public String[] getBccField() {
        return getMultipleValuesIfAny((String) emailVariables.get("bcc"));
    }

    @Override
    public void setBccField(String bcc) {
        emailVariables.put("bcc", bcc);
    }

    @Override
    public void setCCField(String cc) {
        emailVariables.put("cc", cc);
    }

    @Override
    public String[] getCCField() {
        return getMultipleValuesIfAny((String) emailVariables.get("cc"));
    }

    @Override
    public String getFrom() {
        return (String) emailVariables.get("from");
    }

    @Override
    public void setFrom(String from) {
        emailVariables.put("from", from);
    }

    @Override
    public void setSubject(String subject) {
        emailVariables.put("subject", subject);
    }

    @Override
    public String getSubject() {
        return (String) emailVariables.get("subject");
    }

    @Override
    public String getTemplateName() {
        return templateName;
    }

    @Override
    public Serializable put(String key, Serializable value) {
        if (StringUtils.isBlank(key)) {
            throw new RuntimeException("Email variable name should not be null or empty. Received " + key + " : " + value);
        }
        return emailVariables.put(key, value);
    }

    @Override
    public Serializable get(String key) {
        return emailVariables.get(key);
    }

    @Override
    public void setCustomer(Customer customer) {
        emailVariables.put("customer", customer);
        emailVariables.put("CustomerName", WordUtils.capitalize(customer.getUserName(), null));

        if (customer != null && (!customer.isAccountActivated())) {
            try {
                String activationPageURL = PasswordProcessors.getActivationPageURL(customer.getEmail(), Constants.key);
                emailVariables.put("account_activation", activationPageURL);
            } catch (EncryptionException e) {
                //log.error("Error: EmailService.setDefaultEmailParams():Failed to generate the activationPageUrl link ", e);
                Throwables.propagate(e);
            }
        }
    }

    Map<String, Serializable> getMap() {
        return emailVariables;
    }

    private String[] getMultipleValuesIfAny(String toValue) {
        return StringUtils.split(toValue, ';');
    }
}
