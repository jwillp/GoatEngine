package com.goatgames.goatengine.leveleditor.Events;

import com.goatgames.goatengine.eventmanager.GameEvent;

/**
 * Triggered when the user wnats to remove a component from an entity
 */
public class RemoveComponentEvent extends GameEvent {

    String componentId;
    String entityId;

    public RemoveComponentEvent(String componentId,String entityId){
        this.componentId = componentId;
        this.entityId = entityId;
    }

}
