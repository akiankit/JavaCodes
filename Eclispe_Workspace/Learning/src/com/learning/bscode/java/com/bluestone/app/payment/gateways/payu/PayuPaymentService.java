package com.bluestone.app.payment.gateways.payu;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.payment.gateways.SHACheckSum;
import com.bluestone.app.payment.spi.PaymentUtil.BLUESTONE_FIELDS;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.CaptureTransaction;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.PaymentRequest;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentResponse.GATEWAYRESPONSE;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.service.PaymentGatewayService;

@Service
public class PayuPaymentService implements PaymentGatewayService {
    
    private static final String REQUEST_HASH_SEQUENCE = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
    private static final String RESPONSE_HASH_SEQUENCE = "status||||||udf5|udf4|udf3|udf2|udf1|email|firstname|productinfo|amount|txnid";
    
    // bank codes for netbanking seamless integration
    private Map<String, String> bankCodes = new HashMap<String, String>();

    private static final String CreditCard            = "CC";
    private static final String DebitCard             = "DC";
    private static final String NetBanking            = "NB";
    private static final String EMI                   = "EMI";
    
    private static final Logger log                   = LoggerFactory.getLogger(PayuPaymentService.class);
    
    @Value("${payu.gatewayUrl}")
    private String              gatewayUrl;
    
    @Value("${payu.merchantId}")
    private String              merchantId;
    
    @Value("${payu.salt}")
    private String              salt;
    
    @Override
    public PaymentGateway getPaymentGateway() {
        return PaymentGateway.PAYU;
    }
    
    @PostConstruct
    public void init() {
        bankCodes.put("corporation_bank", "CRPB");
        bankCodes.put("deutsche_bank", "DSHB");                                        
        bankCodes.put("south_indian_bank", "SOIB");
        bankCodes.put("state_bank_of_hyderbad", "SBHB");                                  
        bankCodes.put("state_bank_of_travankore", "SBTB");                                      
        bankCodes.put("union_bank_of_india", "UBIB");                                        
        bankCodes.put("icici_bank", "ICIB");
        bankCodes.put("hdfc_bank", "HDFB");
        bankCodes.put("citi_bank", "CITNB");
        bankCodes.put("state_bank_of_india", "SBIB");
        bankCodes.put("axis_bank", "AXIB");
        bankCodes.put("idbi_bank", "IDBB");
        bankCodes.put("yes_bank", "YESB");
        bankCodes.put("bank_of_maharashtra", "BOMB");
        bankCodes.put("bank_of_india", "BOIB");
        bankCodes.put("federal_bank", "FEDB");
        bankCodes.put("development_credit_bank", "DCBB");
        bankCodes.put("indian_overseas_bank", "INOB");
        bankCodes.put("indus_ind_bank", "INIB");
        bankCodes.put("jammu_and_kashmir_bank", "JAKB");
        bankCodes.put("karnataka_bank", "KRKB");
        bankCodes.put("state_bank_of_mysore", "SBMB");
        bankCodes.put("state_bank_of_jaipur_and_bikaner", "SBBJB");
        bankCodes.put("united_bank_of_india", "UNIB");
        bankCodes.put("vijaya_bank", "VJYB");
        bankCodes.put("karur_vysya_bank", "KRVB");
    }
    
    @Override
    public PaymentResponse handlePaymentResponse(HttpServletRequest httpServletRequest) throws PaymentException {
        log.debug("PayuPaymentService.handlePaymentResponse(): BEGINS ------- ");
        StringBuilder responseStringBuilder = generateDebugMessage(httpServletRequest);
        String encrytedTxnId = "";
        try {
            encrytedTxnId = httpServletRequest.getParameter("udf1");
            
            if (StringUtils.isBlank(encrytedTxnId)) {
                PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE, encrytedTxnId);
                paymentException.setPaymentResponse(responseStringBuilder.toString());
                throw paymentException;
            }
            PaymentResponse paymentResponse = new PaymentResponse();
            paymentResponse.setResponseAsText(responseStringBuilder.toString());
            paymentResponse.setGatewayTransactionId(httpServletRequest.getParameter("mihpayid"));
            paymentResponse.setAmount(new BigDecimal(httpServletRequest.getParameter("amount")));
            paymentResponse.setBankReferenceNo(httpServletRequest.getParameter("bank_ref_num"));
            getPaymentStatus(httpServletRequest, paymentResponse);
            
            PaymentInstrument paymentInstrument = new PaymentInstrument();
            getPaymentMode(httpServletRequest, paymentInstrument);
            String pgType = httpServletRequest.getParameter("PG_TYPE");
            paymentInstrument.setIssuingAuthority(pgType);
            paymentResponse.setPaymentInstrument(paymentInstrument);
                        
            paymentResponse.addMerchantField(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID, encrytedTxnId);            
            
            String hash = httpServletRequest.getParameter("hash");
            final String computedHash = SHACheckSum.computeResponseHash(httpServletRequest, salt, merchantId, RESPONSE_HASH_SEQUENCE, SHACheckSum.SHA512);
            if (hash.equals(computedHash)) {
                log.debug("Payu hash matched for {}", paymentResponse.getMerchantFields());
            } else {
                log.error("Hash mismatch for PayuPaymentService. Hash received ={} , computed hash ={} . Merchant Fields={}",
                          hash, computedHash, paymentResponse.getMerchantFields());
                //log.info("Payu response = {}", paymentResponse.toString());
                PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE_HASH, encrytedTxnId);
                paymentException.setPaymentResponse(paymentResponse.toString());
                throw paymentException;
            }
            return paymentResponse;
        } catch (PaymentException paymentException) {
            log.error("Error while executing PayuPaymentService.handlePaymentResponse(): EncrytedTxnId={} , Response={} ",
                      encrytedTxnId, responseStringBuilder, paymentException);
            throw paymentException;
        } catch (Exception e) {
            log.error("Error while executing PayuPaymentService.handlePaymentResponse() EncrytedTxnId={} , Response={} ",
                      encrytedTxnId, responseStringBuilder, e);
            PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE, e, encrytedTxnId);
            paymentException.setPaymentResponse(responseStringBuilder.toString());
            throw paymentException;
        }
    }
    
    private void getPaymentMode(HttpServletRequest httpServletRequest, PaymentInstrument paymentInstrument) {
        String paymentMode = httpServletRequest.getParameter("mode");
        if (paymentMode.equalsIgnoreCase(CreditCard)) {
            paymentInstrument.setInstrumentType(Constants.CREDITCARD);
        } else if (paymentMode.equalsIgnoreCase(NetBanking)) {
            paymentInstrument.setInstrumentType(Constants.NETBANKING);
        } else if (paymentMode.equalsIgnoreCase(DebitCard)) {
            paymentInstrument.setInstrumentType(Constants.DEBITCARD);
        } else {
            paymentInstrument.setInstrumentType(paymentMode);
        }
    }
    
    private void getPaymentStatus(HttpServletRequest httpServletRequest, PaymentResponse postAuthorizeResponse) {
        String status = httpServletRequest.getParameter("status");
        log.info("*** PayuPaymentService.getPaymentStatus(): Status=[{}] ***", status);
        if (status.equalsIgnoreCase("SUCCESS")) {
            postAuthorizeResponse.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);
        } else if (status.equalsIgnoreCase("FAILURE")) {
            postAuthorizeResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
            String error = httpServletRequest.getParameter("error");
            if(StringUtils.isBlank(error)) {
                error = httpServletRequest.getParameter("Error");
                if(StringUtils.isBlank(error)) {
                  error = httpServletRequest.getParameter("unmappedstatus");
                }
            }  
            postAuthorizeResponse.setFailureMessage(error);
        } else if (status.equalsIgnoreCase("PENDING")) {
            postAuthorizeResponse.setGatewayResponse(GATEWAYRESPONSE.PENDING);
        }
    }
    
    @Override
    public CaptureTransaction capture(PaymentTransaction paymentTransaction) throws PaymentException{
    	CaptureTransaction captureTransaction = new CaptureTransaction();
    	captureTransaction.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);
    	return captureTransaction;
    }
    
    @Override
    public void cancel(PaymentTransaction paymentTransaction) throws PaymentException {
    }
    
    @Override
    public void refund(PaymentTransaction paymentTransaction) throws PaymentException {
    }
    
    @Override
    public PaymentRequest createPaymentRequest(PaymentTransaction paymentTransaction, Cart clonedCart, Cart originalCart) throws PaymentException {
        log.debug("PayuPaymentService.createPaymentRequest(): paymentTransaction={}", paymentTransaction.getBluestoneTxnId());
        try {
            PaymentRequest preAuthorizeRequest = new PaymentRequest();
            preAuthorizeRequest.setVmTemplate(true);
            preAuthorizeRequest.setVmTemplatePath("pgforms/payu.vm");
            
            Customer customer = clonedCart.getCustomer();
            
            // payUBaseCallbackUrl ~ hostname/contextpath/pg/payu
            String payUBaseCallbackUrl = Util.getSiteUrlWithContextPath() + Constants.PAYMENT_BASE_CALLBACK_URL + PaymentGateway.PAYU.name().toLowerCase();
            preAuthorizeRequest.addTemplateVariable("paymentGatewayUrl", gatewayUrl);
            preAuthorizeRequest.addTemplateVariable("key", merchantId);
            preAuthorizeRequest.addTemplateVariable("txnid", paymentTransaction.getBluestoneTxnId());
            preAuthorizeRequest.addTemplateVariable("amount", paymentTransaction.getAmount().intValue() + "");
            preAuthorizeRequest.addTemplateVariable("firstname", customer.getUserName());
            preAuthorizeRequest.addTemplateVariable("email", customer.getEmail());
            preAuthorizeRequest.addTemplateVariable("phone", customer.getCustomerPhone());
            preAuthorizeRequest.addTemplateVariable("productinfo", clonedCart.getActiveCartItems().get(0).getSkuCodeWithSize());
            preAuthorizeRequest.addTemplateVariable("surl", payUBaseCallbackUrl + "/success");
            preAuthorizeRequest.addTemplateVariable("furl", payUBaseCallbackUrl + "/failure");
            preAuthorizeRequest.addTemplateVariable("udf1", paymentTransaction.getBluestoneTxnId());
            
            String pg = CreditCard;
            String dropCategroy = null;
            PaymentInstrument paymentInstrument = paymentTransaction.getPaymentInstrument();
            String instrumentType = paymentInstrument.getInstrumentType();
            if (instrumentType.equals(Constants.DEBITCARD)) {
                pg = DebitCard;
                dropCategroy = CreditCard + "," +  NetBanking + "," + EMI;
            } else if (instrumentType.equals(Constants.CREDITCARD)) {
                pg = CreditCard;
                dropCategroy = DebitCard + "," +  NetBanking + "," + EMI;
            } else if (instrumentType.equals(Constants.NETBANKING)) {
                pg = NetBanking;
                dropCategroy = CreditCard + "," +  DebitCard + "," + EMI;
                setBankCodeForNetBanking(preAuthorizeRequest, paymentInstrument.getIssuingAuthority());
            } else {
                pg = EMI;
                dropCategroy = CreditCard + "," +  DebitCard + "," + NetBanking;

            }
            preAuthorizeRequest.addTemplateVariable("pg", pg);
            preAuthorizeRequest.addTemplateVariable("drop_category", dropCategroy);
            String computeRequestHash = SHACheckSum.computeRequestHash(preAuthorizeRequest, salt, SHACheckSum.SHA512, REQUEST_HASH_SEQUENCE);
            preAuthorizeRequest.addTemplateVariable("hash", computeRequestHash);            
            return preAuthorizeRequest;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error while creating payment request for payment transaction " + paymentTransaction, e);
            }
            throw new PaymentException(PaymentError.PREAUTH_REQUEST_GENERATION_FAILED, e, paymentTransaction.getBluestoneTxnId());
        }
    }
    
    static StringBuilder generateDebugMessage(HttpServletRequest httpServletRequest) {
        log.debug("PayuPaymentService.generateDebugMessage()");
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        final Set<String> set = parameterMap.keySet();
        StringBuilder stringBuilder = new StringBuilder();
        int counter = 1;
        for (String eachKey : set) {
            stringBuilder.append(eachKey).append("=").append(Arrays.toString(parameterMap.get(eachKey))).append('\t');
            if (counter == 6 || eachKey.contains("hash")) {
                stringBuilder.append("\n");
                counter = 0;
            }
            counter = counter + 1;
        }
        log.debug("PayuPaymentService.generateDebugMessage(): {}", stringBuilder.toString());
        return stringBuilder;
    }
    
    private void setBankCodeForNetBanking(PaymentRequest preAuthorizeRequest, String issuingAuthority) {
        log.debug("PayuPaymentService.setBankCodeForNetBanking(): IssuingAuthority={}", issuingAuthority);
        String bankCode = bankCodes.get(issuingAuthority);
        if(bankCode != null) {
            preAuthorizeRequest.addTemplateVariable("bankcode", bankCode);
        }
    }
    
}
