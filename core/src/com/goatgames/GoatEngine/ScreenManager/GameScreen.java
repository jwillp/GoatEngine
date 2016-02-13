package com.goatgames.goatengine.screenmanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.goatgames.goatengine.GEConfig;
import com.goatgames.goatengine.ai.AISystem;
import com.goatgames.goatengine.ecs.ECSIniSerializer;
import com.goatgames.goatengine.ecs.core.ECSManager;
import com.goatgames.goatengine.ecs.core.EntityManager;
import com.goatgames.goatengine.GoatEngine;
import com.goatgames.goatengine.graphicsrendering.RenderingSystem;
import com.goatgames.goatengine.input.InputSystem;
import com.goatgames.goatengine.physics.PhysicsSystem;
import com.goatgames.goatengine.scriptingengine.lua.LuaEntityScriptSystem;
import com.goatgames.goatengine.ui.UIEngine;
import com.goatgames.goatengine.utils.GAssert;
import com.goatgames.goatengine.utils.Logger;

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
        config = new GameScreenConfig(GoatEngine.config.getString("screens.directory") + this.name);
        uiEngine = new UIEngine();
    }


    public void init(GameScreenManager screenManager) {
        Logger.info(String.format("> Game Screen: %s initialisation ... ", this.name));

        GoatEngine.eventManager.registerListener(this.ecsManager.getSystemManager());
        //ecsManager.getSystemManager().addSystem(GroovyScriptSystem.class, new GroovyScriptSystem());
        ecsManager.getSystemManager().addSystem(LuaEntityScriptSystem.class, new LuaEntityScriptSystem());


        physicsSystem = new PhysicsSystem();

        ecsManager.getSystemManager().addSystem(PhysicsSystem.class, physicsSystem);
        ecsManager.getSystemManager().addSystem(AISystem.class, new AISystem());
        ecsManager.getSystemManager().addSystem(InputSystem.class, new InputSystem());
        ecsManager.getSystemManager().addSystem(RenderingSystem.class, new RenderingSystem());

        ecsManager.getSystemManager().initSystems();


        //READ data from Config file
        loadConfigFile();


        // Apply Level Configuration
        applyLevelConfig();

        initialized = true;
        Logger.info(String.format("> Game Screen: %s initialised", this.name));


    }

    public void cleanUp() {
        Logger.info(String.format("Game Screen: %s cleaned up", this.name));
    }

    public void pause() {
        Logger.info(String.format("Game Screen: %s paused", this.name));
    }

    public void resume() {
        Logger.info(String.format("Game Screen: %s resumed", this.name));
    }


    public void handleInput(GameScreenManager screenManager){}

    public void update(GameScreenManager screenManager, float deltaTime){
        //ecsManager.getSystemManager().getSystem(GroovyScriptSystem.class).update(0);
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
            config.load();
            //Gravity
            final float GRAVITY_X = config.getFloat("physics.gravity.x");
            final float GRAVITY_Y = config.getFloat("physics.gravity.y");

            Vector2 gravity = new Vector2(GRAVITY_X, GRAVITY_Y);
            this.ecsManager.getSystemManager().getSystem(PhysicsSystem.class).setGravity(gravity);

        } catch (FileNotFoundException e) {
            GameScreenNotFoundException exception = new GameScreenNotFoundException(this.name);
            Logger.fatal(exception.getMessage());
            throw exception;
        }
    }


    /**
     * Applies the info (add entities/objects) read from
     * the Level Config file
     */
    private void applyLevelConfig(){
        // Load Level Config File
        final String LEVEL_CONFIG = config.getString("level");
        GAssert.that(Gdx.files.internal(LEVEL_CONFIG).exists(),
                String.format("The Level could not be loaded the file \"%s\" does not exist.", LEVEL_CONFIG));
        ECSIniSerializer serializer = new ECSIniSerializer(LEVEL_CONFIG, this.getEntityManager());
        serializer.load();
        Logger.info(String.format("> Number of entity added: %d", getEntityManager().getEntityCount()));
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

    public class GameScreenNotFoundException extends RuntimeException {
        public GameScreenNotFoundException(String name) {
            super(String.format("Could not find game screen : %s File not found : %s%s",
                    name, GoatEngine.config.getString("screens.directory"), name));
        }
    }
}
