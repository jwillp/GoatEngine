package com.goatgames.goatengine.ecs.prefabs;

import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an entity prefab in memory
 */
public class Prefab {
    /**
     * Map of component. Each component is represented by a Map where the key is the name of the component field,
     * and the value is the value to use for that field. Each component is identified by its componentId in the map.
     */
    private Map<String, NormalisedEntityComponent> components;

    public Prefab(){
        components = new HashMap<>();
    }

    /**
     * Adds a component to the prefab definition
     * @param componentData Normalised component
     */
    public void addComponent(NormalisedEntityComponent componentData){
        this.components.put(componentData.getId(), componentData);
    }

    /**
     * Returns a list of normalised component for the current prefab.
     * @return list of components
     */
    public List<NormalisedEntityComponent> getComponents(){
        return (List<NormalisedEntityComponent>) components.values();
    }
}
