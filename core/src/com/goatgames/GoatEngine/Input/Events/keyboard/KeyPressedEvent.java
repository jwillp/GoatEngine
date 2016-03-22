package com.goatgames.goatengine.input.events.keyboard;

import com.badlogic.gdx.Input;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered when a Key is pressed
 */
public class KeyPressedEvent extends InputEvent {

    public int key;
    public final String keyName;

    public KeyPressedEvent(int key){
        this.key = key;
        keyName = Input.Keys.toString(key).toUpperCase();
    }
}
