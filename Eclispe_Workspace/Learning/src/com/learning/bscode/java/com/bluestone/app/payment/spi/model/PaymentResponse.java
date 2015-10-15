package com.bluestone.app.payment.spi.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.payment.spi.PaymentUtil.BLUESTONE_FIELDS;

@Audited
@Entity
@Table(name = "paymentresponse")
public class PaymentResponse extends BaseEntity {
    
    public enum GATEWAYRESPONSE {
        SUCCESS,
        FAILURE,
        PENDING
    }
    
    private static final long serialVersionUID = -9144669201411105323L;
    
    @OneToOne
    private PaymentInstrument paymentInstrument;
    
    @Column(scale=2)
    private BigDecimal        amount;
    
    private String            gatewayTransactionId;
    
    private String            bankReferenceNo;
    
    @Column(nullable=false ,columnDefinition = "bigint(20) default '0'")
    private long            clonedCartId;
    
    @Enumerated(EnumType.STRING)
    private GATEWAYRESPONSE gatewayResponse;
    
    @Transient
    private Map<BLUESTONE_FIELDS, String> merchantFields = new HashMap<BLUESTONE_FIELDS, String>();

    @Lob 
    private String failureMessage;

    @Lob 
    private String responseAsText;
    
    public String getGatewayTransactionId() {
        return gatewayTransactionId;
    }
    
    public void setGatewayTransactionId(String gatewayTransactionId) {
        this.gatewayTransactionId = gatewayTransactionId;
    }
    
    public String getBankReferenceNo() {
        return bankReferenceNo;
    }
    
    public void setBankReferenceNo(String bankReferenceNo) {
        this.bankReferenceNo = bankReferenceNo;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public PaymentInstrument getPaymentInstrument() {
        return paymentInstrument;
    }
    
    public void setPaymentInstrument(PaymentInstrument paymentInstrument) {
        this.paymentInstrument = paymentInstrument;
    }

    public Map<BLUESTONE_FIELDS, String> getMerchantFields() {
        return merchantFields;
    }

    public void setMerchantFields(Map<BLUESTONE_FIELDS, String> merchantFields) {
        this.merchantFields = merchantFields;
    }

    public void addMerchantField(BLUESTONE_FIELDS key, String value) {
        this.merchantFields.put(key, value);
        
    }

    public GATEWAYRESPONSE getGatewayResponse() {
        return gatewayResponse;
    }

    public void setGatewayResponse(GATEWAYRESPONSE gatewayResponse) {
        this.gatewayResponse = gatewayResponse;
    }
  
    public void setResponseAsText(String responseAsText) {
        this.responseAsText = responseAsText;
    }
    
    public String getResponseAsText() {
        return responseAsText;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }
    
    @Override
    public String toString() {
        return "PaymentResponse=Amount=" + amount
                + ", GatewayTransactionId=" + gatewayTransactionId + ", BankReferenceNo=" + bankReferenceNo
                + "\nGatewayResponse=" + gatewayResponse + "\nMerchantFields=" + merchantFields + "\nFailureMessage=" + getFailureMessage()
                + "\nResponseAsText=\n" + responseAsText + "]";
    }

    public long getClonedCartId() {
        return clonedCartId;
    }

    public void setClonedCartId(long clonedCartId) {
        this.clonedCartId = clonedCartId;
    }

}
