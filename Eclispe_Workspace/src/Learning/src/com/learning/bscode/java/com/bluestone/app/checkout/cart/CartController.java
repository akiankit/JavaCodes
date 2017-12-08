package com.bluestone.app.checkout.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.checkout.cart.service.CartValidator;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.NumberUtil;
import com.google.gson.Gson;

@Controller
@RequestMapping(value = "/cart*")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;
    
    @Autowired
    private CartValidator cartValidator;

    @RequestMapping(value = "/updateQuantity")
    public @ResponseBody String updateQuantity(HttpServletRequest httpRequest) throws Exception {
        Long cartId = Long.parseLong(httpRequest.getParameter("cartId"));
        Long cartItemId = Long.parseLong(httpRequest.getParameter("cartItemId"));
        int cartItemQuantity = Integer.parseInt(httpRequest.getParameter("cartItemQuantity"));
        log.info("CartController.updateQuantity(): CartId={} , cartItemId={} , cartItemQuantity={}", cartId, cartItemId, cartItemQuantity);

        Cart cart = cartService.getActiveCart(cartId);
        Map<String, Object> responseMap = new HashMap<String, Object>();

        if(cart != null) {
        	cart = cartValidator.validateAndRefreshCart(cart);
        	boolean hasError = false;
            Map<String, Object> updateCartQuantityResponse = cartService.updateQuantity(cartId, cartItemId, cartItemQuantity);
            CartItem updatedCartItem = (CartItem) updateCartQuantityResponse.get("cartItem");
            if (updateCartQuantityResponse.containsKey("errorMessage")) {
                hasError = true;
                responseMap.put(Constants.MESSAGE, updateCartQuantityResponse.get("errorMessage"));
                responseMap.put("cartItemQuantity", updatedCartItem.getQuantity());
            } else {
                responseMap.put("cartId", cartId);
                responseMap.put("cartItemId", cartItemId);
                responseMap.put("cartItemPrice", NumberUtil.formatPriceIndian(updatedCartItem.getTotalPrice(), ","));
                responseMap.put("totalCartPrice", NumberUtil.formatPriceIndian(cart.getTotalPrice(), ","));
                responseMap.put("discountPrice", NumberUtil.formatPriceIndian(cart.getDiscountPrice(), ","));
                responseMap.put("finalPrice", NumberUtil.formatPriceIndian(cart.getFinalPrice(), ","));
                Set<CartItem> childCartItems = new HashSet<CartItem>();
                if(updatedCartItem.getParentCartItem() == null) {
                	childCartItems = updatedCartItem.getChildCartItems();
                	responseMap.put("isParentCartItem", true);
                } else{
                	childCartItems = updatedCartItem.getParentCartItem().getChildCartItems();
                	responseMap.put("isParentCartItem", false);
                }
                List<Long> cartItemIds = new ArrayList<Long>();
                for (CartItem cartItem : childCartItems) {
					cartItemIds.add(cartItem.getId());
				}
				responseMap.put("childItems", cartItemIds);
                CartItem parentCartItem = updatedCartItem.getParentCartItem();
				responseMap.put("noOfParentItem", parentCartItem == null ? updatedCartItem.getQuantity() : parentCartItem.getQuantity());
				responseMap.put("parentId", parentCartItem == null ? updatedCartItem.getId() : parentCartItem.getId());
            }
            responseMap.put(Constants.HAS_ERROR, hasError);
        }

        return new Gson().toJson(responseMap);
    }
}
