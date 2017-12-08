package com.bluestone.app.account;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.axis.encoding.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.EncryptionException;

public class CipherUtil {

    private static final Logger log = LoggerFactory.getLogger(CipherUtil.class);

    private static final String ALGORITHM = "AES";
    private static final String PRESTASHOP_ACCOUNT_ACTIVATION_SECURE_KEY = "secret account activation";

    public static String encrypt(String valueToEnc, byte[] key) throws EncryptionException {
        String encryptedValue;
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, secretKeySpec); // ////////LINE 20
            byte[] encValue = c.doFinal(valueToEnc.getBytes());
            encryptedValue = new BASE64Encoder().encode(encValue);
        } catch (Exception e) {
            log.error("Error: CipherUtil.encrypt(): {}", e.toString(), e);
            throw new EncryptionException(e);
        }
        return encryptedValue;
    }

    public static String decrypt(String encryptedValue, byte[] key) throws EncryptionException {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decodedValue = new BASE64Decoder().decodeBuffer(encryptedValue);
            byte[] decValue = c.doFinal(decodedValue);
            return new String(decValue);
        } catch (Exception e) {
            throw new EncryptionException(e);

        }
    }

    public static String decryptPrestashopActivationId(String encText) throws EncryptionException {
        log.debug("CipherUtil.decryptPrestashopActivationId(): Activation Id Encrypted Text={}", encText);
        String key = PRESTASHOP_ACCOUNT_ACTIVATION_SECURE_KEY;
        key = StringUtils.rightPad(key, 32, '\0');
        try {
            byte[] sessionKey = key.getBytes(Constants.UTF8_ENCODING);
            byte[] cipherText = Base64.decode(encText);
            String decryptedVal = "";
            Cipher cipher = Cipher.getInstance(Constants.COOKIE_CIPHER_STR_NOPADDING);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sessionKey, Constants.COOKIE_CIPHER));
            byte[] dectext = cipher.doFinal(cipherText);
            decryptedVal = new String(dectext, Constants.UTF8_ENCODING);
            log.debug("CipherUtil.decryptPrestashopActivationId(): Activation Id Encrypted Text={} , Decrypted Text={}", encText, decryptedVal);
            return decryptedVal;
        } catch (Exception e) {
            throw new EncryptionException(e);
        }
    }

}
