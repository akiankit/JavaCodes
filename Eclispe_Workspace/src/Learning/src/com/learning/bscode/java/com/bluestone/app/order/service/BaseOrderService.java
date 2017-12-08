package com.bluestone.app.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.service.CartService;
import com.bluestone.app.payment.service.MasterPaymentService;
import com.bluestone.app.payment.service.PaymentTransactionService;
import com.bluestone.app.payment.spi.model.MasterPayment;
import com.bluestone.app.payment.spi.model.PaymentResponse;
import com.bluestone.app.payment.spi.model.PaymentTransaction;
import com.bluestone.app.payment.spi.model.MasterPayment.MasterPaymentStatus;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.service.VoucherService;

public class BaseOrderService {

    private static final Logger log = LoggerFactory.getLogger(BaseOrderService.class);
    
    @Autowired
    protected CartService cartService;
    
    @Autowired
    protected VoucherService voucherService;

    
    @Autowired
    protected PaymentTransactionService paymentTransactionService;

    @Autowired
    protected MasterPaymentService masterPaymentService;
    
	protected Cart getCartToBeUsed(PaymentTransaction paymentTransaction, MasterPayment masterPayment) {
		Cart cartTobeUsed = masterPayment.getCart();
        PaymentResponse paymentResponse = paymentTransaction.getPaymentResponse();
        if (paymentResponse != null) {
            long clonedCartId = paymentResponse.getClonedCartId();
            if (clonedCartId != 0l) {
                cartTobeUsed = cartService.getInactiveCart(Long.valueOf(clonedCartId));
            } else {
                log.warn("PaymentGateway used for this order {} didn't set merchantfields", paymentTransaction.getPaymentGateway().name());
            }
        } else {
            log.warn("PaymentGateway used for this order {} didn't set payementResponse", paymentTransaction.getPaymentGateway().name());
        }
		return cartTobeUsed;
	}
	
	protected MasterPayment getMasterPayment(PaymentTransaction paymentTransaction) {
		MasterPayment masterPayment = paymentTransaction.getSubPayment().getMasterPayment();
        // need to get the latest master payment from db , as masterpayment
        // object is set by webflow , and would not be in consistence with db
        MasterPayment latestMasterPaymentFromDb = masterPaymentService.getMasterPayment(masterPayment.getId());
		return latestMasterPaymentFromDb;
	}
	
    protected void preOrderProcessing(MasterPayment masterPayment, Cart cartToCreateOrder) {
        // cart = cartService.getCart(cart.getId());
        // TODO vikas:later when subpayment is implemented , then this code
        // should move out and should be done only when full payment is done

        // deactivate original cart
        Cart originalCart = masterPayment.getCart();
        originalCart.setIsActive(false);
        cartService.updateCart(originalCart);

        if (originalCart.getId() == cartToCreateOrder.getId()) {
            log.info("Original Cart {} and cart to be used {} create order are same. This can happen if payment is not from any payment gateway", originalCart.getId(), cartToCreateOrder.getId());
        } else {
            log.info("Original Cart {} and cart to be used {} create order are not same. Which means its a payment gateway transaction type. No need to inactivate this cart as it is already in that state {}", originalCart.getId(), cartToCreateOrder.getId(), cartToCreateOrder.isActive());
        }

        // TODO vikas: in case of post paid orders like cod/pdc what should be the status
        masterPayment.setPaymentStatus(MasterPaymentStatus.PAYMENT_FULLY_MADE);
        masterPayment.setCart(cartToCreateOrder);
        masterPaymentService.updateMasterPayment(masterPayment);

        DiscountVoucher discountVoucher = cartToCreateOrder.getDiscountVoucher();
        if (discountVoucher != null) {
            voucherService.decrementMaxAllowed(discountVoucher);
        }
    }

}
