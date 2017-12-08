package com.bluestone.app.order.dao;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.ShippingItem;
import com.bluestone.app.order.model.ShippingPacket;
import com.bluestone.app.order.model.ShippingPacketStatus;

@Repository("orderShipmentDao")
public class OrderShipmentDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(OrderShipmentDao.class);
    
    public ShippingItem getShippingItemFromOrderItemId(Long orderItemId) {
        try {
            Query query = getEntityManagerForActiveEntities().createNamedQuery("ShippingItem.findShippingItemFromOrderItem");
            query.setParameter("orderItem", find(OrderItem.class, orderItemId, true));
            ShippingItem shippingItem = (ShippingItem) query.getSingleResult();
            return shippingItem;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ShippingPacket getShippingPacket(String shippingPackageCode) {
        try {
            Query query = getEntityManagerForActiveEntities().createNamedQuery("ShippingPacket.findFromShippingPackageCode");
            query.setParameter("shipmentCode", shippingPackageCode);
            ShippingPacket shippingPacket = (ShippingPacket) query.getSingleResult();
            return shippingPacket;
        } catch (NoResultException e) {
            return null;
        }
    }

    public ShippingPacketStatus getShippingPacketStatus(String shippingPacketStatusName) {
        try {
            Query query = getEntityManagerForActiveEntities().createNamedQuery("shippingPacketStatus.getShippingPacketStatus");
            query.setParameter("shippingPacketStatus", shippingPacketStatusName);
            ShippingPacketStatus shippingPacketStatus = (ShippingPacketStatus) query.getSingleResult();
            return shippingPacketStatus;
        } catch (NoResultException e) {
            return null;
        }
    }
}
