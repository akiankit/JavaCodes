package com.bluestone.app.voucher.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.checkout.cart.model.Cart;
import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.util.NumberUtil;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.model.DiscountVoucher.VoucherType;

/**
 * @author Rahul Agrawal
 *         Date: 2/2/13
 */
@Service
public class DiscountVoucherValidator {

    private static final Logger log = LoggerFactory.getLogger(DiscountVoucherValidator.class);

    @Autowired
    private NPGDiscountVoucherValidator npgDiscountVoucherValidator;

    @Autowired
    private BasicDiscountVoucherValidator basicDiscountVoucherValidator;


    public String execute(DiscountVoucher discountVoucher, Cart cart) {

        String validationResult = basicDiscountVoucherValidator.execute(discountVoucher, cart.getTotalPrice());
        if (StringUtils.isBlank(validationResult)) {
            validationResult = performAdditionalValidations(discountVoucher, cart.getActiveCartItems());
        }
        return validationResult;

    }

    String performAdditionalValidations(DiscountVoucher discountVoucher, List<CartItem> cartItems) {
        String result = npgDiscountVoucherValidator.execute(discountVoucher, cartItems);
        return result;
    }

    public String validateVoucherForCRMOrders(DiscountVoucher discountVoucher, BigDecimal totalAmount) {
        return basicDiscountVoucherValidator.execute(discountVoucher, totalAmount);
    }
    
    //TODO vikas .. need to move it into diffretn service
    @RaiseEvent(eventType = EventType.CART_UPDATED)
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void applyDiscountVoucher(Cart cart, DiscountVoucher discountVoucher) {
        log.debug("CartService.applyDiscountVoucher(): Cart id={} , VoucherName={}, VoucherType={}", cart.getId(),
                  discountVoucher.getName(), discountVoucher.getVoucherType().name());
        // TODO lock voucher code
        VoucherType voucherType = discountVoucher.getVoucherType();
        cart.setDiscountVoucher(discountVoucher);
        switch (voucherType) {
            case AMOUNT:
                cart.setDiscountPrice(discountVoucher.getValue());
                break;
            case PERCENTAGE:
                BigDecimal value = cart.getTotalPrice().multiply(discountVoucher.getValue());
                BigDecimal percentageValue = value.divide(BigDecimal.valueOf(100));
                cart.setDiscountPrice(NumberUtil.roundUp(percentageValue));
                break;
            default:
                break;
        }
    }
}
