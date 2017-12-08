package com.bluestone.app.checkout.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.bluestone.app.account.model.Customer;

@Component
public class CustomerValidator {

	@Autowired
	private Validator validator;
	
	public void validateCheckoutFlowRegistration(Customer customer, ValidationContext context) {
	    BindingResult result = new BeanPropertyBindingResult(customer, "customer");
	    for (ConstraintViolation<Customer> constraint : validator.validate(customer)) {
	        result.rejectValue(constraint.getPropertyPath().toString(), "", constraint.getMessage());
	    }
	
	    MessageContext messages = context.getMessageContext();
	    if (result.hasErrors()) {
	        for (FieldError fieldError : result.getFieldErrors()) {
	            messages.addMessage(new MessageBuilder().error().source(fieldError.getField()).defaultText(fieldError.getDefaultMessage()).build());
	          }
	      }
	}
}
