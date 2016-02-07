package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.input.GamePadMap;

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
