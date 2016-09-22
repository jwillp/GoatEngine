package com.goatgames.goatengine.eventmanager.engineevents;

/**
 * Triggered when the screen is resized
 */
public class ScreenResizedEvent extends EngineEvent {
    public final int newWidth;
    public final int newHeight;

    public ScreenResizedEvent(int newWidth, int newHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
    }
}

