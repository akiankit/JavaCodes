package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.account.model.User;
import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@Table(name = "orderitem_history")
public class OrderItemHistory extends BaseEntity {

    private static final Logger log = LoggerFactory.getLogger(OrderItemHistory.class);

    private static final long serialVersionUID = 6600356214841360489L;

    @ManyToOne
    @JoinColumn(name = "orderitem_id", nullable = false)
    private OrderItem             orderItem;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User             user;
    
    @ManyToOne
    @JoinColumn(name = "orderitem_status_id", nullable = false)
    private OrderItemStatus       orderItemStatus;

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
        log.debug("OrderItemHistory.setOrderItem()");
		this.orderItem = orderItem;
		orderItem.addOrderItemHistory(this);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderItemStatus getOrderItemStatus() {
		return orderItemStatus;
	}

	public void setOrderItemStatus(OrderItemStatus orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}
    
}
