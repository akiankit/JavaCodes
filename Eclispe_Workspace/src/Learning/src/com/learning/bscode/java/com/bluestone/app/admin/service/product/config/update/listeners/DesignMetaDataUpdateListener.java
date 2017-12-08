package com.bluestone.app.admin.service.product.config.update.listeners;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.bluestone.app.admin.service.FilterTagService;
import com.bluestone.app.admin.service.product.config.DesignFactory;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateConfigRegistry.UpdateProperty;
import com.bluestone.app.admin.service.product.config.update.UpdateMetaData;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.search.tag.TagDao;

@Service
public class DesignMetaDataUpdateListener implements ProductUpdateListener<UpdateMetaData> {
    
    private static final String ERROR_MESSAGE = "Product Update Listener wrongly configured." + DesignMetaDataUpdateListener.class.getName()  
                                                        + " only supports design name and description updates";
    
    private static final Logger log = LoggerFactory.getLogger(DesignMetaDataUpdateListener.class);

    @Autowired
    private FilterTagService filterTagService;
    
    @Override
    public void update(UpdateProperty updateProperty, UpdateMetaData updateMetaData) throws ProductUploadException {
        log.debug("DesignMetaDataUpdateListener.update for {}  updatemetadata {} ", updateProperty.name(), updateMetaData);
        switch (updateProperty) {
            case Design_Name:
                updateDesignName(updateMetaData);
                break;
            case Design_Description:
                updateDesignDescription(updateMetaData);
                break;
            case Tag:
            	updateDesignTags(updateMetaData);
            default:
                throw new ProductUploadException(ERROR_MESSAGE);
        }
        
    }

    private void updateDesignDescription(UpdateMetaData updateMetaData) {
        Design design = updateMetaData.getDesign();
        design.setLongDescription(updateMetaData.getUpdatedValue().toString());
    }

    private void updateDesignName(UpdateMetaData updateMetaData) {
        Design design = updateMetaData.getDesign();
        design.setDesignName(updateMetaData.getUpdatedValue().toString());
    }

    private void updateDesignTags(UpdateMetaData updateMetaData) throws ProductUploadException{
    	Design design = updateMetaData.getDesign();
    	String tags = (String)updateMetaData.getUpdatedValue();
    	String[] split = tags.split(",");
        List<String> tagsList = new LinkedList<String>(Arrays.asList(split));
        design.setTags(filterTagService.processTags(tagsList, updateMetaData.getDesign()));
    }

    @Override
    public void validate(UpdateProperty updateProperty, UpdateMetaData updateMetaData) throws ProductUploadException {
        switch (updateProperty) {
            case Design_Name:          	 
            case Design_Description:
            case Tag:
            	String value = (String) updateMetaData.getUpdatedValue();
                if (StringUtils.isBlank(value)) {
               	 throw new ProductUploadException(updateProperty.toString()+" is empty ");
                }
                break;
            default:
                throw new ProductUploadException(ERROR_MESSAGE);
        }
    }
    
}
