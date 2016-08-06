package com.goatgames.goatengine.graphicsrendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.GL20;
import com.goatgames.goatengine.GoatEngine;

/**
 * Graphics Engine abstracting Gdx Calls
 * DANGER: This should only be used by the rendering thread
 */
public class GraphicsEngine{


    public void init(){

        // get the current display mode of the monitor the window is on

        // set the window to fullscreen mode ?
        Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
        if(GoatEngine.config.fullscreen){
            Gdx.graphics.setFullscreenMode(mode);
        }else{
            int width = GoatEngine.config.resolution.width;
            int height = GoatEngine.config.resolution.height;
            if(width == -1){
                width = mode.width;
            }
            if(height == -1){
                height = mode.height;
            }
            Gdx.graphics.setWindowedMode(width,height);
        }
        Gdx.graphics.setTitle(GoatEngine.config.game.name);

    }

    /**
     * Delta time between frames
     * @return
     */
    public float getDeltaTime(){
        return Gdx.graphics.getDeltaTime();
    }

    /**
     * Number of frames per second
     * @return
     */
    public float getFPS(){
        return Gdx.graphics.getFramesPerSecond();
    }

    /**
     * Returns the screen Width
     * @return
     */
    public int getScreenWidth(){
        return Gdx.graphics.getWidth();
    }

    /**
     * Returns the screen Width
     * @return
     */
    public int getScreenHeight(){
        return Gdx.graphics.getHeight();
    }


    public void clearScreen(){
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    }


    public void beginrender(){
        // TODO Spritebatch call
    }


    public void endRender(){
        // TODO spritebatch call
    }



    public boolean isFullScreen(){
        return Gdx.graphics.isFullscreen();
    }

    public void setFullScreen(boolean fullScreen){

    }




}
