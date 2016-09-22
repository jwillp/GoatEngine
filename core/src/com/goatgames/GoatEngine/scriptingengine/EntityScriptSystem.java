package com.goatgames.goatengine.scriptingengine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.gdk.GAssert;
import com.goatgames.gdk.eventdispatcher.IEventListener;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.EntityEvent;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.scriptingengine.common.IEntityScript;

import java.util.Objects;

/**
 * Entity System managing entity scripts
 */
public class EntityScriptSystem extends EntitySystem implements IEventListener {

    @Override
    public void init() {
        GoatEngine.eventManager.register(this); // TODO Use Current screen event listener
    }

    @Override
    public void preUpdate() {
        EntityManager entityManager = getEntityManager();
        if(!GAssert.notNull(entityManager, "entityManager == null")){
            return;
        }

        for(Entity entity: entityManager.getEntitiesWithComponent(EntityScriptComponent.ID)){
            EntityScriptComponent scriptComp = (EntityScriptComponent)entity.getComponent(EntityScriptComponent.ID);
            for (ObjectMap.Entry<String, IEntityScript> entry : scriptComp.getScripts().entries()) {
                IEntityScript script = entry.value;
                if(!script.isInitialised()){
                    script.init(entity);
                }
            }
            entityManager.freeEntityObject(entity);
        }
    }

    @Override
    public void update(float dt) {
        EntityManager entityManager = getEntityManager();
        if(!GAssert.notNull(entityManager, "entityManager == null")){
            return;
        }

        for(Entity entity: entityManager.getEntitiesWithComponent(EntityScriptComponent.ID)){
            EntityScriptComponent scriptComp = (EntityScriptComponent)entity.getComponent(EntityScriptComponent.ID);
            for (ObjectMap.Entry<String, IEntityScript> entry : scriptComp.getScripts().entries()) {
                IEntityScript script = entry.value;
                if(script.isInitialised()){
                    script.update(entity,dt);
                }
            }
            entityManager.freeEntityObject(entity);
        }
    }

    @Override
    public boolean onEvent(Event e) {
        EntityManager entityManager = getEntityManager();
        if (!GAssert.notNull(entityManager, "entityManager == null")) {
            return false;
        }
        for(Entity entity: entityManager.getEntitiesWithComponent(EntityScriptComponent.ID)){
            EntityScriptComponent scriptComp = (EntityScriptComponent)entity.getComponent(EntityScriptComponent.ID);
            // Save scripts in an array to avoid nested iterators
            Array<IEntityScript> scripts = scriptComp.getScripts().values().toArray();
            for(int i=0; i < scripts.size; i++){
                IEntityScript script = scripts.get(i);
                if (script.isInitialised()) {
                    if (e instanceof InputEvent) {
                        script.onInputEvent(entity, (InputEvent) e);
                    } else if (e instanceof CollisionEvent) {
                        final CollisionEvent collisionEvent = (CollisionEvent) e;
                        if(Objects.equals(collisionEvent.getEntityA(), entity.getId()))
                            script.onCollision(entity, collisionEvent);
                    } else if (e instanceof GameEvent) {
                        script.onGameEvent(entity, (GameEvent) e);
                    } else if (e instanceof EntityEvent) {
                        final EntityEvent entityEvent = (EntityEvent) e;
                        if(Objects.equals(entityEvent.getEntityId(), entity.getId()))
                            script.onEntityEvent(entity, (EntityEvent)e);
                    }
                }
            }
            entityManager.freeEntityObject(entity);
        }
        return false;
    }
}
