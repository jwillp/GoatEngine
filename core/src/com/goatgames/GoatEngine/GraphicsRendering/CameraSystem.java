package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.EntityEvent;
import com.goatgames.goatengine.utils.Logger;

import java.util.ArrayList;

/**
 * A system handling all cameras and their movements
 * The camera will always try to display every entity
 * with a CameraTargetComponent
 */
public class CameraSystem extends EntitySystem {

    Viewport viewport;

    public CameraSystem(){}

    @Override
    public void init() {
        Logger.info("Camera System initialisation ...");
        Logger.info("Camera System initialised");
    }



    @Override
    public void update(float dt){
        if(viewport == null)
            this.viewport = new FitViewport(80, 48, getMainCamera());
        if(getMainCamera() == null) return;
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }


    @Override
    public <T extends EntityEvent> void onEntityEvent(T event) {

    }

    /**
     * Returns the orthographic camera
     * @return
     */
    public OrthographicCamera getMainCamera() {
        try{
            Array<EntityComponent> comps = getEntityManager().getComponents(CameraComponent.ID);
            CameraComponent camComp = (CameraComponent) comps.get(0);
            return camComp.getCamera();
        }catch (IndexOutOfBoundsException e){
            throw new NoCameraAvailableException();
        }
    }


    private class NoCameraAvailableException extends RuntimeException {
        public NoCameraAvailableException() {
            super("Could not find any camera, please create one");
        }
    }

}