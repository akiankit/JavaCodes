package com.bluestone.app.design.solitaire;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.product.Product;

@Entity
@Table(name = "solitaire")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class Solitaire extends Product {
    
    private static final long   serialVersionUID = 1L;
    
    private static final Logger log              = LoggerFactory.getLogger(Solitaire.class);
    
    private String              itemId;
    private String              supplierStockRef;
    private String              cut;
    private BigDecimal          carat;
    private String              color;
    private String              naturalFancyColor;
    private String              naturalFancyColorIntensity;
    private String              naturalFancyColorOvertone;
    private String              treatedColor;
    private String              clarity;
    private String              make;
    private String              gradingLab;
    private String              certificateNumber;
    private String              certificatePath;
    private String              imagePath;
    private String              onlineReport;
    private BigDecimal          pricePerCarat;
    private BigDecimal          totalPrice;
    private String              polish;
    private String              symmetry;
    private String              measurements;
    private BigDecimal          depth;
    
    @Column(name="table_data")
    private BigDecimal          table_data;
    private BigDecimal          crownHeight;
    private BigDecimal          pavilionDepth;
    private String              girdle;
    private String              culetSize;
    private String              culetCondition;
    private String              graining;
    private String              fluorescenceIntensity;
    private String              fluorescenceColor;
    private String              enhancement;
    private String              supplier;
    private String              country;
    private String              state;
    private String              remarks;
    private String              phone;
    private String              pairStockRef;
    private String              email;
    
    @Column(name="is_sold", columnDefinition = "tinyint(1) default'0'")
    private boolean             isSold = false;
    
    @Column(name="is_available",  columnDefinition = "tinyint(1) default'1'")
    private boolean             isAvailable = true;

    public String getItemId() {
        return itemId;
    }
    
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    
    public String getSupplierStockRef() {
        return supplierStockRef;
    }
    
    public void setSupplierStockRef(String supplierStockRef) {
        this.supplierStockRef = supplierStockRef;
    }
    
    public String getCut() {
        return cut;
    }
    
    public void setCut(String cut) {
        this.cut = cut;
    }
    
    public BigDecimal getCarat() {
        return carat;
    }
    
    public void setCarat(BigDecimal carat) {
        this.carat = carat;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getNaturalFancyColor() {
        return naturalFancyColor;
    }
    
    public void setNaturalFancyColor(String naturalFancyColor) {
        this.naturalFancyColor = naturalFancyColor;
    }
    
    public String getNaturalFancyColorIntensity() {
        return naturalFancyColorIntensity;
    }
    
    public void setNaturalFancyColorIntensity(String naturalFancyColorIntensity) {
        this.naturalFancyColorIntensity = naturalFancyColorIntensity;
    }
    
    public String getNaturalFancyColorOvertone() {
        return naturalFancyColorOvertone;
    }
    
    public void setNaturalFancyColorOvertone(String naturalFancyColorOvertone) {
        this.naturalFancyColorOvertone = naturalFancyColorOvertone;
    }
    
    public String getTreatedColor() {
        return treatedColor;
    }
    
    public void setTreatedColor(String treatedColor) {
        this.treatedColor = treatedColor;
    }
    
    public String getClarity() {
        return clarity;
    }
    
    public void setClarity(String clarity) {
        this.clarity = clarity;
    }
    
    public String getMake() {
        return make;
    }
    
    public void setMake(String make) {
        this.make = make;
    }
    
    public String getGradingLab() {
        return gradingLab;
    }
    
    public void setGradingLab(String gradingLab) {
        this.gradingLab = gradingLab;
    }
    
    public String getCertificateNumber() {
        return certificateNumber;
    }
    
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }
    
    public String getCertificatePath() {
        return certificatePath;
    }
    
    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public String getOnlineReport() {
        return onlineReport;
    }
    
    public void setOnlineReport(String onlineReport) {
        this.onlineReport = onlineReport;
    }
    
    public BigDecimal getPricePerCarat() {
        return pricePerCarat;
    }
    
    public void setPricePerCarat(BigDecimal pricePerCarat) {
        this.pricePerCarat = pricePerCarat;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public String getPolish() {
        return polish;
    }
    
    public void setPolish(String polish) {
        this.polish = polish;
    }
    
    public String getSymmetry() {
        return symmetry;
    }
    
    public void setSymmetry(String symmetry) {
        this.symmetry = symmetry;
    }
    
    public String getMeasurements() {
        return measurements;
    }
    
    public void setMeasurements(String measurements) {
        this.measurements = measurements;
    }
    
    public BigDecimal getDepth() {
        return depth;
    }
    
    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }
    
    public BigDecimal getCrownHeight() {
        return crownHeight;
    }
    
    public void setCrownHeight(BigDecimal crownHeight) {
        this.crownHeight = crownHeight;
    }
    
    public BigDecimal getPavilionDepth() {
        return pavilionDepth;
    }
    
    public void setPavilionDepth(BigDecimal pavilionDepth) {
        this.pavilionDepth = pavilionDepth;
    }
    
    public String getGirdle() {
        return girdle;
    }
    
    public void setGirdle(String girdle) {
        this.girdle = girdle;
    }
    
    public String getCuletSize() {
        return culetSize;
    }
    
    public void setCuletSize(String culetSize) {
        this.culetSize = culetSize;
    }
    
    public String getCuletCondition() {
        return culetCondition;
    }
    
    public void setCuletCondition(String culetCondition) {
        this.culetCondition = culetCondition;
    }
    
    public String getGraining() {
        return graining;
    }
    
    public void setGraining(String graining) {
        this.graining = graining;
    }
    
    public String getFluorescenceIntensity() {
        return fluorescenceIntensity;
    }
    
    public void setFluorescenceIntensity(String fluorescenceIntensity) {
        this.fluorescenceIntensity = fluorescenceIntensity;
    }
    
    public String getFluorescenceColor() {
        return fluorescenceColor;
    }
    
    public void setFluorescenceColor(String fluorescenceColor) {
        this.fluorescenceColor = fluorescenceColor;
    }
    
    public String getEnhancement() {
        return enhancement;
    }
    
    public void setEnhancement(String enhancement) {
        this.enhancement = enhancement;
    }
    
    public String getSupplier() {
        return supplier;
    }
    
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPairStockRef() {
        return pairStockRef;
    }
    
    public void setPairStockRef(String pairStockRef) {
        this.pairStockRef = pairStockRef;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    /*@PostLoad
    public void setFormattedTotalPrice() {
        formattedTotalPrice = NumberUtil.formatPriceIndian(totalPrice.doubleValue(), ",");
    }*/

    public BigDecimal getTable_data() {
        return table_data;
    }

    public void setTable_data(BigDecimal table_data) {
        this.table_data = table_data;
    }

    @Override
    public String getName() {
        return carat + " Carat " + cut + " Diamond ";
    }

    @Override
    public String getCategory() {
        return PRODUCT_TYPE.SOLITAIRE.name();
    }

    @Override
    public BigDecimal getPrice() {
        return totalPrice;
    }

    @Override
    public BigDecimal getPrice(String size) {
        return totalPrice;  
    }

    @Override
    public boolean isProductActive() {
        return isActive() && !isSold && isAvailable;
    }
    
    public boolean getIsProductActive(){
    	return isProductActive();
    }

    @Override
    public String getSkuCode() {
        return "SOL_" + certificateNumber;
    }

    @Override
    public String getSkuCode(String size) {
        return "SOL_" + certificateNumber;
    }

    @Override
    public String getImageUrl(String imageSize) {
        return "/" + cut.toLowerCase() + "-tp-" + imageSize + ".jpg?version=1"; 
        // TODO:vikas version is hardcoded as of now. As we dont have design image view for solitaires, need to hardcode version here
    }

    @Override
    public String getUrl() {
        return "/solitaire/the-" + cut.toLowerCase() + "-solitaire~" + getId() + ".html";
    }
    
    @Override
	public PRODUCT_TYPE getProductType() {		
		return PRODUCT_TYPE.SOLITAIRE;
	}

    @Override
    public Map<String, Object> getProductDisplayFeatures(String size) {
        Map<String, Object> displayFeatures = new LinkedHashMap<String, Object>();
        displayFeatures.put("Shape", cut);
        displayFeatures.put("Carat", carat);
        displayFeatures.put("Clarity", clarity);
        displayFeatures.put("Color", color);
        return displayFeatures;
    }

    @Override
    public String getCode() {
        return super.getProductCode(getId());
    }

    @Override
    public String getShortDescription() {
        return getName();
    }

    public boolean getIsSold() {
        return isSold;
    }

    public void setSold(boolean isSold) {
        this.isSold = isSold;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

}
