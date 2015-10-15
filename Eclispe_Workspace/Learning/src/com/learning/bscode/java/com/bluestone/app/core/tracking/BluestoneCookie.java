package com.bluestone.app.core.tracking;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.CookieGenerator;

import static com.bluestone.app.core.tracking.BluestoneCookieKeys.CHECKSUM;

class BluestoneCookie implements BSCookie {

    private static final Logger log = LoggerFactory.getLogger(BluestoneCookie.class);
    static final ImmutableList<String> BS_DOMAIN_SUBDOMIANS = ImmutableList.of("www.bluestone.com", "bluestone.com", ".bluestone.com");


    static final String KEYS_SEPARATOR = "[]";

    static final String KEY_VALUE_SEPERATOR = "|";

    private Map<String, String> content;

    private final int expires;

    /*private final String path;*/

    private final String host;

    final private String clientIP;

    public String CURRENT_COOKIE_NAME = CookieName.COOKIE_NAME_IN_USE.getValue();

    ImmutableList<String> OLD_COOKIE_NAMES = ImmutableList.of(CookieName.psCookieName.getValue(),
                                                              CookieName.newBluestoneCookiename.getValue(),
                                                              CookieName.oldBluestoneCookiename.getValue(),
                                                              CookieName.refactoredBluestoneCookiename.getValue());


    BluestoneCookie(int cookieLifetime, String host, String clientIP) {
        /*this.path = path;*/
        this.expires = cookieLifetime;
        this.content = new HashMap<String, String>();
        this.host = host;
        this.clientIP = clientIP;
    }

    /**
     * This method inspects the incoming cookies and picks our cookie. After that it parses our cookie and stores the
     * name value pairs in a map inside this class - named as content.
     *
     * @param request
     * @param response
     */
    void init(HttpServletRequest request, HttpServletResponse response) {
        this.initialize(request.getCookies(), response);
    }

    private void initialize(Cookie[] incomingCookies, HttpServletResponse response) {
        log.debug("BluestoneCookie.initialize()");
        if (incomingCookies != null) {
            for (Cookie eachCookie : incomingCookies) {
                String cookieName = eachCookie.getName();
                if (this.CURRENT_COOKIE_NAME.equals(cookieName)) {
                    log.debug("Found the latest Bluestone Cookie with name=[{}]", cookieName);
                    String cookieVal = eachCookie.getValue();
                    log.debug("Start ** Decrypting the cookie=[{}] with value=[{}]", cookieName, cookieVal);
                    try {
                        String decryptedString = BSCookieEncryptor.decrypt(cookieVal);
                        content = BSCookieParser.parseCookies(decryptedString);
                    } catch (Throwable e) {
                        removeAllDomainCookies(response, cookieName);
                        content.clear(); // as we know that we failed to parse our cookie, we clear the state
                        Throwable rootCause = Throwables.getRootCause(e);
                        if (rootCause == null) {
                            rootCause = e;
                        }
                        log.warn("WARN: Exception while decrypting cookie={} from ClientIP=[{}] Reason={}",
                                 cookieVal, clientIP, rootCause.toString());
                    }
                } else {
                    if (isOldCookie(cookieName)) {
                        log.debug("Cookie is one of the old cookies. [CookieName]={} [ClientIP={}]", cookieName, clientIP);
                        removeAllDomainCookies(response, cookieName);
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("Some other Cookie [{}]=[{}] from [ClientIP={}] [Path={}] [Domain={}]",
                                      cookieName, eachCookie.getValue(), clientIP, eachCookie.getPath(), eachCookie.getDomain());
                        }
                    }
                }
            }
        } else {
            log.debug("BluestoneCookie.initialize(): No cookies were found in the incoming request.");
        }
        log.trace("BluestoneCookie.initialize() **** End");
    }

    private boolean isOldCookie(String incomingCookieName) {
        if (OLD_COOKIE_NAMES.contains(incomingCookieName)) {
            return true;
        } else {
            return false;
        }
    }


    private void removeAllDomainCookies(HttpServletResponse response, String cookieName) {
        log.debug("BluestoneCookie.removeAllDomainCookies(): Cookie Name={}", cookieName);
        for (String s : BS_DOMAIN_SUBDOMIANS) {
            removeOldBluestoneCookie(response, s, cookieName);
        }
        removeOldBluestoneCookie(response, host, cookieName);
    }


    private void removeOldBluestoneCookie(HttpServletResponse response, String domain, String cookieName) {
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieName(cookieName);
        cookieGenerator.setCookieDomain(domain);
        cookieGenerator.removeCookie(response);
    }


    @Override
    public void update(HttpServletResponse response) {
        log.debug("BluestoneCookie.update()");
        try {
            CookieContentValidator.execute(content);
            String cookieString = getCookieString();
            final long checksumValue = BSCookieParser.computeCookieChecksum(content);
            StringBuffer finalCookieValue = new StringBuffer(CHECKSUM.getKeyName()).append(KEY_VALUE_SEPERATOR).append(checksumValue);
            finalCookieValue.append(KEYS_SEPARATOR).append(cookieString);
            write(finalCookieValue.toString(), response);
        } catch (Throwable e) {
            removeAllDomainCookies(response, CURRENT_COOKIE_NAME);
            log.error("Error while writing the cookies to response - Reason={}", e.toString(), e);
        }
    }

    private String getCookieString() {
        // cookieValue format = key|value[]key|value[]key|value
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Set<String> keySet = content.keySet();
        for (String key : keySet) {
            i++;
            String value = content.get(key);
            sb.append(key).append(KEY_VALUE_SEPERATOR).append(value);
            if (i < content.size()) {
                sb.append(KEYS_SEPARATOR);
            }
        }
        String cookieVal = sb.toString();
        log.debug("Cookie string={}", cookieVal);
        return cookieVal;
    }

    private void write(final String cookieVal, HttpServletResponse response) {
        log.debug("BluestoneCookie.Write(): Cookie in Plain Text={}", cookieVal);
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE); //TODO do we need to set max age for 1 year or so.
        cookieGenerator.setCookieName(this.CURRENT_COOKIE_NAME);
        cookieGenerator.setCookieDomain(host);
        String encryptedCookie = null;
        try {
            encryptedCookie = BSCookieEncryptor.encrypt(cookieVal);
            log.debug("BluestoneCookie.write(): Encrypted Cookie Value={}", encryptedCookie);
            cookieGenerator.addCookie(response, encryptedCookie);
        } catch (Throwable e) {
            Throwable rootCause = Throwables.getRootCause(e);
            if (rootCause == null) {
                rootCause = e;
            }
            log.error("Exception while encrypting cookie={} : Reason={}", cookieVal, e.toString(), rootCause);
        }
    }

    @Override
    public void set(BluestoneCookieKeys key, String value) {
        //@todo : Rahul Agrawal : DISCUSS THIS WITH VIKAS
        if (value != null && (value.contains(KEY_VALUE_SEPERATOR))) {
            log.error("ERROR: Failed to set cookie key/value {}={} because,the value has the char='{}'",
                      key, value, KEY_VALUE_SEPERATOR);
            throw new RuntimeException("Failed to populate the cookie because the value has the " + KEY_VALUE_SEPERATOR + " character");
        }
        content.put(key.getKeyName(), value);
    }

    @Override
    public String get(BluestoneCookieKeys key) {
        return content.get(key.getKeyName());
    }

    @Override
    public String remove(BluestoneCookieKeys key) {
        return content.remove(key.getKeyName());
    }

    @Override
    public void clear() {
        log.debug("BluestoneCookie.clear()");
        content.clear();
    }
}

