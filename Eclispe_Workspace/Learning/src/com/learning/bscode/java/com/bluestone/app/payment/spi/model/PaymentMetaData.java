package com.bluestone.app.payment.spi.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name="payment_meta_data")
public class PaymentMetaData extends BaseEntity{
	
	public enum Name {
		SCHEME_TERM,
        PAYMENTPLAN_ID,
        CLONED_CART_ID,
        FLOW_EXECUTION_ID
	}
	
	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
	private Name  name;
	
    @Column(name = "value",nullable=false, length = 20000)
	private String value;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_ransaction_id", nullable = false)
    private PaymentTransaction paymentTransaction;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public PaymentTransaction getPaymentTransaction() {
		return paymentTransaction;
	}

	public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}

	public Name getName() {
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "PaymentMetaData [name=" + name + ", value=" + value + "]";
	}
	
	
}
