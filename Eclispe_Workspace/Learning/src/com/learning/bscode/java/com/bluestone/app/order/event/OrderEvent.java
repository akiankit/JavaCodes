package com.bluestone.app.order.event;

import com.bluestone.app.core.event.Event;
import com.bluestone.app.core.event.RaiseEvent;

public class OrderEvent extends Event {

    private static final long serialVersionUID = -5011180679053388013L;

    public OrderEvent(RaiseEvent source) {
        super(source);
    }
}
