package com.bluestone.app.integration.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jms.support.JmsUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.CustomerService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.goldMineOrders.model.GoldMinePlan;
import com.bluestone.app.goldMineOrders.model.GoldMinePlanPayment;
import com.bluestone.app.goldMineOrders.service.GoldMinePlanPaymentService;
import com.bluestone.app.integration.RestConnection;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderHistory;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.OrderItemHistory;
import com.bluestone.app.order.service.OrderItemService;
import com.bluestone.app.order.service.OrderService;
import com.bluestone.app.payment.service.MasterPaymentService;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.PaymentInstrument;
import com.bluestone.app.payment.spi.model.SubPayment;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.service.ShippingService;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

@Component
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CrmMessageConsumer implements SessionAwareMessageListener {
    
    @Value("${crm.service_url}")
    private String               crmServiceUrl;
    
    @Value("${crm.path}")
    private String               crmPath;
    
    @Autowired
    private MasterPaymentService masterPaymentService;
    
    @Autowired
    private OrderService         orderService;
    
    @Autowired
    private OrderItemService     orderItemService;
    
    @Autowired
    private CustomerService      customerService;
    
    @Resource
    private RestConnection       restConnection;
    
    @Autowired
    private CartService          cartService;
    
    @Autowired
    private CRMService           crmService;
    
    @Autowired
    private ShippingService      shippingService;
    
    @Autowired
    private GoldMinePlanPaymentService goldMinePlanPaymentService;
    
    private static final Logger  log = LoggerFactory.getLogger(CrmMessageConsumer.class);
    
    @Override
    public void onMessage(Message message, Session session) {
        try {
            log.debug("CrmMessageConsumer.onMessage(): Is Redelivered={} , JMSMessageID={}", message.getJMSRedelivered(), message.getJMSMessageID());
            Serializable object = ((ObjectMessage) message).getObject();
            handleCRMMessage(object);
            message.acknowledge();
            log.debug("CrmMessageConsumer.onMessage(): JMSMessageID={} has been successfully acknowledged.", message.getJMSMessageID());
        } catch (JMSException jmsAPIException) {
            log.error("JMS Error at CrmMessageConsumer.onMessage(): Code={} Cause={}", jmsAPIException.getErrorCode(), JmsUtils.buildExceptionMessage(jmsAPIException), jmsAPIException);
            try {
                session.recover();
            } catch (JMSException e) {
                if (log.isErrorEnabled()) {
                    log.error("Error while execution", e);
                }
            }
            throw JmsUtils.convertJmsAccessException(jmsAPIException);
        } catch (Throwable throwable) {
            log.error("Error at CrmMessageConsumer.onMessage() - ", throwable.toString(), Throwables.getRootCause(throwable));
            try {
                session.recover();
            } catch (JMSException e) {
                if (log.isErrorEnabled()) {
                    log.error("Error while execution", e);
                }
            }
            
            throw new RuntimeException("Error at CrmMessageConsumer.onMessage()", throwable);
        }
    }
    
    private void handleCRMMessage(Serializable object) throws Exception {
        CRMMessage crmMessage = (CRMMessage) object;
        switch (crmMessage.getCrmEvent()) {
            case CART_CREATED_EVENT:
                sendCartCreatedEvent(crmMessage);
                break;
            case CART_UPDATED_EVENT:
                sendCartUpdatedEvent(crmMessage);
                break;
            case CUSTOMER_REGISTERED:
                sendCustomerRegistrationToCRM(crmMessage);
                break;
            case HAVE_A_QUESTION:
                sendQuestionToCRM(crmMessage);
                break;
            case ORDER_CREATED:
                sendOrderCreationUpdateToCRM(crmMessage);
                break;
            case ORDER_ITEM_SHIPPING_STATUS_UPDATE:
                sendOrderItemShippingStatusUpdateToCRM(crmMessage);
                break;
            case ORDER_ITEM_STATUS_UPDATE:
                sendOrderItemStatusUpdateToCRM(crmMessage);
                break;
            case ORDER_PAYMENT_STATUS_UPDATE:
                sendOrderPaymentStatusUpdateToCRM(crmMessage);
                break;
            case ORDER_STATUS_UPDATE:
                sendOrderStatusUpdateToCRM(crmMessage);
                break;
            case PAYMENT_FAILURE:
                sendPaymentFailureToCRM(crmMessage);
                break;
            case GOLD_MINE:
                sendGoldMineUpdateToCRM(crmMessage);
                break;
            default:
                log.warn("Falling into the default break at CrmMessageConsumer.onMessage() NO Event associated.");
                break;
        }
    }
    
    public void sendQuestionToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendQuestionToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        String name = (String) messageContextMap.get("name");
        String email = (String) messageContextMap.get("email");
        String phone = (String) messageContextMap.get("phone");
        String question = (String) messageContextMap.get("question");
        String pageType = (String) messageContextMap.get("pageType");
        
        Map<String, Object> jsonParams = new HashMap<String, Object>();
        jsonParams.put("name", name);
        jsonParams.put("email", email);
        jsonParams.put("phone", phone);
        jsonParams.put("question", question);
        if (StringUtils.isNotBlank(pageType)) {
            jsonParams.put("pageType", pageType);
        }
        Gson gson = new Gson();
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("module", "flow");
        queryParams.put("action", "newQuestion");
        queryParams.put("access", "internal");
        queryParams.put("json", gson.toJson(jsonParams));
        try {
            sendHttpRequest(queryParams);
        } catch (Exception e) {
            String errorMessage = "Error posting question to CRM for name = " + name + " question = " + question
                    + " email = " + email + " phone = " + phone;
            log.error(errorMessage, e);
            throw e;
        }
    }
    
    
    private void sendGoldMineUpdateToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendGoldMineUpdateToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        try {
            Long paymentId = (Long) messageContextMap.get(MessageContext.GOLD_MINE_PAYMENT_ID);
            GoldMinePlanPayment goldMinePlanPayment = goldMinePlanPaymentService.getGoldMinePlanPaymentById(paymentId);
            log.debug("CRMService.sendGoldMineUpdateToCRM() for plan Id = {}", goldMinePlanPayment.getId());
            // get orderDate
            Date orderDate = goldMinePlanPayment.getUpdatedAt(); // using updatedAt date as gold mine installment payments would have createdDate as the gold mine plan creation date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
            final String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            
            log.debug("Order Date = {}", dateString);
            // get paymentMethod
            String paymentMethod = "Cash On Delivery (COD)";
            
            MasterPayment masterPayment = masterPaymentService.getMasterPayment(goldMinePlanPayment.getMasterPayment().getId());
            
            // TODO getMasterPayment from DB again
            List<SubPayment> subPaymentList = masterPayment.getSubPayments();
            SubPayment subPayment = null;
            if (subPaymentList != null && (!subPaymentList.isEmpty())) {
                subPayment = subPaymentList.get(0);
            }
            if (subPayment != null) {
                PaymentInstrument paymentInstrument = subPayment.getPaymentTransaction().getPaymentInstrument();
                String instrumentType = paymentInstrument.getInstrumentType();
                if (!Constants.CASHONDELIVERY.equalsIgnoreCase(instrumentType)) {
                    paymentMethod = instrumentType;
                }
            } else {
                log.debug("Subpayment is not present for the master payment {}", masterPayment.getId());
            }
            
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            setGoldMineOrderEventParams(goldMinePlanPayment, dateString, paymentMethod, masterPayment, jsonParams);
            
        } catch (Exception e) {
            log.error("Error sending OrderCreationUpdate info to CRM for order id ={} messageContext={}", messageContextMap, e);
            throw e;
        }

        
    }

    public void sendOrderCreationUpdateToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendOrderCreationUpdateToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        try {
            Long orderId = (Long) messageContextMap.get(MessageContext.ORDERID);
            Order order = orderService.getOrder(orderId);
            log.debug("CRMService.sendOrderCreationUpdateToCRM() for order Id = {}", order.getId());
            
            
            // get orderDate
            Date orderDate = order.getCreatedAt();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(orderDate);
            final String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            
            log.debug("Order Date = {}", dateString);
            // get paymentMethod
            String paymentMethod = "Cash On Delivery (COD)";
            
            MasterPayment masterPayment = masterPaymentService.getMasterPayment(order.getMasterPayment().getId());
            
            // TODO getMasterPayment from DB again
            List<SubPayment> subPaymentList = masterPayment.getSubPayments();
            SubPayment subPayment = null;
            if (subPaymentList != null && (!subPaymentList.isEmpty())) {
                subPayment = subPaymentList.get(0);
            }
            if (subPayment != null) {
                PaymentInstrument paymentInstrument = subPayment.getPaymentTransaction().getPaymentInstrument();
                String instrumentType = paymentInstrument.getInstrumentType();
                if (!Constants.CASHONDELIVERY.equalsIgnoreCase(instrumentType)) {
                    paymentMethod = instrumentType;
                }
            } else {
                log.debug("Subpayment is not present for the master payment {}", masterPayment.getId());
            }
            
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            setOrderEventParams(order, dateString, paymentMethod, masterPayment, jsonParams);
        } catch (Exception e) {
            log.error("Error sending OrderCreationUpdate info to CRM for order id ={} messageContext={}", messageContextMap, e);
            throw e;
        }
    }

    private void setOrderEventParams(Order order, final String dateString, String paymentMethod, MasterPayment masterPayment, Map<String, Object> jsonParams) throws Exception {
        jsonParams.put("orderDate", dateString);
        Cart cart = order.getCart();
        List<CartItem> goldMineCartItems = cart.getCartItems(PRODUCT_TYPE.GOLDMINE);
        if (goldMineCartItems.isEmpty()) {
            jsonParams.put("cartId", cart.getId());
            jsonParams.put("customerEmail", cart.getCustomer().getEmail());
            jsonParams.put("orderStatus", order.getLatestOrderStatus().getStatusName());
            jsonParams.put("orderPaymentStatus", masterPayment.getLatestSubPayment().getPaymentTransaction().getPaymentTransactionStatus().name());
            jsonParams.put("orderTotal", cart.getFinalPrice());
            jsonParams.put("PSOrderId", order.getId());
            jsonParams.put("paymentMethod", paymentMethod);
            jsonParams.put("productDetails", crmService.getOrderItemsForCRM(order));
            
            Gson gson = new Gson();
            Map<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("module", "salesOrder");
            queryParams.put("action", "createNewOrderForPSHook");
            queryParams.put("access", "internal");
            queryParams.put("json", gson.toJson(jsonParams));
            sendHttpRequest(queryParams);
        } else {
            log.info("Cart {} for order {} contains gold mine products . Not sending order creation details to crm", cart.getId(), order.getId());
        }
    }
    
    private void setGoldMineOrderEventParams(GoldMinePlanPayment goldMinePlanPayment, final String dateString, String paymentMethod, MasterPayment masterPayment, Map<String, Object> jsonParams) throws Exception {
        jsonParams.put("orderDate", dateString);
        Cart cart = goldMinePlanPayment.getCart();
        jsonParams.put("cartId", cart.getId());
        jsonParams.put("customerEmail", cart.getCustomer().getEmail());
        jsonParams.put("orderPaymentStatus", masterPayment.getLatestSubPayment().getPaymentTransaction().getPaymentTransactionStatus().name());
        jsonParams.put("orderTotal", cart.getFinalPrice());
        GoldMinePlan goldMinePlan = goldMinePlanPayment.getGoldMinePlan();
        jsonParams.put("schemeName", goldMinePlan.getSchemeName());
        jsonParams.put("PSGoldMinePlanId", goldMinePlan.getId());
        jsonParams.put("PSGoldMinePaymentId", goldMinePlanPayment.getId());
        jsonParams.put("paymentMethod", paymentMethod);
        
        Gson gson = new Gson();
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("module", "salesOrder");
        queryParams.put("action", "createNewGoldMineOrderForPSHook");
        queryParams.put("access", "internal");
        queryParams.put("json", gson.toJson(jsonParams));
        sendHttpRequest(queryParams);
    }
    
    public void sendCustomerRegistrationToCRM(CRMMessage crmMessage) {
        log.debug("CrmMessageConsumer.sendCustomerRegistrationToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        try {
            Long customerId = (Long) messageContextMap.get(MessageContext.CUSTOMERID);
            Customer customer = customerService.getCustomerById(customerId);
            // log.debug("CRMService.sendCustomerRegistrationToCRM():");
            
            Map<String, Object> jsonParams = new HashMap<String, Object>();
            jsonParams.put("customerName", customer.getUserName());
            jsonParams.put("customerEmail", customer.getEmail());
            jsonParams.put("customerPhone", customer.getCustomerPhone());
            jsonParams.put("PSCustomerId", customer.getId());
            Gson gson = new Gson();
            Map<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("module", "customer");
            queryParams.put("action", "createNewCustomerForPSHook");
            queryParams.put("access", "internal");
            queryParams.put("json", gson.toJson(jsonParams));
            sendHttpRequest(queryParams);
        } catch (Exception e) {
            log.error("Error posting customer registration info to CRM for messageContextMap = {}", messageContextMap, e);
        }
    }
    
    public void sendOrderItemStatusUpdateToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendOrderItemStatusUpdateToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        LinkedList<Long> linkedList = (LinkedList<Long>) messageContextMap.get(MessageContext.ORDERITEM_UPDATE_HISTORY);
        if (linkedList.isEmpty()) {
            log.warn("CrmMessageConsumer.sendOrderItemStatusUpdateToCRM() ****** No OrderItemHistory found so not taking any action ******");
        } else {
            for (int i = 0; i < linkedList.size(); i++) {
                Long orderItemHistoryId = linkedList.get(i);
                try {
                    OrderItemHistory orderItemHistory = orderItemService.getOrderItemHistory(orderItemHistoryId);
                    OrderItem orderItem = orderItemHistory.getOrderItem();
                    Order order = orderItem.getOrder();
                    Cart cart = order.getCart();
                    List<CartItem> goldMineCartItems = cart.getCartItems(PRODUCT_TYPE.GOLDMINE);
                    if(goldMineCartItems.isEmpty()){
                        log.debug("CRMService.sendOrderItemStatusUpdateToCRM(): orderItemID={}", orderItem.getId());
                        Map<String, Object> jsonParams = new HashMap<String, Object>();
                        jsonParams.put("PSOrderItemId", orderItem.getId());
                        jsonParams.put("PSOrderId", order.getId());
                        final String statusName = orderItemHistory.getOrderItemStatus().getStatusName();
                        jsonParams.put("newStatus", statusName);
                        // jsonParams.put("isCancellable",
                        // orderItem.isCancellable()); //@todo : Rahul Agrawal :
                        // tell dharam abt this.
                        Gson gson = new Gson();
                        Map<String, Object> queryParams = new HashMap<String, Object>();
                        queryParams.put("module", "salesOrder");
                        queryParams.put("action", "updateOrderItemStatusForPSHook");
                        queryParams.put("access", "internal");
                        queryParams.put("json", gson.toJson(jsonParams));
                        sendHttpRequest(queryParams);
                    } else {
                        log.info("Cart {} for order {} contains gold mine products . Not sending order item status update details to crm", cart.getId(), order.getId());
                    }
                } catch (Exception e) {
                    log.error("Error posting order item status update(orderItemHistoryId={}) to CRM for the messageContextMap={}", orderItemHistoryId, messageContextMap, e);
                    throw e;
                }
            }
        }
    }
    
    public void sendOrderStatusUpdateToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendOrderStatusUpdateToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        try {
            Long orderHistoryId = (Long) messageContextMap.get(MessageContext.ORDER_UPDATE_HISTORY);
            if (orderHistoryId == null) {
                log.debug("CrmMessageConsumer.sendOrderStatusUpdateToCRM(): This order was actually not updated, so not acting on this event.");;
            } else {
                OrderHistory orderHistory = orderService.getOrderHistory(orderHistoryId);
                Order order = orderHistory.getOrder();
                Cart cart = order.getCart();
                List<CartItem> goldMineCartItems = cart.getCartItems(PRODUCT_TYPE.GOLDMINE);
                if(goldMineCartItems.isEmpty()){
                    log.debug("CRMService.sendOrderStatusUpdateToCRM(): orderID={}", order.getId());
                    Map<String, Object> jsonParams = new HashMap<String, Object>();
                    jsonParams.put("PSOrderId", order.getId());
                    final String statusName = orderHistory.getOrderStatus().getStatusName();
                    jsonParams.put("newStatus", statusName);
                    Gson gson = new Gson();
                    Map<String, Object> queryParams = new HashMap<String, Object>();
                    queryParams.put("module", "salesOrder");
                    queryParams.put("action", "updateStatusForPSHook");
                    queryParams.put("access", "internal");
                    queryParams.put("json", gson.toJson(jsonParams));
                    sendHttpRequest(queryParams);
                } else {
                    log.info("Cart {} for order {} contains gold mine products . Not sending order status update details to crm", cart.getId(), order.getId());
                }
            }
        } catch (Exception e) {
            log.error("Error posting order status update to CRM for the messageContextMap={}", messageContextMap, e);
            throw e;
        }
    }
    
    private String sendHttpRequest(Map<String, Object> queryParams) throws Exception {
        return restConnection.executePostRequest(crmServiceUrl, crmPath, queryParams);
    }
    
    public void sendPaymentFailureToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendPaymentFailureToCRM()");
        sendCartModificationInfoToCRM(crmMessage, true);
    }
    
    public void sendCartCreatedEvent(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendCartCreatedEvent()");
        sendCartModificationInfoToCRM(crmMessage, false);
    }
    
    public void sendCartUpdatedEvent(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendCartUpdatedEvent()");
        sendCartModificationInfoToCRM(crmMessage, false);
    }
    
    private void sendCartModificationInfoToCRM(CRMMessage crmMessage, Boolean paymentFailure) throws Exception {
        log.debug("CRMService.sendCartModificationInfoToCRM (payment failure ? ={})", paymentFailure);
        // {"customerPhone":"0","customerName":"aaas","customerEmail":"a@b.com","PSCustomerId":17,"products":[],"cartId":14027,"cartUpdateTime":1347453307,"addresses":[{"firstname":null,"address1":null,"postcode":null,"city":null,"phone":null}]}
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        try {
            Long cartId = (Long) messageContextMap.get(MessageContext.CART_ID);
            Cart cart = cartService.getCart(cartId);
            if(cart != null) {
                Customer customer = cart.getCustomer();
                if (customer != null) {
                    Map<String, Object> jsonParams = new HashMap<String, Object>();
                    jsonParams.put("cartId", cartId);
                    jsonParams.put("customerPhone", customer.getCustomerPhone());
                    jsonParams.put("customerName", customer.getUserName());
                    jsonParams.put("customerEmail", customer.getEmail());
                    jsonParams.put("PSCustomerId", customer.getId());
                    jsonParams.put("paymentError", paymentFailure ? 1 : 0);
                    List<CartItem> goldMineCartItems = cart.getCartItems(PRODUCT_TYPE.GOLDMINE);
                    if (!goldMineCartItems.isEmpty()) {
                        log.info("Cart {} have a gold mine product . sending gold mine cart event to CRM", cart.getId());
                        jsonParams.put("cartType", "GOLDMINE");
                    } else {
                        jsonParams.put("cartType", "");
                    }
                    
                    Address shippingAddress = cart.getShippingAddress();
                    if (shippingAddress != null) {
                        Map<String, Object> address = new HashMap<String, Object>();
                        address.put("firstname", shippingAddress.getFullname());
                        address.put("address1", shippingAddress.getAddress());
                        address.put("postcode", shippingAddress.getPostCode());
                        address.put("city", shippingAddress.getCityName());
                        address.put("phone", shippingAddress.getContactNumber());
                        address.put("alias", "shipping");
                        
                        Map<String, Object>[] addresses = new HashMap[1];
                        addresses[0] = address;
                        jsonParams.put("addresses", addresses);
                    }
                    List products = new ArrayList<Object>();
                    for (CartItem cartItem : cart.getActiveCartItems()) {
                        Map<String, Object> product = new HashMap<String, Object>();
                        Product eachProduct = cartItem.getProduct();
                        product.put("price", eachProduct.getPrice());
                        product.put("category", eachProduct.getCategory());
                        product.put("name", eachProduct.getName());
                        products.add(product);
                    }
                    
                    jsonParams.put("products", products.toArray());
                    
                    Gson gson = new Gson();
                    Map<String, Object> queryParams = new HashMap<String, Object>();
                    queryParams.put("module", "flow");
                    if (paymentFailure) {
                        queryParams.put("action", "createNewPaymentError");
                    } else {
                        queryParams.put("action", "createNewDyingCartForPSHook");
                    }
                    queryParams.put("access", "internal");
                    queryParams.put("json", gson.toJson(jsonParams));
                    sendHttpRequest(queryParams);
                }
            }  else {
                log.warn("CRMMessageConsumer.sendCrtModificationInfo cart is null .. Need to investigate. messageContextMap {} ", messageContextMap);
            }
            
        } catch (Exception e) {
            log.error("Error posting CartModificationInfo to CRM for messageContextMap={}", messageContextMap, e);
            throw e;
        }
    }
    
    public void sendOrderItemShippingStatusUpdateToCRM(CRMMessage crmMessage) throws Exception {
        /*
         * log.debug("CrmMessageConsumer.sendOrderItemShippingStatusUpdateToCRM()"
         * ); Map<String, Serializable> messageContextMap =
         * crmMessage.getMessageContextMap(); try { Long shippingHistoryId =
         * (Long)
         * messageContextMap.get(MessageContext.SHIPPINGITEM_UPDATE_HISTORY);
         * ShippingItemHistory shippingItemHistory =
         * shippingService.getShippingItemHistory(shippingHistoryId); long
         * orderItemId =
         * shippingItemHistory.getShippingItem().getOrderItem().getId();
         * log.debug
         * ("CRMService.sendOrderItemShippingStatusUpdateToCRM(): orderItemId={}"
         * , orderItemId); Map<String, Object> jsonParams = new HashMap<String,
         * Object>(); jsonParams.put("PSOrderItemId", orderItemId);
         * jsonParams.put("PSOrderId",
         * shippingItemHistory.getShippingItem().getOrderItem
         * ().getOrder().getId()); final String statusName =
         * shippingItemHistory.getShippingStatus(); jsonParams.put("newStatus",
         * statusName); Gson gson = new Gson(); Map<String, Object> queryParams
         * = new HashMap<String, Object>(); queryParams.put("module",
         * "salesOrder"); queryParams.put("action",
         * "updateOrderItemShippingStatusForPSHook"); queryParams.put("access",
         * "internal"); queryParams.put("json", gson.toJson(jsonParams));
         * sendHttpRequest(queryParams); } catch (Exception e) { log.error(
         * "Error posting orderitem shipping status update to CRM for the messageContextMap = "
         * + messageContextMap, e); throw e; }
         */
    }
    
    public void sendOrderPaymentStatusUpdateToCRM(CRMMessage crmMessage) throws Exception {
        log.debug("CrmMessageConsumer.sendOrderPaymentStatusUpdateToCRM()");
        Map<String, Serializable> messageContextMap = crmMessage.getMessageContextMap();
        try {
            Long orderId = (Long) messageContextMap.get(MessageContext.ORDERID);
            Order order = orderService.getOrder(orderId);
            Cart cart = order.getCart();
            List<CartItem> goldMineCartItems = cart.getCartItems(PRODUCT_TYPE.GOLDMINE);
            if(goldMineCartItems.isEmpty()){
                log.debug("CRMService.sendOrderPaymentStatusUpdateToCRM(): orderID={}", order.getId());
                Map<String, Object> jsonParams = new HashMap<String, Object>();
                jsonParams.put("PSOrderId", order.getId());
                jsonParams.put("newStatus", messageContextMap.get(MessageContext.ORDER_PAYMENT_TRANSACTION_STATUS));
                Gson gson = new Gson();
                Map<String, Object> queryParams = new HashMap<String, Object>();
                queryParams.put("module", "salesOrder");
                queryParams.put("action", "updateOrderPaymentStatusForPSHook");
                queryParams.put("access", "internal");
                queryParams.put("json", gson.toJson(jsonParams));
                sendHttpRequest(queryParams);
            } else {
                log.info("Cart {} for order {} contains gold mine products . Not sending order payment status update to crm", cart.getId(), order.getId());
            }
        } catch (Exception e) {
            log.error("Error posting order payment status update to CRM for messageContextMap={}", messageContextMap, e);
            throw e;
        }
    }
    
}
