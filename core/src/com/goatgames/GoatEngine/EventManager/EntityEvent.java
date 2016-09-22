package com.goatgames.goatengine.eventmanager;

import com.goatgames.gdk.eventdispatcher.Event;

/**
 * Entity Events used by Entity systems for intercommunication
 * Events mostly related to entities
 */
public abstract class EntityEvent extends Event {

    /**
     * The entity to which the event mostly applies (can be null for some special cases)
     */
    private final String entityId;

    protected EntityEvent(String entityId){
        this.entityId = entityId;
    }

    /**
     * Returns the id of the entity this events concerns
     * @return String id of the concerned entity
     */
    public String getEntityId() {
        return entityId;
    }
}
