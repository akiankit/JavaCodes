package com.bluestone.app.integration.uniware;

import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bluestone.app.checkout.cart.model.CartItem;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;

/**
 * @author Rahul Agrawal
 *         Date: 3/28/13
 */
@Service
class FixedFlexibleProcessor {

    private static final Logger log = LoggerFactory.getLogger(FixedFlexibleProcessor.class);

    void execute(Order order) {
        log.debug("FixedFlexibleProcessor.execute(): OrderId=[{}]", order.getId());
        setPacketNumber(order);
        log.info("FixedFlexibleProcessor.execute for OrderId=[{}] resulted in:", order.getId());
        List<OrderItem> orderItems = order.getOrderItems();
        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);
            log.info("OrderItemId=[{}] - CartItemId=[{}] - PacketNumber=[{}]", orderItem.getId(), orderItem.getCartItem().getId(), orderItem.getPacketNumber());
        }
    }

    private void setPacketNumber(final Order order) {
        log.info("FixedFlexibleProcessor.setPacketNumber(): OrderId=[{}]", order.getId());
        List<OrderItem> orderItems = order.getOrderItems();
        if (log.isDebugEnabled()) {
            for (OrderItem orderItem : orderItems) {
                log.debug("{}", orderItem);
            }
        }
        ImmutableListMultimap<Long, OrderItem> groupByRoot = Multimaps.index(orderItems, groupOrderItemsByParent);
        ImmutableSet<Long> allRoots = groupByRoot.keySet();

        int packetNumber = 1;
        for (Long eachParentCartItemId : allRoots) {
            log.debug("FixedFlexibleProcessor.groupByBigFamily(): *** Root CartItemId=[{}]", eachParentCartItemId);
            ImmutableList<OrderItem> eachFixedGroup = groupByRoot.get(eachParentCartItemId);
            for (OrderItem eachMember : eachFixedGroup) {
                CartItem cartItem = eachMember.getCartItem();
                int quantity = cartItem.getQuantity();
                if (eachFixedGroup.size() == quantity) {
                    eachMember.setPacketNumber(0);
                } else {
                    eachMember.setPacketNumber(packetNumber);
                }
                log.debug("EachMember: Packet=[{}] OrderItemId=[{}] CartItemId=[{}]", eachMember.getPacketNumber(), eachMember.getId(), cartItem.getId());
            }
            packetNumber = packetNumber + 1;
        }
    }

    private CartItem getRootParent(CartItem cartItem) {
        CartItem anchorItem = null;
        CartItem parentCartItem = cartItem.getParentCartItem();
        if (parentCartItem == null) {
            anchorItem = cartItem;
        } else {
            anchorItem = getRootParent(parentCartItem);
        }
        log.debug("FixedFlexibleProcessor.getRootParent(): CartItemId=[{}] ParentCartId=[{}]", cartItem.getId(), anchorItem.getId());
        return anchorItem;
    }


    private final Function<OrderItem, Long> groupOrderItemsByParent = new Function<OrderItem, Long>() {
        @Override
        public Long apply(OrderItem orderItem) {
            CartItem cartItem = orderItem.getCartItem();
            CartItem anchorCartItem = getRootParent(cartItem);
            log.info("Function().OrderItemsByParent: OrderItemId=[{}] \nProductType=[{}] \nCartItemId=[{}] \nAnchorParentCartItemId=[{}] \nSku=[{}]",
                     orderItem.getId(), anchorCartItem.getProduct().getProductType(),
                     cartItem.getId(), anchorCartItem.getId(), cartItem.getProduct().getSkuCode());

            return anchorCartItem.getId();
        }
    };

}