package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "shipping_packet_history")
public class ShippingPacketHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "shipping_packet_id", nullable = false)
    private ShippingPacket shippingPacket;
    
    @ManyToOne
    @JoinColumn(name = "shipping_packet_status_id", nullable = false)
    private ShippingPacketStatus   shippingPacketStatus;

    public ShippingPacketStatus getShippingPacketStatus() {
        return shippingPacketStatus;
    }

    public void setShippingPacketStatus(ShippingPacketStatus shippingPacketStatus) {
        this.shippingPacketStatus = shippingPacketStatus;
    }

    public ShippingPacket getShippingPacket() {
        return shippingPacket;
    }

    public void setShippingPacket(ShippingPacket shippingPacket) {
        this.shippingPacket = shippingPacket;
    }
    
}
