package com.goatgames.goatengine.desktop.input.events.mouse;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggfered when mouse is scrolled
 */
public class MouseScrolledEvent extends MouseEvent {

    public final int amount;

    public MouseScrolledEvent(int amount) {

        this.amount = amount;
    }
}
