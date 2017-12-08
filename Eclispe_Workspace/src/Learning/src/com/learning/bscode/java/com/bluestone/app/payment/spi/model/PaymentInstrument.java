package com.bluestone.app.payment.spi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.core.util.Constants;

@Audited
@Entity
@Table(name = "paymentinstrument")
public class PaymentInstrument extends BaseEntity {

    private static final long serialVersionUID = 4113734141306910557L;

    // cashondelivery, creditcard, debitcard etc
    @Column(nullable=false)
    private String instrumentType = Constants.NONE;
    
    // visa, maestro, mastercard etc
    @Column(nullable=false)
    private String cardPaymentNetwork = Constants.NONE;

    // name of issuing bank in case of netbanking
    @Column(nullable=false)
	private String issuingAuthority = Constants.NONE;

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        if(!StringUtils.isBlank(instrumentType)) {
           this.instrumentType = instrumentType;
        }
    }

    public String getCardPaymentNetwork() {
        return cardPaymentNetwork;
    }

    public void setCardPaymentNetwork(String cardPaymentNetwork) {
        if(!StringUtils.isBlank(cardPaymentNetwork)) {
           this.cardPaymentNetwork = cardPaymentNetwork;
        }
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        if(!StringUtils.isBlank(issuingAuthority)) {
            this.issuingAuthority = issuingAuthority;
        }
    }

    @Override
    public String toString() {
        return "PaymentInstrument=[instrumentType=" + instrumentType + ", cardPaymentNetwork=" + cardPaymentNetwork
                + ", issuingAuthority=" + issuingAuthority + "]";
    }
	
    
	
}
