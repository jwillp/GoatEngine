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

    /**
     * Filters out entities with a certain component
     *
     * @param componentId the component filter
     * @return a new collection using this collection as a base
     */
    public EntityCollection withoutComponent(String componentId) {
        EntityCollection collection = new EntityCollection();
        for (Entity e : this) {
            if (!e.hasComponent(componentId)) {
                collection.add(e);
            }
        }
        return collection;
    }

    /**
     * Filters in entities with a certain component
     *
     * @param componentId the component filter
     * @return a new collection using this collection as a base
     */
    public EntityCollection withComponent(String componentId) {
        EntityCollection collection = new EntityCollection();
        for (Entity e : this) {
            if (e.hasComponent(componentId)) {
                collection.add(e);
            }
        }
        return collection;
    }

    /**
     * Indicates if this collection contains a certain entity
     *
     * @param entity entity object instance
     * @return
     */
    public boolean contains(Entity entity) {
        if(entity == null) return false;
        return contains(entity.getId());
    }

    /**
     * Indicates if this collection contains a certain entity with the provided Id
     *
     * @param entityId id of the entity to find
     * @return true if entity found, false otherwise
     */
    public boolean contains(String entityId) {
        if (entityId == null) return false;
        for (int i = 0; i < size; i++) {
            Entity other = get(i);
            if (other.getId().equals(entityId))
                return true;
        }
        return false;
    }

    /**
     * Removes a certain entity from this collection
     * @param entity
     */
    public void remove(Entity entity) {
        if (entity == null) return;
        remove(entity.getId());
    }

    /**
     * Removes an entity with provided id from this collection
     *
     * @param entityId id of the entity to remove
     */
    public void remove(String entityId) {
        if (entityId == null) return;
        for (int i = 0; i < size; i++) {
            Entity other = get(i);
            if (other.getId().equals(entityId)) {
                removeIndex(i);
                break;
            }
        }
    }

    /**
     * Removes the entities contained in provided collection from the current collection
     *
     * @param collection collection of the entities to remove from the current collection
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
