package com.goatgames.goatengine;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goatgames.goatengine.utils.GAssert;

/**
 * Manages Resources and Assets
 */
public class ResourceManager {

    private AssetManager manager;
    private final String ATLAS_EXTENSION = ".atlas";


    public void init(){
        manager = new AssetManager();
    }



    /**
     * Asynchronously loads a texture atlas in the texture atlas folder
     * First try to find atlas under resources.textures_directory
     * config parameter.
     */
    public void loadTextureAtlas(String atlasResource){
        String textureDirectory  = GoatEngine.config.getString("resources.textures_directory");
        manager.load(textureDirectory + atlasResource + ATLAS_EXTENSION, TextureAtlas.class);
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
    public TextureRegion getTextureRegion(String textureResource){
        // Resource naming format atlas:resource
        String[] split = textureResource.split(":");

        GAssert.that(split.length == 2, String.format("Invalid texture identifier %s", textureResource));

        String atlasName = split[0];
        String resourceName = split[1];

        String texureDirectory  = GoatEngine.config.getString("resources.textures_directory");


        String assetKey = texureDirectory + atlasName + ATLAS_EXTENSION;
        GAssert.that(manager.isLoaded(assetKey), String.format("Asset not loaded '%s' did you call Load() beforehand?", assetKey));
        TextureAtlas atlas = manager.get(assetKey, TextureAtlas.class);
        return atlas.findRegion(resourceName);
    }

    /**
     * Returns a loaded Font (must have been loaded before)
     * @param fontResource
     * @return
     */
    public BitmapFont getFont(String fontResource){
        return manager.get(fontResource, BitmapFont.class);
    }


    public TextureAtlas getAtlas(String atlasResource){
        String textureDirectory  = GoatEngine.config.getString("resources.textures_directory");
        return manager.get(textureDirectory + atlasResource + ATLAS_EXTENSION, TextureAtlas.class);
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
