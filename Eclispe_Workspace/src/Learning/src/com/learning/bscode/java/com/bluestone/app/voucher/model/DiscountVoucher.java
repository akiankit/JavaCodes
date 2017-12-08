package com.bluestone.app.voucher.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.DateTimeUtil;

@Audited
@Entity
@Table(name = "discountvoucher", uniqueConstraints = @UniqueConstraint(columnNames = { "name" }))
public class DiscountVoucher extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    public enum VoucherType {
        PERCENTAGE, AMOUNT
    }
    
    @NotNull(message = "Name can not be null.")
    @Column(name = "name", nullable = false)
    private String      name;
    
    @Enumerated(EnumType.STRING)
    private VoucherType voucherType;
    
    @Column(nullable = false, scale = 2)
    private BigDecimal  value;
    
    private String      description;
    
    @NotNull(message = "MaxAllowed can not be null.")
    private int         maxAllowed;
    
    @NotNull(message = "Valid From can not be null.")
    private Date        validFrom;
    
    @NotNull(message = "Valid To can not be null.")
    private Date        validTo;
    
    @Column(nullable = false, scale = 2)
    private BigDecimal  minimumAmount;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public VoucherType getVoucherType() {
        return voucherType;
    }
    
    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    public void setValue(BigDecimal amount) {
        this.value = amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public int getMaxAllowed() {
        return maxAllowed;
    }
    
    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }
    
    public Date getValidFrom() {
        return validFrom;
    }
    
    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    
    public Date getValidTo() {
        return validTo;
    }
    
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }
    
    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }
    
    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }
    
    public void decrementMaxAllowed() {
        maxAllowed = maxAllowed - 1;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DiscountVoucher");
        sb.append(" { name='").append(name).append('\'');
        sb.append(", voucherType=").append(voucherType);
        sb.append(", description='").append(description).append('\'');
        sb.append(", value=").append(value);
        sb.append(", maxAllowed=").append(maxAllowed);
        sb.append(", validFrom=").append(DateTimeUtil.getSimpleDateAndTime(validFrom));
        sb.append(", validTo=").append(DateTimeUtil.getSimpleDateAndTime(validTo));
        sb.append(", minimumAmount=").append(minimumAmount);
        sb.append('}');
        sb.append("\n");
        return sb.toString();
    }
}
