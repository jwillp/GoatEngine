package com.goatgames.goatengine.ecs.core;

import com.badlogic.gdx.utils.Pool;

/**
 * Entity Pooling
 */
public class EntityPool extends Pool<Entity> {
    private static int alloc = 0;

    @Override
    protected Entity newObject() {
        //Logger.info("ENTITY ALLOC: " + ++alloc);
        return new Entity();
    }


    @Override
    public void free(Entity entity){
        super.free(entity);
        entity.setId("");
        entity.setManager(null);
    }

}
