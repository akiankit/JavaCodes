package com.bluestone.app.admin.service.product.config;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import antlr.collections.impl.Vector;

import com.bluestone.app.admin.service.FilterTagService;
import com.bluestone.app.admin.service.product.config.customization.ParentCustomizationIntegrationProcessor;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.DesignCategoryDao;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignImageView;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.design.model.StoneSpecification;
import com.bluestone.app.design.service.CustomizationService;
import com.bluestone.app.pricing.PricingEngineService;
import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;
import com.google.common.base.Throwables;
import com.googlecode.ehcache.annotations.TriggersRemove;
import com.googlecode.ehcache.annotations.When;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class ProductUploadService {

    private static final Logger log = LoggerFactory.getLogger(ProductUploadService.class);

    @Autowired
    private TagDao tagdao;

    @Autowired
    private DesignDao designdao;

    @Autowired
    private CustomizationDao customizationdao;

    @Autowired
    private DesignCategoryDao designcategorydao;

    @Autowired
    private FilterTagService filterTagService;

    @Autowired
    private CustomizationFactory customizationFactory;

    @Autowired
    DesignViewHelper designViewHelper;

    @Autowired
    private PricingEngineService pricingEngineService;

    @Autowired
    private ProductUploadContext productUploadContext;

    @Autowired
    private CustomizationService customizationService;

    @Autowired
    ParentCustomizationIntegrationProcessor parentCustomizationIntegrationProcessor;


    @PostConstruct
    public void init() {
        //log.info("ProductUploadService.init() : Product Upload Path = {}", productUploadContext.getProductUploadPath());
        //System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
    }

    /*@TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<String> validateAndUpdateProduct(String designCode) {
        Design design = designdao.getDesignByCode(designCode);
        List<String> error = new ArrayList<String>();
        if (design == null) {
            error.add("Unable to find the design with code=" + designCode);
        } else {
            log.info("ProductUploadService.validateAndCreateProduct()for design=[{}]", design.toShortString());
            Vector dataSheetVector = ExcelSheetHelper.importExcelSheet(design.getDesignCode(), productUploadPath, error);
            if (dataSheetVector == null) {
                error.add("Failed to find/read the Datasheet spreadsheet " + productUploadPath);
                log.error("Error: ProductUploadService: Failed to find the DataSheet spreadsheet at path=[{}]", productUploadPath);
                return error;
            }
            //deleteAllCustomizationForDesign(design);
            DesignFactory designFactory = new DesignFactory(designcategorydao, tagdao, pricingEngineService, designdao);
            design = designFactory.validateAndCreateDesign(dataSheetVector, error, design);
            if (design == null) {
                error.add("Error during updation of design.");
                log.error("Error: ProductUploadService.validateAndUpdateProduct(): Failed during updation of Design=[{}]", design.toShortString());
                return error;
            }
            Set<Tag> tagSet = designFactory.getTagSet(dataSheetVector, error);
            if (tagSet != null) {
                design.setTags(tagSet);
            }
            final List<String> listOfDesignViewErrors = designViewHelper.updateDesignView(design);
            if (!listOfDesignViewErrors.isEmpty()) {
                error.add("Error during updation of design views");
                log.error("Error: ProductUploadService.validateAndUpdateProduct(): Failed during updation of design views.");
                error.addAll(listOfDesignViewErrors);
                return error;
            }

            List<Customization> listOfCustomizations = customizationFactory.createAllCustomizations(design, dataSheetVector, error, true);


            if (listOfCustomizations == null) {
                error.add("Error while creating customizations");
                log.error("Error: ProductUploadService.validateAndCreateProduct(): While creating customizations.");
                return error;
            }

            log.info("ProductUploadService.validateAndCreateProduct(): List of customizations have been prepared.");

            resizeAndUploadImages(design.getDesignCode(), error, designFactory, listOfCustomizations);
            //@todo : Rahul Agrawal :  commented this part
            //commit(design.getDesignCode(), error, design, listOfCustomizations);
        }
        return error;
    }
*/
    private void deleteAllCustomizationForDesign(Design design) {
        log.debug("ProductUploadService.deleteAllCustomizationForDesign()");
        List<Customization> customizations = design.getCustomizations();
        for (Customization customization : customizations) {
            StoneSpecification[] stoneSpecifications = new StoneSpecification[4];
            MetalSpecification[] metalSpecifications = new MetalSpecification[4];
            List<CustomizationStoneSpecification> customizationStoneSpecification = customization.getCustomizationStoneSpecification();
            for (CustomizationStoneSpecification customizationStoneSpec : customizationStoneSpecification) {
                stoneSpecifications[customizationStoneSpec.getDesignStoneSpecification().getFamily()] = customizationStoneSpec.getStoneSpecification();
            }
            List<CustomizationMetalSpecification> customizationMetalSpecification = customization.getCustomizationMetalSpecification();
            for (CustomizationMetalSpecification customizationMetalSpec : customizationMetalSpecification) {
                metalSpecifications[customizationMetalSpec.getDesignMetalFamily().getFamily()] = customizationMetalSpec.getMetalSpecification();
            }
            customization.setStoneSpecificationsForUpdate(Arrays.asList(stoneSpecifications));
            customization.setMetalSpecificationsForUpdate(Arrays.asList(metalSpecifications));

            customizationdao.deleteFromTable("CustomizationMetalSpecification", customization);
            customizationdao.deleteFromTable("CustomizationStoneSpecification", customization);
        }
    }


    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public void validateAndCreateProduct(String designCode) throws ProductUploadException {
        try {
            log.info("ProductUploadService.validateAndCreateProduct()for code={}", designCode);
            Vector dataSheetVector = ExcelSheetHelper.importExcelSheet(designCode, productUploadContext.getProductUploadPath());

            if (dataSheetVector == null) {
                throw new ProductUploadException("Failed to find/read the Data sheet spreadsheet at path " + productUploadContext.getProductUploadPath());
            }

            //Create a Design and related entities
            DesignFactory designFactory = new DesignFactory(designcategorydao, tagdao, pricingEngineService, designdao, filterTagService);
            Design design = designFactory.validateAndCreateDesign(dataSheetVector, null);

            //Get tags
            Set<Tag> tagSet = designFactory.getTagSet(dataSheetVector, design);
            if (tagSet != null) {
                design.setTags(tagSet);
            }

            //Create DesignView
            DesignImageView designView = new DesignImageView();
            designView.setDesign(design);
            String categoryType = design.getDesignCategory().getCategoryType();
            designViewHelper.getCreateDesignView(designCode, categoryType.toLowerCase(), false, designView);
            design.setDesignImageView(designView);


            // create customization and set stone weight, price and sku code
            List<Customization> listOfCustomizations = customizationFactory.createAllCustomizations(design, dataSheetVector, false);
            if (listOfCustomizations == null) {
                log.error("Error: ProductUploadService.validateAndCreateProduct(): While creating customizations.");
                throw new ProductUploadException("Error while creating customizations. Please check the log file");
            }

            log.info("ProductUploadService.validateAndCreateProduct(): List of customizations have been prepared.");

            resizeAndUploadImages(designFactory, listOfCustomizations);

            commit(design, listOfCustomizations);

            deleteBundle(designCode);
        } catch (ProductUploadException e) {
            e.getErrorList().add("Design Code :" + designCode);
            throw e;
        } catch (Throwable e) {
            throw new ProductUploadException("Error while processing design code :" + designCode + " error:"  + e.getMessage(), e);
        }
    }

    private void deleteBundle(String designCode) {
        Cleaner.backupAndDelete(designCode, productUploadContext.getProductUploadPath());
        log.info("No errors during product upload. Going ahead to delete the files used for product upload.");
    }

    private void commit(Design design, List<Customization> listOfCustomizations) throws ProductUploadException {
        // create all the db entities if there are no errors
        try {
            design.setCustomizations(listOfCustomizations);
            design.setIsActive(false);
            designdao.create(design);
            filterTagService.generateFilterTags(listOfCustomizations);
        } catch (Exception exception) {
            log.error("Error: ProductUploadService.commit(): Failed during saving to database. Reason=[{}]", exception.toString(), exception);
            throw new ProductUploadException("Error during saving to database." + exception.toString());
        }

    }

    private void resizeAndUploadImages(DesignFactory designFactory, List<Customization> listOfCustomizations) throws ProductUploadException {
        for (Customization customization : listOfCustomizations) {
            designFactory.setMetalPriceForSize(customization);
            try {
                parentCustomizationIntegrationProcessor.pushToS3AndUniware(customization);
            } catch (ProductUploadException exception) {
                throw exception;
            } catch (Exception exception) {
                final Throwable rootCause = Throwables.getRootCause(exception);
                log.error("Error: ProductUploadService.validateAndCreateProduct(): Failed to resize and rename images. Reason={}",
                          exception.toString(), rootCause);
                throw new ProductUploadException("Failed to resize and rename images : " + rootCause.toString());
            }
        }
    }

    private List<CustomizationMetalSpecification> setMetalWeightForCustomizationUpdate(List<CustomizationMetalSpecification> customizationMetalSpecifications,
                                                                                       Map<String, String> metalWeightMap) {
        for (CustomizationMetalSpecification customizationMetalSpecification : customizationMetalSpecifications) {
            int family = customizationMetalSpecification.getDesignMetalFamily().getFamily();
            String key = "metalWeight" + family;
            if (metalWeightMap.containsKey(key)) {
                Double parseDouble = Double.parseDouble(metalWeightMap.get(key).trim());
                BigDecimal weight = BigDecimal.valueOf(parseDouble);
                customizationMetalSpecification.setMetalWeight(weight);
            }
        }
        return customizationMetalSpecifications;
    }

    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Customization updateCustomization(long customizationId, Map<String, String> metalWeightMap) throws ProductUploadException {
        Customization customization = customizationdao.findAny(Customization.class, customizationId);
        if (customization == null) {
            log.error("Unable to find customization for {} ", customizationId);
            throw new ProductUploadException("Unable to find customization for " + customizationId);
        } else {
            List<CustomizationMetalSpecification> setMetalWeightForCustomizationUpdate = setMetalWeightForCustomizationUpdate(customization.getCustomizationMetalSpecification(), metalWeightMap);
            customization.setCustomizationMetalSpecification(setMetalWeightForCustomizationUpdate);
            customization.setPrice(customizationFactory.getCustomizationPrice(customization));
            customization.setEdit((short) 0);
            customization.setIsActive(true);
            return customizationdao.update(customization);
        }
    }


    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void addParentCustomizationsToDesign(String designCode) throws ProductUploadException {
        log.info("ProductUploadService.addParentCustomizationsToDesign()");
        Design design = designdao.getDesignByCode(designCode);
        Customization customization = customizationService.getBriefCustomizationByDesignId(design.getId(), true, Customization.DEFAULT_PRIORITY);

        String dir;
        //File folder = new File(productUploadPath + File.separator + designCode);
        File folder = new File(productUploadContext.getDesignBundleDirPath(designCode));
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            int priority = 2;
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isDirectory()) {
                    dir = listOfFiles[i].getName();
                    //@todo : Rahul Agrawal :
                    Customization addCustomization = null; //customizationFactory.addCustomization(design, customization, dir, errors, (short) (priority), false, null);
                    priority++;
                    //@todo : Rahul Agrawal :
                    Set<Customization> addDefaultCustomizations = new HashSet<Customization>();//customizationFactory.addDefaultCustomizations(errors, addCustomization, priority);
                    addCustomization.setDerivedCustomizations(addDefaultCustomizations);
                    //@todo : Rahul Agrawal :
                    priority = priority + addDefaultCustomizations.size() + 1;
                    //   LinkedList<String> checkForMissingImagesErrors = designViewHelper.checkForMissingImages(design.getDesignCategory().getCategoryType(), code, dir);
                    //    if(checkForMissingImagesErrors.isEmpty()){
                    parentCustomizationIntegrationProcessor.pushToS3AndUniware(addCustomization);
                    customizationdao.create(addCustomization);
                    for (Customization cust : addDefaultCustomizations) {
                        customizationdao.create(cust);
                    }
                    try {
                        delete(new File(productUploadContext.getDesignBundleDirPath(designCode) + File.separator + dir));
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
        try {
            delete(new File(productUploadContext.getDesignBundleDirPath(designCode)));
        } catch (IOException e) {
            log.error("Error: ProductUploadService.addParentCustomizationsToDesign(): Reason={}", e.getLocalizedMessage(), Throwables.getRootCause(e));
        }
    }


    private void delete(File file) throws IOException {
        Cleaner.delete(file);
    }

}
