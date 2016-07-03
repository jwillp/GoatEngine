package com.goatgames.goatengine.desktop.input.events.gamepad;

import com.goatgames.goatengine.desktop.input.GamePadMap;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Fired when one of the analog stick is moved
 */
public class GamePadAxisMoved extends InputEvent {

    public final String controllerId;
    public final GamePadMap.Button axisCode;
    public final float value;

    public GamePadAxisMoved(String controllerId, GamePadMap.Button axisCode, float value){
        this.controllerId = controllerId;
        this.axisCode = axisCode;
        this.value = value;
    }
}
