package com.goatgames.goatengine.desktop.input.events.mouse;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered every time the mouse is Dragged from one
 * position to another
 */
public class MouseDragEvent extends MouseEvent {

    public final int screenX;
    public final int screenY;
    public final int lastScreenX;
    public final int lastScreenY;
    public int button;

    public MouseDragEvent(int button, int screenX, int posY, float lastScreenX, float lastScreenY) {
        this.screenX = screenX;
        this.screenY = posY;
        this.lastScreenX = (int) lastScreenX;
        this.lastScreenY = (int) lastScreenY;
    }
}
