package com.brm.GoatEngine.Input.Events;

import com.brm.GoatEngine.EventManager.GameEvent;

/**
 * Fire when a Mouse is clicked
 */
public class MouseClickEvent extends GameEvent {
    private final int screenX;
    private final int screenY;
    private final int button;

    public MouseClickEvent(int screenX, int screenY, int button) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
    }
}
