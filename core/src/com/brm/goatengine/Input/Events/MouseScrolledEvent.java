package com.brm.GoatEngine.Input.Events;

import com.brm.GoatEngine.EventManager.GameEvent;

/**
 * Triggfered when mouse is scrolled
 */
public class MouseScrolledEvent extends GameEvent {

    public final int amount;

    public MouseScrolledEvent(int amount) {

        this.amount = amount;
    }
}
