package com.bluestone.app.popup;

import java.util.regex.Pattern;

public final class PopUpConstant

{
    private PopUpConstant() {
        throw new UnsupportedOperationException("AdsConstant is not meant to be instantiated.");
    }
    
    public static final String  REGEX_IP                     = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                                                                     + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                                                                     + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                                                                     + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    
    public static final Pattern PATTERN_IP                   = Pattern.compile(REGEX_IP);
    
    public static final String  EMPTY_STRING                 = "";
    
    public static final String  VALID_PHONE_NO_REGEXP        = "^(([123456789]{1})([0-9]{9})|)$";
    
    public static final Pattern PATTERN_PHONE_NO             = Pattern.compile(VALID_PHONE_NO_REGEXP);
    
    public static final String  EMPTY_PHONE_NO_ERROR_MESSAGE = "Phone number can't be empty.";
    
    
    
}
