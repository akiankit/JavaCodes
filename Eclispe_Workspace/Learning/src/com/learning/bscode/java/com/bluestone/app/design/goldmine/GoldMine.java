package com.bluestone.app.design.goldmine;

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


// insert into product(createdAt,updatedAt,version,is_active,productType) values (now(),now(),0,1,'GOLDMINE');
// insert into jewelvault(id) values(49546);
@Entity
@Table(name = "goldmine")
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class GoldMine extends Product {
    
    private static final long serialVersionUID = 1L;
    
    private static final Logger log = LoggerFactory.getLogger(GoldMine.class);
    
    @Column(nullable=false,scale=0)
    private BigDecimal amount;
    
    @Override
    public String getName() {
        return "GOLD MINE SAVING PLAN";
    }

    @Override
    public String getCategory() {
        return PRODUCT_TYPE.GOLDMINE.name();
    }

    @Override
    public BigDecimal getPrice() {
        return amount;
    }

    @Override
    public BigDecimal getPrice(String size) {
        return getPrice();  
    }

    @Override
    public boolean isProductActive() {
        return isActive();
    }

    @Override
    public String getSkuCode() {
        return "GM_"+amount.intValue();
    }

    @Override
    public String getSkuCode(String size) {
        return getSkuCode();
    }

    @Override
    public String getImageUrl(String imageSize) {
        return "/goldmine.jpg?version=1"; 
        // TODO:vikas version is hardcoded as of now. As we dont have design image view for jewelvault, need to hardcode version here
    }

    @Override
    public String getUrl() {
        return "/goldmine.html";
    }
    
    @Override
    public PRODUCT_TYPE getProductType() {      
        return PRODUCT_TYPE.GOLDMINE;
    }

    @Override
    public Map<String, Object> getProductDisplayFeatures(String size) {
        Map<String, Object> displayFeatures = new LinkedHashMap<String, Object>();
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
