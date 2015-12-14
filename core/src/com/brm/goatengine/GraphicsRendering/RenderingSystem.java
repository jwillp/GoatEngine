package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterDrawer;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterLoader;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
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

    private CameraDebugRenderer cameraDebugRenderer;


    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        Logger.info("GraphicsRendering System initialisation ... ");

        // Camera System
        this.cameraSystem = new CameraSystem();
        this.getSystemManager().addSystem(CameraSystem.class, this.cameraSystem);

        // Physic 2D debug rendering
        debugRenderer = new Box2DDebugRenderer();

        // Spriter
        Spriter.setDrawerDependencies(spriteBatch, shapeRenderer);
        Spriter.init(LibGdxSpriterLoader.class, LibGdxSpriterDrawer.class);

        Logger.info("GraphicsRendering System initalised");
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt) {

    }

    @Override
    public void draw() {
        cameraSystem.update(0); // TODO deltatime  + documenting why this is here
        spriteBatch.setProjectionMatrix(cameraSystem.getMainCamera().combined);



        if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().TEXTURE_RENDERING){
            spriteBatch.begin();
            renderSpriterAnimations(0);
            renderSprites(0);
            spriteBatch.end();
        }

        if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().PHYSICS_DEBUG_RENDERING){
            renderPhysicsDebug();
        }


        // CAMERA DEBUG //
        if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().CAMERA_DEBUG_RENDERING){
            if(cameraDebugRenderer == null){
                cameraDebugRenderer = new CameraDebugRenderer(cameraSystem.getMainCamera(), shapeRenderer);
            }
            cameraDebugRenderer.render();
        }
    }

    /**
     * Renders simple 2D game  sprites
     * @param dt
     */
    private void renderSprites(float dt){
        for(Entity entity: getEntityManager().getEntitiesWithComponentEnabled(SpriteComponent.ID)){
            SpriteComponent sprite = (SpriteComponent) entity.getComponent(SpriteComponent.ID);
            PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
            if(sprite.autoAdjust){

                float ratio = sprite.getCurrentSprite().getRegionWidth()/sprite.getCurrentSprite().getRegionHeight();
                float width = phys.getWidth();
                spriteBatch.draw(sprite.getCurrentSprite(),
                        phys.getPosition().x - width + sprite.offsetX,
                        phys.getPosition().y - phys.getHeight() + sprite.offsetY,
                        width * 2,
                        phys.getHeight() * 2
                );
            }else{
                float width = sprite.getCurrentSprite().getRegionWidth() * sprite.scale;
                float height = sprite.getCurrentSprite().getRegionHeight() * sprite.scale;
                spriteBatch.draw(
                        sprite.getCurrentSprite(),
                        phys.getPosition().x - width + sprite.offsetX,
                        phys.getPosition().y - height + sprite.offsetY,
                        width,
                        height

                );
            }

        }
    }

    /**
     * Renders spriter animations
     * @param delta the delta time
     */
    private void renderSpriterAnimations(float delta){
        //UPDATE SPRITER
        Spriter.update();
        for(Entity entity: getEntityManager().getEntitiesWithComponentEnabled(SpriterAnimationComponent.ID)){
            SpriterAnimationComponent anim = (SpriterAnimationComponent)entity.getComponent(SpriterAnimationComponent.ID);
            PhysicsComponent phys = (PhysicsComponent)  entity.getComponent(PhysicsComponent.ID);

            if(!anim.isEnabled()){
                //anim.getPlayer().setScale(0); // Fake not draw
                //TODO Fix this
            }else{
                //anim.getPlayer().speed = GoatEngine.gameScreenManager.isRunning() ? 1 : 0;
                float posX = phys.getPosition().x + anim.getOffsetX();
                float posY =  phys.getPosition().y + anim.getOffsetY();
                anim.getPlayer().setPosition(posX, posY);
                anim.getPlayer().setAngle(phys.getBody().getAngle());
                anim.getPlayer().setScale(anim.getScale());
            }
        }
        Spriter.draw();
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
