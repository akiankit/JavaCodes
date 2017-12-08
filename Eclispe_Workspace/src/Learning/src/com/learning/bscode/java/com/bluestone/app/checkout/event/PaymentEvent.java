package com.bluestone.app.checkout.event;

import com.bluestone.app.core.event.Event;
import com.bluestone.app.core.event.RaiseEvent;

public class PaymentEvent extends Event {
    
    private static final long serialVersionUID = 1L;

    public PaymentEvent(RaiseEvent source) {
        super(source);
    }
}
