package com.goatgames.goatengine.input.events.touch;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Called when the user drags a finger over the screen.
 */
public class PanEvent extends InputEvent {

    public final float x;
    public final float y;
    public final float deltaX;
    public final float deltaY;

    public PanEvent(float x, float y, float deltaX, float deltaY) {
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }
}
