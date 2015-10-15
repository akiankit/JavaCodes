package com.bluestone.app.admin.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "carrier")
public class Carrier extends BaseEntity {
    
    private static final long    serialVersionUID = 1L;
    
    @Column(name = "name", columnDefinition = "varchar(64)", nullable = false)
    private String               companyName;
    
    @Column(name = "url", columnDefinition = "varchar(256)")
    private String               url;
    
    @Column(name = "priority", columnDefinition = "tinyint", nullable = false)
    private int                  priority;
    
    @Column(name = "account_number", columnDefinition = "varchar(32)")
    private String               accountNumber;
    
    @Column(name = "prepaid_limit", columnDefinition = "Decimal(20,2) default 0.00", nullable = false)
    private double               prepaidLimit;
    
    @Column(name = "cod_limit", columnDefinition = "Decimal(20,2) default 0.00", nullable = false)
    private double               codLimit;
    
    @OneToMany(mappedBy = "carrier", cascade = CascadeType.ALL)
    private List<CarrierZipcode> carrierZips;
    
    public List<CarrierZipcode> getCarrierZips() {
        return carrierZips;
    }
    
    public void setCarrierZips(List<CarrierZipcode> carrierZips) {
        this.carrierZips = carrierZips;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public int getPriority() {
        return priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public double getPrepaidLimit() {
        return prepaidLimit;
    }
    
    public void setPrepaidLimit(double prepaidLimit) {
        this.prepaidLimit = prepaidLimit;
    }
    
    public double getCodLimit() {
        return codLimit;
    }
    
    public void setCodLimit(double codLimit) {
        this.codLimit = codLimit;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        //sb.append(super.toString());
        sb.append("Carrier");
        sb.append("{\n\t accountNumber=").append(accountNumber).append("\n");
        sb.append("\t carrierZips=").append(carrierZips).append("\n");
        sb.append("\t codLimit=").append(codLimit).append("\n");
        sb.append("\t companyName=").append(companyName).append("\n");
        sb.append("\t prepaidLimit=").append(prepaidLimit).append("\n");
        sb.append("\t priority=").append(priority).append("\n");
        sb.append("\t url=").append(url).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
