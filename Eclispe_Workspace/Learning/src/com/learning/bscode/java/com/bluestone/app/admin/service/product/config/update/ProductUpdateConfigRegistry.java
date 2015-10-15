package com.bluestone.app.admin.service.product.config.update;

import java.util.EnumMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.admin.service.product.config.update.listeners.DesignMetaDataUpdateListener;
import com.bluestone.app.admin.service.product.config.update.listeners.DesignStonesUpdateListener;
import com.bluestone.app.admin.service.product.config.update.listeners.ProductUpdateListener;

@Service
public class ProductUpdateConfigRegistry {
    
    @Autowired
    private DesignStonesUpdateListener designStonesUpdateListener;
    
    @Autowired
    private DesignMetaDataUpdateListener designMetaDataUpdateListener;
    
    // enum map for update listeners against the update property
    public Map<UpdateProperty, ProductUpdateListener<UpdateMetaData>[]> productPropertiesToListenersMap = new EnumMap<UpdateProperty, ProductUpdateListener<UpdateMetaData>[]>(UpdateProperty.class);  
    
    public enum PostUpdateAction {
        // price
        UPDATE_ALL_CUSTOMIZATION_PRICE,
        UPDATE_CUSTOMIZATION_PRICE,
        
        // uniware
        PUSH_ALL_CUSTOMIZATIONS_TO_UNIWARE,
        PUSH_CUSTOMIZATION_TO_UNIWARE,
        
        // s3 for image updates
        PUSH_ALL_CUSTOMIZATIONS_TO_S3,
        PUSH_CUSTOMIZATION_TO_S3,
        NONE
    }
    
    // enum for registering property name and there post update actions. Post update Actions would be executed in order they are mentioned here. 
    public enum UpdateProperty {
        
        // Stone properties
        Stone_Size(PostUpdateAction.UPDATE_ALL_CUSTOMIZATION_PRICE, PostUpdateAction.PUSH_ALL_CUSTOMIZATIONS_TO_UNIWARE),
        No_Of_Stones(PostUpdateAction.UPDATE_ALL_CUSTOMIZATION_PRICE, PostUpdateAction.PUSH_ALL_CUSTOMIZATIONS_TO_UNIWARE),
        Stone_Setting(PostUpdateAction.PUSH_ALL_CUSTOMIZATIONS_TO_UNIWARE),
        
        // Design Properties
        Design_Name(PostUpdateAction.PUSH_ALL_CUSTOMIZATIONS_TO_UNIWARE),
        Design_Description(PostUpdateAction.NONE),
        Tag(PostUpdateAction.NONE),
        
        // Customization Properties
        Customization_Short_Description(PostUpdateAction.NONE);
        
        private final PostUpdateAction[] postUpdateAction;

        private UpdateProperty(PostUpdateAction... postUpdateAction) {
            this.postUpdateAction = postUpdateAction;
        }

        public PostUpdateAction[] getPostUpdateAction() {
            return postUpdateAction;
        }
    }
    
    @PostConstruct
    public void init() {
        ProductUpdateListener<UpdateMetaData>[] stoneUpdateListeners = new ProductUpdateListener[] {designStonesUpdateListener};
        
        productPropertiesToListenersMap.put(UpdateProperty.Stone_Size, stoneUpdateListeners);
        productPropertiesToListenersMap.put(UpdateProperty.No_Of_Stones, stoneUpdateListeners);
        productPropertiesToListenersMap.put(UpdateProperty.Stone_Setting, stoneUpdateListeners);
        
        ProductUpdateListener<UpdateMetaData>[] designNameUpdateServices = new ProductUpdateListener[] {designMetaDataUpdateListener};
        productPropertiesToListenersMap.put(UpdateProperty.Design_Name, designNameUpdateServices);
        productPropertiesToListenersMap.put(UpdateProperty.Design_Description, designNameUpdateServices);
        productPropertiesToListenersMap.put(UpdateProperty.Tag, designNameUpdateServices);
    }
    
    public ProductUpdateListener<UpdateMetaData>[] getUpdateServices(UpdateProperty updateProperty) {
        return productPropertiesToListenersMap.get(updateProperty);
    }
    
}
