package com.bluestone.app.core.tracking;


enum BluestoneCookieKeys {

    CHECKSUM("checksum"),
    //DATE_ADD("date_add"),
    VISITOR_ID("id_visitors"),
    VISITOR_TAG_ID("id_visitor_tag"),
    VISITOR_STR("visitorStr");

    private final String keyName;

    private BluestoneCookieKeys(String keyName) {
        this.keyName = keyName;
    }

    String getKeyName() {
        return keyName;
    }
}
