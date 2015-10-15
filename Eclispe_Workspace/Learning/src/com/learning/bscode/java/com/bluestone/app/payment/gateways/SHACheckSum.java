package com.bluestone.app.payment.gateways;

import java.security.MessageDigest;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.payment.spi.model.PaymentRequest;

public class SHACheckSum {
    
    private static final Logger log    = LoggerFactory.getLogger(SHACheckSum.class);
    
    public static final String SHA512 = "SHA-512";
    public static final String MD5    = "MD5";
    
    static String checksum(String algoName, String str) throws Exception {
        StringBuffer hexString = new StringBuffer();
        byte[] strBytes = str.getBytes("UTF-8");
        MessageDigest algorithm = MessageDigest.getInstance(algoName);
        algorithm.reset();
        algorithm.update(strBytes);
        byte[] digest = algorithm.digest();
        for (int i = 0; i < digest.length; i++) {
            String hex = Integer.toHexString(0xFF & digest[i]);
            if (hex.length() == 1) {
                hexString.append("0");
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String computeRequestHash(PaymentRequest preAuthorizeRequest, String salt, String algo, String hashSequence) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SHACheckSum.computeRequestHash(): ");
        }
        String[] split = hashSequence.split("\\|");
        StringBuilder hashString = new StringBuilder("");
        Map<String, Object> templateVariables = preAuthorizeRequest.getTemplateVariables();
        for (String eachKey : split) {
            if(templateVariables.containsKey(eachKey)) {
                hashString.append(templateVariables.get(eachKey));
            } else {
                hashString.append("");
            }
            hashString.append("|");
        }
        hashString.append(salt);
        
        return checksum(algo, hashString.toString());
    }
    
    public static String computeResponseHash(HttpServletRequest httpServletRequest, String salt, String merchantId, String responseHashSequence, String algo) throws Exception {
        StringBuilder hashString = new StringBuilder("");
        hashString.append(salt).append("|");
    
        String[] split = responseHashSequence.split("\\|");
        for (String eachKey : split) {
            if (StringUtils.isBlank(eachKey)) {
                hashString.append("");
            } else {
                hashString.append(httpServletRequest.getParameter(eachKey));
            }
            hashString.append("|");
        }
        hashString.append(merchantId);
        String checksum = SHACheckSum.checksum(algo, hashString.toString());
        return checksum;
    }
    
    public static String computeResponseHashForInnoviti(String orderId, String salt, String merchantId, String uniPayId, String responseCode, String responseMessage) throws Exception {
        //MD5(orderId|merchantId|unipayid|responseCode|responseMessage|SaltValue)
        StringBuilder hashString = new StringBuilder("");
        hashString.append(StringUtils.isBlank(orderId) ? "" : orderId).append("|");
        hashString.append(merchantId).append("|");
        hashString.append(StringUtils.isBlank(uniPayId) ? "" : uniPayId).append("|");
        hashString.append(StringUtils.isBlank(responseCode) ? "" : responseCode).append("|");
        hashString.append(StringUtils.isBlank(responseMessage) ? "" : responseMessage).append("|");
        hashString.append(salt);
        String checksum = SHACheckSum.checksum(MD5, hashString.toString());
        return checksum;
    }
    
}
