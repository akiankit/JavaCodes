package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "orderitem_status")
@NamedQuery(name = "orderItemStatus.getOrderItemStatus", query = "Select ois from OrderItemStatus ois where ois.statusName = :orderItemStatus")
public class OrderItemStatus extends BaseEntity {

    private static final long serialVersionUID = -3770991192129829186L;

    private String statusName;

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
