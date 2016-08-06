package com.goatgames.goatengine.config.gamescreen;

import com.goatgames.goatengine.graphicsrendering.camera.CameraStrategy;
import com.goatgames.goatengine.graphicsrendering.camera.FixedWidthStrategy;

/**
 * Configuration for the rendering system of a game screen
 */
public class RenderingConfig {
    /**
     * Wether or not to render the textures
     */
    public boolean texture = true;

    /**
     * Lighting configuration
     */
    public LightingConfig lighting = new LightingConfig();

    /**
     * If true, renders the physics debug lines.
     */
    public boolean physicsDebug = false;


    /**
     * If true, renders pathfinding nodes.
     */
    public boolean pathfindingDebug = false;

    /**
     * Camera rendering configuration
     */
    public CameraConfig camera = new CameraConfig();

    /**
     * Lighting configuration
     */
    public class LightingConfig{
        /**
         * If true, the rendering system will render
         * the lighting effects.
         */
        public boolean enabled = true;

        /**
         * The ambient light color
         */
        public String ambientLight = "4c6066ff";
    }

    public class CameraConfig {
        /**
         * If true, renders debug lines at the center of the camera.
         */
        public boolean debug = false;

        /**
         * Camera Strategy
         */
        public CameraStrategy strategy = new FixedWidthStrategy(40);

        /**
         * Zoom of the camera
         */
        public float zoom = 1.0f;
    }
}
