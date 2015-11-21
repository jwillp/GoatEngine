package com.brm.GoatEngine.ScreenManager;

import com.badlogic.gdx.math.Vector2;
import com.brm.GoatEngine.ECS.common.TagsComponent;
import com.brm.GoatEngine.ECS.core.Entity;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.LevelEditor.LevelEditor;
import com.brm.GoatEngine.Physics.PhysicsSystem;
import com.brm.GoatEngine.Rendering.RenderingSystem;
import com.brm.GoatEngine.ScriptingEngine.ScriptComponent;
import com.brm.GoatEngine.ScriptingEngine.ScriptSystem;
import com.brm.GoatEngine.ECS.core.ECSManager;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.TmxSupport.MapConfig;
import com.brm.GoatEngine.TmxSupport.MapConfigObject;
import com.brm.GoatEngine.UI.UIEngine;
import com.brm.GoatEngine.Utils.GameConfig;
import com.brm.GoatEngine.Utils.Logger;
import com.brm.GoatEngine.Utils.OrderedProperties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public final class GameScreen{

    protected ECSManager ecsManager = new ECSManager();
    private String name;

    private MapConfig mapConfig;
    private PhysicsSystem physicsSystem;

    private GameScreenConfig config;

    private UIEngine uiEngine;

    public GameScreen(final String name){
        this.name = name;
        config = new GameScreenConfig();
        uiEngine = new UIEngine();
    }






    public void init(GameScreenManager screenManager) {
        Logger.info("Game Screen: " + this.name + " initialisation ... ");

        // The Default Script System
        GoatEngine.eventManager.registerListener(this.ecsManager.getSystemManager());
        ecsManager.getSystemManager().addSystem(ScriptSystem.class, new ScriptSystem());

        physicsSystem = new PhysicsSystem();
        ecsManager.getSystemManager().addSystem(PhysicsSystem.class, physicsSystem);


        ecsManager.getSystemManager().addSystem(RenderingSystem.class, new RenderingSystem());


        ecsManager.getSystemManager().initSystems();




        //READ data from Config file
        loadConfigFile();



        // Apply Map Configuration
        applyMapConfig();

        Logger.info("Game Screen: " + this.name + " initialised");
    }

    public void cleanUp() {
        Logger.info("Game Screen: " + this.name + "cleaned up");
    }

    public void pause() {
        Logger.info("Game Screen: " + this.name + "paused");
    }

    public void resume() {
        Logger.info("Game Screen: " + this.name + "resumed");
    }


    public void handleInput(GameScreenManager screenManager){}

    public void update(GameScreenManager screenManager, float deltaTime){
        //ecsManager.getSystemManager().getSystem(ScriptSystem.class).update(0);
        ecsManager.getSystemManager().update();
    }

    public void draw(GameScreenManager screenManager, float deltaTime){
        uiEngine.render(deltaTime);
        ecsManager.getSystemManager().draw();
    }


    /**
     * Reads the game screen config file
     */
    private void loadConfigFile(){
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(GEConfig.ScreenManager.SCREEN_DIR + this.name);
            OrderedProperties prop = new OrderedProperties();
            prop.load(inputStream);

            this.mapConfig = new MapConfig(GEConfig.ScreenManager.LEVEL_DIR + prop.getProperty("map_config_file")); // Required

            //Gravity
            Vector2 gravity = new Vector2();
            if(prop.getProperty("gravity_x") != null){  // TODO put default values as private static constants
                gravity.x = GameConfig.getFloatProperty(0.0f, prop.getProperty("gravity_x"));
            }
            if(prop.getProperty("gravity_y") != null){
                gravity.y = GameConfig.getFloatProperty(0.0f, prop.getProperty("gravity_y"));
            }
            this.ecsManager.getSystemManager().getSystem(PhysicsSystem.class).setGravity(gravity);



            // OTHER PARAMETERS
            this.config.PHYSICS_DEBUG_RENDERING = GameConfig.getBooleanProperty(
                    this.config.PHYSICS_DEBUG_RENDERING,
                    "physics_debug_rendering"
            );

            this.config.CAMERA_DEBUG_RENDERING = GameConfig.getBooleanProperty(
                    this.config.CAMERA_DEBUG_RENDERING,
                    "camera_debug_rendering"
            );

            this.config.TEXTURE_RENDERING = GameConfig.getBooleanProperty(
                    this.config.TEXTURE_RENDERING,
                    "texture_rendering"
            );

            this.config.FOG_RENDERING = GameConfig.getBooleanProperty(
                    this.config.FOG_RENDERING,
                    "fog_rendering"
            );

            this.config.LIGHTING_RENDERING = GameConfig.getBooleanProperty(
                    this.config.LIGHTING_RENDERING,
                    "lighting_rendering"
            );






        } catch (FileNotFoundException e) {
            GameScreenNotFoundException exception = new GameScreenNotFoundException(this.name);
            Logger.fatal(exception.getMessage());
            throw exception;
        } catch (IOException e) {
            Logger.fatal(e.getMessage());
            Logger.logStackTrace(e);
        }
    }


    /**
     * Applies the info (add entities/objects) read from
     * the Map Config file
     */
    private void applyMapConfig(){
        // Load Map Config File
        ArrayList<MapConfigObject> objects =  this.mapConfig.read();

        for(MapConfigObject obj : objects){
            Entity entity = ecsManager.getEntityManager().createEntity();
            if(obj.tag != null){
                ((TagsComponent)entity.getComponent(TagsComponent.ID)).addTag(obj.tag);
            }
            if(obj.script != null){
                ((ScriptComponent)entity.getComponent(ScriptComponent.ID)).addScript(
                        GEConfig.ScriptingEngine.SCRIPTS_DIR + obj.script
                );
            }
        }

        Logger.info("> Number of entity added: " + objects.size());
    }





    public EntityManager getEntityManager(){
        return this.ecsManager.getEntityManager();
    }

    public PhysicsSystem getPhysicsSystem() {
        return physicsSystem;
    }

    public GameScreenConfig getConfig() {
        return config;
    }

    public UIEngine getUiEngine() {
        return uiEngine;
    }

    public String getName() {
        return name;
    }


    // EXCEPTIONS //

    private class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super("Could not find game screen : " + name + " File not found : " + GEConfig.ScreenManager.SCREEN_DIR + name);
        }
    }
}
