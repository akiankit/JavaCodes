package com.bluestone.app.payment.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.payment.spi.model.PaymentMetaData;

/**
 * @author Rahul Agrawal
 *         Date: 5/22/13
 */
public class PaymentMetaDataFactory {

    private static final Logger log = LoggerFactory.getLogger(PaymentMetaDataFactory.class);

    public static void addMetaData(PaymentMetaData.Name name, String value, List<PaymentMetaData> paymentMetaDatas) {
        PaymentMetaData paymentMetaData = create(name, value);
        paymentMetaDatas.add(paymentMetaData);
    }

    private static PaymentMetaData create(PaymentMetaData.Name name, String value) {
        PaymentMetaData paymentMetaData = new PaymentMetaData();
        paymentMetaData.setName(name);
        paymentMetaData.setValue(value);
        return paymentMetaData;
    }
}
