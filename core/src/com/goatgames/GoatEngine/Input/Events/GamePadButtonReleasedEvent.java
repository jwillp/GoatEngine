package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.input.GamePadMap;

/**
 * Whena button is released
 */
public class GamePadButtonReleasedEvent extends InputEvent {

    private final int gamePadId;
    private final GamePadMap.Button button;
    private final int buttonCode;

    public GamePadButtonReleasedEvent(int gamePadId, GamePadMap.Button button, int buttonCode) {
        this.gamePadId = gamePadId;
        this.button = button;
        this.buttonCode = buttonCode;
    }
}
