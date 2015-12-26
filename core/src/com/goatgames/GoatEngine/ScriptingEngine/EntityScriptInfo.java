package com.goatgames.goatengine.scriptingengine;

import java.util.HashMap;

/**
 * Represents info about a script (last edit date, path etc.)
 */
class EntityScriptInfo {

    private long lastModified = 0;
    // Key is id of entity and value is instance of script
    private final HashMap<String, EntityScript> instances = new HashMap<String, EntityScript>();


    EntityScriptInfo(long lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Returns the script instance for a certain entity Id
     * @param id
     * @return
     */
    public EntityScript getInstance(String id){
        return this.instances.get(id);
    }

    /**
     * Adds a new instance associated with an entity Id
     * @param entityId
     * @param instance
     */
    public void addInstance(String entityId, EntityScript instance){
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
    public HashMap<String,EntityScript> getInstances() {
        return instances;
    }
}
