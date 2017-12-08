package com.bluestone.app.core.tracking;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.VisitorHistoryServiceV2;
import com.bluestone.app.account.VisitorService;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.Util;

import static com.bluestone.app.core.tracking.BluestoneCookieKeys.VISITOR_ID;
import static com.bluestone.app.core.tracking.BluestoneCookieKeys.VISITOR_STR;
import static com.bluestone.app.core.tracking.BluestoneCookieKeys.VISITOR_TAG_ID;

/**
 * @author Rahul Agrawal
 *         Date: 2/28/13
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class VisitorCookieService {

    private static final Logger log = LoggerFactory.getLogger(VisitorCookieService.class);

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitorHistoryServiceV2 visitorHistoryService;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Visitor setVisitorInCookie(HttpServletRequest request, HttpServletResponse response,
                                      BSCookie bsCookie, MessageContext messageContext) {

        log.debug("VisitorCookieService.setVisitorInCookie() *** Start");

        Visitor visitor = getVisitorFromCookie(bsCookie, messageContext, request);

        if (visitor == null) {
            String visitorStr = bsCookie.get(VISITOR_STR);
            if (Util.isEmptyString(visitorStr)) {
                String value = createVisitorMarker(request);
                if (StringUtils.isNotBlank(value)) {
                    bsCookie.set(VISITOR_STR, value);
                } else {
                    log.info("VisitorCookieService.setVisitorInCookie(): Not setting the {} in cookie because it's value is {} ",
                             VISITOR_STR.getKeyName(), value);
                }
            } else {
                visitor = setNewVisitorInCookie(bsCookie, messageContext);
                bsCookie.remove(VISITOR_STR);
            }
        }
        log.debug("VisitorCookieService.setVisitorInCookie(): ***** End\n");
        return visitor;
    }

    private Visitor getVisitorFromCookie(BSCookie bsCookie, MessageContext messageContext, HttpServletRequest request) {
        String visitorIdInsideCookie = bsCookie.get(VISITOR_ID);
        log.debug("VisitorCookieService.getVisitorFromCookie() for bsCookie.get(VISITOR_ID)={}", visitorIdInsideCookie);
        Visitor visitor = null;
        if (StringUtils.isNotBlank(visitorIdInsideCookie) && StringUtils.isNumeric(visitorIdInsideCookie)) {
            Long visitorId = Long.parseLong(visitorIdInsideCookie);
            log.debug("VisitorId={} obtained from the cookie.", visitorId);
            visitor = visitorService.getVisitor(visitorId);
            if (visitor == null) {
                log.debug("No Visitor found for the visitorId={} obtained from the incoming cookie.", visitorId);
                removeVisitorRelatedCookies(bsCookie);
            } else {
                visitorHistoryService.saveOrGetVisitorHistory(request, visitor, true);
                //@todo : Rahul Agrawal : Discuss why we should NOT get the tag id from the visitor saved in he DB ????
                //int visitorTagId = Integer.parseInt(bsCookie.get(VISITOR_TAG_ID));
                messageContext.put(MessageContext.VISITOR_ID, visitorId);
                messageContext.put(MessageContext.VISITOR_TAG_ID, visitor.getTagId());
            }
        } else {
            removeVisitorRelatedCookies(bsCookie);
            if (visitorIdInsideCookie != null) {
                log.warn("Error: VisitorId={} obtained from the incoming cookie is not numeric", visitorIdInsideCookie);
            }
        }
        return visitor;
    }

    private void removeVisitorRelatedCookies(BSCookie bsCookie) {
        bsCookie.remove(VISITOR_ID);
        bsCookie.remove(VISITOR_TAG_ID);
    }

    private String createVisitorMarker(HttpServletRequest request) {
        log.debug("VisitorCookieService.createVisitorMarker()");
        //First Visit  - Mark the Visitor - Not creating visitors to eliminate bots(most of them don't send cookies back)
        return visitorHistoryService.saveOrGetVisitorHistory(request, null, false);
    }

    private Visitor setNewVisitorInCookie(BSCookie bsCookie, MessageContext messageContext) {
        log.debug("VisitorCookieService.setNewVisitorInCookie()");
        //Second Visit - Unmark and create the visitor
        String clientIp = messageContext.getClientIP();
        int tagId = getRandomInteger(1, 1000);
        Visitor visitor = visitorService.createVisitor(clientIp, tagId);

        String visitorStr = bsCookie.get(VISITOR_STR);
        visitorHistoryService.createVisitorHistory(visitorStr, visitor);

        bsCookie.set(VISITOR_ID, String.valueOf(visitor.getId()));
        bsCookie.set(VISITOR_TAG_ID, String.valueOf(visitor.getTagId()));

        messageContext.put(MessageContext.VISITOR_ID, visitor.getId());
        messageContext.put(MessageContext.VISITOR_TAG_ID, visitor.getTagId());

        return visitor;
    }

    private static int getRandomInteger(int start, int end) {
        end += 1;
        return RANDOM.nextInt(end - start) + start;
    }
    private static final Random RANDOM = new Random();

}
