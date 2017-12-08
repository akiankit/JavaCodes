package com.bluestone.app.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.model.Customer;
import com.bluestone.app.account.model.Visitor;
import com.bluestone.app.core.dao.BaseDao;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class VisitorService {

    private static final Logger log = LoggerFactory.getLogger(VisitorService.class);

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private CustomerService customerService;

    public Visitor getVisitor(long visitorId) {
        return baseDao.find(Visitor.class, visitorId, true);
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Visitor createVisitor(String clientIp, int tagId) {
        log.debug("VisitorService.createVisitor(): clientIp=[{}] tagId=[{}]", clientIp, tagId);
        Visitor visitor = new Visitor(clientIp, tagId);
        baseDao.create(visitor);
        return visitor;
    }

    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Visitor assignCustomerToVisitor(long visitorId, Long customerId) {
        log.debug("VisitorService.assignCustomerToVisitor(): VisitorId=[{}] CustomerId[{}]", visitorId, customerId);
        Visitor visitor = getVisitor(visitorId);
        if (visitor != null) {
            Customer customer = customerService.getCustomerById(customerId);
            visitor.setCustomer(customer);
            visitor = baseDao.update(visitor);
        }
        return visitor;
    }

}

