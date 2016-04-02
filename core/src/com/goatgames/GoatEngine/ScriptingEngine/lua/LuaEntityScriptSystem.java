package com.goatgames.goatengine.scriptingengine.lua;

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
import com.goatgames.goatengine.scriptingengine.ScriptComponent;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.Logger;

import java.util.Objects;

/**
 * Entity System managing entity scripts as lua scripts
 */
public class LuaEntityScriptSystem extends EntitySystem implements GameEventListener {


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
        for(Entity entity: getEntityManager().getEntitiesWithComponent(ScriptComponent.ID)){
            ScriptComponent scriptComp = (ScriptComponent) entity.getComponent(ScriptComponent.ID);
            for(String scriptFile: scriptComp.getScripts()){
                try{
                    LuaScript script = GoatEngine.scriptEngine.getScript(scriptFile, entity.getID());

                    if(script == null){
                        script = GoatEngine.scriptEngine.addScript(scriptFile,entity.getID());
                        onEntityInit(entity, script);
                    }

                    script.executeFunction("update", dt);
                }catch (LuaScript.LuaScriptException ex){
                    Logger.error(ex.getMessage());
                    Logger.logStackTrace(ex);
                }
            }
            getEntityManager().freeEntityObject(entity);
        }
    }


    /**
     * Initialises an entity Script
     * @param entity
     * @param script
     */
    public void onEntityInit(Entity entity, final LuaScript script){
        // Expose entity to script
        script.exposeJavaFunction(new CtxAPI(getEntityManager().getEntityObject(entity.getID()), script.getName()));
        script.executeFunction("init");
    }


    @Override
    public void onEvent(Event e) {
        for(Entity entity: getEntityManager().getEntitiesWithComponent(ScriptComponent.ID)){
            ScriptComponent scriptComp = (ScriptComponent) entity.getComponent(ScriptComponent.ID);
            try{
                for(String scriptFile: scriptComp.getScripts()){
                    LuaScript script = GoatEngine.scriptEngine.getScript(scriptFile, entity.getID());
                    if (script != null) {
                        if(e instanceof CollisionEvent){
                            // Only if collision is for current entity
                            CollisionEvent ce = (CollisionEvent) e;
                            if(Objects.equals(ce.getEntityA(), entity.getID())){
                                script.executeFunction("onCollision", e);
                            }
                        }else if(e instanceof InputEvent){
                            String onInputEvent = "onInputEvent";
                            if(script.functionExists(onInputEvent))
                                script.executeFunction(onInputEvent, e);
                        }else if(e instanceof GameEvent){
                            String onGameEvent = "onGameEvent";
                            if(script.functionExists(onGameEvent))
                                script.executeFunction(onGameEvent, e);
                        } else if(e instanceof PreRenderEvent){
                            final String preRender = "preRender";
                            if(script.functionExists(preRender))
                                script.executeFunction(preRender);
                        }else if(e instanceof PostRenderEvent){
                            final String postRender = "postRender";
                            if(script.functionExists(postRender))
                                script.executeFunction(postRender);
                        } else{
                            String msg = "Scripting System: Event of type " + e.toString() + " will not be processed";
                            Logger.warn(msg);
                            GAssert.that(true, msg);
                        }
                    }
                }
            }catch(LuaScript.LuaScriptException ex){
                Logger.error(ex.getMessage());
                Logger.logStackTrace(ex);
            }
            getEntityManager().freeEntityObject(entity);
        }
    }
}
