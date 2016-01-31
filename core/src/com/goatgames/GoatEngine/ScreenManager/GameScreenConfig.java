package com.goatgames.goatengine.screenmanager;

import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.utils.EngineConfig;
import com.goatgames.goatengine.utils.GameConfig;
import com.goatgames.goatengine.utils.OrderedProperties;

import java.io.FileInputStream;
import java.io.IOException;


/**
 * Game screen config
 */
public class GameScreenConfig extends EngineConfig {

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

    public String AMBIENT_LIGHT = "FFFFFFFF"; // WHITE

    public boolean PATHFINDER_DEBUG_RENDERING = false;



    public void loadConfig(String gameScreenConfigFile) throws IOException {
        FileInputStream inputStream;
        inputStream = new FileInputStream(gameScreenConfigFile);
        OrderedProperties prop = new OrderedProperties();
        prop.load(inputStream);

        this.LEVEL_CONFIG = GEConfig.DATA_DIR + prop.getProperty("map_config_file"); // Required

        // [PHYSICS]
        GRAVITY_X = Float.parseFloat(prop.getProperty("gravity_x", String.valueOf(GRAVITY_X)));
        GRAVITY_Y = Float.parseFloat(prop.getProperty("gravity_y", String.valueOf(GRAVITY_Y)));



        // [RENDERING]
        this.PHYSICS_DEBUG_RENDERING = Boolean.parseBoolean(
                prop.getProperty("physics_debug_rendering", String.valueOf(this.PHYSICS_DEBUG_RENDERING)));

        this.CAMERA_DEBUG_RENDERING = Boolean.parseBoolean(
                prop.getProperty("camera_debug_rendering", String.valueOf(this.CAMERA_DEBUG_RENDERING)));

        this.TEXTURE_RENDERING = Boolean.parseBoolean(
                prop.getProperty("texture_rendering", String.valueOf(this.TEXTURE_RENDERING)));

        this.FOG_RENDERING = Boolean.parseBoolean(
                prop.getProperty("fog_rendering", String.valueOf(this.FOG_RENDERING)));

        this.LIGHTING_RENDERING = Boolean.parseBoolean(
                prop.getProperty("lighting_rendering", String.valueOf(this.LIGHTING_RENDERING)));

        this.AMBIENT_LIGHT = prop.getProperty("ambient_light", this.AMBIENT_LIGHT);


        this.PATHFINDER_DEBUG_RENDERING = Boolean.parseBoolean(
                prop.getProperty("pathfinder_debug_rendering", String.valueOf(this.PATHFINDER_DEBUG_RENDERING)));
    }
}
