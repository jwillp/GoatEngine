package com.goatgames.goatengine.input.events;

/**
 * Triggered when a controller was connected
 */
public class GamePadConnectedEvent extends InputEvent {
    public final int gamePadId;
    public GamePadConnectedEvent(int gamePadId) {
        this.gamePadId = gamePadId;
    }
}
