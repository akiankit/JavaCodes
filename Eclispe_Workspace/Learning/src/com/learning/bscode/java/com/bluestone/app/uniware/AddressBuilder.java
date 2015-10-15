package com.bluestone.app.uniware;

import com.unicommerce.uniware.services.Address;
import com.unicommerce.uniware.services.AddressRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AddressBuilder {

    private static final Logger log = LoggerFactory.getLogger(AddressBuilder.class);

    public static AddressRef createUniwareAddressRef(Address uniwareAddress) {
        log.trace("AddressBuilder.createUniwareAddressRef() for UniwareAddress Id={}", uniwareAddress.getId());
        return new AddressRef(uniwareAddress.getId());
    }

    static Address createUniwareAddress(com.bluestone.app.shipping.model.Address bluestoneAddress) {
        log.debug("AddressBuilder.createUniwareAddress() from BlueStone {}", bluestoneAddress);
        Address uniwareAddress = new Address();
        uniwareAddress.setId(Long.toString(bluestoneAddress.getId()));
        uniwareAddress.setName(bluestoneAddress.getFullname());
        uniwareAddress.setAddressLine1(bluestoneAddress.getAddress());
        uniwareAddress.setAddressLine2("");
        uniwareAddress.setCity(bluestoneAddress.getCityName());
        uniwareAddress.setState(bluestoneAddress.getState().getStateName());
        uniwareAddress.setCountry("IN");
        uniwareAddress.setPincode(bluestoneAddress.getPostCode());
        uniwareAddress.setPhone(bluestoneAddress.getContactNumber());
        log.debug("AddressBuilder.createUniwareAddress(): resulted in Uniware Address={}", uniwareAddress);
        return uniwareAddress;
    }


}