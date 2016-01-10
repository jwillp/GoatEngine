package com.goatgames.goatengine.ecs.core;

import java.util.Map;

/**
 * Mostly used by scripts to easily create new components
 */
public class GameComponent extends EntityComponent {

    public GameComponent(Map<String, String> map) {
        super(map);
    }

    @Override
    protected Map<String, String> makeMap() {
        return null;
    }

    @Override
    protected void makeFromMap(Map<String, String> map) {

    }

    @Override
    public String getId() {
        return null;
    }
}
