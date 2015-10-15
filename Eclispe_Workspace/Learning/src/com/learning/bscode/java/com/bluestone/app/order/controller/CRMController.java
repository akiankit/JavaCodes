package com.bluestone.app.order.controller;

import java.io.Serializable;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.admin.model.Carrier;
import com.bluestone.app.admin.service.CarrierService;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.ProductService;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan.GoldMinePlanStatus;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanService;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.integration.crm.CRMService;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderCancellationResponse;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.UniwareStatus.ORDER_ITEM_STATUS;
import com.bluestone.app.order.model.UniwareStatus.ORDER_STATUS;
import com.bluestone.app.order.service.CODConfirmationService;
import com.bluestone.app.order.service.OrderItemService;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.exception.PaymentException;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.model.State;
import com.bluestone.app.shipping.service.ShippingService;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.model.DiscountVoucher.VoucherType;
import com.bluestone.app.voucher.service.DiscountVoucherValidator;
import com.bluestone.app.voucher.service.VoucherService;
import com.google.common.base.Throwables;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Controller
@RequestMapping(value = "/crm")
public class CRMController {
    
    private static final Logger    log = LoggerFactory.getLogger(CRMController.class);
    
    @Autowired
    private OrderService           orderService;
    
    @Autowired
    private OrderItemService       orderItemService;
    
    @Autowired
    private CartService            cartService;
    
    @Autowired
    private CustomerService        customerService;
    
    @Autowired
    private PaymentTransactionService         paymentTransactionService;
    
    @Autowired
    private VoucherService         voucherService;

    @Autowired
    private DiscountVoucherValidator discountVoucherValidator;
    
    @Autowired
    private ShippingService        shippingService;
    
    @Autowired
    private CarrierService         carrierService;
    
    @Autowired
    private ProductService   productService;
    
    @Autowired
    private CRMService             crmService;
    
    @Autowired
    private CODConfirmationService codConfirmationService;
    
    @Autowired
    private GoldMinePlanService goldMinePlanService;
    
    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public @ResponseBody
    String sendNewQuestionToCRM(@RequestParam String name, @RequestParam String email, @RequestParam String phone, @RequestParam String question, ModelMap responseMap,HttpServletRequest request) {
        log.debug("CRMController.sendNewQuestionToCRM() entered with name = {} email = {} phone = {} question = {}", name, email, phone, question);
        String pageType = request.getParameter("pageType");
        Map<String, Serializable> map = new HashMap<String, Serializable>();
        map.put("name", name);
        map.put("email", email);
        map.put("question", question);
        map.put("phone", phone);
        map.put("pageType", pageType);
        crmService.send(CRM_EVENT.HAVE_A_QUESTION, map);
        responseMap.put("success", "Your question has been successfully submitted.");
        
        Gson gson = new Gson();
        String retVal = gson.toJson(responseMap);
        log.debug("CRMController.sendNewQuestionToCRM() exiting with retVal = {}", retVal);
        return retVal;
    }
    
    @RequestMapping(value = "/cancelGoldMine", method = RequestMethod.POST)
    public @ResponseBody String cancelGoldMinePlan(@RequestParam String cancellationDetails,ModelMap responseMap){
    	Gson gson = new Gson();
    	JsonObject jsonObject = getJsonObject(cancellationDetails);
    	Long planId = jsonObject.get("planId").getAsLong();
    	String userEmail = jsonObject.get("userEmail").getAsString();
    	log.debug("CRMController.cancelGoldMinePlan() userEmail:{}",userEmail);
    	if(planId!=null){
    		GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlan(planId);
    		validateDataForCancelGoldMine(goldMinePlan, responseMap, planId, userEmail);
    		Boolean hasError = (Boolean)responseMap.get(Constants.HAS_ERROR);
			if(hasError == null || hasError == false){
    			try{
    				goldMinePlanService.cancelGoldMinePlan(goldMinePlan);
    				log.info("Gold mine plan id "+planId+" cancelled successfully");
        			responseMap.put("message","Plan "+planId+" cancelled succcessfully.");
    			}
    			catch(Exception e){
    				log.error("Error during CRMController.cancelGoldMinePlan: Reason:{},userEmail:{}", e.toString(), e,userEmail);
    				setErrorResponse(responseMap, e.getMessage());
    			}
    		}
    	}else{
    		setErrorResponse(responseMap, "Plan id is not provided.");
    	}
    	
    	return gson.toJson(responseMap);
    }
    
    private void validateDataForCancelGoldMine(GoldMinePlan goldMinePlan,ModelMap responseMap,Long planId,String userEmail){
    	if(goldMinePlan == null){
        	log.warn("Gold mine plan could not be found for plan id:{},userEmail:{}",planId,userEmail);
        	setErrorResponse(responseMap, "Gold Mine Plan  id "+planId+" is not Valid");
        }
		GoldMinePlanStatus goldMinePlanStatus = goldMinePlan.getGoldMinePlanStatus();
		if(!goldMinePlanStatus.equals(GoldMinePlanStatus.RUNNING)){
        	if(goldMinePlanStatus.equals(GoldMinePlanStatus.CANCELLED)){
        		log.warn("Plan could not be cancelled for id:{} because it is already cancelled ,userEmail:{}",planId,userEmail);
        		setErrorResponse(responseMap, "Gold Mine Plan  id "+planId+" is already cancelled.");
        	}else{
        		log.warn("Plan could not be cancelled for id:{} because it is already matured ,userEmail:{}",planId,userEmail);
        		setErrorResponse(responseMap, "Gold Mine Plan  id "+planId+" is matured.");
        	}
        }
    }
    
    @RequestMapping(value = "/addGoldMinePlanPayment", method = RequestMethod.POST)
    public @ResponseBody String addGoldMinePaymentToPlan(@RequestParam String planPaymentDetails,ModelMap responseMap){
    	Gson gson = new Gson();
        JsonObject jsonObject = getJsonObject(planPaymentDetails);
        String userEmail = jsonObject.get("userEmail").getAsString();
        Long goldMinePlanId = jsonObject.get("planId").getAsLong();
        String instrumentType = jsonObject.get("instrumentType").getAsString();
        String comment = jsonObject.get("comment").getAsString();
        String date = jsonObject.get("paymentDate").getAsString();
        Date paymentDate = DateTimeUtil.parseDateString(date);
        
        log.info("Adding payment to plan id:{},instrument type:{},userEmail:{}",goldMinePlanId,instrumentType,userEmail);
        
        PaymentTransactionStatus paymentTransactionStatus = computePaymentTransactionStatus(instrumentType);
        try{
        	GoldMinePlan goldMinePlan = goldMinePlanService.getGoldMinePlan(goldMinePlanId);
        	validateDataForAddPaymentToPlan(goldMinePlan,goldMinePlanId,userEmail,responseMap);
        	Boolean hasError = (Boolean)responseMap.get(Constants.HAS_ERROR);
			if(hasError == null || hasError == false){
        		String receiptNumber = goldMinePlanService.addGoldMinePaymentToPlan(goldMinePlan, instrumentType,userEmail, comment, paymentTransactionStatus, paymentDate);
            	responseMap.put("receiptNumber",receiptNumber);
            	log.info("Payment:{} added to gold mine :{} plan added successfully",receiptNumber,goldMinePlanId);
        	}
        }
    	catch (Exception e) {
			log.error("Error during CRMController.addGoldMinePaymentToPlan: Reason:{},userEmail:{}", e.toString(), e,userEmail);
			setErrorResponse(responseMap, e.getMessage());
		}
    	return gson.toJson(responseMap);
    }
    
    private void validateDataForAddPaymentToPlan(GoldMinePlan goldMinePlan,Long goldMinePlanId,String userEmail,ModelMap responseMap){
		if(goldMinePlan == null){
	    	log.warn("Gold mine plan could not be found for plan id:{},userEmail:{}",goldMinePlanId,userEmail);
	    	setErrorResponse(responseMap, "Gold Mine Plan  id "+goldMinePlanId+" is not Valid");
	    }
	    GoldMinePlanStatus goldMinePlanStatus = goldMinePlan.getGoldMinePlanStatus();
		if(!goldMinePlanStatus.equals(GoldMinePlanStatus.RUNNING)){
	    	if(goldMinePlanStatus.equals(GoldMinePlanStatus.CANCELLED)){
	    		log.warn("Payment can not be added to gold mine plan :{} because it is cancelled state.,userEmail:{}",goldMinePlanId,userEmail);
	    		setErrorResponse(responseMap, "Gold Mine Plan  id "+goldMinePlanId+" is in cancelled state.");
	    	}else{
	    		log.warn("Payment can not be added to gold mine plan :{} because it is matured state.,userEmail:{}",goldMinePlanId,userEmail);
	    		setErrorResponse(responseMap, "Gold Mine Plan  id "+goldMinePlanId+" is in matured state.");
	    	}
	    }
	    GoldMinePlanPayment nextDuePlanPayment = goldMinePlan.getNextDuePlanPayment();
	    if(nextDuePlanPayment == null){
	    	log.warn("No due payment left for gold mine plan id:{},userEmail:{}",goldMinePlanId,userEmail);
	    	setErrorResponse(responseMap,"No due payment left for plan Id "+goldMinePlanId);
	    }
    }
    
    private  JsonObject getJsonObject(String objectDetails) {
		JsonReader jsonReader = new JsonReader(new StringReader(objectDetails));
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(jsonReader);
        JsonObject jsonObject = parse.getAsJsonObject();
        return jsonObject;
	} 
    
    @RequestMapping(value = "/orders", method = RequestMethod.POST)
    public @ResponseBody
    String createNewOrder(@RequestParam String orderDetails, ModelMap responseMap) {
        log.debug("CRMController.createNewOrder() with input=\n{}", orderDetails);
        // @todo P1 : Rahul Agrawal : try to move the json parsing into a
        // separate class
        // so that it's parsing logic can be unit tested independently
        Gson gson = new Gson();
        JsonObject jsonObject = getJsonObject(orderDetails);
        
        /*
         * {"customerId":"4400","billingAddrId":"7207","postcode":"560093",
         * "voucherCode"
         * :"","shippingAddrId":"7206","orderTotal":"10742","products"
         * :[{"designId"
         * :"751",customizationId:"","quantity":"1","size":"",price:""}]}
         */
        
        long customerId = jsonObject.get("customerId").getAsLong();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            log.warn("Could not get customer from id for CRM Order Creation, customerId = {}", customerId);
            setErrorResponse(responseMap, "No Customer for customerId = " + customerId);
            return gson.toJson(responseMap);
        }
        
        long billingAddrId = jsonObject.get("billingAddrId").getAsLong();
        Address billingAddress = shippingService.getAddress(billingAddrId);
        if (billingAddress == null) {
            log.warn("Could not get billing address from id for CRM Order Creation, addressId = {}", billingAddrId);
            setErrorResponse(responseMap, "No Billing Address for id = " + billingAddrId);
            return gson.toJson(responseMap);
        }
        long shippingAddrId = jsonObject.get("shippingAddrId").getAsLong();
        Address shippingAddress = shippingService.getAddress(shippingAddrId);
        
        if (shippingAddress == null) {
            log.warn("Could not get shipping address from id for CRM Order Creation, addressId = {}", shippingAddrId);
            setErrorResponse(responseMap, "No Shipping Address for id = " + shippingAddrId);
            return gson.toJson(responseMap);
        }
        BigDecimal orderTotal = BigDecimal.valueOf(jsonObject.get("orderTotal").getAsDouble());

        DiscountVoucher voucher = null;
        JsonElement voucherElem = jsonObject.get("voucherCode");
        if (!voucherElem.isJsonNull()) {
            String voucherCode = voucherElem.getAsString();
            if (StringUtils.isNotBlank(voucherCode)) {
                //long voucherId = voucherElem.getAsLong();
                //voucher = voucherService.getVoucher(voucherId);
                voucher = voucherService.getVoucherByName(voucherCode);
                String validateVoucherResponse = discountVoucherValidator.validateVoucherForCRMOrders(voucher, orderTotal);
                if (StringUtils.isNotBlank(validateVoucherResponse)) {
                    log.error("Validation for voucher={} from CRM during Order Creation failed: Reason={}",
                             voucherCode, validateVoucherResponse );
                    setErrorResponse(responseMap, validateVoucherResponse);
                    return gson.toJson(responseMap);
                }
            } else {
                log.info("CRMController.createNewOrder(): VoucherCode was empty. It was {}", voucherCode);
            }
        } else {
            log.info("CRMController.createNewOrder(): Voucher element from CRM is null");
        }
        
        String instrumentType = jsonObject.get("instrumentType").getAsString();
        log.info("CRMController.createNewOrder(): InstrumentType={}", instrumentType);
        if (instrumentType.equals(Constants.CASHONDELIVERY)) { // skip carrier
                                                              // availability
                                                              // check for other type of instruments
            String postcode = jsonObject.get("postcode").getAsString();
            Carrier applicableCarrierForCOD = carrierService.getApplicableCarrierForCOD(postcode, orderTotal.doubleValue());
            if (applicableCarrierForCOD == null) {
                log.info("Carrier not available for Pincode={} and Amount={} for CRM order creation", postcode, orderTotal);
                setErrorResponse(responseMap, "Carrier not available for this pincode and amount");
                return gson.toJson(responseMap);
            }
        }
        
        JsonElement productElem = jsonObject.get("products");
        
        if (productElem.isJsonArray()) {
            JsonArray productArray = productElem.getAsJsonArray();
            Iterator<JsonElement> iterator = productArray.iterator();
            
            Cart cart = cartService.createCart(Constants.CRM_VISITOR_ID, customer);
            cart.setBillingAddress(billingAddress);
            cart.setShippingAddress(shippingAddress);
            cart = cartService.updateCart(cart);
            try {
                while (iterator.hasNext()) {
                    JsonElement eachProduct = iterator.next();
                    if (eachProduct.isJsonObject()) {
                        JsonObject productDetails = eachProduct.getAsJsonObject();
                        int quantity = productDetails.get("quantity").getAsInt();
                        long productId = productDetails.get("productId").getAsLong();
                        
                        JsonElement jsonElement = productDetails.get("size");
                        String size = null;
                        if (!jsonElement.isJsonNull()) {
                            size = jsonElement.getAsString();
                        }
                        cartService.addItemToCart(cart, productId, size, quantity, null);
                    }
                }
                if (voucher != null) {
                    final String validateVoucherResponse = discountVoucherValidator.execute(voucher, cart);
                    if (StringUtils.isNotBlank(validateVoucherResponse)) {
                        // take a u turn
                        log.error("Validation for voucher={} from CRM during Order Creation failed: Reason={}",
                                  voucher.getName(), validateVoucherResponse);
                        setErrorResponse(responseMap, validateVoucherResponse);
                        return gson.toJson(responseMap);
                    }
                    discountVoucherValidator.applyDiscountVoucher(cart, voucher);
                    cart = cartService.updateCart(cart);
                }

                PaymentTransaction paymentTransaction = paymentTransactionService.createPaymentTransaction(cart, null, instrumentType, null, null, "CRM_flow");

                PaymentTransactionStatus paymentTransactionStatus = computePaymentTransactionStatus(instrumentType);
                log.info("CRMController.createNewOrder(): Setting the payment transaction status to {}", paymentTransactionStatus);
                paymentTransaction.setPaymentTransactionStatus(paymentTransactionStatus);
                Order order = orderService.createOrder(paymentTransaction);
                responseMap.put("orderId", order.getId());
            } catch (Exception e) {
                log.error("Error while creating order request made from CRM for request - {}", jsonObject, e);
                setErrorResponse(responseMap, e.getMessage());
            }
        }
        return gson.toJson(responseMap);
    }

    private PaymentTransactionStatus computePaymentTransactionStatus(String instrumentType) {
        log.debug("CRMController.computePaymentTransactionStatus()");
        PaymentTransactionStatus paymentTransactionStatus = PaymentTransactionStatus.COD_CONFIRMED;
        if (Constants.POSTDATEDCHQ.equals(instrumentType)) {
            paymentTransactionStatus = PaymentTransactionStatus.PDC_ACCEPTED;
        } else if (Constants.REPLACEMENT_ORDER.equalsIgnoreCase(instrumentType)) {
            paymentTransactionStatus = PaymentTransactionStatus.REPLACEMENT_ORDER;
        } else if (Constants.NET_BANKING_TRANSFER.equalsIgnoreCase(instrumentType)) {
            paymentTransactionStatus = PaymentTransactionStatus.NET_BANKING_TRANSFER;
        } else if (Constants.CHEQUE_PAID.equalsIgnoreCase(instrumentType)) {
            paymentTransactionStatus = PaymentTransactionStatus.CHEQUE_PAID;
        } else if (Constants.PAYMENT_ACCEPTED.equalsIgnoreCase(instrumentType)) {
            paymentTransactionStatus = PaymentTransactionStatus.PAYMENT_ACCEPTED;
        }
        return paymentTransactionStatus;
    }

    private void setErrorResponse(ModelMap responseMap, String errorMessage) {
        responseMap.put(Constants.HAS_ERROR, true);
        responseMap.put(Constants.MESSAGE, errorMessage);
    }
    
    // @RequestMapping(value = "/order/{id}/address", method =
    // RequestMethod.POST)
    @Deprecated
    public void updateOrderAddress(@PathVariable Long id, @RequestParam Long addressId, @RequestParam String userEmail, ModelMap responseMap) {
        try {
            log.debug("CRMController.updateOrderAddress(): CRM requested for New Address id={} for order id {} from user {}", addressId, id, userEmail);
            Order order = orderService.getOrder(id);
            if (order == null) {
                setErrorResponse(responseMap, "Order with id " + id + " not found.");
            } else {
                Address address = shippingService.getAddress(addressId);
                if (address == null) {
                    setErrorResponse(responseMap, "Address with id " + addressId + " not found");
                } else {
                    for (OrderItem orderItem : order.getOrderItems()) {
                        orderItemService.updateOrderItemAddress(orderItem, address); // @todo
                                                                                     // P1
                                                                                     // :
                                                                                     // Rahul
                                                                                     // Agrawal
                                                                                     // :
                                                                                     // what
                                                                                     // if
                                                                                     // some
                                                                                     // items
                                                                                     // are
                                                                                     // already
                                                                                     // shipped
                                                                                     // ?
                                                                                     // Handle
                                                                                     // that
                                                                                     // case
                    }
                    responseMap.put("response", "Updated with New address for all items of order id  = " + id);
                }
            }
        } catch (Exception e) {
            log.error("Error while updating the address for the customer with email = {} order id={}, address id={}", userEmail, id, addressId, e);
            setErrorResponse(responseMap, e.getMessage());
        }
    }
    

    @RequestMapping(value = "/customerFromOrder/{orderId}", method = RequestMethod.GET)
    public @ResponseBody
    String getCustomerFromOrder(@PathVariable Long orderId, ModelMap responseMap) {
        log.info("CRMController.getCustomerFromOrder() for orderId={}", orderId);
        
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            log.error("Could not get order from id in getCustomerFromOrder for id = {}", orderId);
            setErrorResponse(responseMap, "Order with id " + orderId + " not found.");
        } else {
            Cart cart = order.getCart();
            if(cart != null) {
                Customer customer = cart.getCustomer();
                if(customer != null) {
                    responseMap.put("name", customer.getUserName());
                    responseMap.put("phone", customer.getCustomerPhone());
                    responseMap.put("email", customer.getEmail());
                    responseMap.put("PSCustomerId", customer.getId());
                } else {
                    setErrorResponse(responseMap, "Customer for order with id " + orderId + " not found.");
                }
            } else {
                setErrorResponse(responseMap, "Cart for order with id " + orderId + " not found.");
            }
        }
        return new Gson().toJson(responseMap);
    }
    
    // @RequestMapping(value = "/orderitem/{id}/address", method =
    // RequestMethod.POST)
    public void updateOrderItemAddress(@PathVariable Long id, @RequestParam Long addressId, @RequestParam String userEmail, ModelMap responseMap) {
        try {
            log.debug("CRMController.updateOrderItemAddress(): CRM requested for New Address id={} for order item id {} from user {}", addressId, id, userEmail);
            OrderItem orderItem = orderItemService.getOrderItem(id);
            if (orderItem == null) {
                setErrorResponse(responseMap, "OrderItem with id " + id + " not found");
            } else {
                Address address = shippingService.getAddress(addressId);
                if (address == null) {
                    setErrorResponse(responseMap, "Address with id " + addressId + " not found.");
                } else {
                    orderItemService.updateOrderItemAddress(orderItem, address);
                    responseMap.put("response", "Updated with New address for orderItemId = " + id);
                }
            }
        } catch (Exception e) {
            log.error("Error while updating the address for the customer with email = {} orderItem id={}, address id={}", userEmail, id, addressId, e);
            setErrorResponse(responseMap, e.getMessage());
        }
    }
    
    @RequestMapping(value = "/order/{id}/status", method = RequestMethod.POST)
    public @ResponseBody
    String cancelOrder(@PathVariable Long id, @RequestParam String newStatus, @RequestParam String userEmail, ModelMap responseMap) {
        OrderCancellationResponse orderCancellationResponse = null;
        try {
            log.info("CRMController.cancelOrder(): CRM requested for {} for order id {} from user {}", newStatus, id, userEmail);
            Order order = orderService.getOrder(id);
            if (order == null) {
                String errorMessage = "Order with id " + id + " does not exist in the system.";
                log.error("CRM Order Cancellation:{} ", errorMessage);
                orderCancellationResponse = new OrderCancellationResponse(false, errorMessage);
            } else {
                if (ORDER_STATUS.CANCELLED.getValue().equalsIgnoreCase(newStatus)) {
                    String latestOrderStatus = order.getStatus().getStatusName();
                    if (ORDER_STATUS.CANCELLED.equals(latestOrderStatus)
                        || ORDER_STATUS.CANCELLATION_REQUESTED.equals(latestOrderStatus)) {
                        log.info("OrderId={} is already in {} state. Hence not doing anything.", order.getId(), latestOrderStatus);
                        String errorMessage = "Order is already in " + latestOrderStatus + " state.";
                        orderCancellationResponse = new OrderCancellationResponse(false, errorMessage);
                    } else {
                        // fetch the latest affair from uniware.
                        orderService.syncWithUniwareStatus(Long.toString(id));
                        orderCancellationResponse = orderService.cancelOrder(id, userEmail);
                        if (!orderCancellationResponse.isSuccess()) {
                            final List<OrderItem> orderItems = orderItemService.getOrderItems(id);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("Error Message", orderCancellationResponse.getMessage());
                            for (OrderItem eachOrderItem : orderItems) {
                                final long eachOrderItemId = eachOrderItem.getId();
                                final String statusName = eachOrderItem.getStatus().getStatusName();
                                log.debug("CRMController.cancelOrder(): Cancellation failed - OrderItemId={} Status={}", eachOrderItemId, statusName);
                                map.put(eachOrderItemId + "", statusName);
                            }
                            Gson gson = new Gson();
                            String json = gson.toJson(map);
                            orderCancellationResponse.setMessage(json);
                        }
                    }
                } else {
                    String errorMessage = "Except for cancellation nothing else is supported.";
                    orderCancellationResponse = new OrderCancellationResponse(false, errorMessage);
                    log.warn("CRMController.updateOrderStatus(): Except for cancellation no other order state update is supported via CRM");
                }
            }
            responseMap.put("response", orderCancellationResponse.getMessage());
        } catch (Exception exception) {
            log.error("Error while updating the orderId={} for user={} with the new status={}", id, userEmail, newStatus, exception);
            String errorMessage = "Error in updating the status of the order to " + newStatus + " Reason ="
                    + exception.getMessage();
            setErrorResponse(responseMap, errorMessage);
        }
        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }
    
    /*
     * This call will never come from crm to bluestone app - it will be from BS
     * app to crm
     * @RequestMapping(value = "/orderitem/{id}/shipping_status", method =
     * RequestMethod.POST) public void
     * updateOrderItemShippingStatus(@PathVariable Long id, @RequestParam String
     * status, @RequestParam String userEmail, ModelMap responseMap) { try {
     * log.debug(
     * "CRMController.updateOrderItemShippingStatus(): CRM requested for New shipping Status={} for orderitem id {} from user {}"
     * , status, id, userEmail); OrderItem orderItem =
     * orderService.getOrderItem(id); if (orderItem == null) { throw new
     * ResourceNotFoundException("OrderItem with id " + id + " not found"); }
     * shippingService.updateShippingStatus(id, status);
     * responseMap.put("response", "New shipping status = " + status +
     * " for orderItemId = " + id); } catch (Exception e) {
     * setErrorResponse(responseMap, e.getMessage()); } }
     */
    @RequestMapping(value = "/orderitem/{id}/status", method = RequestMethod.POST)
    public @ResponseBody
    String updateOrderItemStatus(@PathVariable Long id, @RequestParam String status, @RequestParam String userEmail, ModelMap responseMap) {
        try {
            log.debug("CRMController.updateOrderItemStatus(): CRM requested for New Status={} for orderitem id {} from user {}", status, id, userEmail);
            OrderItem orderItem = orderItemService.getOrderItem(id);
            if (orderItem == null) {
                String errorMessage = "OrderItem with id " + id + " does not exist in the system.";
                setErrorResponse(responseMap, errorMessage);
            } else {
                boolean isOrderItemCancellable = orderItem.isCancellable();
                if (ORDER_ITEM_STATUS.CANCELLED.getValue().equalsIgnoreCase(status)) {
                    if (isOrderItemCancellable) {
                        // orderService.cancel order item
                    } else {
                        // order item cannot be cancelled.
                    }
                } else {
                    // any other status other than cancelling
                    orderItemService.updateOrderItemStatus(orderItem, status, isOrderItemCancellable, userEmail);
                }
                responseMap.put("response", "New status = " + status + " for orderItemId = " + id);
            }
        } catch (Exception e) {
            log.error("Failed to update the status to {} for the orderItemId={} for user email={}", status, id, userEmail, e);
            setErrorResponse(responseMap, e.toString());
        }
        Gson gson = new Gson();
        return gson.toJson(responseMap);
    }
    
    @RequestMapping(value = "/orders/{id}/paymentStatus", method = RequestMethod.POST)
    public @ResponseBody
    String updateOrderPaymentStatus(@PathVariable Long id, @RequestParam String paymentStatus, @RequestParam String userEmail, ModelMap modelMap) {
        log.debug("CRMController.updateOrderPaymentStatus: CRM requested for New Status={} for order id {} from user {}", paymentStatus, id, userEmail);
        Order order = orderService.getOrder(id);
        if (order == null) {
            setErrorResponse(modelMap, ("Order with id " + id + " not found in the system."));
            log.warn("CRMController.updateOrderPaymentStatus: In correct order id {}", id);
        } else {
            boolean isSuccess=false;
            try {
                isSuccess = codConfirmationService.execute(order, paymentStatus, userEmail);
            } catch (Exception exception) {
                log.error("Error: CRMController.updateOrderPaymentStatus(): Reason={}", exception.getLocalizedMessage(), exception);
            }
            if (isSuccess) {
                log.debug("CRMController.updateOrderPaymentStatus() is Successful for OrderId={}", order.getId());
            } else {
                log.error("Cannot process the request to update the order payment status={} for orderId {}", paymentStatus, id);
                setErrorResponse(modelMap, "Could not update the order payment status for orderId=" + id);
            }
        }
        Gson gson = new Gson();
        return gson.toJson(modelMap);
    }
    
    // @todo: Rahul Agrawal : improve upon the regex for email - see this link -
    // http://www.w3.org/TR/html5/states-of-the-type-attribute.html#valid-e-mail-address
    // [a-zA-Z0-9.!#$%&'*+-/=?\^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*
    // we also have 1 reg ex defined in the Constants.java in our code.
    @RequestMapping(value = "/customers/{email:.+}", method = RequestMethod.GET)
    public @ResponseBody
    Long getCustomerByEmail(@PathVariable String email) {
        log.debug("CRMController.getCustomerByEmail() for emailId={}", email);
        Customer customer = customerService.getCustomerByEmail(email);
        if (customer != null) {
            return customer.getId();
        } else {
            log.info("CRMController.getCustomerByEmail(): Failed to get the customer with emailId={}", email);
            return 0l;
        }
    }

    @RequestMapping(value = "/discountvoucher", method = RequestMethod.POST)
    public
    @ResponseBody
    String createDiscountVoucher(@RequestParam String percentage) {
        log.debug("CRMController.createDiscountVoucher() with percentage discount={}%", percentage);
        boolean  voucherCreationIsSuccess = false;
        DiscountVoucher discountVoucher = new DiscountVoucher();
        discountVoucher.setMaxAllowed(1);
        discountVoucher.setVoucherType(VoucherType.PERCENTAGE);
        discountVoucher.setDescription("Created through CRM");
        discountVoucher.setValue(new BigDecimal(percentage));
        discountVoucher.setIsActive(true);
        discountVoucher.setMinimumAmount(new BigDecimal(0));
        Calendar calendar = Calendar.getInstance();
        discountVoucher.setValidFrom(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        discountVoucher.setValidTo(calendar.getTime());

        discountVoucher.setName(UUID.randomUUID().toString());
        try {
            voucherService.createVoucher(discountVoucher);
            String encryptVoucherName = voucherService.encryptVoucherName(discountVoucher.getId() + "");
            discountVoucher.setName("CXX" + encryptVoucherName);
            discountVoucher = voucherService.updateVoucher(discountVoucher);

            log.info("CRMController.createDiscountVoucher() resulted in {}", discountVoucher.toString());
            voucherCreationIsSuccess=true;
        } catch (Exception exception) {
            log.error("Error: CRMController.createDiscountVoucher():Reason={} ", exception.getLocalizedMessage(), Throwables.getRootCause(exception));
        }
        if(voucherCreationIsSuccess){
            return discountVoucher.getName();
        }else {
            return ""; // this is being handled at crm level
        }

    }
    
    @RequestMapping(value = "/getOrderItems/{orderId}", method = RequestMethod.GET)
    public @ResponseBody
    String getOrderItems(@PathVariable Long orderId, ModelMap responseMap) {
        log.info("CRMController.getOrderItems() for OrderId={}", orderId);
        
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            log.error("Could not get order from id in getOrderItems for id = {}", orderId);
            setErrorResponse(responseMap, "Order with id " + orderId + " not found.");
        } else {
            try {
                orderService.syncWithUniwareStatus(Long.toString(orderId)); // this is like an on demand pull. So we shd recompute the order status status.
                orderService.reComputeOrderStatus(order.getId(), null);
                //@todo : Rahul Agrawal : experimental fix
                order = orderService.getOrder(orderId);
                log.info("CRMController.getOrderItems():After sync from Uniware: OrderId=[{}] Latest Status=[{}]", orderId, order.getLatestOrderStatus().getStatusName());
            } catch (Exception exception) {
                setErrorResponse(responseMap, "Order with id " + orderId + " Error Cause=" + exception.getLocalizedMessage());
                log.error("Error: CRMController.getOrderItems(): Reason={} ", exception.getLocalizedMessage(),
                          Throwables.getRootCause(exception));
            }
            List<Map<String, Object>> orderItemsForCRM = crmService.getOrderItemsForCRM(order);
            responseMap.put("response", orderItemsForCRM);
        }
        Gson gson = new Gson();
        String retVal = gson.toJson(responseMap);
        log.debug("Response from getOrderItems = {}", retVal);
        return retVal;
    }
    
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    public @ResponseBody
    String getProduct(@PathVariable Long productId, ModelMap responseMap) {
        log.debug("CRMController.getProduct() for ProductCode={}", productId);
        Product product = productService.getProduct(productId);
        if (product == null) {
            log.error("CRM: No Product found for productId:" + productId);
            setErrorResponse(responseMap, "No Product found for ProductCode:" + productId);
        } else {
            responseMap.put("productId", product.getId());
            responseMap.put("price", product.getPrice());
            responseMap.put("category", product.getCategory());
            responseMap.put("name", product.getName());
        }
        Gson gson = new Gson();
        String retVal = gson.toJson(responseMap);
        log.debug("Response from getProduct() = {}" + retVal);
        return retVal;
    }
    
    @RequestMapping(value = "/price/{productId}/{size}", method = RequestMethod.GET)
    public @ResponseBody
    String getProductPriceFromSize(@PathVariable Long productId, @PathVariable String size, ModelMap responseMap) {
        log.debug("CRMController.getProductPriceFromSize() for productId = {} size = {}", productId, size);
        Product product = productService.getProduct(productId);
        if (product == null) {
            log.error("CRM: No Product found for id = {}" + productId);
            setErrorResponse(responseMap, "No Product found for id = :" + productId);
        } else {
            responseMap.put("price", product.getPrice(size));
        }
        Gson gson = new Gson();
        String retVal = gson.toJson(responseMap);
        log.debug("Response from getProductPriceFromSize() = {}" + retVal);
        return retVal;
    }
    
    @RequestMapping(value = "/address", method = RequestMethod.POST)
    public @ResponseBody
    Long createAddress(@RequestParam String addressDetails, ModelMap responseMap) {
        Long addressId = 0l;
        log.info("CRMController.createAddress() with input={}", addressDetails);
        JsonObject jsonObject = getJsonObject(addressDetails);
        
        Address address = new Address();
        long customerId = jsonObject.get("customerId").getAsLong();
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            log.error("Could not get customer from id = {}", customerId);
        } else {
            address.setCustomerId(customer);
            
            String stateName = jsonObject.get("stateName").getAsString();
            State state = shippingService.getStateFromName(stateName);
            if (state == null) {
                log.error("Could not get state from name = {}", stateName);
            } else {
                address.setState(state);
                
                String city = jsonObject.get("city").getAsString();
                address.setCityName(city);
                
                String postCode = jsonObject.get("pincode").getAsString();
                address.setPostCode(postCode);
                
                String phone = jsonObject.get("phone").getAsString();
                address.setContactNumber(phone);
                
                String customerName = jsonObject.get("customerName").getAsString();
                address.setFullname(customerName);
                
                String fullAddress = jsonObject.get("fullAddress").getAsString();
                address.setAddress(fullAddress);
                
                String alias = jsonObject.get("alias").getAsString();
                address.setAlias(alias);
                
                shippingService.createAddress(address);
                addressId = address.getId();
            }
        }
        log.debug("Returning address id = {}",address.getId());
        return addressId;
    }
}
