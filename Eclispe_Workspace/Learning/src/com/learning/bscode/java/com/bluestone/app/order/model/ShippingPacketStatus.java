package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "shipping_packet_status")
@NamedQuery(name = "shippingPacketStatus.getShippingPacketStatus", query = "Select sps from ShippingPacketStatus sps where sps.statusName = :shippingPacketStatus")
public class ShippingPacketStatus extends BaseEntity {
    
    private static final long serialVersionUID = 1L;
    
    private String            statusName;
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ShippingPacketStatus [statusName=");
        builder.append(statusName);
        builder.append(", id=");
        builder.append(getId());
        builder.append("]");
        return builder.toString();
    }

    
    
    
}
