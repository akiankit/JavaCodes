package com.bluestone.app.payment.spi.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.bluestone.app.core.model.BaseEntity;

@Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
@Entity
@Table(name = "paymenttransaction")
public class PaymentTransaction extends BaseEntity {

    private static final long serialVersionUID = 7856370062905311189L;

    public enum PaymentTransactionStatus {
        CREATED,
        COD_ON_HOLD,
        COD_CONFIRMED,
        COD_CANCELLED,
        PDC_ACCEPTED,
        REPLACEMENT_ORDER,
        NET_BANKING_TRANSFER,
        CHEQUE_PAID,
        PAYMENT_ACCEPTED,
        PAYMENT_REQUEST_STARTED,
        SUCCESS,
        FAILURE,
        PENDING,
        AUTHORIZATION_SUCCESS,
        AUTHORIZATION_FAILURE,
        AUTHORIZATION_PENDING;
    }
    
    @Column(nullable = true)
    private String                   customerIP;
    
    @Column(nullable = true)
    private String                   bluestoneTxnId; // this is encrypted
    
    private String                   flowExecutionId;
    
    @Column(scale = 2, nullable = false)
    private BigDecimal               amount;
    
    @OneToOne
    private PaymentInstrument        paymentInstrument;
    
    @Enumerated(EnumType.STRING)
    private PaymentGateway           paymentGateway;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_response_id")
    private PaymentResponse    paymentResponse;
    
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "capture_transaction_id")
    private CaptureTransaction    captureTransaction;
    
    @OneToOne(mappedBy = "paymentTransaction")
    private SubPayment               subPayment;
    
    @Enumerated(EnumType.STRING)
    private PaymentTransactionStatus paymentTransactionStatus;
    
    @OneToMany(mappedBy = "paymentTransaction", cascade = CascadeType.ALL)
    private List<PaymentMetaData> paymentMetaDatas = new ArrayList<PaymentMetaData>();
    
    public PaymentInstrument getPaymentInstrument() {
        return paymentInstrument;
    }
    
    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public PaymentGateway getPaymentGateway() {
        return paymentGateway;
    }
    
    public void setPaymentGateway(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }
    
  
    public SubPayment getSubPayment() {
        return subPayment;
    }
    
    public void setSubPayment(SubPayment subPayment) {
        this.subPayment = subPayment;
    }
    
    public PaymentTransactionStatus getPaymentTransactionStatus() {
        return paymentTransactionStatus;
    }
    
    public void setPaymentTransactionStatus(PaymentTransactionStatus paymentTransactionStatus) {
        this.paymentTransactionStatus = paymentTransactionStatus;
    }
    
    public String getCustomerIP() {
        return customerIP;
    }
    
    public void setCustomerIP(String customerIP) {
        this.customerIP = customerIP;
    }
    
    public String getFlowExecutionId() {
        return flowExecutionId;
    }
    
    public void setFlowExecutionId(String flowExecutionId) {
        this.flowExecutionId = flowExecutionId;
    }

    public PaymentResponse getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(PaymentResponse paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

    /**
     * EncryptedTrnasactionId
     * @return
     */
    public String getBluestoneTxnId() {
        return bluestoneTxnId;
    }

    public void setBluestoneTxnId(String bluestoneTxnId) {
        this.bluestoneTxnId = bluestoneTxnId;
    }
    
    public List<PaymentMetaData> getPaymentMetaDatas() {
		return paymentMetaDatas;
	}

	public void setPaymentMetaDatas(List<PaymentMetaData> paymentMetaDatas) {
		this.paymentMetaDatas = paymentMetaDatas;
	}

    public CaptureTransaction getCaptureTransaction() {
		return captureTransaction;
	}

	public void setCaptureTransaction(CaptureTransaction captureTransaction) {
		this.captureTransaction = captureTransaction;
	}
	
	public String getPaymentMetaDataValue(PaymentMetaData.Name name) {
		for (PaymentMetaData paymentMetaData : paymentMetaDatas) {
			if (paymentMetaData.getName().equals(name)) {
				return paymentMetaData.getValue();
			}
		}
		return null;
	}

	@Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PaymentTransaction Id=").append(getId());
        sb.append("{\n\t Amount=").append(amount).append("\n");
        sb.append("\t BluestoneTxnId=").append(bluestoneTxnId).append("\n");
        sb.append("\t CustomerIP=").append(customerIP).append("\n");
        sb.append("\t FlowExecutionId=").append(flowExecutionId).append("\n");
        sb.append("\t PaymentGateway=").append(paymentGateway).append("\n");
        if (paymentInstrument == null) {
            sb.append("\t PaymentInstrument=").append(paymentInstrument).append("\n");
        } else {
            sb.append("\t ").append(paymentInstrument).append("\n");
        }
        sb.append("\t Payment Transaction Status=").append(paymentTransactionStatus).append("\n");
        sb.append("\t SubPayment=").append(subPayment).append("\n");
        if (paymentResponse == null) {
            sb.append("\t PaymentResponse=null").append("\n");
        } else {
            sb.append("\t ").append(paymentResponse).append("\n");
        }
        if (captureTransaction == null) {
            sb.append("\t CaptureTransaction=null").append("\n");
        } else {
            sb.append("\t ").append(captureTransaction).append("\n");
        }
        if(paymentMetaDatas == null) {
        	sb.append("\t PaymentMetaData=null").append("\n");
        } else {
        	sb.append("\t ").append(paymentMetaDatas).append("\n");
        }
        sb.append("\n}");
        return sb.toString();
    }

    public String toStringBrief() {
        final StringBuilder sb = new StringBuilder();
        //sb.append(super.toString());
        sb.append("PaymentTransaction Id=").append(getId());
        sb.append("[\n\t Amount=").append(amount).append("\n");
        sb.append("\t BluestoneTxnId=").append(bluestoneTxnId).append("\n");
        sb.append("\t CustomerIP=").append(customerIP).append("\n");
        sb.append("\t FlowExecutionId=").append(flowExecutionId).append("\n");
        sb.append("\t PaymentGateway=").append(paymentGateway).append("]\n");
        if (paymentInstrument == null) {
            sb.append("\t PaymentInstrument=").append(paymentInstrument).append("\n");
        } else {
            sb.append("\t ").append(paymentInstrument).append("\n");
        }
        sb.append("\t Payment Transaction Status=").append(paymentTransactionStatus).append("\n");
        //sb.append("\t SubPayment=").append(subPayment).append("\n");
        return sb.toString();
    }

}
