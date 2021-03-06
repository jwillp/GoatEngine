package com.goatgames.goatengine.desktop.input.events.mouse;

/**
 * Fire when a Mouse is clicked
 */
public class MouseClickEvent extends MouseEvent {
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
