package com.bluestone.app.integration.uniware;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QService {

    private static final Logger log = LoggerFactory.getLogger(QService.class);

    @Autowired
    private UniwareJMSProducer uniwareJMSProducer;

    public void send(long orderId) {
        log.debug("QService.send message to Uniware Q for OrderId=[{}]", orderId);
        uniwareJMSProducer.queueMessage(Long.toString(orderId));
    }
}
