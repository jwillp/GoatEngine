package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.goatgames.goatengine.ecs.core.EntityComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * A component enabling an entity to act as a camera.
 * The reason cameras are components is to enable multiple cameras
 * and easily edit them like any other entity.
 */
public class CameraComponent extends EntityComponent{

    public static final String ID = "CAMERA_COMPONENT";

    private OrthographicCamera camera = null;

    public CameraComponent(){
        super(true);
        camera = new OrthographicCamera();
    }

    public CameraComponent(Map<String, String> map) {
        super(map);
        camera = new OrthographicCamera();
    }


    /**
     * Constructs a Map, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("zoom", String.valueOf(camera.zoom));
        map.put("position_x", String.valueOf(camera.position.x));
        map.put("position_y", String.valueOf(camera.position.y));
        return map;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param map the pod representation to use
     */
    @Override
    protected void makeFromMap(Map<String, String> map) {
        camera = new OrthographicCamera();
        camera.position.x = Float.parseFloat(map.get("position_x"));
        camera.position.y = Float.parseFloat(map.get("position_y"));
        camera.zoom = Float.parseFloat(map.get("zoom"));
    }


    @Override
    public String getId() {
        return ID;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}