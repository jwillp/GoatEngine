package com.goatgames.goatengine.desktop.input.events.keyboard;

import com.badlogic.gdx.Input;
import com.goatgames.goatengine.input.events.InputEvent;

/**
 * Triggered when a Key is pressed
 */
public class KeyPressedEvent extends KeyboardEvent {

    public final int key;
    public final String keyName;

    public KeyPressedEvent(final int key){
        this.key = key;
        keyName = Input.Keys.toString(key).toUpperCase();
    }
}
