package com.goatgames.goatengine.input.events.touch;

/**
 * Touch screen. Touch down
 */
public class TouchDownEvent extends TouchEvent {

    public final float x;
    public final float y;

    public TouchDownEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
