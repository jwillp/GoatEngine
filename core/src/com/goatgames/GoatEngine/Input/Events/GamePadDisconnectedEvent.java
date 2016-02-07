package com.goatgames.goatengine.input.events;

/**
 * Triggered when a controller was disconnected
 */
public class GamePadDisconnectedEvent extends InputEvent {

    private final int gamePadId;

    public GamePadDisconnectedEvent(int gamePadId) {
        this.gamePadId = gamePadId;
    }
}
