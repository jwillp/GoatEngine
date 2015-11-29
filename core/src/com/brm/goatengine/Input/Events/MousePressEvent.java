package com.brm.GoatEngine.Input.Events;

import com.brm.GoatEngine.EventManager.GameEvent;

/**
 * Fired when mouse is pressed
 */
public class MousePressEvent extends GameEvent {
    private final int screenX;
    private final int screenY;
    private final int button;

    public MousePressEvent(int screenX, int screenY, int button) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
    }
}