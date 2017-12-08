package com.bluestone.app.payment.spi.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "masterpayment")
public class MasterPayment extends BaseEntity {
    
    public enum MasterPaymentStatus {
        PARTIAL_PAYMENT_MADE, 
        PAYMENT_FULLY_MADE, 
        NO_PAYMENT_MADE
    }
    
    private static final long   serialVersionUID = 1L;
    
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart                cart;
    
    @Enumerated(EnumType.STRING)
    private MasterPaymentStatus paymentStatus;
    
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "masterPayment", fetch = FetchType.EAGER)
    @BatchSize(size=20)
    private List<SubPayment>    subPayments;
    
    public MasterPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(MasterPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public List<SubPayment> getSubPayments() {
        return subPayments;
    }
    
    public void addSubPayment(SubPayment subPayment) {
        if(this.subPayments == null) {
            this.subPayments = new ArrayList<SubPayment>();
        }
        this.subPayments.add(subPayment);
    }
    
    public void setSubPayments(List<SubPayment> subPayments) {
        this.subPayments = subPayments;
    }
    
    public SubPayment getLatestSubPayment() {
        if (this.subPayments != null && this.subPayments.size() > 0) {
            return subPayments.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MasterPayment");
        sb.append("{\t Id=").append(getId()).append("\n");
        sb.append("{\t cartId=").append(cart.getId()).append("\n");
        sb.append("\t paymentStatus=").append(paymentStatus).append("\n");
        sb.append("\n}");
        return sb.toString();
    }
}
