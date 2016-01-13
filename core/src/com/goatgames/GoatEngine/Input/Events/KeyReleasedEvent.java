package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.Event;

/**
 * Triggered when a key is released
 */
public class KeyReleasedEvent extends Event {

    private int key;

    public KeyReleasedEvent(int key){
        this. key = key;
    }

    public int getKey() {
        return key;
    }
}
