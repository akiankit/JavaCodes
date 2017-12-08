package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@NamedQuery(name = "orderStatus.getOrderStatus", query = "Select os from OrderStatus os where os.statusName = :orderStatus")
@Table(name = "orderstatus")
public class OrderStatus extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    private String            statusName;
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    
}
