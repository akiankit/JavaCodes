package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Rahul Agrawal
 *         Date: 10/30/12
 */
class UniwareItemType {

    private String categoryCode;

    private String itemSKU;

    private String name;

    private String description;

    private int length;

    private int width;

    private int height;

    private int weight;

    private BigDecimal MRP;

    private String taxTypeCode;

    private String features;

    private String imageURL;

    private String productPageUrl;

    private ItemTypeRequestBuilder.ShipTogether shipTogether;

    private Boolean traceable = true;

    private Object[] tags;

    private Map<String, String> customFields;

    /*public UniwareItemType(String categoryCode, String itemSKU, String name, String imageURL) {
        this.setCategoryCode(categoryCode);
        this.setItemSKU(itemSKU);
        this.setName(name);
        this.setImageURL(imageURL);
    }

    public UniwareItemType(String categoryCode, String itemSKU, String name, String description, String imageURL) {
        this.setCategoryCode(categoryCode);
        this.setItemSKU(itemSKU);
        this.setName(name);
        this.description = description;
        this.setImageURL(imageURL);
    }*/

    UniwareItemType() {
    }


    public String getCategoryCode() {
        return categoryCode;
    }

    public String getItemSKU() {
        return itemSKU;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public BigDecimal getMRP() {
        return MRP;
    }

    public void setMRP(BigDecimal MRP) {
        this.MRP = MRP;
    }

    public String getTaxTypeCode() {
        return taxTypeCode;
    }

    public void setTaxTypeCode(String taxTypeCode) {
        this.taxTypeCode = taxTypeCode;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getProductPageUrl() {
        return productPageUrl;
    }

    public void setProductPageUrl(String productPageUrl) {
        this.productPageUrl = productPageUrl;
    }

    public ItemTypeRequestBuilder.ShipTogether getShipTogether() {
        return shipTogether;
    }

    public void setShipTogether(ItemTypeRequestBuilder.ShipTogether shipTogether) {
        this.shipTogether = shipTogether;
    }

    public Boolean getTraceable() {
        return traceable;
    }

    public Object[] getTags() {
        return tags;
    }

    public void setTags(Object[] tags) {
        this.tags = tags;
    }

    public Map<String, String> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, String> customFields) {
        this.customFields = customFields;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UniwareItemType");
        sb.append("{\n\t categoryCode=").append(getCategoryCode()).append("\n");
        sb.append("\t name=").append(getName()).append("\n");
        sb.append("\t itemSKU=").append(getItemSKU()).append("\n");
        sb.append("\t imageURL=").append(getImageURL()).append("\n");
        sb.append("\t description=").append(description).append("\n");
        sb.append("\t weight=").append(weight).append("\n");
        sb.append("\t height=").append(height).append("\n");
        sb.append("\t length=").append(length).append("\n");
        sb.append("\t width=").append(width).append("\n");
        sb.append("\t MRP=").append(MRP).append("\n");
        sb.append("\t tags=").append(tags == null ? "null" : Arrays.asList(tags).toString()).append("\n");
        sb.append("\t customFields=").append(customFields).append("\n");
        sb.append("\t features=").append(features).append("\n");
        sb.append("\t productPageUrl=").append(productPageUrl).append("\n");
        sb.append("\t shipTogether=").append(shipTogether).append("\n");
        sb.append("\t taxTypeCode=").append(taxTypeCode).append("\n");
        sb.append("\t traceable=").append(traceable).append("\n");
        sb.append("\n}");
        return sb.toString();
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItemSKU(String itemSKU) {
        this.itemSKU = itemSKU;
    }
}
