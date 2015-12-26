package com.brm.GoatEngine.ECS.core;

import com.badlogic.gdx.utils.Pool;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Entity Pooling
 */
public class EntityPool extends Pool<Entity> {
    private static int alloc = 0;

    @Override
    protected Entity newObject() {
        Logger.info("ENTITY ALLOC: " + ++alloc);
        return new Entity();
    }


    @Override
    public void free(Entity entity){
        super.free(entity);
        entity.setID("");
        entity.setManager(null);
    }

}
