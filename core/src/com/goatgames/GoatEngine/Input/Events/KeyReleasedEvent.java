package com.goatgames.goatengine.input.events;

/**
 * Triggered when a key is released
 */
public class KeyReleasedEvent extends InputEvent {

    private int key;

    public KeyReleasedEvent(int key){
        this. key = key;
    }

    public int getKey() {
        return key;
    }
}
