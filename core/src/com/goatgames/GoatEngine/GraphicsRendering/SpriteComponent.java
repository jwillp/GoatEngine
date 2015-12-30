package com.goatgames.goatengine.graphicsrendering;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goatgames.goatengine.ecs.core.EntityComponent;

import java.util.HashMap;
import java.util.Map;


/**
 * Used so an entity has a graphical representation
 */
public class SpriteComponent extends EntityComponent {

    public final static String ID = "SPRITE_COMPONENT";

    private String resourceName;
    private TextureRegion currentSprite;
    protected Color color;

    public float offsetX;
    public float offsetY;

    // When set to true the rendering engines will force a ration and size according to PhysicsComponent
    public boolean autoAdjust;
    // If auto adjust is on, this value is updated automatically,
    public float scale;

    private int zIndex;

    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public SpriteComponent(Map<String, String> map) {
        super(map);
    }

    public TextureRegion getCurrentSprite() {
        return currentSprite;
    }

    public void setCurrentSprite(TextureRegion currentSprite) {
        this.currentSprite = currentSprite;
    }

    /**
     * Sets the color of the sprite
     * @param color
     */
    public void setColor(Color color){
        this.color = color;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public Color getColor() {
        return color;
    }


    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String>  makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("offset_x", String.valueOf(offsetX));
        map.put("offset_y", String.valueOf(offsetY));
        map.put("resource_name", resourceName);
        map.put("auto_adjust", String.valueOf(autoAdjust));
        map.put("scale", String.valueOf(scale));
        return map;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param map the pod representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
        this.offsetX = Float.parseFloat(map.get("offset_x"));
        this.offsetY = Float.parseFloat(map.get("offset_y"));
        this.resourceName =  map.get("resource_name");
        this.autoAdjust = Boolean.parseBoolean(map.get("auto_adjust"));
        this.scale = Float.parseFloat(map.get("scale"));
        this.currentSprite = new TextureRegion(new Texture(Gdx.files.internal(resourceName)));

    }

    @Override
    public String getId() {
        return ID;
    }



}