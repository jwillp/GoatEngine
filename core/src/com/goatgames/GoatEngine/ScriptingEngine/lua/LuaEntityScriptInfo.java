package com.goatgames.goatengine.scriptingengine.lua;

import com.badlogic.gdx.utils.ObjectMap;

/**
 * Represents info about a script (last edit date, path etc.)
 */
public class LuaEntityScriptInfo {

    private long lastModified = 0;
    // Key is id of entity and value is instance of script
    private final ObjectMap<String, LuaScript> instances = new ObjectMap<String, LuaScript>();


    LuaEntityScriptInfo(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Returns the script instance for a certain entity Id
     * @param id
     * @return
     */
    public LuaScript getInstance(String id){
        return this.instances.get(id);
    }

    /**
     * Adds a new instance associated with an entity Id
     * @param entityId
     * @param instance
     */
    public void addInstance(LuaScript instance, String entityId){
        this.instances.put(entityId, instance);
    }

    /**
     * Returns the last time the script was modified
     * @return
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Sets the last time the script was modified
     * @param lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Returns all instances of scripts
     * @return
     */
    public ObjectMap<String,LuaScript> getInstances() {
        return instances;
    }
}
