package com.goatgames.goatengine.config.engine;

/**
 * Entity definitions prefab configuration
 */
public class PrefabConfig {

    /**
     * Indicates if the loaded entity definition should be cached or reloaded every time.
     * Not caching prefabs can be useful while developing, as tiny changes can be easily tested
     * without needing to manually clear the cache or restarting the game.
     */
    public boolean caching = true;
}
