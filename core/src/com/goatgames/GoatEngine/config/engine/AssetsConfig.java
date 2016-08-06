package com.goatgames.goatengine.config.engine;

/**
 * Asset configuration
 */
public class AssetsConfig {
    /**
     * Directory containing the texture atlases and their sprites
     */
    public String texturesDirectory = "sprites/";

    /**
     * Directory containing fonts
     */
    public String fontsDirectory = "fonts/";

    /**
     * Automatically load resources, when needed.
     * Should be true only in debug
     */
    public boolean autoLoad;

    /**
     * Maps directory
     */
    public String mapDirectory;
}
