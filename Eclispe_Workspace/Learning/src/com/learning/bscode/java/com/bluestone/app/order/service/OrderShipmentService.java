package com.bluestone.app.order.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.order.dao.OrderShipmentDao;
import com.bluestone.app.order.model.ShippingItem;
import com.bluestone.app.order.model.ShippingPacket;
import com.bluestone.app.order.model.ShippingPacketHistory;
import com.bluestone.app.order.model.ShippingPacketStatus;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class OrderShipmentService {
    
    private static final Logger log = LoggerFactory.getLogger(OrderShipmentService.class);
    
    @Autowired
    private OrderShipmentDao orderShipmentDao;
    
    public ShippingPacketHistory getShippingItemHistory(long shippingItemHistoryId) {
        return orderShipmentDao.findAny(ShippingPacketHistory.class, shippingItemHistoryId);
    }

    public ShippingPacket getShippingPacket(String shippingPackageCode) {
        return orderShipmentDao.getShippingPacket(shippingPackageCode);
    }

    public ShippingPacketStatus getShippingPacketStatus(String statusCode) {
        return orderShipmentDao.getShippingPacketStatus(statusCode);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createShippingPacket(ShippingPacket shippingPacket) {
        orderShipmentDao.create(shippingPacket);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ShippingPacket updateShippingPacket(ShippingPacket shippingPacket) {
        return orderShipmentDao.update(shippingPacket);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ShippingPacketStatus createShippingPacketStatus(String statusCode) {
        ShippingPacketStatus shippingPacketStatus = new ShippingPacketStatus();
        shippingPacketStatus.setStatusName(statusCode);
        orderShipmentDao.create(shippingPacketStatus);
        return shippingPacketStatus;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createShippingPacketHistory(ShippingPacketHistory shippingPacketHistory) {
        orderShipmentDao.create(shippingPacketHistory);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createShippingItem(ShippingItem shippingItem) {
        orderShipmentDao.create(shippingItem);
    }
    
}
