package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;

/**
 * System used to handle entity Destruction. By the use of a LifespanComponent
 * entities can flagged to be destroyed in a certain amount of time
 */
public class EntityDestructionSystem extends EntitySystem {
    @Override
    public void init() {

    }

    @Override
    public void update(float dt) {
        for (Entity entity : getEntityManager().getEntitiesWithComponent(LifespanComponent.ID)) {
            LifespanComponent lifespanComponent = (LifespanComponent)entity.getComponent(LifespanComponent.ID);
            if(lifespanComponent.isFinished()){
                getEntityManager().deleteEntity(entity.getId());
            }

        }
    }
}
