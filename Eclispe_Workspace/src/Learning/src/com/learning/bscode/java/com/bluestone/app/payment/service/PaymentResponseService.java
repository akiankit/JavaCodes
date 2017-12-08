package com.bluestone.app.payment.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.payment.spi.PaymentUtil.BLUESTONE_FIELDS;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentError;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.PaymentMetaData;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class PaymentResponseService {
	
	private static final Logger log = LoggerFactory.getLogger(PaymentResponseService.class);
	
	@Autowired
    protected PaymentTransactionService paymentTransactionService;

    @Autowired
    protected PaymentInstrumentService paymentInstrumentService;

	
	private PaymentTransaction updatePaymentTransaction(PaymentTransaction paymentTransaction, PaymentResponse paymentResponse) {
    	log.info("PaymentResponseService.updatePaymentTransaction(): Updating payment transaction for id {}",paymentTransaction.getBluestoneTxnId());
    	PaymentInstrument paymentInstrument = paymentResponse.getPaymentInstrument();
        if (paymentInstrument != null) {
            paymentInstrument = paymentInstrumentService.createOrGetPaymentInstrument(paymentInstrument);
            paymentResponse.setPaymentInstrument(paymentInstrument);
        }
        
    	String clonedCartId = paymentTransaction.getPaymentMetaDataValue(PaymentMetaData.Name.CLONED_CART_ID);
        paymentResponse.setClonedCartId(Long.parseLong(clonedCartId));
        
        paymentTransaction.setPaymentResponse(paymentResponse);        

        paymentTransaction = paymentTransactionService.updatePaymentTransaction(paymentTransaction);
        log.info("PaymentResponseService.updatePaymentTransaction(): Payment transaction successfully updated for id {}",paymentTransaction.getBluestoneTxnId());
        return paymentTransaction;
	}
    
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
	public PaymentTransaction updatePaymentStatusAfterResponse(PaymentResponse paymentResponse) throws PaymentException{    	
    	Map<BLUESTONE_FIELDS, String> merchantFields = paymentResponse.getMerchantFields();
        String encryptedTxnId = merchantFields.get(BLUESTONE_FIELDS.ENCRYPTED_PAYMENT_TXN_ID);
        log.info("PaymentResponseService.getPaymentTransactionFromResponse(): Retrieving payment transaction for id {}",encryptedTxnId);
        String decryptTxnId = CryptographyUtil.decrypt(encryptedTxnId);
        long txnId = Long.parseLong(decryptTxnId);
        PaymentTransaction paymentTransaction = paymentTransactionService.getPaymentTransactionWithRowLock(txnId);
        if(paymentTransaction==null){        	
        	PaymentException paymentException = new PaymentException(PaymentError.TRANSACTION_NOT_FOUND, encryptedTxnId);
            paymentException.setPaymentResponse(paymentResponse.toString());            
            throw paymentException;
        }
        if (paymentTransaction.getPaymentTransactionStatus().equals(PaymentTransactionStatus.PAYMENT_REQUEST_STARTED)) {
            paymentTransaction = updatePaymentTransaction(paymentTransaction, paymentResponse);
        } else {
            PaymentException paymentException = new PaymentException(PaymentError.TRANSACTION_STATE_MISMATCHED, encryptedTxnId);
            paymentException.setDetailedReason("[Expected Status=" + PaymentTransactionStatus.PAYMENT_REQUEST_STARTED.name()
                                               + " Actual Status = " + paymentTransaction.getPaymentTransactionStatus().name()
                                               + " ]");
            paymentException.setPaymentResponse(paymentResponse.toString());
            throw paymentException;
        }
        return paymentTransaction;
    }
    

}
