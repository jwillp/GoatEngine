package com.goatgames.goatengine.ecs.common;

import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.utils.GAssert;

import java.util.HashMap;
import java.util.Map;

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




    /**
     * Ctor taking a map Representation of the current component
     *
     * @param map
     */
    public TransformComponent(Map<String, String> map) {
        super(map);
    }

    public TransformComponent() {
        super(true);
    }

    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return the map built
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("x",String.valueOf(this.x));
        map.put("y",String.valueOf(this.y));
        map.put("rotation",String.valueOf(this.rotation));
        map.put("width",String.valueOf(this.width));
        map.put("height", String.valueOf(this.height));
        return map;
    }

    /**
     * Builds the current object from a map representation
     *
     * @param map the map representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
       this.x = Float.parseFloat(map.get("x"));
       this.y = Float.parseFloat(map.get("y"));
       this.rotation = Float.parseFloat(map.get("rotation"));
       this.width = Float.parseFloat(map.get("width"));
       this.height = Float.parseFloat(map.get("height"));
    }

    /**
     * Used to clone a component
     *
     * @return
     */
    @Override
    public EntityComponent clone() {
        return new Factory().processMapData(this.getId(), this.makeMap());
    }

    @Override
    public String getId() {
        return ID;
    }

    public void setSize(float width, float height){
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

    public static class Factory implements EntityComponentFactory {
        /**
         * Takes a data map and tries to construct a component with it
         * if the data map was incompatible with the factory
         * return null
         *
         * @param componentId
         * @param map         a map representation of a component
         * @return A Contstructed Component or null if it could not be constructed
         */
        @Override
        public EntityComponent processMapData(String componentId, Map<String, String> map) {
            GAssert.that(componentId.equals(TransformComponent.ID),
                    "Component Factory Mismatch: TransformComponent.ID != " + componentId);
            TransformComponent component = new TransformComponent(map);
            return component;
        }
    }
}
