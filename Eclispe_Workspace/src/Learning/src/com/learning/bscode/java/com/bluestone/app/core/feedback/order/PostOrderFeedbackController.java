package com.bluestone.app.core.feedback.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bluestone.app.core.util.EncryptionException;

@Controller
@RequestMapping(value = "/feedback/order*")
public class PostOrderFeedbackController {
	
	private static final Logger log = LoggerFactory.getLogger(PostOrderFeedbackController.class);
	
	@Autowired
	private OrderFeedbackEmailService orderFeedbackEmailService;
	
	@RequestMapping(value = "/email/{orderId}", method = RequestMethod.GET)
	public ModelAndView sendOrderFeedbackEmail(@PathVariable("orderId") String orderId){
		log.info("PostOrderFeedbackController.sendOrderFeedbackEmail for order id {}",orderId);
		try {
			orderFeedbackEmailService.sendOrderFeedbackEmail(orderId);
		} catch (EncryptionException e) {
			log.error("Error while sending email to customer with order id {} - Reason={}",orderId, e.toString(), e);
		}
		return null;
	}

}
