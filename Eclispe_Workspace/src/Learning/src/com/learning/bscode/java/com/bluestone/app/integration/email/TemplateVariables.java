package com.bluestone.app.integration.email;

import java.io.Serializable;

import com.bluestone.app.account.model.Customer;

/**
 * @author Rahul Agrawal
 *         Date: 4/9/13
 */
public interface TemplateVariables extends Serializable {


    /**
     * multiple recipients should be seperated by ";"
     *
     * @param to
     */
    void setToField(String to);

    void setCCField(String cc);

    void setBccField(String bcc);

    String[] getToField();

    String[] getBccField();

    String[] getCCField();

    void setFrom(String from);

    String getFrom();

    void setSubject(String subject);

    String getSubject();

    String getTemplateName();

    Serializable put(String key, Serializable value);

    Serializable get(String key);

    void setCustomer(Customer customer);
}
