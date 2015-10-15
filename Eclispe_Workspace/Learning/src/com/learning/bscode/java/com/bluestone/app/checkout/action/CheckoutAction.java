package com.bluestone.app.checkout.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.bluestone.app.account.AccountCreationException;
import com.bluestone.app.account.AccountLoginService;
import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.NewAccountRegistrationServiceV2;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.admin.model.Carrier;
import com.bluestone.app.admin.service.CarrierService;
import com.bluestone.app.checkout.BaseCartAction;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.model.State;
import com.bluestone.app.shipping.service.ShippingService;

@Component
public class CheckoutAction extends BaseCartAction {

    private static final Logger log = LoggerFactory.getLogger(CheckoutAction.class);
    
	@Autowired
	private CustomerService customerService;

    @Autowired
    private NewAccountRegistrationServiceV2 accountRegistrationService;

    @Autowired
    private AccountLoginService accountLoginService;
	
	@Autowired
	private ShippingService shippingService;

	@Autowired
    private CarrierService carrierService;
	
	public Event getCustomer(RequestContext context) throws Exception {
        log.debug("CheckoutAction.getCustomer()");
		try {
		    Cart cart = getCartFromFlow(context);
		    if(cart == null || !cart.isActive()) {
		       throw new Exception("Cart is no longer active");
		    }
		    Customer customer = cart.getCustomer();
		    if(customer == null) {
		       customer = new Customer();
		    }
            context.getFlowScope().put("customer", customer);
            return success();
        } catch (Exception e) {
            log.error("Error at CheckoutAction.getCustomer()", e);
            handleErrors(context, e.getMessage());
            return error();
        }
	}

	public Event getCustomerShippingAddress(RequestContext context) throws Exception {
        log.debug("CheckoutAction.getCustomerShippingAddress()");
		try {                     
            Cart cart = getCartFromFlow(context);
            Customer customer = cart.getCustomer();
            if (customer == null) {
                throw new Exception("Customer account not found.");
            }
            Address address = null;
            Address addressFromDb = shippingService.getLatestShippingAddress(customer);
            if(addressFromDb != null) {
                address = (Address) addressFromDb.clone();
                address.setId(0);
            } else {
                address = new Address();
            	address.setFullname(customer.getUserName());
            	address.setContactNumber(customer.getCustomerPhone());
            	address.setCustomerId(customer);
            	address.setAlias("shipping");
            	State state = shippingService.getStateById(1); //TODO vikas:not sure why this is done. Need to check while testing
            	address.setState(state);
            }
            context.getFlowScope().put("address", address);
            return success();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error during CheckoutAction.getCustomerShippingAddress()", e);
            }
            handleErrors(context, e.getMessage());
            return error();
        }
	}

	public Event saveShippingAddress(RequestContext context) {
        log.debug("CheckoutAction.saveShippingAddress()");
        try {
            MutableAttributeMap flowScope = context.getFlowScope();
            Cart cart = getCartFromFlow(context);
            if(cart == null || !cart.isActive()) {
                throw new Exception("Cart is no longer active");
            }
            
            Customer customer = cart.getCustomer();
        
            Address address = (Address)flowScope.get("address");
            State stateFromDb = shippingService.getStateById(address.getState().getId()); 
            setShippingAddress(cart, customer, address, stateFromDb);
            setBillingAddress(cart, customer, address,stateFromDb);
            updateCart(context, cart);
            return success();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error during CheckoutAction.saveShippingAddress() ", e);
            }
            handleErrors(context, e.getMessage());
            return error();
        }
    }

    private void setShippingAddress(Cart cart, Customer customer, Address address, State stateFromDb) throws Exception {
        log.debug("CheckoutAction.setShippingAddress(): CartId={} , CustomerId={}", cart.getId(), customer.getId());
        if (address == null) {
            log.error("CheckoutAction.setShippingAddress(): Shipping address is null for CartId={} CustomerId={}", cart.getId(), customer.getId());
            throw new Exception("Customer shipping address not found");
        }
        
        Address latestShippingAddress = shippingService.getLatestShippingAddress(customer);
        if(latestShippingAddress == null || !address.equals(latestShippingAddress)) {
            // create new address
            address.setId(0);
            log.debug("CheckoutAction.setShippingAddress(): Create a new Shipping address for CustomerId={} using Address", customer.getId(), address);
            address.setState(stateFromDb);
            shippingService.createAddress(address);
        } else {
            // Customer didn't changed any fields in address page, use the same address id. Don't create new one.
            log.debug("CheckoutAction.setShippingAddress(): CustomerId={} didn't changed any fields in address page, use the same address id. Don't create new one.", customer.getId());
            address.setId(latestShippingAddress.getId());
        }
        cart.setShippingAddress(address);
    }

    private void setBillingAddress(Cart cart, Customer customer, Address address, State stateFromDb) {
        log.debug("CheckoutAction.setBillingAddress(): CartdId={}", cart.getId());
        Address billingAddress = new Address();
        billingAddress.setAddress(address.getAddress());
        billingAddress.setAlias("billing");
        billingAddress.setCityName(address.getCityName());
        billingAddress.setContactNumber(address.getContactNumber());
        billingAddress.setFullname(address.getFullname());
        billingAddress.setPostCode(address.getPostCode());
        billingAddress.setState(stateFromDb);
        billingAddress.setCustomerId(address.getCustomerId());
        shippingService.createAddress(billingAddress);
        cart.setBillingAddress(billingAddress);
    }
	
	public Event registerCustomer(RequestContext context) {
        log.debug("CheckoutAction.registerCustomer()");
        Customer customer = null;
        try {
            Cart cart = getCartFromFlow(context);
            customer = (Customer)context.getFlowScope().get("customer");
            if (customer == null) {
                throw new RuntimeException("Customer account could not be found");
            }
            Customer existingCustomer = customerService.getCustomerByEmail(customer.getEmail());
            if(existingCustomer != null) {
            	// dont update the phone no. if its empty
            	if(StringUtils.isNotBlank(customer.getCustomerPhone())) {
            		existingCustomer.setCustomerPhone(customer.getCustomerPhone());
            	}
            	existingCustomer.setUserName(customer.getUserName());
            	existingCustomer.setNewsletter(customer.getNewsletter());
            	existingCustomer = customerService.updateCustomer(existingCustomer);
            	addCustomerToCart(context, existingCustomer, cart);
            } else {
                if(customer.getId() != 0) {
                    // customer has provided new credentials. But the form backing customer still points to existing customer  
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(customer.getEmail());
                    newCustomer.setCustomerPhone(customer.getCustomerPhone());
                    newCustomer.setNewsletter(customer.getNewsletter());
                    newCustomer.setUserName(customer.getUserName());
                    customer = newCustomer;
                }

                accountRegistrationService.createAccount(customer);
                addCustomerToCart(context, customer, cart);

                //Map<Object, Object> createAccountResponse = accountRegistrationService.createAccount(customer);
                /*Boolean hasError = (Boolean) createAccountResponse.get(Constants.HAS_ERROR);
                    if(!hasError) {
                        addCustomerToCart(context, customer, cart);
                    } else {
                        handleErrors(context, createAccountResponse.get(Constants.MESSAGE).toString());
                        return error();
                    }*/
            }
            return success();
        } catch (AccountCreationException accountCreationException){
            log.error("Error during CheckoutAction.registerCustomer(): {}", (customer == null ? "" : customer.getEmail()), accountCreationException);
            handleErrors(context, accountCreationException.getMessage());
            return error();
        }
        catch (Exception e) {
            log.error("Error during CheckoutAction.registerCustomer(): {}", (customer == null ? "" : customer.getEmail()), e);
            handleErrors(context, e.getMessage());
            return error();
        }
    }

    public Event login(RequestContext context) {
        log.debug("CheckoutAction.login()");
        String email = null;
        try {
            email = context.getRequestParameters().get("email");
            String password = context.getRequestParameters().get("password");
            String errorMessage = accountLoginService.login(email, password);
            Cart cart = getCartFromFlow(context);
            if (StringUtils.isBlank(errorMessage)) {
                Customer customer = SessionUtils.getAuthenticatedCustomer();
                addCustomerToCart(context, customer, cart);
                return success();
            } else {
                handleErrors(context, errorMessage);
                return error();
            }
        } catch (Exception e) {
            log.error("Failed during CheckoutAction.login() for User=[{}]", email, e);
            handleErrors(context, e.getMessage());
            return error();
        }
    }
	
    private void addCustomerToCart(RequestContext context, Customer customer, Cart cart) {
        log.debug("CheckoutAction.addCustomerToCart(): CartId={} CustomerId={}", cart.getId(), customer.getId());
        Cart activeCart = cartService.getActiveCart(cart.getId());
        activeCart.setCustomer(customer);
        updateCart(context, activeCart);
        
        
        Visitor visitor = cart.getVisitor();
        if(visitor != null) {
            // TODO vikas: if old customer is already assigned to visitor , what should be done?  
           visitor.setCustomer(customer);
        }
	}
	
	public Event checkCarrierAvailability(RequestContext context) {
        log.debug("CheckoutAction.checkCarrierAvailability()");
        try {
        	Cart cart = getCartFromFlow(context);
        	Address shippingAddress = cart.getShippingAddress();
        	String postCode = shippingAddress.getPostCode();
        	double amount = cart.getFinalPrice().doubleValue();
            Carrier applicableCarrier = carrierService.getApplicableCarrier(postCode, amount);
            Double maxPrepaidLimit = carrierService.getMaxPrepaidLimit(postCode);
        	if(applicableCarrier==null){
        		context.getFlowScope().put("hasCarrierError", true);
        		if(maxPrepaidLimit!=null){
        			if(amount > maxPrepaidLimit){        				
        				context.getFlowScope().put("errorStr","Delivery is available for your location only for order amount less than " + NumberUtil.formatPriceIndian(maxPrepaidLimit, ","));
        			}
        		}else{
        			context.getFlowScope().put("errorStr","Sorry! Our third party delivery services do not deliver to the pincode ("+postCode+") provided. We are working on it.Please try an alternative pincode where the shipment can be delivered.");
        		}        		        		        	
        	}else{
        		context.getFlowScope().put("hasCarrierError", false);
        		Carrier applicableCarrierForCOD = carrierService.getApplicableCarrierForCOD(postCode, amount);
        		Double maxCODLimit = carrierService.getMaxCODLimit(postCode, amount);
            	if(applicableCarrierForCOD==null){
            		context.getFlowScope().put("hasCODError", true);
            		if(maxCODLimit!=null){            			
        				context.getFlowScope().put("errorStr","Cash On Delivery (COD) option is available for your location only for order amount upto Rs " + NumberUtil.formatPriceIndian(maxCODLimit, ","));
            		}else{
            			context.getFlowScope().put("errorStr","Cash On Delivery (COD) option is not available for your location.");
            		}            	            	
            	}else{            		            		
            		context.getFlowScope().put("hasCODError", false);            	
            	}
        	}        	
        	return success();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Error during CheckoutAction.checkCarrierAvailability()", e);
            }
            handleErrors(context, e.getMessage());
            return error();
        }
    }
	
	public Event checkCODAvailability(RequestContext context){
		log.debug("CheckoutAction.checkCODAvailability()");
		Cart cart = getCartFromFlow(context);
		List<CartItem> cartItems = cart.getCartItems(PRODUCT_TYPE.SOLITAIRE);
		if(cartItems.isEmpty()){
			context.getFlowScope().put("hasSolitaire", false);
		}else{
			context.getFlowScope().put("hasSolitaire", true);
		}
		return success();
	}
}
