package com.bluestone.app.order.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.account.model.User;
import com.bluestone.app.core.model.BaseEntity;

@Audited
@Entity
@NamedQuery(name = "orderHistory.getOrderStatesHistory", query = "select history from OrderHistory history where history.order =:order order by history.createdAt DESC")
@Table(name = "order_history")
public class OrderHistory extends BaseEntity implements Comparable<OrderHistory> {

    private static final Logger log = LoggerFactory.getLogger(OrderHistory.class);
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order             order;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User              user;
    
    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus       orderStatus;
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        log.trace("OrderHistory.setOrder()");
        this.order = order;
        order.addOrderHistory(this);
    }
    
    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public int compareTo(OrderHistory o) {
        return o.getCreatedAt().compareTo(this.getCreatedAt());
    }
}
