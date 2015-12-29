package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.*;
import com.bitfire.utils.ShaderLoader;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterDrawer;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterLoader;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.ai.AISystem;
import com.goatgames.goatengine.ai.Pathfinding.PathNode;
import com.goatgames.goatengine.eventmanager.GameEvent;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.eventmanager.engineevents.EngineEvents;
import com.goatgames.goatengine.physics.PhysicsComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.utils.Logger;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Responsible for displaying all visual elements on screen
 */
public class RenderingSystem extends EntitySystem implements GameEventListener{

    private Box2DDebugRenderer debugRenderer;
    private SpriteBatch spriteBatch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    private PostProcessor postProcessor;

    // SubSystem
    private CameraSystem cameraSystem;

    private CameraDebugRenderer cameraDebugRenderer;


    private ArrayList<Entity> entitiesByZIndex = new ArrayList<Entity>(); // Entities ordered by Zindex

    CrtMonitor crt;



    // LightBuffer
    FrameBuffer lightBuffer;
    TextureRegion lightBufferRegion;










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



        // Post processor //
        ShaderLoader.BasePath = "data/shaders/";



        boolean isDesktop = Gdx.app.getType() == Application.ApplicationType.Desktop;
        postProcessor = new PostProcessor(false,false, true);

        int vpW = Gdx.graphics.getWidth();
        int vpH = Gdx.graphics.getHeight();

        /*int effects = CrtScreen.Effect.TweakContrast.v |
                CrtScreen.Effect.PhosphorVibrance.v |
                CrtScreen.Effect.Scanlines.v |
                CrtScreen.Effect.Tint.v;
        crt = new CrtMonitor( vpW, vpH, false, false, CrtScreen.RgbMode.ChromaticAberrations, effects );
        Combine combine = crt.getCombinePass();
        combine.setSource1Intensity( 0f );
        combine.setSource2Intensity( 1f );
        combine.setSource1Saturation( 0f );
        combine.setSource2Saturation( 1f );
        crt = new CrtMonitor( vpW, vpH, false, false, CrtScreen.RgbMode.ChromaticAberrations, effects );*/

        Bloom bloom = new Bloom((int)(Gdx.graphics.getWidth() * 0.25f), (int)(Gdx.graphics.getHeight() * 0.25f));
        bloom.setBlurAmount(20);
       // postProcessor.addEffect(crt);
        postProcessor.addEffect(bloom);


        GoatEngine.eventManager.registerListener(this);
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

        //crt.setTime( dt * 1000 );
    }

    @Override
    public void draw() {
        cameraSystem.update(0); // TODO deltatime  + documenting why this is here instead of update?
        spriteBatch.setProjectionMatrix(cameraSystem.getMainCamera().combined);


        //UPDATE SPRITER
        Spriter.update();


        postProcessor.capture();
        spriteBatch.begin();

        // Render entities based on ZIndex
        for(Entity e: entitiesByZIndex){
            if(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().TEXTURE_RENDERING){
                renderSpriterAnimations(e,0);
                renderSprites(e,0);
            }
        }


        // Render lights
        //renderFakeLights();



        spriteBatch.end();

        postProcessor.render();

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
     * Renders fake light
     */
    private void renderFakeLights() {
        // start rendering to the lightBuffer
        lightBuffer.begin();

        // setup the right blending
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        // set the ambient color values, this is the "global" light of your scene
        // imagine it being the sun.  Usually the alpha value is just 1, and you change the darkness/brightness
        // with the Red, Green and Blue values for best effect

        Gdx.gl.glClearColor(0.3f,0.38f,0.4f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // start rendering the lights to our spriteBatch
        spriteBatch.begin();

        int lowDisplayW = Gdx.graphics.getWidth();
        int lowDisplayH = Gdx.graphics.getHeight();
        int displayW = lowDisplayW;
        int displayH = lowDisplayH;

        // set the color of your light (red,green,blue,alpha values)
        spriteBatch.setColor(0.9f, 0.4f, 0f, 1f);

        // tx and ty contain the center of the light source
        float tx= (lowDisplayW/2);
        float ty= (lowDisplayH/2);

        // tw will be the size of the light source based on the "distance"
        // (the light image is 128x128)
        // and 96 is the "distance"
        // Experiment with this value between based on your game resolution
        // my lights are 8 up to 128 in distance
        float tw=(128/100f)*96;

        // make sure the center is still the center based on the "distance"
        tx-=(tw/2);
        ty-=(tw/2);

        // and render the sprite
        // TODO for every fake light components
        // spriteBatch.draw(lightSprite, tx,ty,tw,tw,0,0,128,128,false,true);

        spriteBatch.end();
        lightBuffer.end();


        // now we render the lightBuffer to the default "frame buffer"
        // with the right blending !

        Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        spriteBatch.begin();
        spriteBatch.draw(lightBufferRegion, 0, 0,displayW,displayH);
        spriteBatch.end();

        // post light-rendering
        // you might want to render your status bar stuff here

    }

    private void updateFakeLights(int newWidth, int newHeight){
        if(true) return;
        // Fakedlight system (alpha blending)
        int lowDisplayW = newWidth;
        int lowDisplayH = newHeight;

        // if lightBuffer was created before, dispose, we recreate a new one
        if (lightBuffer!=null) lightBuffer.dispose();
        lightBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                (int)Math.pow(lowDisplayW,2),
                (int)Math.pow(lowDisplayH,2),
                false);

        lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        lightBufferRegion = new TextureRegion(
                lightBuffer.getColorBufferTexture(),
                0,
                lightBuffer.getHeight()-lowDisplayH
                ,lowDisplayW,
                lowDisplayH);

        lightBufferRegion.flip(false, false);
    }



    @Override
    public void deInit(){
        spriteBatch.dispose();
        shapeRenderer.dispose();
        postProcessor.dispose();
        GoatEngine.eventManager.unregisterListener(this);
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


    @Override
    public void onEvent(GameEvent e) {
        if(e instanceof EngineEvents.ScreenResizedEvent){
            onScreenResize((EngineEvents.ScreenResizedEvent) e);
        }
    }

    private void onScreenResize(EngineEvents.ScreenResizedEvent e) {
        updateFakeLights(e.newWidth, e.newHeight);

        if(GEConfig.DevGeneral.DEV_CTX){ Gdx.graphics.setTitle("[" + e.newWidth + "x" +  e.newHeight + "]"); }
    }



}
