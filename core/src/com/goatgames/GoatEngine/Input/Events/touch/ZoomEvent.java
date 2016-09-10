package com.goatgames.goatengine.input.events.touch;

/**
 * Called when the user performs a pinch zoom gesture.
 * The original distance is the distance in pixels when the gesture
 * started.
 */
public class ZoomEvent extends TouchEvent {

    public final float initialDistance;
    public final float distance;

    public ZoomEvent(float initialDistance, float distance) {

        this.initialDistance = initialDistance;
        this.distance = distance;
    }
}
