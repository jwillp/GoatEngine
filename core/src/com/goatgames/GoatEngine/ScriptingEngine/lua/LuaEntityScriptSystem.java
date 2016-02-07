package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.Event;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.Logger;

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
                LuaScript script = GoatEngine.scriptEngine.getScript(scriptFile, entity.getID());

                if(script == null){
                    script = GoatEngine.scriptEngine.addScript(scriptFile,entity.getID());
                    onEntityInit(entity, script);
                }

                script.executeFunction("update", dt);
            }
            getEntityManager().freeEntity(entity);
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
            for(String scriptFile: scriptComp.getScripts()){
                LuaScript script = GoatEngine.scriptEngine.getScript(scriptFile, entity.getID());

                GAssert.notNull(script, "Script instance was null when trying to execute " + scriptFile);
                if (script != null) {
                    if(e instanceof CollisionEvent){
                        script.executeFunction("onCollision", e);
                    }else if(e instanceof InputEvent){
                        script.executeFunction("onInputEvent", e);
                    }else if(e instanceof GameEvent){
                        script.executeFunction("onGameEvent", e);
                    } else{
                        Logger.warn("Event of type " + e.toString() + " could not be processed");
                        GAssert.that(true, "Event of type " + e.toString() + " could not be processed");
                    }

                }
            }
            getEntityManager().freeEntity(entity);
        }
    }
}
