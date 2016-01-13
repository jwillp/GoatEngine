package com.goatgames.goatengine.eventmanager;

/**
 * Entity Events used by Entity systems for intercommunication
 * Events mostly related to entities
 */
public abstract class EntityEvent extends Event {

    private final String entityId;  //The entity to which the event mostly applies (can be null for some special events)


    protected EntityEvent(String entityId){
        this.entityId = entityId;
    }


    public String getEntityId() {
        return entityId;
    }




}
