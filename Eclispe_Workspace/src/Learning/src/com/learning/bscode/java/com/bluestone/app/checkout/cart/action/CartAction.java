package com.bluestone.app.checkout.cart.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.BaseCartAction;
import com.bluestone.app.checkout.cart.CartUtil;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.core.HttpRequestParser;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.LinkUtils;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.service.DiscountVoucherValidator;
import com.bluestone.app.voucher.service.VoucherService;
import com.google.common.base.Throwables;

@Component
public class CartAction extends BaseCartAction {
    
    private static final Logger log = LoggerFactory.getLogger(CartAction.class);

    @Autowired
    private DiscountVoucherValidator discountVoucherValidator;

    @Autowired
    private VoucherService voucherService;
    
    public Event addItemToCart(RequestContext requestContext) throws Exception {
        log.debug("CartAction.addItemToCart()");
        try {
            MessageContext messageContext = Util.getMessageContext();
            Long visitorId = (Long) messageContext.get(MessageContext.VISITOR_ID);
            ParameterMap requestParameters = requestContext.getRequestParameters();
            
            String size = getSize(requestParameters);
            Cart cart = validateAndGetCart(requestContext);
            String customizationId = requestParameters.get("customizationId");
            
            if(customizationId != null) {
            	Long customizationIdAsLong = Long.parseLong(customizationId);
                String parentId = requestParameters.get("parentId");
                CartItem parentCartItem = null;
                if (StringUtils.isNotBlank(parentId)) {
                    Long parentCartItemId = Long.valueOf(parentId);
                    parentCartItem = cart.getCartItem(parentCartItemId);
                }
                
                String validationMessage  = validateParentChildAssociation(cart, parentCartItem, customizationIdAsLong);
                if(!StringUtils.isBlank(validationMessage)){
                	handleErrors(requestContext, validationMessage);
                	return success();
                }else{
                    if (cart == null) {
                        // if cart is null create a new cart and assign a logged in customer to it if present
                        cart = createNewCart(requestContext, visitorId);
                    } 
                    cartService.addItemToCart(cart, customizationIdAsLong, size, 1, parentCartItem);
                }
                cart = cartService.getCart(cart.getId());
                requestContext.getFlowScope().put(CART, cart);
            }
            return success();
        } catch (Throwable throwable) {
            log.error("Error: CartAction.addItemToCart(): {} ", throwable.getLocalizedMessage(), throwable);
            handleErrors(requestContext, throwable.getLocalizedMessage());
            return error();
        }
    }

    private String getSize(ParameterMap requestParameters) {
        String size = requestParameters.get("ringselect");
        if (size == null) {
            size = requestParameters.get("chainselect");
        }
        return size;
    }
    
    public Event addSolitaireDesignToCart(RequestContext requestContext) throws Exception {
        log.debug("CartAction.addSolitaireDesignToCart()");
        try {
            MessageContext messageContext = Util.getMessageContext();
            Long visitorId = (Long) messageContext.get(MessageContext.VISITOR_ID);
            ParameterMap requestParameters = requestContext.getRequestParameters();
            
            String size = getSize(requestParameters);
            String customizationId = requestParameters.get("customizationId");
            String solitaireId = requestParameters.get("solitaireId");
            
            Cart cart = validateAndGetCart(requestContext);
            if(customizationId != null && solitaireId != null) {
                if (cart == null) {
                    // if cart is null create a new cart and assign a logged in customer to it if present
                    cart = createNewCart(requestContext, visitorId);
                }
                
                Long customizationIdAsLong = Long.parseLong(customizationId);
                Long solitaireIdAsLong = Long.parseLong(solitaireId);
                CartItem solitareCartItem = cartService.addItemToCart(cart, solitaireIdAsLong, null, 1, null);
                
                cartService.removeChildItems(solitareCartItem, cart);
                
                cartService.addItemToCart(cart, customizationIdAsLong, size, 1, solitareCartItem);
                cart = cartService.getCart(cart.getId());
                requestContext.getFlowScope().put(CART, cart);
            }
            return success();
        } catch (Throwable throwable) {
            log.error("Error: CartAction.addSolitaireDesignToCart(): {} ", throwable.getLocalizedMessage(), throwable);
            handleErrors(requestContext, throwable.getLocalizedMessage());
            return error();
        }
    }


	private Cart createNewCart(RequestContext requestContext, Long visitorId) throws Exception {
		Customer customer = null;
		if (SecurityUtils.getSubject().isAuthenticated()) {
		    customer = SessionUtils.getAuthenticatedCustomer();
		}
		Cart cart = cartService.createCart(visitorId, customer);
		HttpSession session = getHttpSession(requestContext);
		CartUtil.addCartToSession(cart,session);
		return cart;
	}

	public Event removeItemFromCart(RequestContext context) throws Exception {
        log.debug("CartAction.removeItemFromCart()");
        try {
            MessageContext messageContext = Util.getMessageContext();
            Long cartId = (Long) messageContext.get(MessageContext.CART_ID);
            String cartItemId = context.getRequestParameters().get("cartItemId");
            log.debug("In CartAction.removeFromCart cart id {} and cartItemId {}", new Object[] { cartId, cartItemId });
            Cart cart = cartService.getCart(cartId);
            if (cartItemId != null) {
                long cartItemAsLong = Long.parseLong(cartItemId);
                CartItem cartItem = cartService.getCartItem(cartItemAsLong);
                if (cart != null && cartItem != null) {
                    cartService.deleteCartItem(cartItem, cart);
                }
            }
            context.getFlowScope().put(CART, cartService.getCart(cartId));
            return success();
        } catch (Throwable throwable) {
            log.error("Error: CartAction.removeItemFromCart(): {}", throwable.getLocalizedMessage(), throwable);
            handleErrors(context, throwable.getLocalizedMessage());
            return error();
        }
    }
    
    public Event getCart(RequestContext context) throws Exception {
        log.debug("CartAction.getCart()");
        try {
            MutableAttributeMap flowScope = context.getFlowScope();
            ExternalContext externalContext = context.getExternalContext();
            HttpServletResponse httpServletResponse = (HttpServletResponse) externalContext.getNativeResponse();
            HttpSession session = getHttpSession(context);

            ParameterMap requestParameters = context.getRequestParameters();
            String customizationId = requestParameters.get("customizationId");
            
            Cart cart = validateAndGetCart(context);
            if(cart != null && !cart.isActive()) {
                log.warn("CartAction.getCart() cart {} not active. Removing from session" , cart.getId());
                CartUtil.removeCartFromSession(session);
                redirectToBrowsePage(context, externalContext, httpServletResponse, LinkUtils.getSiteLink("jewellery.html?inactive_cart=1"));
            }
             
            if(cart == null && customizationId == null) {
                log.warn("CartAction.getCart() cart is null. Removing from session");
                CartUtil.removeCartFromSession(session);
                redirectToBrowsePage(context, externalContext, httpServletResponse, LinkUtils.getSiteLink("jewellery.html?empty_cart=1"));
            }
             
            flowScope.put(CART, cart);
            flowScope.put("breadCrumb", cartService.getBreadCrumbForCartPage());
            return success();
        } catch (Throwable throwable) {
            log.error("Error: CartAction.getCart(): {}", throwable.getLocalizedMessage(), throwable);
            handleErrors(context, throwable.getLocalizedMessage());
            return error();
        }
    }

    private void redirectToBrowsePage(RequestContext context, ExternalContext externalContext, HttpServletResponse httpServletResponse, String url)
            throws IOException {
        if(context.getFlowExecutionContext().hasEnded()) {
            httpServletResponse.sendRedirect(url);
        } else {
            externalContext.requestExternalRedirect(url);
        }
    }

        
    /*public Event validateIsPaymentInProgress(RequestContext context) throws Exception {
        log.debug("CartAction.validateIsPaymentInProgress()");
        try {
            log.debug("In CartAction.validateIsPayemntInProgress");
            HttpServletRequest nativeRequest = (HttpServletRequest) context.getExternalContext().getNativeRequest();
            HttpSession httpSession = nativeRequest.getSession();
            Cart cart = getCartFromFlow(context);
            if(cart == null) { 
                log.debug("In CartAction.validateIsPayemntInProgress cart is null.");
                return success();
            } else{
                boolean checkPaymentInProgress = isPaymentInProgress(httpSession, cart);
                if(checkPaymentInProgress) {
                    log.warn("In CartAction.validateIsPayemntInProgress Payment is in progress for cart {} .", cart.getId());    
                    return error();
                } else {
                    return success();
                }
            }
        } catch (Throwable throwable) {
            log.error("Error: CartAction.validateIsPaymentInProgress(): {}", throwable.getLocalizedMessage(), Throwables.getRootCause(throwable));
            handleErrors(context, throwable.getLocalizedMessage());
            return error();
        }
    }*/

    /*private boolean isPaymentInProgress(HttpSession httpSession, Cart cart) {
        boolean isPaymentTransactionInProgress = false;
        if(cart != null) {
            isPaymentTransactionInProgress = cartService.validateIsPaymentInProgress(httpSession, cart.getId());
        }
        return isPaymentTransactionInProgress;
    }*/
    
    public Event applyDiscountCode(RequestContext context) {
        log.debug("CartAction.applyDiscountCode()");
        Cart cart = null;
        String voucherCodeFrmHTTPRequest = null;
        HttpServletRequest httpRequest = null;
        try {
            // boolean hasVoucherError = false;
            httpRequest = (HttpServletRequest) context.getExternalContext().getNativeRequest();
            voucherCodeFrmHTTPRequest = httpRequest.getParameter("discountCode");

            if(StringUtils.isBlank(voucherCodeFrmHTTPRequest)){
                // take a u turn
                return success();
            }
            voucherCodeFrmHTTPRequest = StringUtils.trim(voucherCodeFrmHTTPRequest);
            DiscountVoucher discountVoucher = voucherService.getVoucherByName(voucherCodeFrmHTTPRequest);
            if (discountVoucher == null) {
                log.info("CartAction.applyDiscountCode(): Voucher Code from http request={} is non existent", voucherCodeFrmHTTPRequest);
            }
            String validationResult = null;
            cart = getCartFromFlow(context);
            // check if the cart already has an existing discount voucher. We don't allow more than 1 voucher to be applied.
            if (cart.getDiscountVoucher() != null) {
                log.info("CartAction.applyDiscountCode(): We don't allow more than 1 voucher to be applied. Existing Voucher Code={}. Attempting to add another voucher={}",
                         cart.getDiscountVoucher().getName(), voucherCodeFrmHTTPRequest);
                throw new Exception("Voucher= " + cart.getDiscountVoucher().getName() + " already applied in the cart.");
            } else {
                validationResult = discountVoucherValidator.execute(discountVoucher, cart);
                if (StringUtils.isBlank(validationResult)) {
                    discountVoucherValidator.applyDiscountVoucher(cart, discountVoucher);
                    updateCart(context, cart);
                } else {
                    throw new Exception(validationResult);
                }
            }
            return success();
        } catch (Throwable throwable) {
            long cartId = -1l;
            if (cart != null) {
                cartId = cart.getId();
            }
            if (voucherCodeFrmHTTPRequest == null) {
                voucherCodeFrmHTTPRequest = " ";
            }
            log.error("Error: CartAction.applyDiscountCode(): ClientIP=[{}] [VoucherCode={}]:[CartId={}] : Reason={}",
                      HttpRequestParser.getClientIp(httpRequest), voucherCodeFrmHTTPRequest, cartId, throwable.toString());
            handleErrors(context, throwable.getLocalizedMessage());
            return error();
        }
    }
    
    public Event removeDiscountCode(RequestContext context) throws Exception {
        log.debug("CartAction.removeDiscountCode()");
        try {
            Cart cart = getCartFromFlow(context);
            if (cart.getDiscountVoucher() != null) {
                cart = cartService.removeDiscountVoucher(cart);
            }
            context.getFlowScope().put(CART, cart);
            return success();
        } catch (Throwable throwable) {
            log.error("Error: CartAction.removeDiscountCode(): {}", throwable.getLocalizedMessage(), Throwables.getRootCause(throwable));
            handleErrors(context, throwable.getLocalizedMessage());
            return error();
        }
    }
}
