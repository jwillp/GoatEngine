package com.goatgames.goatengine.eventmanager;

import com.goatgames.goatengine.eventmanager.engineevents.EngineEvent;

/**
 * Entity Events used by Entity systems for intercommunication
 * Events mostly related to entities
 */
public abstract class EntityEvent extends EngineEvent {

    private final String entityId;  //The entity to which the event mostly applies (can be null for some special events)


    protected EntityEvent(String entityId){
        this.entityId = entityId;
    }


    public String getEntityId() {
        return entityId;
    }




}
