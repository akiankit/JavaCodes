package com.bluestone.app.shipping.dao;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.dao.BaseDao;
import com.bluestone.app.order.model.OrderItem;
import com.bluestone.app.order.model.ShippingItem;
import com.bluestone.app.shipping.model.Address;
import com.bluestone.app.shipping.model.Locality;
import com.bluestone.app.shipping.model.State;

@Repository("shippingDao")
public class ShippingDao extends BaseDao {
    
    private static final Logger log = LoggerFactory.getLogger(ShippingDao.class);
    
    public List<Locality> getDetailsByZipcode(Integer pincode) {
        
        Query getDetailsByZipcodeQuery = getEntityManagerForActiveEntities().createNamedQuery("locality.findDetailsByLocality");
        
        getDetailsByZipcodeQuery.setParameter("pincode", pincode);
        getDetailsByZipcodeQuery.setMaxResults(1);
        
        return getDetailsByZipcodeQuery.getResultList();
    }
   
    public State getStateFromName(String stateName) {
        try {
            Query query = getEntityManagerForActiveEntities().createNamedQuery("state.getStateFromName");
            query.setParameter("stateName", stateName); 
            State state = (State) query.getSingleResult();
            return state;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<State> getStatesList() {
        Query getStatesListQuery = getEntityManagerForActiveEntities().createNamedQuery("state.getStatesListQuery");
        return getStatesListQuery.getResultList();
    }
    
    public Address findLatestShippingAddress(Customer customer) {
        try {
            Query query = getEntityManagerForActiveEntities().createNamedQuery("Address.latestAddress");
            query.setParameter("customerId", customer.getId());
            query.setParameter("alias", "shipping");
            Address address = (Address) query.getSingleResult();
            return address;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public Address findLatestBillingAddress(Customer customer) {
        try {
            Query query = getEntityManagerForActiveEntities().createNamedQuery("Address.latestAddress");
            query.setParameter("customerId", customer.getId());
            query.setParameter("alias", "billing");
            Address address = (Address) query.getSingleResult();
            return address;
        } catch (NoResultException e) {
            return null;
        }
    }
    
}
