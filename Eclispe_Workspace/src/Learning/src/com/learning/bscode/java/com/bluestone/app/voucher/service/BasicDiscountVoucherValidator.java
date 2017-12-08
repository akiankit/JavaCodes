package com.bluestone.app.voucher.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bluestone.app.core.util.DateTimeUtil;
import com.bluestone.app.voucher.model.DiscountVoucher;

/**
 * @author Rahul Agrawal
 *         Date: 2/1/13
 */
@Service
public class BasicDiscountVoucherValidator {

    private static final Logger log = LoggerFactory.getLogger(BasicDiscountVoucherValidator.class);

    BasicDiscountVoucherValidator() {
    }

    String execute(DiscountVoucher discountVoucher, BigDecimal totalAmount) {
        if (discountVoucher == null) {
            return "Invalid Voucher code.";
        }

        log.debug("BasicDiscountVoucherValidator.performBasicVoucherValidations(): discountVoucher={} , Total cart Value={}",
                  discountVoucher.getName(), totalAmount);

        final String discountVoucherName = discountVoucher.getName() + " ";
        if (discountVoucher.getIsActive() == 0) {
            return "Voucher " + discountVoucherName + " is not active.";
        }
        if (discountVoucher.getMaxAllowed() <= 0) {
            // in a case where 2 users start shopping togethor and use the same voucher, AS OF NOW, we allow both of them.
            // in such a case, when the decrement the voucher twice and it's value reaches to -1
            return "This Voucher " + discountVoucherName + " can no longer be applied";
        }

        if (totalAmount.doubleValue() < discountVoucher.getMinimumAmount().doubleValue()) {
            return "Voucher " + discountVoucherName +
                   " can not be applied on current total amount = " + totalAmount.doubleValue()
                   + " . Minimum total cart value required = " + discountVoucher.getMinimumAmount().doubleValue();
        }

        final Date today = DateTimeUtil.getDate();
        final Date validTo = discountVoucher.getValidTo();

        if (validTo.before(today)) {
            if (DateUtils.isSameDay(today, validTo)) {
                log.info("BasicDiscountVoucherValidator: Evaluates to true as the expiry date ignoring time is valid. Now={} , Validity={}",
                         DateTimeUtil.getSimpleDateAndTime(today), DateTimeUtil.getSimpleDateAndTime(validTo));
            } else {
                return "Voucher " + discountVoucherName + " expired on " + DateTimeUtil.formatDate(validTo);
            }
        }

        final Date validFrom = discountVoucher.getValidFrom();
        if (validFrom.after(today)) {
            log.warn("Discount voucher {} cannot be applied today. It is valid on or after {}", discountVoucherName, DateTimeUtil.getSimpleDateAndTime(validFrom));
            return "Voucher " + discountVoucherName + " can not be applied.";
        }
        log.info("BasicDiscountVoucherValidator: Discount VoucherCode={} PASSED basic validations successfully.", discountVoucherName);
        return null;
    }
}
