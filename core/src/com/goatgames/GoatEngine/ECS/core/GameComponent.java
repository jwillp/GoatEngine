package com.goatgames.goatengine.ecs.core;

import java.util.Map;

/**
 * Dynamic storing attributes in a Map<String,String>
 */
public class GameComponent extends DynamicComponent {

    private Map<String, String> attribute;

    public GameComponent(NormalisedEntityComponent data) {
        super(data);
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent normData = super.normalise();
        normData.putAll(attribute);
        return normData;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        attribute = data;
    }
}
