package com.goatgames.goatengine.input.events.touch;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Called when the user performs a pinch zoom gesture.
 * The original distance is the distance in pixels when the gesture
 * started.
 */
public class ZoomEvent extends InputEvent {

    public final float initialDistance;
    public final float distance;

    public ZoomEvent(float initialDistance, float distance) {

        this.initialDistance = initialDistance;
        this.distance = distance;
    }
}
