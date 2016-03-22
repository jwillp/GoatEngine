package com.goatgames.goatengine.input.events.mouse;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered when a mouse button is released
 */
public class MouseReleasedEvent extends InputEvent {
    public final int screenX;
    public final int screenY;
    public final int button;

    public MouseReleasedEvent(int screenX, int screenY, int button) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
    }
}
