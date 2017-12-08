package com.bluestone.app.core.tracking;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Rahul Agrawal
 *         Date: 2/28/13
 */
public interface BSCookie {

    void set(BluestoneCookieKeys key, String value);

    String get(BluestoneCookieKeys key);

    String remove(BluestoneCookieKeys key);

    void update(HttpServletResponse response);

    void clear();

}
