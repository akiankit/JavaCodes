package com.bluestone.app.payment.spi.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "subpayment")
public class SubPayment extends BaseEntity {

    private static final long serialVersionUID = -1714939863464422479L;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="master_payment_id",nullable=false)
    private MasterPayment masterPayment;
    
    @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name = "payment_transaction_id", nullable = false)
    private PaymentTransaction paymentTransaction;
    
    public MasterPayment getMasterPayment() {
        return masterPayment;
    }

    public void setMasterPayment(MasterPayment masterPayment) {
        this.masterPayment = masterPayment;
        masterPayment.addSubPayment(this);
    }
    
    public PaymentTransaction getPaymentTransaction() {
        return paymentTransaction;
    }

    public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
        this.paymentTransaction = paymentTransaction;
    }

}
