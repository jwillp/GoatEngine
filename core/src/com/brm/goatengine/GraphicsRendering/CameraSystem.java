package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brm.GoatEngine.ECS.core.EntityComponent;
import com.brm.GoatEngine.ECS.core.EntitySystem;
import com.brm.GoatEngine.EventManager.EntityEvent;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.Utils.Logger;

import java.util.ArrayList;

/**
 * A system handling all cameras and their movements
 * The camera will always try to display every entity
 * with a CameraTargetComponent
 */
public class CameraSystem extends EntitySystem {

    private Viewport viewport;

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
            ArrayList<EntityComponent> comps = getEntityManager().getComponents(CameraComponent.ID);
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