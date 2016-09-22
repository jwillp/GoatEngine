package com.goatgames.goatengine.graphicsrendering;

import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * Used to display simple fake lights.
 * Which are simple sprites with alpha blending.
 * Hence the extend sprite Component
 */
public class LightComponent extends SpriteComponent {

    public final static String ID = "LIGHT_COMPONENT";

    public LightComponent(NormalisedEntityComponent data) {
        super(data);
    }

    @Override
    public String getId() {
        return ID;
    }
}
