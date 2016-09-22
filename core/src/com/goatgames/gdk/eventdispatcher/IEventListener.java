package com.goatgames.gdk.eventdispatcher;

/**
 * All classes that need to listen to events
 * should implement this interface
 */
public interface IEventListener {

    /**
     * Handles an event
     * @param e event
     * @return true if the event should be consumed and not propagated, otherwise false
     */
    boolean onEvent(Event e);
}
