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
import com.goatgames.goatengine.ecs.core.Entity;
import com.goatgames.goatengine.ecs.core.EntitySystem;
import com.goatgames.goatengine.physics.PhysicsComponent;

/**
 * System responsible of rendering Fake lights
 */
public class FakeLightSystem extends EntitySystem {

    private RenderingSystem renderingSystem;
    SpriteBatch spriteBatch;

    private FrameBuffer lightBuffer;
    private TextureRegion lightBufferRegion;

    private Matrix4 overlayMatrix;





    public FakeLightSystem(RenderingSystem renderingSystem){
        this.renderingSystem = renderingSystem;
        spriteBatch = renderingSystem.getSpriteBatch();
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
        Color c = Color.valueOf(GoatEngine.gameScreenManager.getCurrentScreen().getConfig().AMBIENT_LIGHT);
        Gdx.gl.glClearColor(c.r,c.g,c.b,c.a);
        //Gdx.gl.glClearColor(0.3f,0,0f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // start rendering the lights to our spriteBatch
        spriteBatch.begin();

        int lowDisplayW = Gdx.graphics.getWidth();
        int lowDisplayH = Gdx.graphics.getHeight();
        int displayW = lowDisplayW;
        int displayH = lowDisplayH;


        // and render the sprite
        for(Entity e: getEntityManager().getEntitiesWithComponent(FakeLightComponent.ID)){
           this.renderSprites(e);
            getEntityManager().freeEntity(e);
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

    }






    private void renderSprites(Entity entity){
        FakeLightComponent light = (FakeLightComponent) entity.getComponent(FakeLightComponent.ID);
        PhysicsComponent phys = (PhysicsComponent) entity.getComponent(PhysicsComponent.ID);
        spriteBatch.setColor(light.getColor());
        if(light.autoAdjust){

            float width = phys.getWidth();
            spriteBatch.draw(light.getCurrentSprite(),
                    phys.getPosition().x - width + light.offsetX,
                    phys.getPosition().y - phys.getHeight() + light.offsetY,
                    width * 2,
                    phys.getHeight() * 2
            );
        }else{
            float width = light.getCurrentSprite().getRegionWidth() * light.scale;
            float height = light.getCurrentSprite().getRegionHeight() * light.scale;
            spriteBatch.draw(
                    light.getCurrentSprite(),
                    phys.getPosition().x - width * 0.5f + light.offsetX,
                    phys.getPosition().y - height * 0.5f + light.offsetY,
                    width,
                    height

            );
        }
    }











}
