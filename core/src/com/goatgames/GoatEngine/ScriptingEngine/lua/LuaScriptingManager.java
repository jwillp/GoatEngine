package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.GoatEngine;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;

/**
 * Lua Scripting Engine managing script instances and
 * data caching for efficient performances.
 * TODO: Move this logic to LuaEntityScriptSystem
 */
public final class LuaScriptingManager {

    // For entity scripts
    private ObjectMap<String, LuaEntityScriptInfo> entityScripts;

    // Keep a list of scripts that ran with errors in order not to rerun them
    // (at least until their source was changed)
    private ArrayList<String> errorScripts = new ArrayList<String>();


    /**
     * Constructor
     */
    public LuaScriptingManager(){
        entityScripts = new ObjectMap<>();
    }

    public void init() {

    }

    /**
     * Cleanly disposes the manager
     */
    public void dispose(){
        this.entityScripts.clear();
    }

    /**
     * Adds an entity script to the engine.
     * First creates an instance of the script and then, associate it with an entity id
     * for easy retrieval. Also loads the GoatEngine API into the script's global
     * @param scriptFile path to the script file
     * @param entityId the id of the entity to assciate the instance
     */
    public LuaEntityScript addScript(String scriptFile, String entityId){

        scriptFile = GoatEngine.config.getString("scripting.directory") + scriptFile;

        // If the script is not in memory, we'll "register" it. (List as a potential script)
        if(!isScriptRegistered(scriptFile)){
            registerScript(scriptFile);
        }

        // Does the current entity has an instance of that script?
        LuaEntityScript script = this.entityScripts.get(scriptFile).getInstance(entityId);
        if(script == null){
            script = new LuaEntityScript(scriptFile);
            // Expose API and import libraries
            script.exposeJavaFunction(new GoatEngineAPI());
            script.load();
            //preloadLuaModule("flux.lua", script.getGlobals());

            this.entityScripts.get(scriptFile).addInstance(script, entityId);
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
     * Returns an instance of a script for a certain entity
     * and if necessary reloads it.
     * @param scriptFile the path to the script file
     * @param entityId the Id of the entity
     */
    public LuaEntityScript getScript(String scriptFile, String entityId){
        scriptFile =  GoatEngine.config.getString("scripting.directory")  + scriptFile;
        LuaEntityScriptInfo info = this.entityScripts.get(scriptFile);
        // Means the script has not been loaded yet (it will be done eventually)
        if(info == null) return null;

        // Check for reload
        if(GoatEngine.config.getBoolean("scripting.auto_reload")){
            // Is source newer?
            long lastModifiedOnDisk = Gdx.files.internal(scriptFile).lastModified();
            long lastModifiedInMemory = info.getLastModified();
            if(lastModifiedOnDisk != lastModifiedInMemory){
                reloadScript(scriptFile, info);
            }
        }
        return info.getInstance(entityId);
    }

    /**
     * Reloads all instances of a script in memory
     * @param info script info object
     */
    private void reloadScript(String scriptFile, LuaEntityScriptInfo info){
        for (ObjectMap.Values<LuaEntityScript> iterator = info.getInstances().values().iterator(); iterator.hasNext(); ) {
            LuaScript instance = iterator.next();
            instance.reload();
            iterator.remove();
        }
        info.setLastModified(Gdx.files.internal(scriptFile).lastModified());
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
