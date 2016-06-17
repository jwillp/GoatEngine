package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.scriptingengine.EntityScriptComponent;
import com.goatgames.goatengine.scriptingengine.IEntityScript;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.Logger;

/**
 * Entity System managing entity scripts as lua scripts
 */
public class LuaEntityScriptSystem extends EntitySystem {

    LuaScriptingManager scriptManager;
    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        scriptManager = new LuaScriptingManager();
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {
        for(Entity entity: getEntityManager().getEntitiesWithComponent(LuaEntityScriptComponent.ID)){
            LuaEntityScriptComponent luaScriptComp;
            luaScriptComp = (LuaEntityScriptComponent) entity.getComponent(LuaEntityScriptComponent.ID);
            if(!GAssert.that(entity.hasComponent(EntityScriptComponent.ID),
                    "Entity does not have an EntityScriptComponent, an instance will automatically be added")){
                entity.addComponent(new EntityScriptComponent(true), EntityScriptComponent.ID);
            }
            EntityScriptComponent entityScriptComp = (EntityScriptComponent)entity.getComponent(ScriptComponent.ID);
            // Add missing script instances
            for(String scriptFile: luaScriptComp.getScripts()){
                if(!entityScriptComp.hasScriptWithName(scriptFile)){
                    entityScriptComp.addScript(getScriptInstance(scriptFile, entity));
                }
            }
            // Remove unused scripts
            for(String scriptToRemove: luaScriptComp.getScriptsToRemove()){
                entityScriptComp.removeScriptByName(scriptToRemove);
                luaScriptComp.getScriptsToRemove().remove(scriptToRemove);
            }
            getEntityManager().freeEntityObject(entity);
        }
    }

    /**
     * Returns an instance of a script
     * @param scriptFile
     * @param entity
     * @return
     */
    public LuaEntityScript getScriptInstance(String scriptFile, Entity entity){
        LuaEntityScript script = null;
        try{
             script = scriptManager.getScript(scriptFile, entity.getID());
            if(script == null){
                script = scriptManager.addScript(scriptFile,entity.getID());
            }
        }catch (LuaScript.LuaScriptException ex){
            Logger.error(ex.getMessage());
            Logger.logStackTrace(ex);
        }
        return script;
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

}
