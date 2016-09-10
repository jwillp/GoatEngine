package com.goatgames.goatengine.input.events.touch;

/**
 * Screen tap event A tap happens if a touch went down on the screen and was lifted again without moving outside
 * of the tap square.
 */
public class TapEvent extends TouchEvent {

    public final float x;
    public final float y;
    public final int count;

    public TapEvent(float x, float y, int count) {
        this.x = x;
        this.y = y;
        this.count = count;
    }
}
