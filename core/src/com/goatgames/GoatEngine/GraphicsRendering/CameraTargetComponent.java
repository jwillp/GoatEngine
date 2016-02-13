package com.goatgames.goatengine.graphicsrendering;

import com.goatgames.goatengine.ecs.core.EntityComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to make an entity important for a camera.
 * The cameras concentrate on these kind of entities.
 */
public class CameraTargetComponent extends EntityComponent {
    public final static String ID = "CAMERA_TARGET_PROPERTY";

    public CameraTargetComponent(){
        super(true);
    }

    /**
     * Ctor taking a Pod Representation of the current component
     *
     * @param map
     */
    public CameraTargetComponent(Map<String, String> map) {
        super(map);
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected Map<String, String> makeMap() {
        return new HashMap<String, String>();
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param map the pod representation to use
     */
    protected void makeFromMap(Map<String, String> map) {

    }

    @Override
    public String getId() {
        return ID;
    }


}
