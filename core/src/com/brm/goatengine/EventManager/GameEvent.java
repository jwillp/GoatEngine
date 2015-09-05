package com.brm.goatengine.EventManager;

/**
 * Base class for all events
 */
public abstract class GameEvent{
    /**
     * Returns the Id of the current event
     * @return id of the current event
     */
    public abstract String getId();
}