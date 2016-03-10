package com.goatgames.goatengine.ecs.core;

import com.badlogic.gdx.utils.Array;

/**
 * A Collection of entities. Offers method to filter out entities
 * based on their component or lack there of
 */
public class EntityCollection extends Array<Entity> {

    public EntityCollection(){
        super();
    }
    public EntityCollection(Array<Entity> entities) {
        super(entities);
    }

    public EntityCollection withoutComponent(String componentId){
        EntityCollection collection = new EntityCollection();
        for(Entity e: this){
            if(!e.hasComponent(componentId)){
                collection.add(e);
            }
        }
        return collection;
    }

    public EntityCollection withComponent(String componentId){
        EntityCollection collection = new EntityCollection();
        for(Entity e: this){
            if(e.hasComponent(componentId)){
                collection.add(e);
            }
        }
        return collection;
    }

}
