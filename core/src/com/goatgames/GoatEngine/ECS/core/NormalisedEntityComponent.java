package com.goatgames.goatengine.ecs.core;

import java.util.HashMap;
import java.util.Map;

/**
 * A Normalised Entity Component
 * Syntactic sugar
 */
public class NormalisedEntityComponent extends HashMap<String, String> {
    public NormalisedEntityComponent() {
        super();
    }

    public NormalisedEntityComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Returns the id of this Normalised Component
     *
     * @return id of the component, null if none found
     */
    public String getId() {
        return get("component_id");
    }
}
