package com.bluestone.app.voucher.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.ListFilterCriteria;
import com.bluestone.app.core.util.CryptographyUtil;
import com.bluestone.app.core.util.PaginationUtil;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.voucher.dao.VoucherDao;
import com.bluestone.app.voucher.model.DiscountVoucher;
import com.bluestone.app.voucher.model.DiscountVoucher.VoucherType;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class VoucherService {

    private static final Logger log = LoggerFactory.getLogger(VoucherService.class);

    @Autowired
    private VoucherDao voucherDao;

    public DiscountVoucher getVoucher(Long voucherId) {
        return voucherDao.getVoucher(voucherId);
    }

    public DiscountVoucher getVoucherByName(String voucherCode) {
        return voucherDao.getVoucherByCode(voucherCode);
    }

    public List<String> validateVoucherForSave(DiscountVoucher voucher) {
        List<String> errors = new ArrayList<String>();
        boolean isNameChanged = true;
        if (voucher.getId() != 0) {
            DiscountVoucher voucherById = voucherDao.getVoucher(voucher.getId());
            if (voucherById.getName().equalsIgnoreCase(voucher.getName())) {
                isNameChanged = false;
            }
        }
        DiscountVoucher existingVoucher = voucherDao.getVoucherByCode(voucher.getName());
        if (existingVoucher != null && isNameChanged == true) {
            errors.add("Voucher code already exists.");
        }

        if (voucher.getValue().compareTo(BigDecimal.ZERO) == 0) {
            errors.add("Voucher value should be more than zero.");
        }

        if (voucher.getVoucherType().name().equalsIgnoreCase("percentage")) {
            if (voucher.getValue().compareTo(new BigDecimal(100)) > 0) {
                errors.add("Voucher value in percentage should not be more than 100.");
            }
        }
        if (voucher.getVoucherType().name().equalsIgnoreCase("amount")) {
            if (voucher.getMinimumAmount().compareTo(BigDecimal.ZERO) == 0) {
                errors.add("Minimum Amount should be more than zero.");
            }
            if (voucher.getMinimumAmount().compareTo(voucher.getValue()) < 0) {
                errors.add("Minimum amount should not be less than voucher amount.");
            }
        }
        if (voucher.getId() == 0) {
            //if (!DateUtils.isSameDay(voucher.getValidTo(), Calendar.getInstance().getTime())) {
            if (voucher.getValidTo().before(Calendar.getInstance().getTime())) {
                errors.add("Valid to should not be less than preset date and time.");
            }
            //}
            if (voucher.getMaxAllowed() == 0) {
                errors.add("Max allowed should be more than zero.");
            }
        }
        if (voucher.getValidFrom().after(voucher.getValidTo())) {
            errors.add("Valid from should not be greater than valid to date.");
        }
        return errors;

    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createVoucher(DiscountVoucher voucher) {
        log.debug("VoucherService.createVoucher():[{}]", voucher.toString());
        voucherDao.create(voucher);
        log.info("VoucherService.created successfully [{}]", voucher);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DiscountVoucher updateVoucher(DiscountVoucher voucher) {
        log.info("VoucherService.updateVoucher():{}", voucher);
        return voucherDao.update(voucher);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Set<DiscountVoucher> createAndGetVouchersForOrder(Order order) {
        log.info("VoucherService.createAndGetVouchersForOrder(): order id={}", order.getId());
        Set<DiscountVoucher> customerVouchers = new HashSet<DiscountVoucher>();
        double[] voucherAmounts = {500, 1000, 2000};
        for (int i = 0; i < voucherAmounts.length; i++) {
            BigDecimal amount = new BigDecimal(voucherAmounts[i]);
            customerVouchers.add(createAndGetVoucherForOrder(order, amount, i));

        }
        return customerVouchers;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private DiscountVoucher createAndGetVoucherForOrder(Order order, BigDecimal amount, int index) {
        log.debug("VoucherService.createAndGetVoucherForOrder(): order id={} , voucher amount={}", order.getId(), amount.intValue());
        DiscountVoucher voucher = new DiscountVoucher();
        voucher.setVoucherType(VoucherType.AMOUNT);
        voucher.setValue(amount);
        voucher.setMaxAllowed(1);
        voucher.setMinimumAmount(amount.multiply(new BigDecimal(10)));
        voucher.setIsActive((short) 1);
        setValidity(voucher);
        String uniqueVoucherName = "V" + index + order.getCode();
        uniqueVoucherName = encryptVoucherName(uniqueVoucherName);
        voucher.setName("V" + index + uniqueVoucherName);
        createVoucher(voucher);
        return voucher;
    }

    public String encryptVoucherName(String voucherName) {
        return CryptographyUtil.encrypt(voucherName, 8);
    }

    void setValidity(DiscountVoucher voucher) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        voucher.setValidFrom(now);
        calendar.add(Calendar.YEAR, 1);
        /*
         * Taken care for same day concept during validation while it is being
         * used calendar.set(Calendar.HOUR_OF_DAY, 23);
         * calendar.set(Calendar.MINUTE, 59); calendar.set(Calendar.SECOND, 59);
         */
        voucher.setValidTo(calendar.getTime());
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public DiscountVoucher decrementMaxAllowed(DiscountVoucher discountVoucher) {
        log.debug("VoucherService.decrementMaxAllowed() for Voucher={}", discountVoucher.getName());
        discountVoucher.decrementMaxAllowed();
        return voucherDao.update(discountVoucher);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void save(DiscountVoucher voucher) {
        if (voucher.getId() == 0) {
            voucherDao.create(voucher);
        } else {
            voucherDao.update(voucher);
        }
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void disableVoucher(DiscountVoucher voucher) {
        voucherDao.markEntityAsDisabled(voucher);
    }

    public String validateVoucherForDisable(DiscountVoucher voucher) {
        String error = "";
        int voucherUsedCount = voucherDao.ordersCountForVoucher(voucher);
        if (voucherUsedCount != 0) {
            error = "Voucher has already been used " + voucherUsedCount + " No. of times.";
        }
        return error;
    }

    public Map<Object, Object> getVoucherDetails(ListFilterCriteria filterCriteria, boolean forActive) {
        int startingFrom = (filterCriteria.getP() - 1) * filterCriteria.getItemsPerPage();
        Map<Object, Object> vouchersListAndTotalCount = voucherDao.getVouhersAndTotalCount(startingFrom, filterCriteria, forActive);
        String baseURLForPagination = forActive == true ? "/admin/voucher/list" : "/admin/voucher/disabledlist";
        List<DiscountVoucher> vouchersList = (List<DiscountVoucher>) vouchersListAndTotalCount.get("list");
        Map<Object, Object> dataForView = PaginationUtil.generateDataForView(vouchersListAndTotalCount, filterCriteria.getItemsPerPage(), filterCriteria.getP(),
                                                                             startingFrom, baseURLForPagination, vouchersList.size());
        Map<String, String> searchFieldMap = new HashMap<String, String>();
        searchFieldMap.put("Id", "id");
        searchFieldMap.put("Voucher Name", "name");
        searchFieldMap.put("VoucherType", "voucherType");
        searchFieldMap.put("Max Allowed", "maxAllowed");
        searchFieldMap.put("Description", "description");
        searchFieldMap.put("Value", "value");

        dataForView.put("searchFieldMap", searchFieldMap);
        return dataForView;
    }

}
