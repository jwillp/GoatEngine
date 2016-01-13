package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Triggfered when mouse is scrolled
 */
public class MouseScrolledEvent extends Event {

    public final int amount;

    public MouseScrolledEvent(int amount) {

        this.amount = amount;
    }
}
