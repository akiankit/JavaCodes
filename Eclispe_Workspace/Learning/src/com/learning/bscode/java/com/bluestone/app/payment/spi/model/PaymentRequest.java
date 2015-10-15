package com.bluestone.app.payment.spi.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PaymentRequest {
    
    private boolean isVmTemplate;
    
    private String  vmTemplatePath;
    
    private Map<String, Object> templateVariables = new HashMap<String, Object>();

    public boolean isVmTemplate() {
        return isVmTemplate;
    }

    public void setVmTemplate(boolean isVmTemplate) {
        this.isVmTemplate = isVmTemplate;
    }

    public String getVmTemplatePath() {
        return vmTemplatePath;
    }

    public void setVmTemplatePath(String vmTemplatePath) {
        this.vmTemplatePath = vmTemplatePath;
    }

    public Map<String, Object> getTemplateVariables() {
        return templateVariables;
    }

    public void setTemplateVariables(Map<String, Object> templateVariables) {
        this.templateVariables = templateVariables;
    }
    
    public void addTemplateVariable(String key, Object value) {
        templateVariables.put(key, value);
    }

    @Override
    public String toString() {
        Set<String> keys = templateVariables.keySet();
        StringBuilder stringBuilder= new StringBuilder();
        for (String key : keys) {
            stringBuilder.append(key).append(":").append(templateVariables.get(key)).append("\n");
        }
        return "PaymentRequest [isVmTemplate=" + isVmTemplate + ", vmTemplatePath=" + vmTemplatePath
                + "\ntemplateVariables=\n" + stringBuilder.toString() + "]";
    }
    
    
    
}
