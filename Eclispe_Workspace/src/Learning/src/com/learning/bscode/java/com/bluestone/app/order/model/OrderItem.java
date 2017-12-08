package com.bluestone.app.order.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.envers.Audited;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.core.model.BaseEntity;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.order.model.UniwareStatus.ORDER_ITEM_STATUS;
import com.bluestone.app.shipping.model.Address;

@Audited
@Entity
@NamedQueries( {
              @NamedQuery(name = "orderItem.getId", query = "Select orderItem from OrderItem orderItem join fetch orderItem.cartItem ci" +
              		                                            " where orderItem.uniwareOrderItemId = :uniwareId"),
              @NamedQuery(name = "orderItem.getOrderItems", query = "Select orderItem from OrderItem orderItem where orderItem.order.id = :orderId")
    }
)


@Table(name = "orderitem")
public class OrderItem extends BaseEntity {

    private static final Logger log = LoggerFactory.getLogger(OrderItem.class);

    private static final long serialVersionUID = -1440723161336922413L;
    
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "cart_item_id", nullable = false)
    private CartItem cartItem;       

    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "orderItem", cascade=CascadeType.ALL, orphanRemoval=true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @BatchSize(size=20)
    private List<OrderItemHistory> orderItemHistoryList = new ArrayList<OrderItemHistory>();
	
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;
    
    private BigDecimal price;
    
    private String uniwareOrderItemId;

    @Transient
    private int packetNumber = 0; // default value Zero means flexible orders.
    
    @Column(columnDefinition = "tinyint(1)", nullable = false)
    private short isCancellable = 1;
    //private boolean isCancellable;

    @PostPersist
    public void populateUniwareOrderItemId(){
        uniwareOrderItemId=Long.toString(getId());
        log.debug("OrderItem.populateUniwareOrderItemId(): uniwareOrderIdItem={}", uniwareOrderItemId);
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

	public CartItem getCartItem() {
        return cartItem;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setCartItem(CartItem cartItem) {
        this.cartItem = cartItem;
    }
    
    public List<OrderItemHistory> getOrderItemHistoryList() {
		return orderItemHistoryList;
	}

	public void setOrderItemHistoryList(List<OrderItemHistory> orderItemHistoryList) {
		this.orderItemHistoryList = orderItemHistoryList;
	}
	
	public void addOrderItemHistory(OrderItemHistory orderItemHistory) {
        log.trace("OrderItem.addOrderItemHistory()");
		orderItemHistoryList.add(orderItemHistory);
	}
	
	public OrderItemHistory getLatestOrderItemHistory(){
		if(this.orderItemHistoryList != null && this.orderItemHistoryList.size() > 0) {
			return orderItemHistoryList.get(0);
		} else {
			return null;
		}
	}

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUniwareOrderItemId() {
        return uniwareOrderItemId;
    }

    public void setUniwareOrderItemId(String uniwareOrderItemId) {
        this.uniwareOrderItemId = uniwareOrderItemId;
    }

    public OrderItemStatus getStatus() {
        OrderItemStatus orderItemStatus = null;
        OrderItemHistory latestOrderItemHistory = getLatestOrderItemHistory();
        if (latestOrderItemHistory != null) {
            orderItemStatus = latestOrderItemHistory.getOrderItemStatus();
        } else {
            // not required but keeping it for finding bug in migration etc
            log.warn("OrderItem={} Status should not be null. It may happen if the item history was never populated in cases like when the order is not in Uniware DB." , getId());
        }
        return orderItemStatus;
    }

    public boolean isCancelled() {
        boolean isCancelled = false;
        OrderItemStatus latestStatus = getStatus();
        if (latestStatus != null) {
            isCancelled = ORDER_ITEM_STATUS.CANCELLED.getValue().equals(latestStatus.getStatusName());
        }
        return isCancelled;
    }

    public boolean isCancellable() {
        if (isCancellable == 1) {
            return true;
        } else {
            return false;
        }
    }

    public void setCancellable(boolean cancellable) {
        if (cancellable) {
            setIsCancellable((short) 1);
        } else {
            setIsCancellable((short) 0);
        }
    }

    public short getIsCancellable() {
        return isCancellable;
    }

    public void setIsCancellable(short cancellable) {
        isCancellable = cancellable;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public void setPacketNumber(int packetNumber) {
        this.packetNumber = packetNumber;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" OrderItem:");
        sb.append("[Id=").append(getId());
        sb.append(" , uniwareOrderItemId=").append(uniwareOrderItemId);
        sb.append(" , ProductType=").append(getProductType().name());
        sb.append(" , packetNumber=").append(packetNumber);
        sb.append("]");
        return sb.toString();
    }

    public Product.PRODUCT_TYPE getProductType(){
        return this.getCartItem().getProduct().getProductType();
    }
}
