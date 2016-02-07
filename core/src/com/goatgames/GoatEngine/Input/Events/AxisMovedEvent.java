package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.input.GamePadMap;

/**
 * When an axis is moved
 */
public class AxisMovedEvent extends InputEvent {
    private final int gamePadId;
    private final GamePadMap.Axis axis;
    private final float value;

    public AxisMovedEvent(int gamePadId, GamePadMap.Axis axis, float value) {
        super();
        this.gamePadId = gamePadId;
        this.axis = axis;
        this.value = value;
    }
}
