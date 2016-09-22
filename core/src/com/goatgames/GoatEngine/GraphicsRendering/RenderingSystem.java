package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.CrtMonitor;
import com.bitfire.postprocessing.filters.Combine;
import com.bitfire.postprocessing.filters.CrtScreen;
import com.bitfire.utils.ShaderLoader;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.Spriter;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterDrawer;
import com.brashmonkey.spriter.gdxIntegration.LibGdxSpriterLoader;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ai.AISystem;
import com.goatgames.goatengine.ai.pathfinding.PathNode;
import com.goatgames.goatengine.config.gamescreen.RenderingConfig;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.gdk.eventdispatcher.Event;
import com.goatgames.goatengine.eventmanager.GameEventListener;
import com.goatgames.goatengine.eventmanager.engineevents.ScreenResizedEvent;
import com.goatgames.goatengine.graphicsrendering.camera.CameraDebugRenderer;
import com.goatgames.goatengine.graphicsrendering.camera.CameraSystem;
import com.goatgames.goatengine.physics.PhysicsSystem;

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
    private LightSystem lightSystem;


    private TiledMap tiledMap = null;
    private TiledMapRenderer tiledMapRenderer = null;

    private CameraDebugRenderer cameraDebugRenderer;


    private Array<Entity> entitiesByZIndex = new Array<Entity>(); // Entities ordered by Zindex

    CrtMonitor crt;


    /**
     * Used to initialise the system
     */
    @Override
    public void init() {
        GoatEngine.logger.info("GraphicsRendering System initialisation ... ");

        // Camera System
        this.cameraSystem = new CameraSystem();
        this.getSystemManager().addSystem(CameraSystem.class, this.cameraSystem);

        // Physic 2D debug rendering
        debugRenderer = new Box2DDebugRenderer();

        // LightSystem
        lightSystem = new LightSystem(this);
        this.lightSystem.setEntityManager(getEntityManager());

        // Spriter
        Spriter.setDrawerDependencies(spriteBatch, shapeRenderer);
        Spriter.init(LibGdxSpriterLoader.class, LibGdxSpriterDrawer.class);

        // Post processor //
        ShaderLoader.BasePath = "priv_data/shaders/";

        boolean isDesktop = Gdx.app.getType() == Application.ApplicationType.Desktop;
        postProcessor = new PostProcessor(false,false, true);

        int vpW = Gdx.graphics.getWidth();
        int vpH = Gdx.graphics.getHeight();

        int effects = CrtScreen.Effect.TweakContrast.v |
                CrtScreen.Effect.PhosphorVibrance.v |
                CrtScreen.Effect.Scanlines.v |
                CrtScreen.Effect.Tint.v;
        crt = new CrtMonitor( vpW, vpH, false, false, CrtScreen.RgbMode.ChromaticAberrations, effects );
        Combine combine = crt.getCombinePass();
        combine.setSource1Intensity( 0f );
        combine.setSource2Intensity( 0.5f );
        combine.setSource1Saturation( 0f );
        combine.setSource2Saturation( 0.5f );
        crt = new CrtMonitor( vpW, vpH, false, false, CrtScreen.RgbMode.ChromaticAberrations, effects );

        Bloom bloom = new Bloom((int)(Gdx.graphics.getWidth() * 0.25f), (int)(Gdx.graphics.getHeight() * 0.25f));
        bloom.setBlurAmount(20);
        postProcessor.addEffect(crt);
        postProcessor.addEffect(bloom);


        GoatEngine.eventManager.register(this); // TODO use current screen event manager
        GoatEngine.logger.info("GraphicsRendering System initialised");
    }

    /**
     * Called once per game frame
     *
     * @param dt
     */
    @Override
    public void update(float dt){

        for(Entity e: entitiesByZIndex) {
            getEntityManager().freeEntityObject(e);
        }

        //Order entities by ZIndex
        entitiesByZIndex = getEntityManager().getEntitiesWithComponentEnabled(ZIndexComponent.ID);
        Sort.instance().sort(entitiesByZIndex, new ZIndexComponent.ZIndexComparator());

        //crt.setTime( dt * 1000 );
    }

    private final static PreRenderEvent preRenderEvent = new PreRenderEvent();
    private final static PostRenderEvent postRenderEvent = new PostRenderEvent();
    @Override
    public void draw() {

        GoatEngine.graphicsEngine.clearScreen();
        GoatEngine.eventManager.fireEvent(preRenderEvent);

        cameraSystem.update(0); // TODO deltatime  + documenting why this is here instead of update?
        spriteBatch.setProjectionMatrix(cameraSystem.getMainCamera().combined);


        //UPDATE SPRITER
        Spriter.update();


       // postProcessor.capture();

        // Render Map (if necessary)
        if(tiledMap != null){
            if(tiledMapRenderer == null){
                float tileSize = (int)tiledMap.getProperties().get("tilewidth");
                tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap,1/tileSize);
            }
            tiledMapRenderer.setView(getCamera());
            tiledMapRenderer.render();
        }

        spriteBatch.begin();
        // Render entities based on ZIndex
        final RenderingConfig renderingConfig = GoatEngine.gameScreenManager.getCurrentScreen().getConfig().rendering;

        for(Entity e: entitiesByZIndex){
            if(renderingConfig.texture){
                renderSpriterAnimations(e,0);
                renderSprites(e,0);
            }
        }
        spriteBatch.end();

        // Render lights
        if(renderingConfig.lighting.enabled){
            this.lightSystem.draw();
        }

       // postProcessor.render();

        if(renderingConfig.physicsDebug){
            renderPhysicsDebug();
        }

        // PATHFINDING NODES //
        renderPathfinding();


        // CAMERA DEBUG //
        if(renderingConfig.camera.debug){
            if(cameraDebugRenderer == null){
                cameraDebugRenderer = new CameraDebugRenderer(cameraSystem.getMainCamera(), shapeRenderer);
            }
            cameraDebugRenderer.render();
        }
        GoatEngine.eventManager.fireEvent(postRenderEvent);
    }


    @Override
    public void deInit(){
        spriteBatch.dispose();
        shapeRenderer.dispose();
        postProcessor.dispose();
        GoatEngine.eventManager.register(this);
    }




    /**
     * Renders simple 2D game  sprites
     * @param dt
     */
    private void renderSprites(Entity entity, float dt){
        if(entity.hasComponentEnabled(SpriteComponent.ID)){
            SpriteComponent sprite = (SpriteComponent) entity.getComponent(SpriteComponent.ID);
            TransformComponent transform = (TransformComponent)entity.getComponent(TransformComponent.ID);
            spriteBatch.setColor(sprite.getColor());

            float width;        // The display width of the sprite
            float height;       // The display height of the sprite
            float x;            // The display x coordinate of the sprite
            float y;            // The display y coordinate of the sprite

            // Get Texture Region
            TextureRegion region = GoatEngine.resourceManager.getTextureRegion(sprite.getResource());
            // Auto adjust the sprite to it's transform component
            if(sprite.autoAdjust){
                //float ratio = sprite.getCurrentSprite().getRegionWidth()/sprite.getCurrentSprite().getRegionHeight();
                width = transform.getWidth() * 2;
                height = transform.getHeight() * 2;
                x = transform.getX() - transform.getWidth() + sprite.offsetX;
                y = transform.getY() - transform.getHeight() + sprite.offsetY;
            }else{
                //Use the image size and the scaling factor
                width = region.getRegionWidth() * sprite.scale;
                height = region.getRegionHeight() * sprite.scale;
                x = transform.getX() - width  * 0.5f + sprite.offsetX;
                y = transform.getY() - height * 0.5f + sprite.offsetY;
            }

            spriteBatch.draw(region, x, y, width, height);
            spriteBatch.setColor(Color.WHITE); // Reset Sprite batch color
        }
    }

    /**
     * Renders spriter animations
     * @param delta the delta time
     */
    private void renderSpriterAnimations(Entity entity, float delta){
        if(entity.hasComponentEnabled(SpriterAnimationComponent.ID)){
            SpriterAnimationComponent anim = (SpriterAnimationComponent)entity.getComponent(SpriterAnimationComponent.ID);
            TransformComponent transform = (TransformComponent)  entity.getComponent(TransformComponent.ID);

            // 15 == Default speed
            anim.getPlayer().speed = !GoatEngine.gameScreenManager.isRunning() ? 0 : 15;

            if(!anim.isEnabled()){
                //anim.getPlayer().setScale(0); // Fake not draw
                //TODO Fix this
            }else{
                //anim.getPlayer().speed = GoatEngine.gameScreenManager.isRunning() ? 1 : 0;
                float posX = transform.getX() + anim.getOffsetX();
                float posY =  transform.getY() + anim.getOffsetY();
                anim.getPlayer().setPosition(posX, posY);
                anim.getPlayer().setAngle(transform.getRotation());
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
        World world = GoatEngine.gameScreenManager.getCurrentScreen().getEntitySystemManager().getSystem(PhysicsSystem.class).getWorld();
        debugRenderer.render(
                world,
                cameraSystem.getMainCamera().combined
        );
        this.spriteBatch.end();
    }

    /**
     * Debug method to render the path and nodes of AI
     */
    private void renderPathfinding() {
        if(!GoatEngine.gameScreenManager.getCurrentScreen().getConfig().rendering.pathfindingDebug) return;
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
    public boolean onEvent(Event e) {
        if(e instanceof ScreenResizedEvent){
            onScreenResize((ScreenResizedEvent) e);
        }
        return false;
    }

    private void onScreenResize(ScreenResizedEvent e) {
        this.lightSystem.onResize(e.newWidth, e.newHeight);
        if(GoatEngine.config.dev_ctx){
            Gdx.graphics.setTitle(GoatEngine.config.game.name + "[" + e.newWidth + "x" +  e.newHeight + "]");
        }
    }


    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public OrthographicCamera getCamera() {
        return cameraSystem.getMainCamera();
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void setTiledMap(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
    }
}
