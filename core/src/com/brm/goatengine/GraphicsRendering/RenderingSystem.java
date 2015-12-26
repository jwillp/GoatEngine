package com.brm.GoatEngine.GraphicsRendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterDrawer;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterLoader;
import com.brm.GoatEngine.AI.AISystem;
import com.brm.GoatEngine.AI.Pathfinding.PathNode;
import com.brm.GoatEngine.ECS.common.PhysicsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.ECS.core.EntitySystem;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.Utils.Logger;

import java.util.ArrayList;
import java.util.Collections;

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


    private ArrayList<Entity> entitiesByZIndex = new ArrayList<Entity>(); // Entities ordered by Zindex



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
    public void update(float dt){

        for(Entity e: entitiesByZIndex) {
            getEntityManager().freeEntity(e);
        }
        //Order entities by ZIndex // TODO When TransformComponent will exist use it's Z position instead
        entitiesByZIndex = getEntityManager().getEntitiesWithComponent(ZIndexComponent.ID);
        Collections.sort(entitiesByZIndex, new ZIndexComponent.ZIndexComparator());
    }

    @Override
    public void draw() {
        cameraSystem.update(0); // TODO deltatime  + documenting why this is here instead of update?
        spriteBatch.setProjectionMatrix(cameraSystem.getMainCamera().combined);


        //UPDATE SPRITER


        Spriter.update();

        // Render entities based on ZIndex
        for(Entity e: entitiesByZIndex){

            if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().TEXTURE_RENDERING){
                spriteBatch.begin();
                renderSpriterAnimations(e,0);
                renderSprites(e,0);
                spriteBatch.end();
            }
        }


        if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().PHYSICS_DEBUG_RENDERING){
            renderPhysicsDebug();
        }


        // PATHFINDING NODES //
        renderPathfinding();


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
    private void renderSprites(Entity entity, float dt){
        if(entity.hasComponentEnabled(SpriteComponent.ID)){
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
    private void renderSpriterAnimations(Entity entity, float delta){
        if(entity.hasComponentEnabled(SpriterAnimationComponent.ID)){
            SpriterAnimationComponent anim = (SpriterAnimationComponent)entity.getComponent(SpriterAnimationComponent.ID);
            PhysicsComponent phys = (PhysicsComponent)  entity.getComponent(PhysicsComponent.ID);

            // 15 == Default speed
            anim.getPlayer().speed = !GoatEngine.gameScreenManager.isRunning() ? 0 : 15;

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
                Spriter.drawer().setLoader((Loader)Spriter.getLoader(anim.getAnimationFile()));
                Spriter.drawer().draw(anim.getPlayer());
            }

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








    /**
     * Debug method to render the path and nodes of AI
     */
    private void renderPathfinding() {
        if(!GoatEngine.gameScreenManager.getCurrentScreen().getConfig().PATFINDING_DEBUG_RENDERING) return;
        float NODE_SIZE = 0.4f;
        for(PathNode node: AISystem.pathfinder.nodes) {
            shapeRenderer.setProjectionMatrix(this.cameraSystem.getMainCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            if (node.isWalkable)
                shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(node.position.x, node.position.y, NODE_SIZE, NODE_SIZE);
            shapeRenderer.end();
        }

        //Pathfinding display
        /*for (Entity e : this.getEntityManager().getEntitiesWithComponent(AIComponent.ID)){

           // AIComponent aiComp = (AIComponent) e.getComponent(AIComponent.ID);

            //NODES
            for(PathNode node: aiComp.getCurrentPath()) {
            for(PathNode node: AISystem.pathfinder.openNodes) {
                shapeRenderer.setProjectionMatrix(this.cameraSystem.getMainCamera().combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                if (node.isWalkable)
                    shapeRenderer.setColor(Color.RED);
                if(aiComp.getCurrentPath().indexOf(node) == 0)
                    shapeRenderer.setColor(Color.GREEN);
                if(aiComp.getCurrentPath().indexOf(node) == aiComp.getCurrentPath().size()-1)
                    shapeRenderer.setColor(Color.MAGENTA);
                shapeRenderer.rect(node.position.x, node.position.y, NODE_SIZE, NODE_SIZE);
                shapeRenderer.end();
            }

            // PATH
            /*for(PathNode node: aiComp.getCurrentPath()){
                if(node.parent != null){
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // shape type
                    shapeRenderer.setColor(1, 0, 0, 1); // line's color

                    float offset = NODE_SIZE/2;
                    shapeRenderer.line(
                            node.position.x + offset , node.position.y + offset,
                            node.parent.position.x + offset, node.parent.position.y + offset
                    );
                    shapeRenderer.end();
                }
            }*/
        }




}
