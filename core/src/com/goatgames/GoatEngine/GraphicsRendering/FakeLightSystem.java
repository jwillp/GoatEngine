package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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
    TextureRegion lightBufferRegion;



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
        //spriteBatch.setColor(0.9f, 0.4f, 0f, 1f);
        spriteBatch.setColor(0.2f, 0.4f, 0.5f, 1f);


        // and render the sprite
        // TODO for every fake light components
        for(Entity e: getEntityManager().getEntitiesWithComponent(FakeLightComponent.ID)){
           this.renderSprites(e);
            getEntityManager().freeEntity(e);
        }

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

    public void onResize(int newWidth, int newHeight){
        // Fakedlight system (alpha blending)
        int lowDisplayW = newWidth;
        int lowDisplayH = newHeight;

        // if lightBuffer was created before, dispose, we recreate a new one
        if (lightBuffer!=null) lightBuffer.dispose();
        lightBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                lowDisplayW,
                lowDisplayH,
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






    private void renderSprites(Entity entity){
        FakeLightComponent sprite = (FakeLightComponent) entity.getComponent(FakeLightComponent.ID);
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
