package com.bluestone.app.payment.gateways.innoviti;

import java.io.StringReader;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.payment.gateways.SHACheckSum;
import com.bluestone.app.payment.service.PaymentParametersHandler;
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
public class InnovitiPaymentService implements PaymentGatewayService {

    private static final String REQUEST_HASH_SEQUENCE = "orderId|merchantId|subMerchantId|amt|cur|proSku|Cname|mobile|emailId|redirUrl";

    private static final Logger log = LoggerFactory.getLogger(InnovitiPaymentService.class);

    @Value("${innoviti.gatewayUrl}")
    private String gatewayUrl;

    @Value("${innoviti.merchantId}")
    private String merchantId;

    @Value("${innoviti.subMerchantId}")
    private String subMerchantId;

    @Value("${innoviti.salt}")
    private String salt;

    @Value("${innoviti.redirectUrl}")
    private String redirectUrl;
    
    @Autowired
    private PaymentParametersHandler paymentParametersHandler;

    @Override
    public PaymentGateway getPaymentGateway() {
        return PaymentGateway.INNOVITI;
    }

    @Override
    public PaymentResponse handlePaymentResponse(HttpServletRequest httpServletRequest) throws PaymentException {
        log.debug("InnovitiPaymentService.handlePaymentResponse(): ");

        StringBuilder responseStringBuilder = paymentParametersHandler.generateDebugMessage(httpServletRequest);
        StringReader characterStream = null;
        InputSource is = null;
        String encrytedTxnId = "";
        PaymentResponse paymentResponse = null;
        try {
            paymentResponse = new PaymentResponse();
            paymentResponse.setResponseAsText(responseStringBuilder.toString());

            String parameter = httpServletRequest.getParameter("transresponse");
            if (StringUtils.isBlank(parameter)) {
                PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE, encrytedTxnId);
                paymentException.setPaymentResponse(paymentResponse.getResponseAsText());
                throw paymentException;
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            characterStream = new StringReader(parameter);
            is = new InputSource(characterStream);
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("sres");
            if (nList.getLength() == 1) {
                Element sResElem = (Element) nList.item(0);
                String orderId = getTagValue("orderId", sResElem);
                String merchantIdFromResponse = getTagValue("merchantId", sResElem);
                String resCode = getTagValue("resCode", sResElem);
                String resmsg = getTagValue("resmsg", sResElem);
                if (resCode.equalsIgnoreCase("00")) {
                    paymentResponse.setGatewayResponse(GATEWAYRESPONSE.SUCCESS);
                } else {
                    paymentResponse.setGatewayResponse(GATEWAYRESPONSE.FAILURE);
                    paymentResponse.setFailureMessage(resmsg);
                }
                encrytedTxnId = getTagValue("mdf1", sResElem);
                
                paymentResponse.addMerchantField(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID, encrytedTxnId);

                NodeList responseDetailsList = sResElem.getElementsByTagName("respDet");
                if (responseDetailsList.getLength() == 1) {
                    Element resDetailsElem = (Element) responseDetailsList.item(0);
                    String unipayId = getTagValue("UnipayId", resDetailsElem);
                    String txnRefNo = getTagValue("txnRefNo", resDetailsElem);
                    paymentResponse.setBankReferenceNo(txnRefNo);
                    paymentResponse.setGatewayTransactionId(unipayId);

                    String checkSum = getTagValue("checkSum", sResElem);
                    final String computedCheckSum = SHACheckSum.computeResponseHashForInnoviti(orderId, salt, merchantId, unipayId, resCode, resmsg);
                    if (checkSum.equals(computedCheckSum)) {
                        log.debug("Innovti checksum matched for {}", paymentResponse.getMerchantFields());
                    } else {
                        log.error("Checksum mismatch for InnovitiPaymentService. Checksum received ={} , computed checksum ={}", checkSum, computedCheckSum);
                        PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE_HASH, encrytedTxnId);
                        paymentException.setPaymentResponse(paymentResponse.toString());
                        throw paymentException;
                    }
                }
            }
            return paymentResponse;
        } catch (PaymentException paymentException) {
            log.error("Error while executing InnovitiPaymentService.handlePaymentResponse(): Response for encrytedTxnId={} was {} .\nError={}",
                      encrytedTxnId, responseStringBuilder, paymentException.toString(), paymentException);
            throw paymentException;
        } catch (Throwable e) {
            log.error("Error while executing InnovitiPaymentService.handlePaymentResponse(): Response for encrytedTxnId={} was {} .\nError={}",
                      encrytedTxnId, responseStringBuilder, e.toString(), e);
            PaymentException paymentException = new PaymentException(PaymentError.MALFORMED_RESPONSE, e, encrytedTxnId);
            paymentException.setPaymentResponse(paymentResponse.toString());
            throw paymentException;
        } finally {
            if (characterStream != null) {
                characterStream.close();
            }
        }
    }

    private static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue != null) {
            return nValue.getNodeValue();
        } else {
            return null;
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
        log.debug("InnovitiPaymentService.createPaymentRequest() for {}", paymentTransaction.toStringBrief());
        try {
            PaymentRequest preAuthorizeRequest = new PaymentRequest();
            preAuthorizeRequest.setVmTemplate(true);
            preAuthorizeRequest.setVmTemplatePath("pgforms/innoviti.vm");

            /*SubPayment subPayment = paymentTransaction.getSubPayment();
         Cart cart = subPayment.getMasterPayment().getCart();*/
            Customer customer = clonedCart.getCustomer();

            Date date = DateTimeUtil.getDate();
            String purchaseDate = DateTimeUtil.formatDate(date, "yyyyMMDD");

            preAuthorizeRequest.addTemplateVariable("paymentGatewayUrl", gatewayUrl);
            preAuthorizeRequest.addTemplateVariable("orderId", paymentTransaction.getBluestoneTxnId());
            preAuthorizeRequest.addTemplateVariable("merchantId", merchantId);
            preAuthorizeRequest.addTemplateVariable("subMerchantId", subMerchantId);
            preAuthorizeRequest.addTemplateVariable("amt", paymentTransaction.getAmount().intValue());
            preAuthorizeRequest.addTemplateVariable("cur", "INR");

            String processingCode = null;
            setProcessingCode(paymentTransaction, preAuthorizeRequest, processingCode);
            preAuthorizeRequest.addTemplateVariable("redirUrl", redirectUrl);
            preAuthorizeRequest.addTemplateVariable("proSku", clonedCart.getActiveCartItems().get(0).getSkuCodeWithSize());
            preAuthorizeRequest.addTemplateVariable("isCtx", "NO");
            preAuthorizeRequest.addTemplateVariable("Cname", customer.getUserName());
            preAuthorizeRequest.addTemplateVariable("userId", customer.getId() + "");
            preAuthorizeRequest.addTemplateVariable("emailId", customer.getEmail());
            preAuthorizeRequest.addTemplateVariable("mobile", customer.getCustomerPhone());
            preAuthorizeRequest.addTemplateVariable("purchDate", purchaseDate);
            preAuthorizeRequest.addTemplateVariable("purchTime", DateTimeUtil.formatDate(date, "HHmmss"));
            preAuthorizeRequest.addTemplateVariable("mdf1", paymentTransaction.getBluestoneTxnId());
            String computeRequestHash = SHACheckSum.computeRequestHash(preAuthorizeRequest, salt, SHACheckSum.MD5, REQUEST_HASH_SEQUENCE);
            preAuthorizeRequest.addTemplateVariable("chksum", computeRequestHash);            
            return preAuthorizeRequest;
        } catch (Exception e) {
            log.error("Error while creating payment request for {} ", paymentTransaction, e);
            throw new PaymentException(PaymentError.PREAUTH_REQUEST_GENERATION_FAILED, e, paymentTransaction.getBluestoneTxnId());
        }
    }

    private void setProcessingCode(PaymentTransaction paymentTransaction, PaymentRequest preAuthorizeRequest, String processingCode) {
        PaymentInstrument paymentInstrument = paymentTransaction.getPaymentInstrument();
        int tenureCode = 0;
        String instrumentType = paymentInstrument.getInstrumentType();
        if (instrumentType.equals("emi3months")) {
            tenureCode = 1;
        } else if (instrumentType.equals("emi6months")) {
            tenureCode = 2;
        } else if (instrumentType.equals("emi9months")) {
            tenureCode = 3;
        } else if (instrumentType.equals("emi12months")) {
            tenureCode = 4;
        }

        String issuingAuthority = paymentInstrument.getIssuingAuthority();
        int bankCode = 0;
        if (issuingAuthority.equalsIgnoreCase("hdfc_bank")) {
            bankCode = 1;
        } else if (issuingAuthority.equalsIgnoreCase("axis_bank")) {
            bankCode = 2;
        } else if (issuingAuthority.equalsIgnoreCase("icici_bank")) {
            bankCode = 3;
        } else if (issuingAuthority.equalsIgnoreCase("citi_bank")) {
            bankCode = 5;
        } else if (issuingAuthority.equalsIgnoreCase("standard_chartered_bank")) {
            bankCode = 6;
        } else if (issuingAuthority.equalsIgnoreCase("hsbc_bank")) {
            bankCode = 7;
        } else if (issuingAuthority.equalsIgnoreCase("kotak_bank")) {
            bankCode = 8;
        } else if (issuingAuthority.equalsIgnoreCase("sbi_bank")) {
            bankCode = 9;
        }

        StringBuilder procCodeBuilder = new StringBuilder().append(bankCode).append(tenureCode).append("0000");
        preAuthorizeRequest.addTemplateVariable("processingCode", procCodeBuilder.toString());
    }
    
}
