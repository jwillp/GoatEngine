package com.goatgames.goatengine.input.events;

import com.badlogic.gdx.Input;

/**
 * Triggered when a key is released
 */
public class KeyReleasedEvent extends InputEvent {

    public final int key;
    public final String keyName;

    public KeyReleasedEvent(int key){
        this. key = key;
        keyName = Input.Keys.toString(key).toUpperCase();
    }
}
