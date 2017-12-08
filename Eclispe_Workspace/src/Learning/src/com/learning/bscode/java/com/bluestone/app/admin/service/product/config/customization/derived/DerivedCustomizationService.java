package com.bluestone.app.admin.service.product.config.customization.derived;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.SkuCodeFactory;
import com.bluestone.app.admin.service.product.config.CustomizationFactory;
import com.bluestone.app.admin.service.product.config.MetalWeightConstant;
import com.bluestone.app.admin.service.product.config.ProductUploadException;
import com.bluestone.app.core.util.DesignConstants;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.dao.MetalSpecificationDao;
import com.bluestone.app.design.dao.StoneSpecificationsDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.pricing.PricingEngineService;
import com.google.common.base.Throwables;

@Service
public class DerivedCustomizationService {
    
    @Autowired
    private PricingEngineService pricingEngineService;
    
    @Autowired
    private CustomizationDao customizationDao;
    
    @Autowired
    private CustomizationFactory customizationFactory;
    
    @Autowired
    private StoneSpecificationsDao stoneSpecificationsDao;
    
    @Autowired
    private MetalSpecificationDao metalSpecificationDao;

    @Autowired
    private DesignDao designDao;
    
    @Autowired
    private DerivedCustomizationIntegrationProcessor derivedCustomizationIntegrationProcessor;

    Integer[]                                          metalCombinations                      = new Integer []{14,18,22};
    
    String                                          customizationStoneCombinations         = "VVS:EF,VS:GH,SI:GH";

    
    private static final Logger log = LoggerFactory.getLogger(DerivedCustomizationService.class);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Set<Customization> createDerivedCustomization(Customization parentCustomization) throws ProductUploadException {
        log.info("DerivedCustomizationService.createDerivedCustomization() for ParentCustomization: Id[{}] Sku=[{}]",
                 parentCustomization.getId(), parentCustomization.getSkuCode());

        Design design = parentCustomization.getDesign();
        long designId = design.getId();
        Set<Customization> derivedCustomizationList = new HashSet<Customization>();
        try {
            DesignCategory designCategory = design.getDesignCategory();
            if (designCategory.isChains() || designCategory.isTanmaniyaChains()) {
                throw new ProductUploadException("Derived Customizations not supported for chains and tanmaniya chains");
            } else {
                log.info("Starting to create derived customizations for ParentCustomizationId=[{}] DesignId=[{}]", parentCustomization.getId(), designId);
                derivedCustomizationList = createCustomizations(parentCustomization);
                design.setIsCustomizationSupported((short) 1);

                derivedCustomizationIntegrationProcessor.pushToS3AndUniware(parentCustomization, derivedCustomizationList);

                for (Customization derivedCustomization : derivedCustomizationList) {
                    customizationDao.create(derivedCustomization);
                }
                designDao.update(design);
            }
        } catch (Exception e) {
            Throwables.propagateIfInstanceOf(e, ProductUploadException.class);
            throw new ProductUploadException( "ERROR during creation of derived customizations for customizationId="
                                              + parentCustomization.getId() +"Reason="
                                              + e.getMessage(), e);
        }
        return derivedCustomizationList;
    }

    private Set<Customization> createCustomizations(Customization parentCustomization) throws Exception {
        log.info("DerivedCustomizationService.createCustomizations()");
        Set<Customization> derivedCustomizationList = new HashSet<Customization>();
        for (Integer eachMetalPurity : metalCombinations) {
            List<CustomizationStoneSpecification> customizationStoneSpecification = parentCustomization.getCustomizationStoneSpecification();
            List<CustomizationMetalSpecification> parentCustomizationMetalSpecification = parentCustomization.getCustomizationMetalSpecification();
            if(customizationStoneSpecification.isEmpty()) {
                // pure metal
                if (eachMetalPurity == 14) {
                    derivedCustomizationList.add(generateOnlyMetalCombinations(parentCustomization, eachMetalPurity));
                }
                if (eachMetalPurity == 22) {
                    // for rose or plain white/rose gold don't generate 22KT customization
                    boolean isWhileOrRoseGoldProduct = isWhileOrRoseGoldProduct(parentCustomizationMetalSpecification);
                    boolean isSolitaireCategory = parentCustomization.getDesign().getDesignCategory().iSolitaireMountCategroy();
                    
                    if(isWhileOrRoseGoldProduct || isSolitaireCategory) {
                        // dont generate 22 kt for solitaire categories and white and rose gold
                    } else {
                        derivedCustomizationList.add(generateOnlyMetalCombinations(parentCustomization, eachMetalPurity));
                    }
                }
                continue;
            }
            
            boolean isDiamondProduct = isDiamondProduct(customizationStoneSpecification);
            if (!isDiamondProduct) {
                // studded & non diamond 
                if (eachMetalPurity == 14) {
                    derivedCustomizationList.add(generateMetalAndStoneCombinations(parentCustomization, eachMetalPurity, customizationStoneSpecification));
                }
                continue;
            }
            // diamond product
            boolean heartOrBaguetteDiamondProduct = isHeartOrBaguetteDiamondProduct(customizationStoneSpecification);
            if (isDiamondProduct) {
                if (eachMetalPurity == 14) {
                    if (heartOrBaguetteDiamondProduct) {
                        // 14 SI-GH - for heart and bagutte
                        derivedCustomizationList.add(generateMetalAndStoneCombinations(parentCustomization, eachMetalPurity, customizationStoneSpecification));
                    } else {
                        // for round and princess
                        // 14 VVS EF, 14 VSGH,14 SIGH
                        derivedCustomizationList.addAll(generateStoneCombinations(parentCustomization, eachMetalPurity));
                    }
                }
                if (eachMetalPurity == 18) {
                    if (heartOrBaguetteDiamondProduct) {
                        // 18 SI-GH - which is already present So dont do anything
                    } else {
                        // 18 VVS EF, 18 VSGH, for round and princess
                        derivedCustomizationList.addAll(generateStoneCombinations(parentCustomization, eachMetalPurity));
                    }
                }
             }
        }
        return derivedCustomizationList;
    }
    
    private Customization generateOnlyMetalCombinations(Customization parentCustomization, Integer eachMetalPurity) throws Exception {
        Customization newCustomization = null;
        List<CustomizationMetalSpecification> parentCustomizationMetalSpecification = parentCustomization.getCustomizationMetalSpecification();
        try {
            log.debug("Starting for customization=[{}] each metalSpec=[{}]", parentCustomization.getId(), eachMetalPurity);
            newCustomization = createNewCustomization(parentCustomization);
            createCustomizationMetalSpecifications(newCustomization, parentCustomizationMetalSpecification, eachMetalPurity);
            setPriceAndSkuCode(newCustomization, eachMetalPurity);
            return newCustomization;
        } catch (Exception e) {
            throw new ProductUploadException(e.getMessage(), e);
        }
    }

    private void createCustomizationMetalSpecifications(Customization newCustomization, List<CustomizationMetalSpecification> parentCustomizationMetalSpecification, int eachMetalPurity) {
        List<CustomizationMetalSpecification> cmsList = new ArrayList<CustomizationMetalSpecification>();
        
        for (CustomizationMetalSpecification eachCMS : parentCustomizationMetalSpecification) {
            MetalSpecification parentMetalSpecification = eachCMS.getMetalSpecification();
            CustomizationMetalSpecification newCustomizationMetalSpecification = new CustomizationMetalSpecification();
            String color = parentMetalSpecification.getColor();
            String type = parentMetalSpecification.getType();
            MetalSpecification metalSpecification = metalSpecificationDao.getMetalSpecification(color, eachMetalPurity, type);
            if(metalSpecification == null) {
                metalSpecification = new MetalSpecification();
                metalSpecification.setColor(color);
                metalSpecification.setPurity(eachMetalPurity);
                metalSpecification.setType(type);
                String fullName = eachMetalPurity + "Kt " + color + " " + type;
                metalSpecification.setFullname(fullName);
            }
            newCustomizationMetalSpecification.setMetalSpecification(metalSpecification);
            BigDecimal metalWeight = getMetalWeight(eachMetalPurity, eachCMS);
            newCustomizationMetalSpecification.setMetalWeight(metalWeight);
            newCustomizationMetalSpecification.setCustomization(newCustomization);
            newCustomizationMetalSpecification.setDesignMetalFamily(eachCMS.getDesignMetalFamily());
            cmsList.add(newCustomizationMetalSpecification);
        }
        newCustomization.setCustomizationMetalSpecification(cmsList);
    }

    private BigDecimal getMetalWeight(int eachMetalPurity, CustomizationMetalSpecification eachCMS) {
        BigDecimal metalWeight = eachCMS.getMetalWeight();
        switch (eachMetalPurity) {
            case 14:
                metalWeight = metalWeight.multiply(MetalWeightConstant.MultiplyFactor14K);
                break;
            case 22:
                metalWeight = metalWeight.multiply(MetalWeightConstant.MultiplyFactor22K);
                break;
            default:
                break;
        }
        log.debug("New Metal Weight {} for purity {} ", metalWeight, eachMetalPurity);
        return metalWeight;
    }
    
    private Customization generateMetalAndStoneCombinations(Customization parentCustomization, int eachMetalPurity, List<CustomizationStoneSpecification> parentCustomizationStoneSpecification)
            throws Exception {
        try {
            log.debug("Starting for customization=[{}] each metalSpec=[{}]", parentCustomization.getId(), eachMetalPurity);
            List<CustomizationMetalSpecification> parentCustomizationMetalSpecification = parentCustomization.getCustomizationMetalSpecification();
            
            Customization newCustomization = createNewCustomization(parentCustomization);
            createCustomizationMetalSpecifications(newCustomization, parentCustomizationMetalSpecification, eachMetalPurity);
            
            List<CustomizationStoneSpecification> cssList = new ArrayList<CustomizationStoneSpecification>();
            for (CustomizationStoneSpecification eachCSS : parentCustomizationStoneSpecification) {
                CustomizationStoneSpecification newCustomizationStoneSpecification = new CustomizationStoneSpecification();
                newCustomizationStoneSpecification.setCustomization(newCustomization);
                newCustomizationStoneSpecification.setDesignStoneSpecification(eachCSS.getDesignStoneSpecification());
                newCustomizationStoneSpecification.setStoneSpecification(eachCSS.getStoneSpecification());
                cssList.add(newCustomizationStoneSpecification);
            }
            newCustomization.setCustomizationStoneSpecification(cssList);
            setPriceAndSkuCode(newCustomization, eachMetalPurity);
            log.info("Customization done for old customizationId=[{}] and metalSpec=[{}]  New customizationId=[{}]", parentCustomization.getId(), eachMetalPurity, newCustomization.getId());
            return newCustomization;
        } catch (Exception e) {
            throw new ProductUploadException(e.getMessage(), e);
        }
        
    }
    
    private List<Customization> generateStoneCombinations(final Customization parentCustomization, Integer eachMetalPurity) throws Exception {
        List<Customization> derivedCustomizationList = new ArrayList<Customization>();
        try {
            String[] stoneCombStrings = customizationStoneCombinations.split(",");
            for (String eachComb : stoneCombStrings) {
                if (eachComb.startsWith("SI:GH") && eachMetalPurity == 18) {
                    continue; // as this combination is already present
                } else {
                    log.info("Customization started for CustomizationId=[{}] and metalSpec=[{}] and stone combination=[{}] ",
                             parentCustomization.getId(), eachMetalPurity, eachComb);
                    Customization newCustomization = createNewCustomization(parentCustomization);
                    List<CustomizationMetalSpecification> parentCustomizationMetalSpecification = parentCustomization.getCustomizationMetalSpecification();
                    createCustomizationMetalSpecifications(newCustomization, parentCustomizationMetalSpecification, eachMetalPurity);
                    createCustomizationStoneSpecifications(parentCustomization, eachComb, newCustomization);
                    setPriceAndSkuCode(newCustomization, eachMetalPurity);
                    derivedCustomizationList.add(newCustomization);
                    log.info("Customization done for old customization=[{}] and metalSpec=[{}] and stone combination=[{}] new customizationId=[{}]",
                             parentCustomization.getId(), eachMetalPurity, eachComb, newCustomization.getId());
                }
            }
        } catch (Exception e) {
            log.error("Exception while generateStoneCombinations for CustomizationId=[{}] Metal purity=[{}]", parentCustomization.getId(), eachMetalPurity, e);
            throw new ProductUploadException(e.getMessage(), e);
        }
        return derivedCustomizationList;
    }

    private void setPriceAndSkuCode(Customization newCustomization, int eachMetalPurity) throws Exception {
        newCustomization.setSkuCode(SkuCodeFactory.getSKuCode(newCustomization));
        BigDecimal price = new BigDecimal(pricingEngineService.getPrice(newCustomization));
        MetalSpecification metalSpecification = newCustomization.getCustomizationMetalSpecification().get(0).getMetalSpecification();
        String color = metalSpecification.getColor();
        newCustomization.setPrice(price);
        
        StringBuilder fileNameBuilder = new StringBuilder(eachMetalPurity + "");
        fileNameBuilder.append(color.charAt(0)).append("_XXX_");
        
        Map<String, Map<String, Object>> stoneFamiliesDetails = newCustomization.getStoneFamiliesDetails();
        for (String eachStoneType : stoneFamiliesDetails.keySet()) {
            fileNameBuilder.append(DesignConstants.stoneCodes.get(eachStoneType.toLowerCase()));
            fileNameBuilder.append("_"); // except for the last stone
        }
        String fileName = fileNameBuilder.toString();
        fileName = fileName.substring(0, fileName.length() - 1);
        String categoryType = newCustomization.getDesign().getDesignCategory().getCategoryType();
        Map<String, Object> extractCustomizationDetails = customizationFactory.extractCustomizationDetails(fileName, categoryType);
        newCustomization.setShortDescription(extractCustomizationDetails.get("shortDesc").toString());
        
    }
    
    private void createCustomizationStoneSpecifications(final Customization parentCustomization, String eachComb, Customization newCustomization) {
        List<CustomizationStoneSpecification> ssList = new ArrayList<CustomizationStoneSpecification>();
        
        for (CustomizationStoneSpecification eachCSS : parentCustomization.getCustomizationStoneSpecification()) {
            StoneSpecification parentStoneSpecification = eachCSS.getStoneSpecification();
            String stoneType = parentStoneSpecification.getStoneType();
            CustomizationStoneSpecification newCustomizationStoneSpecification = new CustomizationStoneSpecification();
            newCustomizationStoneSpecification.setCustomization(newCustomization);
            newCustomizationStoneSpecification.setDesignStoneSpecification(eachCSS.getDesignStoneSpecification());
            
            if ("Diamond".equalsIgnoreCase(stoneType)) {
                if (eachComb.startsWith("SI:GH")) {
                    newCustomizationStoneSpecification.setStoneSpecification(parentStoneSpecification);
                } else {
                    StoneSpecification stoneSpecification = getStoneSpecification(eachComb, parentStoneSpecification, stoneType);
                    newCustomizationStoneSpecification.setStoneSpecification(stoneSpecification);
                }
                ssList.add(newCustomizationStoneSpecification);
            } else {
                newCustomizationStoneSpecification.setStoneSpecification(parentStoneSpecification);
                ssList.add(newCustomizationStoneSpecification);
            }
        }
        newCustomization.setCustomizationStoneSpecification(ssList);
    }

    private StoneSpecification getStoneSpecification(String eachComb, StoneSpecification parentStoneSpecification, String stoneType) {
        log.debug("DerivedCustomizationService.getStoneSpecification(): StoneType=[{}] {}", stoneType, eachComb);
        String[] clarityAndColor = StringUtils.split(eachComb, ":");
        String color = clarityAndColor[1];
        String clarity = clarityAndColor[0];
        String size = parentStoneSpecification.getSize();
        String shape = parentStoneSpecification.getShape();
        BigDecimal stoneWeight = parentStoneSpecification.getStoneWeight();

        StoneSpecification stoneSpecification = new StoneSpecification();
        stoneSpecification.setClarity(clarity);
        stoneSpecification.setColor(color);
        stoneSpecification.setIsActive(true);
        stoneSpecification.setShape(shape);
        stoneSpecification.setSize(size);
        stoneSpecification.setStoneType(stoneType);
        stoneSpecification.setStoneWeight(stoneWeight);
        stoneSpecification = stoneSpecificationsDao.checkForExistingStone(stoneSpecification);

        return stoneSpecification;
    }
    
    private boolean isDiamondProduct(List<CustomizationStoneSpecification> customizationStoneSpecification) {
        if(customizationStoneSpecification.isEmpty()) {
            return false;
        }
        boolean isDiamondProduct = false;
        for (CustomizationStoneSpecification eachCMSpecification : customizationStoneSpecification) {
            StoneSpecification stoneSpecification = eachCMSpecification.getStoneSpecification();
             if(stoneSpecification.isDiamond()) {
                 return true;
             } 
        }
        return isDiamondProduct;
    }
    
    private boolean isWhileOrRoseGoldProduct(List<CustomizationMetalSpecification> parentCustomizationMetalSpecification) {
        for (CustomizationMetalSpecification customizationMetalSpecification : parentCustomizationMetalSpecification) {
            String color = customizationMetalSpecification.getMetalSpecification().getColor();
            if("Rose".equalsIgnoreCase(color.trim())) {
                return true;
            }
            if("White".equalsIgnoreCase(color.trim())) {
                return true;
            }
        }
        return false;
    }

    private boolean isHeartOrBaguetteDiamondProduct(List<CustomizationStoneSpecification> customizationStoneSpecification) {
        boolean isHeartOrBaguetteDiamondProduct = false;
        for (CustomizationStoneSpecification eachCMSpecification : customizationStoneSpecification) {
            StoneSpecification stoneSpecification = eachCMSpecification.getStoneSpecification();
            if (stoneSpecification.isDiamond()) {
                if (stoneSpecification.isHeartShape()) {
                    isHeartOrBaguetteDiamondProduct = true;
                    break;
                } else if (stoneSpecification.isStraightBaguetteShape()) {
                    isHeartOrBaguetteDiamondProduct = true;
                    break;
                }
            }
        }
        return isHeartOrBaguetteDiamondProduct;
    }
    
    private boolean isPrincessCutDiamondProduct(List<CustomizationStoneSpecification> customizationStoneSpecification) {
        boolean isPricessCutDiamondProduct = false;
        for (CustomizationStoneSpecification eachCMSpecification : customizationStoneSpecification) {
            String shape = eachCMSpecification.getStoneSpecification().getShape();
            String stoneType = eachCMSpecification.getStoneSpecification().getStoneType();
            if ("Princess".equalsIgnoreCase(shape) && stoneType.equals("Diamond")) {
                return true;
            }
        }
        return isPricessCutDiamondProduct;
    }
    
    private Customization createNewCustomization(Customization parentCustomization) {
        Customization newCustomization = new Customization();
        newCustomization.setDesign(parentCustomization.getDesign());
        newCustomization.setPriority(0);
        newCustomization.setProductType(Product.PRODUCT_TYPE.JEWELLERY);
        newCustomization.setIsActive(parentCustomization.isActive());
        newCustomization.setImageVersion(parentCustomization.getImageVersion());
        newCustomization.setShortDescription(parentCustomization.getShortDescription());
        newCustomization.setParentCustomization(parentCustomization);
        newCustomization.setProductType(PRODUCT_TYPE.JEWELLERY);
        return newCustomization;
    }

}
