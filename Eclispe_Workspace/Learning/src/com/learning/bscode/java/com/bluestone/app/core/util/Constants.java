package com.bluestone.app.core.util;

public interface Constants {

	public static final String BRACELETS = "bracelets";
	public static final String DIAMOND = "diamond";
	public static final String BANGLES = "bangles";
	public static final String EARRINGS = "earrings";
	public static final String RINGS = "rings";
	public static final String PENDANTS = "pendants";
	public static final String ID_CATEGORY = "id_category";
	public static final String EQUALS_SIGN = "=";
	public static final String QUESTION_MARK = "?";
	public static final String URL_QUERY_STRING_DELIMITER = "&";
	public static final String PAGE_NUMBER_PARAM = "p";
	public static final String ELLIPSIS = "...";
	public static final int PAGINATION_CONST = 5;
	public static final String SORTBY = "sortby";
	public static final String DATE_ADD = "date_add";
	public static final String FETCH_FILTER_DATA_PARAM = "fetchFilterData";
	public static final String SEARCHED_STRING = "searched_string"; 
	public static final String JEWELLERY_BASE_PATH = "jewellery";
	public static final String ACCOUNT_BASE_PATH = "/account";
	public static final String DUE = "due";
	public static final String OVERDUE = "overdue"; 
	
	
	// source for email regexp:-
	// http://stackoverflow.com/questions/4674682/java-spring-mvc-3-validation-of-an-email-address
	public static final String VALID_EMAIL_REGEXP = "(?i)(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	public static final String INVALID_EMAIL_ADDRESS_ERROR_MESSAGE = "Invalid email address";
	public static final String INVALID_PHONE_NO_ERROR_MESSAGE = "Invalid phone number";
	// source for phone no regexp
	// :-http://stackoverflow.com/questions/3422638/regular-expression-to-allow-range-of-numbers-or-null
	public static final String VALID_PHONE_NO_REGEXP =  "^(([123456789]{1})([0-9]{9})|)$"; 
	public static final String VALID_USER_NAME_REGEXP =  "^([a-zA-Z]{1})+\\.?([a-zA-Z\\. ])+$"; 
	public static final String INVALID_NAME_ERROR_MESSAGE = "Invalid name";
	public static final String INVALID_PASSWORD_ERROR_MESSAGE = "Password size should be between 5 to 32 characters";

	public static final String UTF8_ENCODING = "UTF-8";

	public static final String MESSAGE = "message";

    public static final String HAS_ERROR = "hasError";
    public static final String ERRORS = "errors";
	
	public static final int RECENTLY_VIEWED_LIST_SIZE = 4;
	
	public static final byte[] key=("2dert55yuppbnjhu").getBytes();//length should be 16 because no. of bytes in key should be 128!!!
	public static final String ALGORITHM = "AES";
	public static final String PASSWORD_SALT = "jv8qHpiTOUdAzfJcac1TGbWTOVOhxetuoTuBIQr2Vt7P4ZoqKddy8KHx";

	public static final String SEARCH_QUERY = "search_query";
	public static final String SEARCH_BASE_PATH = "search";
	public static final String PRICELOW = "pricelow";
	public static final String PRICEHIGH = "pricehigh";

	public static final String EMAIL_NOT_REGISTERED = "This email is not registerd";
	public static final String WRONG_PASSWORD = "Wrong password";
	public static final String CANNOT_AUTHENTICATE = "Cannot authenticate. Pleae check your Email/Password.";
	public static final String REDIRECT_URL = "redirectUrl";

	public static final String INVALID_PIN_CODE_ERROR_MESSAGE = "Invalid Pin Code.";
	public static final String PINCODE_REGEX="([0-9]{6})";
	public static final String IS_LOGGED_IN = "isLoggedIn";
	public static final String TAGS = "tags";
	public static final int ORDER_ID_PADDING = 8;
	
	public static final String CASHONDELIVERY = "cashondelivery";
	public static final String CREDITCARD = "creditcard";
	public static final String DEBITCARD = "debitcard";
	public static final String AMEXCARD = "amexcard";
	public static final String AMEX_ERROR_MSG = "Transaction is unsuccessful, the bank has declined your transaction. You may contact American Express Helpdesk on 1800 419 1414 for further details.";
	public static final String NETBANKING = "netbanking";
    public static final String POSTDATEDCHQ ="pdc";
    public static final String REPLACEMENT_ORDER ="replacement-order";
    public static final String NET_BANKING_TRANSFER ="netbanking-transfer";
    public static final String CHEQUE_PAID ="cheque-paid";
    public static final String PAYMENT_ACCEPTED ="payment-accepted";
    

    // this is used only between crm and app so we should have this a better name to prevent it from being used else where.
    @Deprecated
    public static final String ORDER_COD_CONFIRMED = "COD- Confirmed";
    /*
    public static final String ORDER_CANCELLED = "Cancelled";
    public static final String ORDER_COD_ON_HOLD = "COD- On Hold";
    public static final String ORDER_COD_RETURN = "COD-Return";
    public static final String ORDER_DELIVERED = "Delivered";
    public static final String ORDER_GOODS_IN_FOR_QC = "Goods In for QC";
    public static final String ORDER_GOODS_IN_QC_OK = "Goods in QC Ok";
    public static final String ORDER_ON_BACKORDER = "On backorder";
    public static final String ORDER_ON_HOLD = "On Hold (Customer Request)";
    public static final String ORDER_PAYMENT_ACCEPTED = "Payment accepted";
    public static final String ORDER_PAYMENT_ERROR = "Payment error";
    public static final String ORDER_PREPARATION_IN_PROGRESS = "Preparation in progress";
    public static final String ORDER_SHIPPED = "Shipped";
    */
	public static final String CUSTOMER_ID = "customerId";
    public static final String NEWLINE_REGEX = "\\r?\\n";
	public static final Long CRM_VISITOR_ID = new Long(1l); 
	
    public static final String PAYMENT_BASE_CALLBACK_URL = "/pg/";
    
    /*Bluestone Cookie Details*/
    public static String COOKIE_CIPHER = "AES";
    public static String COOKIE_CIPHER_STR_WITH_PADDING = "AES/ECB/PKCS5Padding";
    public static String COOKIE_CIPHER_STR_NOPADDING = "AES/ECB/NoPadding";
    public static String RIJNDAEL_KEY = "BFPbkmsn8swTR9Eh5yIjveRF7Fa5qDFk";//
    public static String RIJNDAEL_IV = "rDJ3aneP7oDgT+/pji2eCQ==";
    //public static int COOKIE_LIFETIME = 480;//TODO: Tinku - to decide on cookie's lifetime
//    public static String COOKIE_IV = "pmAuV6v6";

/*    public static final String ORDER_ = "Payment remotely accepted";
    public static final String ORDER_ = "PDC Accepted";
    public static final String ORDER_ = "Process but do not ship";
    public static final String ORDER_ = "Ready To Ship";
    public static final String ORDER_ = "Ready to Ship (Partial)";
    public static final String ORDER_ = "Refund Processed";
    public static final String ORDER_ = "Refund Requested";
    public static final String ORDER_ = "Return Pack received";
    public static final String ORDER_ = "Return Pack Sent";
    public static final String ORDER_ = "Return Pick up Scheduled";
    public static final String ORDER_ = "Return Received";
    public static final String ORDER_ = "Return Requested";
    public static final String ORDER_ = "Return Shipped from Customer end";
  
    public static final String ORDER_ = "Shipped (Partial)";*/


    public static final String CUSTOMER_ROLE = "customer";
	
	public static final String NONE = "NONE";




    public static final String MOSTPOPULAR = "mostpopular";
    public static final String PREFERED_PRODUCTS = "preferredProducts";
	public static final String PREFERRED_PRODUCTS_SEPRATOR = ",";

}