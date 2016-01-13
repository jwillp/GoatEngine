package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Fired when mouse is pressed
 */
public class MousePressEvent extends Event {
    public final int screenX;
    public final int screenY;
    public final int button;

    public MousePressEvent(int screenX, int screenY, int button) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
    }
}
