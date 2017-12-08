package com.bluestone.app.checkout.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bluestone.app.core.MessageContext;
import com.bluestone.app.core.event.RaiseEvent;
import com.bluestone.app.core.event.listeners.EventListener;
import com.bluestone.app.integration.crm.CRMMessage.CRM_EVENT;
import com.bluestone.app.integration.crm.CRMService;

@Component
public class CartEventListener extends EventListener<CartEvent> {

    private static final Logger log = LoggerFactory.getLogger(CartEventListener.class);

	@Autowired
	private CRMService crmService;

	@Override
	protected void handleSyncEvent(CartEvent event) {
		RaiseEvent raiseEvent = event.getRaiseEvent();
        MessageContext messageContext = event.getMessageContext();
        Long cartId = (Long) messageContext.get(MessageContext.CART_ID);
        log.debug("CartEventListener.handleSyncEvent(): {} cartId={} , MessageContext Id={}",
                  raiseEvent.eventType(),
                  cartId, messageContext.getId());

        if(cartId != null) {
		    switch (raiseEvent.eventType()) {
	            case CART_CREATED:
	                // crmService.send(CRM_EVENT.CART_CREATED_EVENT, messageContext.getMessageContextMap());
	                // vikas: commented out cart creation event as this is not required by CRM
	                break;

                case CART_UPDATED:
	                crmService.send(CRM_EVENT.CART_UPDATED_EVENT, messageContext.getMessageContextMap());
	                break;
	            default:
	                break;
	        }
		} else {
           log.warn("*******CartEventListener.handleSyncEvent(): CartId is null for event {} , It should not happen so. ******Please investigate*******.", raiseEvent.eventType().name());
        }
	}

    @Override
	protected void handleAsyncEvent(CartEvent event) {

	}

}
