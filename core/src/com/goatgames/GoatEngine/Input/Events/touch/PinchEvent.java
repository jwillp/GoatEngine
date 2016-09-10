package com.goatgames.goatengine.input.events.touch;

import com.badlogic.gdx.math.Vector2;

/**
 * Called when a user performs a pinch zoom gesture. Reports the initial positions of the two involved fingers and their
 * current positions.
 */
public class PinchEvent extends TouchEvent {
    public final Vector2 initialPointer1;
    public final Vector2 initialPointer2;
    public final Vector2 pointer1;
    public final Vector2 pointer2;

    public PinchEvent(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {

        this.initialPointer1 = initialPointer1;
        this.initialPointer2 = initialPointer2;
        this.pointer1 = pointer1;
        this.pointer2 = pointer2;
    }
}
