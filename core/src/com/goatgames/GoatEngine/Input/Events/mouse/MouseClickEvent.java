package com.goatgames.goatengine.input.events.mouse;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Fire when a Mouse is clicked
 */
public class MouseClickEvent extends InputEvent {
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
