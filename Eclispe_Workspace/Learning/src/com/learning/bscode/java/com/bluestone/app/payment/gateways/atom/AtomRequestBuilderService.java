package com.bluestone.app.payment.gateways.atom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.service.ShippingService;

@Component
public class AtomRequestBuilderService {
	
	private static final Logger log = LoggerFactory.getLogger(AtomRequestBuilderService.class);

	private static final String CreditCard = "cc";

	private static final String DebitCard = "DC";

	private static final String NetBanking = "NB";
	
	private final String ATOMURL = Constants.PAYMENT_BASE_CALLBACK_URL + PaymentGateway.ATOM.name().toLowerCase();
	
	@Autowired
	private AtomXMLParserService atomXMLParserService;
	
	@Autowired
	private ShippingService shippingService;
	
	public String buildFirstRequestURL(PaymentTransaction paymentTransaction,Cart clonedCart, String gatewayUrl, String loginId, String password,String productId) throws UnsupportedEncodingException{
		String firstRequestURL="";		              
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(gatewayUrl).append("?");
		Map<String, Object> requestParameterMap = getRequestParameterMap(paymentTransaction, clonedCart , loginId, password, productId);
		for ( String key : requestParameterMap.keySet()) {
			urlBuilder.append(key).append("=").append(requestParameterMap.get(key)).append("&");
		}
		firstRequestURL = urlBuilder.toString();
		if(StringUtils.isNotBlank(firstRequestURL)){
			firstRequestURL = firstRequestURL.substring(0,firstRequestURL.length()-1);
        }
		firstRequestURL = firstRequestURL.replace(" ","%20");		
		log.info("AtomRequestBuilderService.buildFirstRequestURL() - First Request URL={}",firstRequestURL);
		return firstRequestURL;
	}
	
	public Map<String, Object> getRequestParameterMap(PaymentTransaction paymentTransaction,Cart clonedCart, String loginId, String password,String productId) throws UnsupportedEncodingException{
		log.debug("AtomRequestBuilderService.getRequestParameterMap() - loginID={},password={},productId={}",loginId,password,productId);
		Map<String,Object> requestParamsMap = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");  
        String CurrDateTime = sdf.format(new Date()).toString();
        String b64ClientCode = Base64.encodeBase64String("shcil".getBytes("UTF-8"));
        String atomCallbackUrl = Util.getSiteUrlWithContextPath() + ATOMURL;
		Customer customer = clonedCart.getCustomer();
		requestParamsMap.put("login",loginId);
		requestParamsMap.put("pass",password);            
		requestParamsMap.put("ttype","NBFundTransfer");
		requestParamsMap.put("prodid",productId);
		requestParamsMap.put("amt",paymentTransaction.getAmount().intValue()+"");
		requestParamsMap.put("txncurr","INR");
		requestParamsMap.put("txnscamt","0");
		requestParamsMap.put("clientcode",b64ClientCode);
		requestParamsMap.put("txnid",paymentTransaction.getBluestoneTxnId());
		requestParamsMap.put("date",CurrDateTime);
		requestParamsMap.put("custacc","888888888888");
		requestParamsMap.put("ru",atomCallbackUrl);			
		requestParamsMap.put("udf1",customer.getUserName());
		requestParamsMap.put("udf2",customer.getEmail());
		Address latestShippingAddress = shippingService.getLatestShippingAddress(customer);
		requestParamsMap.put("udf3",latestShippingAddress.getContactNumber());
		requestParamsMap.put("udf4",latestShippingAddress.getContactNumber());//TODO: tinku - is it necessary to set Customer Address
		
		String mdd = CreditCard;        
        PaymentInstrument paymentInstrument = paymentTransaction.getPaymentInstrument();
        String instrumentType = paymentInstrument.getInstrumentType();
        if (Constants.DEBITCARD.equals(instrumentType)) {
            mdd = DebitCard;
            requestParamsMap.put("mdd",mdd);
        } else if (Constants.CREDITCARD.equals(instrumentType)) {
            mdd = CreditCard;
            requestParamsMap.put("mdd",mdd);
        } else if (Constants.NETBANKING.equals(instrumentType)) {
            mdd = NetBanking;            
            requestParamsMap.put("mdd",mdd);
        }else{
        	requestParamsMap.put("udf5",getBankForEMI(paymentTransaction));//Bank Name
    		requestParamsMap.put("udf6",getEMITenure(paymentTransaction));//EMI Tenure
        }
        log.debug("AtomRequestBuilderService.getRequestParameterMap() - built param Map=[{}]",requestParamsMap);
		return requestParamsMap;
	}
	
	public String buildSecondRequestURL(String vXMLStr) throws ParserConfigurationException, SAXException, IOException{
		log.info("AtomRequestBuilderService.buildSecondRequestURL() - xmlString={}",vXMLStr);
		String secondRequestURL="";
		Map<String, String> xmlResponseMap = atomXMLParserService.parseResponse(vXMLStr);
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(xmlResponseMap.get("xmlURL")).append("?");
		urlBuilder.append("ttype=").append(xmlResponseMap.get("xmlttype")).append("&");
		urlBuilder.append("tempTxnId=").append(xmlResponseMap.get("xmltempTxnId")).append("&");
		urlBuilder.append("token=").append(xmlResponseMap.get("xmltoken")).append("&");
		urlBuilder.append("txnStage=").append(xmlResponseMap.get("xmltxnStage"));
		secondRequestURL = urlBuilder.toString();
    	secondRequestURL = secondRequestURL.replace(" ","%20");
    	log.info("AtomRequestBuilderService.buildSecondRequestURL() - Second Request URL={}",secondRequestURL);
		return secondRequestURL;
	}	
	
	private String getBankForEMI(PaymentTransaction paymentTransaction) {
	    PaymentInstrument paymentInstrument = paymentTransaction.getPaymentInstrument();	    
	
	    String issuingAuthority = paymentInstrument.getIssuingAuthority();
	    String bankCode = null;
	    if (issuingAuthority.equalsIgnoreCase("axis_bank")) {
	        bankCode = "AXIS";
	    } else if (issuingAuthority.equalsIgnoreCase("icici_bank")) {
	        bankCode = "ICICI";
	    } else if (issuingAuthority.equalsIgnoreCase("standard_chartered_bank")) {
	        bankCode = "SCB";
	    } else if (issuingAuthority.equalsIgnoreCase("hsbc_bank")) {
	        bankCode = "HSBC";
	    } else if (issuingAuthority.equalsIgnoreCase("kotak_bank")) {
	        bankCode = "KOTAK";
	    }
	    log.debug("AtomRequestBuilderService.getBankForEMI() - Bank Code={}",bankCode);
	    return bankCode;
	}
	
	private String getEMITenure(PaymentTransaction paymentTransaction) {
	    PaymentInstrument paymentInstrument = paymentTransaction.getPaymentInstrument();
	    String tenureCode = null;
	    String instrumentType = paymentInstrument.getInstrumentType();
	    if (instrumentType.equals("emi3months")) {
	        tenureCode = "02";
	    } else if (instrumentType.equals("emi6months")) {
	        tenureCode = "03";
	    } else if (instrumentType.equals("emi9months")) {
	        tenureCode = "04";
	    } /*else if (instrumentType.equals("emi12months")) {
	        tenureCode = "05";
	    }*/	
	    log.debug("AtomRequestBuilderService.getEMITenure() - Tenure Code={}",tenureCode);
	    return tenureCode;
	}	

}
