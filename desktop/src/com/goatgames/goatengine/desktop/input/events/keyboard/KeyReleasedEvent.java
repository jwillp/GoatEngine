package com.goatgames.goatengine.desktop.input.events.keyboard;

import com.badlogic.gdx.Input;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered when a key is released
 */
public class KeyReleasedEvent extends KeyboardEvent {

    public final int key;
    public final String keyName;

    public KeyReleasedEvent(int key){
        this. key = key;
        keyName = Input.Keys.toString(key).toUpperCase();
    }
}
