package com.brm.GoatEngine.ECS.common;

import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.core.EntityComponent;

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
     * @param pod
     */
    protected CameraTargetComponent(EntityComponentPOD pod) {
        super(pod);
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected EntityComponentPOD makePOD() {
        return new EntityComponentPOD();
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromPOD(EntityComponentPOD pod) {

    }


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
