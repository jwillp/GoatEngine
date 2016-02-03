package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Triggered when a controller was disconnected
 */
public class GamePadDisconnectedEvent extends Event {

    private final int gamePadId;

    public GamePadDisconnectedEvent(int gamePadId) {
        this.gamePadId = gamePadId;
    }
}
