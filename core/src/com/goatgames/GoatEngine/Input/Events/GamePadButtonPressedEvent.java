package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.input.GamePadMap;

/**
 * Sent when an Game pad button is pressed
 */
public class GamePadButtonPressedEvent extends InputEvent {

    public final int controllerId;
    public final GamePadMap.Button button;
    public final int rawCode;

    public GamePadButtonPressedEvent(int controllerId, GamePadMap.Button button, int rawCode){

        this.controllerId = controllerId;
        this.button = button;
        this.rawCode = rawCode;
    }
}
