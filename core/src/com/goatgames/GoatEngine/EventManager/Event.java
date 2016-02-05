package com.goatgames.goatengine.eventmanager;

/**
 * Base class for all events
 * If an Event directly inherits from this class, it will only be visible by
 * the engine internally. The entity system and thus scripts won't be notified by these kinds of events.
 * ( Unless script instance inherits from GameEventListener )
 * They could therefore be considered as EngineEvents
 */
public abstract class Event {

    public <T extends Event> boolean isOfType(Class<T> type){
        return this.getClass() == type;
    }

    public boolean isOfType(final String typeName){
        return this.getClass().getSimpleName().equals(typeName);
    }


}