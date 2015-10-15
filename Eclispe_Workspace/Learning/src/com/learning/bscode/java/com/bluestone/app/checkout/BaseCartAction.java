package com.bluestone.app.checkout;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.account.VisitorService;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.checkout.cart.CartUtil;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.checkout.cart.service.CartQuantityValidator;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.checkout.cart.service.CartValidator;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.Util;

public class BaseCartAction extends MultiAction {
    
    private static final Logger     log  = LoggerFactory.getLogger(BaseCartAction.class);
    
    protected static final String   CART = "cart";
    
    @Autowired
    protected CartService           cartService;
    
    @Autowired
    protected CartQuantityValidator cartQuantityValidator;
    
    @Autowired
    protected CartValidator cartValidator;
    
    @Autowired
    private VisitorService          visitorService;
    
    protected void updateCart(RequestContext requestContext, Cart cart) {
        Cart updatedCart = cartService.updateCart(cart);
        requestContext.getFlowScope().put(CART, updatedCart);
    }
    
    protected Cart getCartFromFlow(RequestContext context) {
        Cart cart = (Cart) context.getFlowScope().get(CART);
        if (cart != null) {
            cart = cartService.getCart(cart.getId());
        }
        return cart;
    }
    
    protected void handleErrors(RequestContext context, String msg) {
        ArrayList<String> errorList = new ArrayList<String>();
        errorList.add(msg);
        context.getFlashScope().put("errors", errorList);
    }
    
    protected String validateParentChildAssociation(Cart cart, CartItem parentCartItem, Long customizationId) {
        String validationMessage = "";
        if (cart != null) {
            validationMessage = cartQuantityValidator.validateOnChildQuantityChange(cart, parentCartItem, customizationId, 1);
        }
        return validationMessage;
    }
    
    protected Cart validateAndGetCart(RequestContext context) {
        MessageContext messageContext = Util.getMessageContext();
        Long cartId = (Long) messageContext.get(MessageContext.CART_ID);
        Cart cart = null;
        if (cartId == null) {
            log.debug("CartAction.getActiveCart() cart not found in message context.");
        } else {
            cart = cartService.getCart(cartId);
            if (cart == null) {
                log.warn("CartAction.getActiveCart() cart not found for id {}. Removing from cookie", cartId);
                CartUtil.removeCartFromSession(getHttpSession(context));
            } else {
                cart = cartValidator.validateAndRefreshCart(cart);
                Visitor visitor = cart.getVisitor();
                if (visitor != null && visitor.getId() == -1l) {
                    log.warn("Cart {} have wrong visitor id {} set as prestashop db didn't have visitor in cart.", cart.getId(), visitor.getId());
                    Long visitorId = (Long) messageContext.get(MessageContext.VISITOR_ID);
                    // fail-safe check
                    if (visitorId != null) {
                        Visitor visitorFromCookie = visitorService.getVisitor(visitorId);
                        if (visitorFromCookie != null) {
                            log.info("Setting correct visitor id {} in Cart {}.", visitorFromCookie.getId(), cart.getId());
                            cart.setVisitor(visitorFromCookie);
                            cart = cartService.updateCart(cart);
                        }
                    }
                }
                context.getFlowScope().put(CART, cart);
            }
        }
        
        return cart;
    }
    
    protected HttpSession getHttpSession(RequestContext requestContext) {
        HttpServletRequest httpRequest = (HttpServletRequest) requestContext.getExternalContext().getNativeRequest();
        HttpSession session = httpRequest.getSession();
        return session;
    }
    
    protected boolean doesCartHaveAnyActiveProducts(RequestContext requestContext, Cart cart) {
        if (cart == null || cart.getActiveCartItems().isEmpty()) {
            handleErrors(requestContext, "Cart no longer has any active products. Please add new products to the cart.");
            String errorMessage = "Cart " + "  no longer has any active products.";
            log.warn(errorMessage);
            return false;
        } else {
            return true;
        }
    }
    
    protected boolean isCartValueLessThenZero(RequestContext requestContext, Cart cart) {
        if (cart.getFinalPrice().doubleValue() < 0) {
            handleErrors(requestContext, "Cart price was less then 0. Not proceeding further with payment.");
            log.warn("Cart {} price {} was less then 0. Not proceeding further with payment ", cart.getId(), cart.getFinalPrice().doubleValue());
            return true;
        }
        return false;
        
    }
    
    protected String getFlowIdFromRequestContext(RequestContext requestContext) {
        return requestContext.getFlowExecutionContext().getKey().toString();
    }
    
}
