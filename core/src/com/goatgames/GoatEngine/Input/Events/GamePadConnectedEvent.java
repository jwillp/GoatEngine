package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Triggered when a controller was connected
 */
public class GamePadConnectedEvent extends Event {
    public final int gamePadId;
    public GamePadConnectedEvent(int gamePadId) {
        this.gamePadId = gamePadId;
    }
}
