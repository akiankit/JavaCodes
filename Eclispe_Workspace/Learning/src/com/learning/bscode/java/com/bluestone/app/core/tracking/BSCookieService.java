package com.bluestone.app.core.tracking;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.core.MessageContext;

/**
 * @author Rahul Agrawal
 *         Date: 2/28/13
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class BSCookieService {

    private static final Logger log = LoggerFactory.getLogger(BSCookieService.class);

    @Autowired
    private VisitorCookieService visitorCookieService;

    private static int COOKIE_LIFETIME = 480;//TODO: Tinku - to decide on cookie's lifetime

    public BSCookie getBSCookie(HttpServletRequest request, HttpServletResponse response, MessageContext messageContext) {
        log.debug("BSCookieService.getBSCookie()");
        BluestoneCookie bluestoneCookie = new BluestoneCookie(COOKIE_LIFETIME,
                                                              messageContext.getHost(),
                                                              messageContext.getClientIP());
        bluestoneCookie.init(request, response);
        return bluestoneCookie;
    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Visitor setVisitorInCookie(HttpServletRequest request, HttpServletResponse response,
                                      BSCookie bluestoneCookie, MessageContext messageContext) {
        log.trace("BSCookieService.setVisitorInCookie()");
        Visitor visitor = null;
        try {
            visitor = visitorCookieService.setVisitorInCookie(request, response, bluestoneCookie, messageContext);
        } catch (Exception exception) {
            log.error("Error: BSCookieService.setVisitorInCookie(): Reason={} ", exception.toString(), Throwables.getRootCause(exception));
            bluestoneCookie.clear();
        }
        return visitor;

    }
}
