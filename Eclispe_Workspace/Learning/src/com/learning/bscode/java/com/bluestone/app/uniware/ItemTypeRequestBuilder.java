package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.util.Map;

import com.unicommerce.uniware.services.ItemTypeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 10/1/12
 */
class ItemTypeRequestBuilder {

    public enum ShipTogether {
        EXCLUSIVE, INTER_CATEGORY, INTRA_CATEGORY, INTRA_SKU
    }

    ;

    private static final Logger log = LoggerFactory.getLogger(ItemTypeRequestBuilder.class);

    private String categoryCode;

    private String itemSKU;

    private String name;

    private String description;

    private int length;

    private int width;

    private int height;

    private int weight;

    private String imageURL;

    private BigDecimal MRP;

    private Object[] tags;

    private String taxTypeCode;

    private String features;

    private String productPageUrl;

    private ShipTogether shipTogether;

    private Boolean traceable;

    private Map<String, String> customFields;

    ItemTypeRequestBuilder(UniwareItemType uniwareItemType) {
        categoryCode = uniwareItemType.getCategoryCode();
        itemSKU = uniwareItemType.getItemSKU();
        name = uniwareItemType.getName();
        description = uniwareItemType.getDescription();
        length = uniwareItemType.getLength();
        height = uniwareItemType.getHeight();
        width = uniwareItemType.getWidth();
        weight = uniwareItemType.getWeight();
        MRP = uniwareItemType.getMRP();
        imageURL = uniwareItemType.getImageURL();
        features = uniwareItemType.getFeatures();
        //tags = uniwareItemType.getTags();
        //taxTypeCode = uniwareItemType.getTaxTypeCode();
        //productPageUrl = uniwareItemType.getProductPageUrl();
        //shipTogether = uniwareItemType.getShipTogether();
        //traceable = uniwareItemType.getTraceable();
        //customFields = uniwareItemType.getCustomFields();
    }


    ItemTypeRequest create() {

        ItemTypeRequest itemTypeRequest = new ItemTypeRequest();

        /*if (shipTogether != null) {
            itemTypeRequest.setShipTogether(createShipTogethor(shipTogether));
        }*/

        itemTypeRequest.setCategoryCode(categoryCode);
        itemTypeRequest.setItemSKU(itemSKU);
        itemTypeRequest.setName(name);
        itemTypeRequest.setDescription(description);
        itemTypeRequest.setLength(length);
        itemTypeRequest.setHeight(height);
        itemTypeRequest.setWeight(weight);
        itemTypeRequest.setImageURL(imageURL);
        itemTypeRequest.setWidth(width);
        itemTypeRequest.setMRP(MRP);
        itemTypeRequest.setFeatures(features);
        if (tags != null) {
            itemTypeRequest.setTags(tags);
        }
        //itemTypeRequest.setCustomFields(CustomFieldBuilder.createCustomFields(customFields));
        //itemTypeRequest.setProductPageUrl(productPageUrl);
        //itemTypeRequest.setTaxTypeCode(taxTypeCode);
        //itemTypeRequest.setTraceable(traceable);
        return itemTypeRequest;
    }


    /* private ItemTypeRequestShipTogether createShipTogethor(ShipTogether shipTogether) {
        return ItemTypeRequestShipTogether.fromString(shipTogether.name());
    }*/

}
