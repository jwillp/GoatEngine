package com.goatgames.goatengine.input.events.touch;

/**
 *  Called when no longer panning.
 */
public class PanStoppedEvent extends TouchEvent {

    public final float x;
    public final float y;

    public PanStoppedEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
