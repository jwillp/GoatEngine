package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Fire when a Mouse is clicked
 */
public class MouseClickEvent extends Event {
    public final int screenX;
    public final int screenY;
    public final int button;

    public MouseClickEvent(int screenX, int screenY, int button) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
    }
}
