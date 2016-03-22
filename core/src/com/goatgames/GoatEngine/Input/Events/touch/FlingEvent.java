package com.goatgames.goatengine.input.events.touch;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Fired when the user dragged a finger over the screen and lifted it.
 */
public class FlingEvent extends InputEvent {

    public final float velocityX;
    public final float velocityY;

    public FlingEvent(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}
