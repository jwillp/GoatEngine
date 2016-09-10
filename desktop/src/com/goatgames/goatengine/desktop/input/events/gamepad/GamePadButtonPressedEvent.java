package com.goatgames.goatengine.desktop.input.events.gamepad;

import com.goatgames.goatengine.desktop.input.GamePadMap;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Sent when an Game pad button is pressed
 */
public class GamePadButtonPressedEvent extends GamePadInputEvent {

    public final int controllerId;
    public final GamePadMap.Button button;
    public final int rawCode;

    public GamePadButtonPressedEvent(int controllerId, GamePadMap.Button button, int rawCode){

        this.controllerId = controllerId;
        this.button = button;
        this.rawCode = rawCode;
    }
}
