package com.goatgames.goatengine.input.events.gamepad;

import com.goatgames.goatengine.input.GamePadMap;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Whena button is released
 */
public class GamePadButtonReleasedEvent extends InputEvent {

    public final int gamePadId;
    public final GamePadMap.Button button;
    public final int buttonCode;

    public GamePadButtonReleasedEvent(int gamePadId, GamePadMap.Button button, int buttonCode) {
        this.gamePadId = gamePadId;
        this.button = button;
        this.buttonCode = buttonCode;
    }
}