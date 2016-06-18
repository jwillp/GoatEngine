package com.goatgames.goatengine.graphicsrendering.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntityComponentFactory;
import com.goatgames.goatengine.ecs.core.NormalisedEntityComponent;
import com.goatgames.goatengine.utils.GAssert;

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

    public boolean isDirty = true; // Means no Width and Height have been set to this camera

    public CameraComponent(){
        super(true);
        camera = new OrthographicCamera();
    }

    public CameraComponent(NormalisedEntityComponent data) {
        super(data);
        camera = new OrthographicCamera();
    }

    @Override
    public NormalisedEntityComponent normalise() {
        NormalisedEntityComponent data = super.normalise();
        data.put("zoom", String.valueOf(camera.zoom));
        data.put("position_x", String.valueOf(camera.position.x));
        data.put("position_y", String.valueOf(camera.position.y));
        return data;
    }

    @Override
    public void denormalise(NormalisedEntityComponent data) {
        super.denormalise(data);
        camera = new OrthographicCamera();
        camera.position.x = Float.parseFloat(data.get("position_x"));
        camera.position.y = Float.parseFloat(data.get("position_y"));
        camera.zoom = Float.parseFloat(data.get("zoom"));
    }

    @Override
    public EntityComponent clone() {
        return new CameraComponent(normalise());
    }

    @Override
    public String getId() {
        return ID;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
