package com.bluestone.app.goldMineOrders.event;

import com.bluestone.app.core.event.Event;
import com.bluestone.app.core.event.RaiseEvent;

public class GoldMineEvent extends Event {

	private static final long serialVersionUID = 1L;

	public GoldMineEvent(RaiseEvent source) {
		super(source);
	}

}
