package com.bluestone.app.payment.gateways.innoviti;

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
@RequestMapping(value="/pg")
public class InnovitiResponseController extends PaymentResponseController {
    
    private static final Logger log = LoggerFactory.getLogger(InnovitiResponseController.class);

    @RequestMapping(value="/innoviti*")
    public void paymentResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws PaymentException {
        log.debug("PaymentResponse from Innoviti {}", httpServletRequest.getParameter("transresponse"));
        handlePostAuthorizeResponse(httpServletRequest, httpServletResponse, PaymentGateway.INNOVITI);
    }
    
}
