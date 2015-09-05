package com.brm.goatengine.EventManager;

/**
 * All classes that need to listen to events
 * should implement this interface
 */
public interface GameEventListener {

    public void onEvent(GameEvent e);
}
