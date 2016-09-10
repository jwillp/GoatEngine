package com.goatgames.goatengine.desktop.input.events.gamepad;

import com.badlogic.gdx.controllers.PovDirection;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * When dpad is moved
 */
public class DPADMovedEvent extends GamePadInputEvent {
    public final int gamePadId;
    public final int rawCode;
    public final PovDirection value;

    public DPADMovedEvent(int gamePadId, int rawCode, PovDirection value) {
        this.gamePadId = gamePadId;
        this.rawCode = rawCode;
        this.value = value;
    }
}
