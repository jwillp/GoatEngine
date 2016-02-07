package com.goatgames.goatengine.input.events;

/**
 * Triggered when a Key is pressed
 */
public class KeyPressedEvent extends InputEvent {

    private int key;

    public KeyPressedEvent(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
