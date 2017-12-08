package com.bluestone.app.design.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.bluestone.app.core.model.BaseEntity;


@Entity
@Table(name = "ready_to_ship")
public class ReadyToShip extends BaseEntity {

    private static final long serialVersionUID = 1L;
    @Column(name = "skuCode", nullable = false, unique = true)
    private String skuCode;

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Override
    public String toString() {
        return getSkuCode();
    }
}
