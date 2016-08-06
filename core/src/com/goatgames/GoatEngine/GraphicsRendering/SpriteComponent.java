package com.goatgames.goatengine.graphicsrendering;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;


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

    public SpriteComponent(NormalisedEntityComponent data) {
        super(data);
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

    /**
     * Sets the color of the sprite
     * @param colorHex RRGGBBAA Hex
     */
    public void setColor(String colorHex){
        this.color = new Color(Color.valueOf(colorHex));
    }

    public String getResource() {
        return resourceName;
    }

    /**
     * Sets the resource
     * @param resourceName
     */
    public void setResource(String resourceName){
        this.resourceName = resourceName;
    }

    public Color getColor() {
        return color;
    }

    public int getZIndex() {
        return zIndex;
    }

    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.put("offset_x", String.valueOf(offsetX));
        data.put("offset_y", String.valueOf(offsetY));
        data.put("resource_name", resourceName);
        data.put("auto_adjust", String.valueOf(autoAdjust));
        data.put("scale", String.valueOf(scale));
        data.put("color", this.color.toString());
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        this.offsetX = Float.parseFloat(data.get("offset_x"));
        this.offsetY = Float.parseFloat(data.get("offset_y"));
        this.resourceName =  data.get("resource_name");
        this.autoAdjust = Boolean.parseBoolean(data.get("auto_adjust"));
        this.scale = Float.parseFloat(data.get("scale"));
        String colorHex = data.getOrDefault("color", Color.WHITE.toString()).replace("#","");
        if(colorHex.length() == 6) colorHex += "FF";
        setColor(colorHex);
    }

    @Override
    public String getId() {
        return ID;
    }
}
