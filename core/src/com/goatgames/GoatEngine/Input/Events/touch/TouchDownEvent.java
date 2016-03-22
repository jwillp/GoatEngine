package com.goatgames.goatengine.input.events.touch;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Touch screen. Touch down
 */
public class TouchDownEvent extends InputEvent {

    public final float x;
    public final float y;

    public TouchDownEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
