package com.goatgames.goatengine.input;

import com.goatgames.goatengine.eventmanager.EntityEvent;

/**
 * Fired when an entity is touched
 */
public class EntityTouchedEvent extends EntityEvent {

    protected EntityTouchedEvent(String entityId) {
        super(entityId);
    }
}
