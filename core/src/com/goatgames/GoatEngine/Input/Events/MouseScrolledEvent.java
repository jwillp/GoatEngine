package com.goatgames.goatengine.input.events;

/**
 * Triggfered when mouse is scrolled
 */
public class MouseScrolledEvent extends InputEvent {

    public final int amount;

    public MouseScrolledEvent(int amount) {

        this.amount = amount;
    }
}
