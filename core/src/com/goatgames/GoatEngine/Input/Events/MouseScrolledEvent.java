package com.goatgames.goatengine.input.Events;

import com.goatgames.goatengine.eventmanager.GameEvent;

/**
 * Triggfered when mouse is scrolled
 */
public class MouseScrolledEvent extends GameEvent {

    public final int amount;

    public MouseScrolledEvent(int amount) {

        this.amount = amount;
    }
}
