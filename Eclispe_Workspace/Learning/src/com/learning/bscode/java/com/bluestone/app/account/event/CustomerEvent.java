package com.bluestone.app.account.event;

import com.bluestone.app.core.event.Event;
import com.bluestone.app.core.event.RaiseEvent;

public class CustomerEvent extends Event {
    
    public CustomerEvent(RaiseEvent source) {
        super(source);
    }

    private static final long serialVersionUID = 1L;

    
}
