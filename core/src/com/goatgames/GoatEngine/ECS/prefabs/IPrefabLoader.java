package com.goatgames.goatengine.ecs.prefabs;

/**
 * Represent a Loader of rpefabs. This interface defines the necessary methods for a loader to load prefabs
 * (whether from file (XML,ini, Json), network etc.)
 */
public interface IPrefabLoader {

    /**
     * Loads a prefab from a certain path.
     *
     * @param pathToPrefab path to the prefab file
     * @return A Prefab Object
     */
    Prefab load(String pathToPrefab);
}
