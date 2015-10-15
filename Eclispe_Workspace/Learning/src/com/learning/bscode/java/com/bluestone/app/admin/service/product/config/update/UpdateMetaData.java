package com.bluestone.app.admin.service.product.config.update;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.Design;

public class UpdateMetaData {
    
    private static final Logger log = LoggerFactory.getLogger(UpdateMetaData.class);
    
    private Design design;
    
    private Customization customization;
    
    private Object updatedValue;
    
    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public Customization getCustomization() {
        return customization;
    }

    public void setCustomization(Customization customization) {
        this.customization = customization;
    }

    public Object getUpdatedValue() {
        return updatedValue;
    }

    public void setUpdatedValue(Object updatedValue) {
        this.updatedValue = updatedValue;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UpdateMetaData [design=");
        builder.append(design);
        builder.append(", customization=");
        builder.append(customization);
        builder.append(", updatedValue=");
        builder.append(updatedValue);
        builder.append("]");
        return builder.toString();
    }
    
}
