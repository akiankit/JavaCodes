package com.bluestone.app.admin.model;

import org.apache.commons.lang.StringUtils;


public class OrderListFilterCriteria extends ListFilterCriteria {
    
    public String getSortBy() {
        return StringUtils.isBlank(sortBy) ? "orders.id" : sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = StringUtils.isBlank(sortBy) ? "orders.id" : sortBy;
    }
    
    public String getColumn() {
        return StringUtils.isBlank(column) ? "orders.id" : column;
    }

    public void setColumn(String column) {
        this.column = StringUtils.isBlank(column) ? "orders.id" : column;
    }
}
