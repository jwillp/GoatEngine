package com.brm.GoatEngine.GraphicsRendering;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


/**
 * Used so an entity has a graphical representation
 */
public class SpriteComponent extends EntityComponent {

    public final static String ID = "SPRITE_COMPONENT";

    private String resourceName;
    private TextureRegion currentSprite;
    private Color color = Color.WHITE;

    public float offsetX = 0;
    public float offsetY = 0;

    private int zIndex = 0;

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
        this.currentSprite = new TextureRegion(new Texture(Gdx.files.internal(resourceName)));

    }

    @Override
    public String getId() {
        return ID;
    }

    /**
     * Comparator used to sort in ascending order (greatest z to smallest z)
     * source: https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/SortedSpriteTest.java
     */
    public static class EntitySpriteComponentComparator implements Comparator<Entity>{
        @Override
        public int compare(Entity e1, Entity e2) {
            SpriteComponent s1 = (SpriteComponent) e1.getComponent(SpriteComponent.ID);
            SpriteComponent s2 = (SpriteComponent) e2.getComponent(SpriteComponent.ID);

            return (s2.getZIndex() - s1.getZIndex()) > 0 ? 1 : -1;
        }
    }

}