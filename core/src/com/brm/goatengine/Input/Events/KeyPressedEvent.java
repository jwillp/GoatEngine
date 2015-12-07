package com.brm.GoatEngine.Input.Events;

import com.brm.GoatEngine.EventManager.GameEvent;

/**
 * Triggered when a Key is pressed
 */
public class KeyPressedEvent extends GameEvent{

    private int key;

    public KeyPressedEvent(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
