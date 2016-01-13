package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Triggered when a Key is pressed
 */
public class KeyPressedEvent extends Event {

    private int key;

    public KeyPressedEvent(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
