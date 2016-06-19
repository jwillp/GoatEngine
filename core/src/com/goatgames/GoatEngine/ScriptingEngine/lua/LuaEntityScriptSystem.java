package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.scriptingengine.EntityScriptComponent;
import com.goatgames.goatengine.scriptingengine.IEntityScript;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.Logger;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

/**
 * Entity System managing entity scripts as lua scripts
 */
public class LuaEntityScriptSystem extends EntitySystem {

    // For entity scripts
    private ObjectMap<String, LuaEntityScriptInfo> entityScripts;

    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        entityScripts = new ObjectMap<>();
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
            if(!entity.hasComponent(EntityScriptComponent.ID)){
                Logger.warn("Entity does not have an EntityScriptComponent, an instance will automatically be added");
                entity.addComponent(new EntityScriptComponent(true), EntityScriptComponent.ID);
            }
            EntityScriptComponent entityScriptComp = (EntityScriptComponent)entity.getComponent(EntityScriptComponent.ID);
            // Add missing script instances
            for(String scriptFile: luaScriptComp.getScripts()){
                String fullPath = GoatEngine.config.getString("scripting.directory") + scriptFile;
                if(!entityScriptComp.hasScriptWithName(fullPath)){
                    // Before adding it check if the script is already registered and if so, if it is valid
                    if (this.entityScripts.containsKey(fullPath) && !this.entityScripts.get(fullPath).isValid()) {
                        continue;
                    }
                    IEntityScript script = getScriptInstance(fullPath);
                    if(GAssert.notNull(script, "script == null")) {
                        entityScriptComp.addScript(script);
                    }else{
                        // flag the script as being invalid (erroneous)
                        this.entityScripts.get(fullPath).setValid(false);

                    }
                }
            }
            // Remove unused scripts
            for(String scriptToRemove: luaScriptComp.getScriptsToRemove()){
                entityScriptComp.removeScriptByName(scriptToRemove);
                luaScriptComp.getScriptsToRemove().remove(scriptToRemove);
            }

            getEntityManager().freeEntityObject(entity);
        }
        checkReload();
    }

    /**
     * Returns an instance of a script
     * @param scriptFile
     * @return
     */
    public LuaEntityScript getScriptInstance(String scriptFile){
        LuaEntityScript script = null;
        try{
            script = loadScript(scriptFile);
        }catch (LuaScript.LuaScriptException ex){
            Logger.error(ex.getMessage());
            Logger.logStackTrace(ex);
        }
        return script;
    }

    /**
     * Loads a script and returns an instance of that script
     * Also loads the GoatEngine API into the script's global
     * @param scriptFile full path to the script file
     */
    public LuaEntityScript loadScript(String scriptFile){
        // If the script is not in memory, we'll add it in memory.
        if(!isScriptRegistered(scriptFile)){
            registerScript(scriptFile);
        }

        LuaEntityScript script = new LuaEntityScript(scriptFile);
        // Expose API and import libraries
        script.exposeJavaFunction(new GoatEngineAPI());
        if(!GAssert.that(script.load(), String.format("Error loading script: \"%s\"", scriptFile))){
            return null;
        }
        return script;
    }

    /**
     * Indicates if a given script was already registered with the manager or not
     * @param scriptFile path to script file
     * @return true if registered
     */
    private boolean isScriptRegistered(String scriptFile){
        return entityScripts.containsKey(scriptFile);
    }

    /**
     * Registers the script with the manager. (Make it available to the manager)
     * It will load metadata about the script and keep this in memory.
     * This metadata is mainly used for script reloading
     * @param scriptFile path to script file
     */
    private void registerScript(String scriptFile){
        long lastModified = Gdx.files.internal(scriptFile).lastModified();
        entityScripts.put(scriptFile, new LuaEntityScriptInfo(lastModified));
    }

    /**
     * Reloads all instances of a script in memory
     * @param info script info object
     */
    private void reloadScript(String scriptFile, LuaEntityScriptInfo info){
        // Get Entity With Lua Script component
        // Get entities having the script
        // Reload the script
        info.setLastModified(Gdx.files.internal(scriptFile).lastModified());
    }

    private void checkReload(){
        if(!GoatEngine.config.getBoolean("scripting.auto_reload")) return;
        for (ObjectMap.Entry<String, LuaEntityScriptInfo> entry: this.entityScripts.entries()){
            String scriptFile = entry.key;
            LuaEntityScriptInfo info = entry.value;
            long lastModifiedOnDisk = Gdx.files.internal(scriptFile).lastModified();
            if (info.getLastModified() != lastModifiedOnDisk){
                info.setLastModified(lastModifiedOnDisk);
                // Find entities with that script
                for (Entity entity: getEntityManager().getEntitiesWithComponent(EntityScriptComponent.ID)){
                    Logger.info(String.format("Reloading script \"%s\" ...", scriptFile));
                    EntityScriptComponent comp = (EntityScriptComponent) entity.getComponent(EntityScriptComponent.ID);
                    if (comp.hasScriptWithName(scriptFile)){
                        IEntityScript script = comp.getScriptByName(scriptFile);
                        LuaEntityScript luaScript = (LuaEntityScript) script;
                        if(GAssert.notNull(luaScript,
                                String.format("Script %s was not a lua script instance.", scriptFile))){
                            luaScript.reload();
                            Logger.info(String.format("Reloaded script \"%s\".", scriptFile));
                        }else{
                            Logger.info("Could not reload script.");
                        }
                    }
                }
            }
        }
    }

    /**
     * Preloads a module in a script's globals
     * @param moduleFile
     * @param globals
     */
    private LuaValue preloadLuaModule(String moduleFile, Globals globals){
        LuaValue chunk = globals.load("require '" + moduleFile + "';");
        chunk.call();
        chunk = globals.loadfile(moduleFile);
        return chunk.call();
    }
}
