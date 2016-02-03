package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.input.GamePadMap;

/**
 * Sent when an Game pad button is pressed
 */
public class GamePadButtonPressedEvent extends Event {

    private final String controllerId;
    private final GamePadMap.Button button;
    private final int rawCode;

    public GamePadButtonPressedEvent(String controllerId, GamePadMap.Button button, int rawCode){

        this.controllerId = controllerId;
        this.button = button;
        this.rawCode = rawCode;
    }
}
