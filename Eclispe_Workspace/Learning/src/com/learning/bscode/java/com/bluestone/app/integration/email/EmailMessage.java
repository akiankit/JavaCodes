package com.bluestone.app.integration.email;

import java.io.Serializable;
import java.util.Map;

class EmailMessage implements Serializable {

    private static final long serialVersionUID = 1451867514574661581L;

    private TemplateVariables templateVariables;

    private final Map<String, String> embeddedImages;

    EmailMessage(TemplateVariables templateVariables, Map<String, String> embeddedImages) {
        this.templateVariables = templateVariables;
        this.embeddedImages = embeddedImages;
    }


    public Map<String, String> getEmbeddedImages() {
        //return ImmutableMap.copyOf(embeddedImages);
        return embeddedImages;
    }

    public TemplateVariables getTemplateVariables() {
        return templateVariables;
    }

    public String getTemplateName() {
        return templateVariables.getTemplateName();
    }


    Map<String, Serializable> getMap() {
        //return ImmutableMap.copyOf(((TemplateVariablesImpl) templateVariables).getMap());
        return ((TemplateVariablesImpl) templateVariables).getMap();
    }
}