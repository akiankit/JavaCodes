package com.bluestone.app.core.tracking;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bluestone.app.core.tracking.BluestoneCookieKeys.CHECKSUM;

/**
 * @author Rahul Agrawal
 *         Date: 3/5/13
 */
class BSCookieParser {

    private static final Logger log = LoggerFactory.getLogger(BSCookieParser.class);

    static ImmutableList<String> BS_COOKIE_KEY_NAMES = ImmutableList.of(BluestoneCookieKeys.VISITOR_ID.getKeyName(), 
                                                                        BluestoneCookieKeys.VISITOR_TAG_ID.getKeyName(),
                                                                        BluestoneCookieKeys.VISITOR_STR.getKeyName());

    private static final String COOKIE_IV = "pmAuV6v6";


    static Map<String, String> parseCookies(String decryptedString) throws Exception {
        log.debug("BSCookieParser: Begin Parsing our decrypted cookie = {}", decryptedString);
        Map<String, String> content = new HashMap<String, String>();
        // final cookieValue format = checksum|value[]key|value[]key|value[]key|value
        String checkSum = "";

        // array is {checksum|value , key1|value1 , key2|value2 }
        String[] keyValuePairs = StringUtils.split(decryptedString, BluestoneCookie.KEYS_SEPARATOR); //@todo : Rahul Agrawal :  unit test
        for (String eachKeyValue /*key1|value*/ : keyValuePairs) {
            String[] parts = StringUtils.split(eachKeyValue, BluestoneCookie.KEY_VALUE_SEPERATOR);

            if (parts.length == 2) {
                String key = parts[0];
                String value = parts[1];
                log.debug(" {}={}", key, value);
                if (key.equals(CHECKSUM.getKeyName())) {
                    checkSum = value;
                } else {
                    content.put(key, value);
                }
            } else {
                log.warn("BSCookieParser.parseCookies():Cookie={} has a key Value=[{}] which as {} in either key or value",
                         decryptedString, eachKeyValue, BluestoneCookie.KEY_VALUE_SEPERATOR);
            }
        }
        if (log.isDebugEnabled()) {
            Set<String> contentKeys = content.keySet();
            for (String key : contentKeys) {
                log.debug("Content Map : [{}]=[{}]", key, content.get(key));
            }
        }

        try {
            final long incomingCheckSumValue = Long.parseLong(checkSum);
            long expectedCheckSumValue = computeCookieChecksum(content);
            log.debug("******* ComputedChecksum={} , Incoming CookieChecksum={} ", expectedCheckSumValue, checkSum);
            if (incomingCheckSumValue != expectedCheckSumValue) {
                log.error("Checksum doesn't match. Actual={} , expected={}", incomingCheckSumValue, expectedCheckSumValue);
                throw new RuntimeException("BSCookieChecksum value does not match");
            }
        } catch (NumberFormatException nfe) {
            log.error("Error: BSCookieParser.parseCookies(): Incoming checksum={}. Either it did not come in cookie or was not a number. Cause={}",
                      checkSum, nfe.toString());
            Throwables.propagate(nfe);
        }
        return content;
    }

    static long computeCookieChecksum(final Map<String, String> content) throws Exception {
        log.trace("BSCookieParser.computeCookieChecksum(): {}", content);
        StringBuilder hashString = new StringBuilder("");
        for (String eachKey : BS_COOKIE_KEY_NAMES) {
            if (content.containsKey(eachKey)) {
                hashString.append("|");
                hashString.append(content.get(eachKey));
            }
        }
        
        // hashString is of the type in a fixed order = value1|value2|value3
        Checksum checksum = new CRC32();

        StringBuilder sb = new StringBuilder(COOKIE_IV);
        sb.append(hashString.toString());
        String checksumStr = sb.toString();

        byte[] bytes = checksumStr.getBytes("UTF-8");
        checksum.update(bytes, 0, bytes.length);
        final long checksumValue = checksum.getValue();
        log.debug("ComputeCookieChecksum():checksumStr=[{}] checksumValue=[{}]", checksumStr, checksumValue);
        return checksumValue;
    }
}
