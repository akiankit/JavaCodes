package com.bluestone.app.uniware;

import java.util.LinkedList;
import java.util.List;

import com.unicommerce.uniware.services.Address;
import com.unicommerce.uniware.services.AddressRef;
import com.unicommerce.uniware.services.SaleOrderAddress;
import com.unicommerce.uniware.services.SaleOrderAddressItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 9/28/12
 */
public class SaleOrderEditBuilder {
    private static final Logger log = LoggerFactory.getLogger(SaleOrderEditBuilder.class);

    static SaleOrderAddress createSaleOrderAddress(String saleOrderCode,
                                                   Address billingAddress,
                                                   Address shippingAddress,
                                                   SaleOrderAddressItem[] saleOrderAddressItems) {
        log.debug("SaleOrderEditBuilder.createSaleOrderAddress(): orderCode={}", saleOrderCode);
        SaleOrderAddress saleOrderAddress = new SaleOrderAddress();
        saleOrderAddress.setSaleOrderCode(saleOrderCode);
        List<Address> list = new LinkedList<Address>();

        if (billingAddress != null) {
            AddressRef billingAddressRef = AddressBuilder.createUniwareAddressRef(billingAddress);
            list.add(billingAddress);
            saleOrderAddress.setBillingAddress(billingAddressRef);
        }
        if (shippingAddress != null) {
            AddressRef shippingAddressRef = AddressBuilder.createUniwareAddressRef(shippingAddress);
            list.add(shippingAddress);
            saleOrderAddress.setShippingAddress(shippingAddressRef);
        }
        Address address[] = new Address[list.size()];
        for (int i = 0; i < list.size(); i++) {
            address[i] = list.get(i);
        }
        saleOrderAddress.setAddresses(address);
        if (saleOrderAddressItems != null) {
            saleOrderAddress.setSaleOrderAddressItems(saleOrderAddressItems);
        }
        return saleOrderAddress;
    }

    static SaleOrderAddressItem createSaleOrderAddressItem(String code, AddressRef shippingAddressRef) {
        log.debug("SaleOrderEditBuilder.createSaleOrderAddressItem(): OrderCode={} , shippingAddressRef={}", code, shippingAddressRef.getRef());
        return new SaleOrderAddressItem(code, shippingAddressRef);
    }


}
