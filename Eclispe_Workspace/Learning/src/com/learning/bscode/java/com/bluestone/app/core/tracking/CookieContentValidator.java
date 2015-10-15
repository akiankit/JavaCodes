package com.bluestone.app.core.tracking;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 3/11/13
 */
class CookieContentValidator {

    private static final Logger log = LoggerFactory.getLogger(CookieContentValidator.class);

    static final boolean execute(final Map<String, String> content) {
        log.debug("CookieContentValidator.execute()");
        boolean result = false;

        Set<String> keys = content.keySet();
        if (keys.contains(BluestoneCookieKeys.VISITOR_STR.getKeyName())
            &&
            (keys.contains(BluestoneCookieKeys.VISITOR_ID.getKeyName())
             || keys.contains(BluestoneCookieKeys.VISITOR_TAG_ID.getKeyName()))) {
            log.error("Error: CookieContentValidator.execute(): We should not have both [{}] and [{} or {} ] at the same time",
                      BluestoneCookieKeys.VISITOR_STR.getKeyName(), BluestoneCookieKeys.VISITOR_ID.getKeyName(), BluestoneCookieKeys.VISITOR_TAG_ID.getKeyName());
            throw new RuntimeException("Invalid Content{}" + content);
        }
        return result;
    }
}
