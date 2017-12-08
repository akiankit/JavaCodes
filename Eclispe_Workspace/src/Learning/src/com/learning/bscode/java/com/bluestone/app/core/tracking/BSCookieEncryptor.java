package com.bluestone.app.core.tracking;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 2/28/13
 */
class BSCookieEncryptor {

    private static final Logger log = LoggerFactory.getLogger(BSCookieEncryptor.class);

    static String encrypt(String valueToEnc) throws UnsupportedEncodingException {
        log.debug("BSCookieEncryptor.Encrypting(): {}", valueToEnc);
        String encryptedValue;
        String urlEncoded;
        urlEncoded = URLEncoder.encode(valueToEnc, "UTF-8"); //http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars
        byte[] bytes = urlEncoded.getBytes("UTF-8");
        encryptedValue = Base64.encodeBase64String(bytes);
        return encryptedValue;
    }

    static String decrypt(String encryptedValue) throws Exception {
        log.debug("BSCookieEncryptor: Decrypting {}", encryptedValue);
        byte[] decodeBase64 = Base64.decodeBase64(encryptedValue);
        String decodedString = new String(decodeBase64, "UTF-8");
        return URLDecoder.decode(decodedString, "UTF-8");
    }
}
