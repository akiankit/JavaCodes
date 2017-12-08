package com.bluestone.app.reporting.order;

import java.util.Date;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.reporting.Report;

public class GoldMineReport extends Report {
    
    private final String id;
    private final String code;
    private final Date enrollementDate;
    private final String customerName;
    private final String customerEmail;
    private final String installmentAmount;
    private final String status;
    private final String schemeName;
    private final Date maturityDate;
    private final String cumulativeAmount;
    
    public GoldMineReport(String id, String code, String customerName, 
                       String customerEmail, String installmentAmount, String schemeName,
                       String status, Date enrollementDate, Date maturityDate,String cumulativeAmount) {
        this.id = id;
        this.code = code;
        this.enrollementDate = enrollementDate;
        this.customerName = customerName;
        this.customerEmail=customerEmail;
        this.installmentAmount = installmentAmount;
        this.schemeName=schemeName;
        this.status = status;
        this.maturityDate=maturityDate;
        this.cumulativeAmount=cumulativeAmount;
    }

	@Override
	public String[] asCSVRecord() {
		return new String[]{id, code, customerName, customerEmail, installmentAmount, schemeName, status,
							DateTimeUtil.formatDate(enrollementDate),DateTimeUtil.formatDate(maturityDate),cumulativeAmount};
	}
    
}
