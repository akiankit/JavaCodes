package com.bluestone.app.account.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.dao.BaseDao;

@Repository("customerDao")
public class CustomerDao extends BaseDao {

    private static final Logger log = LoggerFactory.getLogger(CustomerDao.class);

    public Customer getCustomerFromEmail(String email) {
        Query getByEmailIdQuery = getEntityManagerForActiveEntities().createNamedQuery("Customer.findByEmailId");
        getByEmailIdQuery.setParameter("email", email);
        List<Customer> customerList = getByEmailIdQuery.getResultList();
        if (customerList.size() == 0) {
            return null;
        } else {
            return customerList.get(0);
        }
    }

    public Customer getCustomerByFacebookId(String facebookId) {
        Query getByFacebookIdQuery = getEntityManagerForActiveEntities().createNamedQuery("Customer.findByFacebookId");
        getByFacebookIdQuery.setParameter("socialId", facebookId);
        Customer customer = null;
        try {
            customer = (Customer) getByFacebookIdQuery.getSingleResult();
        } catch (NoResultException noResultException) {
            log.debug("Exception while executing CustomerDao.getCustomerByFacebookId():No user registered with FacebookId={}",
                      facebookId, Throwables.getRootCause(noResultException));
        }
        return customer;
    }

    public boolean activateAccountAndSetPassword(Customer customer, String hashedPassword) {
        boolean result = false;
        try {
            customer.setPassword(hashedPassword);
            customer.activateAccount();
            Customer updatedCustomer = update(customer);
            result = true;
        } catch (Exception e) {
            log.error("Error while activating the account for Customer=[{}] Reason={}", customer.getEmail(), e.getMessage(), Throwables.getRootCause(e));
            throw Throwables.propagate(e);
        }
        return result;
    }

    public Customer updatePassword(Customer customer, String hashedPassword) {
        try {
            customer.setPassword(hashedPassword);
            Date date = new Date();
            customer.setLastPasswdGen(new Timestamp(date.getTime()));
            Customer updatedEntity = update(customer);
            return updatedEntity;
        } catch (Exception e) {
            log.error("Error while updating password for customer=[{}] . Reason=[{}]", customer.getEmail(), e.getMessage(), Throwables.getRootCause(e));
            throw Throwables.propagate(e);
        }
    }
	
}
