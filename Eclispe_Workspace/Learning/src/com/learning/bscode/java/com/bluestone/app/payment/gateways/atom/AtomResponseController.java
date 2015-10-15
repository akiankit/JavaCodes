package com.bluestone.app.payment.gateways.atom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bluestone.app.payment.spi.controller.PaymentResponseController;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentGateway;

@Controller
@RequestMapping(value="/pg*")
public class AtomResponseController extends PaymentResponseController{

	private static final Logger log = LoggerFactory.getLogger(AtomResponseController.class);

    @RequestMapping(value="/atom*")
    public void paymentResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws PaymentException {
    	try {
            handlePostAuthorizeResponse(httpServletRequest, httpServletResponse, PaymentGateway.ATOM);
        } catch (PaymentException e) {
            if(log.isErrorEnabled()) {
                log.error("Error while handling paymentResponse", e);
            }
            throw e;
        }
    }
}
