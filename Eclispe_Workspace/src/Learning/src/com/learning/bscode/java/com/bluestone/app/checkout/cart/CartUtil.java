package com.bluestone.app.checkout.cart;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.service.ProductService;

@Component
public class CartUtil {

	private static final Logger log = LoggerFactory.getLogger(CartUtil.class);
	
	private static ProductService productService;

	@Autowired
    public void setProductService(ProductService productService) {
        CartUtil.productService = productService;
    }

	public static void addCartToSession(Cart cart, HttpSession session) throws Exception {
		if (cart != null) {
			session.setAttribute("cartId", cart.getId());
			Util.getMessageContext().put(MessageContext.CART_ID, cart.getId());
		} else {
			throw new Exception("Null cart is beong passed to set in session");
		}

	}

	public static void removeCartFromSession(HttpSession session) {
		try {
			session.removeAttribute("cartId");
			Util.getMessageContext().put(MessageContext.CART_ID, 0l);
		} catch (Throwable th) {
			Util.getMessageContext().put(MessageContext.CART_ID, 0l);
			log.error("Error in getting cart form session:", th.getMessage());
		}
	}

	public static Long getCartIdFromSession(HttpSession httpSession) {
		try {
			Object cartIdAttribute = httpSession.getAttribute(MessageContext.CART_ID);
			if (cartIdAttribute != null) {
				return (Long) cartIdAttribute;
			} else {
				return null;
			}
		} catch (Throwable th) {
			log.error("Error in getting cart form session:", th.getMessage());
			return null;
		}
	}

	/*public static Map<Long, List<Long>> getParentChildMap(Cart cart) throws Exception {
		return cartParentChildAssociationService.getParentChildMap(cart);
	}*/
	
	public static String getChildCategory(String parentCategory) throws Exception {
	    List<String> childCategoryByParent = productService.getChildCategoryByParent(parentCategory);
	    if(childCategoryByParent.size() > 0) {
	        return childCategoryByParent.get(0);
	    }
        return null;
    }
	
}
