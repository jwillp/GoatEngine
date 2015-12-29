package com.goatgames.goatengine.graphicsrendering;

import java.util.Map;

/**
 * Used to display simple fake lights (alpha blending)
 */
public class FakeLightComponent extends SpriteComponent {

    public final static String ID = "FAKE_LIGHT_COMPONENT";

    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public FakeLightComponent(Map<String, String> map) {
        super(map);
    }

    @Override
    public String getId() {
        return ID;
    }
}
