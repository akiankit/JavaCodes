package com.bluestone.app.core;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.Util;

/**
 * @author Rahul Agrawal
 *         Date: 5/22/13
 */
public class MessageContextFactory {

    private static final Logger log = LoggerFactory.getLogger(MessageContextFactory.class);


    public static MessageContext create(HttpServletRequest httpServletRequest) {
        return new MessageContext(httpServletRequest);
    }

}
