package com.goatgames.goatengine.desktop.input.events.gamepad;

import com.goatgames.goatengine.desktop.input.GamePadMap;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * When an axis is moved
 */
public class AxisMovedEvent extends InputEvent {
    public final int gamePadId;
    public final GamePadMap.Axis axis;
    public final float value;

    public AxisMovedEvent(int gamePadId, GamePadMap.Axis axis, float value) {
        super();
        this.gamePadId = gamePadId;
        this.axis = axis;
        this.value = value;
    }
}
