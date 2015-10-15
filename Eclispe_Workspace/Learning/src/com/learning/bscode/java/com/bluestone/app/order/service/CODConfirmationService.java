package com.bluestone.app.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.core.util.Constants;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderCancellationResponse;
import com.bluestone.app.order.model.UniwareStatus;

import static com.bluestone.app.payment.spi.model.PaymentTransaction.PaymentTransactionStatus;

/**
 * @author Rahul Agrawal
 *         Date: 11/27/12
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class CODConfirmationService {

    @Autowired
    private OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(CODConfirmationService.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean execute(Order order, String paymentStatus, String userEmail) {
        log.info("CODConfirmationService.execute(): OrderId={} PaymentStatus={}", order.getId(), paymentStatus);
        boolean result = false;
        final long id = order.getId();

        PaymentTransactionStatus newPaymentTransactionStatus = getNewPaymentTransactionStatus(paymentStatus);
        if (newPaymentTransactionStatus != null) {
            result = checkIfFlowIsValid(order, newPaymentTransactionStatus);
            if (result) {
                orderService.updateOrderPaymentStatus(order, newPaymentTransactionStatus);
                result = updateOrder(id, newPaymentTransactionStatus, userEmail);
            }
        } else {
            log.error("CODConfirmationService.execute(): Invalid Value of the PaymentTransactionStatus:{} for the OrderId={} status",
                      paymentStatus, id);
        }
        return result;
    }

    private boolean checkIfFlowIsValid(Order order, PaymentTransactionStatus newPaymentTransactionStatus) {
        log.info("CODConfirmationService.checkIfFlowIsValid() for orderId=[{}] , newPaymentTransactionStatus={}", order.getId(), newPaymentTransactionStatus.name());
        boolean isValid = false;
        PaymentTransactionStatus currentPaymentTransactionStatus = getCurrentPaymentTransactionStatus(order);
        /*if (currentPaymentTransactionStatus.equals(newPaymentTransactionStatus)) {
            isValid = false;
            log.error("CODConfirmationService: Not updating the payment status to {} as the current payment status of the OrderId=[{}] is {}",
                      newPaymentTransactionStatus.name(), order.getId(), currentPaymentTransactionStatus.name());
        }*/
        if (currentPaymentTransactionStatus.equals(PaymentTransactionStatus.COD_ON_HOLD)) {
            isValid = true;
        } else {
            log.error(
                    "ERROR : CODConfirmationService: Not updating the payment status to [{}] as the current payment status of the OrderId=[{}] is [{}]. ",
                    newPaymentTransactionStatus.name(), order.getId(), currentPaymentTransactionStatus.name());
        }
        return isValid;
    }

    private boolean updateOrder(long orderId, PaymentTransactionStatus newPaymentTransactionStatus, String userEmail) {
        log.info("CODConfirmationService.updateOrder(): OrderId=[{}]", orderId);
        boolean result = false;
        Order order = null;
        if (PaymentTransactionStatus.COD_CONFIRMED.equals(newPaymentTransactionStatus)) {
            order = orderService.updateOrderStatus(orderId, UniwareStatus.ORDER_STATUS.PROCESSING.getValue(), userEmail);
            result = true;
        }
        if (PaymentTransactionStatus.COD_CANCELLED.equals(newPaymentTransactionStatus)) {
            OrderCancellationResponse orderCancellationResponse = orderService.cancelOrder(orderId, userEmail);
            result = orderCancellationResponse.isSuccess();
            log.info("Result for Order id={} cancellation was {}.", orderId, result);
        }
        return result;
    }

    private PaymentTransactionStatus getCurrentPaymentTransactionStatus(Order order) {
        log.debug("CODConfirmationService.getCurrentPaymentTransactionStatus() for OrderId={}", order.getId());
        return order.getMasterPayment().getLatestSubPayment().getPaymentTransaction().getPaymentTransactionStatus();
    }

    private PaymentTransactionStatus getNewPaymentTransactionStatus(String paymentStatus) {
        PaymentTransactionStatus paymentTransactionStatus = null;
        //@todo P1 : Rahul Agrawal :  we should use clean this deprecated stuff
        if (Constants.ORDER_COD_CONFIRMED.equalsIgnoreCase(paymentStatus)) {
            paymentTransactionStatus = PaymentTransactionStatus.COD_CONFIRMED;
        } else if (UniwareStatus.ORDER_STATUS.CANCELLED.getValue().equalsIgnoreCase(paymentStatus)) {
            paymentTransactionStatus = PaymentTransactionStatus.COD_CANCELLED;
        }
        return paymentTransactionStatus;
    }
}
