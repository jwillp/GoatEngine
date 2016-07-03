package com.goatgames.goatengine.input.events;

/**
 * This represents a "Click Event". Since the way to click and select things on mobile and desktop is
 * not done in the same, way this event is supposed to represent both platforms. The "real" events are
 * located in the desktop and mobile modules, and derive from this event
 */
public class InputClickReleaseEvent extends InputEvent{
    public final float screenX;
    public final float screenY;

    public InputClickReleaseEvent(float screenX, float screenY) {
        this.screenX = screenX;
        this.screenY = screenY;
    }
}
