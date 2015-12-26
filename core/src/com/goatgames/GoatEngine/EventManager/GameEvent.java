package com.goatgames.goatengine.eventmanager;

/**
 * Base class for all events
 * If an Event directly inherits from this class, it will only be visible by
 * the engine internally. The entity system ad thus scripts wnot be notified by these kinds of events.
 * They could therefore be considered as EngineEvents
 */
public abstract class GameEvent{

    public <T extends GameEvent> boolean isOfType(Class<T> type){
        return this.getClass() == type;
    }

}
