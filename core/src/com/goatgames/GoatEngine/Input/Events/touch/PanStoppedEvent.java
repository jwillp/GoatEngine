package com.goatgames.goatengine.input.events.touch;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 *  Called when no longer panning.
 */
public class PanStoppedEvent extends InputEvent {

    public final float x;
    public final float y;

    public PanStoppedEvent(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
