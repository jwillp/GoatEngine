package com.goatgames.goatengine.desktop.input.events.gamepad;

import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered when a controller was disconnected
 */
public class GamePadDisconnectedEvent extends GamePadInputEvent {

    public final int gamePadId;

    public GamePadDisconnectedEvent(int gamePadId) {
        this.gamePadId = gamePadId;
    }
}
