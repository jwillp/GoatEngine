package com.brm.GoatEngine.GraphicsRendering;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntityComponent;

import java.util.Comparator;


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


    class SpriteComponentPOD extends EntityComponentPOD{

        @SerializeName("offset_x")
        public float offsetX;

        @SerializeName("offset_y")
        public float offsetY;

        @SerializeName("resource_name")
        public String resourceName;
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
    protected EntityComponentPOD makePOD() {
        SpriteComponentPOD pod = new SpriteComponentPOD();
        pod.offsetX = this.offsetX;
        pod.offsetY = offsetY;
        pod.resourceName = this.resourceName;
        return pod;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromPOD(EntityComponentPOD pod) {
        SpriteComponentPOD spritePOD = (SpriteComponentPOD) pod;
        this.offsetX = spritePOD.offsetX;
        this.offsetY = spritePOD.offsetY;
        this.resourceName = spritePOD.resourceName;
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