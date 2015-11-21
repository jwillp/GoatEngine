package com.brm.GoatEngine.Rendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.brm.GoatEngine.ECS.core.EntitySystem;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Utils.Logger;

/**
 * Responsible for displaying all visual elements on screen
 */
public class RenderingSystem extends EntitySystem {

    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    // SubSystem
    private CameraSystem cameraSystem;


    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        Logger.info("Rendering System initialisation ... ");

        this.cameraSystem = new CameraSystem();
        this.getSystemManager().addSystem(CameraSystem.class, this.cameraSystem);

        debugRenderer = new Box2DDebugRenderer();

        Logger.info("Rendering System initalised");
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {

        cameraSystem.update(dt);

    }

    @Override
    public void draw() {
        renderPhysicsDebug();

        /** CAMERA DEBUG */
        if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().CAMERA_DEBUG_RENDERING){
            new CameraDebugRenderer(cameraSystem.getMainCamera(), shapeRenderer).render();
        }
    }


    /**
     * Renders the Physics Debug
     */
    private void renderPhysicsDebug(){
        // TODO get Info from current Screen to know if we need to render Debug Physics
        this.spriteBatch.begin();
        debugRenderer.render(
                GoatEngine.gameScreenManager.getCurrentScreen().getPhysicsSystem().getWorld(),
                cameraSystem.getMainCamera().combined
        );
        this.spriteBatch.end();

    }









}
