package com.bluestone.app.admin.model;

import java.util.LinkedList;
import java.util.List;

public enum ProductHeaderEnum {
	DESIGN_ID("Design Id"),
    DESIGN_CODE("Design Code"),
    DESIGN_CATEGORY("Design Category"),
    DESIGN_DESCRIPTION("Long Description"),
    DESIGN_URL("Design Url"),
    
    CUSTOMIZATION_ID("Customization Id"),
    CUSTOMIZATION_SKU("Sku Code"),
    CUSTOMIZATION_SHORT_DESCRIPTION("Short Description"),
    CUSTOMIZATION_PRIORITY("Priority"),
    
    METAL_TYPE("Metal Type"),
    METAL_WEIGHT("Metal Weight"),
    METAL_COLOR("Metal Color"),
    
    PRICE("Price"),
    TOTAL_WEIGHT("Product Weight"),
    
    STONE_SIZE("Stone Size"),
    STONE_TYPE("Stone Type"),
    STONE_WEIGHT("Stone Weight"),
    STONE_QUANTITY("No. Of Stones"),
    STONE_SETTING_TYPE("Stone Setting Type"),
    STONE_SHAPE("Stone Shape"),
    
    DIAMOND_COLOR("Diamond Color"),
    DIAMOND_CLARITY("Diamond Clarity"),
    DIAMOND_SIZE("Diamond Size"),
    DIAMOND_WEIGHT("Diamond Weight"),
    DIAMOND_QUANTITY("No. Of diamonds"),
    DIAMOND_SETTING_TYPE("Diamond Setting Type"),
    DIAMOND_SHAPE("Diamond Shape");
    
    private String headerName;
    
    private ProductHeaderEnum(String headerName){
        this.headerName=headerName;
    }

    public String getHeaderName() {
        return this.headerName; 
    }
    
	public static ProductHeaderEnum getHeader(String columnName) {
		ProductHeaderEnum headerEnum= null;
		ProductHeaderEnum[] headerEnums = ProductHeaderEnum.values();
		for (ProductHeaderEnum productHeaderEnum : headerEnums) {
			if(productHeaderEnum.toString().equalsIgnoreCase(columnName)){
				headerEnum =  productHeaderEnum;
				break;
			}
		}
		return headerEnum;
	}
	
	public static LinkedList<ProductHeaderEnum> getOrderedHeaders(List<ProductHeaderEnum> headerNames){
		ProductHeaderEnum[] headerEnums = ProductHeaderEnum.values();
		LinkedList<ProductHeaderEnum> productHeaders2 = new LinkedList<ProductHeaderEnum>();
		LinkedList<ProductHeaderEnum> productHeaders = new LinkedList<ProductHeaderEnum>();
		for (ProductHeaderEnum productHeaderEnum : headerEnums) {
			productHeaders.add(productHeaderEnum);
		}
		for (ProductHeaderEnum productHeaderEnum : productHeaders) {
			if(headerNames.contains(productHeaderEnum)){
				productHeaders2.add(productHeaderEnum);
			}
		}
		return productHeaders2;
	}
	
}
