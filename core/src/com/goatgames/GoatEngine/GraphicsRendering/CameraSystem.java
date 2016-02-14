package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.EntityEvent;
import com.goatgames.goatengine.screenmanager.GameScreenConfig;
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
        GameScreenConfig config = GoatEngine.gameScreenManager.getCurrentScreen().getConfig();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        for(EntityComponent comp: getEntityManager().getComponents(CameraComponent.ID)){
            CameraComponent camComp = (CameraComponent)comp;
            OrthographicCamera cam = camComp.getCamera();
            if(camComp.isDirty){


                float viewportWidth = config.getFloat("rendering.camera.viewportWidth");
                float viewportHeight = config.getFloat("rendering.camera.viewportHeight");
                float zoom = config.getFloat("rendering.camera.zoom");
                cam = new OrthographicCamera(30, 30 * (h / w));

                camComp.isDirty = false;
            }


            float viewportWidth = config.getFloat("rendering.camera.viewportWidth");
            float viewportHeight = config.getFloat("rendering.camera.viewportHeight");


            cam.viewportWidth = viewportWidth;                 // Viewport of 30 units!
            cam.viewportHeight = viewportHeight * h/w;          // Lets keep things in proportion.

            // The following resize strategy will show less/more of the world depending on the resolution
            /*cam.viewportWidth = w/viewportWidth;  //We will see width/32f units!
            cam.viewportHeight = cam.viewportWidth * h/w;*/


            cam.update();
        }




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