package com.bluestone.app.order.model;


public class OrderCancellationResponse {

    private final boolean isSuccess;
    
    private String message;
    
    public OrderCancellationResponse(boolean isSuccess, String message) {
        super();
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("OrderCancellationResponse [isSuccess=");
        builder.append(isSuccess);
        builder.append(", message=");
        builder.append(message);
        builder.append("]");
        return builder.toString();
    }
    
}
