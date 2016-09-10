package com.goatgames.goatengine.desktop.input.events.gamepad;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered when a controller was connected
 */
public class GamePadConnectedEvent extends GamePadInputEvent {
    public final int gamePadId;
    public GamePadConnectedEvent(int gamePadId) {
        this.gamePadId = gamePadId;
    }
}
