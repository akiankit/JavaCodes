package com.bluestone.app.admin.service.product.config.update.listeners;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.admin.service.product.config.update.ProductUpdateConfigRegistry.UpdateProperty;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.MetalSpecificationDao;
import com.bluestone.app.design.dao.StoneSpecificationsDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.DesignStoneSpecification;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.pricing.PricingEngineService;

@Service
public class DesignStonesUpdateListener implements ProductUpdateListener<StoneUpdateMetaData> {
    
    private static final String ERROR_MESSAGE = "Product Update Listener wrongly configured." + DesignStonesUpdateListener.class.getName()  
            + " only supports stone weight , noofstones, stone setting updates";
    
    private static final Logger log = LoggerFactory.getLogger(DesignStonesUpdateListener.class);

    @Autowired
    private StoneSpecificationsDao stonespecificationdao;
    
    @Autowired
    private PricingEngineService   pricingEngineService;
    
	
    
    @Override
    public void update(UpdateProperty updateProperty, StoneUpdateMetaData updateMetaData) throws ProductUploadException {
        log.debug("DesignMetaDataUpdateListener.update for {}  updatemetadata {} ", updateProperty.name(), updateMetaData);
        String[] value = (String[]) updateMetaData.getUpdatedValue();
        String[] stoneIds = (String[]) updateMetaData.getDesignStoneSpecificationIds();
        switch (updateProperty) {
            case No_Of_Stones:
            case Stone_Setting:
            	updateDesignStoneSpecification(updateProperty, value, stoneIds,updateMetaData);
                break;
            case Stone_Size:
            	updateCustomizationStoneSpecification(updateProperty,value,stoneIds,updateMetaData);
                break;
            default:
                throw new ProductUploadException(ERROR_MESSAGE);
        }
    }



	private void updateDesignStoneSpecification(UpdateProperty updateProperty,
			String[] value, String[] stoneIds,
			StoneUpdateMetaData updateMetaData)
			throws ProductUploadException {
			List<DesignStoneSpecification> designStoneSpecifications = updateMetaData.getDesign().getDesignStoneSpecifications();
			for (DesignStoneSpecification designspec : designStoneSpecifications) {
		    for (int i = 0; i < stoneIds.length; i++) {
		        try {
		            if (designspec.getId() == Long.parseLong(stoneIds[i])) {
		            	switch (updateProperty) {
		                case No_Of_Stones:
		                	designspec.setNoOfStones(Integer.parseInt(value[i]));
		                	break;
		                case Stone_Setting:
		                	designspec.setSettingType(value[i]);
		                	break;
		            	}
		            }
		        } catch (Exception e) {
		        	log.error("Error while updating the number of stones.", e);
		        	throw new ProductUploadException("Error while updating "+updateProperty.toString()+": " + value[i]);
		            
		        }
		    }
		}
	}

	private void updateCustomizationStoneSpecification(UpdateProperty updateProperty,
			String[] value, String[] custStoneIds,
			StoneUpdateMetaData updateMetaData)
			throws ProductUploadException {
		
			List<Customization> customizations = new LinkedList<Customization>();
	        customizations.addAll(updateMetaData.getCustomization().getDerivedCustomizations());
	        	List<CustomizationStoneSpecification> customizationStoneSpecification = updateMetaData.getCustomization().getCustomizationStoneSpecification();
	 	        for (CustomizationStoneSpecification custStoneSpec : customizationStoneSpecification) {
	 			    for (int i = 0; i < custStoneIds.length; i++) {
	 			        try {
	 			            if (custStoneSpec.getId() == Long.parseLong(custStoneIds[i])) {
	 			            	StoneSpecification stoneSpecification = custStoneSpec.getStoneSpecification();
	 			            	StoneSpecification newStoneSpecification = getStoneSpecification(value[i], stoneSpecification);
	 			            	custStoneSpec.setStoneSpecification(newStoneSpecification);
	 			            	for (Customization customization : customizations) {
	 			            		List<CustomizationStoneSpecification> derivedCustomizationStoneSpecification =customization.getCustomizationStoneSpecification();
	 			            		for (CustomizationStoneSpecification derivedcustomizationStoneSpec : derivedCustomizationStoneSpecification) {
	 			            			StoneSpecification derivedStoneSpecification = derivedcustomizationStoneSpec.getStoneSpecification();
	 			            			if(stoneSpecification.getStoneType().equalsIgnoreCase(derivedStoneSpecification.getStoneType()) && stoneSpecification.getShape().equalsIgnoreCase(derivedStoneSpecification.getShape()) && stoneSpecification.getSize().equalsIgnoreCase(derivedStoneSpecification.getSize())){
	 			            				StoneSpecification derivedNewStoneSpecification = getStoneSpecification(value[i], stoneSpecification);
	 			            				derivedcustomizationStoneSpec.setStoneSpecification(derivedNewStoneSpecification);
	 			            			}
									}
	 			            		customization.setCustomizationStoneSpecification(derivedCustomizationStoneSpecification);
								}
	 			            	
	 			            }
	 			        } catch (Exception e) {
	 			        	log.error("Error while updating the number of stones.", e);
	 			        	throw new ProductUploadException("Error while updating "+updateProperty.toString()+": " + value[i]);
	 			            
	 			        }
	 			    }
	 			}
	 	       updateMetaData.getCustomization().setCustomizationStoneSpecification(customizationStoneSpecification);
		}

	private StoneSpecification getStoneSpecification(String value,StoneSpecification stoneSpecification) throws Exception {
		
		StoneSpecification newStone = new StoneSpecification();
		newStone.setClarity(stoneSpecification.getClarity());
		newStone.setColor(stoneSpecification.getColor());
		newStone.setShape(stoneSpecification.getShape());
		newStone.setStoneType(stoneSpecification.getStoneType());
		newStone.setSize(value);
		newStone.setStoneWeight(pricingEngineService.getStoneFamilyWeight(newStone));
		StoneSpecification objectFromDB = stonespecificationdao.getStoneSpecifications(newStone);
		return objectFromDB;
	}
   

    @Override
    public void validate(UpdateProperty updateProperty, StoneUpdateMetaData updateMetaData) throws ProductUploadException {
        switch (updateProperty) {
            case No_Of_Stones:
            case Stone_Setting:
            case Stone_Size:
            	String[] value = (String[]) updateMetaData.getUpdatedValue();
            	if (updateMetaData.getDesign().getDesignStoneSpecifications().size() != value.length) {
            		throw new ProductUploadException("Missing "+updateMetaData.toString()+" property for stone");
            	}
                break;
            default:
                throw new ProductUploadException(ERROR_MESSAGE);
        }
    }

    
}
