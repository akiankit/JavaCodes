package com.bluestone.app.checkout.cart.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.checkout.cart.dao.CartDao;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.service.DiscountVoucherValidator;

@Service
public class CartValidator {

    private static final Logger log = LoggerFactory.getLogger(CartValidator.class);
    
    @Autowired
    private CartDao              cartDao;
    
    @Autowired
    private DiscountVoucherValidator discountVoucherValidator;
    
    @Autowired
    private CartService              cartService;

    
    public List<CartItem> getAllInactiveProductsList(List<CartItem> cartItems) {
        List<CartItem> allInactiveCartItems = new ArrayList<CartItem>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if(!product.isProductActive()) {
                allInactiveCartItems.add(cartItem);
            }
        }
        return allInactiveCartItems;
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Cart validateAndRefreshCart(Cart cart) {
        log.debug("CartValidator.validateAndRefreshCart(): Cart id={}", cart.getId());
        List<CartItem> cartItems = cart.getCartItems();
        
        List<CartItem> allCartItemsTobeRemoved = getAllInactiveProductsList(cartItems);
        for (CartItem eachInactiveCartItem : allCartItemsTobeRemoved) {
            cartService.removeCartItem(eachInactiveCartItem, cart);
        }
        
        validateAndApplyVoucher(cart);
        return cartDao.update(cart);
    }
    
    private void validateAndApplyVoucher(Cart cart) {
        log.debug("CartValidator.validateAndApplyVoucher(): Cart id={}", cart.getId());
        // validate and Re-apply discount voucher
        DiscountVoucher discountVoucher = cart.getDiscountVoucher();
        if (discountVoucher != null) {
            String validationResponse = discountVoucherValidator.execute(discountVoucher, cart);
            if (StringUtils.isBlank(validationResponse)) {
                // re-apply discount voucher
                discountVoucherValidator.applyDiscountVoucher(cart, discountVoucher);
            } else {
                log.debug("CartService.validateAndApplyVoucher(): Validation response = {}", validationResponse);
                log.debug("Discount voucher is no longer valid because {}. Removing discount voucher and discount price from cart", validationResponse);
                cart.setDiscountVoucher(null);
                cart.setDiscountPrice(new BigDecimal(0));
            }
        }
    }
    
    
    



}
