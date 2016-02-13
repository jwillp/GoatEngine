package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.goatgames.goatengine.GEConfig;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

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

        scriptFile = GEConfig.getString("scripting.directory") + scriptFile;

        // If the script is not in memory, we'll "register" it. (List as a potential script)
        if(!isScriptRegistered(scriptFile)){
            registerScript(scriptFile);
        }

        // Does the current entity has an instance of that script?
        LuaScript script = this.entityScripts.get(scriptFile).getInstance(entityId);
        if(script == null){
            script = new LuaScript(scriptFile);
            // Expose API and import libraries
            script.exposeJavaFunction(new GoatEngineAPI());
            script.load();
            //preloadLuaModule("flux.lua", script.getGlobals());

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
        scriptFile =  GEConfig.getString("scripting.directory")  + scriptFile;
        LuaEntityScriptInfo info = this.entityScripts.get(scriptFile);
        // Means the script has not been loaded yet (it will be done eventually)
        if(info == null) return null;

        // Check for reload
        if(GEConfig.getBoolean("scripting.auto_reload")){
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
        for (ObjectMap.Values<LuaScript> iterator = info.getInstances().values().iterator(); iterator.hasNext(); ) {
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
