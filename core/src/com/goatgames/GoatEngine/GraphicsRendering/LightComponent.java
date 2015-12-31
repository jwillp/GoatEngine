package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.graphics.Color;

import java.util.Map;

/**
 * Used to display simple fake lights (alpha blending)
 */
public class LightComponent extends SpriteComponent {

    public final static String ID = "LIGHT_COMPONENT";


    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public LightComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return the map built
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> map = super.makeMap();

        map.put("color", color.toString());
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
        super.makeFromMap(map);
        // Parse Color
        String colorHex = map.get("color").replace("#","");
        if(colorHex.length() == 6) colorHex += "FF";
        this.color = new Color(Color.valueOf(colorHex));
    }


    @Override
    public String getId() {
        return ID;
    }
}
