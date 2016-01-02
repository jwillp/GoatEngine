package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;

/**
 * Entity System managing entity scripts as lua scripts
 */
public class LuaEntityScriptSystem extends EntitySystem {


    /**
     * Used to initialise the system
     */
    @Override
    public void init() {

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
                    script.executeFunction("onInit");
                }

                script.executeFunction("update");
            }
            getEntityManager().freeEntity(entity);
        }
    }












}
