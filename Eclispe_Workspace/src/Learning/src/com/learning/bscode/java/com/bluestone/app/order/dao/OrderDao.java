package com.bluestone.app.order.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderHistory;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.OrderItemStatus;
import com.bluestone.app.order.model.OrderStatus;

@Repository("orderDao")
public class OrderDao extends BaseDao {
	
    private static final Logger log = LoggerFactory.getLogger(OrderDao.class);
    
    public List<Order> getAllOrdersForCustomer(Customer loggedInCustomer) {
        Query getAllOrdersForCustomerQuery = getEntityManagerWithoutFilter().createNamedQuery("order.getAllOrdersForCustomer");
        getAllOrdersForCustomerQuery.setParameter("customerId", loggedInCustomer.getId());
        return getAllOrdersForCustomerQuery.getResultList();
    }
    
    public List<OrderHistory> getOrderStatesHistory(Long orderId) {
        Query getOrderStatesHistoryQuery = getEntityManagerForActiveEntities().createNamedQuery("orderHistory.getOrderStatesHistory");
        getOrderStatesHistoryQuery.setParameter("order", find(Order.class, orderId, true));
        return getOrderStatesHistoryQuery.getResultList();
    }
    
    public OrderItemStatus getOrderItemStatus(String orderItemStatus) {
        try {
            Query getOrderItemStatusQuery = getEntityManagerForActiveEntities().createNamedQuery("orderItemStatus.getOrderItemStatus");
            getOrderItemStatusQuery.setParameter("orderItemStatus", orderItemStatus);
            return (OrderItemStatus) getOrderItemStatusQuery.getSingleResult();
        } catch (NoResultException e) {
            log.warn("Exception during GetOrderItemStatus({}) hence return a null", orderItemStatus, e);
            return null;
        }
    }
    
    public OrderStatus getOrderStatus(String orderStatus) {
        log.trace("OrderDao.getOrderStatus() for status={}", orderStatus);
        try {
            Query getOrderStatusQuery = getEntityManagerForActiveEntities().createNamedQuery("orderStatus.getOrderStatus");
            getOrderStatusQuery.setParameter("orderStatus", orderStatus);
            return (OrderStatus) getOrderStatusQuery.getSingleResult();
        } catch (NoResultException e) {
            log.warn("Exception during GetOrderStatus({}) hence return a null status", orderStatus, e);
            return null;
        }
    }
    
    public Order getOrderFromCartId(Long cartId) {
        try {
            Query getOrderFromCartQuery = getEntityManagerForActiveEntities().createNamedQuery("order.getOrderForCart");
            getOrderFromCartQuery.setParameter("cartId", cartId);
            return (Order) getOrderFromCartQuery.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<Cart> getAllCartsForCustomer(Customer customer) {
        Query getAllCartsForCustomerQuery = getEntityManagerWithoutFilter().createNamedQuery("cart.getAllCartsForCustomer");
        getAllCartsForCustomerQuery.setParameter("customer", customer);
        return getAllCartsForCustomerQuery.getResultList();
    }
    
    public OrderItem getOrderItemIdFrmUniwareItemIdCode(String uniwareId) {
        log.debug("OrderDao.getOrderItemId() from UniwareOrderItemId={}", uniwareId);
        try {
            Query namedQuery = getEntityManagerForActiveEntities().createNamedQuery("orderItem.getId");
            namedQuery.setParameter("uniwareId", uniwareId);
            return (OrderItem) namedQuery.getSingleResult();
        } catch (NoResultException exception) {
            log.error("Exception during GetOrderItemId from UniwareOrderItemId={}, hence return a null status.", uniwareId, exception);
            return null;
        }
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        log.debug("OrderDao.getOrderItems() from orderId={}", orderId);
        Query namedQuery = getEntityManagerForActiveEntities().createNamedQuery("orderItem.getOrderItems");
        namedQuery.setParameter("orderId", orderId);
        return namedQuery.getResultList();
    }

	/*public Order getOrderFormOrderCode(String orderCode) {
		try {
            Query namedQuery = getEntityManagerForActiveEntities().createQuery("select o from Order o where o.code = :orderCode");
            namedQuery.setParameter("orderCode", orderCode);
            return (Order) namedQuery.getSingleResult();
        } catch (NoResultException exception) {
            log.warn("Exception during fetching order from orderCode {}", orderCode, exception);
            return null;
        }
		
	}*/
    
}
