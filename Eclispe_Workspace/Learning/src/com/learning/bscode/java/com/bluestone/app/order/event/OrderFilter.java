package com.bluestone.app.order.event;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.payment.spi.model.PaymentTransaction;

/**
 * @author Rahul Agrawal
 *         Date: 3/26/13
 */
public class OrderFilter {

    private static final Logger log = LoggerFactory.getLogger(OrderFilter.class);

    public static boolean isEligibleForUniware(Order order, String paymentTransactionStatus) {
        log.info("OrderFilter.isEligibleForUniware(): OrderId=[{}] PaymentTransactionStatus=[{}]", order.getId(), paymentTransactionStatus);
        boolean isEligible = false;
        final boolean isPaymentOK = filterPayments(paymentTransactionStatus);
        if (isPaymentOK && !isGoldMine(order)) {
            isEligible = true;
        }
        log.info("OrderFilter.isEligibleForUniware(): {}", isEligible);
        return isEligible;
    }


    static boolean filterPayments(String paymentTransactionStatus) {
        boolean isEligible = false;
        try {
            PaymentTransaction.PaymentTransactionStatus status = PaymentTransaction.PaymentTransactionStatus.valueOf(paymentTransactionStatus);
            switch (status) {
                case COD_CONFIRMED:
                case PDC_ACCEPTED:
                case CHEQUE_PAID:
                case NET_BANKING_TRANSFER:
                case PAYMENT_ACCEPTED:
                case REPLACEMENT_ORDER:
                case SUCCESS:
                    isEligible = true;
                    break;
            }
        } catch (IllegalArgumentException e) {
            log.error("Error: OrderFilter.checkPaymentTransactionStatus(): **This needs investigation** : PaymentTransaction status={}", paymentTransactionStatus, e);
            isEligible = false;
        }
        log.info("OrderFilter.filterPayments(): IsEligible For Uniware={}", isEligible);
        return isEligible;
    }

    static boolean isGoldMine(Order order) {
        List<OrderItem> goldMineItems = order.getOrderItems(Product.PRODUCT_TYPE.GOLDMINE);
        boolean isGoldMine = true;
        if (goldMineItems.isEmpty()) {
            isGoldMine = false;
        } else {
            log.info("OrderId={} has Products from [{}].", order.getId(), Product.PRODUCT_TYPE.GOLDMINE.name());
            int numberOfItems = order.getOrderItems().size();
            if (goldMineItems.size() != numberOfItems) {
                log.error("Error: OrderFilter:GoldMine Orders were mixed with other products. This is not an error and needs investigation.");
            }
        }
        log.info("OrderFilter.isGoldMine():[{}]", isGoldMine);
        return isGoldMine;
    }
}
