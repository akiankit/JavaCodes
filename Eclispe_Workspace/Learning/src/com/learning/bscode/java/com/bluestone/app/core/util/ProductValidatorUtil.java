package com.bluestone.app.core.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductValidatorUtil {

    private static final Logger log = LoggerFactory.getLogger(ProductValidatorUtil.class);

	public static String validateMetalType(String metalType){
		String type = metalType.trim().toLowerCase();
		if(ValidatorConstants.findings.get(type)!=null){
			return ValidatorConstants.findings.get(type);
		}else if(type.equalsIgnoreCase("gold") || type.equalsIgnoreCase("silver") || type.equalsIgnoreCase("platinum")){
			return Character.toUpperCase(type.charAt(0)) + type.substring(1);
		}else{
			return null;
		}
	}
	
	public static String validateMetalColor(String metalColor,String metalType){
		String color = metalColor.trim().toLowerCase();
		String type = metalType.trim().toLowerCase();
		if(type.equalsIgnoreCase("silver")){
			if(!color.equalsIgnoreCase("white")){
				return null;
			}else{
				return Character.toUpperCase(color.charAt(0)) + color.substring(1);
			}
		}/*else if(ValidatorConstants.findings.get(type)!=null){
			return "";
		}*/else if(ValidatorConstants.metalColors.get(color) != null){
			return ValidatorConstants.metalColors.get(color);
		}else{
			return null;
		}
	}
	
	public static Integer validateMetalPurity(String metalPurity,String metalType){
	    if(metalPurity == null)
	        return null;
		String type = metalType.trim().toLowerCase();
		String purity = metalPurity.trim().toLowerCase();
		if(purity.contains("kt")){
			purity = purity.replace("kt", " ");
			purity = purity.trim().toLowerCase();
		}
		if(type.equalsIgnoreCase("silver")){
			if(!purity.equalsIgnoreCase("0.925")){
				return null;
			}else{
			    return Integer.parseInt(purity);
			}
		}else if(!purity.equalsIgnoreCase("14") && !purity.equalsIgnoreCase("18")){
			return null;
		}
		return Integer.parseInt(purity);
	}
	
	public static String validateStoneType(String stoneType){
        log.info("ProductValidatorUtil.validateStoneType(): stoneType={}", stoneType);
		String stone = stoneType.trim().toLowerCase();
		if(ValidatorConstants.stoneTypes.containsKey(stone)){
			return ValidatorConstants.stoneTypes.get(stone);
		}
		return null;
	}
	
	public static String validateStoneShape(String stoneShape){
        log.info("ProductValidatorUtil.validateStoneShape() for {}", stoneShape);
		String shape = stoneShape.trim().toLowerCase();
		if(ValidatorConstants.stoneShapes.get(shape)!=null){
			return ValidatorConstants.stoneShapes.get(shape);
		}
		return null;
	}
	public static String validateStoneSettingType(String settingType){
		String setting = settingType.trim().toLowerCase();
		if(ValidatorConstants.settingTypes.get(setting)!=null){
			return ValidatorConstants.settingTypes.get(setting);
		}
		return null;
	}
	
	public static String getStoneColor(String color){
		String stoneColor = color.trim().toLowerCase();
		if(ValidatorConstants.stoneColors.get(stoneColor)!=null){
			return ValidatorConstants.stoneColors.get(stoneColor);
		}
		return "";
	}
	
	public static Map<Integer,String> validateCustomization(String[] metals,String[] stones,String purity){
		Map<Integer,String> errors = new HashMap<Integer,String>();
		int errorIndex = 0;
		
		
		return errors;
	}
	
	
	
}
