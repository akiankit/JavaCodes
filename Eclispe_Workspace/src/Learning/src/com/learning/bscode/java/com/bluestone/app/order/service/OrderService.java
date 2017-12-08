package com.bluestone.app.order.service;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.UserService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.User;
import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.design.service.ProductService;
import com.bluestone.app.design.service.ReadyToShipService;
import com.bluestone.app.design.solitaire.Solitaire;
import com.bluestone.app.order.dao.OrderDao;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderCancellationResponse;
import com.bluestone.app.order.model.OrderHistory;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.OrderItemHistory;
import com.bluestone.app.order.model.OrderItemStatus;
import com.bluestone.app.order.model.OrderStatus;
import com.bluestone.app.order.model.UniwareStatus.ORDER_ITEM_STATUS;
import com.bluestone.app.order.model.UniwareStatus.ORDER_STATUS;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;
import com.bluestone.app.shipping.service.HolidayService;
import com.bluestone.app.uniware.UniwareOrderService;
import com.bluestone.app.voucher.model.DiscountVoucher;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class OrderService extends BaseOrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private UserService userService;

    @Autowired
    private UniwareOrderService uniwareOrderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UniwareSynchroniser uniwareSynchroniser;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReadyToShipService readyToShipService;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @RaiseEvent(eventType = EventType.ORDER_CREATED)
    public Order createOrder(PaymentTransaction paymentTransaction) {
        log.info("OrderService.createOrder(): Begin for {}", paymentTransaction.toStringBrief());
        MasterPayment masterPayment = getMasterPayment(paymentTransaction);
        String paymentInstrumentType = paymentTransaction.getPaymentInstrument().getInstrumentType();
        final PaymentTransactionStatus paymentTransactionStatus = paymentTransaction.getPaymentTransactionStatus();
        log.debug("OrderService.createOrder() for payment status={} , payment instrument type={} ", paymentTransactionStatus.name(), paymentInstrumentType);

        Cart cartTobeUsed = getCartToBeUsed(paymentTransaction, masterPayment);

        preOrderProcessing(masterPayment, cartTobeUsed);
        Order order = new Order();
        order.setCart(cartTobeUsed);
        order.setDiscountPrice(cartTobeUsed.getDiscountPrice());
        order.setDiscountVoucher(cartTobeUsed.getDiscountVoucher());
        order.setTotalOrderAmount(cartTobeUsed.getFinalPrice());
        order.setCustomer(cartTobeUsed.getCustomer());

        order.setMasterPayment(masterPayment);

        List<CartItem> activeCartItems = cartTobeUsed.getActiveCartItems();
        setOrderDeliveryDate(order, activeCartItems);

        OrderStatus orderStatus = computeOrderStatus(order.getId(), paymentTransactionStatus);
        order.setLatestOrderStatus(orderStatus);

        orderDao.create(order);
        
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setOrderStatus(orderStatus);
        
        
        order.setCode(CryptographyUtil.encrypt(order.getId() + "", Constants.ORDER_ID_PADDING));

        for (CartItem cartItem : activeCartItems) {
            for (int i = 0; i < cartItem.getQuantity(); i++) {
                OrderItem eachOrderItem = new OrderItem();
                eachOrderItem.setShippingAddress(cartItem.getCart().getShippingAddress());
                eachOrderItem.setCartItem(cartItem);
                Product product = cartItem.getProduct();
                eachOrderItem.setPrice(product.getPrice(cartItem.getSize()));
                // TODO check if item is in stock and accordingly calculate date
                eachOrderItem.setOrder(order);
                order.addOrderItem(eachOrderItem);

                // Marking solitaire product as inactive
                if (eachOrderItem.getProductType().equals(Product.PRODUCT_TYPE.SOLITAIRE)) {
                    log.info("Marking solitaire product {} as inactive", product.getId());
                    Solitaire solitaire = (Solitaire) product;
                    solitaire.setIsActive(false);
                    solitaire.setSold(true);
                    productService.updateProduct(solitaire);
                }
            }
        }

        // OrderItemStatus orderItemStatusFromDb =
        // orderDao.getOrderItemStatus(orderStatus);
        OrderItemStatus orderItemStatusFromDb = orderDao.getOrderItemStatus(ORDER_ITEM_STATUS.CREATED.getValue());
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderItemHistory orderItemHistory = new OrderItemHistory();
            orderItemHistory.setOrderItem(orderItem);
            orderItemHistory.setOrderItemStatus(orderItemStatusFromDb);
        }

        // Create Vouchers to be shipped to the customer for this order
        // vikas: commenting out creation of vouchers on order creation as we are shipping sending vouchers anymore
        /*Set<DiscountVoucher> customerVouchers = voucherService.createAndGetVouchersForOrder(order);
        order.setVouchersForCustomer(customerVouchers);*/

        order = orderDao.update(order);

        MessageContext messageContext = getMessageContext();
        messageContext.put(MessageContext.ORDERID, order.getId());
        messageContext.put(MessageContext.ORDER_PAYMENT_TRANSACTION_STATUS, paymentTransactionStatus.name());
        log.info("OrderService.createOrder(): *Successfully* created OrderId=[{}] for PaymentTransactionId={}", order.getId(), paymentTransaction.getId());

        return order;
    }

    private void setOrderDeliveryDate(Order order, List<CartItem> cartItems) {
        Date deliveryDate = null;
        boolean areALLReadyToShipItems = true;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            boolean readyToShipItem = productService.isReadyToShipItem(product);
            log.info("OrderService.setOrderDeliveryDate() : Is CustomizationId=[{}] with SkuCodeWithSize=[{}] for CartItem=[{}] Sku=[{}] is readyToShipItem ? [{}]",
                     product.getId(), cartItem.getSkuCodeWithSize(), cartItem.getId(), product.getSkuCode(), readyToShipItem);
            areALLReadyToShipItems = areALLReadyToShipItems & readyToShipItem;
        }

        Date dDate = null;
        if (areALLReadyToShipItems) {
            dDate = readyToShipService.getDeliveryDate();
        } else {
            dDate = holidayService.getStandardDeliveryDate();
        }
        order.setExpectedDeliveryDate(dDate);
    }

    private OrderStatus computeOrderStatus(long id, PaymentTransactionStatus paymentTransactionStatus) {
        String orderStatus;
        switch (paymentTransactionStatus) {
            case COD_CONFIRMED:
            case SUCCESS:
            case PDC_ACCEPTED:
            case CHEQUE_PAID:
            case NET_BANKING_TRANSFER:
            case PAYMENT_ACCEPTED:
            case REPLACEMENT_ORDER:
                orderStatus = ORDER_STATUS.PROCESSING.getValue();
                break;
            default:
                orderStatus = ORDER_STATUS.CREATED.getValue();
                break;
        }
        log.info("OrderService.computeOrderStatus() for OrderId=[{}] for paymentTransactionStatus=[{}] result = [{}]", id, paymentTransactionStatus.name(), orderStatus);
        return orderDao.getOrderStatus(orderStatus);
    }


    @RaiseEvent(eventType = EventType.ORDER_STATUS_UPDATED)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Order reComputeOrderStatus(long orderId, String userEmail) {
        Order order = getOrder(orderId);
        log.info("OrderService.reComputeOrderStatus() for OrderId=[{}]", orderId);
        MessageContext messageContext = getMessageContext();
        messageContext.put(MessageContext.ORDERID, order.getId());
        int dispatchedCount = 0;
        int deliveredCount = 0;
        int cancelledCount = 0;
        final String existingOrderStatusName = order.getStatus().getStatusName();

        log.info("OrderService.reComputeOrderStatus(): OrderId=[{}] has Status in Database=[{}]", orderId, existingOrderStatusName);

        String recomputedOrderStatus = existingOrderStatusName;

        List<OrderItem> orderItemList = order.getOrderItems();
        final int totalItemCount = orderItemList.size();

        for (int i = 0; i < totalItemCount; i++) {
            OrderItem orderItem = orderItemList.get(i);
            OrderItemStatus eachOrderItemStatus = orderItem.getStatus();
            String itemStatusName = eachOrderItemStatus.getStatusName();
            log.info("OrderService.reComputeOrderStatus():OrderId=[{}] OrderItemId=[{}] has status=[{}]", orderId, orderItem.getId(), itemStatusName);

            if (ORDER_ITEM_STATUS.DELIVERED.getValue().equals(itemStatusName)) {
                deliveredCount = deliveredCount + 1;
            }
            if (ORDER_ITEM_STATUS.DISPATCHED.getValue().equals(itemStatusName)) {
                dispatchedCount = dispatchedCount + 1;
            }
            // Note: we have a separate call for cancel order, and there we simply update the oder status to cancel.
            //This check has been kept to ensure that if we for any reason land up cancelling the order by going into uniware
            // we donot miss the status update of order.
            if (ORDER_ITEM_STATUS.CANCELLED.getValue().equals(itemStatusName)) {
                cancelledCount = cancelledCount + 1;
            }
        }
        if (cancelledCount == totalItemCount) {
            recomputedOrderStatus = ORDER_STATUS.CANCELLED.getValue();
        }
        if (deliveredCount == totalItemCount) {
            recomputedOrderStatus = ORDER_STATUS.DELIVERED.getValue();
        }
        if (dispatchedCount == totalItemCount) {
            recomputedOrderStatus = ORDER_STATUS.DISPATCHED.getValue();
        }
        if (recomputedOrderStatus.equals(existingOrderStatusName)) {
            log.info("OrderService.reComputeOrderStatus(): No need to change the status of OrderId=[{}] with Status=[{}]", order.getId(), existingOrderStatusName);
        } else {
            if (ORDER_STATUS.CANCELLATION_REQUESTED.getValue().equals(existingOrderStatusName)) {
                log.info("OrderService.reComputeOrderStatus(): In past a cancellation request for was raised for OrderId=[{}]", orderId);
                if (ORDER_STATUS.CANCELLED.getValue().equals(recomputedOrderStatus)) {
                    // go ahead and cancel the order
                    order = updateOrderStatus(order.getId(), ORDER_STATUS.CANCELLED.getValue(), userEmail);
                } else {
                    log.info("OrderService.reComputeOrderStatus(): Leaving the OrderId=[{}] status=[{}]", orderId, existingOrderStatusName);
                }
            } else {
                log.info("OrderService.reComputeOrderStatus(): ****Status for the OrderId={} should be updated from [{}] to [{}] ****",
                         order.getId(), existingOrderStatusName, recomputedOrderStatus);
                order = updateOrderStatus(order.getId(), recomputedOrderStatus, userEmail);
            }

        }
        return order;
    }


    private static MessageContext getMessageContext() {
        return Util.getMessageContext();
    }

    @RaiseEvent(eventType = EventType.ORDER_STATUS_UPDATED)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Order updateOrderStatus(long orderId, String newOrderStatus, String userEmail) {
        log.debug("OrderService.updateOrderStatus(): orderId={} , newOrderStatus={} , email={}", orderId, newOrderStatus, userEmail);
        Order order = getOrder(orderId);
        MessageContext messageContext = getMessageContext();
        messageContext.put(MessageContext.ORDERID, order.getId());

        final String existingOrderStatus = order.getStatus().getStatusName();

        log.debug("OrderService.updateOrderStatus(): Current Status of OrderId=[{}] in database=[{}]", orderId, existingOrderStatus);

        if (newOrderStatus.equals(existingOrderStatus)) {
            log.info("No need to update the orderId=[{}] status as the exiting status=[{}] equals new status=[{}]", orderId, existingOrderStatus, newOrderStatus);
        } else {
            OrderHistory orderHistory = new OrderHistory();
            //orderHistory.setOrder(order);
            if (StringUtils.isNotBlank(userEmail)) {
                User user = userService.getUserByEmailId(userEmail);
                orderHistory.setUser(user);
            }
            OrderStatus orderStatus = orderDao.getOrderStatus(newOrderStatus);
            log.trace("OrderService.updateOrderStatus(): Before calling orderHistory.setOrderStatus(orderStatus)");
            orderHistory.setOrderStatus(orderStatus);
            order.setLatestOrderStatus(orderStatus);
            orderHistory.setOrder(order);
            
            orderDao.create(orderHistory);

            order = orderDao.update(order); //@todo : Rahul Agrawal : Investigate if this is required.

            //MessageContext messageContext = getMessageContext();
            //messageContext.put(MessageContext.ORDERID, order.getId());
            messageContext.put(MessageContext.ORDER_UPDATE_HISTORY, orderHistory.getId());
        }
        return order;
    }

    @RaiseEvent(eventType = EventType.ORDER_CANCELED)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderCancellationResponse cancelOrder(long orderId, String userEmail) {
        OrderCancellationResponse orderCancellationResponse = null;
        log.info("OrderService.cancelOrder(): OrderId=[{}] , UserEmail=[{}]", orderId, userEmail);
        Order order = getOrder(orderId);
        final String latestOrderStatus = order.getStatus().getStatusName();
        List<OrderItem> orderItems = order.getOrderItems();

        log.info("OrderService.cancelOrder(): OrderId={} has current Status={}", orderId, latestOrderStatus);

        if (ORDER_STATUS.CREATED.getValue().equals(latestOrderStatus)
            || (ORDER_STATUS.PREPARATION_IN_PROGRESS.getValue().equalsIgnoreCase(latestOrderStatus))) {
            // we can safely cancel the order. It has not yet been pushed into uniware.
            log.info("OrderService.cancelOrder(): Cancelling the OrderId={} without going to uniware as it's status={}", orderId, latestOrderStatus);
            for (int i = 0; i < orderItems.size(); i++) {
                OrderItem orderItem = orderItems.get(i);
                OrderItem updateOrderItem = orderItemService.updateOrderItemStatus(orderItem,
                                                                                   ORDER_ITEM_STATUS.CANCELLED.getValue(),
                                                                                   false,
                                                                                   userEmail);
            }
            order = updateOrderStatus(orderId, ORDER_STATUS.CANCELLED.getValue(), userEmail);
            orderCancellationResponse = new OrderCancellationResponse(true, "Order has been successfully Cancelled.");
        } else {
            // No need to fetch the latest state of affairs and cancel the items that can be cancelled as  have already done that before entering the call.
            //syncWithUniwareStatus(String.valueOf(orderId));
            //order = getOrder(orderId);

            List<OrderItem> cancellableOrderItems = new LinkedList<OrderItem>();
            for (OrderItem eachOrderItem : order.getOrderItems()) {
                if (eachOrderItem.isCancellable()) {
                    cancellableOrderItems.add(eachOrderItem);
                }
            }
            if (cancellableOrderItems.isEmpty()) {
                // all the items are in non cancellable state.
                log.info("OrderService.cancelOrder(): OrderId={} , none of the items are in cancellable state.", orderId);
                order = updateOrderStatus(orderId, ORDER_STATUS.CANCELLATION_REQUESTED.getValue(), userEmail);
                orderCancellationResponse = new OrderCancellationResponse(true, "Order could not be cancelled. " +
                                                                                " As all the order items are in NON CANCELLABLE status." +
                                                                                " Either they have been already dispatched or delivered or cancelled.");
            } else if (cancellableOrderItems.size() < orderItems.size()) {
                orderCancellationResponse = partialCancellation(orderId, userEmail, cancellableOrderItems);
            } else { // all items are eligible for cancellation
                orderCancellationResponse = allItemsCancellation(orderId, userEmail, order, cancellableOrderItems);
            }
        }
        log.info("OrderService.cancelOrder(): OrderId={} , OrderCancellationResponse={}", orderId, orderCancellationResponse);
        return orderCancellationResponse;
    }

    private OrderCancellationResponse allItemsCancellation(long orderId, String userEmail, Order order, List<OrderItem> cancellableOrderItems) {
        OrderCancellationResponse orderCancellationResponse = null;
        log.info("All order items are in cancellable state for the OrderId={} . Attempting to cancel orderItem Ids={}", orderId, cancellableOrderItems);
        try {
            boolean isSuccess = uniwareOrderService.cancelOrder(String.valueOf(orderId), cancellableOrderItems);
            if (isSuccess) {
                // mark the items in cancelled state in our system.
                log.info("*** Cancellation of OrderId={} was successful in Uniware.", orderId);
                for (OrderItem eachOrderItem : order.getOrderItems()) {
                    orderItemService.updateOrderItemStatus(eachOrderItem, ORDER_ITEM_STATUS.CANCELLED.getValue(), false, userEmail);
                }
                updateOrderStatus(orderId, ORDER_STATUS.CANCELLED.getValue(), userEmail);
                orderCancellationResponse = new OrderCancellationResponse(isSuccess, "Order has been successfully Cancelled.");
            } else { // error in uniware cancellation
                log.error("Failed to cancel the OrderId={} in Uniware. See logs for the reasons.", orderId);
                orderCancellationResponse = new OrderCancellationResponse(isSuccess, "Uniware did not allow the order to be cancelled.");
                updateOrderStatus(orderId, ORDER_STATUS.CANCELLATION_REQUESTED.getValue(), userEmail);
            }
        } catch (RemoteException e) {
            orderCancellationResponse = new OrderCancellationResponse(false, "Could not reach uniware to cancel order. Please try again after sometime.");
            log.error("OrderService.cancelOrder(): Uniware Error in cancelling the orderId={} . Error={} ", orderId, e.toString(), e);
        }
        return orderCancellationResponse;
    }

    private OrderCancellationResponse partialCancellation(long orderId, String userEmail, List<OrderItem> cancellableOrderItems) {
        OrderCancellationResponse orderCancellationResponse = null;
        log.info("Not all order items are in cancellable state for the OrderId={}. Attempting to cancel the items  that can be cancelled : {}",
                 orderId, cancellableOrderItems);
        // We Should attempt to cancel the ones that can be cancelled
        // and mark the order as partially cancelled if success else as marked  for cancellation.
        try {
            boolean isSuccess = uniwareOrderService.cancelOrder(String.valueOf(orderId), cancellableOrderItems);
            if (isSuccess) {
                log.info("Order Id ={} , OrderItemId's={} marked as cancelled in uniware.", orderId, cancellableOrderItems);
                for (OrderItem cancellableOrderItem : cancellableOrderItems) {
                    orderItemService.updateOrderItemStatus(cancellableOrderItem, ORDER_ITEM_STATUS.CANCELLED.getValue(), false, userEmail);
                }
                //Order order = updateOrderStatus(orderId, ORDER_STATUS.CANCELLATION_REQUESTED.getValue(), userEmail);
                orderCancellationResponse = new OrderCancellationResponse(isSuccess,
                                                                          "Some of the items are cancelled. Some of them are already Dispatched.");
            } else { //isSuccess=false;
                log.error("Failed to cancel the OrderId={} in Uniware. See logs for the reasons.", orderId);
                orderCancellationResponse = new OrderCancellationResponse(isSuccess,
                                                                          "Uniware did not allow the order to be cancelled.");
            }
            Order order = updateOrderStatus(orderId, ORDER_STATUS.CANCELLATION_REQUESTED.getValue(), userEmail);
        } catch (RemoteException e) {
            log.error("OrderService.cancelOrder(): Uniware gave Error in cancelling the orderId={} . Error={} ", orderId, e.toString(), e);
            orderCancellationResponse = new OrderCancellationResponse(false,
                                                                      "Could not reach uniware to cancel order. Please try again after sometime.");
        }
        return orderCancellationResponse;
    }

    public Order getOrder(Long orderId) {
        return orderDao.find(Order.class, orderId, true);
    }


    public Set<DiscountVoucher> getCustomerVouchersForOrder(Long orderId) {
        Order order = getOrder(orderId);
        return order.getVouchersForCustomer();
    }

    public List<OrderHistory> getOrderStatesHistory(Long orderId) {
        List<OrderHistory> result = new LinkedList<OrderHistory>();
        List<OrderHistory> fullHistory = orderDao.getOrderStatesHistory(orderId);
        if (fullHistory != null) {
            result = fullHistory;
        }
        return result;
    }

    public List<Order> getAllOrdersForCustomer(Customer loggedInCustomer) {
        return orderDao.getAllOrdersForCustomer(loggedInCustomer);
    }

    public Order getOrderFromCartId(Long cartId) {
        return orderDao.getOrderFromCartId(cartId);
    }

    public List<Cart> getAllCartsForCustomer(Customer customer) {
        return orderDao.getAllCartsForCustomer(customer);
    }

    @RaiseEvent(eventType = EventType.ORDER_PAYMENT_STATUS_UPDATED)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrderPaymentStatus(Order order, PaymentTransactionStatus paymentTransactionStatus) {
        log.info("OrderService.updateOrderPaymentStatus() for order={} payment status={}", order.getId(), paymentTransactionStatus.name());
        PaymentTransaction paymentTransaction = order.getMasterPayment().getLatestSubPayment().getPaymentTransaction();
        paymentTransaction.setPaymentTransactionStatus(paymentTransactionStatus);
        paymentTransaction = paymentTransactionService.updatePaymentTransaction(paymentTransaction);
        MessageContext messageContext = getMessageContext();
        messageContext.put(MessageContext.ORDERID, order.getId());
        messageContext.put(MessageContext.ORDER_PAYMENT_TRANSACTION_STATUS, paymentTransactionStatus.name());
    }

    public OrderHistory getOrderHistory(Long orderHistoryId) {
        return orderDao.findAny(OrderHistory.class, orderHistoryId);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void syncWithUniwareStatus(String orderId) {
        final Order order = getOrder(Long.parseLong(orderId));
        if (order.getOrderItems(Product.PRODUCT_TYPE.GOLDMINE).isEmpty()) {
            log.info("OrderService.syncWithUniwareStatus():OrderId=[{}]", orderId);
            final String orderStateInDatabase = order.getStatus().getStatusName();
            uniwareSynchroniser.execute(orderId, orderStateInDatabase);
        } else {
            log.info("OrderService.syncWithUniwareStatus():OrderId=[{}] has GoldMine. Don't go to uniware", orderId);
        }
    }


    public Order getOrderFromOrderCode(String orderCode) {
        final String orderId = CryptographyUtil.decrypt(orderCode);
        return getOrder(Long.parseLong(orderId));
    }
}
