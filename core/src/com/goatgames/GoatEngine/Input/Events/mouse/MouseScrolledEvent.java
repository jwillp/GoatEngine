package com.goatgames.goatengine.input.events.mouse;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggfered when mouse is scrolled
 */
public class MouseScrolledEvent extends InputEvent {

    public final int amount;

    public MouseScrolledEvent(int amount) {

        this.amount = amount;
    }
}
