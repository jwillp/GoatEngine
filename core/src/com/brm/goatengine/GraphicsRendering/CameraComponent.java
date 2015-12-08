package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.XmlReader;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.Utils.PODType;

/**
 * A component enabling an entity to act as a camera.
 *
 */
public class CameraComponent extends EntityComponent{

    public static final String ID = "CAMERA_COMPONENT";

    private final OrthographicCamera camera;


    class CameraComponentPOD extends EntityComponentPOD{
        @SerializeName("position_x")
        public float positionX;

        @SerializeName("position_y")
        public float positionY;

        public float zoom;
    }




    public CameraComponent(){
        super(true);
        camera = new OrthographicCamera();
    }

    /**
     * Ctor taking a Pod Representation of the current component
     *
     * @param pod
     */
    protected CameraComponent(EntityComponentPOD pod) {
        super(pod);
        camera = new OrthographicCamera();
        CameraComponentPOD camPOD = new CameraComponentPOD();
        camera.zoom = camPOD.zoom;
        camera.position.set(camPOD.positionX, camPOD.positionY, 0);
    }

    /**
     * Constructs a PODType, to be implemented by subclasses
     *
     * @return
     */
    @Override
    protected EntityComponentPOD makePOD() {
        CameraComponentPOD pod = new CameraComponentPOD();
        pod.zoom = camera.zoom;
        pod.positionX = camera.position.x;
        pod.positionY = camera.position.y;
        return pod;
    }

    /**
     * Builds the current object from a pod representation
     *
     * @param pod the pod representation to use
     */
    @Override
    protected void makeFromPOD(EntityComponentPOD pod) {
        CameraComponentPOD camPOD = (CameraComponentPOD)pod;
        camera.position.x = camPOD.positionX;
        camera.position.y = camPOD.positionY;
        camera.zoom = camPOD.zoom;
    }


    @Override
    public String getId() {
        return ID;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}