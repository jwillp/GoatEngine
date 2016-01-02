package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.GEConfig;

import java.util.ArrayList;

/**
 * Lua Scripting Engine managing script instances and
 * data caching for efficient performances.
 */
public final class LuaScriptingEngine {

    // For entity scripts
    private ObjectMap<String, LuaEntityScriptInfo> entityScripts;

    // Keep a list of scripts that ran with errors in order not to rerun them
    // (at least until their source was changed)
    private ArrayList<String> errorScripts = new ArrayList<String>();


    /**
     * Constructor
     */
    public LuaScriptingEngine(){
        entityScripts = new ObjectMap<String, LuaEntityScriptInfo>();
    }

    public void init() {

    }

    /**
     * Cleanly disposes the scripting engine
     */
    public void dispose(){
        this.entityScripts.clear();
    }

    /**
     * Indicates if a given script was already registered with the engine or not
     * @param scriptFile path to script file
     * @return true if registered
     */
    private boolean isScriptRegistered(String scriptFile){
        return entityScripts.containsKey(scriptFile);
    }

    /**
     * Registers the script with the engine. (Make it available to the engine)
     * @param scriptFile path to script file
     */
    private void registerScript(String scriptFile){
        long lastModified = Gdx.files.internal(scriptFile).lastModified();
        entityScripts.put(scriptFile, new LuaEntityScriptInfo(lastModified));
    }


    /**
     * Adsd an entity script to the engine
     * @param scriptFile path to the script file
     * @param entityId the id of the entity to assciate the instance
     */
    public LuaScript addScript(String scriptFile, String entityId){
        // If the script is not in memory, we'll "register" it. (List as a potential script)
        if(!isScriptRegistered(scriptFile)){
            registerScript(scriptFile);
        }

        // Does the current entity has an instance of that script?
        LuaScript script = this.entityScripts.get(scriptFile).getInstance(entityId);
        if(script == null){
            script = new LuaScript(GEConfig.ScriptingEngine.SCRIPTS_DIR + scriptFile);
            script.load();
            this.entityScripts.get(scriptFile).addInstance(script, entityId);
        }

        return script;
    }

    /**
     * Returns an instance of a script for a certain entity
     * and if necessary reloads it.
     * @param scriptFile the path to the script file
     * @param entityId the Id of the entity
     */
    public LuaScript getScript(String scriptFile, String entityId){
        LuaEntityScriptInfo info = this.entityScripts.get(scriptFile);
        if(info == null) return null;

        // Check for reload
        if(GEConfig.ScriptingEngine.AUTO_RELOAD){
            // Is source newer?
            long lastModifiedOnDisk = Gdx.files.internal(scriptFile).lastModified();
            long lastModifiedInMemory = info.getLastModified();
            if(lastModifiedOnDisk != lastModifiedInMemory){
                reloadScript(scriptFile);
            }
        }
        return info.getInstance(entityId);
    }


    /**
     * Reloads all instances of a script in memory
     * @param scriptFile path to the script file
     */
    public void reloadScript(String scriptFile){
        reloadScript(this.entityScripts.get(scriptFile));
    }


    /**
     * Reloads all instances of a script in memory
     * @param info script info object
     */
    private void reloadScript(LuaEntityScriptInfo info){
        for(LuaScript instance : info.getInstances().values()){
            instance.reload();
        }
    }


}
