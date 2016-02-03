package com.goatgames.goatengine.input.events;

import com.goatgames.goatengine.eventmanager.EntityEvent;

/**
 * Fired when an entity is released
 */
public class EntityReleasedEvent extends EntityEvent {
    public EntityReleasedEvent(String entityId) {
        super(entityId);
    }
}
