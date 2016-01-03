package com.goatgames.goatengine.scriptingengine.lua;

import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.scriptingengine.ScriptComponent;
import com.goatgames.goatengine.utils.Logger;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

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
                    onEntityInit(entity, script);
                }

                script.executeFunction("update");
            }
            getEntityManager().freeEntity(entity);
        }
    }





    public void onEntityInit(Entity entity, LuaScript script){
        // Expose entity
        final LuaValue luaEntity = CoerceJavaToLua.coerce(getEntityManager().getEntityObject(entity.getID()));
        script.exposeJavaFunction(new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue modname, LuaValue env) {
                LuaValue library = tableOf();
                library.set("entity", luaEntity);
                env.set("ctx", library);
                env.get("package").get("loaded").set("ctx", library);
                return library;
            }
        });

        script.executeFunction("init");
    }






}
