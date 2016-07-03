package com.goatgames.goatengine.desktop.input.events.mouse;

import com.goatgames.goatengine.input.events.InputClickPressEvent;

/**
 * Triggered when a mouse button is released
 */
public class MouseReleasedEvent extends InputClickPressEvent {
    public final int button;

    public MouseReleasedEvent(int screenX, int screenY, int button) {
        super(screenX, screenY);
        this.button = button;
    }
}
