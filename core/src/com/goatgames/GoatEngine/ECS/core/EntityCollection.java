package com.goatgames.goatengine.ecs.core;

import com.badlogic.gdx.utils.Array;

/**
 * A Collection of entities. Offers method to filter out entities
 * based on their component or lack there of
 */
public class EntityCollection extends Array<Entity> {

    public EntityCollection() {
        super();
    }

    public EntityCollection(Array<Entity> entities) {
        super(entities);
    }

    public EntityCollection withoutComponent(String componentId) {
        EntityCollection collection = new EntityCollection();
        for (Entity e : this) {
            if (!e.hasComponent(componentId)) {
                collection.add(e);
            }
        }
        return collection;
    }

    public EntityCollection withComponent(String componentId) {
        EntityCollection collection = new EntityCollection();
        for (Entity e : this) {
            if (e.hasComponent(componentId)) {
                collection.add(e);
            }
        }
        return collection;
    }


    public boolean contains(Entity entity) {
        if (entity == null) return false;
        for (int i = 0; i < size; i++) {
            Entity other = get(i);
            if (other.getId().equals(entity.getId()))
                return true;
        }
        return false;
    }


    public void remove(Entity entity) {
        if (entity == null) return;
        for (int i = 0; i < size; i++) {
            Entity other = get(i);
            if (other.getId().equals(entity.getId())) {
                removeIndex(i);
                break;
            }
        }
    }

    /**
     * Removes the entity contained in provided collection from the current collection
     *
     * @param collection
     */
    public void remove(EntityCollection collection) {
        final int otherSize = collection.size;
        for (int i = 0; i < otherSize; i++) {
            Entity entity = get(i);
            if (contains(entity)) {
                remove(entity);
            }
        }
    }
}
