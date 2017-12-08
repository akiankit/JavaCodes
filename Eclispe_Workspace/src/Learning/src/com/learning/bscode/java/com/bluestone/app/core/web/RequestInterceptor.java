package com.bluestone.app.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.checkout.cart.CartUtil;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.HttpRequestParser;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.MessageContextFactory;
import com.bluestone.app.core.tracking.BSCookie;
import com.bluestone.app.core.tracking.BSCookieService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.geolocation.IpCityService;
import com.bluestone.app.popup.service.PopUpEngine;
import com.bluestone.app.popup.service.PopUpResult;
import com.bluestone.app.session.SessionConstants;
import com.google.common.base.Throwables;

public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger log = LoggerFactory.getLogger(RequestInterceptor.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private BSCookieService cookieService;
    
    @Autowired
    private IpCityService ipCityService;
    
    @Autowired
    private PopUpEngine popUpService;
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("RequestInterceptor.preHandle(): Begins here *****\n");
        MessageContext messageContext = null;
        try {
            messageContext = MessageContextFactory.create(request);
            request.setAttribute("messageContext", messageContext);

            if (CookiePreProcessor.shouldSkipCookieProcessing(request)) {
                log.debug("RequestInterceptor: ** Skipping ** Cookie parsing for {}", HttpRequestParser.dumpHeadersForLogging(request));
            } else {
                processCookies(request, response, messageContext);
                setCityFromIp(messageContext, request.getSession());
            }
            return true;
        } catch (Exception e) {
            final Throwable rootCause = Throwables.getRootCause(e);
            String trackingDetails = HttpRequestParser.dumpHeadersForLogging(request);
            log.error("Error occurred while executing preHandle in RequestInterceptor: HttpRequest={} Cause={} ",
                      trackingDetails, e.getLocalizedMessage(), rootCause);
            throw new RuntimeException("Issue in HttpRequest Interceptor for " + trackingDetails, e);
        }
    }

    private void setCityFromIp(MessageContext messageContext, HttpSession httpSession) {
        // storing in session , as only once we have to do ip->geo mapping
        String cityFromIp = (String) httpSession.getAttribute(SessionConstants.CITY_FROM_IP); 
        if(cityFromIp == null) {
            cityFromIp = ipCityService.getCityForIP(messageContext.getClientIP());
            httpSession.setAttribute(SessionConstants.CITY_FROM_IP, cityFromIp);
        }
    }


    private void processCookies(HttpServletRequest request, HttpServletResponse response, MessageContext messageContext) throws Exception {
        String header = request.getHeader("X-Origin");
        if (StringUtils.isBlank(header)) {
            log.debug("RequestInterceptor.processCookies() for [{}]", request.getRequestURL());
            BSCookie bsCookie = cookieService.getBSCookie(request, response, messageContext);
            Visitor visitor = cookieService.setVisitorInCookie(request, response, bsCookie, messageContext);
            bsCookie.update(response);
            setCartInSession(request, messageContext, visitor);
            request.setAttribute("cookie", bsCookie);
        } else {
            log.debug("RequestInterceptor.processCookies(): Checking if header <X-Origin> is present then donot set the cookies , as these calls would be from CRM");
            messageContext.put(MessageContext.VISITOR_ID, Constants.CRM_VISITOR_ID);
        }
    }

    private void setCartInSession(HttpServletRequest request, MessageContext messageContext, Visitor visitor) throws Exception {
        HttpSession httpSession = request.getSession();
        log.debug("RequestInterceptor.setCartInSession()  *** Start for Session=[{}]", httpSession.getId());
        int cartItemCount = 0;
        Long cartIdInSession = CartUtil.getCartIdFromSession(httpSession);
        Cart activeCart = null;
        if (cartIdInSession != null) {
            // get cartFromSession
            activeCart = cartService.getActiveCart(cartIdInSession);
        } else {
            log.debug("RequestInterceptor.setCartInSession(): Session=[{}] does not have a cart.", httpSession.getId());
            // if not there in session get cart for a visitor and set in session
            activeCart = cartService.getVisitorCart(visitor);
            log.debug("RequestInterceptor.setCartInSession():SessionId=[{}] visitorCart=[{}] for visitor=[{}] ",
                      httpSession.getId(), activeCart, visitor);
        }

        if (activeCart != null) {
            cartItemCount = activeCart.getItemCount();
            CartUtil.addCartToSession(activeCart, httpSession);
        } else {
            log.debug("RequestInterceptor.setCartInSession(): Neither Session=[{}] nor Visitor=[{}] have a cart.", httpSession.getId(), visitor);
            CartUtil.removeCartFromSession(httpSession);
            cartItemCount = 0;
        }

        request.setAttribute("cartItemsCount", cartItemCount);
        log.debug("RequestInterceptor.setCartInSession() *** End for Session=[{}]", httpSession.getId());
    }

    // after the handler is executed
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.trace("RequestInterceptor.postHandle() for {}", HttpRequestParser.dumpBriefHeadersForLogging(request));
        try {
            MessageContext messageContext = (MessageContext) request.getAttribute("messageContext");
            if(modelAndView != null) {
                String requestURI = request.getRequestURI();
                boolean adminUrl = StringUtils.startsWith(requestURI, "/admin");
                boolean popUpUrl = StringUtils.startsWith(requestURI, "/popup");
                if(adminUrl || popUpUrl) {
                    // no need for popup processing where popup or admin views are being displayed.
                } else {
                    HttpSession httpSession = request.getSession();
                    PopUpResult popUpResult = popUpService.execute(requestURI,messageContext, httpSession);
                    modelAndView.addObject("popUpResult", popUpResult);
                }
            }
            
            if (messageContext != null) {
                messageContext.destroy();
            }
        } catch (Exception e) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while executing postHandle ",e); 
            }
        }
    }
}
