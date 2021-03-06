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

import java.util.List;
import java.util.Objects;

/**
 * Entity System managing entity scripts.
 * All scripts regardless their underlying technology (lua, groovy, beanshell, javascript or native java).
 * They all implement the IEntityScript interface. This system only work with these interface instances.
 * For the specific workings of the scripts of a technology (e.g: js scripts instantiated as IEntityScripts) is
 * done in a language specific system. (Though this current system is still used)
 */
public class EntityScriptSystem extends EntitySystem {

    @Override
    public void init() {
        this.registerForAllEvents();

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
                script.setEventDispatcher(getSystemManager().getEventDispatcher());
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
                if (!script.isInitialised()) continue;

                if (e instanceof InputEvent) {
                    script.onInputEvent(entity, (InputEvent) e);
                } else if (e instanceof GameEvent) {
                    script.onGameEvent(entity, (GameEvent) e);
                } else if (e instanceof CollisionEvent) {
                    script.onCollision(entity, (CollisionEvent) e);
                } else if (e instanceof EntityEvent) {
                    script.onEntityEvent(entity, (EntityEvent) e);
                }
            }
            entityManager.freeEntityObject(entity);
        }
        return false;
    }
}
