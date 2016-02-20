package com.goatgames.goatengine.ecs.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Map representation of a component
 * Syntactic sugar
 */
public class EntityComponentMap extends HashMap<String, String> {
    public EntityComponentMap() {
        super();
    }

    public EntityComponentMap(Map<String, String> map) {
        super(map);
    }
}
