package com.bluestone.app.payment.gateways.amex;

import org.apache.commons.lang.StringUtils;

/**
 * Helper functions for response codes returned by the Payment Server.
 * The functions contained in this class assist in providing descriptions for the
 * QSI Response Codes, AVS Response codes and CSC response codes returned
 * for an attempted transactions.
 * @author Dialect Payment Technologies
 */
public class VPCPaymentCodesHelper {
    
    /** Creates a new instance of VPCPaymentCodesHelper */
    public VPCPaymentCodesHelper() {
    }
    
    /**
     * Returns the description associated with the QSI Response Code provided.
     * For transaction attempts where a QSI Response code is returned by the Payment
     * Server, this helper function will return a more descriptive meaning of the
     * returned value.
     * @param vResponseCode the QSI Response Code for the attempted transaction.
     * @return the associated description for the QSI Response code provided.
     */
    public String getResponseDescription(String vResponseCode) {

        String result = "";

        // check if a single digit response code
        if (StringUtils.isNotBlank(vResponseCode)) {
        
            // Java cannot switch on a string so turn everything to a char
            char input = vResponseCode.charAt(0);

            switch (input){
                case '0' : result = "Transaction Successful"; break;
                case '1' : result = "Unknown Error"; break;
                case '2' : result = "Bank Declined Transaction"; break;
                case '3' : result = "No Reply from Bank"; break;
                case '4' : result = "Expired Card"; break;
                case '5' : result = "Insufficient Funds"; break;
                case '6' : result = "Error Communicating with Bank"; break;
                case '7' : result = "Payment Server System Error"; break;
                case '8' : result = "Transaction Type Not Supported"; break;
                case '9' : result = "Bank declined transaction (Do not contact Bank)"; break;
                case 'A' : result = "Transaction Aborted"; break;
                case 'C' : result = "Transaction Cancelled"; break;
                case 'D' : result = "Deferred transaction has been received and is awaiting processing"; break;
                case 'F' : result = "3D Secure Authentication failed"; break;
                case 'I' : result = "Card Security Code verification failed"; break;
                case 'L' : result = "Shopping Transaction Locked (Please try the transaction again later)"; break;
                case 'N' : result = "Cardholder is not enrolled in Authentication Scheme"; break;
                case 'P' : result = "Transaction has been received by the Payment Adaptor and is being processed"; break;
                case 'R' : result = "Transaction was not processed - Reached limit of retry attempts allowed"; break;
                case 'S' : result = "Duplicate SessionID (OrderInfo)"; break;
                case 'T' : result = "Address Verification Failed"; break;
                case 'U' : result = "Card Security Code Failed"; break;
                case 'V' : result = "Address Verification and Card Security Code Failed"; break;
                case '?' : result = "Transaction status is unknown"; break;
                default  : result = "Unable to be determined";
            }
            
            return result;
        } else {
            return "No Value Returned";
        }
    }
    
    /**
     * Returns the description associated with the CSC Response Code provided.
     * Where CSC data has been provided for the attempted transaction, this helper
     * function will return a more descriptive meaning of the returned CSC Response
     * Code.
     * @param vCSCResultCode the CSC Response code for the attempted transaction.
     * @return the associated description for the CSC Response code provided.
     */
    public String displayCSCResponse(String vCSCResultCode) {

        String result = "";
        if (StringUtils.isNotBlank(vCSCResultCode)) {

            if (vCSCResultCode.equalsIgnoreCase("Unsupported")  || vCSCResultCode.equalsIgnoreCase("No Value Returned")) {
                result = "CSC not supported or there was no CSC data provided";
            } else {
                // Java cannot switch on a string so turn everything to a char
                char input = vCSCResultCode.charAt(0);

                switch (input){
                    case 'M' : result = "Exact code match"; break;
                    case 'S' : result = "Merchant has indicated that CSC is not present on the card (MOTO situation)"; break;
                    case 'P' : result = "Code not processed"; break;
                    case 'U' : result = "Card issuer is not registered and/or certified"; break;
                    case 'N' : result = "Code invalid or not matched"; break;
                    default  : result = "Unable to be determined";
                }
            }

        } else {
            result = "null response";
        }
        return result;
    }
    
    /**
     * Returns the description associated with the AVS Response Code provided.
     * Where AVS data has been provided for the attempted transaction, this helper
     * function will return a more descriptive meaning of the returned AVS Response
     * Code.
     * @param vAVSResultCode the AVS Response code for the attempted transaction.
     * @return the description associated with the AVS Response Code provided.
     */
    public String displayAVSResponse(String vAVSResultCode) {

        String result = "";
        if (StringUtils.isNotBlank(vAVSResultCode)) {

            if (vAVSResultCode.equalsIgnoreCase("Unsupported") || vAVSResultCode.equalsIgnoreCase("No Value Returned")) {
                result = "AVS not supported or there was no AVS data provided";
            } else {
                // Java cannot switch on a string so turn everything to a char
                char input = vAVSResultCode.charAt(0);

                switch (input){
                    case 'X' : result = "Exact match - address and 9 digit ZIP/postal code"; break;
                    case 'Y' : result = "Exact match - address and 5 digit ZIP/postal code"; break;
                    case 'S' : result = "Service not supported or address not verified (international transaction)"; break;
                    case 'G' : result = "Issuer does not participate in AVS (international transaction)"; break;
                    case 'A' : result = "Address match only"; break;
                    case 'W' : result = "9 digit ZIP/postal code matched, Address not Matched"; break;
                    case 'Z' : result = "5 digit ZIP/postal code matched, Address not Matched"; break;
                    case 'R' : result = "Issuer system is unavailable"; break;
                    case 'U' : result = "Address unavailable or not verified"; break;
                    case 'E' : result = "Address and ZIP/postal code not provided"; break;
                    case 'N' : result = "Address and ZIP/postal code not matched"; break;
                    case '0' : result = "AVS not requested"; break;
                    default  : result = "Unable to be determined";
                }
            }
        } else {
            result = "null response";
        }
        return result;
    }
}
