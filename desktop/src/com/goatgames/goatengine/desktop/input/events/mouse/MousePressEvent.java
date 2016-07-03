package com.goatgames.goatengine.desktop.input.events.mouse;

import com.goatgames.goatengine.input.events.InputClickPressEvent;

/**
 * Fired when mouse is pressed
 */
public class MousePressEvent extends InputClickPressEvent {
    public final int button;

    public MousePressEvent(int screenX, int screenY, int button) {
        super(screenX, screenY);
        this.button = button;
    }
}
