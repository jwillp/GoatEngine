package com.goatgames.goatengine.eventmanager;

/**
 * All classes that need to listen to GameEvents
 * should implement this interface
 */
public interface GameEventListener{

    void onEvent(Event e);
}
