package com.bluestone.app.account.event;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bluestone.app.account.VisitorService;
import com.bluestone.app.account.model.Customer;
import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.EventType;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.event.listeners.EventListener;
import com.bluestone.app.core.util.Constants;
import com.bluestone.app.core.util.SessionUtils;
import com.bluestone.app.core.util.Util;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.integration.crm.CRMService;

@Component
public class CustomerEventListener extends EventListener<CustomerEvent> {
    
    private static final Logger log = LoggerFactory.getLogger(CustomerEventListener.class);
    
    @Autowired
    private VisitorService      visitorService;
    
    @Autowired
    public CRMService           crmService;
    
    @Override
    protected void handleSyncEvent(CustomerEvent event) {
        RaiseEvent raiseEvent = event.getRaiseEvent();
        EventType eventType = raiseEvent.eventType();
        MessageContext messageContext = Util.getMessageContext();
        Long customerId = (Long) messageContext.get(MessageContext.CUSTOMERID);
        Long visitorId = (Long) messageContext.get(MessageContext.VISITOR_ID);
        switch (eventType) {
            case CUSTOMER_LOGIN:
                if (SecurityUtils.getSubject().isAuthenticated()) {
                    Customer authenticatedCustomer = SessionUtils.getAuthenticatedCustomer();
                    if(authenticatedCustomer != null) {
                       assignCustomerToVisitor(visitorId, authenticatedCustomer.getId());
                    }
                }
                break;
            case CUSTOMER_REGISTERED:
                crmService.send(CRM_EVENT.CUSTOMER_REGISTERED, messageContext.getMessageContextMap());
                assignCustomerToVisitor(visitorId, customerId);
                break;
            default:
                break;
        }
    }
    
    private void assignCustomerToVisitor(long visitorId, Long customerId) {
        if (visitorId != Constants.CRM_VISITOR_ID) { //dont assign a customer to visitor, if the visitor is CRM.
            visitorService.assignCustomerToVisitor(visitorId, customerId);
        }
    }
    
    @Override
    @Async
    protected void handleAsyncEvent(CustomerEvent event) {
    }

}
