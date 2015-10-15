package com.bluestone.app.voucher.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.CustomizationStoneSpecification;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.voucher.model.DiscountVoucher;

/**
 * @author Rahul Agrawal
 *         Date: 1/31/13
 */
@Service
public class NPGDiscountVoucherValidator {

    private static final Logger log = LoggerFactory.getLogger(NPGDiscountVoucherValidator.class);

    public static final String VOUCHER_PREFIX = "NPG";

    NPGDiscountVoucherValidator() {
    }

    String execute(DiscountVoucher discountVoucher, List<CartItem> cartItems) {
        final String discountVoucherName = discountVoucher.getName();
        boolean isValid = true;
        if (discountVoucherName.startsWith(VOUCHER_PREFIX)) {
            log.info("NPGDiscountVoucherValidator.execute() for DiscountVoucher code={}", discountVoucherName);
            isValid = isVoucherValid(cartItems, discountVoucherName);
        } else {
            log.info("NPGDiscountVoucherValidator.execute(): Skipping VoucherCode={}", discountVoucherName);
        }
        String returnCode = null;
        if (!isValid) {
            returnCode = discountVoucherName + " is not valid for metal only products.";
        }
        return returnCode;
    }

    private boolean isVoucherValid(List<CartItem> cartItems, String voucherCode) {
        boolean isValid = true;
        for (CartItem eachCartItem : cartItems) {
            final Product product = eachCartItem.getProduct();
            if(product instanceof Customization) {
                final Customization customization = (Customization) product;
                final List<CustomizationStoneSpecification> StoneSpecifications = customization.getCustomizationStoneSpecification();
                if (StoneSpecifications == null || StoneSpecifications.isEmpty()) {
                    log.info("NPGDiscountVoucherValidator:CartItemId={}: StoneSpecifications is empty/null....Check if it is a chain", eachCartItem.getId());
                    DesignCategory designCategory = customization.getDesign().getDesignCategory();
                    final String categoryType = designCategory.getCategoryType();
                    if (designCategory.isChains() || designCategory.isTanmaniyaChains()) {
                        log.info("NPGDiscountVoucherValidator:CartItemId={}:    It is a chain with [Category name={}]", eachCartItem.getId(), categoryType);
                        // chains as pure metals are allowed
                    } else {
                        isValid = false;
                        log.info("NPGDiscountVoucherValidator:[CartItemId={}]:[ProductId={}]:[Design Category={}] is a pure metal without stones. Hence Voucher={} is not valid.",
                                 eachCartItem.getId(), product.getId(), categoryType, voucherCode);
                        break;
                    }
                } else {
                    log.info("NPGDiscountVoucherValidator:CartItemId={}: StoneSpecifications is present.", eachCartItem.getId());
                }
            }
        }
        return isValid;
    }
}
