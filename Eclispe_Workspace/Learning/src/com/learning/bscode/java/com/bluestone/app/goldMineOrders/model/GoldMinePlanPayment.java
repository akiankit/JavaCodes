package com.bluestone.app.goldMineOrders.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.payment.spi.model.MasterPayment;

@Audited
@Entity
@Table(name = "goldmineplanpayment")
@NamedQuery(name="getPlanPaymentById",query="select gmp from GoldMinePlanPayment gmp where gmp.id =:paymentId")
public class GoldMinePlanPayment extends BaseEntity{
	private static final long serialVersionUID = 1L;
	
	public enum GoldMinePlanPaymentStatus {
		OVERDUE,
        DUE,
        PAID,
        LATE_PAYMENT,
        PLANNED_PAYMENT
    }
	
	@ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name = "gold_mine_plan_id", nullable = false)
	private GoldMinePlan             goldMinePlan;
	 
	@Column(nullable = false)
	private Date    dueDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_payment_id", nullable = true)
    private MasterPayment        masterPayment;
	
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = true)
    private Cart                cart;
	
	@Enumerated(EnumType.STRING)
	private GoldMinePlanPaymentStatus goldMinePlanPaymentStatus;

    private String code;
    
    @Column(nullable = true)
    private String comment;
	 
	 public GoldMinePlan getGoldMinePlan() {
		return goldMinePlan;
	}

	public void setGoldMinePlan(GoldMinePlan goldMinePlan) {
		this.goldMinePlan = goldMinePlan;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public GoldMinePlanPaymentStatus getGoldMinePlanPaymentStatus() {
		return goldMinePlanPaymentStatus;
	}

	public void setGoldMinePlanPaymentStatus(GoldMinePlanPaymentStatus goldMinePlanPaymentStatus) {
		this.goldMinePlanPaymentStatus = goldMinePlanPaymentStatus;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    public MasterPayment getMasterPayment() {
        return masterPayment;
    }

    public void setMasterPayment(MasterPayment masterPayment) {
        this.masterPayment = masterPayment;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GoldMinePlanPayment [goldMinePlan=");
        builder.append(goldMinePlan);
        builder.append(", dueDate=");
        builder.append(dueDate);
        builder.append(", masterPayment=");
        builder.append(masterPayment);
        builder.append(", cart=");
        builder.append(cart);
        builder.append(", goldMinePlanPaymentStatus=");
        builder.append(goldMinePlanPaymentStatus);
        builder.append(", code=");
        builder.append(code);
        builder.append("]");
        return builder.toString();
    }

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
