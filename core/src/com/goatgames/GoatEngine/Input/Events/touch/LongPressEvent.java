package com.goatgames.goatengine.input.events.touch;

/**
 * Touch Screen Long Press.
 */
public class LongPressEvent extends TouchEvent {
    public final float x;
    public final float y;

    public LongPressEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
