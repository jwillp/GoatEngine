package com.goatgames.goatengine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Manages Resources and Assets
 */
public class ResourceManager {

    private AssetManager manager;


    public void init(){
        manager = new AssetManager();
    }



    /**
     * Asynchronously loads a texture atlas in the texture atlas folder
     * First try to find atlas under resources.textures_directory
     * config parameter.
     */
    public void loadTextureAtlast(String atlasResource){
        String texureDirectory  = GoatEngine.config.getString("resources.textures_directory");
        manager.load(texureDirectory + atlasResource, TextureAtlas.class);
    }


    /**
     * Asynchronously loads a font looking into the resources.fonts_directory
     * config parameter.
     * @param fontResource
     */
    public void loadFont(String fontResource){
        String fontDirectory  = GoatEngine.config.getString("resources.fonts_directory");
        manager.load(fontDirectory + fontResource, BitmapFont.class);
    }

    /**
     * Returns a loaded texture (must have been loaded before)
     * @param textureResource
     * @return
     */
    public TextureAtlas getTexture(String textureResource){
        return manager.get(textureResource, TextureAtlas.class);
    }

    /**
     * Returns a loaded Font (must have been loaded before)
     * @param fontResource
     * @return
     */
    public BitmapFont getFont(String fontResource){
        return manager.get(fontResource, BitmapFont.class);
    }

    /**
     * Updates any loading job in progress
     */
    public boolean update(){
        return manager.update();
    }

    /**
     * Returns the progress of any currently loading job
     * @return
     */
    public float getLoadingProgess(){
        return manager.getProgress();
    }

    /**
     * Synchronously loads the assets. This will block until all the resources that have
     * been queued are actually done loading. Kinda defeats the purpose of asynchronous loading,
     * but sometimes one might need it
     * (e.g., loading the resource needed to display the loading screen itself).
     */
    public void finishLoading(){
        manager.finishLoading();
    }

    /**
     * Indicates wether or not an resource has been loaded
     * @return true if asset was loaded false otherwise
     */
    public boolean isLoaded(String resource){
        return manager.isLoaded(resource);
    }

    /**
     * Disposes an resource
     */
    public void unload(String resource){
        manager.unload(resource);
    }

    /**
     * Clears the manager from any previously loaded resources
     */
    public void unloadAll(){
        manager.clear();
    }

    /**
     * Clears the manager from any previously loaded resources
     */
    public void dispose(){
        unloadAll();
    }
}
