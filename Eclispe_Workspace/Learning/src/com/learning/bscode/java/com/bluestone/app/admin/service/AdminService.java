package com.bluestone.app.admin.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.admin.model.InvalidOrderStatusForAmountCalculation;
import com.bluestone.app.order.dao.OrderDao;
import com.bluestone.app.order.model.OrderStatus;
import com.bluestone.app.reporting.order.OrderReport;

/**
 * @author Rahul Agrawal
 *         Date: 11/5/12
 */
@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    /*@Autowired
    private HolidayService holidayService;*/

    @Autowired
    private OrderDao orderDao;
    
    /*public Map<Long, Integer> getWorkingDaysPassedForOrders(List<Order> ordersList) {
        Map<Long, Integer> workingDaysPassedMap = new HashMap<Long, Integer>();
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        for (Order order : ordersList) {
            int workingDaysPassed = holidayService.getWorkingDays(order.getCreatedAt(), currentDate);
            workingDaysPassedMap.put(order.getId(), workingDaysPassed);
        }
        return workingDaysPassedMap;
    }*/

    public OrderStatus getorderStatusById(long statusId, boolean forActive) {
        return orderDao.find(OrderStatus.class, statusId, forActive);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void createOrderStatus(OrderStatus orderStatus) {
        orderDao.create(orderStatus);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateOrderStatus(OrderStatus orderStatus) {
        orderDao.update(orderStatus);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void enable(OrderStatus orderStatus) {
        orderDao.markEntityAsEnabled(orderStatus);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void disableOrderStatus(OrderStatus orderStatus) {
        orderDao.markEntityAsDisabled(orderStatus);
    }
    
    public Double getTotalValueOfOrders(List<OrderReport> orderList, boolean forValidOnly) {
        Double totalValue = 0.00;
        boolean ignoreForTotalCalculation = false;
        if (orderList != null && orderList.size() > 0) {
            for (OrderReport order : orderList) {
                ignoreForTotalCalculation = false;
                if (forValidOnly == true) {
                    String latestOrderStatus = order.getOrderStatus();
                    InvalidOrderStatusForAmountCalculation[] values = InvalidOrderStatusForAmountCalculation.values();
                    for (InvalidOrderStatusForAmountCalculation invalidOrderStatusForAmountCalculation : values) {
                        if (latestOrderStatus.equalsIgnoreCase(invalidOrderStatusForAmountCalculation.getOrderStatusName())) {
                            ignoreForTotalCalculation = true;
                            break;
                        }
                    }
                }
                if (ignoreForTotalCalculation == false) {
                    totalValue = totalValue + Double.valueOf(order.getOrderTotal());
                }
            }
        }
        return totalValue;
    }
}
