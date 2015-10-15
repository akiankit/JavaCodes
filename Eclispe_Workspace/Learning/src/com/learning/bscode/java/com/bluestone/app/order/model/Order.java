package com.bluestone.app.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.voucher.model.DiscountVoucher;

@Audited
@Entity
@NamedQueries({
        @NamedQuery(name = "order.getAllOrdersForCustomer", query = "select order from Order order where order.cart in  (select cart from Cart cart where cart.customer in"
                + "(select customer from Customer customer where customer.id=:customerId)) order by order.createdAt DESC"),
        @NamedQuery(name = "order.getOrderForCart", query = "select order from Order order where order.cart in  (select cart from Cart cart where cart.id=:cartId) ") })
@Table(name = "orders")
public class Order extends BaseEntity {
    
    private static final long    serialVersionUID    = 2758991990267023615L;
    
    private static final Logger  log                 = LoggerFactory.getLogger(Order.class);
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @BatchSize(size=20)
    private List<OrderItem>      orderItems          = new ArrayList<OrderItem>();
    
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @BatchSize(size=20)
    private List<OrderHistory>   orderhistory        = new ArrayList<OrderHistory>();
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "latestOrderStatus_id", nullable = false)
    private OrderStatus latestOrderStatus;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="discount_voucher_id",nullable=true)
    private DiscountVoucher discountVoucher;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "master_payment_id", nullable = true)
    private MasterPayment        masterPayment;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart                 cart;
    
    @Column(nullable = true)
    private Date                 actualDeliveryDate;
    
    private String               code;
    
    private BigDecimal totalOrderAmount;
    
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;
    
    @Column(scale=2)
    private BigDecimal discountPrice = new BigDecimal(0);
    
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.TRUE)
    @JoinTable(name = "order_customervouchers", joinColumns = @JoinColumn(name = "orders_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "discountvoucher_id", referencedColumnName = "id"))
    @BatchSize(size=3)
    private Set<DiscountVoucher> vouchersForCustomer = new HashSet<DiscountVoucher>();
    
    @Column(nullable = false)
    private Date                 expectedDeliveryDate;
    
    public MasterPayment getMasterPayment() {
        return masterPayment;
    }
    
    public void setMasterPayment(MasterPayment masterPayment) {
        this.masterPayment = masterPayment;
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    
    public Date getActualDeliveryDate() {
        return actualDeliveryDate;
    }
    
    public void setActualDeliveryDate(Date actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }
    
    public Date getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }
    
    public void setExpectedDeliveryDate(Date expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }
    
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
    
    public List<OrderItem> getOrderItems(PRODUCT_TYPE product_type) {
        List<OrderItem> filteredOrderItems = new LinkedList<OrderItem>();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getProductType().equals(product_type)) {
                filteredOrderItems.add(orderItem);
            }
        }
        return filteredOrderItems;
    }
    
    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    
    public Set<DiscountVoucher> getVouchersForCustomer() {
        return vouchersForCustomer;
    }
    
    public void setVouchersForCustomer(Set<DiscountVoucher> vouchersForCustomer) {
        this.vouchersForCustomer = vouchersForCustomer;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public void addOrderItem(OrderItem eachOrderItem) {
        orderItems.add(eachOrderItem);
    }
    
    public List<OrderHistory> getOrderhistory() {
        return orderhistory;
    }
    
    public void setOrderhistory(List<OrderHistory> orderhistory) {
        this.orderhistory = orderhistory;
    }
    
    public void addOrderHistory(OrderHistory orderHistory) {
        orderhistory.add(orderHistory);
    }
    
    public OrderHistory getLatestOrderHistory() {
        log.trace("Order.getLatestOrderHistory()");
        if (this.orderhistory != null && this.orderhistory.size() > 0) {
            return orderhistory.get(0);
        } else {
            return null;
        }
    }
    
    public OrderStatus getStatus() {
        log.trace("Order.getStatus(): OrderId={}", getId());
        OrderStatus orderStatus = null;
        OrderHistory latestOrderHistory = getLatestOrderHistory();
        if (latestOrderHistory == null) {
            log.warn("****Order id={} has status = null. Please verify.", getId());
        } else {
            orderStatus = latestOrderHistory.getOrderStatus();
        }
        return orderStatus;
    }
    
    public BigDecimal getFinalPrice() {
        return totalOrderAmount;//getTotalPrice().subtract(getDiscountPrice());
    }
    
    public BigDecimal getTotalPrice() {
        BigDecimal total = new BigDecimal(0);
        for (OrderItem eachItem : orderItems) {
            if (eachItem.isActive()) {
                total = total.add(eachItem.getPrice());
            }
        }
        //return totalOrderAmount;//total;
        return total;
    }
    
    public BigDecimal getTotalOrderAmount() {
        return totalOrderAmount;
    }

    public void setTotalOrderAmount(BigDecimal totalOrderAmount) {
        this.totalOrderAmount = totalOrderAmount;
    }

    public DiscountVoucher getDiscountVoucher() {
        return discountVoucher;
    }

    public void setDiscountVoucher(DiscountVoucher discountVoucher) {
        this.discountVoucher = discountVoucher;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public OrderStatus getLatestOrderStatus() {
        return latestOrderStatus;
    }

    public void setLatestOrderStatus(OrderStatus latestOrderStatus) {
        this.latestOrderStatus = latestOrderStatus;
    }
    
}
