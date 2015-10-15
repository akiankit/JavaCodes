package com.bluestone.app.shipping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.shipping.dao.ShippingDao;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.model.State;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class ShippingService {
    
    @Autowired
    private ShippingDao shippingDao;
    
    public List getLocalityDetails(Integer pincode) {
        return shippingDao.getDetailsByZipcode(pincode);
    }
    
    public List<State> getStateList() {
        return shippingDao.getStatesList();
    }
    
    public State getStateFromName(String stateName) {
        return shippingDao.getStateFromName(stateName);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void createAddress(Address newAddress) {
        shippingDao.create(newAddress);
    }
    
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Address updateAddress(Address address) {
        return shippingDao.update(address);
    }
    
    public Address getAddress(long addressId) {
        return shippingDao.find(Address.class, addressId, true);
    }
    
    public State getStateById(long stateId) {
        return shippingDao.find(State.class, stateId, true);
    }
    
    public Address getLatestShippingAddress(Customer customer) {
        return shippingDao.findLatestShippingAddress(customer);
    }
    
    public Address getLatestBillingAddress(Customer customer) {
        return shippingDao.findLatestBillingAddress(customer);
    }
    
}
