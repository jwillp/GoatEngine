package com.brm.GoatEngine.ScreenManager;

import com.badlogic.gdx.math.Vector2;
import com.brm.GoatEngine.AI.AISystem;
import com.brm.GoatEngine.ECS.ECSIniSerializer;
import com.brm.GoatEngine.ECS.core.ECSManager;
import com.brm.GoatEngine.ECS.core.EntityManager;
import com.brm.GoatEngine.GEConfig;
import com.brm.GoatEngine.GoatEngine;
import com.brm.GoatEngine.GraphicsRendering.RenderingSystem;
import com.brm.GoatEngine.Physics.PhysicsSystem;
import com.brm.GoatEngine.ScriptingEngine.ScriptSystem;
import com.brm.GoatEngine.UI.UIEngine;
import com.brm.GoatEngine.Utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;

public final class GameScreen{

    protected ECSManager ecsManager = new ECSManager();
    private String name;

    private PhysicsSystem physicsSystem;

    private GameScreenConfig config;

    private UIEngine uiEngine;
    private boolean initialized = false;

    public GameScreen(final String name){
        this.name = name;
        config = new GameScreenConfig();
        uiEngine = new UIEngine();
    }


    public void init(GameScreenManager screenManager) {
        Logger.info("> Game Screen: " + this.name + " initialisation ... ");

        // The Default Script System
        GoatEngine.eventManager.registerListener(this.ecsManager.getSystemManager());
        ecsManager.getSystemManager().addSystem(ScriptSystem.class, new ScriptSystem());

        physicsSystem = new PhysicsSystem();
        ecsManager.getSystemManager().addSystem(PhysicsSystem.class, physicsSystem);

        ecsManager.getSystemManager().addSystem(RenderingSystem.class, new RenderingSystem());
        ecsManager.getSystemManager().addSystem(AISystem.class, new AISystem());





        ecsManager.getSystemManager().initSystems();


        //READ data from Config file
        loadConfigFile();


        // Apply Level Configuration
        applyLevelConfig();

        initialized = true;
        Logger.info("> Game Screen: " + this.name + " initialised");


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

        try {
            config.loadConfig(GEConfig.ScreenManager.SCREEN_DIR + this.name);
            //Gravity
            Vector2 gravity = new Vector2(config.GRAVITY_X, config.GRAVITY_Y);
            this.ecsManager.getSystemManager().getSystem(PhysicsSystem.class).setGravity(gravity);

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
     * the Level Config file
     */
    private void applyLevelConfig(){
        // Load Level Config File
        ECSIniSerializer serializer = new ECSIniSerializer(this.config.LEVEL_CONFIG, this.getEntityManager());
        serializer.load();
        // TODO : Read from serializer (maybe faster)
        Logger.info("> Number of entity added: " + getEntityManager().getEntityCount());
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

    public boolean isInitialized() {
        return initialized;
    }


    // EXCEPTIONS //

    private class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super("Could not find game screen : " + name + " File not found : " + GEConfig.ScreenManager.SCREEN_DIR + name);
        }
    }
}
