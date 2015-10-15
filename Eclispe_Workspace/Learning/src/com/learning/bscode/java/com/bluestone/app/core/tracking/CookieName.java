package com.bluestone.app.core.tracking;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author Rahul Agrawal
 *         Date: 3/2/13
 */
enum CookieName {
    COOKIE_NAME_IN_USE("bscookie11032013"),
    refactoredBluestoneCookiename("bscookie"),
    psCookieName("ps"),
    newBluestoneCookiename("SF1"),
    oldBluestoneCookiename("SF");

    private String value;
    private String seed;

    private CookieName(String seed) {
        this.seed = seed;
        this.value = new Md5Hash(seed).toString();

    }

    public String getValue() {
        return value;
    }

    public String getSeed() {
        return seed;
    }


    @Override
    public String toString() {
        return getValue();
    }
}
