package com.bluestone.app.design.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.service.product.config.CustomizationFactory;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.design.dao.CustomizationDao;
import com.bluestone.app.design.dao.DesignCategoryDao;
import com.bluestone.app.design.dao.DesignDao;
import com.bluestone.app.design.model.Design;
import com.bluestone.app.design.model.DesignStoneSpecification;
import com.bluestone.app.pricing.PricingEngineService;
import com.bluestone.app.search.tag.TagDao;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
public class DesignService {

    private static final Logger log = LoggerFactory.getLogger(DesignService.class);

    @Autowired
    private DesignDao designDao;

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private CustomizationDao customizationDao;

    @Autowired
    private CustomizationFactory customizationFactory;

    @Autowired
    private DesignCategoryDao designCategoryDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private PricingEngineService pricingEngineService;

    public Design findByPrimaryKey(long primaryKey) {
        return designDao.findAny(Design.class, primaryKey);
    }

    public Design getActiveDesign(Long designId) {
        return designDao.find(Design.class, designId, true);
    }

    public List<Design> getDesignList(String list) {
        List<Long> longList;
        if (list != null) {
            if (!list.trim().equalsIgnoreCase("all")) {
                String[] ids = list.split(",");
                Long intarray[] = new Long[ids.length];
                for (int i = 0; i < ids.length; i++) {
                    intarray[i] = (long) Integer.parseInt(ids[i]);
                }
                longList = Arrays.asList(intarray);
            } else {
                longList = null;
            }
            return (designDao.getDesignList(longList));
        } else {
            return null;
        }
    }

/*    @TriggersRemove(cacheName = {"browsePageProductsCache", "filtersCache", "searchPageCustomizationCache", "seoCategoryListCache", "childTagsCache", "tagListByNames", "seoMinMaxPriceCache", "seoCountCache", "tagSeoCache"}, when = When.AFTER_METHOD_INVOCATION, removeAll = true)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Customization updateDesign(Map<String, Object> designInput) {
        final String designId = (String) designInput.get("designId");
        log.info("DesignService.updateDesign(): DesignId=[{}]", designId);
        List<String> error = new ArrayList<String>();
        Design design = designDao.findAny(Design.class, Long.parseLong(designId));
        if (design == null) {
            log.error("Unable to find design: " + designId);
            return null;
        } else {
            design = validateAndSetDesignForUpdate(design, designInput, error);
            List<DesignStoneSpecification> designSpecForUpdate = validateAndSetDesignSpecForUpdate(design.getDesignStoneSpecifications(), error, designInput);

            for (Customization customization : design.getCustomizations()) {
                if (customization.getPriority() == 1) {

                    customization.setPrice(customizationFactory.getCustomizationPrice(customization, error));
                }
            }
            if (error.isEmpty()) {
                designDao.create(design);
                for (DesignStoneSpecification designStoneSpecifications : designSpecForUpdate) {
                    designStoneSpecifications.setDesign(design);
                    baseDao.create(designStoneSpecifications);
                }
                for (Customization customization : design.getCustomizations()) {
                    if (customization.getPriority() == 1) {
                        customization.setDesign(design);
                        customization.setEdit((short) 0);
                        customization.setIsActive(true);
                        customizationDao.create(customization);
                        return customization;
                    }
                }

            } else {
                design.setError(error);
                for (Customization customization : design.getCustomizations()) {
                    if (customization.getPriority() == 1) {
                        customization.setEdit((short) 1);
                        return customization;
                    }
                }
            }
        }
        return null;
    }
*/

    /*private Design validateAndSetDesignForUpdate(Design design, Map<String, Object> designInput, List<String> error) {
        DesignFactory designFactory = new DesignFactory(designCategoryDao, tagDao, pricingEngineService, designDao);
        String name = (String) designInput.get("designName");
        if (StringUtils.isBlank(name)) {
            error.add("Design name is empty ");
        } else {
            design.setDesignName(name);
        }
        String desc = (String) designInput.get("designDesc");
        if (StringUtils.isBlank(desc)) {
            error.add("Design Description is empty ");
        } else {
            design.setLongDescription(desc);
        }
        String metal1weight = (String) designInput.get("metalWeight1");
        if (StringUtils.isBlank(metal1weight)) {
            error.add("Design metal1 weight is empty ");
        } else {
            design.setMetal1Weight(Float.parseFloat(metal1weight));
        }
        String metal2weight = (String) designInput.get("metalWeight2");
        float metal2Weight2 = design.getMetal2Weight();
        if (StringUtils.isBlank(metal2weight)) {
            if (metal2Weight2 != 0.0) {
                error.add("Design metal2 weight is empty ");
            }
        } else {
            design.setMetal2Weight(Float.parseFloat(metal2weight));
        }

        String tags = (String) designInput.get("tags");
        if (StringUtils.isBlank(tags)) {
            error.add("Design tags is empty ");
        } else {
            String[] split = tags.split(",");
            List<String> tagsList = new LinkedList<String>(Arrays.asList(split));

            //List<String> tagsList = Lists.newArrayList(split);
            design.setTags(designFactory.processTags(tagsList, error));
        }
        String active = (String) designInput.get("isActive");
        if (StringUtils.isBlank(active)) {
            design.setIsActive((short) 0);
        } else {
            design.setIsActive((short) 1);
        }

        return design;
    }*/

/*    private List<DesignStoneSpecification> validateAndSetDesignSpecForUpdate(List<DesignStoneSpecification> designSpec, List<String> error, Map<String, Object> designInput) {
        String[] stoneIds = (String[]) designInput.get("stoneId");
        String[] noofStones = (String[]) designInput.get("noOfStone");
        if (designSpec != null && designSpec.size() > 0) {
            if (designSpec.size() == noofStones.length && designSpec.size() == stoneIds.length) {
                for (DesignStoneSpecification designspec : designSpec) {
                    for (int i = 0; i < stoneIds.length; i++) {
                        try {
                            if (designspec.getId() == Long.parseLong(stoneIds[i])) {
                                designspec.setNoOfStones(Integer.parseInt(noofStones[i]));
                            }
                        } catch (Exception e) {
                            error.add("Error while updating number of stone: " + noofStones[i]);
                            log.error("Error while updating the number of stones.", e);
                        }
                    }
                }
            } else {
                error.add("Missing number of stone property for stone");
            }
        }
        return designSpec;
    }*/

}
