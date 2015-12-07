package com.brm.GoatEngine.ECS.core;

import com.badlogic.gdx.utils.XmlReader;

/**
 * Used to make an entity important for a camera.
 * The cameras concentrate on these kind of entities.
 */
public class CameraTargetComponent extends EntityComponent {
    public final static String ID = "CAMERA_TARGET_PROPERTY";

    public CameraTargetComponent(){}


    /**
     * Desiralizes a component
     *
     * @param componentData the data as an XML element
     */
    public void deserialize(XmlReader.Element componentData) {
        //Nothing to do here
    }

    @Override
    public String getId() {
        return ID;
    }
}
