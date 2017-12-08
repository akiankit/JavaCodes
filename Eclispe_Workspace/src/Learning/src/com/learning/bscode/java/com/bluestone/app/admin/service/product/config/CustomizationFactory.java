package com.bluestone.app.admin.service.product.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import antlr.collections.impl.Vector;

import com.bluestone.app.core.util.DataSheetConstants;
import com.bluestone.app.core.util.DesignConstants;
import com.bluestone.app.core.util.ProductValidatorUtil;
import com.bluestone.app.core.util.ValidatorConstants;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.MetalSpecificationDao;
import com.bluestone.app.design.dao.StoneSpecificationsDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationSizeMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.model.DesignMetalFamily;
import com.bluestone.app.design.model.DesignStoneSpecification;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.pricing.PricingEngineService;

@Component
public class CustomizationFactory {
    
    @Autowired
    private StoneSpecificationsDao stonespecificationdao;
    
    @Autowired
    private CustomizationDao       customizationdao;
    
    @Autowired
    private MetalSpecificationDao  metalpecificationdao;
    
    @Autowired
    private PricingEngineService   pricingEngineService;
    
    private static final Logger    log                                = LoggerFactory.getLogger(CustomizationFactory.class);


    public List<Customization> createAllCustomizations(Design design, Vector dataSheetVector, boolean update) throws ProductUploadException {
        log.info("CustomizationFactory.createAllCustomizations()");
        List<Customization> customizationsList = new ArrayList<Customization>();
        if (update) {
            customizationsList = design.getCustomizations();
        }
        int sheet = DataSheetConstants.dataGroups.get("customization");
        int row = DataSheetConstants.customization.get("start");
        
        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        for (int j = row; j < rowVectorHolder.size(); j++) {
            if (rowVectorHolder.elementAt(j) == null) {
                break;
            }
            StringBuilder shortDesc = new StringBuilder();
            Customization customization = new Customization();
            if (update) {
                for (Customization eachCustomization : customizationsList) {
                    if (eachCustomization.getPriority() == 1) {
                        customization = eachCustomization;
                    }
                }
            }
            customization.setIsActive(false);
            customization.setPriority(j);
            customization.setDesign(design);
            customization.setSheetIndex(j);
            customization.setProductType(PRODUCT_TYPE.JEWELLERY);
            List<CustomizationStoneSpecification> customizationStoneSpecificationsSet = validateAndCreateCustomizationStone(design, customization, dataSheetVector);
            if (customizationStoneSpecificationsSet == null) {
                return null;
            }
            setStoneDetails(customization, dataSheetVector, customizationStoneSpecificationsSet, shortDesc, update);
            
            // @todo : Rahul Agrawal : if the string has trailing spaces, then
            // we need to check if this will work or not.
            final String categoryType = design.getDesignCategory().getCategoryType().trim();
            //@TODO : Handle for design categorie with name not ending with 's'. 
            if(design.getDesignCategory().iSolitaireMountCategroy()){
            	shortDesc.append(categoryType);
            }
            else{
            	shortDesc.append(categoryType.substring(0, design.getDesignCategory().getCategoryType().length() - 1));
            }
            
            setMetalDetails(customization, dataSheetVector, shortDesc, update);
            
            customization.setCustomizationStoneSpecification(customizationStoneSpecificationsSet);
            
            // set Price
            customization.setPrice(getCustomizationPrice(customization));
            
            // setSkuCode
            customization.setSkuCode(getSKuCode(customization));
            
            // set short Desc
            customization.setShortDescription(shortDesc.toString());
            customizationsList.add(customization);
            
            if (update) {
                for (Customization eachCustomization : customizationsList) {
                    if (eachCustomization.getPriority() != 1) {
                        // customizationStoneSpecificationsSet =
                        // validateAndCreateCustomizationStone(design,
                        // customization, dataSheetVector, error);
                        setStoneDetails(customization, dataSheetVector, customizationStoneSpecificationsSet,  shortDesc, update);
                        setMetalDetails(customization, dataSheetVector, shortDesc, update);
                        
                        customization.setCustomizationStoneSpecification(customizationStoneSpecificationsSet);
                        
                        customization.setPrice(getCustomizationPrice(customization));
                        customizationsList.add(customization);
                    }
                }
            }
            break;
        }
        return customizationsList;
    }

    public BigDecimal getCustomizationPrice(Customization customization) {

        return BigDecimal.valueOf(pricingEngineService.getPrice(customization));

        /*log.info("CustomizationFactory.getCustomizationPrice() for SKU={}", customization.getSkuCode());
        BigDecimal price = BigDecimal.ZERO;
        try {
            price = BigDecimal.valueOf(pricingEngineService.getPrice(customization));
        } catch (Exception e) {
            String errorMessage = "Error fetching price from pricing engine. " + e.toString();
            log.error(errorMessage, e);
            throw new ProductUploadException(errorMessage);
        }
        log.info("CustomizationFactory.getCustomizationPrice() returned Price=[{}]", price);
        return price;*/
    }
    
    private void setMetalDetails(Customization customization, Vector dataSheetVector, StringBuilder shortDesc, boolean isCustomizationUpdate) throws ProductUploadException {
        log.info("CustomizationFactory.setMetalDetails()");
        
        List<CustomizationMetalSpecification> customizationMetalSpecifications = new LinkedList<CustomizationMetalSpecification>();
        List<MetalSpecification> metalInformation;
        if (isCustomizationUpdate) {
            metalInformation = customization.getMetalSpecificationsForUpdate();
        } else {
            metalInformation = getMetalInformation(dataSheetVector, customization.getSheetIndex(), shortDesc);
        }
        if (metalInformation == null) {
            throw new ProductUploadException("Error in finding metal information.");
        } else {
            int index = 0;
            for (MetalSpecification eachMetal : metalInformation) {
                if (eachMetal != null) {
                    log.info("CustomizationFactory.setMetalDetails(): Each Metal = {}", eachMetal.getFullname());
                    CustomizationMetalSpecification customizationMetalSpecification = new CustomizationMetalSpecification();
                    customizationMetalSpecification.setCustomization(customization);
                    
                    customizationMetalSpecification.setMetalSpecification(metalpecificationdao.getMetalSpecifications(eachMetal));
                    //final double weightCorrectionFactor = getWeightCorrectionFactor(customization);
                    BigDecimal metalWeight = getMetalWeight(dataSheetVector, index);
                    log.info("CustomizationFactory.setMetalDetails(): metal weight obtained={}", metalWeight);
                    customizationMetalSpecification.setMetalWeight(metalWeight);
                    
                    Design design = customization.getDesign();
                    List<DesignMetalFamily> designMetalFamilies = design.getDesignMetalFamilies();
                    for (DesignMetalFamily designMetalFamily : designMetalFamilies) {
                        if (designMetalFamily.getFamily() == (short) (index + 1)) {
                            customizationMetalSpecification.setDesignMetalFamily(designMetalFamily);
                            DesignCategory designCategory = design.getDesignCategory();
                            if(designCategory.isChains() || designCategory.isTanmaniyaChains()) {
                                getMetalSizeDelta(designMetalFamily, dataSheetVector, customizationMetalSpecification);
                            }
                        }
                    }
                    customizationMetalSpecifications.add(customizationMetalSpecification);
                    index++;
                }
            }
            
            customization.setCustomizationMetalSpecification(customizationMetalSpecifications);
        }
    }

    /**
     * @deprecated No longer applying the correction factor.
     *
     */
    private double getWeightCorrectionFactor(Customization customization) {
        double correctionFactor = 0.3d;
        final DesignCategory designCategory = customization.getDesign().getDesignCategory();
        if (designCategory.isTanmaniyaChains() || designCategory.isChains()) {
            correctionFactor = 0.0d;
        }
        log.info("CustomizationFactory.getWeightCorrectionFactor(): = {}", correctionFactor);
        return correctionFactor;
    }
    
    private List<CustomizationStoneSpecification> validateAndCreateCustomizationStone(Design design,
                                                                                      Customization customization,
                                                                                      Vector dataSheetVector) throws ProductUploadException {
        final String skuCode = customization.getSkuCode();
        log.info("CustomizationFactory.validateAndCreateCustomizationStone() for SKU={}", skuCode);
        List<CustomizationStoneSpecification> customizationStoneSpecs = new ArrayList<CustomizationStoneSpecification>();
        int sheet = DataSheetConstants.dataGroups.get("stone");
        int row = DataSheetConstants.stone.get("start");
        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        for (int j = row; j < rowVectorHolder.size(); j++) {
            if (rowVectorHolder.elementAt(j) == null) {
                break;
            }
            CustomizationStoneSpecification customizationStoneSpecification = new CustomizationStoneSpecification();
            customizationStoneSpecification.setCustomization(customization);
            
            String shape = (String) ExcelSheetHelper.getCell(dataSheetVector, sheet, j, DataSheetConstants.stone.get("cut"));
            log.info("CustomizationFactory.validateAndCreateCustomizationStone() for Sku={} Shape={}", skuCode, shape);
            
            String size = "";
            Cell cell = ExcelSheetHelper.getRawCell(dataSheetVector, sheet, j, DataSheetConstants.stone.get("size"));
            if (cell != null) {
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        size = cell.getRichStringCellValue().getString();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        Double Dsize = cell.getNumericCellValue();
                        size = Dsize.toString();
                        break;
                    default:
                        size = null;
                        break;
                }
            }
            if (shape != null) {
                shape = ProductValidatorUtil.validateStoneShape(shape);
            }
            if (shape == null && size == null) {
                log.info("CustomizationFactory.validateAndCreateCustomizationStone(): Shape and Size both are null.");
                break;
            }
            
            if (shape != null && size != null) {
                log.info("CustomizationFactory.validateAndCreateCustomizationStone(): Shape={} Size={}", shape, size);
                //TODO isn't it supposed to be lookup table, instead of creating a new record everytime
                StoneSpecification stoneSpecification = new StoneSpecification();
                stoneSpecification.setShape(shape);
                stoneSpecification.setSize(size);
                stoneSpecification.setIsActive((short) 1);
                final Integer cellIndex = DataSheetConstants.stone.get("solitaire_wt_range");
                try {
                    Object solitaireWtRange = ExcelSheetHelper.getCell(dataSheetVector, sheet, j, cellIndex);
                    SolitaireDataParser.parse(solitaireWtRange, stoneSpecification);
                } catch (Exception exception) {
                    throw new ProductUploadException("Issue in row=" + (j + 1) + " Col.=" + cellIndex, exception);
                }
                customizationStoneSpecification.setStoneSpecification(stoneSpecification);
            } else {
                log.info("CustomizationFactory.validateAndCreateCustomizationStone(): Shape and Size both are null. Not Considering row "
                        + (j + 4));
                break;
            }
            
            for (DesignStoneSpecification designSpec : design.getDesignStoneSpecifications()) {
                log.info("CustomizationFactory.validateAndCreateCustomizationStone(): Each DesignSpecification Setting Type={}", designSpec.getSettingType());
                if (designSpec.getSheetIndex() == j) {
                    customizationStoneSpecification.setDesignStoneSpecification(designSpec);
                }
            }
            customizationStoneSpecs.add(customizationStoneSpecification);
        }
        
        return customizationStoneSpecs;
    }
    
    // @todo : Rahul Agrawal : Rename this function to getStoneFamilyNames or
    // stoneNames
    public List<String> getStoneDetailsFromCustomizationSheet(Vector dataSheetVector, int sheet, int j, StringBuilder shortDesc) {
        log.info("CustomizationFactory.getStoneDetailsFromCustomizationSheet()-----");
        // List<String> stones = new ArrayList<String>();
        List<String> stones = new LinkedList<String>();
        Set<String> keySet = DataSheetConstants.stoneColumnIndex.keySet();
        int index = 0;
        log.info("CustomizationFactory.getStoneDetailsFromCustomizationSheet(): {}", keySet);
        for (String eachValue : keySet) {
            Integer eachStoneFamilyColumnIndex = DataSheetConstants.stoneColumnIndex.get(eachValue);
            String stoneName = (String) ExcelSheetHelper.getCell(dataSheetVector, sheet, j, eachStoneFamilyColumnIndex);
            log.info("CustomizationFactory.getStoneDetailsFromCustomizationSheet(): Stone Name=[{}]", stoneName);
            if (stoneName != null) {
           		stones.add(stoneName);
           		if(!stoneName.equalsIgnoreCase("solitaire")){
	                if (index == 0) {
	                    shortDesc.append(stoneName);
	                } else if (index == keySet.size()) {
	                    shortDesc.append(" And " + stoneName.trim());
	                } else {
	                    shortDesc.append(" , " + stoneName.trim());
	                }
	                index++;
           		}
            }
        }
        shortDesc.append(" ");
        return stones;
    }
    
    private String getMetalPurityInformation(Vector dataSheetVector, int sheetColumnIndex) {
        log.info("CustomizationFactory.getMetalPurityInformation()");
        Integer sheet = DataSheetConstants.dataGroups.get("customization");
        String purity = "";
        Object purityObject = ExcelSheetHelper.getCell(dataSheetVector, sheet, sheetColumnIndex, DataSheetConstants.customization.get("purity"));
        if (purityObject != null) {
            try {
                purity = (String) purityObject;
            } catch (ClassCastException e) {
                Double dPurity = (Double) purityObject;
                purity = dPurity.toString().substring(0, 2) + " Kt";
            }
        } else {
            purity = null;
        }
        return purity;
    }
    
    private List<MetalSpecification> getMetalInformation(Vector dataSheetVector, int sheetColumnIndex, StringBuilder shortDesc) throws ProductUploadException {
        log.info("CustomizationFactory.getMetalInformation()");
        List<MetalSpecification> metals = new ArrayList<MetalSpecification>();
        Set<String> metalIndexKeySet = DataSheetConstants.metalColumnIndex.keySet();
        Integer sheet = DataSheetConstants.dataGroups.get("customization");
        
        String metalPurityInformation = getMetalPurityInformation(dataSheetVector, sheetColumnIndex);
        int index = 0;
        for (String eachValue : metalIndexKeySet) {
            Integer eachMetalColumnIndex = DataSheetConstants.metalColumnIndex.get(eachValue);
            String metalType = (String) ExcelSheetHelper.getCell(dataSheetVector, sheet, sheetColumnIndex, eachMetalColumnIndex);
            if (metalType != null) {
                String[] metalTypeSplit = metalType.split("\\s");
                if (metalTypeSplit.length == 2) {
                    String metalColor = ProductValidatorUtil.validateMetalColor(metalTypeSplit[0], metalTypeSplit[1]);
                    if (metalColor == null) {
                        throw new ProductUploadException("Invalid metal color " + metalTypeSplit[0] + " for type " + metalTypeSplit[1]);
                    }
                    
                    String metalName = ProductValidatorUtil.validateMetalType(metalTypeSplit[1]);
                    if (metalName == null) {
                        throw new ProductUploadException("Invalid metal type " + metalTypeSplit[1]);
                    }
                    
                    Integer metalPurity = ProductValidatorUtil.validateMetalPurity(metalPurityInformation, metalTypeSplit[1]);
                    if (metalPurity == null) {
                        throw new ProductUploadException("Invalid metal purity " + metalPurity);
                    }
                    MetalSpecification metalSpecifications = new MetalSpecification();
                    metalSpecifications.setPurity(metalPurity);
                    metalSpecifications.setColor(metalColor);
                    metalSpecifications.setType(metalName);
                    if (index == 0) {
                        shortDesc = shortDesc.append(" In " + metalPurity + "Kt " + metalColor + " " + metalName);
                    } else {
                        shortDesc = shortDesc.append(" And " + metalPurity + "Kt " + metalColor + " " + metalName);
                    }
                    metalSpecifications.setFullname(metalPurity + "kt " + metalColor + " " + metalName);
                    metals.add(metalSpecifications);
                    /*
                     * if (i == 0) { customization.setMetal1Type(purity + " " +
                     * metal[0] + " " + metal[1]); shortDesc =
                     * shortDesc.concat(" In " + purity + " " + metal[0] + " " +
                     * metal[1]); } else if (i == 1) {
                     * customization.setMetal2Type(purity + " " + metal[0] + " "
                     * + metal[1]); shortDesc = shortDesc.concat(" And " +
                     * purity + " " + metal[0] + " " + metal[1]); }
                     */}
            }
        }
        if (metals.isEmpty() || metals.size() < 1 || metals.get(0) == null) {
            throw new ProductUploadException("Atleast one metal type need to be defined");
        }
        return metals;
    }


    private BigDecimal getMetalWeight(Vector dataSheetVector, int metalFamily) throws ProductUploadException {
        log.info("CustomizationFactory.getMetalWeight() for metal family={}", metalFamily);
        int row = DataSheetConstants.metal.get("start") + metalFamily;
        Integer cellIndex = DataSheetConstants.metal.get("weight");
        Integer sheet = DataSheetConstants.dataGroups.get("metal");
        Object metalWeight = ExcelSheetHelper.getCell(dataSheetVector,
                                                      sheet, 
                                                      row, 
                                                      cellIndex);
        if (metalWeight != null) {
            // Rahul Agrawal : Need to convert it to string so that BigDecimal
            // does not add unwanted precision points.
            try {
                Double metalWeight2 = (Double) metalWeight;
                BigDecimal weight = (BigDecimal.valueOf(metalWeight2));
                return weight;
            } catch (ClassCastException e) {
                throw new ProductUploadException("Metal Weight is not a number. Probably its using a formula. Please remove formula from there");
            }
        } else {
            throw new ProductUploadException("Metal Weight is missing. Expecting metal weight at sheet no : " + (sheet +1) + " rowno: " + (row +1 ) + " column:" +  (cellIndex +1));
        }
    }

    /**
     * @deprecated : No longer applying the correction factor.
     */
    private BigDecimal getMetalWeightWithCorrection(final double correctionFactor, Vector dataSheetVector, int metalFamily) {
        log.info("CustomizationFactory.getMetalWeightWithCorrection() for metal family={} , Correction Factor=[{}]", metalFamily, correctionFactor);
        Object metalWeight = ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("metal"), DataSheetConstants.metal.get("start")
                + metalFamily, DataSheetConstants.metal.get("weight"));
        if (metalWeight != null) {
            // Rahul Agrawal : Need to convert it to string so that BigDecimal
            // does not add unwanted precision points.
            log.info("CustomizationFactory.getMetalWeight(): Reducing the weight by a factor of {}", correctionFactor);
            BigDecimal weight = (BigDecimal.valueOf((Double) metalWeight)).multiply(BigDecimal.valueOf((1.0d - Double.valueOf(correctionFactor))));
            return weight;
        } else {
            return null;
        }
    }
    
    public void setStoneDetails(Customization customization, Vector dataSheetVector, List<CustomizationStoneSpecification> customizationStoneSpecificationsSet, StringBuilder shortDesc, boolean isCustomizationUpdate) throws ProductUploadException {
        log.info("CustomizationFactory.setStoneDetails(): Iterating over all the stones");
        int sheet = DataSheetConstants.dataGroups.get("customization");
        int row = customization.getSheetIndex();
        
        List<String> stoneFamilies = getStoneDetailsFromCustomizationSheet(dataSheetVector, sheet, row, shortDesc);
        log.info("CustomizationFactory.setStoneDetails(): StoneFamilies={}", stoneFamilies);
        for (CustomizationStoneSpecification aCustomizationStoneSpecification : customizationStoneSpecificationsSet) {
            StoneSpecification stoneSpecification = aCustomizationStoneSpecification.getStoneSpecification();
            log.info("CustomizationFactory.setStoneDetails(): StoneSpecification.StoneType=[{}]", stoneSpecification.getStoneType());
            if (isCustomizationUpdate) {
                short family = aCustomizationStoneSpecification.getDesignStoneSpecification().getFamily();
                stoneSpecification.setStoneType(customization.getStoneSpecificationsForUpdate().get(family).getStoneType());
                stoneSpecification.setClarity(customization.getStoneSpecificationsForUpdate().get(family).getClarity());
                stoneSpecification.setColor(customization.getStoneSpecificationsForUpdate().get(family).getColor());
                getStoneWeight(aCustomizationStoneSpecification, stoneSpecification);
                
            } else {
                String stoneFamilyType = stoneFamilies.get(aCustomizationStoneSpecification.getDesignStoneSpecification().getFamily() - 1);
                if (stoneFamilyType != null) {
                    String stoneName = ProductValidatorUtil.validateStoneType(stoneFamilyType);
                    if (stoneName == null) {
                        throw new ProductUploadException("Invalid stone type " + stoneFamilyType);
                    } else {
                        stoneSpecification.setStoneType(stoneName);
                        if (stoneName.contains("Diamond")) {
                            stoneSpecification.setClarity(ValidatorConstants.DEFAULT_CLARITY);
                            log.info("CustomizationFactory.setStoneDetails(): StoneName is Diamond , setting the clarity to SI (default value).");
                        }
                        stoneSpecification.setColor(ValidatorConstants.stoneColors.get(stoneName.toLowerCase()));
                        log.info("CustomizationFactory.setStoneDetails(): Colour of the Stone={}", stoneSpecification.getColor());
                    }
                    
                    getStoneWeight(aCustomizationStoneSpecification, stoneSpecification);
                } else {
                    throw new ProductUploadException("Invalid stone reference : "
                            + aCustomizationStoneSpecification.getDesignStoneSpecification().getFamily());
                }
            }
            
        }
    }
    
    private void getStoneWeight(CustomizationStoneSpecification aCustomizationStoneSpecification, StoneSpecification stoneSpecification)
            throws ProductUploadException {
        try {
        	stoneSpecification.setStoneWeight(pricingEngineService.getStoneFamilyWeight(stoneSpecification));
            final StoneSpecification objectFromDB = stonespecificationdao.getStoneSpecifications(stoneSpecification);
            aCustomizationStoneSpecification.setStoneSpecification(objectFromDB);
        } catch (Exception e) {
            log.error("Error: CustomizationFactory.setStoneDetails(): {}", e.toString());
            throw new ProductUploadException("Failure while processing the stone weight : Cause=" + e.toString(), Throwables.getRootCause(e));
        }
    }
    
    public static String getSKuCode(Customization customization) {
        log.info("CustomizationFactory.getSKuCode() for customizationSKU={}", customization.getSkuCode());
        String skuCode = "AAA18STF1STF2STF3_ABCD00";
        // Metal Codes - AAA, Stone Families - STF1 - STF2 - STF3, all metals
        // have same purity
        
        int purity = 18;
        List<CustomizationMetalSpecification> customizationMetalSpecifications = customization.getCustomizationMetalSpecification();
        if (customizationMetalSpecifications != null) {
            for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
                MetalSpecification metalSpecification = customizationMetalSpecification.getMetalSpecification();
                skuCode = StringUtils.replaceOnce(skuCode, "A", getMetalCode(metalSpecification.getColor().toLowerCase()
                        + " " + metalSpecification.getType().toLowerCase()));
                purity = metalSpecification.getPurity();
            }
            skuCode = StringUtils.replaceOnce(skuCode, "18", purity + "");
            log.info("CustomizationFactory.getSKuCode(): Derived SKU={}", skuCode);
        }
        
        List<CustomizationStoneSpecification> customizationStoneSpecifications = customization.getCustomizationStoneSpecification();
        if (customizationStoneSpecifications != null) {
            for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecifications) {
                short family = customizationStoneSpecification.getDesignStoneSpecification().getFamily();
                StoneSpecification stoneSpecification = customizationStoneSpecification.getStoneSpecification();
                if (family == 1) {
                    skuCode = StringUtils.replaceOnce(skuCode, "STF1", getStoneCode(stoneSpecification));
                } else if (family == 2) {
                    skuCode = StringUtils.replaceOnce(skuCode, "STF2", getStoneCode(stoneSpecification));
                } else if (family == 3) {
                    skuCode = StringUtils.replaceOnce(skuCode, "STF3", getStoneCode(stoneSpecification));
                }
            }
            skuCode = StringUtils.replaceOnce(skuCode, "STF1", "XXXX");
            skuCode = StringUtils.replaceOnce(skuCode, "STF2", "XXXX");
            skuCode = StringUtils.replaceOnce(skuCode, "STF3", "XXXX");
        }
        skuCode = customization.getDesign().getDesignCode() + "_" + skuCode;
        log.info("CustomizationFactory.getSKuCode(customization) returned SkuCode=[{}]", skuCode);
        return new String(skuCode);
    }
    
    private static String getStoneCode(StoneSpecification stoneSpecification) {
        String stoneCode = "XXXX";
        String stoneType = stoneSpecification.getStoneType();
        if (StringUtils.isBlank(stoneType)) {
            return stoneCode;
        }
        if (stoneType.toLowerCase().contains("diamond")) {
            // Assumption: always Diamond in "SI GH" and code is G4
            stoneCode = DesignConstants.stoneCodes.get("diamond_" + stoneSpecification.getClarity() + "_"
                    + stoneSpecification.getColor());
            // stoneCode = DesignConstants.stoneCodes.get("diamond") + "G4";
        } else {
            stoneCode = DesignConstants.stoneCodes.get(stoneType.toLowerCase());
        }
        stoneCode = stoneCode == null ? "XXXX" : stoneCode;
        return stoneCode;
    }
    
    private static String getMetalCode(String metalType) {
        String metalCode = "A";// Default Character for Metal Code
        
        if (StringUtils.isBlank(metalType)) {
            return metalCode;
        }
        metalCode = DesignConstants.metalCodes.get(metalType.trim());
        metalCode = metalCode == null ? "A" : metalCode;
        return metalCode;
    }
    
/*    private StoneSpecification getStoneSpecification(StoneSpecification stoneSpecification, String stoneFamilyType) throws ProductUploadException {
        log.info("CustomizationFactory.getStoneSpecification()");
        StoneSpecification stoneSpec = new StoneSpecification();
        stoneSpec.setShape(stoneSpecification.getShape());
        stoneSpec.setSize(stoneSpecification.getSize());
        if (stoneFamilyType != null) {
            String stoneName = ProductValidatorUtil.validateStoneType(stoneFamilyType);
            if (stoneName == null) {
                throw new ProductUploadException("Invalid stone type " + stoneFamilyType);
            } else {
                stoneSpec.setStoneType(stoneName);
                if (stoneName.contains("Diamond")) {
                    stoneSpec.setClarity(ValidatorConstants.DEFAULT_CLARITY);
                }
                stoneSpec.setColor(ValidatorConstants.stoneColors.get(stoneName.toLowerCase()));
            }
            try {
                stoneSpec = stonespecificationdao.getStoneSpecifications(stoneSpec);
                stoneSpec.setStoneWeight(pricingEngineService.getStoneFamilyWeight(stoneSpec));
            } catch (Exception e) {
                throw new ProductUploadException("Error while fetching stone weight : " + e.toString());
            }
        }
        return stoneSpec;
    }
*/    
/*    private List<CustomizationStoneSpecification> getCustomizationStoneSpecification(List<CustomizationStoneSpecification> customizationStoneSpecP1, Customization customization, Map<String, Object> stoneFamily) throws ProductUploadException {
        log.info("CustomizationFactory.getCustomizationStoneSpecification()");
        List<CustomizationStoneSpecification> customizationStoneSpecifications = new ArrayList<CustomizationStoneSpecification>();
        for (CustomizationStoneSpecification customizationStoneSpecification : customizationStoneSpecP1) {
            String key = "stone" + customizationStoneSpecification.getDesignStoneSpecification().getFamily();
            if (stoneFamily.get(key) != null) {
                String family = (String) stoneFamily.get(key);
                CustomizationStoneSpecification customizationStoneSpec = new CustomizationStoneSpecification();
                customizationStoneSpec.setCustomization(customization);
                customizationStoneSpec.setDesignStoneSpecification(customizationStoneSpecification.getDesignStoneSpecification());
                customizationStoneSpec.setStoneSpecification(getStoneSpecification(customizationStoneSpecification.getStoneSpecification(), family));
                customizationStoneSpecifications.add(customizationStoneSpec);
            } else {
                throw new ProductUploadException("Unable to find metal for family "
                        + customizationStoneSpecification.getDesignStoneSpecification().getFamily());
            }
            
        }
        return customizationStoneSpecifications;
    }*/
    
    private MetalSpecification getMetalSpecification(MetalSpecification metalSpecification, String[] metalFamily) {
        log.info("CustomizationFactory.getMetalSpecification()");
        MetalSpecification metalSpec = new MetalSpecification();
        metalSpec.setPurity(Integer.parseInt(metalFamily[0]));
        metalSpec.setColor(metalFamily[1]);
        metalSpec.setType(metalFamily[2]);
        metalSpec.setCode(metalFamily[3]);
        metalSpec.setFullname(metalFamily[0] + " Kt " + metalFamily[1] + " " + metalFamily[2]);
        return metalpecificationdao.getMetalSpecifications(metalSpec);
    }
    
    private List<CustomizationMetalSpecification> getCustomizationMetalSpecifications(List<CustomizationMetalSpecification> customizationMetalSpecP1, Customization customization, Map<String, Object> metalFamily) throws ProductUploadException {
        log.info("CustomizationFactory.getCustomizationMetalSpecifications()");
        List<CustomizationMetalSpecification> customizationMetalSpecifications = new ArrayList<CustomizationMetalSpecification>();
        for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecP1) {
            if (metalFamily.get("metal" + customizationMetalSpecification.getDesignMetalFamily().getFamily()) != null) {
                String[] family = (String[]) metalFamily.get("metal"
                        + customizationMetalSpecification.getDesignMetalFamily().getFamily());
                CustomizationMetalSpecification customizationMetalSpec = new CustomizationMetalSpecification();
                customizationMetalSpec.setCustomization(customization);
                customizationMetalSpec.setDesignMetalFamily(customizationMetalSpecification.getDesignMetalFamily());
                customizationMetalSpec.setMetalWeight(customizationMetalSpecification.getMetalWeight());
                customizationMetalSpec.setMetalSpecification(getMetalSpecification(customizationMetalSpecification.getMetalSpecification(), family));
                customizationMetalSpecifications.add(customizationMetalSpec);
            } else {
                throw new ProductUploadException("Unable to find metal for family "
                        + customizationMetalSpecification.getDesignMetalFamily().getFamily());
            }
        }
        return customizationMetalSpecifications;
    }
    
    /*public Customization addCustomization(Design design, Customization customizationP1, String filename, short priority) throws ProductUploadException {
        log.info("CustomizationFactory.addCustomization()");
        Customization customization = new Customization();
        customization.setIsActive(true);
        customization.setPriority(priority);
        customization.setDesign(design);
        Map<String, Object> extractCustomizationDetails = extractCustomizationDetails(filename, design.getDesignCategory().getCategoryType());
        customization.setCustomizationMetalSpecification(getCustomizationMetalSpecifications(customizationP1.getCustomizationMetalSpecification(), customization, extractCustomizationDetails));
        customization.setCustomizationStoneSpecification(getCustomizationStoneSpecification(customizationP1.getCustomizationStoneSpecification(), customization, extractCustomizationDetails));
        customization.setShortDescription((String) extractCustomizationDetails.get("shortDesc"));
        // set Price
        customization.setPrice(getCustomizationPrice(customization));
        
        // setSkuCode
        customization.setSkuCode(getSKuCode(customization));
        return customizationdao.getCustomizationBySku(customization);
    }*/
    
    public Map<String, Object> extractCustomizationDetails(String filename, String category) throws ProductUploadException {
        log.info("CustomizationFactory.extractCustomizationDetails()");
        Map<String, Object> details = new HashMap<String, Object>();
        StringBuilder shortDesc = new StringBuilder();
        String[] properties = filename.split("_");
        for (int i = 2; i < properties.length; i++) {
            if (!properties[i].equalsIgnoreCase("XXXX")) {
                String stoneName = getStoneDetail(properties[i]);
                details.put("stone" + (i - 1), stoneName);
                if (stoneName != null) {
                    if (i == 2) {
                        shortDesc.append(stoneName);
                    } else if (i == properties.length - 1) {
                        shortDesc.append(" And " + stoneName.trim());
                    } else {
                        shortDesc.append(" , " + stoneName.trim());
                    }
                } else {
                    throw new ProductUploadException("Could not find stone: " + properties[i]);
                }
            }
        }
        shortDesc.append(" " + category.trim().substring(0, category.length() - 1));
        for (int i = 0; i < 2; i++) {
            if (!properties[i].equalsIgnoreCase("XXX")) {
                String[] metalDetail = getMetalDetail(properties[i]);
                
                if (metalDetail != null) {
                    
                    details.put("metal" + (i + 1), metalDetail);
                    if (i == 0) {
                        shortDesc = shortDesc.append(" In " + metalDetail[0] + "Kt " + metalDetail[1] + " "
                                + metalDetail[2]);
                    } else {
                        shortDesc = shortDesc.append(" And " + metalDetail[0] + "Kt " + metalDetail[1] + " "
                                + metalDetail[2]);
                    }
                } else {
                    throw new ProductUploadException("Could not find metal: " + properties[i]);
                }
            }
        }
        details.put("shortDesc", shortDesc.toString());
        return details;
    }
    
    private String getStoneDetail(String stoneCode) {
        String stone = null;
        stone = searchInMap(DesignConstants.stoneCodes, stoneCode.trim());
        stone = ProductValidatorUtil.validateStoneType(stone);
        return stone;
        
    }
    
    private String[] getMetalDetail(String metalCode) {
        String[] metal = new String[4];
        metal[0] = metalCode.trim().substring(0, 2);
        String metalName = searchInMap(DesignConstants.metalCodes, metalCode.trim().substring(2));
        if (metalName != null) {
            String[] metalSplit = metalName.split("\\s");
            metal[1] = ProductValidatorUtil.validateMetalColor(metalSplit[0], metalSplit[1]);
            metal[2] = ProductValidatorUtil.validateMetalType(metalSplit[1]);
            metal[3] = metalCode;
        } else {
            return null;
        }
        return metal;
    }
    
    private String searchInMap(Map<String, String> data, String value) {
        String ret = null;
        for (Entry<String, String> entry : data.entrySet()) {
            if (entry.getValue().equals(value)) {
                ret = entry.getKey();
            }
        }
        return ret;
    }
    
    private List<CustomizationSizeMetalSpecification> getMetalSizeDelta(DesignMetalFamily designMetalFamily, Vector dataSheetVector, CustomizationMetalSpecification customizationMetalSpecification) throws ProductUploadException {
        log.debug("CustomizationFactory.getMetalSizeDelta()");
        List<CustomizationSizeMetalSpecification> designSizeMetalSpecifications = new ArrayList<CustomizationSizeMetalSpecification>();
        int sheet = DataSheetConstants.dataGroups.get("size");
        int stoneListEndIndex = getStoneListEndIndex(dataSheetVector);
        int row = stoneListEndIndex + DataSheetConstants.size.get("rowAdd");
        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        
        for (int eachRowNo = row; eachRowNo < rowVectorHolder.size(); eachRowNo++) {
            if (rowVectorHolder.elementAt(eachRowNo) == null) {
                break;
            }
            Integer cellIndex = DataSheetConstants.size.get("size");
            Object size = ExcelSheetHelper.getCell(dataSheetVector, sheet, eachRowNo, cellIndex);
            
            if (size == null) break;
            if(!(size instanceof Double)) {
                throw new ProductUploadException("Size is not a number. Value from sheet:" + size.toString());
            }
            Double dSize = (Double) size;
            
            Integer eachMetalColumnIndex = DataSheetConstants.metalWeightIndex.get("metal " + designMetalFamily.getFamily());
            Object cell = ExcelSheetHelper.getCell(dataSheetVector, sheet, eachRowNo, eachMetalColumnIndex);
            if (cell != null) {
                CustomizationSizeMetalSpecification customizationSizeMetalSpecification = new CustomizationSizeMetalSpecification();
                customizationSizeMetalSpecification.setSize(((Integer) dSize.intValue()).toString());
                BigDecimal weight = BigDecimal.valueOf((Double) cell);
                
                Object unit = ExcelSheetHelper.getCell(dataSheetVector, sheet, eachRowNo, eachMetalColumnIndex + 1);
                BigDecimal metalWeight = getMetalWeight(dataSheetVector, designMetalFamily.getFamily() - 1, weight);
                if (metalWeight != null) {
                    customizationSizeMetalSpecification.setDelta(metalWeight);
                } else {
                    throw new ProductUploadException("Error while calculating delta ");
                }
                if (unit == null) {
                    customizationSizeMetalSpecification.setMetalUnit(DataSheetConstants.units.get("default"));
                } else {
                    if (DataSheetConstants.units.get((String) unit) != null) {
                        customizationSizeMetalSpecification.setMetalUnit(DataSheetConstants.units.get((String) unit));
                    } else {
                        throw new ProductUploadException("Invalid unit for metal weight");
                    }
                }
                customizationSizeMetalSpecification.setCustomizationMetalSpecification(customizationMetalSpecification);
                designSizeMetalSpecifications.add(customizationSizeMetalSpecification);
            }
            
        }
        
        return designSizeMetalSpecifications;
    }
    
    private int getStoneListEndIndex(Vector dataSheetVector) {
        log.debug("DesignFactory.getStoneListEndIndex()");
        int sheet = DataSheetConstants.dataGroups.get("stone");
        int row = DataSheetConstants.stone.get("start");
        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        int j;
        for (j = row; j < rowVectorHolder.size(); j++) {
            if (rowVectorHolder.elementAt(j) == null) {
                break;
            }
            Object oNumbers = ExcelSheetHelper.getCell(dataSheetVector, sheet, j, DataSheetConstants.stone.get("numbers"));
            String setting = (String) ExcelSheetHelper.getCell(dataSheetVector, sheet, j, DataSheetConstants.stone.get("setting"));
            Object oFamily = ExcelSheetHelper.getCell(dataSheetVector, sheet, j, DataSheetConstants.stone.get("family"));
            if (oNumbers == null && setting == null && oFamily == null) {
                break;
            }
        }
        return j;
    }
    
    private BigDecimal getMetalWeight(Vector dataSheetVector, int metalFamily, BigDecimal sizeWeight) {
        log.debug("DesignFactory.getMetalWeight() metalFamily {} , sizeWeight {}" , metalFamily, sizeWeight);
        Object metalWeight = ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("metal"), DataSheetConstants.metal.get("start")
                + metalFamily, DataSheetConstants.metal.get("weight"));
        if (metalWeight != null) {
            BigDecimal weight = BigDecimal.valueOf((Double) metalWeight);
            return sizeWeight.subtract(weight); // substract with base
        } else {
            return null;
        }
    }
}
