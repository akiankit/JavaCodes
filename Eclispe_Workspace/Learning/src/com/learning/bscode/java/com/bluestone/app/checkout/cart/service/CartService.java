package com.bluestone.app.checkout.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.VisitorService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.checkout.cart.CartUtil;
import com.bluestone.app.checkout.cart.dao.CartDao;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.DataSheetConstants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.ProductService;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CartService {
    
    private static final Logger  log = LoggerFactory.getLogger(CartService.class);
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CartDao              cartDao;
    
    @Autowired
    private VisitorService       visitorService;
    
    @Autowired
    private CartQuantityValidator cartQuantityValidator;
    
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deleteCartItem(CartItem cartItem, Cart cart) {
        log.debug("CartService.deleteCartItem(): CartItem Id={}, Cart Id {}", cartItem.getId(), cart.getId());
        removeCartItem(cartItem, cart);
        assertMessageContextHasCartId();
    }

    @RaiseEvent(eventType = EventType.CART_CREATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Cart createCart(Long visitorId, Customer customer) {
        log.debug("CartService.createCart(): Visitor Id={}", visitorId);
        Cart cart = new Cart();
        if (visitorId != null) {
            Visitor visitor = visitorService.getVisitor(visitorId);
            cart.setVisitor(visitor);
        } else {
            log.error("Visitor Id is null. There must be some problem in parsing cookie. Needs to be Investigated");
        }
        
        if (customer != null) {
            // if customer was already logged in apart from checkout flow,
            // associate customer with the cart
            cart.setCustomer(customer);
        }
        cartDao.create(cart);
        MessageContext messageContext = Util.getMessageContext();
        messageContext.put(MessageContext.CART_ID, cart.getId());
        return cart;
    }

    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CartItem addItemToCart(Cart cart, Long productId, String size, int quantity, CartItem parentCartItem) throws Exception {
        log.debug("CartService.addItemToCart(): cartId={}, customizationId={}, size={}", cart, productId, size);
        CartItem cartItem = null;
        Product product = null;
        if (productId != null && productId > 0) {
            product = productService.getProduct(productId);
        }
        if (product != null && product.isProductActive()) {
            cartItem = createOrUpdateCartItem(size, product, cart, quantity, parentCartItem);
            log.debug("CartService.addItemToCart(): CartItem={}", cartItem);;
        } else {
            throw new Exception("Item with id:" + productId + " doesn't exist or no longer active");
        }
        assertMessageContextHasCartId();
        return cartItem;

    }
    
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CartItem addItemToCart(Cart cart, Long productId, int quantity) throws Exception {
        return addItemToCart(cart, productId, null, quantity, null);
    }
    
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public CartItem addItemToCart(Cart cart, Long productId,String size,int quantity) throws Exception {
        return addItemToCart(cart, productId, size, quantity, null);
    }

    private void assertMessageContextHasCartId() {
        MessageContext messageContext = Util.getMessageContext();
        Object cartId = messageContext.get(MessageContext.CART_ID);
        if (cartId == null) {
            log.warn("CartService.addItemToCart() : The message context does not have a cart id. This is an error*****");
        }
    }

    private CartItem createOrUpdateCartItem(String size, Product product, Cart cart, int quantity, CartItem parentCartItem) {
        log.debug("CartService.createOrUpdateCartItem(): Size{} , cart={} , quantity={}", size, cart, quantity);
        String sKuCodeWithSize = product.getSkuCode(size);
        CartItem item = cart.getCartItem(sKuCodeWithSize, parentCartItem);
        if (item == null) {
            item = new CartItem(product, size, sKuCodeWithSize);
            item.setCart(cart);
            cartDao.create(item);
		} else {
			String category = item.getProduct().getCategory();
			boolean isSolitaire = category.equalsIgnoreCase(Product.PRODUCT_TYPE.SOLITAIRE.name());
			boolean isSolitaireRingMount = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_RING_MOUNT.getName().equalsIgnoreCase(category);
			boolean isSolitairePendantMount = DataSheetConstants.DESIGN_CATEGORYTYPE.SOLITAIRE_PENDANT_MOUNT.getName().equalsIgnoreCase(category);
			if (isSolitaire || isSolitairePendantMount || isSolitaireRingMount ) {
				log.warn("Tried to add same solitaire category product {}. Not increasing the quantity ", product.getId());
				return item;
			}}
        if (parentCartItem != null) {
            item.setParentCartItem(parentCartItem);
        }
        item.incrementQuantity(quantity);
        return cartDao.update(item);
    }
    
    private CartItem mergeVisitorCartItemInCustomerCart(CartItem visitorCartItem, Cart customerCart, CartItem mergedCustomerParentCartItem) {
        log.debug("CartService.createOrUpdateCartItem(): CartItemId={} , CartId={}", visitorCartItem.getId(), customerCart.getId());
        return createOrUpdateCartItem(visitorCartItem.getSize(),
                                      visitorCartItem.getProduct(),
                                      customerCart,
                                      visitorCartItem.getQuantity(),
                                      mergedCustomerParentCartItem);
    }
    
    public Cart getActiveCart(long cartId) {
        return cartDao.find(Cart.class, cartId, true);
    }
    
    public Cart getInactiveCart(long cartId) {
        return cartDao.find(Cart.class, cartId, false);
    }
    
    public Cart getCart(long cartId) {
        return cartDao.findAny(Cart.class, cartId);
    }
    
    public CartItem getCartItem(long cartItemId) {
        return cartDao.findAny(CartItem.class, cartItemId);
    }
    
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Cart updateCart(Cart cart) {
        log.debug("CartService.updateCart(): CartId={}", cart.getId());
        Cart updatedCart = cartDao.update(cart);
        assertMessageContextHasCartId();
        return updatedCart;
    }
    
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Map<String, Object> updateQuantity(Long cartId, Long cartItemId, int cartItemQuantity) {
        log.debug("CartService.updateQuantity(): CartId={} , cartItem Id={} , cart item quantity={}", cartId, cartItemId, cartItemQuantity);
        Map<String, Object> cartItemDetails = new HashMap<String, Object>();
        CartItem cartItemTobeUpdated = cartDao.find(CartItem.class, cartItemId, true);
        Cart cart = getCart(cartId);

        Product product = cartItemTobeUpdated.getProduct();
        String validateParentChildQuantities = null;
        
        if (productService.isCustomizationCategoryChild(product)) {
        	CartItem parentCartItem = cartItemTobeUpdated.getParentCartItem();
            //long parentCustomizationId = parentCartItem.getProduct().getId();
        	validateParentChildQuantities = cartQuantityValidator.validateOnChildQuantityChange(cart, parentCartItem, product, cartItemQuantity);
        } else {
            validateParentChildQuantities = cartQuantityValidator.validateOnParentQuantityChange(cartItemQuantity, cartItemTobeUpdated);
        }
        
        if(!StringUtils.isBlank(validateParentChildQuantities)) {
        	cartItemDetails.put("errorMessage", validateParentChildQuantities);
        	cartItemDetails.put("cartItem", cartItemTobeUpdated);
        	return cartItemDetails;
    	}
        
        cartItemTobeUpdated.setQuantity(cartItemQuantity);
        cartItemTobeUpdated = cartDao.update(cartItemTobeUpdated);
        cartItemDetails.put("cartItem", cartItemTobeUpdated);
        assertMessageContextHasCartId();
        return cartItemDetails;
    }

    public void removeCartItem(CartItem cartItem, Cart cart) {
        List<CartItem> cartItems = cart.getCartItems();
        CartItem parentCartItem = cartItem.getParentCartItem();
        if(parentCartItem != null) {
            parentCartItem.getChildCartItems().remove(cartItem); // remove association from parent collection of this child
        }
        cartItems.remove(cartItem); // from cart level collection
        cartItems.removeAll(cartItem.getDeepChildItemsList()); // remove all childs of this item from cart level collection
    }
    
    public void removeChildItems(CartItem cartItem,Cart cart){
    	List<CartItem> cartItems = cart.getCartItems();
    	cartItems.removeAll(cartItem.getDeepChildItemsList()); // from cart level collection
    }
    
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Cart removeDiscountVoucher(Cart cart) {
        log.debug("CartService.removeDiscountVoucher(): Cart Id={}", cart.getId());
        cart.setDiscountVoucher(null);
        cart.setDiscountPrice(BigDecimal.valueOf(0));
        return cartDao.update(cart);
    }
    
    public Cart getLatestCartForCustomer(Customer customer) {
        return cartDao.getLatestCartForCustomer(customer);
        
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void assignCartToCustomer(Cart cart, Customer customer) {
       cart.setCustomer(customer);
       updateCart(cart);
    }
    
    public Cart getVisitorCart(Visitor visitor) {
        log.debug("CartService.getVisitorCart(): visitor={}" , visitor);
        Cart visitorCart = null;
        if(visitor != null) {
            visitorCart = cartDao.getVisitorCart(visitor);
        }
        log.debug("CartService.getVisitorCart(): visitorCart={}" , visitorCart);
        return visitorCart;
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void setCustomerCartInSession(Customer customer, HttpSession httpsession) throws Exception {
        log.debug("CartService.setCustomerCartInSession for {}" , customer);
        Long visitorId = (Long) Util.getMessageContext().get(MessageContext.VISITOR_ID);
        Visitor visitor = null;
        if(visitorId != null) {
            visitor = visitorService.getVisitor(visitorId);
        }
        Cart visitorCart = getVisitorCart(visitor);
        log.debug("CartService.setCustomerCartInSession for visitorCart={}" , visitorCart);
        Cart customerCart = getLatestCartForCustomer(customer);
        log.debug("CartService.setCustomerCartInSession for customerCart {}" , customerCart);
        
        // if customer cart is null , and visitor cart not null or empty, just assign customer to that cart  
        if(customerCart == null && visitorCart != null) {
            log.debug("Customer cart is null and visitor cartId={} is present. Hence assigning customer to that cart. i.e {}", visitorCart.getId(), visitorCart);
            // associate assign customer to cart
            assignCartToCustomer(visitorCart, customer);
            return;
        }
        
        // if customer has cart , and visitor cart is null or empty, set customer cart in cookie
        if(customerCart != null && (visitorCart == null || visitorCart.getItemCount() == 0)) {
            log.debug("Customer cart Id={} is present and visitor cart is empty or null. Hence assigning customer cart in cookie. Customer Cart={}", customerCart.getId(), customerCart);
            CartUtil.addCartToSession(customerCart, httpsession);
            return;
        }
        
        // if customer has cart , and visitor also has cart, merge visitor cart in with customer cart , and set customer cart in cookie
        if(customerCart != null && visitorCart != null) {
            if(customerCart.getId() != visitorCart.getId()) {
                log.debug("Customer cartId={} is present and visitor cart Id={}  is also present. Merging both the carts", customerCart.getId(), visitorCart.getId());
                // merge carts as both the cart are not same
                List<CartItem> activeParentCartItems = visitorCart.getActiveParentCartItems();
                for (CartItem eachVistorParentCartItem : activeParentCartItems) {
                    // first merge all parent cart items 
                    CartItem mergedCustomerParentCartItem = mergeVisitorCartItemInCustomerCart(eachVistorParentCartItem, customerCart, null);
                    Set<CartItem> childCartItems = eachVistorParentCartItem.getChildCartItems();
                    // now merge all active child cart items
                    for (CartItem eachVisitorChildCartItem : childCartItems) {
                        if(eachVisitorChildCartItem.isActiveCartItem()) {
                            CartItem mergeVisitorChildItemInCustomerCart = mergeVisitorCartItemInCustomerCart(eachVisitorChildCartItem, customerCart, mergedCustomerParentCartItem);
                            Set<CartItem> subChildCartItems = eachVisitorChildCartItem.getChildCartItems();
                            for (CartItem eachVisitorSubChildCartItem : subChildCartItems) {
                                // now merge all active sub child cart items
                                if(eachVisitorSubChildCartItem.isActiveCartItem()) {
                                    mergeVisitorCartItemInCustomerCart(eachVisitorSubChildCartItem, customerCart, mergeVisitorChildItemInCustomerCart);
                                } else {
                                    log.debug("Visitor cartId={} has inactive sub child cart item {} . Not merging it in customer cart {} ", visitorCart.getId(), eachVisitorSubChildCartItem, customerCart.getId());
                                }
                            }
                        } else {
                            log.debug("Visitor cartId={} has inactive child cart item {} . Not merging it in customer cart {} ", visitorCart.getId(), eachVisitorChildCartItem, customerCart.getId());
                        }
                    }
                }
            } else {
                log.debug("Customer cartId={} and visitor cartId={} . Both are same. Hence not merging the carts." ,customerCart.getId() , visitorCart.getId());
            }
            CartUtil.addCartToSession(customerCart, httpsession);
            return;
        }
    }

	public Map<String,String> getBreadCrumbForCartPage() {
		Map<String, String> breadCrumb = new LinkedHashMap<String, String>();
        breadCrumb.put("Home","");
        breadCrumb.put("Shopping Bag", "last");       
        return breadCrumb;
    }
    

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Cart cloneCart(Cart cart) {
        Cart clonedCart = (Cart) cart.clone();
        clonedCart.setId(0l);
        clonedCart.setIsActive(false);
        clonedCart.setCartItems(new ArrayList<CartItem>());
        List<CartItem> activeParentCartItems = cart.getActiveParentCartItems();
        for (CartItem cartItem : activeParentCartItems) { // Solitaire or Ring or Earring or Pendant
            CartItem clonedParentCartItem = (CartItem) cartItem.clone(); // Solitaire Item clone
            clonedParentCartItem.setChildCartItems(new HashSet<CartItem>());
            cartDao.create(clonedParentCartItem);
            clonedParentCartItem.setCart(clonedCart);
            
            Set<CartItem> childCartItems = cartItem.getChildCartItems();// PendMount Item or Ring Mount or Chain
            for (CartItem cartItem2 : childCartItems) {
                CartItem clonedParentCartItem2 = (CartItem) cartItem2.clone();
                clonedParentCartItem2.setChildCartItems(new HashSet<CartItem>());
                cartDao.create(clonedParentCartItem2);
                clonedParentCartItem2.setParentCartItem(clonedParentCartItem);
                clonedParentCartItem2.setCart(clonedCart);
                
                Set<CartItem> childCartItems2 = cartItem2.getChildCartItems(); // Chain Item clone
                for (CartItem cartItem3 : childCartItems2) {
                    CartItem clonedParentCartItem3 = (CartItem) cartItem3.clone();
                    clonedParentCartItem3.setChildCartItems(new HashSet<CartItem>());
                    cartDao.create(clonedParentCartItem3);
                    clonedParentCartItem3.setParentCartItem(clonedParentCartItem2);
                    clonedParentCartItem3.setCart(clonedCart);
                }
            }
        }
        cartDao.create(clonedCart);
        return clonedCart;
    }

}
