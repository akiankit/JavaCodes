package com.bluestone.app.uniware;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.unicommerce.uniware.services.SaleOrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bluestone.app.design.model.Customization;
import com.bluestone.app.design.model.DesignCategory;
import com.bluestone.app.design.model.product.Product;
import com.bluestone.app.order.model.Order;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.voucher.model.DiscountVoucher;

class SaleOrderItemFactory {

    private static final Logger log = LoggerFactory.getLogger(SaleOrderItemFactory.class);

    static List<SaleOrderItem> createOrderItems(Order blueStoneOrder) {
        log.debug("SaleOrderItemFactory.createOrderItems(): Creating Uniware SaleOrderItems for OrderId={}", blueStoneOrder.getId());
        List<OrderItem> bsOrderItems = blueStoneOrder.getOrderItems();
        List<SaleOrderItem> saleOrderItemList = new LinkedList<SaleOrderItem>();

        DiscountVoucher discountVoucher = blueStoneOrder.getDiscountVoucher();
        String discountVoucherName = "";
        if (discountVoucher != null) {
            discountVoucherName = discountVoucher.getName();
        }
        for (OrderItem blueStoneOrderItem : bsOrderItems) {
            SaleOrderItemBuilder itemBuilder = new SaleOrderItemBuilder();

            itemBuilder.setCode(Long.toString(Long.parseLong(blueStoneOrderItem.getUniwareOrderItemId())));
            itemBuilder.setItemSKU(getSkuCode(blueStoneOrderItem));
            itemBuilder.setVoucherCode(discountVoucherName);

            // spreading of discount takes care of putting the right discount. At anytime, the selling price and total price has to be same
            // and the Selling price and the total price are the ones after the discount has been applied.
            itemBuilder.setDiscount(new BigDecimal(0.0));
            final BigDecimal MRP = blueStoneOrderItem.getPrice();
            itemBuilder.setTotalPrice(MRP);
            itemBuilder.setSellingPrice(MRP);
            itemBuilder.setOnHold(isOrderItemOnHold(blueStoneOrderItem));
            itemBuilder.setPacketNumber(getPacketNumber(blueStoneOrderItem));
            itemBuilder.setShippingAddress(AddressBuilder.createUniwareAddress(blueStoneOrderItem.getShippingAddress()));
            SaleOrderItem uniwareSaleOrderItem = itemBuilder.create();
            saleOrderItemList.add(uniwareSaleOrderItem);
        }
        DiscountProcessor.spreadDiscountToItems(blueStoneOrder, saleOrderItemList);
        return saleOrderItemList;
    }

    private static boolean isOrderItemOnHold(OrderItem blueStoneOrderItem) {
        boolean isOnHold = false;
        Product.PRODUCT_TYPE productType = blueStoneOrderItem.getProductType();
        if (Product.PRODUCT_TYPE.JEWELLERY.equals(productType)) {
            Customization customization = (Customization) blueStoneOrderItem.getCartItem().getProduct();
            final DesignCategory designCategory = customization.getDesign().getDesignCategory();
            if (designCategory.isBangles() || designCategory.isRings()) {
                isOnHold = true;
            }
        }
        return isOnHold;
    }

    private static BigInteger getPacketNumber(OrderItem blueStoneOrderItems) {
        BigInteger packetNumber = BigInteger.valueOf(blueStoneOrderItems.getPacketNumber());
        return packetNumber;
    }

    private static String getSkuCode(OrderItem blueStoneOrderItems) {
        String skuCode = "";
        final Product.PRODUCT_TYPE productType = blueStoneOrderItems.getProductType();

        switch (productType) {
            case JEWELLERY:
                skuCode = blueStoneOrderItems.getCartItem().getSkuCodeWithSize();
                break;

            case SOLITAIRE:
                skuCode = SolitaireProcessor.SOLITAIRE_FIXED_SKUCODE;
                break;

            default:
                log.error("Error : SaleOrderItemFactory.getSkuCode(): ProductType={} not supported in Uniware", productType.name());
                throw new RuntimeException("ProductType=" + productType.name() + " is not supported in uniware");

        }
        return skuCode;
    }
}