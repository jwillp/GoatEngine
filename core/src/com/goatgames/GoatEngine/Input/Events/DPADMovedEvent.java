package com.goatgames.goatengine.input.events;

import com.badlogic.gdx.controllers.PovDirection;

/**
 * When dpad is moved
 */
public class DPADMovedEvent extends InputEvent{
    public final int gamePadId;
    public final int rawCode;
    public final PovDirection value;

    public DPADMovedEvent(int gamePadId, int rawCode, PovDirection value) {
        this.gamePadId = gamePadId;
        this.rawCode = rawCode;
        this.value = value;
    }
}
