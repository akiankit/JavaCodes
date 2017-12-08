package com.bluestone.app.payment.spi.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.payment.spi.model.PaymentResponse.GATEWAYRESPONSE;

@Entity
@Table(name = "capturetransaction")
public class CaptureTransaction extends BaseEntity{

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
    private GATEWAYRESPONSE gatewayResponse;
    
    @Lob 
    private String requestAsText;

    @Lob 
    private String responseAsText;
    
    @Column(columnDefinition = "varchar(255)")
    private String failureMessage;

	public String getFailureMessage() {
		return failureMessage;
	}

	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}

	public GATEWAYRESPONSE getGatewayResponse() {
		return gatewayResponse;
	}

	public void setGatewayResponse(GATEWAYRESPONSE gatewayResponse) {
		this.gatewayResponse = gatewayResponse;
	}

	public String getRequestAsText() {
		return requestAsText;
	}

	public void setRequestAsText(String requestAsText) {
		this.requestAsText = requestAsText;
	}

	public String getResponseAsText() {
		return responseAsText;
	}

	public void setResponseAsText(String responseAsText) {
		this.responseAsText = responseAsText;
	}

	@Override
	public String toString() {
		return "CaptureTransaction [gatewayResponse=" + gatewayResponse + ", requestAsText=" + requestAsText + ", responseAsText=" + responseAsText + "]";
	}    
    
}
