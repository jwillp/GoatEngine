package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Lua Scripting Engine managing script instances and
 * data caching for efficient performances.
 */
public final class LuaScriptingEngine {

    private static ObjectMap<String, LuaScript> scriptCache;


    public LuaScriptingEngine(){
        scriptCache = new ObjectMap<String, LuaScript>();
    }

    /**
     * Adds a new script to the engine and caches it if necessary
     * otherwise reload it
     * @param fileName the name of the script file
     * @return true when the operation was done successfully
     */
    public boolean addScript(String fileName){
        LuaScript script = new LuaScript(fileName);
        if(!script.load()){
            return false; // error Loading file
        }

        // Does it exist in cache ?
        if(!scriptCache.containsKey(fileName)){
            scriptCache.put(fileName, script);
        }

        return true;
    }



    public void clear(){
        scriptCache.clear();
        scriptCache = null;
    }


}
