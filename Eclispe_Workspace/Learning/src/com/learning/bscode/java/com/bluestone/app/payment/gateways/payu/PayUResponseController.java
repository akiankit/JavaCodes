package com.bluestone.app.payment.gateways.payu;

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
@RequestMapping(value = "/pg/payu/*")
public class PayUResponseController extends PaymentResponseController {

    private static final PaymentGateway PAYU = PaymentGateway.PAYU;

    private static final Logger log = LoggerFactory.getLogger(PayUResponseController.class);

    @RequestMapping(value = "/success*")
    public void paymentSuccess(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) throws PaymentException {
        try {
            handlePostAuthorizeResponse(httpServletRequest, httpServletResponse, PAYU);
        } catch (PaymentException e) {
            log.error("Error: PayUResponseController.paymentSuccess(): ", e);
            throw e;
        }
    }

    @RequestMapping(value = "/failure*")
    public void paymentFailure(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) throws PaymentException {
        try {
            handlePostAuthorizeResponse(httpServletRequest, httpServletResponse, PAYU);
        } catch (PaymentException e) {
            log.error("Error: PayUResponseController.paymentFailure(): ");
            throw e;
        }

    }
}
