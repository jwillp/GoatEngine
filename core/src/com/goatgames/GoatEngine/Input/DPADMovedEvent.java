package com.goatgames.goatengine.input;

import com.badlogic.gdx.controllers.PovDirection;
import com.goatgames.goatengine.eventmanager.Event;

/**
 * When dpad is moved
 */
public class DPADMovedEvent extends Event{
    private final int gamePadId;
    private final int rawCode;
    private final PovDirection value;

    public DPADMovedEvent(int gamePadId, int rawCode, PovDirection value) {
        this.gamePadId = gamePadId;
        this.rawCode = rawCode;
        this.value = value;
    }
}
