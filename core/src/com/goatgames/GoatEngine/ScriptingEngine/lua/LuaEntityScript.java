package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.input.events.InputEvent;
import com.goatgames.goatengine.physics.CollisionEvent;
import com.goatgames.goatengine.scriptingengine.common.IEntityScript;

import java.util.Objects;

/**
 * Lua Entity Script
 */
public class LuaEntityScript extends LuaScript implements IEntityScript {

    // Indicates if the script is initialised
    private boolean initialised;

    /**
     * Constructs and initializes a script file
     *
     * @param scriptFile represents the path to the script file
     */
    public LuaEntityScript(String scriptFile) {
        super(scriptFile);
        initialised = false;
    }

    /**
     * Called when the script instance is considered initialised by the engine.
     * it can also be considered as a method to call when the script instance is attached to the entity
     * @param entity
     */
    @Override
    public void init(Entity entity) {
        if(GAssert.notNull(entity, "entity == null")){
            try{
                exposeJavaFunction(new CtxAPI(entity.getManager().getEntityObject(entity.getId()), getName()));
                executeFunction("init");
                initialised = true;
            } catch (LuaScript.LuaScriptException ex){
                GoatEngine.logger.error(ex.getMessage());
                GoatEngine.logger.error(ex);
            }
        }
    }

    /**
     * Called every gameloop tick
     *
     * @param dt delta time
     */
    @Override
    public void update(Entity entity, float dt) {
        GAssert.notNull(entity, "entity == null");
        try{
            executeFunction("update", dt);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }
    }

    /**
     * Called on user input events
     *
     * @param event
     */
    @Override
    public void onInputEvent(Entity entity, InputEvent event) {
        GAssert.notNull(entity, "entity == null");
        GAssert.notNull(event, "event == null");
        try{
            final String onInputEvent = "onInputEvent";
            if (!functionExists(onInputEvent)) return;
            executeFunction(onInputEvent, event);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }

    }

    /**
     * Called when a collision occurs with the entity
     *
     * @param collisionEvent
     */
    @Override
    public void onCollision(Entity entity, CollisionEvent collisionEvent) {
        if(GAssert.notNull(entity, "entity == null") && GAssert.notNull(collisionEvent, "collisionEvent == null")){

            try{
                // Only if collision is for current entity
                if (!Objects.equals(collisionEvent.getEntityA(), entity.getId())) return;
                executeFunction("onCollision", collisionEvent);
            } catch (LuaScript.LuaScriptException ex){
                GoatEngine.logger.error(ex.getMessage());
                GoatEngine.logger.error(ex);
            }
        }

    }

    /**
     * Called when a game event occurs
     *
     * @param event
     */
    @Override
    public void onGameEvent(Entity entity, GameEvent event) {
        GAssert.notNull(entity, "entity == null");
        GAssert.notNull(event, "event == null");
        try{
            final String onGameEvent = "onGameEvent";
            if (!functionExists(onGameEvent)) return;
            executeFunction(onGameEvent, event);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }
    }

    /**
     * Called when the script is detached from the entity
     */
    @Override
    public void onDetach(Entity entity) {
        GAssert.notNull(entity, "entity == null");
        try{
            final String onDetach = "onDetach";
            if (!functionExists(onDetach)) return;
            executeFunction(onDetach);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }
    }

    @Override
    public void postRender(Entity entity) {
        GAssert.notNull(entity, "entity == null");
        try{
            final String postRender = "postRender";
            if (!functionExists(postRender)) return;
            executeFunction(postRender);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }
    }

    @Override
    public void preRender(Entity entity) {
        GAssert.notNull(entity, "entity == null");
        try{
            final String preRender = "preRender";
            if (!functionExists(preRender)) return;
            executeFunction(preRender);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }
    }

    @Override
    public void lateUpdate(Entity entity) {
        GAssert.notNull(entity, "entity == null");
        try{
            final String lateUpdate = "lateUpdate";
            if (!functionExists(lateUpdate)) return;
            executeFunction(lateUpdate);
        } catch (LuaScript.LuaScriptException ex){
            GoatEngine.logger.error(ex.getMessage());
            GoatEngine.logger.error(ex);
        }
    }

    @Override
    public boolean isInitialised() {
        return initialised;
    }

    @Override
    public boolean reload() {
        boolean retVal =  super.reload();
        this.initialised = false;
        return retVal;
    }
}
