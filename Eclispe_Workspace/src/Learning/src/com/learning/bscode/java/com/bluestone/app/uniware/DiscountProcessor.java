package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.unicommerce.uniware.services.SaleOrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.order.model.Order;

class DiscountProcessor {

    private static final Logger log = LoggerFactory.getLogger(DiscountProcessor.class);

    static void spreadDiscountToItems(Order bsOrder, List<SaleOrderItem> uniwareSaleOrderItemList) {
        long orderId = bsOrder.getId();
        BigDecimal totalDiscountAllowed = bsOrder.getDiscountPrice();
        if (totalDiscountAllowed.compareTo(new BigDecimal(0)) == 0) {
            return;
        }

        log.debug("DiscountProcessor.spreadDiscount(): Adding Discount Amount to Items for OrderId={}", orderId);
        //@todo : Rahul Agrawal : If we have to exclude the Solitare
        final BigDecimal orderValue = bsOrder.getTotalPrice();
        final BigDecimal percentageDiscount = totalDiscountAllowed.multiply(new BigDecimal(100)).divide(orderValue, 8, RoundingMode.UP);
        log.debug("DiscountProcessor.spreadDiscount(): OrderId={} , Total Discount Amount={} , Equivalent % Discount={}",
                  orderId, totalDiscountAllowed, percentageDiscount);

        BigDecimal MRPOfItem;

        for (int i = 0; i < uniwareSaleOrderItemList.size(); i++) {
            SaleOrderItem uniwareSaleOrderItem = uniwareSaleOrderItemList.get(i);
            //sellingPriceOfItem = saleOrderItem.getSellingPrice();
            MRPOfItem = uniwareSaleOrderItem.getTotalPrice();
            log.debug("DiscountProcessor.spreadDiscountToItems(): OrderItemId={} , MRP={}", uniwareSaleOrderItem.getCode(), MRPOfItem);
            BigDecimal discountAmountForItem = percentageToAmount(percentageDiscount, MRPOfItem);
            BigDecimal roundedUpDiscountAmount = discountAmountForItem.setScale(0, RoundingMode.UP);

            if (roundedUpDiscountAmount.compareTo(totalDiscountAllowed) <= 0) {
                // allow it and reduce the total discount by this discount
                totalDiscountAllowed = totalDiscountAllowed.subtract(roundedUpDiscountAmount);
            } else {
                roundedUpDiscountAmount = totalDiscountAllowed;
                totalDiscountAllowed = new BigDecimal(0.0);
            }
            uniwareSaleOrderItem.setDiscount(roundedUpDiscountAmount);
            //Rahul Agrawal :  reset the selling price and the total price.
            final BigDecimal netPrice = uniwareSaleOrderItem.getTotalPrice().subtract(uniwareSaleOrderItem.getDiscount());
            uniwareSaleOrderItem.setSellingPrice(netPrice);
            uniwareSaleOrderItem.setTotalPrice(netPrice);
            log.info("DiscountProcessor.spreadDiscount(): OrderId={} , ItemId={} , MRP={} , Discount={} , Net Price={}",
                     orderId, uniwareSaleOrderItem.getCode(), MRPOfItem, roundedUpDiscountAmount, netPrice);

        }
    }

    static BigDecimal percentageToAmount(BigDecimal percentageDiscount, BigDecimal amount) {
        BigDecimal result = percentageDiscount.multiply(amount).divide(new BigDecimal(100.00));
        log.debug("DiscountProcessor.percentageToAmount():{}% Discount on Amount={} = {}",
                  percentageDiscount, amount, result);
        return result;
    }
}