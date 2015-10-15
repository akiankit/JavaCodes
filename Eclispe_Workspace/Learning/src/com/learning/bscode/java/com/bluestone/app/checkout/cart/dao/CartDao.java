package com.bluestone.app.checkout.cart.dao;

import java.util.List;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.dao.BaseDao;

@Repository("cartDao")
public class CartDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(CartDao.class);

/*    public List<CartItem> getDependentCartItems(Product product, List<CartItem> cartItems) {
    	if(product != null && cartItems != null && cartItems.size() > 0){
    		Query getDependentCartItemsQuery = getEntityManagerForActiveEntities().createNamedQuery("CartItem.getAllChildCartItems");
            getDependentCartItemsQuery.setParameter("cartItems", cartItems);
            getDependentCartItemsQuery.setParameter("parentProductId", product.getId());
            return getDependentCartItemsQuery.getResultList();
    	}
    	return new ArrayList<CartItem>();
    }

    public CartItem getParentCartItem(Product parentProduct, List<CartItem> cartItems) {
        Query getParentCartItemQuery = getEntityManagerForActiveEntities().createNamedQuery("CartItem.getParentCartItem");
        getParentCartItemQuery.setParameter("cartItems", cartItems);
        getParentCartItemQuery.setParameter("product", parentProduct);
        return (CartItem) getParentCartItemQuery.getSingleResult();
    }*/

    public Cart getLatestCartForCustomer(Customer customer) {
        Query cartQuery = getEntityManagerForActiveEntities().createNamedQuery("Cart.latestCartForCustomer");
        cartQuery.setParameter("customer", customer);
        List resultList = cartQuery.getResultList();
        if(resultList != null &&  resultList.size() > 0) {
            return (Cart) resultList.get(0);
        }
        return null;
    }

    public Cart getVisitorCart(Visitor visitor) {
        Query cartQuery = getEntityManagerForActiveEntities().createNamedQuery("Cart.latestCartForVisitor");
        cartQuery.setParameter("visitor", visitor);
        List resultList = cartQuery.getResultList();
        if(resultList != null &&  resultList.size() > 0) {
            return (Cart) resultList.get(0);
        }
        return null;
    }

}
