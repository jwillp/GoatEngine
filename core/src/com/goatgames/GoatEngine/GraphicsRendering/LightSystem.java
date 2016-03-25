package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.ecs.common.TransformComponent;
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.physics.PhysicsComponent;

/**
 * System responsible of rendering Fake lights
 */
public class LightSystem extends EntitySystem {

    private RenderingSystem renderingSystem;
    SpriteBatch spriteBatch;

    private FrameBuffer lightBuffer;
    private TextureRegion lightBufferRegion;

    private Matrix4 overlayMatrix;





    public LightSystem(RenderingSystem renderingSystem){
        this.renderingSystem = renderingSystem;
        spriteBatch = renderingSystem.getSpriteBatch();
        onResize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    /**
     * Used to initialise the system
     */
    @Override
    public void init() {

    }

    public void initMatrix(){
        overlayMatrix = renderingSystem.getCamera().combined.cpy();
        overlayMatrix.setToOrtho2D(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
    public void draw(){
        // start rendering to the lightBuffer
        lightBuffer.begin();


        // setup the right blending
        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);




        // set the ambient color values, this is the "global" light of your scene
        // imagine it being the sun.  Usually the alpha value is just 1, and you change the darkness/brightness
        // with the Red, Green and Blue values for best effect
        String colorStr = GoatEngine.gameScreenManager.getCurrentScreen().getConfig().getString("rendering.lighting.ambient_light");
        Color c = Color.valueOf(colorStr);
        Gdx.gl.glClearColor(c.r,c.g,c.b,c.a);
        //Gdx.gl.glClearColor(0.3f,0,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // start rendering the lights to our spriteBatch
        spriteBatch.begin();

        int displayW = Gdx.graphics.getWidth();
        int displayH = Gdx.graphics.getHeight();


        // and render the sprite
        for(Entity e: getEntityManager().getEntitiesWithComponentEnabled(LightComponent.ID)){
           this.renderSprites(e);
            getEntityManager().freeEntityObject(e);
        }

        spriteBatch.end();
        lightBuffer.end();

        //spriteBatch.setProjectionMatrix(gameCamera.combined);



        // now we render the lightBuffer to the default "frame buffer"
        // with the right blending !

        spriteBatch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_ZERO);
        spriteBatch.setColor(Color.WHITE);
        if(overlayMatrix == null) initMatrix();
        spriteBatch.setProjectionMatrix(overlayMatrix);
        spriteBatch.begin();
        spriteBatch.draw(lightBufferRegion,0, 0,displayW,displayH);
        spriteBatch.end();

        // draw fbo without fancy blending, for debug
        /*float scale = 0.4f;
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        spriteBatch.begin();
        spriteBatch.draw(lightBufferRegion, 0, displayH - displayH * scale, displayW * scale, displayH * scale);
        spriteBatch.end();*/

        // BLEND ALPHA
        spriteBatch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        // post light-rendering
        // you might want to render your status bar stuff here
    }

    public void onResize(int newWidth, int newHeight){
        // Fakedlight system (alpha blending)

        // if lightBuffer was created before, dispose, we recreate a new one
        if (lightBuffer!=null) lightBuffer.dispose();
        lightBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                newWidth,
                newHeight,
                false);

        lightBuffer.getColorBufferTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        lightBufferRegion = new TextureRegion(
                lightBuffer.getColorBufferTexture(),
                0,
                lightBuffer.getHeight()- newHeight
                , newWidth,
                newHeight);

        lightBufferRegion.flip(false, true);

        // Update projection Matrix
        if(overlayMatrix != null)
            initMatrix();
    }






    private void renderSprites(Entity entity){
        LightComponent light = (LightComponent) entity.getComponent(LightComponent.ID);
        TransformComponent transform = (TransformComponent) entity.getComponent(TransformComponent.ID);
        float width;        // The display width of the sprite
        float height;       // The display height of the sprite
        float x;            // The display x coordinate of the sprite
        float y;            // The display y coordinate of the sprite

        // Get Texture Region
        TextureRegion region = GoatEngine.resourceManager.getTextureRegion(light.getResource());
        // Auto adjust the sprite to it's transform component
        if(light.autoAdjust){
            //float ratio = sprite.getCurrentSprite().getRegionWidth()/sprite.getCurrentSprite().getRegionHeight();
            width = transform.getWidth() * 2;
            height = transform.getHeight() * 2;
            x = transform.getX() - transform.getWidth() + light.offsetX;
            y = transform.getY() - transform.getHeight() + light.offsetY;
        }else{
            //Use the image size and the scaling factor
            width = region.getRegionWidth() * light.scale;
            height = region.getRegionHeight() * light.scale;
            x = transform.getX() - width  * 0.5f + light.offsetX;
            y = transform.getY() - height * 0.5f + light.offsetY;
        }

        spriteBatch.draw(region, x, y, width, height);
    }











}
