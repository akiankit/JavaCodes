package com.bluestone.app.checkout.cart.service;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.ProductService;

@Service
public class CartQuantityValidator {


    private static final Logger  log = LoggerFactory.getLogger(CartQuantityValidator.class);
    
    @Autowired
    private ProductService productService;
    
    public String validateOnParentQuantityChange(int parentCartItemQuantity, CartItem parentCartItem) {
    	log.debug("CartQuantityValidator.validateOnParentQuantityChange() parentCartItemQuantity {}, parentCartItemId {}",	parentCartItemQuantity, parentCartItem);
		String validateParentChildQuantities;
		//List<CartItem> childCartItems = cartDao.getDependentCartItems(parentProduct, cartItems);
		int noOfParentItems = parentCartItemQuantity;
		String parentCategory = parentCartItem.getProduct().getCategory();
		String childCategory = "";
		Set<CartItem> childCartItems = parentCartItem.getChildCartItems();
		for (CartItem cartItem : childCartItems) {
			childCategory = cartItem.getProduct().getCategory();
			break;
		}
		
		int noOfChildItems = parentCartItem.getChildQuantityCount();
		validateParentChildQuantities = validateParentChildCartQuantity(true, noOfParentItems, noOfChildItems, childCategory, parentCategory).toString();
		return validateParentChildQuantities;
	}

    public String validateOnChildQuantityChange(Cart cart, CartItem parentCartItem, long productId, int userSelectedCartItemQuantity) {
        return validateOnChildQuantityChange(cart, parentCartItem, productService.getProduct(productId), userSelectedCartItemQuantity);
    }
    
    public String validateOnChildQuantityChange(Cart cart, CartItem parentCartItem, Product childProduct, int userSelectedCartItemQuantity) {
    	log.debug("CartQuantityValidator.validateOnChildQuantityChange() cart {}, parentCartItem {}, childProduct {}, userSelectedCartItemQuantity {}",
				cart, parentCartItem, childProduct, userSelectedCartItemQuantity);
    	
    	StringBuilder errorMessage = new StringBuilder("");
    	if(productService.isCustomizationCategoryChild(childProduct)){
    	    boolean isParentProductAvailable = false;
            int existingParentQuantity = 0;
            int existingChildQuantity = 0;
            
            String parentCategory = "";
            if(parentCartItem != null) {
                parentCategory = parentCartItem.getProduct().getCategory();
                isParentProductAvailable = true;
                existingParentQuantity = parentCartItem.getQuantity();
                
                Set<CartItem> childCartItems = parentCartItem.getChildCartItems();
                for (CartItem eachChildCartItem : childCartItems) {
                     if(eachChildCartItem.getProduct().getId() == childProduct.getId()) {
                        // ignore this as we are adding user selected quantity below
                     } else {
                         existingChildQuantity += eachChildCartItem.getQuantity();
                     }
                }
            }
			
			existingChildQuantity += userSelectedCartItemQuantity;
        	String childCategory = childProduct.getCategory();
			errorMessage = validateParentChildCartQuantity(isParentProductAvailable, existingParentQuantity, existingChildQuantity, childCategory, parentCategory);
    	}
    	return errorMessage.toString();
	}

	private StringBuilder validateParentChildCartQuantity(boolean isParentProductAvailable, int parentQuantity,	int childQuantity, String childCategory, String parentCategory) {
		log.debug("CartQuantityValidator.validateParentChildCartQuantity() isParentProductAvailable {} , parentQuantity {} , childQuantity {} , childCategory {}, parentCategory {}",
				 isParentProductAvailable, parentQuantity, childQuantity, childCategory, parentCategory);
    	StringBuilder errorMessage = new StringBuilder("");
		if(isParentProductAvailable == false){
			errorMessage.append(childCategory).append(" can not be purchased individually.");
			if(StringUtils.isNotBlank(parentCategory)) {
				errorMessage.append("First add ").append(parentCategory).append(" and later choose ").append(childCategory);
			}
			log.error(errorMessage.toString());
		} else {
			if (childQuantity > parentQuantity) {  
				errorMessage.append("Quantity of ").append(childCategory).append(" can not be more than quantity of ").append(parentCategory);
				log.error(errorMessage.toString());
			}
		}
		return errorMessage;
	}


}
