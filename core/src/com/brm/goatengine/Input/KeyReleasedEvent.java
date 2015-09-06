package com.brm.GoatEngine.Input;

import com.brm.GoatEngine.EventManager.GameEvent;

/**
 * Triggered when a key is released
 */
public class KeyReleasedEvent extends GameEvent {

    private int key;

    public KeyReleasedEvent(int key){
        this. key = key;
    }

    public int getKey() {
        return key;
    }
}
