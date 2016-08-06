package com.goatgames.goatengine.graphicsrendering.camera;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.goatgames.gdk.GAssert;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.core.EntityComponent;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.eventmanager.EntityEvent;
import com.goatgames.goatengine.screenmanager.GameScreenConfig;

/**
 * A system handling all cameras and their movements
 */
public class CameraSystem extends EntitySystem {

    CameraStrategy strategy;


    public CameraSystem(){}


    @Override
    public void init() {
        GoatEngine.logger.info("Camera System initialisation ...");
        GoatEngine.logger.info("Camera System initialised");
    }



    @Override
    public void update(float dt){
        GameScreenConfig config = GoatEngine.gameScreenManager.getCurrentScreen().getConfig();

        for(EntityComponent comp: getEntityManager().getComponents(CameraComponent.ID)){
            CameraComponent camComp = (CameraComponent)comp;
            OrthographicCamera cam = camComp.getCamera();
            if(camComp.isDirty){
                strategy = config.rendering.camera.strategy;
                GAssert.notNull(strategy,"Camera strategy Null");
                cam = new OrthographicCamera(strategy.getWidth(), strategy.getHeight());
                camComp.isDirty = false;
            }
            GAssert.notNull(strategy,"Camera strategy Null");
            cam.viewportWidth = strategy.getWidth();
            cam.viewportHeight = strategy.getHeight();
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
