package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.goatgames.goatengine.ecs.core.EntityComponent;

import java.util.Map;

/**
 * Used to display simple fake lights (alpha blending)
 */
public class FakeLightComponent extends EntityComponent {

    public final static String ID = "FAKE_LIGHT_COMPONENT";


    FrameBuffer lightBuffer;
    TextureRegion lightBufferRegion;


    public void update(){


    }



    public FakeLightComponent(boolean enabled) {
        super(enabled);
    }

    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public FakeLightComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return the map built
     */
    @Override
    protected Map<String, String> makeMap() {
        return null;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {

    }

    @Override
    public String getId() {
        return ID;
    }
}
