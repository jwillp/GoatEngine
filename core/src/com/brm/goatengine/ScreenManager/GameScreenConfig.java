package com.brm.GoatEngine.ScreenManager;

import com.brm.GoatEngine.Utils.GameConfig;
import com.brm.GoatEngine.Utils.OrderedProperties;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Game screen config
 */
public class GameScreenConfig extends GameConfig {

    // CONFIG
    public String LEVEL_CONFIG = "";

    // [PHYSICS]
    public float GRAVITY_X = 0.0f;
    public float GRAVITY_Y = 0.0f;

    // [RENDERING]
    public boolean PHYSICS_DEBUG_RENDERING = false;

    public boolean CAMERA_DEBUG_RENDERING = false;
    public boolean TEXTURE_RENDERING = true;
    public boolean FOG_RENDERING = true;
    public boolean LIGHTING_RENDERING = true;







    public void loadConfig(String gameScreenConfigFile) throws IOException {
        FileInputStream inputStream;
        inputStream = new FileInputStream(gameScreenConfigFile);
        OrderedProperties prop = new OrderedProperties();
        prop.load(inputStream);

        this.LEVEL_CONFIG = prop.getProperty("map_config_file"); // Required

        // [PHYSICS]
        GRAVITY_X = GameConfig.getFloatProperty(GRAVITY_X, prop.getProperty("gravity_x"));
        GRAVITY_Y = GameConfig.getFloatProperty(GRAVITY_Y, prop.getProperty("gravity_y"));



        // [RENDERING]
        this.PHYSICS_DEBUG_RENDERING = GameConfig.getBooleanProperty(
                this.PHYSICS_DEBUG_RENDERING,
                prop.getProperty("physics_debug_rendering")
        );

        this.CAMERA_DEBUG_RENDERING = GameConfig.getBooleanProperty(
                this.CAMERA_DEBUG_RENDERING,
                prop.getProperty("camera_debug_rendering")
        );

        this.TEXTURE_RENDERING = GameConfig.getBooleanProperty(this.TEXTURE_RENDERING, prop.getProperty("texture_rendering"));

        this.FOG_RENDERING = GameConfig.getBooleanProperty(this.FOG_RENDERING, prop.getProperty("fog_rendering"));

        this.LIGHTING_RENDERING = GameConfig.getBooleanProperty(this.LIGHTING_RENDERING, prop.getProperty("lighting_rendering"));


    }






}
