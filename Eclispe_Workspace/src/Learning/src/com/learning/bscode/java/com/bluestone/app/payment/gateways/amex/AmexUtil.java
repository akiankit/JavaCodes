package com.bluestone.app.payment.gateways.amex;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang.StringUtils;

import com.bluestone.app.core.util.Constants;

/**
 * Helper class to perform 3 party transactions for Virtual Payment Client. This
 * class contains all the necessary functions to set the Secure Secret and
 * provide a sorted MD5 secure hash for the data provided.
 * 
 */
public class AmexUtil {	

	// This is an array for creating hex chars
	static final char[] HEX_TABLE = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    private static final String VALUE_MISSING = "No Value Returned";

    /**
	 * This method is for sorting the fields and creating an MD5 secure hash.
	 * 
	 * @param fields
	 *            is a map of all the incoming hey-value pairs from the VPC
	 * @return the MD5 secure has once the fields have been sorted.
	 * @throws NoSuchAlgorithmException
	 */
	public static String hashAllFieldsMD5(Map<String, String> fields, String secureSecret) throws NoSuchAlgorithmException {

		// create a list and sort it
		List<String> fieldNames = new ArrayList<String>(fields.keySet());
		Collections.sort(fieldNames);

		// create a buffer for the md5 input and add the secure secret first
		StringBuffer buf = new StringBuffer();
		buf.append(secureSecret);

		// iterate through the list and add the remaining field values
		Iterator<String> itr = fieldNames.iterator();

		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) fields.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				buf.append(fieldValue);
			}
		}

		MessageDigest md5 = null;
		byte[] ba = null;

		// create the md5 hash and UTF-8 encode it
		md5 = MessageDigest.getInstance("MD5");
		ba = md5.digest(buf.toString().getBytes());

		// return buf.toString();
		return hex(ba);

	}

	/**
	 * This method is for sorting the fields and creating an SHA256 secure hash.
	 * 
	 * @param fields
	 *            is a map of all the incoming hey-value pairs from the VPC
	 * @return the SHA256 secure hash once the fields have been sorted.
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	 static String hashAllFields(Map<String, Object> fields, String secureSecret) throws NoSuchAlgorithmException, InvalidKeyException {

		// create a list and sort it
		List<String> fieldNames = new ArrayList<String>(fields.keySet());
		Collections.sort(fieldNames);

		// create a buffer for the sha 256 hmac input and add the secure secret
		// first
		StringBuffer buf = new StringBuffer();

		// iterate through the list and add the remaining field values
		Iterator<String> itr = fieldNames.iterator();

		while (itr.hasNext()) {
			String fieldName = itr.next();
			String fieldValue = (String) fields.get(fieldName);
			buf.append(fieldName + "=" + fieldValue);
			if (itr.hasNext()) {
				buf.append("&");
			}
		}

		Mac mac;
		byte[] hmacData = null;
		SecretKeySpec secretKey = new SecretKeySpec(hexStringToByteArray(secureSecret), "HmacSHA256");
		mac = Mac.getInstance("HmacSHA256");
		mac.init(secretKey);
		hmacData = mac.doFinal(buf.toString().getBytes());

		return hex(hmacData);

	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * Returns Hex output of byte array
	 * 
	 * @param input
	 *            the input data to be converted to HEX.
	 * @return the string in HEX format.
	 */
	public static String hex(byte[] input) {
		// create a StringBuffer 2x the size of the hash array
		StringBuffer sb = new StringBuffer(input.length * 2);

		// retrieve the byte array data, convert it to hex
		// and add it to the StringBuffer
		for (int i = 0; i < input.length; i++) {
			sb.append(HEX_TABLE[(input[i] >> 4) & 0xf]);
			sb.append(HEX_TABLE[input[i] & 0xf]);
		}
		return sb.toString();
	}

	/**
	 * This method is for creating a URL query string.
	 * 
	 * @param buf
	 *            is the inital URL for appending the encoded fields to
	 * @param fields
	 *            is the input parameters from the order page
	 * @throws UnsupportedEncodingException
	 */
	// Method for creating a URL query string
	public static void appendQueryFields(StringBuffer buf, Map<String, String> fields) throws UnsupportedEncodingException {

		// create a list
		List<String> fieldNames = new ArrayList<String>(fields.keySet());
		Iterator<String> itr = fieldNames.iterator();

		// move through the list and create a series of URL key/value pairs
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) fields.get(fieldName);

			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// append the URL parameters
				buf.append(URLEncoder.encode(fieldName, Constants.UTF8_ENCODING));
				buf.append('=');
				buf.append(URLEncoder.encode(fieldValue, Constants.UTF8_ENCODING));
			}

			// add a '&' to the end if we have more fields coming.
			if (itr.hasNext()) {
				buf.append('&');
			}
		}

	}

	/**
	 * This method takes a data String and returns a predefined value if empty
	 * If data Sting is null, returns string "No Value Returned", else returns
	 * input
	 * 
	 * @param in
	 *            String containing the data String
	 * @return String containing the output String
	 */
	public static String null2unknownDR(String in) {
		if (in == null || in.length() == 0) {
			return VALUE_MISSING;
		} else {
			return in;
		}
	}

	/**
	 * Helper function to create the POST data from the fields provided.
	 * 
	 * @param fields
	 *            a map of the fields to be included in the POST request.
	 * @return the POST data string to be sent for processing.
	 * @throws UnsupportedEncodingException
	 */
	public static String createPostDataFromMap(Map<String, String> fields) throws UnsupportedEncodingException {
		StringBuffer buf = new StringBuffer();

		String ampersand = "";

		// append all fields in a data string
		for (Iterator<String> i = fields.keySet().iterator(); i.hasNext();) {

			String key = (String) i.next();
			String value = (String) fields.get(key);

			if ((value != null) && (value.length() > 0)) {
				// append the parameters
				buf.append(ampersand);
				buf.append(URLEncoder.encode(key, Constants.UTF8_ENCODING));
				buf.append('=');
				buf.append(URLEncoder.encode(value, Constants.UTF8_ENCODING));
			}
			ampersand = "&";
		}

		// return string
		return buf.toString();
	}

	/**
	 * Helper function to create a map of the response data.
	 * 
	 * @param queryString
	 *            the result string returned from the POST request.
	 * @return the map of the response fields returned.
	 */
	public static Map<String, String> createMapFromResponse(String queryString) {
		Map<String, String> map = new HashMap<String, String>();
		StringTokenizer st = new StringTokenizer(queryString, "&");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			int i = token.indexOf('=');
			if (i > 0) {
				try {
					String key = token.substring(0, i);
					String value = URLDecoder.decode(token.substring(i + 1, token.length()), Constants.UTF8_ENCODING);
					map.put(key, value);
				} catch (Exception ex) {
					// Do Nothing and keep looping through data
				}
			}
		}
		return map;
	}

	/**
	 * Helper function to get the response data returned for the given field.
	 * 
	 * @param in
	 *            the field to retrieve the data for.
	 * @param responseFields
	 *            the map of fields in the response string to the transaction
	 *            attempt.
	 * @return the data for the given field or "No Value Returned".
	 */
	public static String null2unknown(String in, Map<String, String> responseFields) {
        if (StringUtils.isBlank(in)) {
            return VALUE_MISSING;
        } else {
            String value = responseFields.get(in);
            if (value == null) {
                return VALUE_MISSING;
            } else {
                return value;
            }
        } // AVOID LOOKING UP THE MAP TWICE .

        /*if (in == null || in.length() == 0 || (String) responseFields.get(in) == null) {
			return "No Value Returned";
		} else {
			return (String) responseFields.get(in);
		}*/
	}

}
