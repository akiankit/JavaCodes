package com.bluestone.app.goldMineOrders.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment.GoldMinePlanPaymentStatus;
import com.bluestone.app.shipping.model.Address;

@Audited
@Entity
@Table(name = "goldmineplan")
public class GoldMinePlan extends BaseEntity{
	private static final long serialVersionUID = -4461143736086272397L;
	
	public enum GoldMinePlanStatus {
        MATURED,
        RUNNING,
        CANCELLED
    }

	@OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.TRUE)
    @BatchSize(size=20)
    @OneToMany(mappedBy = "goldMinePlan", cascade = CascadeType.ALL)
    private List<GoldMinePlanPayment>      goldMinePlanPayments ;
	
	@IndexedEmbedded(depth = 1)
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;
	
	@Column
	private String schemeName;
	
	@Column(nullable=false,scale=0)
    private BigDecimal amount;
	
	@Column(nullable = false)
    private Date    enrolledDate;
	
	@Column(nullable = false)
    private Date    maturityDate;
	
	@ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "address_id", nullable = true)
    private Address address;
	
	//status cancelled, matured, running -- enum
	 @Enumerated(EnumType.STRING)
	 private GoldMinePlanStatus goldMinePlanStatus;

    private String code;
	
	public String getSchemeName() {
		return schemeName;
	}
	
	public int getNumberOfPayments(){
		int schemeTerm = GoldMinePlanSchemes.getSchemeTerm(schemeName);
		return GoldMinePlanSchemes.getUserPayments(schemeTerm);
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<GoldMinePlanPayment> getGoldMinePlanPayments() {
		return goldMinePlanPayments;
	}

	public void setGoldMinePlanPayments(List<GoldMinePlanPayment> goldMinePlanPayments) {
		this.goldMinePlanPayments = goldMinePlanPayments;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public GoldMinePlanStatus getGoldMinePlanStatus() {
		return goldMinePlanStatus;
	}

	public void setGoldMinePlanStatus(GoldMinePlanStatus goldMinePlanStatus) {
		this.goldMinePlanStatus = goldMinePlanStatus;
	}
	
	public Date getLastGoldMinePaymentDate() {
		Date dueDate = this.goldMinePlanPayments.get(this.goldMinePlanPayments.size() - 1).getDueDate();
		return dueDate;
	}
	
	public Date getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(Date maturityDate) {
		this.maturityDate = maturityDate;
	}

    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public GoldMinePlanPayment getNextDuePlanPayment(){
    	GoldMinePlanPayment nextDuePlanPayment = null;
    	for (GoldMinePlanPayment goldMinePlanPayment: goldMinePlanPayments) {
			if(goldMinePlanPayment.getCart() !=null && !(goldMinePlanPayment.getGoldMinePlanPaymentStatus().equals(GoldMinePlanPaymentStatus.PLANNED_PAYMENT))){
				continue;
			}else{
				nextDuePlanPayment = goldMinePlanPayment;
				break;
			}
		}
    	return nextDuePlanPayment;
    }
    
    public GoldMinePlanPayment getLatestPaidPayment(){
    	GoldMinePlanPayment latestPlanPayment = null;
    	for (GoldMinePlanPayment goldMinePlanPayment: goldMinePlanPayments) {
			if(goldMinePlanPayment.getGoldMinePlanPaymentStatus().equals(GoldMinePlanPaymentStatus.PAID) || goldMinePlanPayment.getGoldMinePlanPaymentStatus().equals(GoldMinePlanPaymentStatus.LATE_PAYMENT)){
				latestPlanPayment = goldMinePlanPayment;
			}
		}
    	return latestPlanPayment;
    }
    
    public int getPaidPaymentsCount(){
    	int paidPaymentsCount = 0;
    	for (GoldMinePlanPayment goldMinePlanPayment: goldMinePlanPayments) {
			if(goldMinePlanPayment.getCart() !=null){
				paidPaymentsCount++;
			}else{
				break;
			}
		}
    	return paidPaymentsCount;
    }
	
	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GoldMinePlan=");
        sb.append("{\n\t Amount=").append(amount).append("\n");
        sb.append("\t SchemeName=").append(schemeName).append("\n");
        sb.append("\t Customer=").append(customer).append("\n");
        sb.append("\t Code=").append(code).append("\n");
        sb.append("\t enrolledDate=").append(enrolledDate).append("\n");
        sb.append("\t goldMineplanStatus=").append(goldMinePlanStatus).append("\n");
        sb.append("\n}");
        return sb.toString();
    }

}
