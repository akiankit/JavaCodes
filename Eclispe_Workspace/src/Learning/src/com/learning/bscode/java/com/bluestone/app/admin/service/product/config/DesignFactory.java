package com.bluestone.app.admin.service.product.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import antlr.collections.impl.Vector;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.admin.service.FilterTagService;
import com.bluestone.app.core.util.DataSheetConstants;
import com.bluestone.app.core.util.ProductValidatorUtil;
import com.bluestone.app.design.dao.DesignCategoryDao;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationMetalSpecification;
import com.bluestone.app.design.model.CustomizationSizeMetalSpecification;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.model.DesignMetalFamily;
import com.bluestone.app.design.model.DesignStoneSpecification;
import com.bluestone.app.design.model.MetalSpecification;
import com.bluestone.app.pricing.PricingEngineService;
import com.bluestone.app.search.tag.TagDao;
import com.bluestone.app.search.tag.model.Tag;
import com.bluestone.app.search.tag.model.TagCategory;

/**
 * @author Rahul Agrawal Date: 9/20/12
 */

public class DesignFactory {

    private static final Logger log = LoggerFactory.getLogger(DesignFactory.class);

    private final DesignCategoryDao designCategoryDao;

    private final TagDao tagdao;

    private DesignDao designdao;

    private FilterTagService filterTagService;

    private final PricingEngineService pricingEngineService;

    public DesignFactory(DesignCategoryDao designCategoryDao, TagDao tagdao, PricingEngineService pricingEngineService, DesignDao designdao, FilterTagService filterTagService) {
        this.designCategoryDao = designCategoryDao;
        this.tagdao = tagdao;
        this.pricingEngineService = pricingEngineService;
        this.designdao = designdao;
        this.filterTagService = filterTagService;
    }

    Design validateAndCreateDesign(Vector dataSheetVector, Design updateDesign) throws ProductUploadException {
        log.debug("DesignFactory.validateAndCreateDesign()");
        Design design = new Design();
        boolean update = false;
        if (updateDesign != null) {
            design = updateDesign;
            update = true;
        }
        // Get category
        final String categoryFromDataSheet = getCategory(dataSheetVector);
        if (categoryFromDataSheet == null) {
            throw new ProductUploadException("Could not find the category=" + categoryFromDataSheet);
        }

        // Get Design Category using the category
        final DesignCategory designCategory = getDesignCategory(categoryFromDataSheet);
        if (designCategory == null) {
            throw new ProductUploadException("Could not find design category for category=" + categoryFromDataSheet);
        } else {
            design.setDesignCategory(designCategory);
        }

        // ######### Get Code
        if (updateDesign == null) {
            final String designCode = getDesignCode(dataSheetVector);

            if (designCode == null) {
                throw new ProductUploadException("Could not find the design code");
            } else {
                Design checkCode = designdao.getDesignByCode(designCode);
                if (checkCode == null) {
                    design.setDesignCode(designCode);
                } else {
                    throw new ProductUploadException("Design with code: " + designCode + " already exists.");
                }
            }
        }

        // Get Design Name
        final String designName = getTitleOrDesc(0, dataSheetVector);
        if (designName == null) {
            throw new ProductUploadException("Could not find the design name.");
        } else {
            design.setDesignName(designName);
        }

        // Get long description
        final String longDescription = getTitleOrDesc(1, dataSheetVector);
        if (longDescription == null) {
            throw new ProductUploadException("No description for design");
        } else {
            design.setLongDescription(longDescription);
        }

        // Get height
        Object height = getHeight(dataSheetVector);
        if (height == null) {
            throw new ProductUploadException("Product height is missing");
        } else {
            design.setHeight(((Double) height).floatValue());
        }

        // Get Width
        Object width = getWidth(dataSheetVector);
        if (width == null) {
            throw new ProductUploadException("Product width is missing");
        } else {
            design.setWidth(((Double) width).floatValue());
        }

        List<DesignStoneSpecification> designStoneSpecificationsSet = validateAndCreateDesignStone(design, dataSheetVector, update);

        if (designStoneSpecificationsSet != null) {
            design.setDesignStoneSpecifications(designStoneSpecificationsSet);
        } else {
            throw new ProductUploadException("Error while creating design stone.");
        }
        List<DesignMetalFamily> createDesignMetalFamily = createDesignMetalFamily(design, dataSheetVector, DataSheetConstants.customization.get("start"), update);
        if (createDesignMetalFamily.size() > 0) {
            design.setDesignMetalFamilies(createDesignMetalFamily);
        } else {
            throw new ProductUploadException("Error while creating design metal family.");
        }

        return design;
    }

    private Object getWidth(Vector dataSheetVector) {
        log.debug("DesignFactory.getWidth()");
        return ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("general"), DataSheetConstants.general.get("start"), DataSheetConstants.general.get("width"));
    }

    private Object getHeight(Vector dataSheetVector) {
        log.debug("DesignFactory.getHeight()");
        return ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("general"), DataSheetConstants.general.get("start"), DataSheetConstants.general.get("height"));
    }

    private String getCategory(Vector dataSheetVector) {
        log.debug("DesignFactory.getCategory()");
        String category = (String) ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("general"),
                                                            DataSheetConstants.general.get("start"), DataSheetConstants.general.get("type"));
        log.debug("Got Category={} from the spread sheet.", category);
        return category;
    }

    private DesignCategory getDesignCategory(String category) {
        DesignCategory designCategory = designCategoryDao.getDesignCategory(DataSheetConstants.categories.get(category.toLowerCase()));
        log.debug("Got DesignCategory=[{}] from Category=[{}]", designCategory, category);
        return designCategory;
    }

    private String getDesignCode(Vector dataSheetVector) {
        String designCode = (String) ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("customization"),
                                                              DataSheetConstants.customization.get("start"), DataSheetConstants.customization.get("design code"));
        log.debug("Got DesignCode={}", designCode);
        return designCode;
    }

    private String getTitleOrDesc(int index, Vector dataSheetVector) {
        log.debug("DesignFactory.getTitleOrDesc()");
        String result = null;
        String text = (String) ExcelSheetHelper.getCell(dataSheetVector, DataSheetConstants.dataGroups.get("title"), index, 0);
        if (!StringUtils.isBlank(text)) {
            result = text.trim().substring(3);
        }
        log.debug("Got Title/Description={}", result);
        return result;
    }

   
    public Set<Tag> getTagSet(Vector dataSheetVector, Design design) throws ProductUploadException {
        int sheet = DataSheetConstants.dataGroups.get("tag");
        int row = 0;
        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        List<String> tags = new ArrayList<String>();
        for (int j = row; j < rowVectorHolder.size(); j++) {
            try {
                Object cell = ExcelSheetHelper.getCell(dataSheetVector, sheet, j, 0);
                if (cell != null) {
                    String tagName = (String) cell;
                    tags.add(tagName.toLowerCase().trim());
                }
            } catch (Exception e) {
                log.error("Invalid tag at row number " + (j + 1) + " on tag sheet", e);
                throw new ProductUploadException("Invalid tag at row number " + (j + 1) + " on tag sheet");
            }
        }
        return filterTagService.processTags(tags, design);
    }

    @VisibleForTesting
    protected List<DesignStoneSpecification> validateAndCreateDesignStone(Design design, Vector dataSheetVector, boolean update)
            throws ProductUploadException {
        log.debug("DesignFactory.validateAndCreateDesignStone()");
        List<DesignStoneSpecification> designStoneSpecs = new LinkedList<DesignStoneSpecification>();
        int sheet = DataSheetConstants.dataGroups.get("stone");
        int row = DataSheetConstants.stone.get("start");

        Vector rowVectorHolder = (Vector) dataSheetVector.elementAt(sheet);
        if (update) {
            List<DesignStoneSpecification> designStoneSpecifications = design.getDesignStoneSpecifications();
            for (DesignStoneSpecification designStoneSpecification : designStoneSpecifications) {
                designdao.remove(designStoneSpecification);
            }
        }
        for (int rowNo = row; rowNo < rowVectorHolder.size(); rowNo++) {
            if (rowVectorHolder.elementAt(rowNo) == null) {
                break;
            }
            DesignStoneSpecification designStoneSpecification = new DesignStoneSpecification();
            designStoneSpecification.setDesign(design);
            Object oNumbers = ExcelSheetHelper.getCell(dataSheetVector, sheet, rowNo, DataSheetConstants.stone.get("numbers"));
            String settingFromSheet = (String) ExcelSheetHelper.getCell(dataSheetVector, sheet, rowNo, DataSheetConstants.stone.get("setting"));
            Object oFamily = ExcelSheetHelper.getCell(dataSheetVector, sheet, rowNo, DataSheetConstants.stone.get("family"));

            if (oNumbers == null && settingFromSheet == null && oFamily == null) {
                break;
            }
            if (oNumbers != null) {
                double numbers = (Double) oNumbers;
                designStoneSpecification.setNoOfStones((int) numbers);

            } else {
                throw new ProductUploadException("Stone count is missing " + (rowNo + 4));
            }

            if (oFamily != null) {
                double family = (Double) oFamily;
                designStoneSpecification.setFamily((short) family);

            } else {
                throw new ProductUploadException("Missing family reference for stone " + (rowNo + 4));
            }

            if (settingFromSheet != null) {
                String settingFromConstants = ProductValidatorUtil.validateStoneSettingType(settingFromSheet);
                if (settingFromConstants != null) {
                    designStoneSpecification.setSettingType(settingFromConstants);
                } else {
                    throw new ProductUploadException("Stone setting is not valid setting " + settingFromSheet);
                }
            } else {
                throw new ProductUploadException("Stone setting not found at row no" + (rowNo + 4));
            }


            designStoneSpecification.setSheetIndex(rowNo);
            designStoneSpecs.add(designStoneSpecification);

        }
        return designStoneSpecs;
    }

    private List<DesignMetalFamily> createDesignMetalFamily(Design design, Vector dataSheetVector, int sheetColumnIndex, boolean update) {
        log.debug("DesignFactory.createDesignMetalFamily()");
        List<DesignMetalFamily> designMetalFamilyList = new ArrayList<DesignMetalFamily>();
        Set<String> metalIndexKeySet = DataSheetConstants.metalColumnIndex.keySet();
        Integer sheet = DataSheetConstants.dataGroups.get("customization");

        int index = 0;
        for (String eachValue : metalIndexKeySet) {
            Integer eachMetalColumnIndex = DataSheetConstants.metalColumnIndex.get(eachValue);
            String metalType = (String) ExcelSheetHelper.getCell(dataSheetVector, sheet, sheetColumnIndex, eachMetalColumnIndex);
            if (metalType != null) {
                DesignMetalFamily designMetalFamily = new DesignMetalFamily();
                designMetalFamily.setFamily((short) (index + 1));
                designMetalFamily.setDesign(design);
                designMetalFamilyList.add(designMetalFamily);
            }
        }
        return designMetalFamilyList;
    }

    public void setMetalPriceForSize(Customization customization) throws ProductUploadException {
        log.info("DesignFactory.setMetalPriceForSize(): Going to iterate over all sizes.");
        List<CustomizationMetalSpecification> customizationMetalSpecification = customization.getCustomizationMetalSpecification();
        for (CustomizationMetalSpecification customizationMetalSpec : customizationMetalSpecification) {
            List<CustomizationSizeMetalSpecification> designSizeMetalSpecifications = customizationMetalSpec.getCustomizationSizeMetalSpecifications();
            if (designSizeMetalSpecifications != null) {
                for (CustomizationSizeMetalSpecification designSizeMetalSpecification : designSizeMetalSpecifications) {
                    BigDecimal deltaPrice = getDeltaPrice(customizationMetalSpec.getMetalSpecification(), designSizeMetalSpecification.getDelta());
                    designSizeMetalSpecification.setPriceDelta(deltaPrice);
                }
            }
        }
    }

    public BigDecimal getDeltaPrice(MetalSpecification metalSpecification, BigDecimal metalWeight) throws ProductUploadException {
        log.info("DesignFactory.getDeltaPrice() for metalSpecification={} metalWeight={}", metalSpecification.getFullname(), metalWeight);
        BigDecimal price = new BigDecimal(0);
        try {
            price = BigDecimal.valueOf(pricingEngineService.getSizePrice(metalSpecification, metalWeight));
            log.info("DesignFactory.getDeltaPrice() for MetalSpecification=[{}] MetalWeight=[{}] Price=[{}]\n", metalSpecification.getFullname(), metalWeight, price);
        } catch (Exception e) {
            String errorMessage = "Error fetching delta price from pricing engine. Reason=" + e.toString();
            log.error("Error : DesignFactory.getDeltaPrice() for metalSpecification={} metalWeight={} : Reason={}", metalSpecification.getFullname(), metalWeight, e.toString(), e);
            throw new ProductUploadException(errorMessage);
        }
        return price;
    }

    private Tag addTag(String tagname, TagCategory tagCategory, int urlPriority, short isActive) {
        Tag tag = new Tag();
        tagname = tagname.toLowerCase().replace("'s ", " s ");
        tag.setName(tagname);
        TagCategory tagCat = tagCategory;
        tag.setUrlPriority(urlPriority);
        tag.setTagCategory(tagCat);
        tag.setIsActive(isActive);
        tag.setDerived(true);
        tagdao.create(tag);
        return tag;
    }

}
