package com.goatgames.goatengine.scriptingengine.nativescripts;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.graphicsrendering.PostRenderEvent;
import com.goatgames.goatengine.graphicsrendering.PreRenderEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.screenmanager.LateUpdateEvent;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;
import com.goatgames.goatengine.scriptingengine.lua.LuaScript;
import com.goatgames.goatengine.utils.Logger;

/**
 * Entity system used for native (java) script
 */
public class NativeScripSystem extends EntitySystem implements GameEventListener {
    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        GoatEngine.eventManager.registerListener(this);
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {
        for(Entity entity: getEntityManager().getEntitiesWithComponent(NativeScriptComponent.ID)){
            NativeScriptComponent scriptComp = (NativeScriptComponent) entity.getComponent(NativeScriptComponent.ID);
            for(NativeScript script: scriptComp.getScripts()){
                if(!script.isInitialised()){
                    script.init();
                    script.setInitialised(true);
                }else {
                    script.update(dt);
                }
            }
            getEntityManager().freeEntityObject(entity);
        }
    }

    @Override
    public void onEvent(Event e) {
        for(Entity entity: getEntityManager().getEntitiesWithComponent(NativeScriptComponent.ID)){
            NativeScriptComponent scriptComp = (NativeScriptComponent) entity.getComponent(NativeScriptComponent.ID);
            for(NativeScript script: scriptComp.getScripts()){
                if (script != null) {
                    if(e instanceof LateUpdateEvent){
                       script.lateUpdate();
                    }else if(e instanceof PreRenderEvent){
                        script.preRender();
                    }else if(e instanceof PostRenderEvent){
                        script.postRender();
                    }else if(e instanceof CollisionEvent){
                        script.onCollision((CollisionEvent) e);
                    }else if(e instanceof InputEvent){
                        script.onInputEvent((InputEvent) e);
                    }else if(e instanceof GameEvent){
                        script.onGameEvent((GameEvent) e);
                    } else{
                        // TODO something ...
                    }
                }
            }
            getEntityManager().freeEntityObject(entity);
        }
    }
}
