package com.bluestone.app.design.goldmine;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.NewAccountRegistrationServiceV2;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.BaseCartAction;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.Util;

@Component
public class GoldMineAction extends BaseCartAction {

    private static final int AMOUNT_MILTIPLICATION_FACTOR = 500;

    private static final int MIN_AMOUNT = 500;

    private static final int MAX_AMOUNT = 50000;

    private static final Logger log = LoggerFactory.getLogger(GoldMineAction.class);

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private NewAccountRegistrationServiceV2 accountRegistrationService;

    @Autowired
    private GoldMineService goldMineService;

    public Event createGoldMineCart(RequestContext requestContext) throws Exception {
        log.debug("GoldMineAction.createGoldMineCart()");
        try {
            Long visitorId = getVisitorId();
            ParameterMap requestParameters = requestContext.getRequestParameters();
            String amountInString = requestParameters.get("amount");
            String goldmineSchemeTerm = requestParameters.get("invest-option");
            String paymentId = requestParameters.get("paymentId");

            int amount = Integer.parseInt(amountInString);

            if (amount < MIN_AMOUNT || amount > MAX_AMOUNT) {
                throw new RuntimeException("Amount should be in range of " + MIN_AMOUNT + " to " + MAX_AMOUNT);
            }

            if (amount % AMOUNT_MILTIPLICATION_FACTOR != 0) {
                throw new RuntimeException("Amount should be in multiples of " + AMOUNT_MILTIPLICATION_FACTOR);
            }

            long goldMineProductId = goldMineService.getGoldMineProduct(BigDecimal.valueOf(amount)).getId();

            String userName = requestParameters.get("username");
            String email = requestParameters.get("email");
            String contactNo = requestParameters.get("contactno");
            Customer customer = getCustomer(requestContext, userName, email, contactNo);
            Cart cart = cartService.createCart(visitorId, customer);
            cart.setIsActive(false);

            cartService.addItemToCart(cart, goldMineProductId, 1);

            cart = cartService.updateCart(cart);
            requestContext.getFlowScope().put(CART, cart);

            //To fetch it in payment details option page. Need to store it in payment data while creating payment transaction.
            if (StringUtils.isNotBlank(goldmineSchemeTerm)) {
                requestContext.getFlowScope().put("goldMineSchemeTerm", goldmineSchemeTerm);
            } else {
                requestContext.getFlowScope().put("paymentId", paymentId);
            }

            return success();
        } catch (Throwable throwable) {
            log.error("Error: GoldMineAction.createGoldMineCart(): {} ", throwable.getMessage(), throwable);
            handleErrors(requestContext, throwable.getMessage());
            return error();
        }
    }

    private Long getVisitorId() {
        MessageContext messageContext = Util.getMessageContext();
        return (Long) messageContext.get(MessageContext.VISITOR_ID);
    }

    private Customer getCustomer(RequestContext context, String userName, String email, String contactNo) throws Exception {
        Customer customer = customerService.getCustomerByEmail(email.trim());
        if (customer != null) {
            // dont update the phone no. if its empty
            if (StringUtils.isNotBlank(contactNo)) {
                customer.setCustomerPhone(contactNo.trim());
            }
            customer.setUserName(userName);
            customer = customerService.updateCustomer(customer);
        } else {
            // customer has provided new credentials. But the form backing
            // customer still points to existing customer
            customer = new Customer();
            customer.setEmail(email.trim());
            customer.setCustomerPhone(contactNo);
            customer.setNewsletter(true);
            customer.setUserName(userName);
            accountRegistrationService.createAccount(customer);
            /*Map<Object, Object> createAccountResponse = accountRegistrationService.createAccount(customer);
			Boolean hasError = (Boolean) createAccountResponse.get(Constants.HAS_ERROR);
			if (hasError) {
				throw new Exception(createAccountResponse.get(Constants.MESSAGE).toString());
			}*/
        }
        return customer;
    }
}
