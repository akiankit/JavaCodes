package com.bluestone.app.admin.service.product.config.update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.admin.service.UniwareCreateUpdateCustomizationService;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateConfigRegistry.PostUpdateAction;
import com.bluestone.app.admin.service.product.config.update.listeners.ProductUpdateListener;
import com.bluestone.app.admin.service.product.config.update.postprocessors.UpdateAllCustomizationPrice;
import com.bluestone.app.admin.service.product.config.update.postprocessors.UpdateCustomizationPrice;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;

@Service
class DesignUpdateService implements ProductUpdateService {
    
    private static final Logger log = LoggerFactory.getLogger(DesignUpdateService.class);
    
    @Autowired
    private UpdateAllCustomizationPrice updateAllCustomizationPrice;
    
    @Autowired
    private UpdateCustomizationPrice updateCustomizationPrice;
    
    @Autowired
    private ProductUpdateConfigRegistry productUpdateConfigRegistry;
    
    @Autowired
    private UniwareCreateUpdateCustomizationService uniwareCreateUpdateCustomizationService;

    @Autowired
    private DesignDao designDao;
    
    @Override
    public void update(ProductUpdateConfigRegistry.UpdateProperty updateProperty, UpdateMetaData updateMetaData) throws ProductUploadException {
        try {
            PostUpdateAction[] postUpdateActions = updateProperty.getPostUpdateAction();
            
            ProductUpdateListener<UpdateMetaData>[] updateListeners = productUpdateConfigRegistry.getUpdateServices(updateProperty);
            
            validateUpdateData(updateProperty, updateMetaData, updateListeners);
            
            executeUpdate(updateProperty, updateMetaData, updateListeners);
            
            
            designDao.update(updateMetaData.getDesign());
            
            executePostUpdateActions(updateMetaData, postUpdateActions);
            
            //TODO once jms topic is implemented , all clearcache here or have CacheClear annotation on top of this function
        } catch (ProductUploadException e) {
             log.error("ProductUpdateException occured while executing update ", e.getErrorList());
             throw e;
        } catch (Exception e) {
            log.error("Error occured while executing update ",e);
            throw new ProductUploadException("Exception while updating product-" + e.getMessage());
        }
    }

    private void executeUpdate(ProductUpdateConfigRegistry.UpdateProperty updateProperty, UpdateMetaData updateMetaData, ProductUpdateListener<UpdateMetaData>[] updateListeners) throws ProductUploadException {
        for (ProductUpdateListener<UpdateMetaData> productUpdateListener : updateListeners) {
            productUpdateListener.update(updateProperty, updateMetaData);
        }
    }

    private void validateUpdateData(ProductUpdateConfigRegistry.UpdateProperty updateProperty, UpdateMetaData updateMetaData, ProductUpdateListener<UpdateMetaData>[] updateListeners) throws ProductUploadException {
        for (ProductUpdateListener<UpdateMetaData> productUpdateListener : updateListeners) {
             productUpdateListener.validate(updateProperty, updateMetaData);
        }
    };

    private void executePostUpdateActions(UpdateMetaData updateMetaData, PostUpdateAction[] postUpdateActions) throws ProductUploadException {
    	for (PostUpdateAction postDesignUpdateAction : postUpdateActions) {
            switch (postDesignUpdateAction) {
                case UPDATE_ALL_CUSTOMIZATION_PRICE:
                    updateAllCustomizationPrice.execute(updateMetaData);
                    break;
                case UPDATE_CUSTOMIZATION_PRICE:
                    updateCustomizationPrice.execute(updateMetaData);
                    break;
                case PUSH_ALL_CUSTOMIZATIONS_TO_UNIWARE:
                	uniwareCreateUpdateCustomizationService.execute(updateMetaData.getCustomization());
                	 for (Customization customization : updateMetaData.getCustomization().getDerivedCustomizations()) {
                         uniwareCreateUpdateCustomizationService.execute(customization);
                     }
                    break;
                case NONE:
                    break;
                default:
                    break;
            }
        }
    }
}

