package com.bluestone.app.reporting.order;

import java.util.Date;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.reporting.Report;

public class OrderReport extends Report {
    
    private final String id;
    private final Date expectedDeliveryDate;
    private final String code;
    private final Date orderDate;
    private final String customerName;
    private final String orderTotal;
    private final String paymentGateway;
    private final String orderStatus;
    private final String paymentInstrument;
    private final String discountVoucherName;
    private final String discountPrice;
    private final String bluestoneTxnId;
    
    public String getId() {
        return id;
    }
    
    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }
    
    public String getCode() {
        return code;
    }
    
    public Date getOrderDate() {
        return orderDate;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public String getOrderTotal() {
        return orderTotal;
    }
    
    public String getPaymentGateway() {
        return paymentGateway;
    }
    
    public String getOrderStatus() {
        return orderStatus;
    }
    
    public String getPaymentInstrument() {
        return paymentInstrument;
    }
    
    public String getDiscountVoucherName() {
        return discountVoucherName;
    }
    
    public String getDiscountPrice() {
        return discountPrice;
    }
    
    public String getBluestoneTxnId() {
        return bluestoneTxnId;
    }
    
    public OrderReport(String id, Date expectedDeliveryDate, String code, Date orderDate, String customerName, 
                       String orderTotal, String orderStatus, String paymentGateway, String paymentInstrument,
                       String bluestoneTxnId, String discountVoucherName, String discountPrice) {
        this.id = id;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.code = code;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.orderTotal = orderTotal;
        this.paymentGateway = paymentGateway;
        this.paymentInstrument = paymentInstrument;
        this.orderStatus = orderStatus;
        this.bluestoneTxnId = bluestoneTxnId;
        this.discountVoucherName = discountVoucherName;
        this.discountPrice = discountPrice;
    }
    
    // TODO As of now there is a hardcoding between column names and values
    @Override
    public String[] asCSVRecord() {
        return new String[] { id, code, customerName, orderTotal, paymentGateway,
                getPaymentInstrument(), orderStatus, DateTimeUtil.formatDate(orderDate),
                DateTimeUtil.formatDate(expectedDeliveryDate), discountVoucherName, discountPrice,
                bluestoneTxnId };
    }
    
}
