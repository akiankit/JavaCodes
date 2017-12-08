package com.bluestone.app.payment.gateways.amex;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.integration.SecureHTTPConnection;
import com.bluestone.app.payment.service.PaymentParametersHandler;
import com.bluestone.app.payment.spi.PaymentUtil.BLUESTONE_FIELDS;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.CaptureTransaction;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentRequest;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentResponse.GATEWAYRESPONSE;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.service.PaymentGatewayService;
import com.google.common.base.Throwables;

@Service
public class AmexPaymentService implements PaymentGatewayService {

	private static final Logger log = LoggerFactory.getLogger(AmexPaymentService.class);
	
	@Value("${amex.merchantId}")
	private String merchantId;
	
	@Value("${amex.hashSecret}")
	private String secureSecret;

	@Value("${amex.accessCode}")
	private String accessCode;

	@Value("${amex.authorizeURL}")
	private String amexAuthorizeURL;
	
	@Value("${amex.captureHost}")
	private String amexCaptureHost;
	
	@Value("${amex.capturePath}")
	private String amexCapturePath;
	
	@Value("${amex.captureUser}")
	private String amexCaptureUser;
	
	@Value("${amex.capturePassword}")
	private String amexCapturePassword;	
	
	@Autowired
    private PaymentParametersHandler paymentParametersHandler;
	
	@Autowired	
	private SecureHTTPConnection httpsConnection;
	
	private final String AMEXURL = Constants.PAYMENT_BASE_CALLBACK_URL + PaymentGateway.AMEX.name().toLowerCase() + "/response";
	
	private static final BigDecimal HUNDRED = new BigDecimal(100);

	@Override
	public PaymentGateway getPaymentGateway() {
		return PaymentGateway.AMEX;
	}

	@Override
	public PaymentRequest createPaymentRequest(PaymentTransaction paymentTransaction, Cart clonedCart, Cart originalCart) throws PaymentException {
		log.debug("AmexPaymentService.createPaymentRequest(): paymentTransactionId=[{}]", paymentTransaction.getBluestoneTxnId());
		try {
			PaymentRequest preAuthorizeRequest = new PaymentRequest();
			preAuthorizeRequest.setVmTemplate(true);
			preAuthorizeRequest.setVmTemplatePath("pgforms/amex.vm");

			// amexBaseCallbackUrl ~ hostname/contextpath/pg/amex
			String amexBaseCallbackUrl = Util.getSiteUrlWithContextPath() + AMEXURL;
			Map<String, Object> fieldsMap = new HashMap<String, Object>();
			fieldsMap.put("vpc_Version", "1");
			fieldsMap.put("vpc_Command", "pay");
			fieldsMap.put("vpc_AccessCode", accessCode);
			fieldsMap.put("vpc_MerchTxnRef", paymentTransaction.getBluestoneTxnId());
			fieldsMap.put("vpc_Merchant", merchantId);
			fieldsMap.put("vpc_OrderInfo", clonedCart.getId() + "");
			// Multiplying amount by 100 as AMEX recieves amount in paise
			fieldsMap.put("vpc_Amount", paymentTransaction.getAmount().multiply(HUNDRED).intValue() + "");
			fieldsMap.put("vpc_Locale", "en");
			fieldsMap.put("vpc_ReturnURL", amexBaseCallbackUrl);
			fieldsMap.put("vpc_Card", "Amex");
			fieldsMap.put("vpc_Gateway", "ssl");

			// User defined fields
			fieldsMap.put("user_udf1", paymentTransaction.getBluestoneTxnId());

			// Create SHA-256 HMAC secure hash and insert it into the hash map.
			String secureHash = AmexUtil.hashAllFields(fieldsMap, secureSecret);
			fieldsMap.put("vpc_SecureHash", secureHash);
			fieldsMap.put("vpc_SecureHashType", "SHA256");

			preAuthorizeRequest.setTemplateVariables(fieldsMap);
			preAuthorizeRequest.addTemplateVariable("paymentGatewayUrl", amexAuthorizeURL);

			return preAuthorizeRequest;
		} catch (Exception e) {
            Throwable rootCause = Throwables.getRootCause(e);
            log.error("Error while creating payment request for payment transaction id=[{}] Reason={},",paymentTransaction.getId(), rootCause);
            throw new PaymentException(PaymentError.PREAUTH_REQUEST_GENERATION_FAILED, e, paymentTransaction.getBluestoneTxnId());
        }
	}

	@Override
	public PaymentResponse handlePaymentResponse(HttpServletRequest httpServletRequest) throws PaymentException{
		log.debug("AmexPaymentService.handlePaymentResponse(): ");
		Map<String, Object> responseFields = new HashMap<String, Object>();
		String responseString = paymentParametersHandler.generateDebugMessage(httpServletRequest).toString();
		String encrytedTxnId = "";				
		PaymentResponse paymentResponse = new PaymentResponse();
		try {
			for (Enumeration<?> en = httpServletRequest.getParameterNames(); en.hasMoreElements();) {
				String fieldName = (String) en.nextElement();
				String fieldValue = httpServletRequest.getParameter(fieldName);
				responseFields.put(fieldName, fieldValue);
			}			

			validateHash(responseFields, paymentResponse);
			
			validateMerchantFields(responseFields, paymentResponse);
			
			encrytedTxnId = paymentResponse.getMerchantFields().get(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID);
			
			paymentResponse.setResponseAsText(responseString);
			paymentResponse.setGatewayTransactionId((String) responseFields.get("vpc_MerchTxnRef"));
			paymentResponse.setAmount(new BigDecimal((String) responseFields.get("vpc_Amount")).divide(HUNDRED));						

			String txnResponseCode = AmexUtil.null2unknownDR((String) responseFields.get("vpc_TxnResponseCode"));
			String vpc_3DSstatus = AmexUtil.null2unknownDR((String) responseFields.get("vpc_3DSstatus"));
//			// CSC Receipt Data
//			String cscResultCode = payconn.null2unknownDR((String) responseFields.get("vpc_CSCResultCode"));
//			String ACQCSCRespCode = payconn.null2unknownDR((String) responseFields.get("vpc_AcqCSCRespCode"));
//			// AVS Receipt Data
//			String avsResultCode = payconn.null2unknownDR((String) responseFields.get("vpc_AVSResultCode"));
//			String ACQAVSRespCode = payconn.null2unknownDR((String) responseFields.get("vpc_AcqAVSRespCode"));												
			
			validateResponseStatus(paymentResponse, txnResponseCode, vpc_3DSstatus);
			
		} catch(PaymentException pe){
				throw pe;
		} catch (Exception e) {
			log.error("Error while executing AmexPaymentService.handlePaymentResponse() EncrytedTxnId={} , Response={} ", encrytedTxnId, responseString, e);
			paymentResponse.setFailureMessage(PaymentError.MALFORMED_RESPONSE.getErrorMsg());
            paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
		}
		return paymentResponse;
	}

	private void validateResponseStatus(PaymentResponse paymentResponse, String txnResponseCode, String vpc_3DSstatus){
		try{
			
			validateInternalError(paymentResponse, txnResponseCode);								
			validateTransactionResponseCode(paymentResponse, txnResponseCode);			
			Validate3DSStatus(paymentResponse, vpc_3DSstatus);			
			paymentResponse.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);

		}catch(PaymentException e){
			paymentResponse.setFailureMessage(e.getPaymentError().getErrorMsg());
			paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
		}
	}

	private void validateMerchantFields(Map<String, Object> responseFields, PaymentResponse paymentResponse) throws PaymentException {
		String encrytedTxnId = (String) responseFields.get("user_udf1");
		
		if (StringUtils.isBlank(encrytedTxnId)) {
			paymentResponse.setFailureMessage(PaymentError.MALFORMED_RESPONSE.getErrorMsg());
		    paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);	            
		    throw new PaymentException(PaymentError.MALFORMED_RESPONSE, encrytedTxnId);
		}		
		paymentResponse.addMerchantField(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID, encrytedTxnId);		
	}

	private void Validate3DSStatus(PaymentResponse paymentResponse, String vpc_3DSstatus) throws PaymentException {
		if (!(vpc_3DSstatus.equals("Y") || vpc_3DSstatus.equals("A"))) {
			log.error("AmexPaymentService: VPC 3DS Failure - 3DS Status Code ={}", vpc_3DSstatus);			
			throw new PaymentException(PaymentError.AMEX_3DS_ERROR);
		}
	}

	private void validateTransactionResponseCode(PaymentResponse paymentResponse, String txnResponseCode) throws PaymentException {
		if ((!txnResponseCode.equalsIgnoreCase("0"))) {
			log.error("AmexPaymentService: Transaction Failure Error - Transaction Response Code ={}", txnResponseCode);			
		    throw new PaymentException(PaymentError.TRANSACTION_ERROR);
		}
	}

	private void validateHash(Map<String, Object> responseFields, PaymentResponse paymentResponse) throws PaymentException {
		String hash = (String) responseFields.remove("vpc_SecureHash");
		responseFields.remove("vpc_SecureHashType");
		String calculatedHash = "";
		
		try {
			calculatedHash = AmexUtil.hashAllFields(responseFields, secureSecret);
		} catch (Exception e) {		
			log.error("AmexPaymentService: Hash Computation Error - Response Fields={}",responseFields);			
		    throw new PaymentException(PaymentError.MALFORMED_RESPONSE_HASH);
		}
		
		if ((!calculatedHash.equalsIgnoreCase(hash))) {
			log.error("AmexPaymentService: Hash mismatch - Hash received ={} , computed hash ={} . Merchant Fields={}", hash, calculatedHash, paymentResponse.getMerchantFields());			
		    throw new PaymentException(PaymentError.MALFORMED_RESPONSE_HASH);
		}		
		
	}

	private void validateInternalError(PaymentResponse paymentResponse, String txnResponseCode) throws PaymentException {
		if ((txnResponseCode.equals("7") || txnResponseCode.equals("No Value Returned"))) {
			log.error("AmexPaymentService: Internal Error - Txn Response Code ={}", txnResponseCode);
			paymentResponse.setFailureMessage(PaymentError.AMEX_INTERNAL_ERROR.getErrorMsg());
			paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);	
			throw new PaymentException(PaymentError.AMEX_INTERNAL_ERROR);
		}		
	}

	@Override
	public CaptureTransaction capture(PaymentTransaction paymentTransaction) throws PaymentException{
        log.info("AmexPaymentService.capture(): {}", paymentTransaction.toStringBrief());
		CaptureTransaction captureTransaction = new CaptureTransaction();

		PaymentResponse paymentResponse = paymentTransaction.getPaymentResponse();
		String responseAsText = paymentResponse.getResponseAsText();
		Map<String, String> responseMap = parseResponseText(responseAsText);
		String amount = responseMap.get("vpc_Amount");
		String merchantID = responseMap.get("vpc_Merchant");
		String merchTxnRef = responseMap.get("vpc_MerchTxnRef");
		String transactionNo = responseMap.get("vpc_TransactionNo");
		
		try {
			Map<String, Object> captRequestFields = new HashMap<String, Object>();

			captRequestFields.put("vpc_Version", "1");
			captRequestFields.put("vpc_Command", "capture");
			captRequestFields.put("vpc_AccessCode", accessCode);
			captRequestFields.put("vpc_MerchTxnRef", merchTxnRef + "-C");
			captRequestFields.put("vpc_Merchant", merchantID);
			captRequestFields.put("vpc_TransNo", transactionNo);
			captRequestFields.put("vpc_Amount", amount);
			captRequestFields.put("vpc_User", amexCaptureUser);
			captRequestFields.put("vpc_Password", amexCapturePassword);

			String captResQS = "";

			try {
				captResQS = httpsConnection.executePostRequest(amexCaptureHost, amexCapturePath, captRequestFields);
			} catch (Exception e) {
				log.error("AmexPaymentService: Capture Request Failure - Transaction Number ={}", transactionNo);
				throw new PaymentException(PaymentError.COMMUNICATION_ERROR_CODE,paymentTransaction.getBluestoneTxnId());
			}
			
			// create a hash map for the response data
			Map<String, String> captResponseFields = AmexUtil.createMapFromResponse(captResQS);
			captureTransaction.setResponseAsText(generateStringFromMap(captResponseFields, ","));

			String captTxnResponseCode = AmexUtil.null2unknown("vpc_TxnResponseCode", captResponseFields);
			validateCaptureResponse(captureTransaction, captTxnResponseCode);

		} catch (Exception e) {
			log.error("ERROR while executing AmexPaymentService.capture() {} ", paymentTransaction.toStringBrief(), e);
			captureTransaction.setFailureMessage(Throwables.getRootCause(e).getMessage());
			captureTransaction.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
		}
		return captureTransaction;
	}

	private void validateCaptureResponse(CaptureTransaction captureTransaction, String captTxnResponseCode) {
		if (captTxnResponseCode.equals("7") || captTxnResponseCode.equals("No Value Returned")) {
			captureTransaction.setFailureMessage(PaymentError.INTERNAL_ERROR.getErrorMsg());
			captureTransaction.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
		}

		if ((!captTxnResponseCode.equals("No Value Returned") && (captTxnResponseCode.equalsIgnoreCase("0")))) {
			captureTransaction.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);
		} else {
			captureTransaction.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
		}
	}

	@Override
	public void cancel(PaymentTransaction paymentTransaction) throws PaymentException {
		
	}

	@Override
	public void refund(PaymentTransaction paymentTransaction) throws PaymentException {		
		
	}
	
	private static Map<String,String> parseResponseText(String responseAsText) {
		Map<String,String> responseMap = new HashMap<String, String>();
		String[] split = StringUtils.split(responseAsText, "\n");
		for (String string : split) {
			String[] split2 = StringUtils.split(string, "=");
			String key = split2[0];
			String[] value = fromString(split2[1]);
			responseMap.put(key, value[0]);
		}
		return responseMap;
    }
	
	private static String[] fromString(String string) {
		String[] values = string.replace("[", "").replace("]", "").split(", ");
		return values;
	}
	
	private static String generateStringFromMap(Map<String,String> parameterMap,String separtor) {
		log.debug("AmexPaymentService.generateStringFromMap()");        
        final Set<String> set = parameterMap.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        for (String eachKey : set) {
            stringBuilder.append(eachKey).append("=").append(parameterMap.get(eachKey)).append(separtor);
        }
        String mapStr = stringBuilder.toString();
        if(StringUtils.isNotBlank(mapStr)){
        	mapStr = mapStr.substring(0,mapStr.length()-1);
        }
        log.debug("AmexPaymentService.generateStringFromMap(): {}", mapStr);
        return mapStr;
    }	

}
