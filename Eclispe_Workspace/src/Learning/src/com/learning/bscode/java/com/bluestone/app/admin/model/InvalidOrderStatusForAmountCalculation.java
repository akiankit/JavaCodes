package com.bluestone.app.admin.model;

public enum InvalidOrderStatusForAmountCalculation {
    PAYMENT_ERROR("Payment Error"),
    CANCELLED("Cancelled"),
    PREPARATION_IN_PROGRESS("Preparation in Progress"),
    COD_CALL1("COD-Call1"),
    COD_CALL2("COD-Call2"),
    COD_CALL3("COD-Call3"),
    COD_ON_HOLD("COD-On Hold");
    
    
    
    private String orderStatusName;
    
    private InvalidOrderStatusForAmountCalculation(String orderStatusName){
        this.orderStatusName=orderStatusName;
    }
    
    public String getOrderStatusName(){
        return this.orderStatusName;
    }
}
