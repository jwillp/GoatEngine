package com.goatgames.goatengine.input.Events;

import com.goatgames.goatengine.eventmanager.GameEvent;

/**
 * Triggered when a Key is pressed
 */
public class KeyPressedEvent extends GameEvent {

    private int key;

    public KeyPressedEvent(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
