package com.brm.GoatEngine.Input.Events;

import com.brm.GoatEngine.EventManager.GameEvent;

/**
 * Created by Home on 2015-11-22.
 */
public class MouseReleasedEvent extends GameEvent {
    public final int screenX;
    public final int screenY;
    public final int button;

    public MouseReleasedEvent(int screenX, int screenY, int button) {
        super();
        this.screenX = screenX;
        this.screenY = screenY;
        this.button = button;
    }
}
