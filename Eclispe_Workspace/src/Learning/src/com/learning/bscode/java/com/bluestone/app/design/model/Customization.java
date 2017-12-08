package com.bluestone.app.design.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.PhoneticFilterFactory;
import org.apache.solr.analysis.SnowballPorterFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FullTextFilterDef;
import org.hibernate.search.annotations.FullTextFilterDefs;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.admin.service.SkuCodeFactory;
import com.bluestone.app.core.search.filters.ActiveEntitiesFilter;
import com.bluestone.app.core.search.filters.PriorityFilter;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.design.model.product.Product;

@Audited
@Entity
@NamedQueries({
        @NamedQuery(name = "Customization.customizationListByTags_date_add", query = Customization.CUSTOMIZATION_BY_TAGS + Customization.ORDER_BY_C_DESIGN_DATE_DESC),
        @NamedQuery(name = "Customization.customizationListByTags_pricehigh", query = Customization.CUSTOMIZATION_BY_TAGS + Customization.ORDER_BY_C_PRICE_DESC),
        @NamedQuery(name = "Customization.customizationListByTags_pricelow", query = Customization.CUSTOMIZATION_BY_TAGS + Customization.ORDER_BY_C_PRICE_ASC),
        @NamedQuery(name = "Customization.customizationListByTags_mostpopular", query = Customization.CUSTOMIZATION_BY_TAGS + Customization.ORDER_BY_C_MOST_POPULAR),

        @NamedQuery(name = "Customization.customizationListBySearchTags_date_add", query = Customization.CUSTOMIZATION_BY_SEARCH_TAGS + Customization.ORDER_BY_C_DESIGN_DATE_DESC),
        @NamedQuery(name = "Customization.customizationListBySearchTags_pricehigh", query = Customization.CUSTOMIZATION_BY_SEARCH_TAGS + Customization.ORDER_BY_C_PRICE_DESC),
        @NamedQuery(name = "Customization.customizationListBySearchTags_pricelow", query = Customization.CUSTOMIZATION_BY_SEARCH_TAGS + Customization.ORDER_BY_C_PRICE_ASC),
        @NamedQuery(name = "Customization.customizationListBySearchTags_mostpopular", query = Customization.CUSTOMIZATION_BY_SEARCH_TAGS + Customization.ORDER_BY_C_MOST_POPULAR),

        @NamedQuery(name = "Customization.customizationDefaultList_date_add", query = Customization.CUSTOMIZATION_DEFAULT + Customization.ORDER_BY_C_DESIGN_DATE_DESC),
        @NamedQuery(name = "Customization.customizationDefaultList_pricehigh", query = Customization.CUSTOMIZATION_DEFAULT + Customization.ORDER_BY_C_PRICE_DESC),
        @NamedQuery(name = "Customization.customizationDefaultList_pricelow", query =  Customization.CUSTOMIZATION_DEFAULT + Customization.ORDER_BY_C_PRICE_ASC),
        @NamedQuery(name = "Customization.customizationDefaultList_mostpopular", query =  Customization.CUSTOMIZATION_DEFAULT + Customization.ORDER_BY_C_MOST_POPULAR),
        })
@Indexed
@AnalyzerDef(name = "customanalyzer", tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class), filters = {
        @TokenFilterDef(factory = LowerCaseFilterFactory.class),
        @TokenFilterDef(factory = PhoneticFilterFactory.class, params = { @Parameter(name = "encoder", value = "RefinedSoundex") }),
        @TokenFilterDef(factory = SnowballPorterFilterFactory.class, params = { @Parameter(name = "language", value = "English") }) })
@FullTextFilterDefs({ @FullTextFilterDef(name = "priority", impl = PriorityFilter.class),
        @FullTextFilterDef(name = "isActive", impl = ActiveEntitiesFilter.class) })
@Analyzer(definition = "customanalyzer")
@Table(name = "customization")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Customization extends Product {

    public static final String QUALITY = "Quality";

	public static final String SHAPE = "Shape";

	public static final String TYPE_OF_SETTING = "Type of Setting";

	public static final String TOTAL_WEIGHT = "Total Weight";

	public static final String NUMBER_OF_STONES = "Number Of Stones";

	public static final int PRIORITY_ONE = 1;

    public static final int DEFAULT_PRIORITY = PRIORITY_ONE;

    private static final long                     serialVersionUID                  = -4607936100224252817L;
    protected static final String                 ORDER_BY_C_PRICE_DESC             = " order by c.price desc";
    protected static final String                 ORDER_BY_C_DESIGN_DATE_DESC       = " order by c.design.createdAt desc ";
    protected static final String                 ORDER_BY_C_PRICE_ASC              = " order by c.price asc ";
    protected static final String                 ORDER_BY_C_MOST_POPULAR           = " order by c.score desc ";

    protected static final String                 CUSTOMIZATION_BY_TAGS             = " select c from Customization c " +
    		                                                                                " join c.design d " +
    		                                                                                " join d.tags t" +
                                                                                            " join d.designCategory dc " +
                                                                                            " where t.name in :tags"   +
                                                                                            " and  dc.id not in :childDesignCategories " +
                                                                                            " and  c.priority = :priority" +
                                                                                            " and d.id not in :preferredProductIds"+
                                                                                            " group by d.id having count(t) =:tag_count";

    protected static final String                 CUSTOMIZATION_DEFAULT             = " select c from Customization c" +
    		                                                                                " join c.design d" +
                                                                                            " join d.designCategory dc " +
                                                                                            " where c.priority = :priority and " +
                                                                                            " dc.id not in :childDesignCategories "+
                                                                                            " and d.id not in :preferredProductIds";
    protected static final String                 CUSTOMIZATION_BY_SEARCH_TAGS      = " select c  from Customization c join c.design d join d.tags t" +
                                                                                            " join d.designCategory dc " +
                                                                                            " where t.name in :tags"   +
                                                                                            " and d.id in :designIds " +
                                                                                            " and c.priority = :priority" +
                                                                                            " group by d.id having count(t) =:tag_count";

    @IndexedEmbedded
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "design_id", nullable = false)
    private Design                                design;

    @Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
    @Column(name = "short_desc", nullable = false)
    private String                                shortDescription;

    @Field
    @Column(nullable = false, scale = 2)
    private BigDecimal                            price;

    @Field
    private int                                   priority;

    @Field
    @Column(name = "sku_code", nullable = false, unique = true)
    private String                                skuCode;

    @ManyToOne
    private Customization  parentCustomization;

    @OneToMany(mappedBy = "parentCustomization",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @BatchSize(size=10)
    private Set<Customization>    derivedCustomizations = new HashSet<Customization>();


    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size=10)
    @OneToMany(mappedBy = "customization", cascade = CascadeType.ALL)
    private List<CustomizationStoneSpecification> customizationStoneSpecification = new ArrayList<CustomizationStoneSpecification>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size=10)
    @OneToMany(mappedBy = "customization", cascade = CascadeType.ALL)
    private List<CustomizationMetalSpecification> customizationMetalSpecification = new ArrayList<CustomizationMetalSpecification>();


    @Column(name = "customization_version", columnDefinition = "int(1) default'0'", nullable = false)
    private int                                   imageVersion                           = 0;

    @Transient
    private CustomizationImages customizationImages = null;

    @Transient
    private float                                 totalWeightOfProduct;

    @Transient
    private String                                expectedDeliveryDate;

    @Transient
    private short                                 edit                              = 0;

    // denotes sheet index from product upload excel sheet
    @Transient
    private int                                   sheetIndex                        = 0;

    @Transient
    private
    List<StoneSpecification> stoneSpecificationsForUpdate;

    @Transient
    private
    List<MetalSpecification> metalSpecificationsForUpdate;

    private static final Logger log = LoggerFactory.getLogger(Customization.class);

    public BigDecimal getTotalStoneWeight() {
        BigDecimal totalWeightOfStones = BigDecimal.valueOf(0);
        List<CustomizationStoneSpecification> customizationStoneSpecifications = this.getCustomizationStoneSpecification();
        for (CustomizationStoneSpecification eachStoneSpecifications : customizationStoneSpecifications) {
            BigDecimal stoneWeight = eachStoneSpecifications.getTotalWeight();
            String stoneType = eachStoneSpecifications.getStoneSpecification().getStoneType();
            log.debug("\t\tStoneType=[{}]  Stone Weight=[{}] in carats", stoneType, stoneWeight);
            BigDecimal stoneWeightInGrams = stoneWeight.divide(new BigDecimal(5));
            log.debug("\t\tStoneType=[{}]  Stone Weight=[{}] in grams", stoneType, stoneWeightInGrams);
            totalWeightOfStones = totalWeightOfStones.add(stoneWeightInGrams);
        }
        log.debug("Customization.getTotalStoneWeight():total wt=[{}] grams", totalWeightOfStones);
        return totalWeightOfStones;
    }
    
    public float getTotalWeightOfProduct() {
        log.debug("Customization.getTotalWeightOfProduct() for Customization SKU=[{}]", skuCode);
        BigDecimal totalWeightOfProduct = new BigDecimal(0);

        List<CustomizationMetalSpecification> customizationMetalSpecifications = this.getCustomizationMetalSpecification();
        for (CustomizationMetalSpecification aCustomizationMetalSpecification : customizationMetalSpecifications) {
            BigDecimal metalWeight = aCustomizationMetalSpecification.getMetalWeight();
            String metalType = aCustomizationMetalSpecification.getMetalSpecification().getType();
            log.debug("\t\tType=[{}] MetalWeight=[{}]", metalType, metalWeight);
            totalWeightOfProduct = totalWeightOfProduct.add(metalWeight);
        }
        totalWeightOfProduct = totalWeightOfProduct.add(this.getTotalStoneWeight());
        log.debug("Customization.getTotalWeightOfProduct(): Total Weight=[{}] in grams", totalWeightOfProduct);
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedValue = df.format(totalWeightOfProduct);
        log.debug("Customization.getTotalWeightOfProduct(): Customization Sku=[{}]  Formatted TotalWeight=[{}] in grams", skuCode, formattedValue);
        return Float.parseFloat(formattedValue);
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public BigDecimal getPrice() {
        return NumberUtil.roundUp(price);
    }

    public BigDecimal getPrice(String size) {
        log.debug("Customization.getPrice(): for Size={} Price={} before adding size delta.", size, price);
        BigDecimal result = null;
        if (StringUtils.isBlank(size) || size.equalsIgnoreCase("-1")) {
            result = NumberUtil.roundUp(price);
        } else {
            BigDecimal finalPrice = price;
            //List<DesignMetalFamily> designMetalFamilies = design.getDesignMetalFamilies();
            log.debug("Customization.getPrice(): Customization Metal Specification size={}", customizationMetalSpecification.size());
            for (CustomizationMetalSpecification eachCustomizationMetalSpecification : customizationMetalSpecification) {
                List<CustomizationSizeMetalSpecification> customizationSizeMetalSpecifications = eachCustomizationMetalSpecification.getCustomizationSizeMetalSpecifications();
                for (CustomizationSizeMetalSpecification eachCustomizationSizeMetalSpecification : customizationSizeMetalSpecifications) {
                    String tempSize = eachCustomizationSizeMetalSpecification.getSize();
                    log.debug("Customization.getPrice(): TempSize={}", tempSize);
                    if (tempSize.equalsIgnoreCase(size)) {
                        BigDecimal priceDelta = eachCustomizationSizeMetalSpecification.getPriceDelta();
                        log.debug("Customization.getPrice(): PriceDelta={}", priceDelta);
                        finalPrice = finalPrice.add(priceDelta);
                    } else {
                        log.debug("Customization.getPrice(): size={} does not equal to TempSize={}", size, tempSize);
                    }
                }
            }
            result = NumberUtil.roundUp(finalPrice);
        }
        log.debug("Customization.getPrice(): Calculated Price={}", result);
        return result;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<Image> getImageUrls() {
        synchronized (this) {
            if (customizationImages == null) {
                customizationImages = new CustomizationImages(this);
            }
        }
        return customizationImages.getImageUrls();
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public short getEdit() {
        return edit;
    }

    public void setEdit(short edit) {
        this.edit = edit;
    }

    public List<CustomizationMetalSpecification> getCustomizationMetalSpecification() {
        return customizationMetalSpecification;
    }

    public void setCustomizationMetalSpecification(List<CustomizationMetalSpecification> customizationMetalSpecification) {
        this.customizationMetalSpecification = customizationMetalSpecification;
    }

    public List<CustomizationStoneSpecification> getCustomizationStoneSpecification() {
        return getCustomizationStoneSpecificationWithoutSolitaire();
    }
    
    private List<CustomizationStoneSpecification> getCustomizationStoneSpecificationWithoutSolitaire() {
        List<CustomizationStoneSpecification> withoutSolititaireCSSList = new ArrayList<CustomizationStoneSpecification>();
        for (CustomizationStoneSpecification customizationStoneSpecification : this.customizationStoneSpecification) {
            StoneSpecification stoneSpecification = customizationStoneSpecification.getStoneSpecification();
            if(!stoneSpecification.isSolitaire()) {
                withoutSolititaireCSSList.add(customizationStoneSpecification);
            }
        }
        return withoutSolititaireCSSList;
    }

    public void setCustomizationStoneSpecification(List<CustomizationStoneSpecification> customizationStoneSpecification) {
        this.customizationStoneSpecification = customizationStoneSpecification;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public Map<Short, List<CustomizationStoneSpecification>> getUniqueFamilies() {
        Map<Short, List<CustomizationStoneSpecification>> map = new HashMap<Short, List<CustomizationStoneSpecification>>();
        for (CustomizationStoneSpecification eachSpec : getCustomizationStoneSpecification()) {
            short family = eachSpec.getDesignStoneSpecification().getFamily();
            List<CustomizationStoneSpecification> list = map.get(family);
            if (list == null) {
                list = new ArrayList<CustomizationStoneSpecification>();
            }
            list.add(eachSpec);
            map.put(family, list);
        }
        return map;
    }

    public Map<String,Map<String,Object>> getStoneFamiliesDetails(){
    	Map<String,Map<String,Object>> uniqueFamilesDetails = new HashMap<String, Map<String,Object>>();
    	Map<Short, List<CustomizationStoneSpecification>> uniqueFamilies = getUniqueFamilies();
    	Set<Short> keySet = uniqueFamilies.keySet();
    	for (Short key : keySet) {
    		Map<String,Object> familyDetails = new HashMap<String, Object>();
			List<CustomizationStoneSpecification> stoneSpecificationsList = uniqueFamilies.get(key);
			int noOfStones = 0;
			BigDecimal weightOfStones = BigDecimal.ZERO;
			StoneSpecification stoneSpecification = stoneSpecificationsList.get(0).getStoneSpecification();
			String stoneType= stoneSpecification.getStoneType();
			if(uniqueFamilesDetails.containsKey(stoneType)){
				Map<String, Object> existingDetails = uniqueFamilesDetails.get(stoneType);
				noOfStones = (Integer) existingDetails.get(NUMBER_OF_STONES);
				String weightInString = ((String)existingDetails.get(TOTAL_WEIGHT)).split(" ")[0];
				weightOfStones = new BigDecimal(weightInString);
    		}
			for (CustomizationStoneSpecification customizationStoneSpecification : stoneSpecificationsList) {
				noOfStones+= customizationStoneSpecification.getDesignStoneSpecification().getNoOfStones();
				weightOfStones= customizationStoneSpecification.getTotalWeight().add(weightOfStones);
			}
			DesignStoneSpecification designStoneSpecification = stoneSpecificationsList.get(0).getDesignStoneSpecification();
			familyDetails.put(TYPE_OF_SETTING,designStoneSpecification.getSettingType());
			familyDetails.put(SHAPE, stoneSpecification.getShape());
			String clarity = stoneSpecification.getClarity();
			String color = stoneSpecification.getColor();
			String quality = color;
			if(!StringUtils.isBlank(clarity)){
				quality = clarity.concat(" ").concat(color);
				familyDetails.put(QUALITY, quality);
			}
			familyDetails.put(NUMBER_OF_STONES, noOfStones);
			familyDetails.put(TOTAL_WEIGHT, weightOfStones.setScale(2, RoundingMode.HALF_UP).toString()+" Carat");
			uniqueFamilesDetails.put(stoneType, familyDetails);
		}
    	return uniqueFamilesDetails;
    }

	public Map<String, Object> getDiamondDetails(){
    	String stoneFamilies = getStoneFamilies();
    	if(stoneFamilies.contains("Diamond")){
    		Map<String, Map<String, Object>> stoneFamiliesDetails = getStoneFamiliesDetails();
    		Map<String, Object> diamondDetails = stoneFamiliesDetails.get("Diamond");
    		return diamondDetails;
    	}else{
    		return null;
    	}
    }
    public String getStoneFamilies() {
        StringBuilder families = new StringBuilder();
        Set<String> stones = new TreeSet<String>();

        int index = 0;
        for (CustomizationStoneSpecification eachSpec : getCustomizationStoneSpecification()) {
            stones.add(eachSpec.getStoneSpecification().getStoneType());
        }
        for (String eachStone : stones) {
            families.append(eachStone);
            if (index + 1 < stones.size()) families.append(", ");
            index++;

        }
        return families.toString();
    }

    public String getMetalFamilies() {
        StringBuilder families = new StringBuilder();
        int index = 1;
        for (CustomizationMetalSpecification eachSpec : this.customizationMetalSpecification) {
            MetalSpecification metalSpecifications = eachSpec.getMetalSpecification();
            families.append(metalSpecifications.getColor() + " " + metalSpecifications.getType());
            if (index < this.customizationMetalSpecification.size()) families.append(", ");
            index++;
        }
        return families.toString();
    }

    public String getUrl() {
        return CustomizationUrlGenerator.getUrl(design);
        /*StringBuilder sbCustomizationUrl = new StringBuilder();
        sbCustomizationUrl.append(design.getDesignCategory().getCategoryType());
        sbCustomizationUrl.append("/").append(design.getDesignName().replace(" ", "-"));
        sbCustomizationUrl.append("~");
        sbCustomizationUrl.append(design.getId());
        sbCustomizationUrl.append(".html");

        StringBuilder sb = new StringBuilder();

        sb.append(sbCustomizationUrl.toString().toLowerCase().replace(' ','+').replace('\'', '-'));
        return sb.toString();*/
    }

    public int getImageVersion() {
        return imageVersion;
    }

    public void setImageVersion(int imageVersion) {
        this.imageVersion = imageVersion;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        //sb.append(super.toString());
        sb.append("Customization");
        sb.append("{\n\t").append(customizationMetalSpecification).append("\n");
        sb.append("\t").append(customizationStoneSpecification).append("\n");
        sb.append("\t").append(design).append("\n");
        sb.append("\t edit=").append(edit).append("\n");
        sb.append("\t expectedDeliveryDate=").append(expectedDeliveryDate).append("\n");
        sb.append("\t price=").append(price).append("\n");
        sb.append("\t priority=").append(priority).append("\n");
        sb.append("\t shortDescription=").append(shortDescription).append("\n");
        sb.append("\t skuCode=").append(skuCode).append("\n");
        sb.append("\t totalWeightOfProduct=").append(totalWeightOfProduct).append("\n");
        //sb.append("\t imageUrls=").append(imageUrls).append("\n");
        sb.append("\n}");
        return sb.toString();
    }

    @Override
    public String getName() {
        return design.getDesignName();
    }

    @Override
    public String getCategory() {
        return design.getDesignCategory().getCategoryType();
    }

    @Override
    public boolean isProductActive() {
        return isActive() && design.isActive();
    }

    @Override
    public String getSkuCode(String size) {
        return SkuCodeFactory.getSkuCodeWithSize(design.getDesignCategory(), skuCode, size);
    }

    @Override
    public String getImageUrl(String imageSize) {
        //@todo : Rahul Agrawal :  fix this dependency on first element and abstract this into CustomizationImages class.
        return getImageUrls().get(0).getImageOfSize(imageSize);
    }

    @Override
    public Map<String, Object> getProductDisplayFeatures(String size) {
        Map<String, Object> displayFeatures = new LinkedHashMap<String, Object>();
        Map<String, Map<String, Object>> stoneFamiliesDetails = getStoneFamiliesDetails();
        for (String eachKey : stoneFamiliesDetails.keySet()) {
            displayFeatures.put("Stone", eachKey);
            displayFeatures.putAll(stoneFamiliesDetails.get(eachKey));
        }
        for (CustomizationMetalSpecification eachCustomizationMetalSpecification : this.customizationMetalSpecification) {
            displayFeatures.put("Metal", eachCustomizationMetalSpecification.getMetalSpecification().getFullname());
            displayFeatures.put("Metal Weight", eachCustomizationMetalSpecification.getMetalWeight(size).setScale(2, RoundingMode.DOWN) + " gms");
        };

        return displayFeatures;
    }

    public Map<String, Object> getProductDisplayFeatures() {
    	return getProductDisplayFeatures(" ");
    }

	@Override
	public PRODUCT_TYPE getProductType() {
		return PRODUCT_TYPE.JEWELLERY;
	}

    @Override
    public String getCode() {
        return super.getProductCode(design.getId()) + "-" + getId();
    }

    public List<StoneSpecification> getStoneSpecificationsForUpdate() {
    	return stoneSpecificationsForUpdate;
    }

    public void setStoneSpecificationsForUpdate(List<StoneSpecification> stoneSpecificationsForUpdate) {
    	this.stoneSpecificationsForUpdate = stoneSpecificationsForUpdate;
    }

    public List<MetalSpecification> getMetalSpecificationsForUpdate() {
    	return metalSpecificationsForUpdate;
    }

    public void setMetalSpecificationsForUpdate(List<MetalSpecification> metalSpecificationsForUpdate) {
    	this.metalSpecificationsForUpdate = metalSpecificationsForUpdate;
    }

	public Customization getParentCustomization() {
		return parentCustomization;
	}

	public void setParentCustomization(Customization baseCustomization) {
		this.parentCustomization = baseCustomization;
	}

	public Set<Customization> getDerivedCustomizations() {
		return derivedCustomizations;
	}

	public void setDerivedCustomizations(Set<Customization> derivedCustomizations) {
		this.derivedCustomizations = derivedCustomizations;
	}

    public boolean hasDefaultPriority() {
        if (getPriority() == DEFAULT_PRIORITY) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((skuCode == null) ? 0 : skuCode.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof Customization)) {
            return false;
        }
        Customization other = (Customization) obj;
        if (skuCode == null) {
            if (other.skuCode != null) {
                return false;
            }
        } else if (!skuCode.equals(other.skuCode)) {
            return false;
        }
        return true;
    }
    
    

}
