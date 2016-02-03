package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.EntityEvent;

/**
 * Fired when an entity is touched
 */
public class EntityTouchedEvent extends EntityEvent {

    public EntityTouchedEvent(String entityId) {
        super(entityId);
    }
}
