package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;

/**
 * Transform component describing the position, size and
 * rotation of an entity
 */
public class TransformComponent extends EntityComponent {
    public final static String ID = "TRANSFORM_COMPONENT";

    private float x;
    private float y;
    private float rotation; // in degrees
    private float width;
    private float height;

    public TransformComponent(NormalisedEntityComponent data) {
        super(data);
    }

    public TransformComponent() {
        super(true);
    }


    public TransformComponent(float x, float y, float width, float height) {
        super(true);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return the map built
     */
    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.put("x", String.valueOf(this.x));
        data.put("y", String.valueOf(this.y));
        data.put("rotation", String.valueOf(this.rotation));
        data.put("width", String.valueOf(this.width));
        data.put("height", String.valueOf(this.height));
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        this.x = Float.parseFloat(data.getOrDefault("x", "0"));
        this.y = Float.parseFloat(data.getOrDefault("y", "0"));
        this.rotation = Float.parseFloat(data.getOrDefault("rotation", "0"));
        this.width = Float.parseFloat(data.getOrDefault("width", "0.1"));
        this.height = Float.parseFloat(data.getOrDefault("height", "0.1"));
    }

    @Override
    public String getId() {
        return ID;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
}
