package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "shippingitem")
@NamedQueries({ @NamedQuery(name = "ShippingItem.findShippingItemFromOrderItem", query = "select si from ShippingItem si where si.orderItem =:orderItem") })
public class ShippingItem extends BaseEntity {
    
    private static final long serialVersionUID = -5721625782565295743L;
    
    @OneToOne
    @JoinColumn(name = "shipping_packet_id")
    private ShippingPacket    shippingPacket;
    
    @OneToOne
    @JoinColumn(name = "orderitem_id")
    private OrderItem         orderItem;
    
    public ShippingPacket getShippingPacket() {
        return shippingPacket;
    }
    
    public void setShippingPacket(ShippingPacket shippingPacket) {
        this.shippingPacket = shippingPacket;
    }
    
    public OrderItem getOrderItem() {
        return orderItem;
    }
    
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
    
}
