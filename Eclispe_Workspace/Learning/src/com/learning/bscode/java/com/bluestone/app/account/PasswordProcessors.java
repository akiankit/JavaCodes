package com.bluestone.app.account;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.base.Throwables;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;
import com.bluestone.app.core.util.Util;

/**
 * @author Rahul Agrawal
 *         Date: 4/12/13
 */
public class PasswordProcessors {

    private static final Logger log = LoggerFactory.getLogger(PasswordProcessors.class);

    static String getMD5HashedPassword(String password) {
        return new Md5Hash(Constants.PASSWORD_SALT + password).toString();
    }


    static String getResetPasswordLink(String email, byte[] key) throws EncryptionException {
        log.debug("PasswordProcessors.getResetPasswordLink() for user=[{}]", email);
        String emailToken = CipherUtil.encrypt(email, key);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeStampToken = CipherUtil.encrypt(dateFormat.format(date), key);
        StringBuilder resetPasswordURL = new StringBuilder(Util.getSiteUrlWithContextPath());
        try {
            resetPasswordURL.append("/forgotpassword")
                    .append("?token1=")
                    .append(URLEncoder.encode(emailToken, Constants.UTF8_ENCODING))
                    .append("&token2=")
                    .append(URLEncoder.encode(timeStampToken, Constants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e) {
            log.error("Error: PasswordProcessors.getResetPasswordLink():{} ", e.toString(), e);
            // ideally we should not be getting here as we know we are using a valid encoding, hence I am throwing this as a runtime exception
            throw Throwables.propagate(e);
        }
        return resetPasswordURL.toString();
    }

    public static String getActivationPageURL(String email, byte[] key) throws EncryptionException {
        log.debug("LinkUtils.getActivationPageURL() for email = {}", email);
        String activationId = CipherUtil.encrypt(email, key);
        StringBuilder activationPageURL = new StringBuilder();
        activationPageURL.append(Util.getSiteUrlWithContextPath());
        activationPageURL.append("/activation");
        try {
            activationPageURL.append("?activation-id=" + URLEncoder.encode(activationId, Constants.UTF8_ENCODING));
        } catch (UnsupportedEncodingException e) {
            log.error("Error: PasswordProcessors.getActivationPageURL(): {}", e.toString(), e);
            // ideally we should not be getting here as we know we are using a valid encoding, hence I am throwing this as a runtime exception
            throw Throwables.propagate(e);
        }
        return activationPageURL.toString();
    }
}
