package com.bluestone.app.payment.gateways.amex;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluestone.app.payment.spi.controller.PaymentResponseController;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentTransaction;

@Controller
@RequestMapping(value="/pg/amex*")
public class AmexResponseController extends PaymentResponseController{
	
private static final PaymentGateway AMEX = PaymentGateway.AMEX;
    
    private static final Logger log = LoggerFactory.getLogger(AmexResponseController.class);
    
    @RequestMapping(value="/response*")
    public void paymentResponse(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) throws PaymentException {
        try {
            handlePostAuthorizeResponse(httpServletRequest, httpServletResponse, AMEX);
        } catch (PaymentException e) {
            if(log.isErrorEnabled()) {
                log.error("Error while handling paymentResponse", e);
            }
            throw e;
        }
        
    }

}
