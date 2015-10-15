package com.bluestone.app.uniware;

import java.util.Calendar;
import java.util.List;

import com.unicommerce.uniware.services.SaleOrder;
import com.unicommerce.uniware.services.SaleOrderItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.design.model.product.Product.PRODUCT_TYPE;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.payment.spi.model.PaymentGateway;
import com.bluestone.app.shipping.model.Address;

/**
 * @author Rahul Agrawal
 *         Date: 11/27/12
 */
class SaleOrderFactory {

    private static final Logger log = LoggerFactory.getLogger(SaleOrderFactory.class);

    private SaleOrderFactory() {
    }

    static SaleOrder create(Order blueStoneOrder) {
        log.debug("SaleOrderFactory.create(): Creating Uniware Sale Order for OrderId={}", blueStoneOrder.getId());
        SaleOrder result = null;

        SaleOrderBuilder saleOrderBuilder = new SaleOrderBuilder();
        final String orderId = Long.toString(blueStoneOrder.getId());
        saleOrderBuilder.setCode(orderId);
        //saleOrderBuilder.setDisplayOrderCode(blueStoneOrder.getCode()); // having issues during invoice etc : To be brought back post Jan 13 release.
        saleOrderBuilder.setDisplayOrderCode(orderId);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(blueStoneOrder.getCreatedAt());
        calendar.getTime();
        saleOrderBuilder.setDisplayOrderDateTime(calendar);

        Customer customer = blueStoneOrder.getCart().getCustomer();
        saleOrderBuilder.setNotificationEmail(customer.getEmail());

        String customerPhone = customer.getCustomerPhone();
        if (StringUtils.isNotBlank(customerPhone)) {
            saleOrderBuilder.setNotificationMobile(customerPhone);
        }

        PaymentGateway paymentGateway = blueStoneOrder.getMasterPayment().getLatestSubPayment().getPaymentTransaction().getPaymentGateway();
        if (PaymentGateway.CASH_ON_DELIVERY.equals(paymentGateway)) {
            saleOrderBuilder.setCashOnDelivery(true);
        } else {
            saleOrderBuilder.setCashOnDelivery(false);
        }

        Address billingAddress = blueStoneOrder.getCart().getBillingAddress();
        log.debug("Creating the Billing Address");
        saleOrderBuilder.setBillingAddress(AddressBuilder.createUniwareAddress(billingAddress));

        List<SaleOrderItem> saleOrderItemList = SaleOrderItemFactory.createOrderItems(blueStoneOrder);
        saleOrderBuilder.setSaleOrderItems(saleOrderItemList);
        final Address blueStoneShippingAddress = blueStoneOrder.getOrderItems().get(0).getShippingAddress();
        log.debug("Creating the Shipping Address");
        saleOrderBuilder.setShippingAddress(AddressBuilder.createUniwareAddress(blueStoneShippingAddress));

        result = saleOrderBuilder.create();
        return result;
    }


}
