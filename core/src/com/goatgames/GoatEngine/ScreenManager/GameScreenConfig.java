package com.goatgames.goatengine.screenmanager;


import com.goatgames.goatengine.config.gamescreen.PhysicsConfig;
import com.goatgames.goatengine.config.gamescreen.RenderingConfig;

/**
 * Game screen config
 */
public class GameScreenConfig {

    /**
     * Entity definitions to be loaded.
     */
    public String level = null;

    /**
     * Path to a tiled Map to load automatically.
     */
    public String tmxMap = null;

    /**
     * Physics System configuration
     */
    public PhysicsConfig physics = new PhysicsConfig();

    /**
     * Rendering System configuration
     */
    public RenderingConfig rendering = new RenderingConfig();
}
