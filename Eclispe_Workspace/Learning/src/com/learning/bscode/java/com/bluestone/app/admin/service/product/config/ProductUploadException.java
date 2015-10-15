package com.bluestone.app.admin.service.product.config;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Throwables;

public class ProductUploadException extends Exception {

    private static final long serialVersionUID = 1L;
    private List<String> errorList = new ArrayList<String>();

    public ProductUploadException(List<String> errorList) {
        errorList.addAll(errorList);
    }

    public ProductUploadException(String errorMessage) {
        errorList.add(errorMessage);
    }

    public ProductUploadException(String errorMessage, Throwable e) {
        super(errorMessage, e);
        errorList.add(errorMessage);
    }

    public String generateErrorMessage() {

        StringBuilder errorMessageBuilder = new StringBuilder();

        for (String eachError : getErrorList()) {
            errorMessageBuilder.append(eachError).append(".\n");
        }
        return errorMessageBuilder.toString();
    }

    public List<String> getErrorList() {
        return errorList;
    }

}
