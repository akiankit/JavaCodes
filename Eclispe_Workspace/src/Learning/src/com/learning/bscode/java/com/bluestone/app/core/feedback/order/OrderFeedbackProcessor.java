package com.bluestone.app.core.feedback.order;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.account.CipherUtil;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;
import com.bluestone.app.core.util.Util;
import com.google.common.base.Throwables;

public class OrderFeedbackProcessor {
	
	private static final Logger log = LoggerFactory.getLogger(OrderFeedbackProcessor.class);
	
	public static String getOrderFeedbackURL(String orderId, byte[] key) throws EncryptionException {
        log.debug("FeedbackProcessor.getOrderFeedbackURL() for orderId = {}", orderId);
        String encryptedOrderCode = CipherUtil.encrypt(orderId, key);
        StringBuilder orderFeedbackURL = new StringBuilder();
        orderFeedbackURL.append(Util.getSiteUrlWithContextPath());
        orderFeedbackURL.append("/feedback/order/");
        try {
            orderFeedbackURL.append(URLEncoder.encode(encryptedOrderCode, Constants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e) {
            log.error("Error: FeedbackProcessor.getOrderFeedbackURL(): {}", e.toString(), e);
            throw Throwables.propagate(e);
        }
        return orderFeedbackURL.toString();
    }

}
