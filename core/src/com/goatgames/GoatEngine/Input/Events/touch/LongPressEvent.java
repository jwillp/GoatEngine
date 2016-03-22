package com.goatgames.goatengine.input.events.touch;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Touch Screen Long Press.
 */
public class LongPressEvent extends InputEvent {
    public final float x;
    public final float y;

    public LongPressEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
