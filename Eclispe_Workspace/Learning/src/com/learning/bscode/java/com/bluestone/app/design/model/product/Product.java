package com.bluestone.app.design.model.product;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.Field;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(name="product.getAllOneTypeProducts",query="select p from Product p where p.productType =:productType")
public abstract class Product extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    public enum PRODUCT_TYPE {
        JEWELLERY, SOLITAIRE, GOLDMINE
    }
    
    @Enumerated(EnumType.STRING)
    private PRODUCT_TYPE productType;
    
    public PRODUCT_TYPE getProductType() {
        return productType;
    }
    
    public void setProductType(PRODUCT_TYPE productType) {
        this.productType = productType;
    }
    
    @Field
    @Column(name = "score",columnDefinition="bigint(20) default '1'", nullable=false)
    private long  score;
    
    public abstract Map<String, Object> getProductDisplayFeatures(String size);
    
    public abstract String getImageUrl(String imageSize);
    
    public abstract String getUrl();
    
    public abstract String getName();
    
    public abstract String getCategory();
    
    public abstract BigDecimal getPrice();
    
    public abstract BigDecimal getPrice(String size);
    
    public abstract boolean isProductActive();
    
    public abstract String getSkuCode();
    
    public abstract String getSkuCode(String size);
    
    public abstract String getCode();
    
    public abstract String getShortDescription();
    
    protected String getProductCode(long id) {
        char[] zeros = new char[6];
        Arrays.fill(zeros, '0');
        // format number as String
        DecimalFormat df = new DecimalFormat(String.valueOf(zeros));
        return df.format(id);
    }

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}
    
}
