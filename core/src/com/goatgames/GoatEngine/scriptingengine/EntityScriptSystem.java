package com.goatgames.goatengine.scriptingengine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.scriptingengine.common.IEntityScript;

/**
 * Entity System managing entity scripts
 */
public class EntityScriptSystem extends EntitySystem implements GameEventListener  {

    @Override
    public void init() {
        GoatEngine.eventManager.registerListener(this);
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
                if(!script.isInitialised()){
                    script.init(entity);
                }else{
                    script.update(entity,dt);
                }
            }
            entityManager.freeEntityObject(entity);
        }
    }

    @Override
    public void onEvent(Event e) {
        EntityManager entityManager = getEntityManager();
        if (!GAssert.notNull(entityManager, "entityManager == null")) {
            return;
        }
        for(Entity entity: entityManager.getEntitiesWithComponent(EntityScriptComponent.ID)){
            EntityScriptComponent scriptComp = (EntityScriptComponent)entity.getComponent(EntityScriptComponent.ID);
            // Save scripts in an array to avoid nested iterators
            Array<IEntityScript> scripts = scriptComp.getScripts().values().toArray();
            for(int i=0; i < scripts.size; i++){
                IEntityScript script = scripts.get(i);
                if(e instanceof InputEvent){
                    script.onInputEvent(entity, (InputEvent) e);
                }else if(e instanceof CollisionEvent){
                    script.onCollision(entity, (CollisionEvent)e);
                }else if(e instanceof GameEvent){
                    script.onGameEvent(entity, (GameEvent) e);
                }
            }
            entityManager.freeEntityObject(entity);
        }
    }
}
